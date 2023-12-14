package com.aafes.settlement.Invoice.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AuthReversalTender {

	@JsonProperty("PaymentTransactionId")
	private String							paymentTransactionId;

	@JsonProperty("PaymentMethod")
	private List<AuthReversalPaymentMethod>	paymentMethod;

	/**
	 * @return the paymentTransactionId
	 */
	public String getPaymentTransactionId() {
		return paymentTransactionId;
	}

	/**
	 * @param paymentTransactionId
	 *            the paymentTransactionId to set
	 */
	public void setPaymentTransactionId(String paymentTransactionId) {
		this.paymentTransactionId = paymentTransactionId;
	}

	/**
	 * @return the paymentMethod
	 */
	public List<AuthReversalPaymentMethod> getPaymentMethod() {
		return paymentMethod;
	}

	/**
	 * @param paymentMethod
	 *            the paymentMethod to set
	 */
	public void setPaymentMethod(
			List<AuthReversalPaymentMethod> paymentMethod
	)
	{
		this.paymentMethod = paymentMethod;
	}

}
