package com.aafes.settlement.Invoice.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PaymentHeader {

	@JsonProperty("PK")
	private String				pK;

	@JsonProperty("OrderId")
	private String				orderId;

	@JsonProperty("PaymentGroupId")
	private String				paymentGroupId;

	@JsonProperty("PaymentMethod")
	private List<PaymentMethod>	paymentMethod = null;

	/**
	 * @return the pK
	 */
	public String getpK() {
		return pK;
	}

	/**
	 * @param pK
	 *            the pK to set
	 */
	public void setpK(String pK) {
		this.pK = pK;
	}

	/**
	 * @return the orderId
	 */
	public String getOrderId() {
		return orderId;
	}

	/**
	 * @param orderId
	 *            the orderId to set
	 */
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	/**
	 * @return the paymentGroupId
	 */
	public String getPaymentGroupId() {
		return paymentGroupId;
	}

	/**
	 * @param paymentGroupId
	 *            the paymentGroupId to set
	 */
	public void setPaymentGroupId(String paymentGroupId) {
		this.paymentGroupId = paymentGroupId;
	}

	/**
	 * @return the paymentMethod
	 */
	public List<PaymentMethod> getPaymentMethod() {
		return paymentMethod;
	}

	/**
	 * @param paymentMethod
	 *            the paymentMethod to set
	 */
	public void setPaymentMethod(List<PaymentMethod> paymentMethod) {
		this.paymentMethod = paymentMethod;
	}

}
