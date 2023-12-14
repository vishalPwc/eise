package com.aafes.settlement.Invoice.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class OrderLineExtended {

	@JsonProperty("ResponsePlan")
	private String responseStrPlan;

	private int	   responsePlan;

	@JsonProperty("PromoPlan")
	private String promoStrPlan;

	private int	   promoPlan;

	public String getResponseStrPlan() {
		return responseStrPlan;
	}

	public void setResponseStrPlan(String responseStrPlan) {
		this.responseStrPlan = responseStrPlan;
		/*
		 * try {
		 * 
		 * setResponsePlan( responseStrPlan != null && responseStrPlan != "" ?
		 * Integer.parseInt(responseStrPlan) : 0 ); } catch (Exception e) {
		 * e.printStackTrace(); }
		 */
	}

	public int getResponsePlan() {
		return responsePlan;
	}

	public void setResponsePlan(int responsePlan) {
		this.responsePlan = responsePlan;
	}

	public String getPromoStrPlan() {
		return promoStrPlan;
	}

	public void setPromoStrPlan(String promoStrPlan) {
		this.promoStrPlan = promoStrPlan;
		/*
		 * try { setPromoPlan( promoStrPlan != null && promoStrPlan != "" ?
		 * Integer.parseInt(promoStrPlan) : 0 ); } catch (Exception e) {
		 * e.printStackTrace(); }
		 */
	}

	public int getPromoPlan() {
		return promoPlan;
	}

	public void setPromoPlan(int promoPlan) {
		this.promoPlan = promoPlan;
	}

}
