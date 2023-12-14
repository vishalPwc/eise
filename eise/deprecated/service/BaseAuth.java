package com.aafes.settlement.service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.aafes.settlement.Invoice.model.InvoiceLine;
import com.aafes.settlement.Invoice.model.InvoiceLineChargeDetail;
import com.aafes.settlement.Invoice.model.OpenAuthReversal;
import com.aafes.settlement.Invoice.model.OpenInvoice;
import com.aafes.settlement.Invoice.model.PaymentMethod;
import com.aafes.settlement.Invoice.model.ResponseInvoiceLine;
import com.aafes.settlement.Invoice.model.ResponseInvoiceLineCharge;
import com.aafes.settlement.Invoice.model.ResponsePaymentMethod;
import com.aafes.settlement.Invoice.model.ResponseTender;
import com.aafes.settlement.constant.Constants;

public abstract class BaseAuth
	implements Constants
{
	private static final Logger LOGGER = LogManager
			.getLogger(BaseAuth.class);

	// -----------------------------------------------------------------------------

	/**
	 * This method iterate the payment method if its more then one and route to
	 * calculateShpment method.
	 * 
	 * @param p_invoiceLine
	 *            : the invoice of item received for shipment in request.
	 * @param p_settlementTender
	 *            : response object for openInvoices.
	 * @param p_sortedPaymentMethod
	 *            : map that contains payment method for calculating Shpment
	 * @param l_authPlan:
	 *            Map which contains total amount of each oderline w.e.t to the
	 *            plans of items.
	 * @param p_openInvoice
	 *            : contains the array of invoice lines.
	 * @throws Exception
	 */

	public void splitingShipmentAuth(
			InvoiceLine p_invoiceLine,
			ResponseTender p_settlementTender,
			List<PaymentMethod> p_sortedPaymentMethod,
			LinkedHashMap<String, Float> p_authPlan,
			OpenInvoice p_openInvoice
	)
			throws Exception
	{
		for (PaymentMethod paymentMethod : p_sortedPaymentMethod) {

			calculateShipment(
					paymentMethod, p_invoiceLine,
					p_settlementTender, p_openInvoice, p_authPlan
			);
		}

	}

	// -----------------------------------------------------------------------------

	/**
	 * 
	 * @param paymentMethod
	 * @param invoiceLine
	 * @param p_settlementTender
	 * @param paymentTypeId
	 * @param l_authPlan
	 */
	public void calculateShipment(
			PaymentMethod p_paymentMethod, InvoiceLine p_invoiceLine,
			ResponseTender p_settlementTender,
			OpenInvoice p_openInvoice,
			LinkedHashMap<String, Float> l_authPlan
	)
	{
		if (p_paymentMethod.getCalcCurrentAuthAmount() == 0) {
			return;
		}

		Float l_balanceAmt = 0F;
		Float l_respAmt = 0F;
		String l_paymentTypeId = null;
		List<ResponsePaymentMethod> listPaymentMethod = null;
		ResponseInvoiceLine l_respLine = null;
		String l_cardType = null;
		float l_perbucketAmt = 0;
		float l_muBalLeft;
		String l_card = null;
		String l_planType = null;
		float l_inMemoryMUProcessedAmt = 0f;

		if (
			l_inMemoryMUProcessedAmt != p_openInvoice
					.getInMemoryMUProcessed()
		)
		{
			l_inMemoryMUProcessedAmt = p_openInvoice
					.getInMemoryMUProcessed();
		}
		float l_orderLineAmt = p_invoiceLine.getInvoiceLineTotal();
		float l_authAmt = p_paymentMethod.getCalcCurrentAuthAmount();
		l_cardType = p_paymentMethod.getCardType().getCardTypeId();
		l_planType = p_paymentMethod.getPlanType();
		if (
			(CARD_TYPE_U).equals(l_cardType) || (PLAN_PROMO_CODE).equals(
					l_planType
			)
		)
		{

			if ((CARD_TYPE_U).equals(l_cardType)) {
				l_card = TENDER_MILITARY_UNIFORM;
				l_perbucketAmt = l_authPlan.get(l_card) - l_orderLineAmt
						- l_inMemoryMUProcessedAmt;
			} else {
				l_card = TENDER_MILTARY_RETAIL;
				l_perbucketAmt = l_authPlan.get(l_card);
			}
			if (p_paymentMethod.getCalcCurrentAuthAmount() >= l_perbucketAmt) {

				l_muBalLeft = p_paymentMethod.getCalcCurrentAuthAmount()
						- l_perbucketAmt;

				if (l_muBalLeft >= l_orderLineAmt) {
					l_balanceAmt = l_muBalLeft
							- l_orderLineAmt;
					p_paymentMethod.setCalcCurrentAuthAmount(l_balanceAmt);
					p_invoiceLine.setInterimOrderLineTotal(0F);
					if ((CARD_TYPE_U).equals(l_cardType)) {
						l_inMemoryMUProcessedAmt = l_inMemoryMUProcessedAmt
								+ l_orderLineAmt;
						p_openInvoice.setInMemoryMUProcessed(
								l_inMemoryMUProcessedAmt
						);
					}

					l_respAmt = l_orderLineAmt;
				} else {
					l_balanceAmt = l_orderLineAmt
							- l_muBalLeft;
					l_respAmt = l_muBalLeft;
					p_paymentMethod.setCalcCurrentAuthAmount(l_perbucketAmt);
					p_invoiceLine.setInterimOrderLineTotal(l_balanceAmt);
					p_paymentMethod.setPlanType("null");
				}

			} else {
				LOGGER.info(
						"No need to process ad per-bucket amount is greater then Auth amt"
				);
				return;

			}
		} else {

			if (
				p_invoiceLine.getInterimOrderLineTotal() >= p_paymentMethod
						.getCalcCurrentAuthAmount()
			)
			{
				l_respAmt = p_paymentMethod.getCalcCurrentAuthAmount();
				l_balanceAmt = p_invoiceLine.getInterimOrderLineTotal()
						- l_respAmt;

				p_paymentMethod.setCalcCurrentAuthAmount(0F);
				p_invoiceLine.setInterimOrderLineTotal(l_balanceAmt);

			} else {
				l_respAmt = p_invoiceLine.getInterimOrderLineTotal();
				l_balanceAmt = p_paymentMethod.getCalcCurrentAuthAmount()
						- l_respAmt;
				p_paymentMethod.setCalcCurrentAuthAmount(l_balanceAmt);
				p_invoiceLine.setInterimOrderLineTotal(0F);
				if ((CARD_TYPE_U).equals(l_cardType)) {
					l_inMemoryMUProcessedAmt = l_inMemoryMUProcessedAmt
							+ l_orderLineAmt;
					p_openInvoice.setInMemoryMUProcessed(
							l_inMemoryMUProcessedAmt
					);
				}

			}
		}
		if (l_authAmt != p_paymentMethod.getCalcCurrentAuthAmount()) {
			p_settlementTender.setInvoiceId(p_openInvoice.getInvoiceId());
			l_respLine = new ResponseInvoiceLine();
			l_respLine.setInvoiceLineId(p_invoiceLine.getItemId());
			l_respLine.setPaymentAmount(l_respAmt);
			l_paymentTypeId = p_paymentMethod.getPaymentMethodId();
			p_settlementTender.setInvoiceTypeId(INVOICE_TYPE_S);
			listPaymentMethod = updatePaymentMethod(
					p_settlementTender,
					l_paymentTypeId, l_respLine
			);
			p_settlementTender.setPaymentMethod(listPaymentMethod);

		}

	}

	// --------------------------------------------------------------------------

	/**
	 * @param p_responseTender
	 * @param p_paymentTypeId
	 * @param p_respLine
	 * @param p_paymentMethod
	 * @return
	 */
	private List<ResponsePaymentMethod> updatePaymentMethod(
			ResponseTender p_responseTender,
			String p_paymentTypeId, ResponseInvoiceLine p_respLine
	)
	{
		ResponsePaymentMethod l_respPaymentMethod = null;
		List<ResponsePaymentMethod> l_listPaymentMethod = null;
		List<ResponseInvoiceLine> l_respLineList = null;
		int l_tempIndex = -1;

		if (p_responseTender.getPaymentMethod() == null) {
			l_respPaymentMethod = new ResponsePaymentMethod();
			l_respPaymentMethod.setPaymentMethodId(p_paymentTypeId);
			l_respLineList = new ArrayList<ResponseInvoiceLine>();
			l_respLineList.add(p_respLine);
			l_listPaymentMethod = new ArrayList<ResponsePaymentMethod>();
			l_respPaymentMethod.setRespInvoiceLine(l_respLineList);
			l_listPaymentMethod.add(l_respPaymentMethod);

		} else {

			l_listPaymentMethod = p_responseTender.getPaymentMethod()
					.stream()
					.filter(
							paymentObj -> p_paymentTypeId
									.equals(paymentObj.getPaymentMethodId())
					)
					.collect(Collectors.toList());
			if (l_listPaymentMethod.size() == 0) {
				l_respLineList = new ArrayList<ResponseInvoiceLine>();
				l_respLineList.add(p_respLine);
				l_listPaymentMethod = p_responseTender
						.getPaymentMethod();
				l_respPaymentMethod = new ResponsePaymentMethod();
				l_respPaymentMethod.setPaymentMethodId(p_paymentTypeId);
				l_respPaymentMethod.setRespInvoiceLine(l_respLineList);
				l_listPaymentMethod.add(l_respPaymentMethod);

			} else {

				l_tempIndex = IntStream
						.range(
								0,
								p_responseTender.getPaymentMethod()
										.size()
						)
						.filter(
								i -> p_responseTender
										.getPaymentMethod().get(i)
										.getPaymentMethodId().equals(
												p_paymentTypeId
										)
						)
						.findFirst().orElse(0);
				l_listPaymentMethod = p_responseTender
						.getPaymentMethod();
				l_respPaymentMethod = l_listPaymentMethod.get(l_tempIndex);
				l_respPaymentMethod.getRespInvoiceLine().add(p_respLine);
				// listPaymentMethod.add(l_respPaymentMethod);

			}

			//
		}
		return l_listPaymentMethod;
	}
	// -----------------------------------------------------------------------------

	/**
	 * 
	 * @param p_paymentMethod
	 * @param p_invoiceLineChargeDetail
	 * @param p_settlementTender
	 */
	public void calculateShippingcharges(
			PaymentMethod p_paymentMethod,
			InvoiceLineChargeDetail p_invoiceLineChargeDetail,
			ResponseTender p_settlementTender
	)

	{

		Float l_balanceAmt = 0F;
		Float l_respAmt = 0F;
		String l_paymentTypeId = p_paymentMethod.getPaymentType()
				.getPaymentTypeId();
		List<ResponsePaymentMethod> listPaymentMethod = null;
		ResponseInvoiceLineCharge l_respLine = null;
		float l_authAmt = p_paymentMethod.getCalcCurrentAuthAmount();

		if (
			p_invoiceLineChargeDetail
					.getInterimOrderLineTotal() >= p_paymentMethod
							.getCalcCurrentAuthAmount()
		)
		{
			l_respAmt = p_paymentMethod.getCalcCurrentAuthAmount();
			l_balanceAmt = p_invoiceLineChargeDetail.getInterimOrderLineTotal()
					- l_respAmt;

			p_paymentMethod.setCalcCurrentAuthAmount(0F);
			p_invoiceLineChargeDetail.setInterimOrderLineTotal(l_balanceAmt);

		} else {
			l_respAmt = p_invoiceLineChargeDetail.getInterimOrderLineTotal();
			l_balanceAmt = p_paymentMethod.getCalcCurrentAuthAmount()
					- l_respAmt;
			p_paymentMethod.setCalcCurrentAuthAmount(l_balanceAmt);
			p_invoiceLineChargeDetail.setInterimOrderLineTotal(0F);
		}
		if (l_authAmt != p_paymentMethod.getCalcCurrentAuthAmount()) {
			l_respLine = new ResponseInvoiceLineCharge();
			l_respLine.setChargeDetailId(
					p_invoiceLineChargeDetail.getChargeDetailId()
			);
			l_respLine.setInvoiceLineId(
					p_invoiceLineChargeDetail.getInvoiceLineId()
			);
			l_respLine.setPaymentAmount(l_respAmt);
			l_paymentTypeId = p_paymentMethod.getPaymentMethodId();

			listPaymentMethod = updatePaymentMethod(
					p_settlementTender,
					l_paymentTypeId, l_respLine
			);
			p_settlementTender.setPaymentMethod(listPaymentMethod);

		}

	}

	// -----------------------------------------------------------------------------

	/**
	 * 
	 * @param p_responseOpenInvoice
	 * @param p_paymentTypeId
	 * @param p_respLine
	 * @return
	 */
	private List<ResponsePaymentMethod> updatePaymentMethod(
			ResponseTender p_responseOpenInvoice,
			String p_paymentTypeId,
			ResponseInvoiceLineCharge p_respLine
	)
	{
		ResponsePaymentMethod l_respPaymentMethod = null;
		List<ResponsePaymentMethod> l_listPaymentMethod = null;
		List<ResponseInvoiceLineCharge> l_respLineList = null;
		int l_tempIndex = -1;

		if (p_responseOpenInvoice.getPaymentMethod() == null) {
			l_respPaymentMethod = new ResponsePaymentMethod();
			l_respPaymentMethod.setPaymentMethodId(p_paymentTypeId);
			l_respLineList = new ArrayList<ResponseInvoiceLineCharge>();
			l_respLineList.add(p_respLine);
			l_listPaymentMethod = new ArrayList<ResponsePaymentMethod>();
			l_respPaymentMethod.setRespInvoiceLineCharge(l_respLineList);
			l_listPaymentMethod.add(l_respPaymentMethod);

		} else {

			l_listPaymentMethod = p_responseOpenInvoice.getPaymentMethod()
					.stream()
					.filter(
							paymentObj -> p_paymentTypeId
									.equals(paymentObj.getPaymentMethodId())
					)
					.collect(Collectors.toList());
			if (l_listPaymentMethod.size() == 0) {
				l_respLineList = new ArrayList<ResponseInvoiceLineCharge>();
				l_respLineList.add(p_respLine);
				l_listPaymentMethod = p_responseOpenInvoice
						.getPaymentMethod();
				l_respPaymentMethod = new ResponsePaymentMethod();
				l_respPaymentMethod.setPaymentMethodId(p_paymentTypeId);
				l_respPaymentMethod.setRespInvoiceLineCharge(l_respLineList);
				l_listPaymentMethod.add(l_respPaymentMethod);

			} else {

				l_tempIndex = IntStream
						.range(
								0,
								p_responseOpenInvoice.getPaymentMethod()
										.size()
						)
						.filter(
								i -> p_responseOpenInvoice
										.getPaymentMethod().get(i)
										.getPaymentMethodId().equals(
												p_paymentTypeId
										)
						)
						.findFirst().orElse(0);
				l_listPaymentMethod = p_responseOpenInvoice
						.getPaymentMethod();
				l_respPaymentMethod = l_listPaymentMethod.get(l_tempIndex);
				l_respLineList = new ArrayList<ResponseInvoiceLineCharge>();
				l_respLineList.add(p_respLine);
				l_respPaymentMethod.setRespInvoiceLineCharge(l_respLineList);
				// l_listPaymentMethod.add(l_respPaymentMethod);

			}

			//

		}

		return l_listPaymentMethod;
	}

	// -----------------------------------------------------------------------------

	/**
	 * This method iterate the payment method if its more then one and route to
	 * calculateReturns method.
	 * 
	 * @param p_invoiceLine
	 *            : the invoice of item received for Retruns in request.
	 * @param p_refundTender
	 *            : response object for openInvoices.
	 * @param p_sortedPaymentMethod
	 * @param p_openInvoice
	 *            : contains the array of invoice lines.
	 * @throws Exception
	 */
	public void splitingReturnsAuth(
			InvoiceLine p_invoiceLine,
			ResponseTender p_refundTender, List<
					PaymentMethod> p_sortedPaymentMethod,
			String openInvoiceId, String p_invoiceType
	)
	{

		for (PaymentMethod paymentMethod : p_sortedPaymentMethod) {

			calculateReturns(
					paymentMethod, p_invoiceLine,
					p_refundTender, openInvoiceId, p_invoiceType
			);
		}

	}

	// -----------------------------------------------------------------------------

	/**
	 * 
	 * @param p_paymentMethod
	 * @param p_invoiceLine
	 * @param p_responseOpenInvoice
	 * @param openInvoiceId
	 */
	public void calculateReturns(
			PaymentMethod p_paymentMethod, InvoiceLine p_invoiceLine,
			ResponseTender p_refundTender, String openInvoiceId,
			String p_invoiceType
	)
	{
		if (p_paymentMethod.getCalcCurrentSettledAmount() == 0) {
			return;
		}
		Float l_balanceAmt = 0F;
		Float l_respAmt = 0F;
		String l_paymentTypeId = null;
		List<ResponsePaymentMethod> listPaymentMethod = null;
		ResponseInvoiceLine l_respLine = null;

		float l_authAmt = p_paymentMethod.getCalcCurrentSettledAmount();
			
		if (
			p_invoiceLine.getInterimOrderLineTotal() >= p_paymentMethod
					.getCalcCurrentSettledAmount()
		)
		{
			l_respAmt = p_paymentMethod.getCalcCurrentSettledAmount();
			l_balanceAmt = p_invoiceLine.getInterimOrderLineTotal()
					- l_respAmt;

			p_paymentMethod.setCalcCurrentSettledAmount(0F);
			p_invoiceLine.setInterimOrderLineTotal(l_balanceAmt);

		} else {
			l_respAmt = p_invoiceLine.getInterimOrderLineTotal();
			l_balanceAmt = p_paymentMethod.getCalcCurrentSettledAmount()
					- l_respAmt;
			p_paymentMethod.setCalcCurrentSettledAmount(l_balanceAmt);
			p_invoiceLine.setInterimOrderLineTotal(0F);

		}
		if (l_authAmt != p_paymentMethod.getCalcCurrentSettledAmount()) {
			p_refundTender.setInvoiceId(openInvoiceId);
			l_respLine = new ResponseInvoiceLine();
			l_respLine.setInvoiceLineId(p_invoiceLine.getItemId());
			l_respLine.setPaymentAmount(l_respAmt);
			l_paymentTypeId = p_paymentMethod.getPaymentMethodId();
			p_refundTender.setInvoiceTypeId(p_invoiceType);
			listPaymentMethod = updatePaymentMethod(
					p_refundTender,
					l_paymentTypeId, l_respLine
			);
			p_refundTender.setPaymentMethod(listPaymentMethod);

		}

	}

	// -----------------------------------------------------------------------------
	/**
	 * 
	 * @param p_openAuthReversal
	 * @param p_authReversalTender
	 * @param p_sortedPaymentMethod
	 * @param p_authPlan
	 */
	public void splitingAuthReversal(
			OpenAuthReversal p_openAuthReversal,
			ResponseTender p_authReversalTender,
			List<PaymentMethod> p_sortedPaymentMethod,
			LinkedHashMap<String, Float> p_authPlan
	)
	{
		for (PaymentMethod paymentMethod : p_sortedPaymentMethod) {

			calculateAuthReversal(
					p_openAuthReversal,
					paymentMethod,
					p_authReversalTender,
					p_authPlan
			);
		}
	}

	// -----------------------------------------------------------------------------

	/**
	 * 
	 * @param p_openAuthReversal
	 * @param p_paymentMethod
	 * @param p_authReversalTender
	 * @param p_authPlan
	 */
	public void calculateAuthReversal(
			OpenAuthReversal p_openAuthReversal,
			PaymentMethod p_paymentMethod,
			ResponseTender p_authReversalTender,
			LinkedHashMap<String, Float> p_authPlan
	)
	{

		if (p_paymentMethod.getCalcCurrentAuthAmount() == 0) {
			return;
		}

		Float l_balanceAmt = 0F;
		Float l_respAmt = 0F;
		String l_paymentTypeId = null;
		List<ResponsePaymentMethod> listPaymentMethod = null;
		String l_cardType = null;
		float l_perbucketAmt = 0;
		float l_muBalLeft = 0;
		String l_card = null;

		float l_orderLineAmt = p_openAuthReversal.getAmount();
		float l_authAmt = p_paymentMethod.getCalcCurrentAuthAmount();
		l_cardType = p_paymentMethod.getCardType().getCardTypeId();
		if (
			(CARD_TYPE_U).equals(l_cardType) || (CARD_TYPE_R).equals(l_cardType)
		)
		{
			if ((CARD_TYPE_U).equals(l_cardType)) {
				l_card = TENDER_MILITARY_UNIFORM;
				l_perbucketAmt = p_authPlan.get(l_card);
			} else {
				l_card = TENDER_MILTARY_RETAIL;
				l_perbucketAmt = p_authPlan.get(l_card);
			}

			if (p_paymentMethod.getCalcCurrentAuthAmount() >= l_perbucketAmt) {

				l_muBalLeft = p_paymentMethod.getCalcCurrentAuthAmount()
						- l_perbucketAmt;

				if (l_muBalLeft >= l_orderLineAmt) {// think abt intermAmount
													// here
					l_balanceAmt = l_muBalLeft
							- l_orderLineAmt;
					p_paymentMethod.setCalcCurrentAuthAmount(l_balanceAmt);
					p_openAuthReversal.setInterimAmount(0F);

					l_respAmt = l_orderLineAmt;
				} else {
					l_balanceAmt = l_orderLineAmt
							- l_muBalLeft;
					l_respAmt = l_muBalLeft;
					p_paymentMethod.setCalcCurrentAuthAmount(l_perbucketAmt);
					p_openAuthReversal.setInterimAmount(l_balanceAmt);
				}
			} else {
				LOGGER.info(
						"No need to process ad per-bucket amount is greater then Auth amt"
				);
				return;

			}

		} else {
			if (
				p_openAuthReversal.getInterimAmount() >= p_paymentMethod
						.getCalcCurrentAuthAmount()
			)
			{
				l_respAmt = p_paymentMethod.getCalcCurrentAuthAmount();
				l_balanceAmt = p_openAuthReversal.getInterimAmount()
						- l_respAmt;

				p_paymentMethod.setCalcCurrentAuthAmount(0F);
				p_openAuthReversal.setInterimAmount(l_balanceAmt);

			} else {
				l_respAmt = p_openAuthReversal.getInterimAmount();
				l_balanceAmt = p_paymentMethod.getCalcCurrentAuthAmount()
						- l_respAmt;
				p_paymentMethod.setCalcCurrentAuthAmount(l_balanceAmt);
				p_openAuthReversal.setInterimAmount(0F);

			}

		}
		if (l_authAmt != p_paymentMethod.getCalcCurrentAuthAmount()) {
			p_authReversalTender.setPaymentTransactionId(
					p_openAuthReversal != null
							? p_openAuthReversal.getPaymentTransactionId()
							: "No transaction Id"
			);
			l_paymentTypeId = p_paymentMethod.getPaymentMethodId();
			p_authReversalTender = updatePaymentMethod(
					p_authReversalTender,
					l_paymentTypeId, l_respAmt
			);

		}

	}

	// -----------------------------------------------------------------------------

	/**
	 * 
	 * @param p_authReversalTender
	 * @param p_paymentTypeId
	 * @param p_respAmt
	 * @return
	 */
	private ResponseTender updatePaymentMethod(
			ResponseTender p_authReversalTender, String p_paymentTypeId,
			Float p_respAmt
	)
	{
		ResponsePaymentMethod l_respPaymentMethod = null;
		List<ResponsePaymentMethod> l_listPaymentMethod = new ArrayList<
				ResponsePaymentMethod>();

		if (p_authReversalTender.getPaymentMethod() == null) {

			LOGGER.info("inside IF");
			l_respPaymentMethod = new ResponsePaymentMethod();
			l_respPaymentMethod.setPaymentMethodId(p_paymentTypeId);
			l_respPaymentMethod.setPaymentAmount(p_respAmt);
			l_listPaymentMethod.add(l_respPaymentMethod);
			p_authReversalTender.setPaymentMethod(l_listPaymentMethod);
		} else {

			l_listPaymentMethod = p_authReversalTender.getPaymentMethod()
					.stream()
					.filter(
							paymentObj -> p_paymentTypeId
									.equals(paymentObj.getPaymentMethodId())
					)
					.collect(Collectors.toList());

			l_respPaymentMethod = new ResponsePaymentMethod();
			l_respPaymentMethod.setPaymentMethodId(p_paymentTypeId);
			l_respPaymentMethod.setPaymentAmount(p_respAmt);
			l_listPaymentMethod = p_authReversalTender.getPaymentMethod();
			l_listPaymentMethod.add(l_respPaymentMethod);
			p_authReversalTender.setPaymentMethod(l_listPaymentMethod);

		}

		return p_authReversalTender;
	}
	// -----------------------------------------------------------------------------
	
	private float calculateFollowUpAmout(PaymentMethod p_paymentMethod) {
		if(!p_paymentMethod.getExtended().isNewRefundGiftCard()) {
			return 0f;
		}
		
		p_paymentMethod.getPaymentTransaction().forEach(
			transaction -> {
				transaction.getFollowOnParentTransaction();
			}
		);
		
		return 0.0f;
	}
	// -----------------------------------------------------------------------------
}
// -----------------------------------------------------------------------------
// END OF FILE
// -----------------------------------------------------------------------------
