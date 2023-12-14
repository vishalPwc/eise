package com.aafes.settlement.Invoice.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class EiseRequest {

	@JsonProperty("SettlementRequest")
	private SettlementRequest settlementRequest;

	@JsonProperty("ReturnRequest")
	private ReturnRequest	  returnRequest;

	@JsonProperty("AuthReversalRequest")
	private SettlementRequest authReversalRequest;

	@JsonProperty("AdjustmentRequest")
	private SettlementRequest adjustmentRequest;

	@JsonProperty("ExchangeRequest")
	private ReturnRequest	  exchangeRequest;

	/**
	 * @return the settlementRequest
	 */
	public SettlementRequest getSettlementRequest() {
		return settlementRequest;
	}

	/**
	 * @param settlementRequest
	 *            the settlementRequest to set
	 */
	public void setSettlementRequest(SettlementRequest settlementRequest) {
		this.settlementRequest = settlementRequest;
	}

	/**
	 * @return the returnRequest
	 */
	public ReturnRequest getReturnRequest() {
		return returnRequest;
	}

	/**
	 * @param returnRequest
	 *            the returnRequest to set
	 */
	public void setReturnRequest(ReturnRequest returnRequest) {
		this.returnRequest = returnRequest;
	}

	/**
	 * @return the authReversalRequest
	 */
	public SettlementRequest getAuthReversalRequest() {
		return authReversalRequest;
	}

	/**
	 * @param authReversalRequest
	 *            the authReversalRequest to set
	 */
	public void setAuthReversalRequest(SettlementRequest authReversalRequest) {
		this.authReversalRequest = authReversalRequest;
	}

	/**
	 * @return the adjustmentRequest
	 */
	public SettlementRequest getAdjustmentRequest() {
		return adjustmentRequest;
	}

	/**
	 * @param adjustmentRequest
	 *            the adjustmentRequest to set
	 */
	public void setAdjustmentRequest(SettlementRequest adjustmentRequest) {
		this.adjustmentRequest = adjustmentRequest;
	}

	/**
	 * @return the exchangeRequest
	 */
	public ReturnRequest getExchangeRequest() {
		return exchangeRequest;
	}

	/**
	 * @param exchangeRequest
	 *            the exchangeRequest to set
	 */
	public void setExchangeRequest(ReturnRequest exchangeRequest) {
		this.exchangeRequest = exchangeRequest;
	}

}
