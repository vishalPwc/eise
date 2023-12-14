/**
 * 
 */
package com.aafes.settlement.core.model;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Stack;

import com.aafes.settlement.constant.Constants;
import com.aafes.settlement.core.invoice.InvoiceChargeDetail;
import com.aafes.settlement.core.invoice.InvoiceLine;
import com.aafes.settlement.core.payment.PaymentMethod;
import com.aafes.settlement.core.response.PaymentMethodResponse;

public class ProcessingContainer
	implements Constants
{

	// ------------------------------------------------------------------------
	private PriorityQueue<InvoiceLine> mOpenInvoiceLines = new PriorityQueue<>(
			new InvoiceLine());

	private PriorityQueue<InvoiceLine> mMilitaryUniformLines = new PriorityQueue<>(
			new InvoiceLine());

	private PriorityQueue<InvoiceLine> mMilitaryPromoLines = new PriorityQueue<>(
			new InvoiceLine());

	private BigDecimal mShippingCharge = new BigDecimal(
			0.0);

	private PriorityQueue<InvoiceChargeDetail> mShippingChargeQueue = new PriorityQueue<>(
			new InvoiceChargeDetail());

	private BigDecimal mOpenInvoiceLineTotal = new BigDecimal(
			0.0);

	private BigDecimal mInvoiceChargeTotal = new BigDecimal(
			0.0);

	private Stack<PaymentMethod> mSettlementCCMethods = new Stack<>();

	private PriorityQueue<PaymentMethod> mSettlementGCMethods = new PriorityQueue<>();

	private PaymentMethod mMilstarUniformCard;

	private PaymentMethod mMilstarRetailCard;

	private BigDecimal mTotalPaymentUniformAuth = new BigDecimal(
			0.0);

	private BigDecimal mTotalPaymentUniformSettled = new BigDecimal(
			0.0);

	private BigDecimal mTotalPaymentRetailAuth = new BigDecimal(
			0.0);

	private BigDecimal mTotalPaymentRetailSettled = new BigDecimal(
			0.0);

	private BigDecimal mTotalPaymentSettled = new BigDecimal(
			0.0);

	private BigDecimal mTotalPaymentAuth = new BigDecimal(
			0.0);

	private BigDecimal mTotalMUReturnedAmount = new BigDecimal(
			0.0);

	private BigDecimal mTotalMRReturnedAmount = new BigDecimal(
			0.0);

	private BigDecimal mTotalReturnedAmount = new BigDecimal(
			0.0);

	private BigDecimal mTotalMUOrderedAmount = new BigDecimal(
			0.0);

	private BigDecimal mTotalMROrderedAmount = new BigDecimal(
			0.0);

	private BigDecimal mTotalOrderedAmount = new BigDecimal(
			0.0);

	private BigDecimal mMilitaryUninformLinesTotal = new BigDecimal(
			0.0);

	private BigDecimal mMilitaryPromoLinesTotalDouble = new BigDecimal(
			0.0);

	public HashMap<String, BigDecimal> newRefundGCTxns = new HashMap<String, BigDecimal>();
	
	private Map<String, PaymentMethodResponse> mPaymentMethodResponses = new HashMap<String, PaymentMethodResponse>();

	private boolean mMilstarBucket = false;

	private BigDecimal mUnevenRefundAmountTotal = new BigDecimal(
        0.0);


	// -----------------------------------------------------------------------------------
	/**
	 * 
	 * @return
	 */
	public BigDecimal getUnevenRefundAmountTotal() {
		return mUnevenRefundAmountTotal;
	}
	/**
	 * 
	 * @param mUnevenRefundAmountTotal
	 */
	public void setUnevenRefundAmountTotal(BigDecimal mUnevenRefundAmountTotal) {
		this.mUnevenRefundAmountTotal = mUnevenRefundAmountTotal;
	}

	/**
	 * @param pMilstarBucket
	 *            the milstarBucket to set
	 */
	public void setMilstarBucket(boolean pMilstarBucket) {
		mMilstarBucket = pMilstarBucket;
	}

	/**
	 * @return the milstarBucket
	 */
	public boolean isMilstarBucket() {
		return mMilstarBucket;
	}


	/**
	 * @return the openInvoiceLineTotal
	 */
	public BigDecimal getOpenInvoiceLineTotal() {
		return mOpenInvoiceLineTotal;
	}

	/**
	 * @param pOpenInvoiceLineTotal
	 *            the openInvoiceLineTotal to set
	 */
	public void setOpenInvoiceLineTotal(BigDecimal pOpenInvoiceLineTotal) {
		mOpenInvoiceLineTotal = pOpenInvoiceLineTotal;
	}

	/**
	 * @return the mInvoiceChargeTotal
	 */
	public BigDecimal getInvoiceChargeTotal() {
		return mInvoiceChargeTotal;
	}

	/**
	 * @param p_mInvoiceChargeTotal
	 *            the mInvoiceChargeTotal to set
	 */
	public void setInvoiceChargeTotal(BigDecimal p_invoiceChargeTotal) {
		mInvoiceChargeTotal = p_invoiceChargeTotal;
	}

	/**
	 * @return the mOpenInvoices
	 */
	public PriorityQueue<InvoiceLine> getOpenInvoiceLines() {
		return mOpenInvoiceLines;
	}

	/**
	 * @param mOpenInvoices
	 *            the mOpenInvoices to set
	 */
	public void setOpenInvoiceLines(PriorityQueue<InvoiceLine> mOpenInvoices) {
		this.mOpenInvoiceLines = mOpenInvoices;
	}

	/**
	 * @return the mMilitaryUniformLines
	 */
	public PriorityQueue<InvoiceLine> getMilitaryUniformLines() {
		return mMilitaryUniformLines;
	}

	/**
	 * @param mMilitaryUniformLines
	 *            the mMilitaryUniformLines to set
	 */
	public void setMilitaryUniformLines(
			PriorityQueue<InvoiceLine> mMilitaryUniformLines
	)
	{
		this.mMilitaryUniformLines = mMilitaryUniformLines;
	}

	/**
	 * @return the mMilitaryPromoLines
	 */
	public PriorityQueue<InvoiceLine> getMilitaryPromoLines() {
		return mMilitaryPromoLines;
	}

	/**
	 * @param mMilitaryPromoLines
	 *            the mMilitaryPromoLines to set
	 */
	public void setMilitaryPromoLines(
			PriorityQueue<InvoiceLine> mMilitaryPromoLines
	)
	{
		this.mMilitaryPromoLines = mMilitaryPromoLines;
	}

	/**
	 * @return the mMilitaryUninformLinesTotal
	 */
	public BigDecimal getMilitaryUninformLinesTotal() {
		return mMilitaryUninformLinesTotal;
	}

	/**
	 * @param mMilitaryUninformLinesTotal
	 *            the mMilitaryUninformLinesTotal to set
	 */
	public void setMilitaryUninformLinesTotal(
			BigDecimal mMilitaryUninformLinesTotal
	)
	{
		this.mMilitaryUninformLinesTotal = mMilitaryUninformLinesTotal;
	}

	/**
	 * @return the mMilitaryPromoLinesTotalDouble
	 */
	public BigDecimal getMilitaryPromoLinesTotalDouble() {
		return this.mMilitaryPromoLinesTotalDouble;
	}

	/**
	 * @param mMilitaryPromoLinesTotalDouble
	 *            the mMilitaryPromoLinesTotalBigDecimal to set
	 */
	public void setMilitaryPromoLinesTotalDouble(
			BigDecimal mMilitaryPromoLinesTotalDouble
	)
	{
		this.mMilitaryPromoLinesTotalDouble = mMilitaryPromoLinesTotalDouble;
	}

	/**
	 * @return the settlementCCMethods
	 */
	public Stack<PaymentMethod> getSettlementCCMethods() {
		return this.mSettlementCCMethods;
	}

	/**
	 * @param pSettlementCCMethods
	 *            the settlementCCMethods to set
	 */
	public void
			setSettlementCCMethods(Stack<PaymentMethod> pSettlementCCMethods)
	{
		mSettlementCCMethods = pSettlementCCMethods;
	}

	/**
	 * @return the settlementGCMethods
	 */
	public PriorityQueue<PaymentMethod> getSettlementGCMethods() {
		return mSettlementGCMethods;
	}

	/**
	 * @param pSettlementGCMethods
	 *            the settlementGCMethods to set
	 */
	public void setSettlementGCMethods(
			PriorityQueue<PaymentMethod> pSettlementGCMethods
	)
	{
		mSettlementGCMethods = pSettlementGCMethods;
	}

	/**
	 * @return the totalPaymentUniformAuth
	 */
	public BigDecimal getTotalPaymentUniformAuth() {
		return mTotalPaymentUniformAuth;
	}

	/**
	 * @param pTotalPaymentUniformAuth
	 *            the totalPaymentUniformAuth to set
	 */
	public void
			setTotalPaymentUniformAuth(BigDecimal pTotalPaymentUniformAuth)
	{
		mTotalPaymentUniformAuth = pTotalPaymentUniformAuth;
	}

	/**
	 * @return the totalPaymentUniformSettled
	 */
	public BigDecimal getTotalPaymentUniformSettled() {
		return mTotalPaymentUniformSettled;
	}

	/**
	 * @param pTotalPaymentUniformSettled
	 *            the totalPaymentUniformSettled to set
	 */
	public void setTotalPaymentUniformSettled(
			BigDecimal pTotalPaymentUniformSettled
	)
	{
		mTotalPaymentUniformSettled = pTotalPaymentUniformSettled;
	}

	/**
	 * @return the totalPaymentRetailAuth
	 */
	public BigDecimal getTotalPaymentRetailAuth() {
		return mTotalPaymentRetailAuth;
	}

	/**
	 * @param pTotalPaymentRetailAuth
	 *            the totalPaymentRetailAuth to set
	 */
	public void setTotalPaymentRetailAuth(BigDecimal pTotalPaymentRetailAuth) {
		mTotalPaymentRetailAuth = pTotalPaymentRetailAuth;
	}

	/**
	 * @return the totalPaymentRetailSettled
	 */
	public BigDecimal getTotalPaymentRetailSettled() {
		return mTotalPaymentRetailSettled;
	}

	/**
	 * @param pTotalPaymentRetailSettled
	 *            the totalPaymentRetailSettled to set
	 */
	public void setTotalPaymentRetailSettled(
			BigDecimal pTotalPaymentRetailSettled
	)
	{
		mTotalPaymentRetailSettled = pTotalPaymentRetailSettled;
	}

	/**
	 * @return the totalPaymentSettled
	 */
	public BigDecimal getTotalPaymentSettled() {
		return mTotalPaymentSettled;
	}

	/**
	 * @param pTotalPaymentSettled
	 *            the totalPaymentSettled to set
	 */
	public void setTotalPaymentSettled(BigDecimal pTotalPaymentSettled) {
		mTotalPaymentSettled = pTotalPaymentSettled;
	}

	/**
	 * @return the totalPaymentAuth
	 */
	public BigDecimal getTotalPaymentAuth() {
		return mTotalPaymentAuth;
	}

	/**
	 * @param pTotalPaymentAuth
	 *            the totalPaymentAuth to set
	 */
	public void setTotalPaymentAuth(BigDecimal pTotalPaymentAuth) {
		mTotalPaymentAuth = pTotalPaymentAuth;
	}

	/**
	 * @return the shippingCharge
	 */
	public BigDecimal getShippingCharge() {
		return mShippingCharge;
	}

	/**
	 * @param pShippingCharge
	 *            the shippingCharge to set
	 */
	public void setShippingCharge(BigDecimal pShippingCharge) {
		mShippingCharge = pShippingCharge;
	}

	/**
	 * @return the totalMUReturnedAmount
	 */
	public BigDecimal getTotalMUReturnedAmount() {
		return mTotalMUReturnedAmount;
	}

	/**
	 * @param pTotalMUReturnedAmount
	 *            the totalMUReturnedAmount to set
	 */
	public void setTotalMUReturnedAmount(BigDecimal pTotalMUReturnedAmount) {
		mTotalMUReturnedAmount = pTotalMUReturnedAmount;
	}

	/**
	 * @return the totalMRReturnedAmount
	 */
	public BigDecimal getTotalMRReturnedAmount() {
		return mTotalMRReturnedAmount;
	}

	/**
	 * @param pTotalMRReturnedAmount
	 *            the totalMRReturnedAmount to set
	 */
	public void setTotalMRReturnedAmount(BigDecimal pTotalMRReturnedAmount) {
		mTotalMRReturnedAmount = pTotalMRReturnedAmount;
	}

	/**
	 * @return the totalReturnedAmount
	 */
	public BigDecimal getTotalReturnedAmount() {
		return mTotalReturnedAmount;
	}

	/**
	 * @param pTotalReturnedAmount
	 *            the totalReturnedAmount to set
	 */
	public void setTotalReturnedAmount(BigDecimal pTotalReturnedAmount) {
		mTotalReturnedAmount = pTotalReturnedAmount;
	}

	/**
	 * @return the totalMUOrderedAmount
	 */
	public BigDecimal getTotalMUOrderedAmount() {
		return mTotalMUOrderedAmount;
	}

	/**
	 * @param pTotalMUOrderedAmount
	 *            the totalMUOrderedAmount to set
	 */
	public void setTotalMUOrderedAmount(BigDecimal pTotalMUOrderedAmount) {
		mTotalMUOrderedAmount = pTotalMUOrderedAmount;
	}

	/**
	 * @return the totalMROrderedAmount
	 */
	public BigDecimal getTotalMROrderedAmount() {
		return mTotalMROrderedAmount;
	}

	/**
	 * @param pTotalMROrderedAmount
	 *            the totalMROrderedAmount to set
	 */
	public void setTotalMROrderedAmount(BigDecimal pTotalMROrderedAmount) {
		mTotalMROrderedAmount = pTotalMROrderedAmount;
	}

	/**
	 * @return the totalOrderedAmount
	 */
	public BigDecimal getTotalOrderedAmount() {
		return mTotalOrderedAmount;
	}

	/**
	 * @param pTotalOrderedAmount
	 *            the totalOrderedAmount to set
	 */
	public void setTotalOrderedAmount(BigDecimal pTotalOrderedAmount) {
		mTotalOrderedAmount = pTotalOrderedAmount;
	}

	/**
	 * @return the milstarUniformCard
	 */
	public PaymentMethod getMilstarUniformCard() {
		return mMilstarUniformCard;
	}

	/**
	 * @param pMilstarUniformCard
	 *            the milstarUniformCard to set
	 */
	public void setMilstarUniformCard(PaymentMethod pMilstarUniformCard) {
		mMilstarUniformCard = pMilstarUniformCard;
	}

	/**
	 * @return the milstarRetailCard
	 */
	public PaymentMethod getMilstarRetailCard() {
		return mMilstarRetailCard;
	}

	/**
	 * @param pMilstarRetailCard
	 *            the milstarRetailCard to set
	 */
	public void setMilstarRetailCard(PaymentMethod pMilstarRetailCard) {
		mMilstarRetailCard = pMilstarRetailCard;
	}

	/**
	 * @return the shippingChargeQueue
	 */
	public PriorityQueue<InvoiceChargeDetail> getShippingChargeQueue() {
		return mShippingChargeQueue;
	}

	/**
	 * @param pShippingChargeQueue
	 *            the shippingChargeQueue to set
	 */
	public void setShippingChargeQueue(
			PriorityQueue<InvoiceChargeDetail> pShippingChargeQueue
	)
	{
		mShippingChargeQueue = pShippingChargeQueue;
	}

	/**
	 * @return the mPaymentMethodResponses
	 */
	public Map<String, PaymentMethodResponse> getPaymentMethodResponses() {
		return mPaymentMethodResponses;
	}

	/**
	 * @param mPaymentMethodResponses
	 *            the mPaymentMethodResponses to set
	 */
	public void setPaymentMethodResponses(
			Map<String, PaymentMethodResponse> mPaymentMethodResponses
	)
	{
		this.mPaymentMethodResponses = mPaymentMethodResponses;
	}
}
