package com.aafes.settlement.Invoice.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PaymentMethodCardType {

	@JsonProperty("CardTypeId")
	private String cardTypeId;

	/**
	 * @return the cardTypeId
	 */
	public String getCardTypeId() {
		return cardTypeId;
	}

	/**
	 * @param cardTypeId
	 *            the cardTypeId to set
	 */
	public void setCardTypeId(String cardTypeId) {
		this.cardTypeId = cardTypeId;
	}

}
