
package com.aafes.settlement.repository.entity;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonInclude;

@Entity
@Table(name = "INVOICE_ITEM_DETAILS")
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class InvoiceItemDetails
	extends
	BaseTranEntity
{
	// ------------------------------------------------------------------------

	@Column(name = "TRANSACTION_TYPE")
	private String				  tranType;

	@Column(name = "INVOICE_ID")
	private String				  invoiceId;

	@Column(name = "INVOICE_LINE_ID")
	private String				  invoiceLineId;

	@Column(name = "INVOICE_ITEM_ID")
	private String				  invoiceItemId;

	@Column(name = "RESPONSE_PLAN")
	private String				  responsePlan;

	@Column(name = "PROMO_PLAN")
	private String				  promoPlan;

	@Column(name = "QUANTITY")
	private BigDecimal			  quantity;

	@Column(name = "PAYMENT_AMOUNT")
	private BigDecimal			  paymentAmount;

	@Column(name = "PAYMENT_METHOD_ID")
	private String				  paymentMethodId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "INVOICE_PAYMENT_METHODS_ID")
	@JsonBackReference
	private InvoicePaymentMethods invoicePaymentMethod;

	// -------------------------------------------------------------------------

	/**
	 * @return the tranType
	 */
	public String getTranType() {
		return tranType;
	}

	/**
	 * @param tranType
	 *            the tranType to set
	 */
	public void setTranType(String tranType) {
		this.tranType = tranType;
	}

	/**
	 * @return the invoiceId
	 */
	public String getInvoiceId() {
		return invoiceId;
	}

	/**
	 * @param invoiceId
	 *            the invoiceId to set
	 */
	public void setInvoiceId(String invoiceId) {
		this.invoiceId = invoiceId;
	}

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
	 * @return the invoiceItemId
	 */
	public String getInvoiceItemId() {
		return invoiceItemId;
	}

	/**
	 * @param invoiceItemId
	 *            the invoiceItemId to set
	 */
	public void setInvoiceItemId(String invoiceItemId) {
		this.invoiceItemId = invoiceItemId;
	}

	/**
	 * @return the responsePlan
	 */
	public String getResponsePlan() {
		return responsePlan;
	}

	/**
	 * @param responsePlan
	 *            the responsePlan to set
	 */
	public void setResponsePlan(String responsePlan) {
		this.responsePlan = responsePlan;
	}

	/**
	 * @return the promoPlan
	 */
	public String getPromoPlan() {
		return promoPlan;
	}

	/**
	 * @param promoPlan
	 *            the promoPlan to set
	 */
	public void setPromoPlan(String promoPlan) {
		this.promoPlan = promoPlan;
	}

	/**
	 * @return the quantity
	 */
	public BigDecimal getQuantity() {
		return quantity;
	}

	/**
	 * @param quantity
	 *            the quantity to set
	 */
	public void setQuantity(BigDecimal quantity) {
		this.quantity = quantity;
	}

	/**
	 * @return the paymentAmount
	 */
	public BigDecimal getPaymentAmount() {
		return paymentAmount;
	}

	/**
	 * @param paymentAmount
	 *            the paymentAmount to set
	 */
	public void setPaymentAmount(BigDecimal paymentAmount) {
		this.paymentAmount = paymentAmount;
	}

	public String getPaymentMethodId() {
		return paymentMethodId;
	}

	public void setPaymentMethodId(String paymentMethodId) {
		this.paymentMethodId = paymentMethodId;
	}

	/**
	 * @return the invoicePaymentMethod
	 */
	public InvoicePaymentMethods getInvoicePaymentMethod() {
		return invoicePaymentMethod;
	}

	/**
	 * @param invoicePaymentMethod
	 *            the invoicePaymentMethod to set
	 */
	public void setInvoicePaymentMethod(
			InvoicePaymentMethods invoicePaymentMethod
	)
	{
		this.invoicePaymentMethod = invoicePaymentMethod;
	}

	// ------------------------------------------------------------------------
}
