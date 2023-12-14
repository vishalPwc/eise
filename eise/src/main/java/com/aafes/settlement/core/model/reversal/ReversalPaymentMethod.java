package com.aafes.settlement.core.model.reversal;

import java.math.BigDecimal;
import java.util.List;

import com.aafes.settlement.constant.Constants;
import com.aafes.settlement.core.payment.PaymentMethodCardType;
import com.aafes.settlement.core.payment.PaymentMethodExtended;
import com.aafes.settlement.core.payment.PaymentTransaction;
import com.aafes.settlement.core.payment.PaymentType;
import com.aafes.settlement.util.Utils;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ReversalPaymentMethod
		implements
			Constants,
			Comparable<ReversalPaymentMethod> {

	@JsonProperty("PK")
	private String pK;
	@JsonProperty("PaymentMethodId")
	private String paymentMethodId;
	@JsonProperty("Amount")
	private BigDecimal amount;
	@JsonProperty("CurrentAuthAmount")
	private BigDecimal currentAuthAmount;
	@JsonProperty("CurrentSettledAmount")
	private BigDecimal currentSettledAmount;
	@JsonProperty("PaymentTransaction")
	private List<PaymentTransaction> paymentTransaction;
	@JsonProperty("PaymentType")
	private PaymentType paymentType;
	@JsonProperty("Extended")
	private PaymentMethodExtended extended;
	@JsonProperty("CardType")
	private PaymentMethodCardType cardType;

	/**
	 * For Auth reversal Scenario, Setting the following Payments with Priority
	 * 1) Military Uniform 2) Military Retail 3) Credit Card 4) Highest GC
	 * 
	 * @param pO1
	 * @param pO2
	 * @return
	 */
	@Override
	public int compareTo(ReversalPaymentMethod pO) {

		// Priority 1 - IF MU
		if (pO.getExtended().getResponsePlan() != null
				&& pO.getExtended().getResponsePlan()
						.equalsIgnoreCase(PLAN_MILITARY_UNIFORM)
				&& Utils.isValueGreater(pO.getCurrentAuthAmount(), IS_FINISHED)
		// pO.getCurrentAuthAmount() > IS_FINISHED
		) {
			return 1;
		}
		//// Priority 2 - IF MR
		if (pO.getExtended().getResponsePlan() != null
				&& pO.getExtended().getResponsePlan()
						.equalsIgnoreCase(PLAN_MILITARY_RETAIL)
				&& Utils.isValueGreater(pO.getCurrentAuthAmount(),
						IS_FINISHED)) {
			// pO.getCurrentAuthAmount() > IS_FINISHED) {
			return 1;
		}

		// Priority 3 - IF CC
		if (pO.getPaymentType().getPaymentTypeId().equalsIgnoreCase(CREDIT_CARD)
				&& this.getExtended().getResponsePlan() == null) {
			return 1;
		}

		// Priority 4 highest GC
		if (Utils.isValueGreater(pO.getCurrentAuthAmount(),
				this.getCurrentAuthAmount())
				// if (pO.getCurrentAuthAmount() > this.getCurrentAuthAmount()
				&& (this.getExtended().getResponsePlan() == null
						&& pO.getExtended().getResponsePlan() == null)) {
			return 1;
		}
		return -1;

	}

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
		return currentAuthAmount;
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
		return currentSettledAmount;
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
			List<PaymentTransaction> pPaymentTransaction) {
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
	 * @return the cardType
	 */
	public PaymentMethodCardType getCardType() {
		return cardType;
	}

	/**
	 * @param pCardType
	 *            the cardType to set
	 */
	public void setCardType(PaymentMethodCardType pCardType) {
		cardType = pCardType;
	}

}
