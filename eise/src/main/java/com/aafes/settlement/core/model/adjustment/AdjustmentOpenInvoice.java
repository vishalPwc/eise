package com.aafes.settlement.core.model.adjustment;

import java.math.BigDecimal;
import java.util.List;

import com.aafes.settlement.core.invoice.InvoiceChargeDetail;
import com.aafes.settlement.core.invoice.InvoiceType;
import com.fasterxml.jackson.annotation.JsonProperty;

public class AdjustmentOpenInvoice

{
	@JsonProperty("InvoiceId")
	private String invoiceId;

	@JsonProperty("OrderId")
	private String orderId;

	@JsonProperty("ParentOrderId")
	private String parentOrderId;

	@JsonProperty("InvoiceTotal")
	private BigDecimal invoiceTotal;

	@JsonProperty("InvoiceType")
	private InvoiceType invoiceType;

	@JsonProperty("InvoiceChargeDetail")
	private List<InvoiceChargeDetail> invoiceChargeDetail = null;

	@JsonProperty("InvoiceLine")
	private List<AdjustmentInvoiceLine> invoiceLine = null;

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
	 * @return the orderId
	 */
	public String getOrderId() {
		return orderId;
	}

	/**
	 * @param pOrderId
	 *            the orderId to set
	 */
	public void setOrderId(String pOrderId) {
		orderId = pOrderId;
	}

	/**
	 * @return the parentOrderId
	 */
	public String getParentOrderId() {
		return parentOrderId;
	}

	/**
	 * @param pParentOrderId
	 *            the parentOrderId to set
	 */
	public void setParentOrderId(String pParentOrderId) {
		parentOrderId = pParentOrderId;
	}

	/**
	 * @return the invoiceTotal
	 */
	public BigDecimal getInvoiceTotal() {
		return invoiceTotal;
	}

	/**
	 * @param pInvoiceTotal
	 *            the invoiceTotal to set
	 */
	public void setInvoiceTotal(BigDecimal pInvoiceTotal) {
		invoiceTotal = pInvoiceTotal;
	}

	/**
	 * @return the invoiceType
	 */
	public InvoiceType getInvoiceType() {
		return invoiceType;
	}

	/**
	 * @param pInvoiceType
	 *            the invoiceType to set
	 */
	public void setInvoiceType(InvoiceType pInvoiceType) {
		invoiceType = pInvoiceType;
	}

	/**
	 * @return the invoiceChargeDetail
	 */
	public List<InvoiceChargeDetail> getInvoiceChargeDetail() {
		return invoiceChargeDetail;
	}

	/**
	 * @param pInvoiceChargeDetail
	 *            the invoiceChargeDetail to set
	 */
	public void setInvoiceChargeDetail(
			List<InvoiceChargeDetail> pInvoiceChargeDetail) {
		invoiceChargeDetail = pInvoiceChargeDetail;
	}

	/**
	 * @return the invoiceLine
	 */
	public List<AdjustmentInvoiceLine> getInvoiceLine() {
		return invoiceLine;
	}

	/**
	 * @param pInvoiceLine
	 *            the invoiceLine to set
	 */
	public void setInvoiceLine(List<AdjustmentInvoiceLine> pInvoiceLine) {
		invoiceLine = pInvoiceLine;
	}

}
