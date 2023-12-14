package com.aafes.settlement.core.invoice;

import java.math.BigDecimal;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ClosedInvoice {
	// -----------------------------------------------------------------------------------
	@JsonProperty("InvoiceId")
	private String invoiceId;

	@JsonProperty("OrderId")
	private String orderId;

	@JsonProperty("ParentOrderId")
	private String parentOrderId;

	@JsonProperty("InvoiceTotal")
	private BigDecimal invoiceTotal;

	@JsonProperty("InvoiceType")
	private InvoiceType invoiceType;

	@JsonProperty("InvoiceChargeDetail")
	private List<InvoiceChargeDetail> invoiceChargeDetail = null;

	@JsonProperty("InvoiceLine")
	private List<InvoiceLine> invoiceLine = null;

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
	 * @return the orderId
	 */
	public String getOrderId() {
		return orderId;
	}

	/**
	 * @param orderId
	 *            the orderId to set
	 */
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	/**
	 * @return the parentOrderId
	 */
	public String getParentOrderId() {
		return parentOrderId;
	}

	/**
	 * @param parentOrderId
	 *            the parentOrderId to set
	 */
	public void setParentOrderId(String parentOrderId) {
		this.parentOrderId = parentOrderId;
	}

	/**
	 * @return the invoiceTotal
	 */
	public BigDecimal getInvoiceTotal() {
		return invoiceTotal;
	}

	/**
	 * @param invoiceTotal
	 *            the invoiceTotal to set
	 */
	public void setInvoiceTotal(BigDecimal invoiceTotal) {
		this.invoiceTotal = invoiceTotal;
	}

	/**
	 * @return the invoiceType
	 */
	/*
	 * public InvoiceType getInvoiceType() { return invoiceType; }
	 * 
	 *//**
		 * @param invoiceType
		 *            the invoiceType to set
		 *//*
			 * public void setInvoiceType(InvoiceType invoiceType) {
			 * this.invoiceType = invoiceType; }
			 */

	/**
	 * @return the invoiceLine
	 */
	public List<InvoiceLine> getInvoiceLine() {
		return invoiceLine;
	}

	/**
	 * @param invoiceLine
	 *            the invoiceLine to set
	 */
	public void setInvoiceLine(List<InvoiceLine> invoiceLine) {
		this.invoiceLine = invoiceLine;
	}

	/**
	 * @return the invoiceType
	 */
	public InvoiceType getInvoiceType() {
		return invoiceType;
	}

	/**
	 * @param pInvoiceType
	 *            the invoiceType to set
	 */
	public void setInvoiceType(InvoiceType pInvoiceType) {
		invoiceType = pInvoiceType;
	}

	/**
	 * @return the invoiceChargeDetail
	 */
	public List<InvoiceChargeDetail> getInvoiceChargeDetail() {
		return invoiceChargeDetail;
	}

	/**
	 * @param pInvoiceChargeDetail
	 *            the invoiceChargeDetail to set
	 */
	public void setInvoiceChargeDetail(
			List<InvoiceChargeDetail> pInvoiceChargeDetail) {
		invoiceChargeDetail = pInvoiceChargeDetail;
	}

}
