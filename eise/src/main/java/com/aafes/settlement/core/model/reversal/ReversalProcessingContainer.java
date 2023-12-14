/**
 * 
 */
package com.aafes.settlement.core.model.reversal;

import java.util.PriorityQueue;

import com.aafes.settlement.core.invoice.OpenAuthReversal;
import com.aafes.settlement.core.model.ProcessingContainer;

public class ReversalProcessingContainer
	extends
	ProcessingContainer
{
	private PriorityQueue<
			OpenAuthReversal>					 mOpenAuthLines			 = new PriorityQueue<>(
					new OpenAuthReversal()
			);

	private PriorityQueue<
			ReversalPaymentMethod>				 mReversalPaymentMethods = new PriorityQueue<>();

	private ReversalPaymentMethod				 mMilstarUniformCard;

	private ReversalPaymentMethod				 mMilstarRetailCard;

	/**
	 * @return the openAuthLines
	 */
	public PriorityQueue<OpenAuthReversal> getOpenAuthLines() {
		return mOpenAuthLines;
	}

	/**
	 * @param pOpenAuthLines
	 *            the openAuthLines to set
	 */
	public void setOpenAuthLines(
			PriorityQueue<OpenAuthReversal> pOpenAuthLines
	)
	{
		mOpenAuthLines = pOpenAuthLines;
	}

	/**
	 * @return the reversalPaymentMethods
	 */
	public PriorityQueue<ReversalPaymentMethod> getReversalPaymentMethods() {
		return mReversalPaymentMethods;
	}

	/**
	 * @param pReversalPaymentMethods
	 *            the reversalPaymentMethods to set
	 */
	public void setReversalPaymentMethods(
			PriorityQueue<ReversalPaymentMethod> pReversalPaymentMethods
	)
	{
		mReversalPaymentMethods = pReversalPaymentMethods;
	}

	/**
	 * @return the milstarUniformCard
	 */
	public ReversalPaymentMethod getMilstarUniformMethod() {
		return mMilstarUniformCard;
	}

	/**
	 * @param pMilstarUniformCard
	 *            the milstarUniformCard to set
	 */
	public void setMilstarUniformCard(
			ReversalPaymentMethod pMilstarUniformCard
	)
	{
		mMilstarUniformCard = pMilstarUniformCard;
	}

	/**
	 * @return the milstarRetailCard
	 */
	public ReversalPaymentMethod getMilstarRetailMethod() {
		return mMilstarRetailCard;
	}

	/**
	 * @param pMilstarRetailCard
	 *            the milstarRetailCard to set
	 */
	public void setMilstarRetailCard(ReversalPaymentMethod pMilstarRetailCard) {
		mMilstarRetailCard = pMilstarRetailCard;
	}
}
