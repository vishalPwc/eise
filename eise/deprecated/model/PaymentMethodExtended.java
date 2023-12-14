package com.aafes.settlement.Invoice.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PaymentMethodExtended {
	//-------------------------------------------------------------------------
	@JsonProperty("ResponsePlan")
	private String responsePlan;
	
	@JsonProperty("ResponsePlanAmount")
	private float  responsePlanAmount;

	@JsonProperty("NewRefundGiftCard")
	private boolean newRefundGiftCard;
	//-------------------------------------------------------------------------
	/**
	 * @return the responsePlan
	 */
	public String getResponsePlan() {
		return responsePlan;
	}

	/**
	 * @param responsePlan
	 *            the responsePlan to set
	 */
	public void setResponsePlan(String p_responsePlan) {
		responsePlan = p_responsePlan;
	}

	/**
	 * @return the responsePlanAmount
	 */
	public float getResponsePlanAmount() {
		return responsePlanAmount;
	}

	/**
	 * @param p_responsePlanAmount the responsePlanAmount to set
	 */
	public void setResponsePlanAmount(float p_responsePlanAmount) {
		responsePlanAmount = p_responsePlanAmount;
	}

	/**
	 * @return the newRefundGiftCard
	 */
	public boolean isNewRefundGiftCard() {
		return newRefundGiftCard;
	}

	/**
	 * @param p_newRefundGiftCard the newRefundGiftCard to set
	 */
	public void setNewRefundGiftCard(boolean p_newRefundGiftCard) {
		newRefundGiftCard = p_newRefundGiftCard;
	}
	//-------------------------------------------------------------------------
}
//-----------------------------------------------------------------------------
// END OF FILE
//-----------------------------------------------------------------------------