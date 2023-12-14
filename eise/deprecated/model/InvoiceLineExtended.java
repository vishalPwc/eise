package com.aafes.settlement.Invoice.model;

import com.aafes.settlement.constant.Constants;
import com.aafes.settlement.util.Utils;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class InvoiceLineExtended
	implements Constants
{

	@JsonProperty("ResponsePlan")
	private String responseStrPlan;

	private int	   responsePlan;

	@JsonProperty("PromoPlan")
	private String promoStrPlan;

	private int	   promoPlan;

	private int	   shipmentPlanOrder;

	public String getResponseStrPlan() {
		return responseStrPlan;
	}

	public void setResponseStrPlan(String responseStrPlan) {
		this.responseStrPlan = responseStrPlan;
		/*
		 * try {
		 * 
		 * setResponsePlan( !Utils.isNull(responseStrPlan) && responseStrPlan !=
		 * "" ? Integer.parseInt(responseStrPlan) : 0 ); if (responsePlan ==
		 * (PLAN_MILITARY_UNIFORM)) { setShipmentPlanOrder(1); } } catch
		 * (Exception e) { e.printStackTrace(); }
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
		 * try { setPromoPlan( !Utils.isNull(promoStrPlan) && promoStrPlan != ""
		 * ? Integer.parseInt(promoStrPlan) : 0 );
		 * 
		 * if ( promoPlan != 0 && !Utils.isNull(promoStrPlan) && promoStrPlan !=
		 * "" ) { setShipmentPlanOrder(2); } else if ( Utils.isNull(promoPlan)
		 * && responsePlan == PLAN_MILTARY_RETAIL ) { setShipmentPlanOrder(3); }
		 * 
		 * } catch (Exception e) { e.printStackTrace(); }
		 */
	}

	public int getPromoPlan() {
		return promoPlan;
	}

	public void setPromoPlan(int promoPlan) {
		this.promoPlan = promoPlan;
	}

	/**
	 * @return the shipmentPlanOrder
	 */
	public int getShipmentPlanOrder() {
		return shipmentPlanOrder;
	}

	/**
	 * @param shipmentPlanOrder
	 *            the shipmentPlanOrder to set
	 */
	public void setShipmentPlanOrder(int shipmentPlanOrder) {
		this.shipmentPlanOrder = shipmentPlanOrder;
	}

}
