package com.aafes.settlement.service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import com.aafes.settlement.Invoice.model.InvoiceLineChargeDetail;
import com.aafes.settlement.Invoice.model.PaymentHeader;
import com.aafes.settlement.Invoice.model.PaymentMethod;
import com.aafes.settlement.Invoice.model.SettlementRequest;
import com.aafes.settlement.Invoice.model.ResponseTender;
import com.aafes.settlement.constant.Constants;

@Service
public class ShippingChargeService
	extends
	BaseAuth
	implements Constants
{

	private static final Logger LOGGER = LogManager
			.getLogger(ShipSettlementService.class);

	// -----------------------------------------------------------------------------

	/**
	 * 
	 * @param p_invoiceLineChargeDetail
	 * @param p_settlementTender
	 * @param p_settlementRequest
	 */
	public void processShippingCharge(
			InvoiceLineChargeDetail p_invoiceLineChargeDetail,
			ResponseTender p_settlementTender,
			SettlementRequest p_settlementRequest
	)
	{

		List<PaymentMethod> p_sortedPaymentMethod = null;
		List<PaymentHeader> l_paymentHeaderList = p_settlementRequest
				.getPaymentHeader();

		for (PaymentHeader paymentHeader : l_paymentHeaderList) {
			// gift cards

			if (p_invoiceLineChargeDetail.getInterimOrderLineTotal() == 0) {
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
									PaymentMethod::getCalcCurrentAuthAmount
							)
			);
			LOGGER.info(p_sortedPaymentMethod);

			splitingSHippingChargeAuth(
					p_invoiceLineChargeDetail, p_settlementTender,
					p_sortedPaymentMethod
			);

			LOGGER.info(p_settlementTender);
			if (p_invoiceLineChargeDetail.getInterimOrderLineTotal() == 0) {
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

			LOGGER.info(p_sortedPaymentMethod);

			splitingSHippingChargeAuth(
					p_invoiceLineChargeDetail, p_settlementTender,
					p_sortedPaymentMethod
			);

			LOGGER.info(p_settlementTender);

		}

	}

	// -----------------------------------------------------------------------------

	/**
	 * 
	 * @param p_invoiceLineChargeDetail
	 * @param p_settlementTender
	 * @param p_sortedPaymentMethod
	 */
	private void splitingSHippingChargeAuth(
			InvoiceLineChargeDetail p_invoiceLineChargeDetail,
			ResponseTender p_settlementTender, List<
					PaymentMethod> p_sortedPaymentMethod
	)
	{
		for (PaymentMethod paymentMethod : p_sortedPaymentMethod) {

			calculateShippingcharges(
					paymentMethod, p_invoiceLineChargeDetail,
					p_settlementTender
			);
		}

	}

	// -----------------------------------------------------------------------------
}
// -----------------------------------------------------------------------------
// END OF FILE
// -----------------------------------------------------------------------------
