package com.aafes.settlement.model;

import java.util.List;

import com.aafes.settlement.exception.Errors;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ResponseSettlement {

	@JsonProperty("responseCode")
	private String				 responseCode;

	@JsonProperty("responseMessage")
	private String				 responseMessage;

	@JsonProperty("errors")
	private List<Errors> error;

	@JsonProperty("SettlementTender")
	private List<ResponseTender> settlementTender;

	@JsonProperty("RefundTender")
	private List<ResponseTender> refundTender;

	@JsonProperty("AuthReversalTender")
	private List<ResponseTender> authReversalTender;

	@JsonProperty("AdjustmentTender")
	private List<ResponseTender> adjustmentTender;

	@JsonProperty("ExchangeTender")
	private List<ResponseTender> exchangeTender;

	/**
	 * @return the responseCode
	 */
	public String getResponseCode() {
		return responseCode;
	}

	/**
	 * @param responseCode
	 *            the responseCode to set
	 */
	public void setResponseCode(String responseCode) {
		this.responseCode = responseCode;
	}

	/**
	 * @return the responseMessage
	 */
	public String getResponseMessage() {
		return responseMessage;
	}

	/**
	 * @param responseMessage
	 *            the responseMessage to set
	 */
	public void setResponseMessage(String responseMessage) {
		this.responseMessage = responseMessage;
	}

	/**
	 * @return the error
	 */
	public List<Errors> getError() {
		return error;
	}

	/**
	 * @param p_list
	 *            the error to set
	 */
	public void setError(List<Errors> p_list) {
		this.error = p_list;
	}

	/**
	 * @return the settlementTender
	 */
	public List<ResponseTender> getSettlementTender() {
		return settlementTender;
	}

	/**
	 * @param settlementTender
	 *            the settlementTender to set
	 */
	public void setSettlementTender(List<ResponseTender> settlementTender) {
		this.settlementTender = settlementTender;
	}

	/**
	 * @return the refundTender
	 */
	public List<ResponseTender> getRefundTender() {
		return refundTender;
	}

	/**
	 * @param refundTender
	 *            the refundTender to set
	 */
	public void setRefundTender(List<ResponseTender> refundTender) {
		this.refundTender = refundTender;
	}

	/**
	 * @return the authReversalTender
	 */
	public List<ResponseTender> getAuthReversalTender() {
		return authReversalTender;
	}

	/**
	 * @param authReversalTender
	 *            the authReversalTender to set
	 */
	public void setAuthReversalTender(List<ResponseTender> authReversalTender) {
		this.authReversalTender = authReversalTender;
	}

	/**
	 * @return the adjustmentTender
	 */
	public List<ResponseTender> getAdjustmentTender() {
		return adjustmentTender;
	}

	/**
	 * @param adjustmentTender
	 *            the adjustmentTender to set
	 */
	public void setAdjustmentTender(List<ResponseTender> adjustmentTender) {
		this.adjustmentTender = adjustmentTender;
	}

	/**
	 * @return the exchangeTender
	 */
	public List<ResponseTender> getExchangeTender() {
		return exchangeTender;
	}

	/**
	 * @param exchangeTender
	 *            the exchangeTender to set
	 */
	public void setExchangeTender(List<ResponseTender> exchangeTender) {
		this.exchangeTender = exchangeTender;
	}

}
