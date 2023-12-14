package com.aafes.settlement.core.model.adjustment;

import java.util.PriorityQueue;

import com.aafes.settlement.core.invoice.InvoiceChargeDetail;
import com.aafes.settlement.core.model.ProcessingContainer;

public class AdjustmentProcessingContainer
	extends
	ProcessingContainer
{
	private PriorityQueue<
			InvoiceChargeDetail>				 mOpenChargeDetails		= new PriorityQueue<>(
					new InvoiceChargeDetail()
			);

	private PriorityQueue<
			InvoiceChargeDetail>				 mMilitaryUniformCharge	= new PriorityQueue<>(
					new InvoiceChargeDetail()
			);

	private PriorityQueue<
			InvoiceChargeDetail>				 mMilitaryRetailCharge	= new PriorityQueue<>(
					new InvoiceChargeDetail()
			);

	private PriorityQueue<
			AdjustmentInvoiceLine>				 mMilitaryUniformLine	= new PriorityQueue<>(
					new AdjustmentInvoiceLine()
			);

	private PriorityQueue<
			AdjustmentInvoiceLine>				 mMilitaryPromoLine		= new PriorityQueue<>(
					new AdjustmentInvoiceLine()
			);

	/**
	 * @return the militaryUniformLine
	 */
	public PriorityQueue<AdjustmentInvoiceLine> getMilitaryUniformLine() {
		return mMilitaryUniformLine;
	}

	/**
	 * @param pMilitaryUniformLine
	 *            the militaryUniformLine to set
	 */
	public void setMilitaryUniformLine(
			PriorityQueue<AdjustmentInvoiceLine> pMilitaryUniformLine
	)
	{
		mMilitaryUniformLine = pMilitaryUniformLine;
	}

	/**
	 * @return the militaryPromoLine
	 */
	public PriorityQueue<AdjustmentInvoiceLine> getMilitaryPromoLine() {
		return mMilitaryPromoLine;
	}

	/**
	 * @param pMilitaryPromoLine
	 *            the militaryPromoLine to set
	 */
	public void setMilitaryPromoLine(
			PriorityQueue<AdjustmentInvoiceLine> pMilitaryPromoLine
	)
	{
		mMilitaryPromoLine = pMilitaryPromoLine;
	}

	/**
	 * @return the openChargeDetails
	 */
	public PriorityQueue<InvoiceChargeDetail> getOpenChargeDetails() {
		return mOpenChargeDetails;
	}

	/**
	 * @param pOpenChargeDetails
	 *            the openChargeDetails to set
	 */
	public void setOpenChargeDetails(
			PriorityQueue<InvoiceChargeDetail> pOpenChargeDetails
	)
	{
		mOpenChargeDetails = pOpenChargeDetails;
	}

	/**
	 * @return the militaryUniformCharge
	 */
	public PriorityQueue<InvoiceChargeDetail> getMilitaryUniformCharge() {
		return mMilitaryUniformCharge;
	}

	/**
	 * @param pMilitaryUniformCharge
	 *            the militaryUniformCharge to set
	 */
	public void setMilitaryUniformCharge(
			PriorityQueue<InvoiceChargeDetail> pMilitaryUniformCharge
	)
	{
		mMilitaryUniformCharge = pMilitaryUniformCharge;
	}

	/**
	 * @return the militaryRetailCharge
	 */
	public PriorityQueue<InvoiceChargeDetail> getMilitaryRetailCharge() {
		return mMilitaryRetailCharge;
	}

	/**
	 * @param pMilitaryRetailCharge
	 *            the militaryRetailCharge to set
	 */
	public void setMilitaryRetailCharge(
			PriorityQueue<InvoiceChargeDetail> pMilitaryRetailCharge
	)
	{
		mMilitaryRetailCharge = pMilitaryRetailCharge;
	}
}
