package com.aafes.settlement.core.response;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

public class InvoiceResponse {

	@JsonInclude(Include.NON_NULL)
	@JsonProperty("InvoiceId")
	private String						invoiceId;

	@JsonInclude(Include.NON_NULL)
	@JsonProperty("InvoiceTypeId")
	private String						invoiceTypeId;

	@JsonInclude(Include.NON_NULL)
	@JsonProperty("PaymentTransactionId")
	private String						paymentTransactionId;

	// @JsonInclude(Include.NON_NULL)
	@JsonProperty("PaymentMethod")
	private List<PaymentMethodResponse>	paymentMethod = new ArrayList<
			PaymentMethodResponse>();

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

	/**
	 * @return the invoiceTypeId
	 */
	public String getInvoiceTypeId() {
		return invoiceTypeId;
	}

	/**
	 * @param pInvoiceTypeId
	 *            the invoiceTypeId to set
	 */
	public void setInvoiceTypeId(String pInvoiceTypeId) {
		invoiceTypeId = pInvoiceTypeId;
	}

	/**
	 * @return the paymentTransactionId
	 */
	public String getPaymentTransactionId() {
		return paymentTransactionId;
	}

	/**
	 * @param pPaymentTransactionId
	 *            the paymentTransactionId to set
	 */
	public void setPaymentTransactionId(String pPaymentTransactionId) {
		paymentTransactionId = pPaymentTransactionId;
	}

	/**
	 * @return the paymentMethod
	 */
	public List<PaymentMethodResponse> getPaymentMethod() {
		return paymentMethod;
	}

	/**
	 * @param pPaymentMethod
	 *            the paymentMethod to set
	 */
	public void setPaymentMethod(List<PaymentMethodResponse> pPaymentMethod) {
		getPaymentMethod().clear();
		paymentMethod = pPaymentMethod;
	}

}
