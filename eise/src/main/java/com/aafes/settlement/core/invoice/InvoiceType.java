package com.aafes.settlement.core.invoice;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class InvoiceType {

	@JsonProperty("InvoiceTypeId")
	private String invoiceTypeId;

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

}
