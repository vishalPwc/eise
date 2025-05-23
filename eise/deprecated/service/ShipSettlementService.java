package com.aafes.settlement.service;
// -----------------------------------------------------------------------------

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aafes.settlement.Invoice.model.InvoiceLine;
import com.aafes.settlement.Invoice.model.InvoiceLineChargeDetail;
import com.aafes.settlement.Invoice.model.OpenInvoice;
import com.aafes.settlement.Invoice.model.OrderLine;
import com.aafes.settlement.Invoice.model.PaymentHeader;
import com.aafes.settlement.Invoice.model.PaymentMethod;
import com.aafes.settlement.Invoice.model.ResponseSettlement;
import com.aafes.settlement.Invoice.model.ResponseTender;
import com.aafes.settlement.Invoice.model.SettlementRequest;
import com.aafes.settlement.configuration.MessagePropertyConfig;
import com.aafes.settlement.constant.Constants;
import com.aafes.settlement.service.implementation.CreditCardAuthImpl;
import com.aafes.settlement.service.implementation.MLRetailAuthImpl;
import com.aafes.settlement.service.implementation.MLUniformAuthImpl;
import com.aafes.settlement.service.implementation.PromoAuthImpl;
import com.aafes.settlement.validators.SettlementValidator;

// -----------------------------------------------------------------------------

@Service
public class ShipSettlementService
	implements Constants
{
	// -------------------------------------------------------------------------

	@Autowired
	private MessagePropertyConfig messagePropConf;

	private static final Logger	  LOGGER = LogManager
			.getLogger(ShipSettlementService.class);

	@Autowired
	private CreditCardAuthImpl	  creditCardImpl;

	@Autowired
	private PromoAuthImpl		  promoAuthImpl;

	@Autowired
	private MLUniformAuthImpl	  mlUniformAuthImpl;

	@Autowired
	private MLRetailAuthImpl	  mlRetailAuthImpl;

	@Autowired
	private ShippingChargeService shippingChargeService;

	@Autowired
	private RepositoryService	  repoService;

	@Autowired
	private SettlementValidator	  gSettlementValidation;
	// -------------------------------------------------------------------------

	/**
	 * This method calculate the each order line image total w.r.t to promo plan
	 * of it and identifies the invoice type i.e Refund/shipment.
	 * 
	 * @param p_settlementRequest
	 *            : its the request object.
	 * @param p_respObj
	 *            : its the response object.
	 */
	public void processInvoiceSettlement(
			SettlementRequest p_settlementRequest,
			ResponseSettlement p_responseSettlement
	)
	{
		ResponseTender l_settlementTender = new ResponseTender();
		List<ResponseTender> l_settlementTenderList = new ArrayList<
				ResponseTender>();
		LOGGER.info("---- Validate Shippment Request Node----");
		if (!gSettlementValidation.validate(p_settlementRequest)) {
			LinkedHashMap<String, Float> l_authPlan = new LinkedHashMap<String,
					Float>();
			try {

				l_authPlan = groupOrderLineByAuth(p_settlementRequest);
				LOGGER.info("EACH AUTH TOTAL IS :: " + l_authPlan);
				processOpenInvoice(
						p_settlementRequest, l_authPlan, l_settlementTender
				);
				l_settlementTenderList.add(l_settlementTender);
				p_responseSettlement.setSettlementTender(
						l_settlementTenderList
				);

			} catch (Exception e) {
				e.printStackTrace();

				// set error
			}
		} else {
			// Utils.setFailureInResponseObj(p_responseSettlement);
		}

	}

	// -------------------------------------------------------------------------

	/**
	 * This method identifies the type of invoice received in request.
	 * 
	 * @param p_settlementRequest
	 *            : its the request object.
	 * @param p_authPlan
	 *            : Map that contains the order line image total w.rt to promo
	 *            plan of each line
	 * @param p_responseOpenInvoice
	 *            : its the response object.
	 * @throws Exception
	 */
	private void processOpenInvoice(
			SettlementRequest p_settlementRequest, LinkedHashMap<String,
					Float> p_authPlan, ResponseTender p_settlementTender
	)
			throws Exception
	{
		List<OpenInvoice> l_openInvoiceList = new ArrayList<OpenInvoice>();
		l_openInvoiceList = p_settlementRequest.getOpenInvoice();
		String l_invoiceType = "";
		for (OpenInvoice openInvoice : l_openInvoiceList) {

			l_invoiceType = openInvoice.getInvoiceType()
					.getInvoiceTypeId();
			LOGGER.info("InvoiceType IS :: " + l_invoiceType);
			processInvoiceLines(
					l_invoiceType, openInvoice,
					p_settlementTender, p_authPlan, p_settlementRequest

			);
			repoService.saveOrderInvoiceDetails(openInvoice);
		}

	}

	// -------------------------------------------------------------------------

	/**
	 * This method calls the respective service implementation as per request
	 * received.
	 * 
	 * @param l_invoiceType
	 *            : Type of Invoice received
	 * @param p_openInvoice
	 *            : the orders received for shipment
	 * @param p_responseOpenInvoice
	 *            : its response onbject
	 * @param p_authPlan
	 *            : Map that contains the orderline image total w.rt to promo
	 *            plan of each line
	 * @param p_settlementRequest:
	 *            its the request object.
	 * @throws Exception
	 */
	private void processInvoiceLines(
			String l_invoiceType, OpenInvoice p_openInvoice,
			ResponseTender p_settlementTender, LinkedHashMap<
					String,
					Float> p_authPlan,
			SettlementRequest p_settlementRequest
	)
			throws Exception
	{
		AuthInterface l_authInterface = null;

		List<InvoiceLine> p_invoiceLineList = p_openInvoice.getInvoiceLine();
		getReArrangedInvoice(p_invoiceLineList);
		for (InvoiceLine invoiceLine : p_invoiceLineList) {
			l_authInterface = getTenderImpl(invoiceLine, p_settlementRequest);

			if (l_invoiceType.equals(INVOICE_TYPE_S)) {
				l_authInterface.processShipment(
						invoiceLine, p_openInvoice,
						p_settlementTender, p_authPlan, p_settlementRequest
				);
			}

		}
		if (p_openInvoice.getInvoiceLineChargeDetail().size() > 0) {

			List<
					InvoiceLineChargeDetail> l_invoiceLineChargeDetail = p_openInvoice
							.getInvoiceLineChargeDetail();

			for (InvoiceLineChargeDetail invoiceLineChargeDetail : l_invoiceLineChargeDetail) {
				if (
					SHIPPING_CHARGES.equals(
							invoiceLineChargeDetail.getChargeType()
									.getChargeTypeId()
					)
				)
				{
					shippingChargeService.processShippingCharge(
							invoiceLineChargeDetail, p_settlementTender,
							p_settlementRequest
					);
				}
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
	 * This method is used to calculate the each orderline image total w.rt to
	 * promo plan of each line
	 * 
	 * @param p_settlementRequest:
	 *            its the request object.
	 * @return LinkedHashMap with promo plan and its total.
	 */
	public LinkedHashMap<String, Float> groupOrderLineByAuth(
			SettlementRequest p_settlementRequest
	)
	{

		LinkedHashMap<String, Float> l_promoPlanMap = new LinkedHashMap<
				String,
				Float>();
		List<Integer> l_plans = new ArrayList<Integer>();
		int l_planobj = 0;
		String l_strPlan = null;
		List<OrderLine> l_orderLineList = new ArrayList<OrderLine>();
		l_orderLineList = p_settlementRequest.getOrderLine();
		for (OrderLine orderLine : l_orderLineList) {
			l_planobj = getOderLinePlan(orderLine);
			l_plans.add(l_planobj);
		}

		List<OpenInvoice> l_closedInvoicesList = new ArrayList<OpenInvoice>();
		List<InvoiceLine> l_invoiceLineList = new ArrayList<InvoiceLine>();
		l_closedInvoicesList = p_settlementRequest.getClosedInvoices();

		for (OpenInvoice closedInvoices : l_closedInvoicesList) {
			l_invoiceLineList = closedInvoices.getInvoiceLine();
			for (InvoiceLine invoiceLine : l_invoiceLineList) {
				l_planobj = getOpenInvoicePlan(invoiceLine);
				l_plans.add(l_planobj);
			}

		}

		float l_OpenPromoAmount = 0f;
		float l_closedPromoAmount = 0f;
		float l_perbucketAmount = 0f;
		LOGGER.info("l_plans ::: " + l_plans);

		for (Integer plan : l_plans) {

			if (plan != PLAN_MILITARY_UNIFORM && plan != PLAN_MILTARY_RETAIL) {

				l_OpenPromoAmount = (float) l_orderLineList.stream()
						.filter(
								a -> a != null
										&& a.getOrderLineTotal() != 0F
										&& a.getExtended()
												.getPromoPlan() >= PLAN_PROMO_START
										&& a.getExtended()
												.getPromoPlan() <= PLAN_PROMO_END
										&& a.getExtended()
												.getResponsePlan() == PLAN_MILTARY_RETAIL
										&& a.getExtended()
												.getResponsePlan() != PLAN_MILITARY_UNIFORM
						)
						.mapToDouble(OrderLine::getOrderLineTotal)
						.sum();

				// Closed Invoice
				l_closedPromoAmount = (float) l_invoiceLineList.stream()
						.filter(
								a -> a != null
										&& a.getInvoiceLineTotal() != 0F
										&& a.getExtended()
												.getPromoPlan() >= PLAN_PROMO_START
										&& a.getExtended()
												.getPromoPlan() <= PLAN_PROMO_END
										&& a.getExtended()
												.getResponsePlan() == PLAN_MILTARY_RETAIL
										&& a.getExtended()
												.getResponsePlan() != PLAN_MILITARY_UNIFORM
						)
						.mapToDouble(InvoiceLine::getInvoiceLineTotal)
						.sum();

				l_strPlan = PLAN_PROMO_CODE;

			} else if (plan == PLAN_MILITARY_UNIFORM) {

				l_OpenPromoAmount = (float) l_orderLineList.stream()
						.filter(
								a -> a != null
										&& a.getOrderLineTotal() != 0F
										&& a.getExtended()
												.getResponsePlan() == plan
						)
						.mapToDouble(OrderLine::getOrderLineTotal)
						.sum();

				l_closedPromoAmount = (float) l_invoiceLineList.stream()
						.filter(
								a -> a != null
										&& a.getInvoiceLineTotal() != 0F
										&& a.getExtended()
												.getResponsePlan() == plan
						)
						.mapToDouble(InvoiceLine::getInvoiceLineTotal)
						.sum();
				l_strPlan = TENDER_MILITARY_UNIFORM;

			} else if (plan == PLAN_MILTARY_RETAIL) {

				l_OpenPromoAmount = (float) l_orderLineList.stream()
						.filter(
								a -> a != null
										&& a.getOrderLineTotal() != 0F
										&& a.getExtended()
												.getResponsePlan() == plan
										&& a.getExtended()
												.getPromoPlan() == PLAN_PROMO
						)
						.mapToDouble(OrderLine::getOrderLineTotal)
						.sum();

				l_closedPromoAmount = (float) l_invoiceLineList.stream()
						.filter(
								a -> a != null
										&& a.getInvoiceLineTotal() != 0F
										&& a.getExtended()
												.getResponsePlan() == plan
										&& a.getExtended()
												.getPromoPlan() == PLAN_PROMO
						)
						.mapToDouble(InvoiceLine::getInvoiceLineTotal)
						.sum();
				l_strPlan = TENDER_MILTARY_RETAIL;

			}

			LOGGER.info("l_promoAmount : " + l_OpenPromoAmount);

			LOGGER.info("l_closedPromoAmount : " + l_closedPromoAmount);

			if (l_OpenPromoAmount == 0) {
				l_perbucketAmount = 0;
			} else {
				l_perbucketAmount = l_OpenPromoAmount - l_closedPromoAmount;
			}

			LOGGER.info(
					l_strPlan + " l_perbucketAmount : "
							+ l_perbucketAmount
			);
			l_promoPlanMap.put(l_strPlan, l_perbucketAmount);
		}
		return l_promoPlanMap;
	}

	// -------------------------------------------------------------------------
	/**
	 * This method gets the plan no of each Closed invoice in image.
	 * 
	 * @param invoiceLine
	 * @return
	 */
	private int getOpenInvoicePlan(InvoiceLine invoiceLine) {

		int l_planobj = 0;
		if (
			invoiceLine.getExtended()
					.getResponsePlan() == PLAN_MILITARY_UNIFORM
		)
		{
			l_planobj = PLAN_MILITARY_UNIFORM;
		} else if (
			invoiceLine.getExtended()
					.getResponsePlan() == PLAN_MILTARY_RETAIL
					&& invoiceLine
							.getExtended().getPromoPlan() == PLAN_PROMO
		)
		{
			l_planobj = PLAN_MILTARY_RETAIL;
		} else if (
			invoiceLine.getExtended()
					.getResponsePlan() == PLAN_MILTARY_RETAIL
					&& invoiceLine
							.getExtended().getPromoPlan() != PLAN_PROMO
		)
		{
			l_planobj = invoiceLine
					.getExtended().getPromoPlan();
		}

		return l_planobj;

	}

	// -------------------------------------------------------------------------
	/**
	 * This method gets the plan no of each order-Line in image.
	 * 
	 * @param p_orderLine
	 *            : each order-Line in image.
	 * @return : the plan no of each order-Line in image.
	 */
	private int getOderLinePlan(OrderLine p_orderLine) {

		int l_planobj = 0;
		if (
			p_orderLine.getExtended().getResponsePlan() == PLAN_MILITARY_UNIFORM
		)
		{
			l_planobj = PLAN_MILITARY_UNIFORM;
		} else if (
			p_orderLine.getExtended().getResponsePlan() == PLAN_MILTARY_RETAIL
					&& p_orderLine
							.getExtended().getPromoPlan() == PLAN_PROMO
		)
		{
			l_planobj = PLAN_MILTARY_RETAIL;
		} else if (
			p_orderLine.getExtended().getResponsePlan() == PLAN_MILTARY_RETAIL
					&& p_orderLine
							.getExtended().getPromoPlan() != PLAN_PROMO
		)
		{
			l_planobj = p_orderLine
					.getExtended().getPromoPlan();
		}

		return l_planobj;
	}

	// -------------------------------------------------------------------------

	/**
	 * This method is used to identify which impl to call for the respective
	 * request.
	 * 
	 * @param p_invoiceLine
	 *            : contains invoice's of order.
	 * @param p_settlementRequest
	 * @return AuthInterface (the IMPL name).
	 */
	private AuthInterface getTenderImpl(
			InvoiceLine p_invoiceLine, SettlementRequest p_settlementRequest
	)
	{

		AuthInterface l_authInterface = null;
		List<PaymentHeader> l_paymentHeaderList = p_settlementRequest
				.getPaymentHeader();
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
					&& p_invoiceLine
							.getExtended().getPromoPlan() == PLAN_PROMO
		)
		{
			l_authInterface = mlRetailAuthImpl;
		} else if (
			p_invoiceLine.getExtended()
					.getResponsePlan() == PLAN_MILTARY_RETAIL
					&& p_invoiceLine
							.getExtended().getPromoPlan() != PLAN_PROMO
					&& p_invoiceLine
							.getExtended()
							.getPromoPlan() >= PLAN_PROMO_START
					&& p_invoiceLine
							.getExtended()
							.getPromoPlan() <= PLAN_PROMO_END
		)
		{
			l_authInterface = promoAuthImpl;
		}

		return l_authInterface;
	}
	// -------------------------------------------------------------------------

}
// -----------------------------------------------------------------------------
// END OF FILE
// -----------------------------------------------------------------------------