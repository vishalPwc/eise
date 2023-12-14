package com.aafes.settlement.core.payment;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class FollowOnParentTransaction {
	// ------------------------------------------------------------------------
	@JsonProperty("ParentPaymentMethodId")
	private String parentPaymentMethodId;

	@JsonProperty("Amount")
	private BigDecimal amount;
	// ------------------------------------------------------------------------
	/**
	 * @return the parentPaymentMethodId
	 */
	public String getParentPaymentMethodId() {
		return parentPaymentMethodId;
	}

	/**
	 * @param p_parentPaymentMethodId
	 *            the parentPaymentMethodId to set
	 */
	public void setParentPaymentMethodId(String p_parentPaymentMethodId) {
		parentPaymentMethodId = p_parentPaymentMethodId;
	}

	/**
	 * @return the amount
	 */
	public BigDecimal getAmount() {
		return amount;
	}

	/**
	 * @param p_amount
	 *            the amount to set
	 */
	public void setAmount(BigDecimal p_amount) {
		amount = p_amount;
	}
	// ------------------------------------------------------------------------
}
// ---------------------------------------------------------------------------
// END OF FILE
// ---------------------------------------------------------------------------