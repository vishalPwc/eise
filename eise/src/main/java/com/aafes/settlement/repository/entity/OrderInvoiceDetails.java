package com.aafes.settlement.repository.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import com.aafes.settlement.constant.Constants;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@Entity
@Table(name = "ORDER_INVOICE_DETAILS")
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class OrderInvoiceDetails
	extends
	BaseEntity
{

	// ------------------------------------------------------------------------

	// @Id
	// @GeneratedValue(strategy = GenerationType.IDENTITY)
	// @Column(name = "ID", unique = true)
	// private int orderInvoiceDetailsId;

	@Column(name = "INVOICE_ID")
	private String				invoiceId;

	@Column(name = "ORDER_ID")
	private String				orderId;

	@JsonFormat(pattern = Constants.ISO_TIME_FORMAT)
	@Column(name = "ORDER_DATE")
	private LocalDateTime		orderDate;

	@Column(name = "TRANSACTION_TYPE")
	private String				tranType;

	// @Column(name = "INVOICE_ID")
	// private String invoiceId;

	@Column(name = "AMOUNT")
	private BigDecimal			amount;

	@Column(name = "AUDIT_UUID")
	private String				auditUUID;

	@Transient
	@JsonSerialize
	@JsonDeserialize
	private Boolean				failed;

	// @Column(name = "INVOICE_TYPE")
	// private String invoiceType;
	//
	@JsonManagedReference
	@OneToMany(
			mappedBy = "orderInvoiceDetails",
			cascade = CascadeType.ALL,
			fetch = FetchType.EAGER)
	@Fetch(FetchMode.SELECT)
	List<InvoicePaymentMethods>	invoicePaymentMethodsList = new ArrayList<
			InvoicePaymentMethods>();

	// -------------------------------------------------------------------------
	/**
	 * @return the tranUUID
	 */
	public String getInvoiceId() {
		return invoiceId;
	}

	/**
	 * @param tranUUID
	 *            the tranUUID to set
	 */
	public void setInvoiceId(String invoiceId) {
		this.invoiceId = invoiceId;
	}

	/**
	 * @return the orderId
	 */
	public String getOrderId() {
		return orderId;
	}

	/**
	 * @param orderId
	 *            the orderId to set
	 */
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	/**
	 * @return the orderDate
	 */
	public LocalDateTime getOrderDate() {
		return orderDate;
	}

	/**
	 * @param orderDate
	 *            the orderDate to set
	 */
	public void setOrderDate(LocalDateTime orderDate) {
		this.orderDate = orderDate;
	}

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
	 * @return the amount
	 */
	public BigDecimal getAmount() {
		return amount;
	}

	/**
	 * @param amount
	 *            the amount to set
	 */
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	/**
	 * @return the auditUUID
	 */
	public String getAuditUUID() {
		return auditUUID;
	}

	/**
	 * @param auditUUID
	 *            the auditUUID to set
	 */
	public void setAuditUUID(String auditUUID) {
		this.auditUUID = auditUUID;
	}

	/**
	 * @return the invoicePaymentMethodsList
	 */
	public List<InvoicePaymentMethods> getInvoicePaymentMethodsList() {
		return invoicePaymentMethodsList;
	}

	/**
	 * @param invoicePaymentMethodsList
	 *            the invoicePaymentMethodsList to set
	 */
	public void setInvoicePaymentMethodsList(
			List<InvoicePaymentMethods> invoicePaymentMethodsList
	)
	{
		this.invoicePaymentMethodsList = invoicePaymentMethodsList;
	}

	/**
	 * @return the status
	 */
	public Boolean getFailed() {
		failed = false;
		if (amount != null && amount.equals(Constants.MINUS_ONE_VALUE)) {
			failed = true;
		}
		return failed;
	}

	/**
	 * @param status
	 *            the status to set
	 */
	public void setFailed(Boolean failed) {
		this.failed = failed;
	}

	// ----------------------------------------------------------------------- -
}
