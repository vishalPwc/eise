package com.aafes.settlement.Invoice.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CancelRequest {

	@JsonProperty("CancelledItems")
	private List<OrderLine>		orderLine;

	@JsonProperty("PaymentHeader")
	private List<PaymentHeader>	paymentHeader;

	@JsonProperty("OpenAuthReversal")
	private OpenAuthReversal	openAuthReversal;

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

	/**
	 * @return the openAuthReversal
	 */
	public OpenAuthReversal getOpenAuthReversal() {
		return openAuthReversal;
	}

	/**
	 * @param openAuthReversal
	 *            the openAuthReversal to set
	 */
	public void setOpenAuthReversal(OpenAuthReversal openAuthReversal) {
		this.openAuthReversal = openAuthReversal;
	}

	/**
	 * @return the openAuthReversal
	 */

}
