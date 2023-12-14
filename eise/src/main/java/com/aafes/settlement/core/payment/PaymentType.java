package com.aafes.settlement.core.payment;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PaymentType {

	@JsonProperty("PaymentTypeId")
	private String paymentTypeId;

	/**
	 * 
	 * @return
	 */
	public String getPaymentTypeId() {
		return paymentTypeId;
	}

	/**
	 * 
	 * @param paymentTypeId
	 */
	public void setPaymentTypeId(String paymentTypeId) {
		this.paymentTypeId = paymentTypeId;
	}
}
