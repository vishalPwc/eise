package com.aafes.settlement.core.response;

import java.math.BigDecimal;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;

public class InvoiceLineResponse {

	@JsonProperty("InvoiceLineId")
	private String	   invoiceLineId;

	@JsonProperty("PaymentAmount")
	private BigDecimal paymentAmount;

	@JsonProperty("InvoiceId")
	private String	   invoiceId;

	public InvoiceLineResponse() {}

	public InvoiceLineResponse(
			String pInvoiceId, String pInvoiceLineId
	)
	{
		// TODO Auto-generated constructor stub
		invoiceId = pInvoiceId;
		invoiceLineId = pInvoiceLineId;
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

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		InvoiceLineResponse lILR = (InvoiceLineResponse) o;
		return Objects.equals(getInvoiceId(), lILR.getInvoiceId()) &&
				Objects.equals(getInvoiceLineId(), lILR.getInvoiceLineId());

	}

	@Override
	public int hashCode() {
		return Objects.hash(getInvoiceId(), getInvoiceLineId());
	}
}
