package com.aafes.settlement.validators;


// ----------------------------------------------------------------------------
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.aafes.settlement.core.model.exchange.UnevenExchangeRO;


// ----------------------------------------------------------------------------

/**
 * Class implements methods to be used for Exchange Request validation
 */
public class UnevenExchangeValidator
	extends ExchangeValidator
{
	// ------------------------------------------------------------------------
	/** information logger **/
	private static final Logger LOGGER = LogManager
			.getLogger(SettlementValidator.class);


	@Override
	public boolean
			isNodeAvailable(Object p_requestNode, ValidationErrors p_errorObj)
	{
		UnevenExchangeRO l_exchangeRequest = (UnevenExchangeRO) p_requestNode;

		LOGGER.info(
				"Uneven Exchange Request Valdation - isNodeAvailable() :: "
						+ "Check Super Implementation of Exchange"
		);
		super.isNodeAvailable(p_requestNode, p_errorObj);

		LOGGER.info(
				"Uneven Exchange Request Valdation - isNodeAvailable() :: "
						+ "Check OrderTotal"
		);

		ValidationUtils.checkIfNull(
				l_exchangeRequest.getOrderTotal(),
				ORDER_TOTAL, EXCHANGE_MANDATORY_MISSING, UNEVEN_EXCHANGE_INVOICE,
				p_errorObj
		);

		return p_errorObj.isValid;
	}
	// ------------------------------------------------------------------------
}
// END OF FILE