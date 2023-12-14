package com.aafes.settlement.core.response;

import java.util.List;

import com.aafes.settlement.exception.Errors;
import com.aafes.settlement.repository.entity.OrderInvoiceDetails;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class EISEResponseGeneric {

	@JsonProperty("responseCode")
	private String mResponseCode;

	@JsonProperty("responseMessage")
	private String mResponseMessage;

	@JsonProperty("errors")
	private List<Errors> mErrors;

	@JsonProperty("SettlementTender")
	private List<InvoiceResponse> settlementTender;

	@JsonProperty("RefundTender")
	private List<InvoiceResponse> refundTender;

	@JsonProperty("ExchangeTender")
	private List<InvoiceResponse> exchangeTender;

	@JsonProperty("CancelledTender")
	private List<InvoiceResponse> cancelledTender;

	@JsonProperty("AdjustmentTender")
	private List<InvoiceResponse> adjustmentTender;

	@JsonProperty("AuthReversalTender")
	private List<InvoiceResponse> authReversalTender;

	@JsonIgnore
	private OrderInvoiceDetails orderInvoiceDetails;

	/*
	 *
	 * 
	 * @JsonProperty("AuthReversalTender") private List<InvoiceResponse>
	 * mAuthReversalTender;
	 * 
	 * @JsonProperty("AdjustmentTender") private List<InvoiceResponse>
	 * mAdjustmentTender;
	 * 
	 */

	/**
	 * @return the exchangeTender
	 */
	public List<InvoiceResponse> getExchangeTender() {
		return exchangeTender;
	}

	/**
	 * @param pExchangeTender
	 *            the exchangeTender to set
	 */
	public void setExchangeTender(List<InvoiceResponse> pExchangeTender) {
		exchangeTender = pExchangeTender;
	}

	/**
	 * @return the responseCode
	 */
	public String getResponseCode() {
		return mResponseCode;
	}

	/**
	 * @param pResponseCode
	 *            the responseCode to set
	 */
	public void setResponseCode(String pResponseCode) {
		mResponseCode = pResponseCode;
	}

	/**
	 * @return the responseMessage
	 */
	public String getResponseMessage() {
		return mResponseMessage;
	}

	/**
	 * @param pResponseMessage
	 *            the responseMessage to set
	 */
	public void setResponseMessage(String pResponseMessage) {
		mResponseMessage = pResponseMessage;
	}

	/**
	 * @return the errors
	 */
	public List<Errors> getErrors() {
		return mErrors;
	}

	/**
	 * @param pErrors
	 *            the errors to set
	 */
	public void setErrors(List<Errors> pErrors) {
		mErrors = pErrors;
	}

	/**
	 * @return the settlementTender
	 */
	public List<InvoiceResponse> getSettlementTender() {
		return settlementTender;
	}

	/**
	 * @param pSettlementTender
	 *            the settlementTender to set
	 */
	public void setSettlementTender(List<InvoiceResponse> pSettlementTender) {
		settlementTender = pSettlementTender;
	}

	/**
	 * @return the refundTender
	 */
	public List<InvoiceResponse> getRefundTender() {
		return refundTender;
	}

	/**
	 * @param pRefundTender
	 *            the refundTender to set
	 */
	public void setRefundTender(List<InvoiceResponse> pRefundTender) {
		refundTender = pRefundTender;
	}

	/**
	 * @return the cancelledTender
	 */
	public List<InvoiceResponse> getCancelledTender() {
		return cancelledTender;
	}

	/**
	 * @param pCancelledTender
	 *            the cancelledTender to set
	 */
	public void setCancelledTender(List<InvoiceResponse> pCancelledTender) {
		cancelledTender = pCancelledTender;
	}

	/**
	 * @return the adjustmentTender
	 */
	public List<InvoiceResponse> getAdjustmentTender() {
		return adjustmentTender;
	}

	/**
	 * @param pAdjustmentTender
	 *            the adjustmentTender to set
	 */
	public void setAdjustmentTender(List<InvoiceResponse> pAdjustmentTender) {
		adjustmentTender = pAdjustmentTender;
	}

	/**
	 * @return the authReversalTender
	 */
	public List<InvoiceResponse> getAuthReversalTender() {
		return authReversalTender;
	}

	/**
	 * @param pAuthReversalTender
	 *            the authReversalTender to set
	 */
	public void
			setAuthReversalTender(List<InvoiceResponse> pAuthReversalTender) {
		authReversalTender = pAuthReversalTender;
	}

	/**
	 * @return the orderInvoiceDetails
	 */
	public OrderInvoiceDetails getOrderInvoiceDetails() {
		return orderInvoiceDetails;
	}

	/**
	 * @param orderInvoiceDetails
	 *            the orderInvoiceDetails to set
	 */
	public void
			setOrderInvoiceDetails(OrderInvoiceDetails orderInvoiceDetails) {
		this.orderInvoiceDetails = orderInvoiceDetails;
	}

}
