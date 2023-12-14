package com.aafes.settlement.Invoice.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ParentOrder {

	@JsonProperty("OrderId")
	private String					orderId;

	@JsonProperty("OrderTotal")
	private double					orderTotal;

	@JsonProperty("FulfillmentStatus")
	private String					fulfillmentStatus;

	@JsonProperty("MinFulfillmentStatusId")
	private String					minFulfillmentStatusId;

	@JsonProperty("MaxFulfillmentStatusId")
	private String					maxFulfillmentStatusId;

	@JsonProperty("OrderChargeDetail")
	private List<OrderChargeDetail>	OrderChargeDetail;

	@JsonProperty("OrderLine")
	private List<OrderLine>			orderLine;

	@JsonProperty("PaymentHeader")
	private List<PaymentHeader>		paymentHeader;

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
	 * @return the orderTotal
	 */
	public double getOrderTotal() {
		return orderTotal;
	}

	/**
	 * @param orderTotal
	 *            the orderTotal to set
	 */
	public void setOrderTotal(double orderTotal) {
		this.orderTotal = orderTotal;
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
	 * @return the orderChargeDetail
	 */
	public List<OrderChargeDetail> getOrderChargeDetail() {
		return OrderChargeDetail;
	}

	/**
	 * @param orderChargeDetail
	 *            the orderChargeDetail to set
	 */
	public void setOrderChargeDetail(
			List<OrderChargeDetail> orderChargeDetail
	)
	{
		OrderChargeDetail = orderChargeDetail;
	}

	/**
	 * @return the orderLine
	 */
	public List<OrderLine> getOrderLine() {
		return orderLine;
	}

	/**
	 * @param orderLine
	 *            the orderLine to set
	 */
	public void setOrderLine(List<OrderLine> orderLine) {
		this.orderLine = orderLine;
	}

	/**
	 * @return the paymentHeader
	 */
	public List<PaymentHeader> getPaymentHeader() {
		return paymentHeader;
	}

	/**
	 * @param paymentHeader
	 *            the paymentHeader to set
	 */
	public void setPaymentHeader(List<PaymentHeader> paymentHeader) {
		this.paymentHeader = paymentHeader;
	}

}
