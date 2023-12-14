package com.aafes.settlement.service.implementation.reversal;

// ----------------------------------------------------------------------------
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aafes.settlement.configuration.MessagePropertyConfig;
import com.aafes.settlement.constant.Constants;
import com.aafes.settlement.constant.ErrorConstants;
import com.aafes.settlement.core.model.reversal.ReversalProcessingContainer;
import com.aafes.settlement.core.model.reversal.ReversalRO;
import com.aafes.settlement.core.response.EISEResponseGeneric;
import com.aafes.settlement.parser.ReversalRequestParser;
import com.aafes.settlement.repository.OrderInvoiceDetailsRepo;
import com.aafes.settlement.repository.RepoContainer;
import com.aafes.settlement.util.RepoUtils;
import com.aafes.settlement.util.Utils;
import com.aafes.settlement.validators.ReversalValidator;
import com.aafes.settlement.validators.ValidationErrors;
// ---------------------------------------------------------------------------

@Service
public class ReversalImpl
	implements
	Constants, ErrorConstants
{
	// ------------------------------------------------------------------------
	@Autowired
	private MessagePropertyConfig	messagePropConf;

	private static final Logger		LOGGER = LogManager
			.getLogger(ReversalImpl.class);

	@Autowired
	private ReversalCalculation		reversalCalculation;

	//@Autowired
	//private OrderInvoiceDetailsRepo	orderInvoiceDetailsRepo;

	// ------------------------------------------------------------------------
	/**
	 * 
	 * @param p_requestObject
	 * @param p_eiseResponseGeneric
	 * @param p_uuid
	 */
	public void doCalculation(
			Object p_requestObject, EISEResponseGeneric p_eiseResponseGeneric,
			String p_uuid
	)
	{
		ReversalRO l_reversalRO = (ReversalRO) p_requestObject;
		ReversalValidator l_reversalValidator = new ReversalValidator();
		ReversalRequestParser l_reversalRequestParser = new ReversalRequestParser();

		LOGGER.info(
				"---- AuthReversal Request initial validation started ----"
		);

		ValidationErrors l_validationErrors = l_reversalValidator
				.validateRequest(l_reversalRO);

		LOGGER.info(
				"---- AuthReversal Request initial validation completed ----"
		);

		if (l_validationErrors.isValid) {

			LOGGER.info(
					"---- AuthReversal Request in-depth validation and parsing started ----"
			);

			// Repository change
			RepoContainer l_repoContainer = new RepoContainer();

			l_repoContainer.setTransactionType(AUTH_REVERSAL_INVOICE);
			l_repoContainer.setAuditUUID(p_uuid);

			ReversalProcessingContainer l_reversalContainer = l_reversalRequestParser
					.reversalParser(
							l_reversalRO, l_validationErrors, l_repoContainer,
							l_reversalValidator
					);

			LOGGER.info(
					"---- AuthReversal Request in-depth validation and parsing completed ----"
			);

			// check if Cancelled invoice amount is less than current Settled
			if (
				l_reversalValidator.compareAmount(
						l_reversalContainer.getTotalPaymentAuth(),
						l_reversalContainer.getOpenInvoiceLineTotal()
				)
			)
			{
				LOGGER.info(
						"---- AuthReversal Request payment tender calculation started ----"
				);

				reversalCalculation.calculatePaymentTenders(
						l_reversalContainer, l_repoContainer,
						p_eiseResponseGeneric
				);
				LOGGER.info(
						"---- AuthReversal Request payment tender calculation completed ----"
				);

				// RepoUtils.saveReversalData(l_repoContainer,
				// orderInvoiceDetailsRepo);
				//RepoUtils.saveData(l_repoContainer, orderInvoiceDetailsRepo);

			} else {
				Utils.setFailureInEISEResponseObj(p_eiseResponseGeneric);

				l_validationErrors.errors.put(
						AUTHREVERSAL_INVOICE_MORE_THAN_AUTH,
						AUTH_REVERSAL_INVOICE
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