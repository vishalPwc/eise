/**
 * 
 */
package com.aafes.settlement.validators;

import java.math.BigDecimal;

/**
 * Class defines methods which will be used to validate incoming request based
 * on different request types
 */
public interface Validator {
	// -------------------------------------------------------------
	public ValidationErrors validateRequest(Object p_requestObject);

	public abstract boolean
			compareAmount(BigDecimal p_amount1, BigDecimal p_amount2);

	public boolean
			isNodeAvailable(Object p_nodeName, ValidationErrors p_errorObj);
	// --------------------------------------------------------------
}
