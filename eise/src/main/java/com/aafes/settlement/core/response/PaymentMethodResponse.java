package com.aafes.settlement.core.response;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.aafes.settlement.core.payment.PaymentTransaction;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

public class PaymentMethodResponse {

	// add auth attribute with null ignore
	@JsonInclude(Include.NON_NULL)
	@JsonProperty("PaymentMethodId")
	private String paymentMethodId;

	@JsonInclude(Include.NON_EMPTY)
	@JsonProperty("InvoiceLine")
	private List<InvoiceLineResponse> invoiceLine = new ArrayList<>();

	@JsonInclude(Include.NON_EMPTY)
	@JsonProperty("InvoiceLineCharges")
	private List<InvoiceChargeDetailResponse> invoiceLineCharge = new ArrayList<>();

	@JsonInclude(Include.NON_NULL)
	@JsonProperty("PaymentAmount")
	private BigDecimal paymentAmount;

	@JsonInclude(Include.NON_EMPTY)
	@JsonProperty("InvoiceChargeDetail")
	private List<InvoiceChargeDetailResponse> invoiceCharge = new ArrayList<>();

	@JsonInclude(Include.NON_EMPTY)
	@JsonProperty("PaymentTransaction")
	private List<PaymentTransaction> paymentTransaction = new ArrayList<>();

	/**
	 * @return the invoiceCharge
	 */
	public List<InvoiceChargeDetailResponse> getInvoiceCharge() {
		return invoiceCharge;
	}

	/**
	 * @param pInvoiceCharge
	 *            the invoiceCharge to set
	 */
	public void
			setInvoiceCharge(List<InvoiceChargeDetailResponse> pInvoiceCharge) {
		invoiceCharge = pInvoiceCharge;
	}

	/**
	 * @return the paymentMethodId
	 */
	public String getPaymentMethodId() {
		return paymentMethodId;
	}

	/**
	 * @param pPaymentMethodId
	 *            the paymentMethodId to set
	 */
	public void setPaymentMethodId(String pPaymentMethodId) {
		paymentMethodId = pPaymentMethodId;
	}

	/**
	 * @return the invoiceLine
	 */
	public List<InvoiceLineResponse> getInvoiceLine() {
		return invoiceLine;
	}

	/**
	 * @param pInvoiceLine
	 *            the invoiceLine to set
	 */
	public void setInvoiceLine(List<InvoiceLineResponse> pInvoiceLine) {
		invoiceLine = pInvoiceLine;
	}

	/**
	 * @return the invoiceLineCharge
	 */
	public List<InvoiceChargeDetailResponse> getInvoiceLineCharge() {
		return invoiceLineCharge;
	}

	/**
	 * @param pInvoiceLineCharge
	 *            the invoiceLineCharge to set
	 */
	public void setInvoiceLineCharge(
			List<InvoiceChargeDetailResponse> pInvoiceLineCharge) {
		invoiceLineCharge = pInvoiceLineCharge;
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
	 * @return the paymentTransaction
	 */
	public List<PaymentTransaction> getPaymentTransaction() {
		return paymentTransaction;
	}

	/**
	 * @param pPaymentTransaction
	 *            the paymentTransaction to set
	 */
	public void setPaymentTransaction(
			List<PaymentTransaction> pPaymentTransaction) {
		paymentTransaction = pPaymentTransaction;
	}

}
