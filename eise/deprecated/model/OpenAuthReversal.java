package com.aafes.settlement.Invoice.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class OpenAuthReversal {

	@JsonProperty("comment")
	private String comment;

	@JsonProperty("Amount")
	private Float  amount;

	@JsonProperty("PaymentTransactionId")
	private String paymentTransactionId;

	private float  interimAmount;

	/**
	 * @return the comment
	 */
	public String getComment() {
		return comment;
	}

	/**
	 * @param comment
	 *            the comment to set
	 */
	public void setComment(String comment) {
		this.comment = comment;
	}

	public Float getAmount() {
		return amount;
	}

	public void setAmount(Float amount) {
		this.amount = amount;
		setInterimAmount(amount);
	}

	public String getPaymentTransactionId() {
		return paymentTransactionId;
	}

	public void setPaymentTransactionId(String paymentTransactionId) {
		this.paymentTransactionId = paymentTransactionId;
	}

	/**
	 * @return the interimAmount
	 */
	public float getInterimAmount() {
		return interimAmount;
	}

	/**
	 * @param interimAmount
	 *            the interimAmount to set
	 */
	public void setInterimAmount(float interimAmount) {
		this.interimAmount = interimAmount;
	}

}
