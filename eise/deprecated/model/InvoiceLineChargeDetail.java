package com.aafes.settlement.Invoice.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class InvoiceLineChargeDetail {

	@JsonProperty("InvoiceLineId")
	private String							  invoiceLineId;
	@JsonProperty("ChargeDetailId")
	private String							  chargeDetailId;
	@JsonProperty("ChargeType")
	private InvoiceLineChargeDetailChargeType chargeType;
	@JsonProperty("ChargeTotal")
	private float							  chargeTotal;
	@JsonProperty("OriginalChargeAmount")
	private float							  originalChargeAmount;

	private float							  interimOrderLineTotal;

	/**
	 * @return the invoiceLineId
	 */
	public String getInvoiceLineId() {
		return invoiceLineId;
	}

	/**
	 * @param invoiceLineId
	 *            the invoiceLineId to set
	 */
	public void setInvoiceLineId(String invoiceLineId) {
		this.invoiceLineId = invoiceLineId;
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
	 * @return the chargeType
	 */
	public InvoiceLineChargeDetailChargeType getChargeType() {
		return chargeType;
	}

	/**
	 * @param chargeType
	 *            the chargeType to set
	 */
	public void setChargeType(InvoiceLineChargeDetailChargeType chargeType) {
		this.chargeType = chargeType;
	}

	/**
	 * @return the chargeTotal
	 */
	public float getChargeTotal() {
		return chargeTotal;
	}

	/**
	 * @param chargeTotal
	 *            the chargeTotal to set
	 */
	public void setChargeTotal(float chargeTotal) {
		this.chargeTotal = chargeTotal;
		setInterimOrderLineTotal(chargeTotal);
	}

	/**
	 * @return the originalChargeAmount
	 */
	public float getOriginalChargeAmount() {
		return originalChargeAmount;
	}

	/**
	 * @param originalChargeAmount
	 *            the originalChargeAmount to set
	 */
	public void setOriginalChargeAmount(float originalChargeAmount) {
		this.originalChargeAmount = originalChargeAmount;
	}

	/**
	 * @return the interimOrderLineTotal
	 */
	public float getInterimOrderLineTotal() {
		return interimOrderLineTotal;
	}

	/**
	 * @param interimOrderLineTotal
	 *            the interimOrderLineTotal to set
	 */
	public void setInterimOrderLineTotal(float interimOrderLineTotal) {
		this.interimOrderLineTotal = interimOrderLineTotal;
	}

}
