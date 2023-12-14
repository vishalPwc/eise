package com.aafes.settlement.core.invoice;

import java.math.BigDecimal;
import java.util.Comparator;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class InvoiceChargeDetail implements Comparator<InvoiceChargeDetail> {
	// -----------------------------------------------------------------------------------
	@JsonProperty("ChargeDetailId")
	private String chargeDetailId;
	@JsonProperty("ChargeTotal")
	private BigDecimal chargeTotal;
	@JsonProperty("OriginalChargeAmount")
	private BigDecimal originalChargeAmount;
	@JsonProperty("ChargeType")
	private InvoiceChargeType chargeType;
	@JsonProperty("InvoiceId")
	private String invoiceId;
	@JsonProperty("InvoiceLineId")
	private String invoiceLineId;
	// used as extended user defined property for Adjustment Request
	@JsonProperty("PlanNumber")
	private String planNumber = "";

	// -----------------------------------------------------------------------------------
	/**
	 * @return the invoiceLineId
	 */
	public String getInvoiceLineId() {
		return invoiceLineId;
	}

	/**
	 * @param pInvoiceLineId
	 *            the invoiceLineId to set
	 */
	public void setInvoiceLineId(String pInvoiceLineId) {
		invoiceLineId = pInvoiceLineId;
	}

	/**
	 * @return the invoiceId
	 */
	public String getInvoiceId() {
		return invoiceId;
	}

	/**
	 * @param pInvoiceId
	 *            the invoiceId to set
	 */
	public void setInvoiceId(String pInvoiceId) {
		invoiceId = pInvoiceId;
	}

	/**
	 * @return the chargeDetailId
	 */
	public String getChargeDetailId() {
		return chargeDetailId;
	}

	/**
	 * @param chargeDetailId
	 *            the chargeDetailId to set
	 */
	public void setChargeDetailId(String chargeDetailId) {
		this.chargeDetailId = chargeDetailId;
	}

	/**
	 * @return the chargeTotal
	 */
	public BigDecimal getChargeTotal() {
		return chargeTotal;
	}

	/**
	 * @param chargeTotal
	 *            the chargeTotal to set
	 */
	public void setChargeTotal(BigDecimal chargeTotal) {
		if (chargeTotal != null)
			this.chargeTotal = chargeTotal.abs();
		else
			this.chargeTotal = chargeTotal;
	}

	/**
	 * @return the originalChargeAmount
	 */
	public BigDecimal getOriginalChargeAmount() {
		return originalChargeAmount;
	}

	/**
	 * @param originalChargeAmount
	 *            the originalChargeAmount to set
	 */
	public void setOriginalChargeAmount(BigDecimal originalChargeAmount) {
		this.originalChargeAmount = originalChargeAmount;
	}

	/**
	 * @return the chargeType
	 */
	public InvoiceChargeType getChargeType() {
		return chargeType;
	}

	/**
	 * @param chargeType
	 *            the chargeType to set
	 */
	public void setChargeType(InvoiceChargeType chargeType) {
		this.chargeType = chargeType;
	}

	/**
	 * compare Invoice Line amount to set priority
	 * 
	 * @param pO1
	 * @param pO2
	 * @return
	 */
	@Override
	public int compare(InvoiceChargeDetail pO1, InvoiceChargeDetail pO2) {
		// overriding the method to set the priority
		return pO1.getChargeTotal().compareTo((pO2.getChargeTotal()));
		// if (pO1.getChargeTotal() < pO2.getChargeTotal())
		// return -1;
		// else
		// return 1;
	}

	/**
	 * @return the planNumber
	 */
	public String getPlanNumber() {
		return planNumber;
	}

	/**
	 * @param pPlanNumber
	 *            the planNumber to set
	 */
	public void setPlanNumber(String pPlanNumber) {
		planNumber = pPlanNumber;
	}
}
// -----------------------------------------------------------------------------------
// END OF FILE
// -----------------------------------------------------------------------------------