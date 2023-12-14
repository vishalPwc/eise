package com.aafes.settlement.Invoice.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class OrderSettlementRequest {

	@JsonProperty("OrderId")
	private String				orderId;
	// @JsonProperty("OrderTotal")
	// private Double orderTotal;
	// @JsonProperty("FulfillmentStatus")
	// private String fulfillmentStatus;
	// @JsonProperty("MinFulfillmentStatusId")
	// private String minFulfillmentStatusId;
	// @JsonProperty("MaxFulfillmentStatusId")
	// private String maxFulfillmentStatusId;
	// /*
	// * @JsonProperty("Extended") private Extended extended;
	// */
	// @JsonProperty("OrderChargeDetail")
	// private List<OrderChargeDetail> orderChargeDetail = null;

	@JsonProperty("OpenAuthReversal")
	private OpenAuthReversal	openAuthReversal;

	@JsonProperty("OrderLine")
	private List<OrderLine>		orderLine	  = null;

	@JsonProperty("OpenInvoice")
	private List<OpenInvoice>	openInvoice	  = null;
	@JsonProperty("PaymentHeader")
	private List<PaymentHeader>	paymentHeader = null;

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

	// /**
	// * @return the orderTotal
	// */
	// public Double getOrderTotal() {
	// return orderTotal;
	// }
	//
	// /**
	// * @param orderTotal
	// * the orderTotal to set
	// */
	// public void setOrderTotal(Double orderTotal) {
	// this.orderTotal = orderTotal;
	// }
	//
	// /**
	// * @return the fulfillmentStatus
	// */
	// public String getFulfillmentStatus() {
	// return fulfillmentStatus;
	// }
	//
	// /**
	// * @param fulfillmentStatus
	// * the fulfillmentStatus to set
	// */
	// public void setFulfillmentStatus(String fulfillmentStatus) {
	// this.fulfillmentStatus = fulfillmentStatus;
	// }
	//
	// /**
	// * @return the minFulfillmentStatusId
	// */
	// public String getMinFulfillmentStatusId() {
	// return minFulfillmentStatusId;
	// }
	//
	// /**
	// * @param minFulfillmentStatusId
	// * the minFulfillmentStatusId to set
	// */
	// public void setMinFulfillmentStatusId(String minFulfillmentStatusId) {
	// this.minFulfillmentStatusId = minFulfillmentStatusId;
	// }
	//
	// /**
	// * @return the maxFulfillmentStatusId
	// */
	// public String getMaxFulfillmentStatusId() {
	// return maxFulfillmentStatusId;
	// }
	//
	// /**
	// * @param maxFulfillmentStatusId
	// * the maxFulfillmentStatusId to set
	// */
	// public void setMaxFulfillmentStatusId(String maxFulfillmentStatusId) {
	// this.maxFulfillmentStatusId = maxFulfillmentStatusId;
	// }
	//
	// /**
	// * @return the orderChargeDetail
	// */
	// public List<OrderChargeDetail> getOrderChargeDetail() {
	// return orderChargeDetail;
	// }
	//
	// /**
	// * @param orderChargeDetail
	// * the orderChargeDetail to set
	// */
	// public void setOrderChargeDetail(
	// List<OrderChargeDetail> orderChargeDetail
	// )
	// {
	// this.orderChargeDetail = orderChargeDetail;
	// }

	public OpenAuthReversal getOpenAuthReversal() {
		return openAuthReversal;
	}

	public void setOpenAuthReversal(OpenAuthReversal openAuthReversal) {
		this.openAuthReversal = openAuthReversal;
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
