package com.aafes.settlement.util;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.aafes.settlement.core.invoice.InvoiceChargeDetail;
import com.aafes.settlement.core.invoice.InvoiceLine;
import com.aafes.settlement.core.invoice.OpenAuthReversal;
import com.aafes.settlement.core.response.PaymentMethodResponse;

public class CalculationUtil {

	private BigDecimal						remainingInvoiceAmountToSettle = new BigDecimal(0.0);
	private BigDecimal						amountMUAvailableForSettlement = new BigDecimal(0.0);
	private BigDecimal						amountMRAvailableForSettlement = new BigDecimal(0.0);
	private InvoiceLine					remainingInvoiceLineToSettle;
	private InvoiceChargeDetail			remainingInvoiceChargeToSettle;
	private OpenAuthReversal			remainingOpenAuthToReverse;
	private List<
			PaymentMethodResponse>		paymentMethodResponses		   = new ArrayList<
					PaymentMethodResponse>();
	private PaymentMethodResponse		uniformMethodResponse		   = new PaymentMethodResponse();
	private PaymentMethodResponse		retailMethodResponse		   = new PaymentMethodResponse();

	/**
	 * @return the remainingInvoiceAmountToSettle
	 */
	public BigDecimal getRemainingInvoiceAmountToSettle() {
		return remainingInvoiceAmountToSettle;
	}

	/**
	 * @param p_remainigInvoiceAmountToSettle
	 *            the remainingInvoiceAmountToSettle to set
	 */
	public void setRemainingInvoiceAmountToSettle(
			BigDecimal p_remainigInvoiceAmountToSettle
	)
	{
		remainingInvoiceAmountToSettle = p_remainigInvoiceAmountToSettle;
	}

	/**
	 * @return the amountMUAvailableForSettlement
	 */
	public BigDecimal getAmountMUAvailableForSettlement() {
		return amountMUAvailableForSettlement;
	}

	/**
	 * @param p_amountMUAvailableForSettlement
	 *            the amountMUAvailableForSettlement to set
	 */
	public void setAmountMUAvailableForSettlement(
			BigDecimal p_amountMUAvailableForSettlement
	)
	{
		amountMUAvailableForSettlement = p_amountMUAvailableForSettlement;
	}

	/**
	 * @return the amountMRAvailableForSettlement
	 */
	public BigDecimal getAmountMRAvailableForSettlement() {
		return amountMRAvailableForSettlement;
	}

	/**
	 * @param p_amountMRAvailableForSettlement
	 *            the amountMRAvailableForSettlement to set
	 */
	public void setAmountMRAvailableForSettlement(
			BigDecimal p_amountMRAvailableForSettlement
	)
	{
		amountMRAvailableForSettlement = p_amountMRAvailableForSettlement;
	}

	/**
	 * @return the remainingInvoiceLineToSettle
	 */
	public InvoiceLine getRemainingInvoiceLineToSettle() {
		return remainingInvoiceLineToSettle;
	}

	/**
	 * @param p_remainingInvoiceLineToSettle
	 *            the remainingInvoiceLineToSettle to set
	 */
	public void setRemainingInvoiceLineToSettle(
			InvoiceLine p_remainingInvoiceLineToSettle
	)
	{
		remainingInvoiceLineToSettle = p_remainingInvoiceLineToSettle;
	}

	/**
	 * @return the mPaymentMethodResponses
	 */
	public List<PaymentMethodResponse> getPaymentMethodResponses() {
		return paymentMethodResponses;
	}

	/**
	 * @param p_mPaymentMethodResponses
	 *            the mPaymentMethodResponses to set
	 */
	public void setPaymentMethodResponses(
			List<PaymentMethodResponse> p_paymentMethodResponses
	)
	{
		paymentMethodResponses = p_paymentMethodResponses;
	}

	/**
	 * @return the uniformMethodResponse
	 */
	public PaymentMethodResponse getUniformMethodResponse() {
		return uniformMethodResponse;
	}

	/**
	 * @param p_uniformMethodResponse
	 *            the uniformMethodResponse to set
	 */
	public void setUniformMethodResponse(
			PaymentMethodResponse p_uniformMethodResponse
	)
	{
		uniformMethodResponse = p_uniformMethodResponse;
	}

	/**
	 * @return the retailMethodResponse
	 */
	public PaymentMethodResponse getRetailMethodResponse() {
		return retailMethodResponse;
	}

	/**
	 * @return the remainingInvoiceChargeToSettle
	 */
	public InvoiceChargeDetail getRemainingInvoiceChargeToSettle() {
		return remainingInvoiceChargeToSettle;
	}

	/**
	 * @param pRemainingInvoiceChargeToSettle
	 *            the remainingInvoiceChargeToSettle to set
	 */
	public void setRemainingInvoiceChargeToSettle(
			InvoiceChargeDetail pRemainingInvoiceChargeToSettle
	)
	{
		remainingInvoiceChargeToSettle = pRemainingInvoiceChargeToSettle;
	}

	/**
	 * @param p_retailMethodResponse
	 *            the retailMethodResponse to set
	 */
	public void setRetailMethodResponse(
			PaymentMethodResponse p_retailMethodResponse
	)
	{
		retailMethodResponse = p_retailMethodResponse;
	}

	/**
	 * @return the remainingOpenAuthToReverse
	 */
	public OpenAuthReversal getRemainingOpenAuthToReverse() {
		return remainingOpenAuthToReverse;
	}

	/**
	 * @param pRemainingOpenAuthToReverse
	 *            the remainingOpenAuthToReverse to set
	 */
	public void setRemainingOpenAuthToReverse(
			OpenAuthReversal pRemainingOpenAuthToReverse
	)
	{
		remainingOpenAuthToReverse = pRemainingOpenAuthToReverse;
	}
}
