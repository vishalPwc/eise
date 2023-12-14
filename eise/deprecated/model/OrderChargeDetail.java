package com.aafes.settlement.Invoice.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class OrderChargeDetail {

	@JsonProperty("ChargeDetailId")
	private String	   chargeDetailId;
	@JsonProperty("ChargeType")
	private ChargeType chargeType;
	@JsonProperty("ChargeTotal")
	private Double	   chargeTotal;
	@JsonProperty("OriginalChargeAmount")
	private Double	   originalChargeAmount;

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
	public ChargeType getChargeType() {
		return chargeType;
	}

	/**
	 * @param chargeType
	 *            the chargeType to set
	 */
	public void setChargeType(ChargeType chargeType) {
		this.chargeType = chargeType;
	}

	/**
	 * @return the chargeTotal
	 */
	public Double getChargeTotal() {
		return chargeTotal;
	}

	/**
	 * @param chargeTotal
	 *            the chargeTotal to set
	 */
	public void setChargeTotal(Double chargeTotal) {
		this.chargeTotal = chargeTotal;
	}

	/**
	 * @return the originalChargeAmount
	 */
	public Double getOriginalChargeAmount() {
		return originalChargeAmount;
	}

	/**
	 * @param originalChargeAmount
	 *            the originalChargeAmount to set
	 */
	public void setOriginalChargeAmount(Double originalChargeAmount) {
		this.originalChargeAmount = originalChargeAmount;
	}

}
