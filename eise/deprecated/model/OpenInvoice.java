package com.aafes.settlement.Invoice.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class OpenInvoice {

	@JsonProperty("comment")
	private String						  comment;
	@JsonProperty("InvoiceId")
	private String						  invoiceId;
	@JsonProperty("OrderId")
	private String						  orderId;
	@JsonProperty("ParentOrderId")
	private String						  parentOrderId;
	@JsonProperty("InvoiceTotal")
	private Double						  invoiceTotal;
	@JsonProperty("InvoiceType")
	private InvoiceType					  invoiceType;
	@JsonProperty("InvoiceLine")
	private List<InvoiceLine>			  invoiceLine			  = null;

	@JsonProperty("InvoiceLineChargeDetail")
	private List<InvoiceLineChargeDetail> invoiceLineChargeDetail = null;

	private float						  inMemoryMUProcessed;

	private float						  inMemoryMRProcessed;

	/**
	 * @return the comment
	 */
	public String getComment() {
		return comment;
	}

	/**
	 * @param comment
	 *            the comment to set
	 */
	public void setComment(String comment) {
		this.comment = comment;
	}

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
	public Double getInvoiceTotal() {
		return invoiceTotal;
	}

	/**
	 * @param invoiceTotal
	 *            the invoiceTotal to set
	 */
	public void setInvoiceTotal(Double invoiceTotal) {
		this.invoiceTotal = invoiceTotal;
	}

	/**
	 * @return the invoiceType
	 */
	public InvoiceType getInvoiceType() {
		return invoiceType;
	}

	/**
	 * @param invoiceType
	 *            the invoiceType to set
	 */
	public void setInvoiceType(InvoiceType invoiceType) {
		this.invoiceType = invoiceType;
	}

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
	 * @return the inMemoryMUProcessed
	 */
	public float getInMemoryMUProcessed() {
		return inMemoryMUProcessed;
	}

	/**
	 * @param inMemoryMUProcessed
	 *            the inMemoryMUProcessed to set
	 */
	public void setInMemoryMUProcessed(float inMemoryMUProcessed) {
		this.inMemoryMUProcessed = inMemoryMUProcessed;
	}

	/**
	 * @return the inMemoryMRProcessed
	 */
	public float getInMemoryMRProcessed() {
		return inMemoryMRProcessed;
	}

	/**
	 * @param inMemoryMRProcessed
	 *            the inMemoryMRProcessed to set
	 */
	public void setInMemoryMRProcessed(float inMemoryMRProcessed) {
		this.inMemoryMRProcessed = inMemoryMRProcessed;

	}

	/**
	 * @return the invoiceLineChargeDetail
	 */
	public List<InvoiceLineChargeDetail> getInvoiceLineChargeDetail() {
		return invoiceLineChargeDetail;
	}

	/**
	 * @param invoiceLineChargeDetail
	 *            the invoiceLineChargeDetail to set
	 */
	public void setInvoiceLineChargeDetail(
			List<InvoiceLineChargeDetail> invoiceLineChargeDetail
	)
	{
		this.invoiceLineChargeDetail = invoiceLineChargeDetail;
	}

}
