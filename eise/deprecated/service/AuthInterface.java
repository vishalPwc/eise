package com.aafes.settlement.service;

import java.util.LinkedHashMap;

import com.aafes.settlement.Invoice.model.InvoiceLine;
import com.aafes.settlement.Invoice.model.OpenInvoice;
import com.aafes.settlement.Invoice.model.ResponseTender;
import com.aafes.settlement.Invoice.model.ReturnRequest;
import com.aafes.settlement.Invoice.model.SettlementRequest;

public interface AuthInterface {

	/**
	 * This method process the Shipment request received with respective IMPL .
	 * 
	 * @param p_invoiceLine
	 *            : the invoice of item received for shipment in request.
	 * @param p_openInvoice
	 *            : contains the array of invoice lines.
	 * @param p_settlementTender
	 *            : response object for openInvoices.
	 * @param l_authPlan
	 *            : Map which contains total amount of each order-line w.r.t to
	 *            the plans of items.
	 * @param p_settlementRequest
	 *            : the request received in API.
	 * @throws Exception
	 */
	public void processShipment(
			InvoiceLine p_invoiceLine,
			OpenInvoice p_openInvoice,
			ResponseTender p_settlementTender,
			LinkedHashMap<String, Float> l_authPlan,
			SettlementRequest p_settlementRequest
	)
			throws Exception;

	/**
	 * This method process the Return request received with respective IMPL .
	 * 
	 * @param p_invoiceLine
	 * @param l_refundTender
	 * @param p_returnRequest
	 * @param p_openInvoiceId
	 * @param p_invoiceType
	 * @throws Exception
	 */
	public void processReturns(
			InvoiceLine p_invoiceLine,
			ResponseTender l_refundTender,
			ReturnRequest p_returnRequest,
			String p_openInvoiceId,
			String p_invoiceType
	)
			throws Exception;

	/**
	 * This method process the Adjustment request received with respective IMPL
	 * .
	 * 
	 * @param p_invoiceLine
	 * @param p_responseTender
	 * @param p_adjustmentRequest
	 * @param p_openInvoiceId
	 */
	public void processAdjustment(
			InvoiceLine p_invoiceLine,
			ResponseTender p_responseTender,
			SettlementRequest p_adjustmentRequest,
			String p_openInvoiceId,
			String p_invoiceType
	)
			throws Exception;

}
