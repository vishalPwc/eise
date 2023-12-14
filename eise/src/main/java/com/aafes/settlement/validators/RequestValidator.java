package com.aafes.settlement.validators;

import java.math.BigDecimal;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.aafes.settlement.core.request.EISERequestGeneric;
import com.aafes.settlement.util.Utils;

// ----------------------------------------------------------------------------
/**
 * This class validates the Request object received from MAO.
 */
public class RequestValidator implements Validator {
	// ------------------------------------------------------------------------
	private static final Logger LOGGER = LogManager
			.getLogger(RequestValidator.class);
	// ------------------------------------------------------------------------
	/**
	 * This method checks if it contains any of the following request nodes:
	 * <ul>
	 * <li>SettlementRequest
	 * <li>ReturnRequest
	 * <li>AuthReversalRequest
	 * <li>ExchangeRequest
	 * <li>AdjustmentRequest
	 * <li>CancelledRequest (BOPIS)
	 * </ul>
	 * 
	 * @param p_requestObject
	 */
	@Override
	public ValidationErrors validateRequest(Object p_requestObject) {
		ValidationErrors l_errorObj = new ValidationErrors();
		EISERequestGeneric l_eiseRequest = (EISERequestGeneric) p_requestObject;

		LOGGER.info("Sanity check of the received request");

		if (Utils.isNull(l_eiseRequest.getSettlementRequest())
				&& Utils.isNull(l_eiseRequest.getRefundRequest())
				&& Utils.isNull(l_eiseRequest.getExchangeRequest())
				&& Utils.isNull(l_eiseRequest.getCancelledRequest())
				&& Utils.isNull(l_eiseRequest.getAdjustmentRequest())
				&& Utils.isNull(l_eiseRequest.getUnevenExchangeRequest())
				&& Utils.isNull(l_eiseRequest.getAuthReversalRequest())) {
			l_errorObj.isValid = false;
		}
		return l_errorObj;
	}

	// ------------------------------------------------------------------------
	@Override
	public boolean
			isNodeAvailable(Object p_nodeName, ValidationErrors p_errorObj) {
		return false;
	}
	// ------------------------------------------------------------------------
	@Override
	public boolean compareAmount(BigDecimal p_pAmount1, BigDecimal p_pAmount2) {
		return false;
	}
	// ------------------------------------------------------------------------
}
// ----------------------------------------------------------------------------
// END OF FILE
// ----------------------------------------------------------------------------
