package com.aafes.settlement.core.payment;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class TransactionType {
	
	// ------------------------------------------------------------------------
	@JsonProperty("PaymentTransactionTypeId")
	private String paymentTransactionTypeId;
	// ------------------------------------------------------------------------
	/**
	 * @return the paymentTransactionTypeId
	 */
	public String getPaymentTransactionTypeId() {
		return paymentTransactionTypeId;
	}

	/**
	 * @param p_paymentTransactionTypeId the paymentTransactionTypeId to set
	 */
	public void setPaymentTransactionTypeId(String p_paymentTransactionTypeId) {
		paymentTransactionTypeId = p_paymentTransactionTypeId;
	}
	// ------------------------------------------------------------------------
}
// ----------------------------------------------------------------------------
// END OF FILE
// ----------------------------------------------------------------------------