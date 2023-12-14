package com.aafes.settlement.service;

import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import com.aafes.settlement.Invoice.model.OpenAuthReversal;
import com.aafes.settlement.Invoice.model.PaymentHeader;
import com.aafes.settlement.Invoice.model.PaymentMethod;
import com.aafes.settlement.Invoice.model.ResponseTender;
import com.aafes.settlement.Invoice.model.SettlementRequest;
import com.aafes.settlement.constant.Constants;

@Service
public class AuthReversalService
	extends
	BaseAuth
	implements Constants
{
	private static final Logger LOGGER = LogManager
			.getLogger(AuthReversalService.class);

	// -----------------------------------------------------------------------------

	/**
	 * This method process the Cancellation request received with respective
	 * IMPL
	 * 
	 * @param p_amount
	 * @param p_authReversalTender
	 * @param p_authPlan
	 * @param p_authReversalRequest
	 */
	public void processAuthReversal(
			OpenAuthReversal p_openAuthReversal,
			ResponseTender p_authReversalTender,
			LinkedHashMap<
					String, Float> p_authPlan,
			SettlementRequest p_authReversalRequest
	)
	{
		List<PaymentMethod> l_sortedPaymentMethod = null;
		List<PaymentHeader> l_paymentHeaderList = p_authReversalRequest
				.getPaymentHeader();

		for (PaymentHeader paymentHeader : l_paymentHeaderList) {

			// MU
			if (p_openAuthReversal.getInterimAmount() == 0) {
				return;
			}
			l_sortedPaymentMethod = paymentHeader
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

			LOGGER.info(l_sortedPaymentMethod);

			splitingAuthReversal(
					p_openAuthReversal,
					p_authReversalTender,
					l_sortedPaymentMethod, p_authPlan
			);

			LOGGER.info(p_authReversalTender);

			// MR
			if (p_openAuthReversal.getInterimAmount() == 0) {
				return;
			}
			l_sortedPaymentMethod = paymentHeader
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

			LOGGER.info(l_sortedPaymentMethod);

			splitingAuthReversal(
					p_openAuthReversal,
					p_authReversalTender,
					l_sortedPaymentMethod,
					p_authPlan
			);

			LOGGER.info(p_authReversalTender);

			// gift cards

			if (p_openAuthReversal.getInterimAmount() == 0) {
				return;
			}
			l_sortedPaymentMethod = paymentHeader.getPaymentMethod()
					.stream()
					.filter(
							payObj -> PAYMENT_TYPE_GC
									.equals(
											payObj.getPaymentType()
													.getPaymentTypeId()
									) && !payObj.getExtended().isNewRefundGiftCard() // Akshay EOR-1729 
					)
					.collect(Collectors.toList());

			LOGGER.info(l_sortedPaymentMethod);
			l_sortedPaymentMethod.sort(
					Comparator
							.comparingDouble(
									PaymentMethod::getCurrentAuthAmount
							).reversed()
			);
			LOGGER.info(l_sortedPaymentMethod);

			splitingAuthReversal(
					p_openAuthReversal,
					p_authReversalTender,
					l_sortedPaymentMethod, p_authPlan
			);

			LOGGER.info(p_authReversalTender);
		}
	}

	// -----------------------------------------------------------------------------

}

// -----------------------------------------------------------------------------
// END OF FILE
// -----------------------------------------------------------------------------
