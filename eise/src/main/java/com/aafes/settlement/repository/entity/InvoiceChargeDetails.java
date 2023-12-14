package com.aafes.settlement.repository.entity;

import java.math.BigDecimal;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonInclude;

@Entity
@Table(name = "INVOICE_CHARGE_DETAILS")
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class InvoiceChargeDetails
	extends
	BaseTranEntity
{

	// ------------------------------------------------------------------------

	@Column(name = "CHARGE_TYPE")
	private String				  chargeType;

	@Column(name = "CHARGE_DETAIL_ID")
	private String				  chargeDetailsId;

	@Column(name = "PAYMENT_AMOUNT")
	private BigDecimal			  paymentAmount;

	@Column(name = "PAYMENT_METHOD_ID")
	private String				  paymentMethodId;

	@JsonBackReference
	@ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinColumn(
			name = "INVOICE_PAYMENT_METHODS_ID",
			referencedColumnName = "ID")
	private InvoicePaymentMethods invoicePaymentMethod;
	// ------------------------------------------------------------------------

	/**
	 * @return the chargeDetailsId
	 */
	public String getChargeDetailsId() {
		return chargeDetailsId;
	}

	/**
	 * @param chargeDetailsId
	 *            the chargeDetailsId to set
	 */
	public void setChargeDetailsId(String chargeDetailsId) {
		this.chargeDetailsId = chargeDetailsId;
	}

	/**
	 * @return the chargeType
	 */
	public String getChargeType() {
		return chargeType;
	}

	/**
	 * @param chargeType
	 *            the chargeType to set
	 */
	public void setChargeType(String chargeType) {
		this.chargeType = chargeType;
	}

	/**
	 * @return the paymentMethodId
	 */
	public String getPaymentMethodId() {
		return paymentMethodId;
	}

	/**
	 * @param paymentMethodId
	 *            the paymentMethodId to set
	 */
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

	// ------------------------------------------------------------------------

}
