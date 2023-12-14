package com.aafes.settlement.service.implementation.adjustment;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aafes.settlement.configuration.MessagePropertyConfig;
import com.aafes.settlement.constant.Constants;
import com.aafes.settlement.constant.ErrorConstants;
import com.aafes.settlement.core.model.adjustment.AdjustmentProcessingContainer;
import com.aafes.settlement.core.model.adjustment.AdjustmentRO;
import com.aafes.settlement.core.response.EISEResponseGeneric;
import com.aafes.settlement.exception.NodeNotFoundException;
import com.aafes.settlement.parser.AdjustmentRequestParser;
import com.aafes.settlement.repository.OrderInvoiceDetailsRepo;
import com.aafes.settlement.repository.RepoContainer;
import com.aafes.settlement.util.RepoUtils;
import com.aafes.settlement.util.Utils;
import com.aafes.settlement.validators.AdjustmentValidator;
import com.aafes.settlement.validators.ValidationErrors;

// ----------------------------------------------------------------------------
@Service
public class AdjustmentImpl
	implements Constants, ErrorConstants
{
	// ------------------------------------------------------------------------
	@Autowired
	private MessagePropertyConfig	messagePropConf;

	private static final Logger		LOGGER = LogManager
			.getLogger(AdjustmentImpl.class);

	@Autowired
	private AdjustmentCalculation	adjustmentCalculation;

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
		AdjustmentRO l_adjustmentRO = (AdjustmentRO) p_requestObject;
		AdjustmentValidator l_adjustmentValidator = new AdjustmentValidator();
		AdjustmentRequestParser l_adjustmentRequestParser = new AdjustmentRequestParser();

		LOGGER.info("---- Adjustment Request initial validation started ----");

		ValidationErrors l_validationErrors = l_adjustmentValidator
				.validateRequest(l_adjustmentRO);

		LOGGER.info(
				"---- Adjustment Request initial validation completed ----"
		);

		if (l_validationErrors.isValid) {

			LOGGER.info(
					"--- Adjustment Request in-depth validation and parsing started ---"
			);

			// Repository change
			RepoContainer l_repoContainer = new RepoContainer();

			l_repoContainer.setTransactionType(ADJUSTMENT_INVOICE);
			l_repoContainer.setAuditUUID(p_uuid);

			AdjustmentProcessingContainer l_adjustmentContainer = l_adjustmentRequestParser
					.adjustmentParser(
							l_adjustmentRO, l_validationErrors,
							l_adjustmentValidator, l_repoContainer
					);

			LOGGER.info(
					"--- Adjustment Request in-depth validation and parsing completed ---"
			);

			// check if Cancelled invoice amount is less than current Settled

			if (
				l_adjustmentValidator.compareAmount(
						l_adjustmentContainer.getTotalPaymentSettled(),
						/*
						 * (l_adjustmentContainer.getTotalPaymentSettled() -
						 * l_adjustmentContainer .getTotalReturnedAmount()),
						 */
						l_adjustmentContainer.getOpenInvoiceLineTotal().add(
								l_adjustmentContainer.getInvoiceChargeTotal()
						)
				)
			)
			{
				LOGGER.info(
						"---- Adjustment Request payment tender calculation started ----"
				);

				adjustmentCalculation.calculatePaymentTenders(
						l_adjustmentContainer, l_repoContainer,
						p_eiseResponseGeneric
				);

				LOGGER.info(
						"---- Adjustment Request payment tender calculation completed ----"
				);

				// Repository change
				/* RepoUtils.saveData(
						l_repoContainer, orderInvoiceDetailsRepo
				); */

			} else {

				Utils.setFailureInEISEResponseObj(p_eiseResponseGeneric);

				l_validationErrors.errors.put(
						ADJUSTMENT_INVOICE_MORE_THAN_SETTLED,
						ADJUSTMENT_INVOICE
				);
				p_eiseResponseGeneric.setErrors(
						Utils
								.getErrors(
										l_validationErrors.errors,
										messagePropConf
								)
				);
			}
		} else {
			p_eiseResponseGeneric.setErrors(
					Utils
							.getErrors(
									l_validationErrors.errors, messagePropConf
							)
			);
		}
	}
	// ------------------------------------------------------------------------
}
// ----------------------------------------------------------------------------
// END OF FILE
// ----------------------------------------------------------------------------