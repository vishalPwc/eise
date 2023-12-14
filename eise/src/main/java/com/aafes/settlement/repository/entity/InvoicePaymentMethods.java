package com.aafes.settlement.repository.entity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@Entity
@Table(name = "INVOICE_PAYMENT_METHODS")
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class InvoicePaymentMethods
	extends
	BaseTranEntity
{

	// ------------------------------------------------------------------------

	@Column(name = "PAYMENT_METHOD_TYPE")
	private String				paymentMethodType;

	@Column(name = "PAYMENT_METHOD_ID")
	private String				paymentMethodId;

	@Column(name = "CARD_TYPE")
	private String				cardType;

	@Column(name = "RESPONSE_PLAN")
	private String				responsePlan;

	@Column(name = "ORDER_ID")
	private String				orderId;

	@Column(name = "INVOICE_ID")
	private String				invoiceId;

	@Column(name = "CURRENT_SETTLED")
	private BigDecimal			currentSettled;

	@Column(name = "CURRENT_AUTH")
	private BigDecimal			currentAuth;

	@Transient
	@JsonSerialize
	@JsonDeserialize
	private BigDecimal			total;

	@JsonBackReference
	@ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@Fetch(FetchMode.SELECT)
	@JoinColumn(
			name = "ORDER_INVOICE_DETAILS_ID",
			referencedColumnName = "ID",
			insertable = true,
			updatable = true)
	private OrderInvoiceDetails	orderInvoiceDetails;

	@JsonManagedReference
	@OneToMany(
			mappedBy = "invoicePaymentMethod",
			cascade = CascadeType.ALL,
			fetch = FetchType.EAGER)
	@Fetch(value = FetchMode.SUBSELECT)
	List<InvoiceItemDetails>	invoiceItemDetailsList	 = new ArrayList<
			InvoiceItemDetails>();

	@JsonManagedReference
	@OneToMany(
			mappedBy = "invoicePaymentMethod",
			cascade = CascadeType.ALL,
			fetch = FetchType.EAGER)
	@Fetch(value = FetchMode.SUBSELECT)
	List<InvoiceChargeDetails>	invoiceChargeDetailsList = new ArrayList<
			InvoiceChargeDetails>();

	// ------------------------------------------------------------------------
	/**
	 * @return the paymentMethodType
	 */

	public String getPaymentMethodType() {
		return paymentMethodType;
	}

	/**
	 * @param p_paymentMethodType
	 *            the paymentMethodType to set
	 */

	public void setPaymentMethodType(String p_paymentMethodType) {
		paymentMethodType = p_paymentMethodType;
	}

	/**
	 * @return the paymentMethodId
	 */

	public String getPaymentMethodId() {
		return paymentMethodId;
	}

	/**
	 * @param p_paymentMethodId
	 *            the paymentMethodId to set
	 */

	public void setPaymentMethodId(String p_paymentMethodId) {
		paymentMethodId = p_paymentMethodId;
	}

	/**
	 * @return the cardType
	 */

	public String getCardType() {
		return cardType;
	}

	/**
	 * @param p_cardType
	 *            the cardType to set
	 */

	public void setCardType(String p_cardType) {
		cardType = p_cardType;
	}

	/**
	 * @return
	 */
	public String getResponsePlan() {
		return responsePlan;
	}

	/**
	 * @param responsePlan
	 */
	public void setResponsePlan(String responsePlan) {
		this.responsePlan = responsePlan;
	}

	/**
	 * @return
	 */
	public String getOrderId() {
		return orderId;
	}

	/**
	 * @param orderId
	 */
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	/**
	 * @return
	 */
	public String getInvoiceId() {
		return invoiceId;
	}

	/**
	 * @param invoiceId
	 */
	public void setInvoiceId(String invoiceId) {
		this.invoiceId = invoiceId;
	}

	/**
	 * @return
	 */
	public BigDecimal getCurrentSettled() {
		return currentSettled;
	}

	/**
	 * @param currentSettled
	 */
	public void setCurrentSettled(BigDecimal currentSettled) {
		this.currentSettled = currentSettled;
	}

	/**
	 * @return
	 */
	public BigDecimal getCurrentAuth() {
		return currentAuth;
	}

	/**
	 * @param currentAuth
	 */
	public void setCurrentAuth(BigDecimal currentAuth) {
		this.currentAuth = currentAuth;
	}

	/**
	 * @return
	 */
	public OrderInvoiceDetails getOrderInvoiceDetails() {
		return orderInvoiceDetails;
	}

	/**
	 * @param orderInvoiceDetails
	 */
	public void setOrderInvoiceDetails(
			OrderInvoiceDetails orderInvoiceDetails
	)
	{
		this.orderInvoiceDetails = orderInvoiceDetails;
	}

	/**
	 * @return
	 */
	public List<InvoiceItemDetails> getInvoiceItemDetailsList() {
		return invoiceItemDetailsList;
	}

	/**
	 * @param invoiceItemDetailsList
	 */
	public void setInvoiceItemDetailsList(
			List<InvoiceItemDetails> invoiceItemDetailsList
	)
	{
		this.invoiceItemDetailsList = invoiceItemDetailsList;
	}

	/**
	 * @return the invoiceChargeDetailsList
	 */
	public List<InvoiceChargeDetails> getInvoiceChargeDetailsList() {
		return invoiceChargeDetailsList;
	}

	/**
	 * @param invoiceChargeDetailsList
	 *            the invoiceChargeDetailsList to set
	 */
	public void setInvoiceChargeDetailsList(
			List<InvoiceChargeDetails> invoiceChargeDetailsList
	)
	{
		this.invoiceChargeDetailsList = invoiceChargeDetailsList;
	}

	/**
	 * @return the total
	 */
	public BigDecimal getTotal() {
		return total;
	}

	/**
	 * @param total
	 *            the total to set
	 */
	public void setTotal(BigDecimal total) {
		this.total = total;
	}

	// ------------------------------------------------------------------------
}