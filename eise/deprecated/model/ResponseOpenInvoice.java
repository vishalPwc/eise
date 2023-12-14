package com.aafes.settlement.Invoice.model;

import java.util.HashMap;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ResponseOpenInvoice {

	@JsonProperty("InvoiceId")
	private String						invoiceId;

	@JsonProperty("PaymentMethod")
	private List<ResponsePaymentMethod>	respPaymentMethod;

	@JsonIgnore
	private HashMap<String, String>		errorMap = new HashMap<String,
			String>();

	public String getInvoiceId() {
		return invoiceId;
	}

	public void setInvoiceId(String invoiceId) {
		this.invoiceId = invoiceId;
	}

	public List<ResponsePaymentMethod> getRespPaymentMethod() {
		return respPaymentMethod;
	}

	public void setRespPaymentMethod(
			List<ResponsePaymentMethod> respPaymentMethod
	)
	{
		this.respPaymentMethod = respPaymentMethod;
	}

	public HashMap<String, String> getErrorMap() {
		return errorMap;
	}

	public void setErrorMap(HashMap<String, String> errorMap) {
		this.errorMap = errorMap;
	}

}
