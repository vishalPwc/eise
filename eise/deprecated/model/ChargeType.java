package com.aafes.settlement.Invoice.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ChargeType {

	@JsonProperty("ChargeTypeId")
	private String chargeTypeId;

	/**
	 * @return the chargeTypeId
	 */
	public String getChargeTypeId() {
		return chargeTypeId;
	}

	/**
	 * @param chargeTypeId
	 *            the chargeTypeId to set
	 */
	public void setChargeTypeId(String chargeTypeId) {
		this.chargeTypeId = chargeTypeId;
	}

}
