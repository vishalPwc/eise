package com.aafes.settlement.service.implementation.refund;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aafes.settlement.configuration.MessagePropertyConfig;
import com.aafes.settlement.constant.Constants;
import com.aafes.settlement.constant.ErrorConstants;
import com.aafes.settlement.core.model.ProcessingContainer;
import com.aafes.settlement.core.model.refund.RefundRO;
import com.aafes.settlement.core.response.EISEResponseGeneric;
import com.aafes.settlement.exception.NodeNotFoundException;
import com.aafes.settlement.parser.RefundRequestParser;
import com.aafes.settlement.repository.OrderInvoiceDetailsRepo;
import com.aafes.settlement.repository.RepoContainer;
import com.aafes.settlement.util.RepoUtils;
import com.aafes.settlement.util.Utils;
import com.aafes.settlement.validators.RefundValidator;
import com.aafes.settlement.validators.ValidationErrors;

@Service
public class RefundImpl
	implements
	Constants, ErrorConstants
{
	// ------------------------------------------------------------------------
	@Autowired
	private MessagePropertyConfig	messagePropConf;

	private static final Logger		LOGGER = LogManager
			.getLogger(RefundImpl.class);

	@Autowired
	private RefundCalculation		refundCalculation;

	//@Autowired
	//private OrderInvoiceDetailsRepo	orderInvoiceDetailsRepo;
	// ------------------------------------------------------------------------

	/**
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

		RefundRO l_refundRO = (RefundRO) p_requestObject;
		RefundValidator l_refundValidation = new RefundValidator();
		RefundRequestParser l_refundRequestParser = new RefundRequestParser();

		LOGGER.info("---- Refund Request initial validation started ----");

		ValidationErrors l_validationErrors = l_refundValidation
				.validateRequest(l_refundRO);

		LOGGER.info("---- Refund Request initial validation completed ----");

		if (l_validationErrors.isValid) {

			LOGGER.info(
					"---- Refund Request in-depth validation and parsing started ----"
			);

			// Repository change
			RepoContainer l_repoContainer = new RepoContainer();

			l_repoContainer.setTransactionType(REFUND);
			l_repoContainer.setAuditUUID(p_uuid);

			ProcessingContainer l_refundContainer = l_refundRequestParser
					.refundParser(
							l_refundRO, l_validationErrors, l_refundValidation,
							l_repoContainer
					);
			LOGGER.info(
					"---- Refund Request in-depth validation and parsing completed ----"
			);

			// check if Refund invoice amount is less than current Settled
			if (
				l_refundValidation.compareAmount(
						l_refundContainer.getTotalPaymentSettled(),
						/*
						 * (l_refundContainer.getTotalPaymentSettled() -
						 * l_refundContainer.getTotalReturnedAmount()),
						 */
						l_refundContainer.getOpenInvoiceLineTotal()
								.add(l_refundContainer.getInvoiceChargeTotal())
				)
			)
			{
				LOGGER.info(
						"---- Refund Request payment tender calculation started ----"
				);

				refundCalculation.calculatePaymentTenders(
						l_refundContainer, l_repoContainer,
						p_eiseResponseGeneric
				);

				LOGGER.info(
						"---- Refund Request payment tender calculation completed ----"
				);

				// Repository change
				/*RepoUtils.saveData(
						l_repoContainer, orderInvoiceDetailsRepo
				);*/
			} else {

				Utils.setFailureInEISEResponseObj(p_eiseResponseGeneric);

				l_validationErrors.errors.put(
						REFUND_INVOICE_MORE_THAN_SETTLED, RETURN_INVOICE
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
}
// ----------------------------------------------------------------------------
// END OF FILE
// ----------------------------------------------------------------------------