package com.aafes.settlement.core.invoice;

import java.math.BigDecimal;
import java.util.Comparator;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class OpenAuthReversal implements Comparator<OpenAuthReversal> {

	@JsonProperty("Amount")
	private BigDecimal amount;

	@JsonProperty("PaymentTransactionId")
	private String paymentTransId;

	/**
	 * @return the amount
	 */
	public BigDecimal getAmount() {
		return amount;
	}

	/**
	 * @param pAmount
	 *            the amount to set
	 */
	public void setAmount(BigDecimal pAmount) {
		amount = pAmount;
	}

	/**
	 * @return the paymentTransId
	 */
	public String getPaymentTransId() {
		return paymentTransId;
	}

	/**
	 * @param pPaymentTransId
	 *            the paymentTransId to set
	 */
	public void setPaymentTransId(String pPaymentTransId) {
		paymentTransId = pPaymentTransId;
	}

	@Override
	public int compare(OpenAuthReversal pO1, OpenAuthReversal pO2) {

		return pO1.getAmount().compareTo((pO2.getAmount()));
		// if (pO1.getAmount() < pO1.getAmount())
		// return -1;
		// else
		// return 1;
	}
}
