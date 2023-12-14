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
import com.aafes.settlement.core.model.exchange.ExchangeRO;
import com.aafes.settlement.core.response.EISEResponseGeneric;
import com.aafes.settlement.exception.NodeNotFoundException;
import com.aafes.settlement.parser.ExchangeRequestParser;
import com.aafes.settlement.repository.OrderInvoiceDetailsRepo;
import com.aafes.settlement.repository.RepoContainer;
import com.aafes.settlement.util.RepoUtils;
import com.aafes.settlement.util.Utils;
import com.aafes.settlement.validators.ExchangeValidator;
import com.aafes.settlement.validators.ValidationErrors;

// ----------------------------------------------------------------------------
@Service
public class ExchangeImpl
	implements
	Constants, ErrorConstants
{
	// ------------------------------------------------------------------------
	@Autowired
	private MessagePropertyConfig	messagePropConf;

	private static final Logger		LOGGER = LogManager
			.getLogger(ExchangeImpl.class);

	@Autowired
	private ExchangeCalculation		exchangeCalculation;

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
		ExchangeRO l_exchangeRO = (ExchangeRO) p_requestObject;
		ExchangeValidator l_exchangeValidator = new ExchangeValidator();
		ExchangeRequestParser l_exchangeRequestParser = new ExchangeRequestParser();

		LOGGER.info("---- Exchange Request initial validation started ----");

		ValidationErrors l_validationErrors = l_exchangeValidator
				.validateRequest(l_exchangeRO);

		LOGGER.info("---- Exchange Request initial validation completed ----");

		if (l_validationErrors.isValid) {

			// Repository change
			RepoContainer l_repoContainer = new RepoContainer();
			l_repoContainer.setTransactionType(EXCHANGE_INVOICE);
			l_repoContainer.setAuditUUID(p_uuid);

			LOGGER.info(
					"---- Exchange Request in-depth validation and parsing started ----"
			);

			ProcessingContainer l_exchangeContainer = l_exchangeRequestParser
					.exchangeParser(
							l_exchangeRO, l_validationErrors,
							l_exchangeValidator, l_repoContainer
					);

			LOGGER.info(
					"---- Exchange Request in-depth validation and parsing completed ----"
			);

			// check if Exchange invoice amount is less than current Settled
			if (
				l_exchangeValidator.compareAmount(
						l_exchangeContainer.getTotalPaymentSettled(),
						/*
						 * (l_exchangeContainer.getTotalPaymentSettled() -
						 * l_exchangeContainer.getTotalReturnedAmount()),
						 */
						l_exchangeContainer.getOpenInvoiceLineTotal()
								.add(
										l_exchangeContainer
												.getInvoiceChargeTotal()
								)
				)
			)
			{
				LOGGER.info(
						"---- Exchange Request payment tender calculation started ----"
				);

				exchangeCalculation.calculatePaymentTenders(
						l_exchangeContainer, l_repoContainer,
						p_eiseResponseGeneric
				);

				LOGGER.info(
						"---- Exchange Request payment tender calculation completed ----"
				);

				// Repository change
				/*RepoUtils.saveData(
						l_repoContainer, orderInvoiceDetailsRepo
				); */

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
