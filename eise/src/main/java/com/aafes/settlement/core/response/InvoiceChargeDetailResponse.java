package com.aafes.settlement.core.response;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

public class InvoiceChargeDetailResponse {

	@JsonProperty("ChargeDetailId")
	private String chargeDetailId;

	@JsonInclude(Include.NON_NULL)
	@JsonProperty("InvoiceLineId")
	private String invoiceLineId;

	@JsonProperty("PaymentAmount")
	private BigDecimal paymentAmount;

	@JsonProperty("InvoiceId")
	private String invoiceId;

	/**
	 * @return the chargeDetailId
	 */
	public String getChargeDetailId() {
		return chargeDetailId;
	}

	/**
	 * @param pChargeDetailId
	 *            the chargeDetailId to set
	 */
	public void setChargeDetailId(String pChargeDetailId) {
		chargeDetailId = pChargeDetailId;
	}

	/**
	 * @return the invoiceLineId
	 */
	public String getInvoiceLineId() {
		return invoiceLineId;
	}

	/**
	 * @param pInvoiceLineId
	 *            the invoiceLineId to set
	 */
	public void setInvoiceLineId(String pInvoiceLineId) {
		invoiceLineId = pInvoiceLineId;
	}

	/**
	 * @return the paymentAmount
	 */
	public BigDecimal getPaymentAmount() {
		return paymentAmount;
	}

	/**
	 * @param pPaymentAmount
	 *            the paymentAmount to set
	 */
	public void setPaymentAmount(BigDecimal pPaymentAmount) {
		paymentAmount = pPaymentAmount;
	}

	/**
	 * @return the invoiceId
	 */
	public String getInvoiceId() {
		return invoiceId;
	}

	/**
	 * @param pInvoiceId
	 *            the invoiceId to set
	 */
	public void setInvoiceId(String pInvoiceId) {
		invoiceId = pInvoiceId;
	}
}
