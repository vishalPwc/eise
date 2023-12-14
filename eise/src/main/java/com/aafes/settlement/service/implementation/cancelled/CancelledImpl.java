package com.aafes.settlement.service.implementation.cancelled;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aafes.settlement.configuration.MessagePropertyConfig;
import com.aafes.settlement.constant.Constants;
import com.aafes.settlement.constant.ErrorConstants;
import com.aafes.settlement.core.model.ProcessingContainer;
import com.aafes.settlement.core.model.cancelled.CancelledRO;
import com.aafes.settlement.core.response.EISEResponseGeneric;
import com.aafes.settlement.exception.NodeNotFoundException;
import com.aafes.settlement.parser.CancelledRequestParser;
import com.aafes.settlement.repository.OrderInvoiceDetailsRepo;
import com.aafes.settlement.repository.RepoContainer;
import com.aafes.settlement.util.CheckResponseDuplicates;
import com.aafes.settlement.util.RepoUtils;
import com.aafes.settlement.util.Utils;
import com.aafes.settlement.validators.CancelledValidator;
import com.aafes.settlement.validators.ValidationErrors;

@Service
public class CancelledImpl
	implements
	Constants, ErrorConstants
{
	// ------------------------------------------------------------------------
	@Autowired
	private MessagePropertyConfig	messagePropConf;

	private static final Logger		LOGGER = LogManager
			.getLogger(CancelledImpl.class);

	@Autowired
	private CancelledCalculation	cancelledCalculation;

	/* @Autowired
	private OrderInvoiceDetailsRepo	orderInvoiceDetailsRepo; */

	// ------------------------------------------------------------------------
	/**
	 * 
	 * @param p_requestObject
	 * @param p_eiseResponseGeneric
	 * @param p_uuid
	 * @throws NodeNotFoundException
	 */
	public void doCalculation(
			Object p_requestObject, EISEResponseGeneric p_eiseResponseGeneric,
			String p_uuid
	)
			throws NodeNotFoundException
	{

		CancelledRO l_cancelledRO = (CancelledRO) p_requestObject;
		CancelledValidator l_cancelledValidator = new CancelledValidator();
		CancelledRequestParser l_cancelledRequestParser = new CancelledRequestParser();

		LOGGER.info(
				"---- BOPIS Cancellation Request initial validation started ----"
		);

		ValidationErrors l_validationErrors = l_cancelledValidator
				.validateRequest(l_cancelledRO);

		LOGGER.info(
				"---- BOPIS Cancellation Request initial validation completed ----"
		);

		if (l_validationErrors.isValid) {

			LOGGER.info(
					"---- BOPIS Cancellation Request in-depth validation and parsing started ----"
			);

			// Repository change
			RepoContainer l_repoContainer = new RepoContainer();

			l_repoContainer.setTransactionType(CANCELLED_INVOICE);
			l_repoContainer.setAuditUUID(p_uuid);

			ProcessingContainer l_cancelledContainer = l_cancelledRequestParser
					.cancelledParser(
							l_cancelledRO, l_validationErrors,
							l_cancelledValidator, l_repoContainer
					);

			LOGGER.info(
					"---- BOPIS Cancellation Request in-depth validation and parsing started ----"
			);

			// check if Cancelled invoice amount is less than current Settled

			if (
				l_cancelledValidator.compareAmount(
						l_cancelledContainer.getTotalPaymentSettled()
						/*
						 * (l_cancelledContainer.getTotalPaymentSettled() -
						 * l_cancelledContainer .getTotalReturnedAmount())
						 */,
						l_cancelledContainer.getOpenInvoiceLineTotal()
								.add(
										l_cancelledContainer
												.getInvoiceChargeTotal()
								)
				)
			)
			{
				LOGGER.info(
						"---- BOPIS Cancellation Request payment tender calculation started ----"
				);

				cancelledCalculation.calculatePaymentTenders(
						l_cancelledContainer, l_repoContainer,
						p_eiseResponseGeneric
				);

				LOGGER.info(
						"---- Cancelled Request payment Check for duplicates in Response ----"
				);
				CheckResponseDuplicates.cancellationResponse(
						p_eiseResponseGeneric
				);
				
				LOGGER.info(
						"---- BOPIS Cancellation Request payment tender calculation completed ----"
				);

				// Repository change
				/* RepoUtils.saveData(
						l_repoContainer, orderInvoiceDetailsRepo
				); */
			} else {

				Utils.setFailureInEISEResponseObj(p_eiseResponseGeneric);

				l_validationErrors.errors.put(
						BOPIS_INVOICE_MORE_THAN_SETTLED, CANCELLED_INVOICE
				);
				p_eiseResponseGeneric.setErrors(
						Utils.getErrors(
								l_validationErrors.errors, messagePropConf
						)
				);
			}
		} else {
			p_eiseResponseGeneric.setErrors(
					Utils.getErrors(l_validationErrors.errors, messagePropConf)
			);
		}
	}
	// ------------------------------------------------------------------------
}
// ----------------------------------------------------------------------------
// END OF FILE
// ----------------------------------------------------------------------------