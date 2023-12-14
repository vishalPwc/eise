package com.aafes.settlement.core.order;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class OrderChargeDetail {

	@JsonProperty("ChargeDetailId")
	private String			chargeDetailId;
	@JsonProperty("ChargeType")
	private OrderChargeType	chargeType;
	@JsonProperty("ChargeTotal")
	private BigDecimal			chargeTotal;
	@JsonProperty("OriginalChargeAmount")
	private BigDecimal			originalChargeAmount;

	/**
	 * @return the chargeDetailId
	 */
	public String getChargeDetailId() {
		return chargeDetailId;
	}

	/**
	 * @param chargeDetailId
	 *            the chargeDetailId to set
	 */
	public void setChargeDetailId(String chargeDetailId) {
		this.chargeDetailId = chargeDetailId;
	}

	/**
	 * @return the chargeType
	 */
	public OrderChargeType getChargeType() {
		return chargeType;
	}

	/**
	 * @param chargeType
	 *            the chargeType to set
	 */
	public void setChargeType(OrderChargeType chargeType) {
		this.chargeType = chargeType;
	}

	/**
	 * @return the chargeTotal
	 */
	public BigDecimal getChargeTotal() {
		return chargeTotal;
	}

	/**
	 * @param chargeTotal
	 *            the chargeTotal to set
	 */
	public void setChargeTotal(BigDecimal chargeTotal) {
		this.chargeTotal = chargeTotal;
	}

	/**
	 * @return the originalChargeAmount
	 */
	public BigDecimal getOriginalChargeAmount() {
		return originalChargeAmount;
	}

	/**
	 * @param originalChargeAmount
	 *            the originalChargeAmount to set
	 */
	public void setOriginalChargeAmount(BigDecimal originalChargeAmount) {
		this.originalChargeAmount = originalChargeAmount;
	}

}
