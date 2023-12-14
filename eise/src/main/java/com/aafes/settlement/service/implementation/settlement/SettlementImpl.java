package com.aafes.settlement.service.implementation.settlement;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aafes.settlement.configuration.MessagePropertyConfig;
import com.aafes.settlement.constant.Constants;
import com.aafes.settlement.constant.ErrorConstants;
import com.aafes.settlement.core.model.ProcessingContainer;
import com.aafes.settlement.core.model.settlement.SettlementRO;
import com.aafes.settlement.core.response.EISEResponseGeneric;
import com.aafes.settlement.exception.NodeNotFoundException;
import com.aafes.settlement.parser.SettlementRequestParser;
import com.aafes.settlement.repository.OrderInvoiceDetailsRepo;
import com.aafes.settlement.repository.RepoContainer;
import com.aafes.settlement.util.CheckResponseDuplicates;
import com.aafes.settlement.util.RepoUtils;
import com.aafes.settlement.util.Utils;
import com.aafes.settlement.validators.SettlementValidator;
import com.aafes.settlement.validators.ValidationErrors;

@Service
public class SettlementImpl
	implements
	Constants, ErrorConstants
{
	// ------------------------------------------------------------------------
	@Autowired
	private MessagePropertyConfig	messagePropConf;

	private static final Logger		LOGGER = LogManager
			.getLogger(SettlementImpl.class);

	@Autowired
	private SettlementCalculation	settlementCalculation;

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

		SettlementRO l_settlementRO = (SettlementRO) p_requestObject;
		SettlementValidator l_settlementValidator = new SettlementValidator();
		SettlementRequestParser l_settlementRequestParser = new SettlementRequestParser();

		LOGGER.info("---- Settlement Request initial validation started ----");

		ValidationErrors l_validationErrors = l_settlementValidator
				.validateRequest(l_settlementRO);

		LOGGER.info(
				"---- Settlement Request initial validation completed ----"
		);

		if (l_validationErrors.isValid) {

			LOGGER.info(
					"---- Settlement Request in-depth validation and parsing started ----"
			);
			// Repository change
			RepoContainer l_repoContainer = new RepoContainer();

			l_repoContainer.setTransactionType(SETTLEMENT);
			l_repoContainer.setAuditUUID(p_uuid);

			ProcessingContainer l_settlementContainer = l_settlementRequestParser
					.settlementParser(
							l_settlementRO, l_validationErrors,
							l_settlementValidator, l_repoContainer
					);

			LOGGER.info(
					"---- Settlement Request in-depth validation and parsing completed ----"
			);

			// check if settlement invoice amount is less than current auth
			// available

			if (
				l_settlementValidator.compareAmount(
						l_settlementContainer.getTotalPaymentAuth(),
						l_settlementContainer.getOpenInvoiceLineTotal()
								.add(
										l_settlementContainer
												.getInvoiceChargeTotal()
								)
				)
			)
			{
				LOGGER.info(
						"---- Settlement Request payment tender calculation started ----"
				);
				settlementCalculation.calculatePaymentTenders(
						l_settlementContainer, l_repoContainer,
						p_eiseResponseGeneric
				);
				LOGGER.info(
						"---- Settlement Request payment tender calculation completed ----"
				);

				LOGGER.info(
						"---- Settlement Request payment Check for duplicates in Response ----"
				);
				CheckResponseDuplicates.settlementResponse(
						p_eiseResponseGeneric
				);

				// Repository change
				/*RepoUtils.saveData(
						l_repoContainer, orderInvoiceDetailsRepo
				);*/

			} else {

				Utils.setFailureInEISEResponseObj(p_eiseResponseGeneric);

				l_validationErrors.errors.put(
						SETTLEMENT_INVOICE_MORE_THAN_AUTH, SETTLEMENT
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
// -----------------------------------------------------------------------------
// END OF FILE
// -----------------------------------------------------------------------------