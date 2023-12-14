package com.aafes.settlement.core.payment;

import java.math.BigDecimal;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PaymentMethod
	implements Comparable<PaymentMethod>
{

	@JsonProperty("PK")
	private String					 pK;

	@JsonProperty("PaymentMethodId")
	private String					 paymentMethodId;

	@JsonProperty("Amount")
	private BigDecimal				 amount;

	@JsonProperty("CurrentAuthAmount")
	private BigDecimal				 currentAuthAmount;

	@JsonProperty("CurrentSettledAmount")
	private BigDecimal				 currentSettledAmount;

	@JsonProperty("PaymentTransaction")
	private List<PaymentTransaction> paymentTransaction;

	@JsonProperty("PaymentType")
	private PaymentType				 paymentType;

	@JsonProperty("CardType")
	private PaymentMethodCardType	 cardType;

	@JsonProperty("Extended")
	private PaymentMethodExtended	 extended;

	private boolean					 considerCurrentSettled;

	/**
	 * @return the pK
	 */
	public String getpK() {
		return pK;
	}

	/**
	 * @param pPK
	 *            the pK to set
	 */
	public void setpK(String pPK) {
		pK = pPK;
	}

	/**
	 * @return the paymentMethodId
	 */
	public String getPaymentMethodId() {
		return paymentMethodId;
	}

	/**
	 * @param pPaymentMethodId
	 *            the paymentMethodId to set
	 */
	public void setPaymentMethodId(String pPaymentMethodId) {
		paymentMethodId = pPaymentMethodId;
	}

	/**
	 * @return the amount
	 */
	public BigDecimal getAmount() {
		return amount;
	}

	/**
	 * @param pAmount
	 *            the amount to set
	 */
	public void setAmount(BigDecimal pAmount) {
		amount = pAmount;
	}

	/**
	 * @return the currentAuthAmount
	 */
	public BigDecimal getCurrentAuthAmount() {
		if (currentAuthAmount != null)
			return currentAuthAmount;
		else
			return new BigDecimal(0.0);
	}

	/**
	 * @param pCurrentAuthAmount
	 *            the currentAuthAmount to set
	 */
	public void setCurrentAuthAmount(BigDecimal pCurrentAuthAmount) {
		currentAuthAmount = pCurrentAuthAmount;
	}

	/**
	 * @return the currentSettledAmount
	 */
	public BigDecimal getCurrentSettledAmount() {

		if (currentSettledAmount != null) {
			return currentSettledAmount;
		} else {
			return new BigDecimal(0.0);
		}
	}

	/**
	 * @param pCurrentSettledAmount
	 *            the currentSettledAmount to set
	 */
	public void setCurrentSettledAmount(BigDecimal pCurrentSettledAmount) {
		currentSettledAmount = pCurrentSettledAmount;
	}

	/**
	 * @return the paymentTransaction
	 */
	public List<PaymentTransaction> getPaymentTransaction() {
		return paymentTransaction;
	}

	/**
	 * @param pPaymentTransaction
	 *            the paymentTransaction to set
	 */
	public void setPaymentTransaction(
			List<PaymentTransaction> pPaymentTransaction
	)
	{
		paymentTransaction = pPaymentTransaction;
	}

	/**
	 * @return the paymentType
	 */
	public PaymentType getPaymentType() {
		return paymentType;
	}

	/**
	 * @param pPaymentType
	 *            the paymentType to set
	 */
	public void setPaymentType(PaymentType pPaymentType) {
		paymentType = pPaymentType;
	}

	/**
	 * @return the cardType
	 */
	public PaymentMethodCardType getCardType() {
		return cardType;
	}

	/**
	 * @param p_cardType
	 *            the cardType to set
	 */
	public void setCardType(PaymentMethodCardType p_cardType) {
		cardType = p_cardType;
	}

	/**
	 * @return the extended
	 */
	public PaymentMethodExtended getExtended() {
		return extended;
	}

	/**
	 * @param pExtended
	 *            the extended to set
	 */
	public void setExtended(PaymentMethodExtended pExtended) {
		extended = pExtended;
	}

	/**
	 * @return the considerCurrentSettled
	 */
	public boolean isConsiderCurrentSettled() {
		return considerCurrentSettled;
	}

	/**
	 * @param p_considerCurrentSettled
	 *            the considerCurrentSettled to set
	 */
	public void setConsiderCurrentSettled(boolean p_considerCurrentSettled) {
		considerCurrentSettled = p_considerCurrentSettled;
	}

	/**
	 * compare current Auth Amount to set priority
	 * 
	 * @param pO1
	 * @param pO2
	 * @return
	 */
	@Override
	public int compareTo(PaymentMethod pO1) {
		// overriding the method to set the priority
		int result;
		int returnValue = 0;
		if (pO1.isConsiderCurrentSettled()) {
			result = pO1.getCurrentSettledAmount()
					.compareTo(this.getCurrentSettledAmount());

			/*
			 * if (result == 1) returnValue = -1; else if (result == -1)
			 * returnValue = 1;
			 */

			return result;

			// if (pO1.getCurrentSettledAmount() >
			// this.getCurrentSettledAmount())
			// return -1;
			// else
			// return 1;
		} else {
			result = pO1.getCurrentAuthAmount()
					.compareTo(this.getCurrentAuthAmount());
			if (result == 1)
				returnValue = -1;
			else if (result == -1)
				returnValue = 1;
			// if (pO1.getCurrentAuthAmount() > this.getCurrentAuthAmount())
			// return -1;
			// else
			// return 1;
		}
		return returnValue;

	}
}
