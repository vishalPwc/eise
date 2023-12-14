package com.aafes.settlement.core.order;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class OrderLineExtended {

	@JsonProperty("ResponsePlan")
	private String mResponsePlan;
	@JsonProperty("PromoPlan")
	private String mPromoPlan;

	/**
	 * @return the responsePlan
	 */
	public String getResponsePlan() {
		return mResponsePlan;
	}

	/**
	 * @param pResponsePlan
	 *            the responsePlan to set
	 */
	public void setResponsePlan(String pResponsePlan) {
		mResponsePlan = pResponsePlan;
	}

	/**
	 * @return the promoPlan
	 */
	public String getPromoPlan() {
		return mPromoPlan;
	}

	/**
	 * @param pPromoPlan
	 *            the promoPlan to set
	 */
	public void setPromoPlan(String pPromoPlan) {
		mPromoPlan = pPromoPlan;
	}

}
