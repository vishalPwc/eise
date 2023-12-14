package com.aafes.settlement.parser;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
// -----------------------------------------------------------------------------------
import org.springframework.stereotype.Service;


import com.aafes.settlement.core.model.ProcessingContainer;
import com.aafes.settlement.core.model.exchange.UnevenExchangeRO;
import com.aafes.settlement.exception.NodeNotFoundException;
import com.aafes.settlement.repository.RepoContainer;
import com.aafes.settlement.validators.UnevenExchangeValidator;
import com.aafes.settlement.validators.ValidationErrors;

// -----------------------------------------------------------------------------
/**
 * This class distinguishes the Exchange request into an object of
 * ProcessingContainer and validates the same for mandatory nodes and
 * attributes.
 */
@Service
public class UnevenExchangeRequestParser
	extends
	ExchangeRequestParser
{
	// ------------------------------------------------------------------------
	private static final Logger LOGGER = LogManager
			.getLogger(ExchangeRequestParser.class);

	// ------------------------------------------------------------------------
	/**
	 * This method calls various distinguishing methods which parse and fill an
	 * object of ProcessingContainer.
	 * 
	 * @param p_unevenExchangeRequest
	 * @param p_validationErrors
	 * @return
	 * @throws NodeNotFoundException
	 *             when a method validating respective nodes/attributes does not
	 *             find a mandatory node/attribute or if it has an invalid
	 *             value.
	 */
	public ProcessingContainer exchangeParser(
			UnevenExchangeRO p_unevenExchangeRequest,
			ValidationErrors p_validationErrors,
			UnevenExchangeValidator p_exchangeValidator,
			RepoContainer p_repoContainer
	)
			throws NodeNotFoundException
	{
		// create instance of ProcessingContainer
		ProcessingContainer l_unevenExchangeBucket = new ProcessingContainer();

		LOGGER.debug("Distinguishing PaymentMethod node");
		// iterate over Payment methods and separate them to different buckets
		distinguishPaymentMethods(
				p_unevenExchangeRequest, l_unevenExchangeBucket, p_repoContainer,
				p_validationErrors, p_exchangeValidator
		);

		LOGGER.debug("Distinguishing OpenInvoice.InvoiceLine node");
		// iterate Invoice Lines and Separate them to different Buckets
		distinguishInvoiceLines(
				p_unevenExchangeRequest, l_unevenExchangeBucket, p_repoContainer,
				p_validationErrors, p_exchangeValidator
		);

		// get shipping charge in invoice detail
		// iterate Invoice Lines and Separate SHipping charges in each invoice
		// line
		distinguishShippingCharges(
				p_unevenExchangeRequest, l_unevenExchangeBucket, p_validationErrors,
				p_exchangeValidator
		);

		// iterate over original order lines to get MU , MR and Order Line
		// Totals
		distinguishOrderLineTotals(
				p_unevenExchangeRequest, l_unevenExchangeBucket, p_validationErrors,
				p_exchangeValidator
		);
		// iterate over closed invoices to get returned MU, MR and Order Line
		// totals
		distinguishReturnedLineTotals(
				p_unevenExchangeRequest, l_unevenExchangeBucket, p_validationErrors,
				p_exchangeValidator
		);
		l_unevenExchangeBucket.setUnevenRefundAmountTotal(p_unevenExchangeRequest.getOrderTotal());
		return l_unevenExchangeBucket;
	}

	// ------------------------------------------------------------------------
}
// ----------------------------------------------------------------------------
// END OF FILE
// ----------------------------------------------------------------------------