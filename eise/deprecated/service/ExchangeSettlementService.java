package com.aafes.settlement.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aafes.settlement.Invoice.model.InvoiceLine;
import com.aafes.settlement.Invoice.model.OpenInvoice;
import com.aafes.settlement.Invoice.model.PaymentHeader;
import com.aafes.settlement.Invoice.model.PaymentMethod;
import com.aafes.settlement.Invoice.model.ResponseSettlement;
import com.aafes.settlement.Invoice.model.ResponseTender;
import com.aafes.settlement.Invoice.model.ReturnRequest;
import com.aafes.settlement.configuration.MessagePropertyConfig;
import com.aafes.settlement.constant.Constants;
import com.aafes.settlement.service.implementation.CreditCardAuthImpl;
import com.aafes.settlement.service.implementation.MLRetailAuthImpl;
import com.aafes.settlement.service.implementation.MLUniformAuthImpl;

@Service
public class ExchangeSettlementService implements Constants {
	// -------------------------------------------------------------------------

	@Autowired
	private MessagePropertyConfig messagePropConf;

	private static final Logger	  LOGGER = LogManager
			.getLogger(ShipSettlementService.class);

	@Autowired
	private CreditCardAuthImpl	  creditCardImpl;

	@Autowired
	private MLUniformAuthImpl	  mlUniformAuthImpl;

	@Autowired
	private MLRetailAuthImpl	  mlRetailAuthImpl;

	// -------------------------------------------------------------------------

	/**
	 * This method identifies the invoice type i.e Refund/shipment.
	 * 
	 * @param p_exchangeRequest
	 *            : its the request object.
	 * @param p_respObj
	 *            : its the response object.
	 */
	public void processExchangeRequest(
			ReturnRequest p_exchangeRequest,
			ResponseSettlement l_responseSettlement
	)
	{
		ResponseTender l_exchangeTender = new ResponseTender();
		List<ResponseTender> l_exchangeTenderList = new ArrayList<
				ResponseTender>();
		try {

			List<OpenInvoice> l_openInvoiceList = new ArrayList<OpenInvoice>();
			l_openInvoiceList = p_exchangeRequest.getOpenInvoice();
			String l_invoiceType = "";
			for (OpenInvoice openInvoice : l_openInvoiceList) {

				l_invoiceType = openInvoice.getInvoiceType()
						.getInvoiceTypeId();
				LOGGER.info("InvoiceType IS :: " + l_invoiceType);
				processInvoiceLines(
						l_invoiceType, openInvoice,
						l_exchangeTender, p_exchangeRequest

				);
			}

			l_exchangeTenderList.add(l_exchangeTender);
			l_responseSettlement.setExchangeTender(l_exchangeTenderList);

		} catch (Exception e) {
			e.printStackTrace();
			// set error
		}
	}

	// -------------------------------------------------------------------------

	/**
	 * This method calls the respective service implementation as per request
	 * received.
	 * 
	 * @param p_invoiceType
	 *            : Type of Invoice received
	 * @param p_openInvoice
	 *            : the orders received for shipment
	 * @param l_exchangeTender
	 *            : its response onbject
	 * @param p_settlementRequest:
	 *            its the request object.
	 * @throws Exception
	 */
	private void processInvoiceLines(
			String p_invoiceType, OpenInvoice p_openInvoice,
			ResponseTender l_exchangeTender,
			ReturnRequest p_exchangeRequest
	)
			throws Exception
	{
		AuthInterface l_authInterface = null;

		List<InvoiceLine> p_invoiceLineList = p_openInvoice.getInvoiceLine();
		getReArrangedInvoice(p_invoiceLineList);
		for (InvoiceLine invoiceLine : p_invoiceLineList) {
			l_authInterface = getTenderImpl(invoiceLine, p_exchangeRequest);

			if (p_invoiceType.equals(INVOICE_TYPE_R)) {
				l_authInterface.processReturns(
						invoiceLine,
						l_exchangeTender, p_exchangeRequest, p_openInvoice
								.getInvoiceId(), p_invoiceType
				);
			}

		}
	}

	// -------------------------------------------------------------------------

	/**
	 * This method re-arrange the Invoice line items in the order which needs to
	 * be picked while shipment
	 * 
	 * @param p_invoiceLineList
	 *            : list of invoice lines.
	 */
	private void getReArrangedInvoice(
			List<InvoiceLine> p_invoiceLineList
	)
	{
		if (p_invoiceLineList.size() > 0) {
			p_invoiceLineList.sort(
					(
							InvoiceLine l_invoiceLine1,
							InvoiceLine l_invoiceLine2) -> l_invoiceLine1
									.getExtended().getShipmentPlanOrder()
									- l_invoiceLine2.getExtended()
											.getShipmentPlanOrder()

			);
		}

	}

	// -------------------------------------------------------------------------

	/**
	 * This method is used to identify which impl to call for the respective
	 * request.
	 * 
	 * @param p_invoiceLine
	 *            : contains invoice's of order.
	 * @param p_returnRequest
	 * @return AuthInterface (the IMPL name).
	 */
	private AuthInterface getTenderImpl(
			InvoiceLine p_invoiceLine, ReturnRequest p_returnRequest
	)
	{

		AuthInterface l_authInterface = null;
		List<PaymentHeader> l_paymentHeaderList = p_returnRequest
				.getParentOrder().getPaymentHeader();
		List<PaymentMethod> l_paymentMethod = null;

		for (PaymentHeader paymentHeader : l_paymentHeaderList) {
			l_paymentMethod = paymentHeader.getPaymentMethod();
			for (PaymentMethod paymentMethod : l_paymentMethod) {

				if (
					PAYMENT_TYPE_CC.equals(
							paymentMethod.getPaymentType()
									.getPaymentTypeId()
					)
				)
				{
					l_authInterface = creditCardImpl;
				}
			}
		}
		if (l_authInterface != null) {
			return l_authInterface;
		} else if (
			p_invoiceLine.getExtended()
					.getResponsePlan() == PLAN_MILITARY_UNIFORM
		)
		{
			l_authInterface = mlUniformAuthImpl;
		} else if (
			p_invoiceLine.getExtended()
					.getResponsePlan() == PLAN_MILTARY_RETAIL

		)
		{
			l_authInterface = mlRetailAuthImpl;
		}

		return l_authInterface;
	}
	// -------------------------------------------------------------------------

}
