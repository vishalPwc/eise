package com.aafes.settlement.Invoice.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class InvoiceLine {

	@JsonProperty("InvoiceLineId")
	private String				invoiceLineId;
	@JsonProperty("ItemId")
	private String				itemId;
	@JsonProperty("OrderLineId")
	private String				orderLineId;
	@JsonProperty("Quantity")
	private Double				quantity;
	@JsonProperty("OrderedQuantity")
	private Double				orderedQuantity;
	@JsonProperty("InvoiceLineTotal")
	private float				invoiceLineTotal;

	private float				interimOrderLineTotal;

	@JsonProperty("Extended")
	private InvoiceLineExtended	extended;

	/**
	 * @return the invoiceLineId
	 */
	public String getInvoiceLineId() {
		return invoiceLineId;
	}

	/**
	 * @param invoiceLineId
	 *            the invoiceLineId to set
	 */
	public void setInvoiceLineId(String invoiceLineId) {
		this.invoiceLineId = invoiceLineId;
	}

	/**
	 * @return the itemId
	 */
	public String getItemId() {
		return itemId;
	}

	/**
	 * @param itemId
	 *            the itemId to set
	 */
	public void setItemId(String itemId) {
		this.itemId = itemId;
	}

	/**
	 * @return the orderLineId
	 */
	public String getOrderLineId() {
		return orderLineId;
	}

	/**
	 * @param orderLineId
	 *            the orderLineId to set
	 */
	public void setOrderLineId(String orderLineId) {
		this.orderLineId = orderLineId;
	}

	/**
	 * @return the quantity
	 */
	public Double getQuantity() {
		return quantity;
	}

	/**
	 * @param quantity
	 *            the quantity to set
	 */
	public void setQuantity(Double quantity) {
		this.quantity = quantity;
	}

	/**
	 * @return the orderedQuantity
	 */
	public Double getOrderedQuantity() {
		return orderedQuantity;
	}

	/**
	 * @param orderedQuantity
	 *            the orderedQuantity to set
	 */
	public void setOrderedQuantity(Double orderedQuantity) {
		this.orderedQuantity = orderedQuantity;
	}

	/**
	 * @return the invoiceLineTotal
	 */
	public float getInvoiceLineTotal() {
		return invoiceLineTotal;
	}

	/**
	 * @param invoiceLineTotal
	 *            the invoiceLineTotal to set
	 */
	public void setInvoiceLineTotal(float invoiceLineTotal) {
		this.invoiceLineTotal = Math.abs(invoiceLineTotal);
		setInterimOrderLineTotal(Math.abs(invoiceLineTotal));
	}

	/**
	 * @return the extended
	 */
	public InvoiceLineExtended getExtended() {
		return extended;
	}

	/**
	 * @param extended
	 *            the extended to set
	 */
	public void setExtended(InvoiceLineExtended extended) {
		this.extended = extended;
	}

	/**
	 * @return the interimOrderLineTotal
	 */
	public float getInterimOrderLineTotal() {
		return interimOrderLineTotal;
	}

	/**
	 * @param interimOrderLineTotal
	 *            the interimOrderLineTotal to set
	 */
	public void setInterimOrderLineTotal(float interimOrderLineTotal) {
		this.interimOrderLineTotal = interimOrderLineTotal;
	}

}
