package com.aafes.settlement.Invoice.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ResponsePaymentMethod {

	// add auth attribute with null ignore
	@JsonInclude(Include.NON_NULL)
	@JsonProperty("PaymentMethodId")
	private String							paymentMethodId;

	@JsonInclude(Include.NON_NULL)
	@JsonProperty("InvoiceLine")
	private List<ResponseInvoiceLine>		respInvoiceLine;

	@JsonInclude(Include.NON_NULL)
	@JsonProperty("InvoiceLineCharge")
	private List<ResponseInvoiceLineCharge>	respInvoiceLineCharge;

	@JsonInclude(Include.NON_NULL)
	@JsonProperty("PaymentAmount")
	private float							paymentAmount;

	public String getPaymentMethodId() {
		return paymentMethodId;
	}

	public void setPaymentMethodId(String paymentMethodId) {
		this.paymentMethodId = paymentMethodId;
	}

	public List<ResponseInvoiceLine> getRespInvoiceLine() {
		return respInvoiceLine;
	}

	public void setRespInvoiceLine(List<ResponseInvoiceLine> respInvoiceLine) {
		this.respInvoiceLine = respInvoiceLine;
	}

	public List<ResponseInvoiceLineCharge> getRespInvoiceLineCharge() {
		return respInvoiceLineCharge;
	}

	public void setRespInvoiceLineCharge(
			List<ResponseInvoiceLineCharge> respInvoiceLineCharge
	)
	{
		this.respInvoiceLineCharge = respInvoiceLineCharge;
	}

	/**
	 * @return the paymentAmount
	 */
	public float getPaymentAmount() {
		return paymentAmount;
	}

	/**
	 * @param paymentAmount
	 *            the paymentAmount to set
	 */
	public void setPaymentAmount(float paymentAmount) {
		this.paymentAmount = paymentAmount;
	}

}
