package com.aafes.settlement.core.request;

import com.aafes.settlement.core.model.adjustment.AdjustmentRO;
import com.aafes.settlement.core.model.cancelled.CancelledRO;
import com.aafes.settlement.core.model.exchange.ExchangeRO;
import com.aafes.settlement.core.model.exchange.UnevenExchangeRO;
import com.aafes.settlement.core.model.refund.RefundRO;
import com.aafes.settlement.core.model.reversal.ReversalRO;
import com.aafes.settlement.core.model.settlement.SettlementRO;
import com.fasterxml.jackson.annotation.JsonProperty;

public class EISERequestGeneric {

	@JsonProperty("SettlementRequest")
	private SettlementRO settlementRequest;

	@JsonProperty("ReturnRequest")
	private RefundRO	 refundRequest;

	@JsonProperty("ExchangeRequest")
	private ExchangeRO	 exchangeRequest;

	@JsonProperty("UnevenExchangeRequest")
	private UnevenExchangeRO	 unevenExchangeRequest;

	@JsonProperty("CancelledRequest")
	private CancelledRO	 cancelledRequest;

	@JsonProperty("AdjustmentRequest")
	private AdjustmentRO adjustmentRequest;

	@JsonProperty("AuthReversalRequest")
	private ReversalRO	 authReversalRequest;

	private String		 UUID;

	/**
	 * @return the adjustmentRequest
	 */
	public AdjustmentRO getAdjustmentRequest() {
		return adjustmentRequest;
	}

	/**
	 * @param pAdjustmentRequest
	 *            the adjustmentRequest to set
	 */
	public void setAdjustmentRequest(AdjustmentRO pAdjustmentRequest) {
		adjustmentRequest = pAdjustmentRequest;
	}

	/**
	 * @return the exchangeRequest
	 */
	public ExchangeRO getExchangeRequest() {
		return exchangeRequest;
	}

	/**
	 * @param pExchangeRequest
	 *            the exchangeRequest to set
	 */
	public void setExchangeRequest(ExchangeRO pExchangeRequest) {
		exchangeRequest = pExchangeRequest;
	}

	/**
	 * @return the refundRequest
	 */
	public RefundRO getRefundRequest() {
		return refundRequest;
	}

	/**
	 * @param pRefundRequest
	 *            the refundRequest to set
	 */
	public void setRefundRequest(RefundRO pRefundRequest) {
		refundRequest = pRefundRequest;
	}

	/**
	 * @return the settlementRequest
	 */
	public SettlementRO getSettlementRequest() {
		return settlementRequest;
	}

	/**
	 * @param pSettlementRequest
	 *            the settlementRequest to set
	 */
	public void setSettlementRequest(SettlementRO pSettlementRequest) {
		this.settlementRequest = pSettlementRequest;
	}

	/**
	 * @return the cancelledRequest
	 */
	public CancelledRO getCancelledRequest() {
		return cancelledRequest;
	}

	/**
	 * @param pCancelledRequest
	 *            the cancelledRequest to set
	 */
	public void setCancelledRequest(CancelledRO pCancelledRequest) {
		cancelledRequest = pCancelledRequest;
	}

	/**
	 * @return the authReversalRequest
	 */
	public ReversalRO getAuthReversalRequest() {
		return authReversalRequest;
	}

	/**
	 * 
	 * @return unevenExchangeRequest
	 */
	public UnevenExchangeRO getUnevenExchangeRequest() {
		return unevenExchangeRequest;
	}

	/**
	 * 
	 * @param unevenExchangeRequest
	 */
	public void setUnevenExchangeRequest(UnevenExchangeRO unevenExchangeRequest) {
		this.unevenExchangeRequest = unevenExchangeRequest;
	}

	/**
	 * @param pAuthReversalRequest
	 *            the authReversalRequest to set
	 */
	public void setAuthReversalRequest(ReversalRO pAuthReversalRequest) {
		authReversalRequest = pAuthReversalRequest;
	}

	public String getUUID() {
		return UUID;
	}

	public void setUUID(String uUID) {
		UUID = uUID;
	}

}
