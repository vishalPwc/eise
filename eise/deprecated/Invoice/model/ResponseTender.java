package com.aafes.settlement.Invoice.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ResponseTender {

	@JsonInclude(Include.NON_NULL)
	@JsonProperty("InvoiceId")
	private String						invoiceId;

	@JsonInclude(Include.NON_NULL)
	@JsonProperty("InvoiceTypeId")
	private String						invoiceTypeId;

	@JsonInclude(Include.NON_NULL)
	@JsonProperty("PaymentTransactionId")
	private String						paymentTransactionId;

	@JsonInclude(Include.NON_NULL)
	@JsonProperty("PaymentMethod")
	private List<ResponsePaymentMethod>	paymentMethod;

	/**
	 * @return the invoiceId
	 */
	public String getInvoiceId() {
		return invoiceId;
	}

	/**
	 * @param invoiceId
	 *            the invoiceId to set
	 */
	public void setInvoiceId(String invoiceId) {
		this.invoiceId = invoiceId;
	}

	/**
	 * @return the invoiceTypeId
	 */
	public String getInvoiceTypeId() {
		return invoiceTypeId;
	}

	/**
	 * @param invoiceTypeId
	 *            the invoiceTypeId to set
	 */
	public void setInvoiceTypeId(String invoiceTypeId) {
		this.invoiceTypeId = invoiceTypeId;
	}

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
	public List<ResponsePaymentMethod> getPaymentMethod() {
		return paymentMethod;
	}

	/**
	 * @param paymentMethod
	 *            the paymentMethod to set
	 */
	public void setPaymentMethod(List<ResponsePaymentMethod> paymentMethod) {
		this.paymentMethod = paymentMethod;
	}

}
