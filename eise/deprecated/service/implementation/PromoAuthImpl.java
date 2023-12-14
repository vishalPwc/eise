package com.aafes.settlement.service.implementation;

import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import com.aafes.settlement.Invoice.model.InvoiceLine;
import com.aafes.settlement.Invoice.model.OpenInvoice;
import com.aafes.settlement.Invoice.model.PaymentHeader;
import com.aafes.settlement.Invoice.model.PaymentMethod;
import com.aafes.settlement.Invoice.model.ResponseTender;
import com.aafes.settlement.Invoice.model.ReturnRequest;
import com.aafes.settlement.Invoice.model.SettlementRequest;
import com.aafes.settlement.constant.Constants;
import com.aafes.settlement.constant.ErrorConstants;
import com.aafes.settlement.service.AuthInterface;
import com.aafes.settlement.service.BaseAuth;

@Service
public class PromoAuthImpl
	extends
	BaseAuth
	implements AuthInterface, ErrorConstants, Constants
{
	private static final Logger LOGGER = LogManager
			.getLogger(PromoAuthImpl.class);

	// -----------------------------------------------------------------------------

	@Override
	public void processShipment(
			InvoiceLine p_invoiceLine,
			OpenInvoice p_openInvoice,
			ResponseTender p_settlementTender,
			LinkedHashMap<String, Float> p_authPlan,
			SettlementRequest p_OrderRequest
	)
			throws Exception
	{
		List<PaymentMethod> p_sortedPaymentMethod = null;
		List<PaymentHeader> l_paymentHeaderList = p_OrderRequest
				.getPaymentHeader();
		List<PaymentMethod> l_paymentMethod = null;

		for (PaymentHeader paymentHeader : l_paymentHeaderList) {
			l_paymentMethod = paymentHeader.getPaymentMethod();
			for (PaymentMethod paymentMethod : l_paymentMethod) {
				paymentMethod.setPlanType(PLAN_PROMO_CODE);
				if (p_invoiceLine.getInterimOrderLineTotal() == 0) {
					return;
				}

				p_sortedPaymentMethod = paymentHeader
						.getPaymentMethod()
						.stream()
						.filter(
								payObj -> payObj.getCardType()
										.getCardTypeId() != null
										&& CARD_TYPE_R
												.equals(
														payObj.getCardType()
																.getCardTypeId()
												)

						)
						.collect(Collectors.toList());
			}
			LOGGER.info(p_sortedPaymentMethod);

			splitingShipmentAuth(
					p_invoiceLine, p_settlementTender,
					p_sortedPaymentMethod, p_authPlan, p_openInvoice
			);

			LOGGER.info(p_settlementTender);

			// gift cards
			for (PaymentMethod paymentMethod : l_paymentMethod) {
				paymentMethod.setPlanType("");
				if (p_invoiceLine.getInterimOrderLineTotal() == 0) {
					return;
				}
				p_sortedPaymentMethod = paymentHeader.getPaymentMethod()
						.stream()
						.filter(
								payObj -> PAYMENT_TYPE_GC
										.equals(
												payObj.getPaymentType()
														.getPaymentTypeId()
										)
						)
						.collect(Collectors.toList());

				LOGGER.info(p_sortedPaymentMethod);
				p_sortedPaymentMethod.sort(
						Comparator
								.comparingDouble(
										PaymentMethod::getCurrentAuthAmount
								)
				);
			}
			LOGGER.info(p_sortedPaymentMethod);

			splitingShipmentAuth(
					p_invoiceLine, p_settlementTender,
					p_sortedPaymentMethod, p_authPlan, p_openInvoice
			);

			LOGGER.info(p_settlementTender);

		}
	}
	// -----------------------------------------------------------------------------

	@Override
	public void processReturns(
			InvoiceLine p_invoiceLine,
			ResponseTender p_refundTender,
			ReturnRequest p_returnRequest, String openInvoiceId,
			String p_invoiceType
	)
			throws Exception
	{
		// TODO Auto-generated method stub

	}
	// -----------------------------------------------------------------------------

	@Override
	public void processAdjustment(
			InvoiceLine p_invoiceLine, ResponseTender p_responseTender,
			SettlementRequest p_adjustmentRequest, String p_openInvoiceId,
			String p_invoiceType
	)
	{
		// TODO Auto-generated method stub

	}

}
// -----------------------------------------------------------------------------
// END OF FILE
// -----------------------------------------------------------------------------
