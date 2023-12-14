package com.aafes.settlement.Invoice.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ReturnRequest {

	@JsonProperty("ParentOrder")
	private ParentOrder		  parentOrder;

	@JsonProperty("OpenInvoice")
	private List<OpenInvoice> openInvoice;

	/*
	 * @JsonProperty("Extended") private Extended extended;
	 */

	@JsonProperty("OrderTotal")
	private Double			  orderTotal;

	@JsonProperty("OrderId")
	private String			  orderId;

	@JsonProperty("MinFulfillmentStatusId")
	private String			  minFulfillmentStatusId;

	@JsonProperty("MaxFulfillmentStatusId")
	private String			  maxFulfillmentStatusId;

	@JsonProperty("FulfillmentStatus")
	private String			  fulfillmentStatus;

	/**
	 * @return the parentOrder
	 */
	public ParentOrder getParentOrder() {
		return parentOrder;
	}

	/**
	 * @param parentOrder
	 *            the parentOrder to set
	 */
	public void setParentOrder(ParentOrder parentOrder) {
		this.parentOrder = parentOrder;
	}

	/**
	 * @return the openInvoice
	 */
	public List<OpenInvoice> getOpenInvoice() {
		return openInvoice;
	}

	/**
	 * @param openInvoice
	 *            the openInvoice to set
	 */
	public void setOpenInvoice(List<OpenInvoice> openInvoice) {
		this.openInvoice = openInvoice;
	}

	/**
	 * @return the orderTotal
	 */
	public Double getOrderTotal() {
		return orderTotal;
	}

	/**
	 * @param orderTotal
	 *            the orderTotal to set
	 */
	public void setOrderTotal(Double orderTotal) {
		this.orderTotal = orderTotal;
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
	 * @return the minFulfillmentStatusId
	 */
	public String getMinFulfillmentStatusId() {
		return minFulfillmentStatusId;
	}

	/**
	 * @param minFulfillmentStatusId
	 *            the minFulfillmentStatusId to set
	 */
	public void setMinFulfillmentStatusId(String minFulfillmentStatusId) {
		this.minFulfillmentStatusId = minFulfillmentStatusId;
	}

	/**
	 * @return the maxFulfillmentStatusId
	 */
	public String getMaxFulfillmentStatusId() {
		return maxFulfillmentStatusId;
	}

	/**
	 * @param maxFulfillmentStatusId
	 *            the maxFulfillmentStatusId to set
	 */
	public void setMaxFulfillmentStatusId(String maxFulfillmentStatusId) {
		this.maxFulfillmentStatusId = maxFulfillmentStatusId;
	}

	/**
	 * @return the fulfillmentStatus
	 */
	public String getFulfillmentStatus() {
		return fulfillmentStatus;
	}

	/**
	 * @param fulfillmentStatus
	 *            the fulfillmentStatus to set
	 */
	public void setFulfillmentStatus(String fulfillmentStatus) {
		this.fulfillmentStatus = fulfillmentStatus;
	}

}
