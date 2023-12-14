package com.aafes.settlement.Invoice.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PaymentTransaction {

	@JsonProperty("PaymentTransactionId")
	private String paymentTransactionId;
	
	@JsonProperty("FollowOnParentTransaction") 
	private List<FollowOnParentTransaction> followOnParentTransaction;

	public String getPaymentTransactionId() {
		return paymentTransactionId;
	}

	public void setPaymentTransactionId(String paymentTransactionId) {
		this.paymentTransactionId = paymentTransactionId;
	}

	/**
	 * @return the followOnParentTransaction
	 */
	public List<FollowOnParentTransaction> getFollowOnParentTransaction() {
		return followOnParentTransaction;
	}

	/**
	 * @param p_followOnParentTransaction the followOnParentTransaction to set
	 */
	public void setFollowOnParentTransaction(
			List<FollowOnParentTransaction> p_followOnParentTransaction
	)
	{
		followOnParentTransaction = p_followOnParentTransaction;
	}
}
