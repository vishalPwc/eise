package com.aafes.settlement.util;

import java.math.BigDecimal;

import com.aafes.settlement.constant.Constants;
import com.aafes.settlement.core.model.ProcessingContainer;
import com.aafes.settlement.core.payment.FollowOnParentTransaction;
import com.aafes.settlement.core.payment.PaymentMethod;
import com.aafes.settlement.core.payment.PaymentTransaction;

public class ParserUtils
	implements Constants
{

	// ------------------------------------------------------------------------
	/**
	 * If the PaymentMethod.Extended.NewRefundGiftCard is true, this method will
	 * distinguish them into a HashMap.
	 * 
	 * @param p_container
	 * @param p_paymentMethod
	 */
	public static void distinguishNewRefundGiftCard(
			ProcessingContainer p_container, PaymentMethod p_paymentMethod
	)
	{
		if (p_paymentMethod.getPaymentTransaction() != null) {
			String l_parentPaymentId = null;
			BigDecimal l_d = null;

			for (PaymentTransaction l_paymentTxn : p_paymentMethod
					.getPaymentTransaction())
			{
				if (
					l_paymentTxn.getTransactionType() != null
							&& REFUND.equalsIgnoreCase(
									l_paymentTxn.getTransactionType()
											.getPaymentTransactionTypeId()
							)
				)
				{
					if (
						!l_paymentTxn.getFollowOnParentTransaction()
								.isEmpty()
					)
					{
						for (FollowOnParentTransaction l_fonpTxn : l_paymentTxn
								.getFollowOnParentTransaction())
						{
							l_parentPaymentId = l_fonpTxn
									.getParentPaymentMethodId();
							if (
								!Utils.isNull(l_paymentTxn)
										&& l_fonpTxn.getAmount() != null
							)
							{
								if (
									p_container.newRefundGCTxns
											.containsKey(l_parentPaymentId)
								)
								{
									l_d = p_container.newRefundGCTxns
											.get(l_parentPaymentId)
											.add(l_fonpTxn.getAmount());
									p_container.newRefundGCTxns
											.put(l_parentPaymentId, l_d);
								} else {
									p_container.newRefundGCTxns.put(
											l_parentPaymentId,
											l_fonpTxn.getAmount()
									);
								}
							}
						}
					}
				}
			}
		}
	}
	// ------------------------------------------------------------------------

	/**
	 * This method will deduct the returned transactions
	 * 
	 * @param p_currentMethod
	 */
	public static void deductReturnedTransactions(
			PaymentMethod p_currentMethod
	)
	{
		if (p_currentMethod.getPaymentTransaction() != null) {
			for (PaymentTransaction l_paymentTransaction : p_currentMethod
					.getPaymentTransaction())
			{
				if (l_paymentTransaction.getTransactionType() != null) {
					if (/*
						 * REFUND.equalsIgnoreCase(l_paymentTransaction
						 * .getTransactionType().getPaymentTransactionTypeId())
						 * ||
						 */
					RETURN_CREDIT.equalsIgnoreCase(
							l_paymentTransaction.getTransactionType()
									.getPaymentTransactionTypeId()
					)
							&& l_paymentTransaction.getProcessedAmount() != null
					)
					{
						p_currentMethod.setCurrentSettledAmount(
								p_currentMethod.getCurrentSettledAmount()
										.subtract(
												l_paymentTransaction
														.getProcessedAmount()
										)
						);
					}
				}
			}
		}
	}
	// ------------------------------------------------------------------------
}
// ----------------------------------------------------------------------------
// END OF FILE
// ----------------------------------------------------------------------------