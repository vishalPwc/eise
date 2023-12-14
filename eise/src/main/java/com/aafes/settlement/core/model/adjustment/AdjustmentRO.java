package com.aafes.settlement.core.model.adjustment;

import java.math.BigDecimal;
import java.util.List;

import com.aafes.settlement.core.invoice.ClosedInvoice;
import com.aafes.settlement.core.order.OrderChargeDetail;
import com.aafes.settlement.core.order.OrderLine;
import com.aafes.settlement.core.payment.PaymentHeader;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AdjustmentRO {

	@JsonProperty("OrderId")
	private String						orderId;

	@JsonProperty("PaymentHeader")
	private List<PaymentHeader>			paymentHeader;

	@JsonProperty("OrderLine")
	private List<OrderLine>				orderLine;

	@JsonProperty("OrderChargeDetail")
	private List<OrderChargeDetail>		OrderChargeDetail;

	/*
	 * @JsonProperty("Extended") private Extended extended;
	 */
	@JsonProperty("OpenInvoice")
	private List<AdjustmentOpenInvoice>	openInvoice;

	@JsonProperty("OrderTotal")
	private BigDecimal					orderTotal;

	@JsonProperty("FulfillmentStatus")
	private String						fulfillmentStatus;

	@JsonProperty("MinFulfillmentStatusId")
	private String						minFulfillmentStatusId;

	@JsonProperty("MaxFulfillmentStatusId")
	private String						maxFulfillmentStatusId;

	@JsonProperty("ClosedInvoice")
	private List<ClosedInvoice>			closedInvoices;

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
	 * @return the orderChargeDetail
	 */
	public List<OrderChargeDetail> getOrderChargeDetail() {
		return OrderChargeDetail;
	}

	/**
	 * @param orderChargeDetail
	 *            the orderChargeDetail to set
	 */
	public void
			setOrderChargeDetail(List<OrderChargeDetail> orderChargeDetail)
	{
		OrderChargeDetail = orderChargeDetail;
	}

	/**
	 * @return the openInvoice
	 */
	public List<AdjustmentOpenInvoice> getOpenInvoice() {
		return openInvoice;
	}

	/**
	 * @param openInvoice
	 *            the openInvoice to set
	 */
	public void setOpenInvoice(List<AdjustmentOpenInvoice> openInvoice) {
		this.openInvoice = openInvoice;
	}

	/**
	 * @return the orderTotal
	 */
	public BigDecimal getOrderTotal() {
		return orderTotal;
	}

	/**
	 * @param orderTotal
	 *            the orderTotal to set
	 */
	public void setOrderTotal(BigDecimal orderTotal) {
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
	 * @return the closedInvoices
	 */
	public List<ClosedInvoice> getClosedInvoices() {
		return closedInvoices;
	}

	/**
	 * @param closedInvoices
	 *            the closedInvoices to set
	 */
	public void setClosedInvoices(List<ClosedInvoice> closedInvoices) {
		this.closedInvoices = closedInvoices;
	}

}
