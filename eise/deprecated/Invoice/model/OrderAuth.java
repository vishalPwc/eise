package com.aafes.settlement.Invoice.model;

public class OrderAuth {

	private Integer	currentAuthAmount;
	private Integer	currentSettledAmount;
	private Integer	currentRefundAmount;
	private Integer	amount;
	private String	paymentMethodId;
	private String	paymentTypeId;
	private String	cardTypeId;
	private String	responsePlan;
	private float	responsePlanAmount;

	/**
	 * @return the currentAuthAmount
	 */
	public Integer getCurrentAuthAmount() {
		return currentAuthAmount;
	}

	/**
	 * @param currentAuthAmount
	 *            the currentAuthAmount to set
	 */
	public void setCurrentAuthAmount(Integer currentAuthAmount) {
		this.currentAuthAmount = currentAuthAmount;
	}

	/**
	 * @return the currentSettledAmount
	 */
	public Integer getCurrentSettledAmount() {
		return currentSettledAmount;
	}

	/**
	 * @param currentSettledAmount
	 *            the currentSettledAmount to set
	 */
	public void setCurrentSettledAmount(Integer currentSettledAmount) {
		this.currentSettledAmount = currentSettledAmount;
	}

	/**
	 * @return the currentRefundAmount
	 */
	public Integer getCurrentRefundAmount() {
		return currentRefundAmount;
	}

	/**
	 * @param currentRefundAmount
	 *            the currentRefundAmount to set
	 */
	public void setCurrentRefundAmount(Integer currentRefundAmount) {
		this.currentRefundAmount = currentRefundAmount;
	}

	/**
	 * @return the amount
	 */
	public Integer getAmount() {
		return amount;
	}

	/**
	 * @param amount
	 *            the amount to set
	 */
	public void setAmount(Integer amount) {
		this.amount = amount;
	}

	/**
	 * @return the paymentMethodId
	 */
	public String getPaymentMethodId() {
		return paymentMethodId;
	}

	/**
	 * @param paymentMethodId
	 *            the paymentMethodId to set
	 */
	public void setPaymentMethodId(String paymentMethodId) {
		this.paymentMethodId = paymentMethodId;
	}

	/**
	 * @return the paymentTypeId
	 */
	public String getPaymentTypeId() {
		return paymentTypeId;
	}

	/**
	 * @param paymentTypeId
	 *            the paymentTypeId to set
	 */
	public void setPaymentTypeId(String paymentTypeId) {
		this.paymentTypeId = paymentTypeId;
	}

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
	 * @return the responsePlanAmount
	 */
	public float getResponsePlanAmount() {
		return responsePlanAmount;
	}

	/**
	 * @param responsePlanAmount
	 *            the responsePlanAmount to set
	 */
	public void setResponsePlanAmount(float responsePlanAmount) {
		this.responsePlanAmount = responsePlanAmount;
	}

}
