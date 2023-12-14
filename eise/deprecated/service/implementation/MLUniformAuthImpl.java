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
public class MLUniformAuthImpl
	extends
	BaseAuth
	implements AuthInterface, ErrorConstants, Constants
{

	private static final Logger LOGGER = LogManager
			.getLogger(MLUniformAuthImpl.class);

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

		for (PaymentHeader paymentHeader : l_paymentHeaderList) {
			if (p_invoiceLine.getInterimOrderLineTotal() == 0) {
				return;
			}

			p_sortedPaymentMethod = paymentHeader
					.getPaymentMethod()
					.stream()
					.filter(
							payObj -> payObj.getCardType()
									.getCardTypeId() != null
									&& CARD_TYPE_U
											.equals(
													payObj.getCardType()
															.getCardTypeId()
											)

					)
					.collect(Collectors.toList());

			LOGGER.info(p_sortedPaymentMethod);

			splitingShipmentAuth(
					p_invoiceLine, p_settlementTender,
					p_sortedPaymentMethod, p_authPlan, p_openInvoice
			);

			LOGGER.info(p_settlementTender);

			// gift cards

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
									) && !payObj.getExtended().isNewRefundGiftCard() // Akshay EOR-1729
					)
					.collect(Collectors.toList());

			LOGGER.info(p_sortedPaymentMethod);
			p_sortedPaymentMethod.sort(
					Comparator
							.comparingDouble(
									PaymentMethod::getCurrentAuthAmount
							)
			);
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
			ResponseTender p_responseTender,
			ReturnRequest p_returnRequest,
			String p_openInvoiceId,
			String p_invoiceType
	)
			throws Exception
	{
		List<PaymentHeader> l_paymentHeaderList = p_returnRequest
				.getParentOrder()
				.getPaymentHeader();

		processingTenderAuth(
				p_invoiceLine, p_responseTender, l_paymentHeaderList,
				p_openInvoiceId, p_invoiceType
		);
	}

	// -----------------------------------------------------------------------------
	/**
	 * 
	 * @param p_invoiceLine
	 * @param p_responseTender
	 * @param p_paymentHeaderList
	 * @param p_openInvoiceId
	 */
	public void processingTenderAuth(
			InvoiceLine p_invoiceLine, ResponseTender p_responseTender, List<
					PaymentHeader> p_paymentHeaderList, String p_openInvoiceId,
			String p_invoiceType
	)
	{
		List<PaymentMethod> p_sortedPaymentMethod = null;
		List<PaymentHeader> l_paymentHeaderList = p_paymentHeaderList;

		for (PaymentHeader paymentHeader : l_paymentHeaderList) {

			if (p_invoiceLine.getInterimOrderLineTotal() == 0) {
				return;
			}

			p_sortedPaymentMethod = paymentHeader
					.getPaymentMethod()
					.stream()
					.filter(
							payObj -> payObj.getCardType()
									.getCardTypeId() != null
									&& CARD_TYPE_U
											.equals(
													payObj.getCardType()
															.getCardTypeId()
											)

					)
					.collect(Collectors.toList());

			LOGGER.info(p_sortedPaymentMethod);

			splitingReturnsAuth(
					p_invoiceLine, p_responseTender,
					p_sortedPaymentMethod, p_openInvoiceId, p_invoiceType
			);

			LOGGER.info(p_responseTender);
			// gift cards

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
					Comparator.comparingDouble(
							PaymentMethod::getCurrentSettledAmount
					).reversed()
			);
			LOGGER.info(p_sortedPaymentMethod);

			splitingReturnsAuth(
					p_invoiceLine, p_responseTender,
					p_sortedPaymentMethod, p_openInvoiceId, p_invoiceType
			);

			LOGGER.info(p_responseTender);

		}

	}
	// -----------------------------------------------------------------------------

	@Override
	public void processAdjustment(
			InvoiceLine p_invoiceLine, ResponseTender p_responseTender,
			SettlementRequest p_adjustmentRequest, String p_openInvoiceId,
			String p_invoiceType
	)
	{
		List<PaymentHeader> l_paymentHeaderList = p_adjustmentRequest
				.getPaymentHeader();

		processingTenderAuth(
				p_invoiceLine, p_responseTender, l_paymentHeaderList,
				p_openInvoiceId, p_invoiceType
		);

	}

}
// -----------------------------------------------------------------------------
// END OF FILE
// -----------------------------------------------------------------------------
