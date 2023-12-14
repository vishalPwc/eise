package com.aafes.settlement.core.invoice;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class InvoiceLineExtended {

	@JsonProperty("ResponsePlan")
	private String responsePlan;
	@JsonProperty("PromoPlan")
	private String promoPlan;

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
	public void setResponsePlan(String responsePlan) {
		this.responsePlan = responsePlan;
	}

	/**
	 * @return the promoPlan
	 */
	public String getPromoPlan() {
		return promoPlan;
	}

	/**
	 * @param promoPlan
	 *            the promoPlan to set
	 */
	public void setPromoPlan(String promoPlan) {
		this.promoPlan = promoPlan;
	}

}
