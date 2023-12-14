/**
 * 
 */
package com.aafes.settlement.core.response.filters;

/**
 * @author vishalt
 *
 */
public class PaymentTransactionFilter {

	@Override
	public boolean equals(Object obj) {
		if (obj == null || !(obj instanceof Double)) {
			return false;
		}

		return ((Double) obj <= 0.0);
	}
}
