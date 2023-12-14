package com.aafes.settlement.repository;

import java.util.ArrayList;
import java.util.List;

import com.aafes.settlement.repository.entity.InvoiceChargeDetails;
import com.aafes.settlement.repository.entity.InvoiceItemDetails;
import com.aafes.settlement.repository.entity.InvoicePaymentMethods;
import com.aafes.settlement.repository.entity.OrderInvoiceDetails;

public class RepoContainer {
	// ------------------------------------------------------------------------
	private String	auditUUID		= null;

	private List<OrderInvoiceDetails>
		orderInvoiceData			= new ArrayList<OrderInvoiceDetails>();
	
	private List<InvoicePaymentMethods>	
		invoicePaymentMethodsList	= new ArrayList<InvoicePaymentMethods>();

	private List<InvoiceItemDetails>
		invoiceItemDetailsList		= new ArrayList<InvoiceItemDetails>();

	private List<InvoiceChargeDetails>
		invoiceChargeDetailsList	= new ArrayList<InvoiceChargeDetails>();

	private String	transactionType;
	// ------------------------------------------------------------------------
	/**
	 * @return the auditUUID
	 */
	public String getAuditUUID() {
		return auditUUID;
	}

	/**
	 * @param p_auditUUID the auditUUID to set
	 */
	public void setAuditUUID(String p_auditUUID) {
		auditUUID = p_auditUUID;
	}

	/**
	 * @return the orderInvoiceData
	 */
	public List<OrderInvoiceDetails> getOrderInvoiceData() {
		return orderInvoiceData;
	}

	/**
	 * @param p_orderInvoiceData the orderInvoiceData to set
	 */
	public void setOrderInvoiceData(List<OrderInvoiceDetails> p_orderInvoiceData) {
		orderInvoiceData = p_orderInvoiceData;
	}

	/**
	 * @return the invoicePaymentMethodsList
	 */
	public List<InvoicePaymentMethods> getInvoicePaymentMethodsList() {
		return invoicePaymentMethodsList;
	}

	/**
	 * @param p_invoicePaymentMethodsList the invoicePaymentMethodsList to set
	 */
	public void setInvoicePaymentMethodsList(
			List<InvoicePaymentMethods> p_invoicePaymentMethodsList
	)
	{
		invoicePaymentMethodsList = p_invoicePaymentMethodsList;
	}

	/**
	 * @return the invoiceItemDetailsList
	 */
	public List<InvoiceItemDetails> getInvoiceItemDetailsList() {
		return invoiceItemDetailsList;
	}

	/**
	 * @param p_invoiceItemDetailsList the invoiceItemDetailsList to set
	 */
	public void setInvoiceItemDetailsList(
			List<InvoiceItemDetails> p_invoiceItemDetailsList
	)
	{
		invoiceItemDetailsList = p_invoiceItemDetailsList;
	}

	/**
	 * @return the invoiceChargeDetailsList
	 */
	public List<InvoiceChargeDetails> getInvoiceChargeDetailsList() {
		return invoiceChargeDetailsList;
	}

	/**
	 * @param p_invoiceChargeDetailsList the invoiceChargeDetailsList to set
	 */
	public void setInvoiceChargeDetailsList(
			List<InvoiceChargeDetails> p_invoiceChargeDetailsList
	)
	{
		invoiceChargeDetailsList = p_invoiceChargeDetailsList;
	}

	/**
	 * @return the transactionType
	 */
	public String getTransactionType() {
		return transactionType;
	}

	/**
	 * @param p_transactionType the transactionType to set
	 */
	public void setTransactionType(String p_transactionType) {
		transactionType = p_transactionType;
	}
	// ------------------------------------------------------------------------
}
// ----------------------------------------------------------------------------
// END OF FILE
// ----------------------------------------------------------------------------