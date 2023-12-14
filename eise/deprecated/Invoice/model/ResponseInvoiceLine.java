package com.aafes.settlement.Invoice.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ResponseInvoiceLine {

	@JsonProperty("InvoiceLineId")
	private String invoiceLineId;

	@JsonProperty("PaymentAmount")
	private float  paymentAmount;

	public String getInvoiceLineId() {
		return invoiceLineId;
	}

	public void setInvoiceLineId(String invoiceLineId) {
		this.invoiceLineId = invoiceLineId;
	}

	public float getPaymentAmount() {
		return paymentAmount;
	}

	public void setPaymentAmount(float paymentAmount) {
		this.paymentAmount = paymentAmount;
	}
}
