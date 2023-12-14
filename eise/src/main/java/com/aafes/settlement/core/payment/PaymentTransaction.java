package com.aafes.settlement.core.payment;

import java.math.BigDecimal;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PaymentTransaction {

	// ------------------------------------------------------------------------
	@JsonProperty("PaymentTransactionId")
	private String paymentTransactionId;

	@JsonInclude(Include.NON_EMPTY)
	@JsonProperty("TransactionType")
	private TransactionType transactionType;

	@JsonInclude(Include.NON_EMPTY)
	@JsonProperty("FollowOnParentTransaction")
	private List<FollowOnParentTransaction> followOnParentTransaction;

	// reused in response of auth reversal
	@JsonProperty("PaymentAmount")
	private BigDecimal paymentAmount;

	@JsonInclude(Include.NON_EMPTY)
	@JsonProperty("ProcessedAmount")
	private BigDecimal processedAmount;

	// ------------------------------------------------------------------------
	/**
	 * 
	 * @return
	 */
	public String getPaymentTransactionId() {
		return paymentTransactionId;
	}

	/**
	 * 
	 * @param paymentTransactionId
	 */
	public void setPaymentTransactionId(String paymentTransactionId) {
		this.paymentTransactionId = paymentTransactionId;
	}

	/**
	 * @return the followOnParentTransaction
	 */
	public List<FollowOnParentTransaction> getFollowOnParentTransaction() {
		return followOnParentTransaction;
	}

	/**
	 * @param p_followOnParentTransaction
	 *            the followOnParentTransaction to set
	 */
	public void setFollowOnParentTransaction(
			List<FollowOnParentTransaction> p_followOnParentTransaction) {
		followOnParentTransaction = p_followOnParentTransaction;
	}
	// ------------------------------------------------------------------------

	/**
	 * @return the paymentAmount
	 */
	public BigDecimal getPaymentAmount() {
		return paymentAmount;
	}

	/**
	 * @param pPaymentAmount
	 *            the paymentAmount to set
	 */
	public void setPaymentAmount(BigDecimal pPaymentAmount) {
		paymentAmount = pPaymentAmount;
	}

	/**
	 * @return the transactionType
	 */
	public TransactionType getTransactionType() {
		return transactionType;
	}

	/**
	 * @param pTransactionType
	 *            the transactionType to set
	 */
	public void setTransactionType(TransactionType pTransactionType) {
		transactionType = pTransactionType;
	}

	/**
	 * @return the processedAmount
	 */
	public BigDecimal getProcessedAmount() {

		if (processedAmount != null) {
			return processedAmount;
		} else {
			return new BigDecimal(0.0);
		}
	}

	/**
	 * @param p_processedAmount
	 *            the processedAmount to set
	 */
	public void setProcessedAmount(BigDecimal p_processedAmount) {
		processedAmount = p_processedAmount;
	}
}
// ----------------------------------------------------------------------------
// END OF FILE
// ----------------------------------------------------------------------------
