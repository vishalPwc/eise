package com.aafes.settlement.Invoice.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AuthReversalPaymentMethod {

	@JsonProperty("PaymentMethodId")
	private String paymentMethodId;

	@JsonProperty("PaymentAmount")
	private float  paymentAmount;

	/**
	 * @return the paymentMethodId
	 */
	public String getPaymentMethodId() {
		return paymentMethodId;
	}

	/**
	 * @param paymentMethodId
	 *            the paymentMethodId to set
	 */
	public void setPaymentMethodId(String paymentMethodId) {
		this.paymentMethodId = paymentMethodId;
	}

	/**
	 * @return the paymentAmount
	 */
	public float getPaymentAmount() {
		return paymentAmount;
	}

	/**
	 * @param paymentAmount
	 *            the paymentAmount to set
	 */
	public void setPaymentAmount(float paymentAmount) {
		this.paymentAmount = paymentAmount;
	}

}
