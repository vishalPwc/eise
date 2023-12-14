package com.aafes.settlement.service.implementation.exchange;
// ----------------------------------------------------------------------------

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aafes.settlement.configuration.MessagePropertyConfig;
import com.aafes.settlement.constant.Constants;
import com.aafes.settlement.constant.ErrorConstants;
import com.aafes.settlement.core.model.ProcessingContainer;
import com.aafes.settlement.core.model.exchange.UnevenExchangeRO;
import com.aafes.settlement.core.response.EISEResponseGeneric;
import com.aafes.settlement.exception.NodeNotFoundException;
import com.aafes.settlement.parser.UnevenExchangeRequestParser;
import com.aafes.settlement.repository.OrderInvoiceDetailsRepo;
import com.aafes.settlement.repository.RepoContainer;
import com.aafes.settlement.util.RepoUtils;
import com.aafes.settlement.util.Utils;
import com.aafes.settlement.validators.UnevenExchangeValidator;
import com.aafes.settlement.validators.ValidationErrors;

// ----------------------------------------------------------------------------
@Service
public class UnevenExchangeImpl
	implements
	Constants, ErrorConstants
{
	// ------------------------------------------------------------------------
	@Autowired
	private MessagePropertyConfig	messagePropConf;

	private static final Logger		LOGGER = LogManager
			.getLogger(ExchangeImpl.class);

	@Autowired
	private UnevenExchangeCalculation		unevenExchangeCalculation;

	@Autowired
	private OrderInvoiceDetailsRepo	orderInvoiceDetailsRepo;

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
		UnevenExchangeRO l_unevenExchangeRO = (UnevenExchangeRO) p_requestObject;
		UnevenExchangeValidator l_unevenExchangeValidator = new UnevenExchangeValidator();
		UnevenExchangeRequestParser l_unevenExchangeRequestParser = new UnevenExchangeRequestParser();

		LOGGER.info("---- Uneven Exchange Request initial validation started ----");

		ValidationErrors l_validationErrors = l_unevenExchangeValidator
				.validateRequest(l_unevenExchangeRO);

		LOGGER.info("---- Uneven Exchange Request initial validation completed ----");

		if (l_validationErrors.isValid) {

			// Repository change
			RepoContainer l_repoContainer = new RepoContainer();
			l_repoContainer.setTransactionType(EXCHANGE_INVOICE);
			l_repoContainer.setAuditUUID(p_uuid);

			LOGGER.info(
					"---- Uneven Exchange Request in-depth validation and parsing started ----"
			);

			ProcessingContainer l_unevenExchangeContainer = l_unevenExchangeRequestParser
					.exchangeParser(
							l_unevenExchangeRO, l_validationErrors,
							l_unevenExchangeValidator, l_repoContainer
					);

			LOGGER.info(
					"---- Uneven Exchange Request in-depth validation and parsing completed ----"
			);

			// check if Uneven Exchange invoice amount is less than current Settled
			if (
				l_unevenExchangeValidator.compareAmount(
						l_unevenExchangeContainer.getTotalPaymentSettled(),
						/*
						 * (l_exchangeContainer.getTotalPaymentSettled() -
						 * l_exchangeContainer.getTotalReturnedAmount()),
						 */
						l_unevenExchangeContainer.getOpenInvoiceLineTotal()
								.add(
										l_unevenExchangeContainer
												.getInvoiceChargeTotal()
								)
				)
			)
			{
				LOGGER.info(
						"---- Uneven Exchange Request payment tender calculation started ----"
				);

				unevenExchangeCalculation.calculatePaymentTenders(
						l_unevenExchangeContainer, l_repoContainer,
						p_eiseResponseGeneric
				);

				LOGGER.info(
						"---- Uneven Exchange Request payment tender calculation completed ----"
				);

				// Repository change
				RepoUtils.saveData(
						l_repoContainer, orderInvoiceDetailsRepo
				);

			} else {
				Utils.setFailureInEISEResponseObj(p_eiseResponseGeneric);

				l_validationErrors.errors.put(
						EXCHANGE_INVOICE_MORE_THAN_SETTLED,
						EXCHANGE_INVOICE
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
