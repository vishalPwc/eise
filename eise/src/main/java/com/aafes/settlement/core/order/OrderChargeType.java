package com.aafes.settlement.core.order;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class OrderChargeType {

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
