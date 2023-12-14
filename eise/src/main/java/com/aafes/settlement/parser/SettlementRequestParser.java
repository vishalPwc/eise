package com.aafes.settlement.parser;

import java.util.StringJoiner;
import java.util.concurrent.atomic.AtomicBoolean;
// ----------------------------------------------------------------------------
import java.util.stream.Collectors;

import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import com.aafes.settlement.constant.Constants;
import com.aafes.settlement.constant.ErrorConstants;
import com.aafes.settlement.core.invoice.ClosedInvoice;
import com.aafes.settlement.core.invoice.InvoiceChargeDetail;
import com.aafes.settlement.core.invoice.InvoiceLine;
import com.aafes.settlement.core.invoice.OpenInvoice;
import com.aafes.settlement.core.model.ProcessingContainer;
import com.aafes.settlement.core.model.settlement.SettlementRO;
import com.aafes.settlement.core.order.OrderLine;
import com.aafes.settlement.core.payment.PaymentHeader;
import com.aafes.settlement.core.payment.PaymentMethod;
import com.aafes.settlement.core.response.PaymentMethodResponse;
import com.aafes.settlement.exception.NodeNotFoundException;
import com.aafes.settlement.repository.RepoContainer;
import com.aafes.settlement.util.RepoUtils;
import com.aafes.settlement.util.Utils;
import com.aafes.settlement.validators.SettlementValidator;
import com.aafes.settlement.validators.ValidationErrors;
import com.fasterxml.jackson.databind.ObjectMapper;

// ----------------------------------------------------------------------------
/**
 * This class distinguishes the Settlement request into an object of
 * ProcessingContainer and validates the same for mandatory nodes and
 * attributes.
 */
@Service
public class SettlementRequestParser
	implements Constants, ErrorConstants
{

	// ------------------------------------------------------------------------
	private static final Logger LOGGER = LogManager
			.getLogger(SettlementRequestParser.class);

	// ------------------------------------------------------------------------
	/**
	 * This method calls various distinguishing methods which parse and fill an
	 * object of ProcessingContainer.
	 * 
	 * @param p_settlementRequest
	 * @param p_validationErrors
	 * @param p_settlementValidator
	 * @return
	 * @throws NodeNotFoundException
	 *             when a method validating respective nodes/attributes does not
	 *             find a mandatory node/attribute or if it has an invalid
	 *             value.
	 */
	public ProcessingContainer settlementParser(
			SettlementRO p_settlementRequest,
			ValidationErrors p_validationErrors,
			SettlementValidator p_settlementValidator,
			RepoContainer p_repoContainer
	)
			throws NodeNotFoundException
	{
		// create instance of Processing Container
		ProcessingContainer l_settlementBucket = new ProcessingContainer();

		LOGGER.debug("Distinguishing PaymentMethod node");
		// iterate over Payment methods and separate them to different buckets
		distinguishPaymentMethods(
				p_settlementRequest, l_settlementBucket,
				p_repoContainer, p_settlementValidator, p_validationErrors
		);

		LOGGER.debug("Distinguishing OpenInvoice.InvoiceLine node");
		// iterate Invoice Lines and Separate them to different Buckets
		distinguishInvoiceLines(
				p_settlementRequest, l_settlementBucket,
				p_repoContainer, p_settlementValidator, p_validationErrors
		);

		LOGGER.debug("Distinguishing OpenInvoice.InvoiceChargeDetail node");
		// iterate Invoice Lines and Separate Shipping charges in each invoice
		// line
		distinguishShippingCharges(
				p_settlementRequest, l_settlementBucket, p_settlementValidator,
				p_validationErrors
		);

		LOGGER.debug("Distinguishing OrderLine node");
		// iterate over original order lines to get MU , MR and Order Line
		// Totals
		distinguishOrderLineTotals(
				p_settlementRequest, l_settlementBucket, p_settlementValidator,
				p_validationErrors
		);

		LOGGER.debug("Distinguishing ClosedInvoice node");
		// iterate over closed invoices to get returned MU, MR and Order Line
		// totals
		distinguishReturnedLineTotals(
				p_settlementRequest, l_settlementBucket, p_settlementValidator,
				p_validationErrors
		);

		return l_settlementBucket;
	}

	// ------------------------------------------------------------------------
	/**
	 * Filter all the invoice charges = shipping from all open invoices
	 * 
	 * @param p_settlementRequest
	 * @param p_settlementBucket
	 */
	private void distinguishShippingCharges(
			SettlementRO p_settlementRequest,
			ProcessingContainer p_settlementBucket,
			SettlementValidator p_settlementValidator,
			ValidationErrors p_validationErrors
	)
	{
		p_settlementBucket.getShippingChargeQueue().addAll(
				p_settlementRequest
						.getOpenInvoice().stream()
						.filter(
								l_currentInvoice -> l_currentInvoice
										.getInvoiceChargeDetail() != null
						)
						.flatMap(
								l_currentInvoice -> l_currentInvoice
										.getInvoiceChargeDetail().stream()
										.filter(
												l_chargeDetail -> p_settlementValidator
														.validateInvoiceChargeDetail(
																l_chargeDetail,
																p_validationErrors
														)
										)
										/*
										 * .filter( l_chargeDetail ->
										 * l_chargeDetail .getChargeType() !=
										 * null ) .filter( l_chargeDetail ->
										 * l_chargeDetail .getChargeType()
										 * .getChargeTypeId() != null ) .filter(
										 * l_chargeDetail -> l_chargeDetail
										 * .getChargeType() .getChargeTypeId()
										 * .equals( SHIPPING_CHARGES ) )
										 */
										.filter(/*
												 * using this filter to just set
												 * the invoiceId avoiding
												 * another for loop
												 */ l_chargeType -> setChargeInvoiceId(
												p_settlementBucket,
												l_chargeType,
												l_currentInvoice
										))
						)
						.collect(Collectors.toList())
		);

		p_settlementBucket.getShippingChargeQueue()
				.forEach(
						invoiceChargeDetail -> p_settlementBucket
								.setInvoiceChargeTotal(
										p_settlementBucket
												.getInvoiceChargeTotal()
												.add(
														invoiceChargeDetail
																.getChargeTotal()
												)
								)
				);
	}

	// ------------------------------------------------------------------------
	/**
	 * 
	 * @param p_adjustmentBucket
	 * @param pInvoiceChargeDetail
	 * @param pOpenInvoice
	 * @return
	 */
	private boolean setChargeInvoiceId(
			ProcessingContainer p_settlementBucket,
			InvoiceChargeDetail pInvoiceChargeDetail,
			OpenInvoice pOpenInvoice
	)
	{
		pInvoiceChargeDetail.setInvoiceId(pOpenInvoice.getInvoiceId());
		p_settlementBucket
				.setShippingCharge(
						p_settlementBucket.getShippingCharge()
								.add(pInvoiceChargeDetail.getChargeTotal())
				);
		return true;
	}

	// ------------------------------------------------------------------------
	/**
	 * Method will identify Invoice lines for Mil-uniform and Mil-retail plan
	 * and sum up the total for each of them
	 * 
	 * @param p_settlementRequest
	 * @param p_settlementBucket
	 * @param p_settlementValidator
	 * @param p_validationErrors
	 */
	private void distinguishInvoiceLines(
			SettlementRO p_settlementRequest,
			ProcessingContainer p_settlementBucket,
			RepoContainer p_repoContainer,
			SettlementValidator p_settlementValidator,
			ValidationErrors p_validationErrors
	)
	{
		StringJoiner l_invoiceIds = new StringJoiner(",");
		OpenInvoice l_openInvoice = null;
		for (OpenInvoice l_currentOpenInvoice : p_settlementRequest
				.getOpenInvoice())
		{
			l_openInvoice = l_currentOpenInvoice;
			p_settlementValidator.validateOpenInvoice(
					l_currentOpenInvoice,
					p_validationErrors
			);

			if (
				l_currentOpenInvoice.getInvoiceLine() != null
						&& ADJUSTMENT_INVOICE
								.equalsIgnoreCase(
										l_currentOpenInvoice.getInvoiceType()
												.getInvoiceTypeId()
								)
			)
			{

				p_settlementRequest.setAdjustmentInvoicePresent(true);
			}

			if (
				l_currentOpenInvoice.getInvoiceLine() != null && SHIPPED_INVOICE
						.equalsIgnoreCase(
								l_currentOpenInvoice.getInvoiceType()
										.getInvoiceTypeId()
						)
			)
			{
				for (InvoiceLine l_currentLine : l_currentOpenInvoice
						.getInvoiceLine())
				{
					// if !l_currentLine.IsRefundGiftCard
					if (!l_currentLine.isRefundGiftCard()) {
						p_settlementValidator.validateInvoiceLine(
								l_currentLine, p_validationErrors,
								p_settlementBucket.isMilstarBucket()
						);

						l_currentLine.setInvoiceId(
								l_currentOpenInvoice.getInvoiceId()
						);
						if (p_settlementBucket.isMilstarBucket()) {
							switch (l_currentLine.getExtended()
									.getResponsePlan())
							{
								case PLAN_MILITARY_UNIFORM:
									p_settlementBucket.getMilitaryUniformLines()
											.add(l_currentLine);
									p_settlementBucket
											.setMilitaryUninformLinesTotal(
													p_settlementBucket
															.getMilitaryUninformLinesTotal()
															.add(
																	l_currentLine
																			.getInvoiceLineTotal()
															)
											);
									break;
								case PLAN_MILITARY_RETAIL:
									p_settlementBucket.getMilitaryPromoLines()
											.add(l_currentLine);
									// if promo-plan is null only then add the
									// the
									// given
									// line's invoice total to total.
									if (
										l_currentLine.getExtended()
												.getPromoPlan() != null
									)
									{
										p_settlementBucket
												.setMilitaryPromoLinesTotalDouble(
														p_settlementBucket
																.getMilitaryPromoLinesTotalDouble()
																.add(
																		l_currentLine
																				.getInvoiceLineTotal()
																)
												);
									}
									break;
							}
						}
						p_settlementBucket.getOpenInvoiceLines().add(
								l_currentLine
						);
						p_settlementBucket.setOpenInvoiceLineTotal(
								p_settlementBucket.getOpenInvoiceLineTotal()
										.add(
												l_currentLine
														.getInvoiceLineTotal()
										)
						);
					}
				}
			}
			l_invoiceIds.add(l_currentOpenInvoice.getInvoiceId());
		}
		// Create copy of OpenInvoice object
		try {

			ObjectMapper objectMapper = new ObjectMapper();

			OpenInvoice l_openInvoiceCopy = objectMapper
					.readValue(
							objectMapper.writeValueAsString(l_openInvoice),
							OpenInvoice.class
					);
			l_openInvoiceCopy.setInvoiceId(l_invoiceIds.toString());
			RepoUtils.addToOrderInvoiceData(
					l_openInvoiceCopy, p_repoContainer
			);
		} catch (Exception l_e) {
			LOGGER.error(
					"Error while creating OrderInvoiceData:" +
							l_e.getLocalizedMessage()
			);
		}

	}

	// ------------------------------------------------------------------------
	/**
	 * This method distinguishes different payment based on:
	 * <ol>
	 * <li>Milstar Uniform
	 * <li>Milstar Retail
	 * <li>Credit Card
	 * <li>Gift Card
	 * </ol>
	 * 
	 * @param p_settlementRequest
	 * @param p_settlementBucket
	 * @param p_validationErrors
	 * @throws NodeNotFoundException
	 *             if the required nodes/attributes are not found in
	 *             validatePaymentMethod()
	 */
	private void distinguishPaymentMethods(
			SettlementRO p_settlementRequest,
			ProcessingContainer p_settlementBucket,
			RepoContainer p_repoContainer,
			SettlementValidator p_settlementValidator,
			ValidationErrors p_validationErrors
	)
			throws NodeNotFoundException
	{
		AtomicBoolean isMUCardPresent = new AtomicBoolean(false);
		AtomicBoolean isMRCardPresent = new AtomicBoolean(false);

		for (PaymentHeader l_paymentHeader : p_settlementRequest
				.getPaymentHeader())
		{
			p_settlementValidator.validatePaymentHeader(
					l_paymentHeader,
					p_validationErrors
			);

			for (PaymentMethod l_currentMethod : l_paymentHeader
					.getPaymentMethod())
			{
				LOGGER.info(
						"Validating PaymentMethod: "
								+ l_currentMethod.getPaymentMethodId()
								+ " for mandatory nodes and attributes"
				);

				p_settlementBucket.getPaymentMethodResponses().put(
						l_currentMethod.getPaymentMethodId(),
						new PaymentMethodResponse()
				);

				p_settlementValidator.validatePaymentMethod(
						l_currentMethod, p_validationErrors, isMUCardPresent,
						isMRCardPresent
				);

				// if currentAuth amount of Card is > 0, then only add
				// otherwise skip
				if (
					Utils.isValueGreater(
							l_currentMethod.getCurrentAuthAmount(), ZERO_VALUE
					)
				)
				{
					String l_paymentType = l_currentMethod.getPaymentType()
							.getPaymentTypeId();

					if (l_paymentType.equalsIgnoreCase(CREDIT_CARD)) {
						if (
							l_currentMethod.getCardType().getCardTypeId()
									.equalsIgnoreCase(MILSTAR_CARD)
						)
						{
							p_settlementBucket.setMilstarBucket(true);
							switch (l_currentMethod.getExtended()
									.getResponsePlan())
							{
								case PLAN_MILITARY_UNIFORM:
									p_settlementBucket
											.setTotalPaymentUniformAuth(
													p_settlementBucket
															.getTotalPaymentUniformAuth()
															.add(
																	l_currentMethod
																			.getCurrentAuthAmount()
															)
											);
									p_settlementBucket
											.setMilstarUniformCard(
													l_currentMethod
											);

									p_settlementBucket.setTotalPaymentAuth(
											p_settlementBucket
													.getTotalPaymentAuth()
													.add(
															l_currentMethod
																	.getCurrentAuthAmount()
													)
									);

									break;
								case PLAN_MILITARY_RETAIL:
									p_settlementBucket
											.setTotalPaymentRetailAuth(
													p_settlementBucket
															.getTotalPaymentRetailAuth()
															.add(
																	l_currentMethod
																			.getCurrentAuthAmount()
															)
											);
									p_settlementBucket.getSettlementCCMethods()
											.add(l_currentMethod);

									p_settlementBucket
											.setMilstarRetailCard(
													l_currentMethod
											);

									p_settlementBucket.setTotalPaymentAuth(
											p_settlementBucket
													.getTotalPaymentAuth()
													.add(
															l_currentMethod
																	.getCurrentAuthAmount()
													)
									);

									break;
							}
						} else {
							p_settlementBucket.getSettlementCCMethods()
									.add(l_currentMethod);
							p_settlementBucket.setTotalPaymentAuth(
									p_settlementBucket.getTotalPaymentAuth()
											.add(
													l_currentMethod
															.getCurrentAuthAmount()
											)
							);

						}
					} else if (l_paymentType.equalsIgnoreCase(GIFT_CARD)) {
						/*
						 * Checks if the current gift card is a refund for an
						 * appeasement. Refer EOR-1729 for more info.
						 */
						if (
							l_currentMethod.getExtended() != null
									&& !l_currentMethod.getExtended()
											.isNewRefundGiftCard()
						)
						{
							p_settlementBucket.getSettlementGCMethods()
									.add(l_currentMethod);
							p_settlementBucket.setTotalPaymentAuth(
									p_settlementBucket.getTotalPaymentAuth()
											.add(
													l_currentMethod
															.getCurrentAuthAmount()
											)
							);
						}
					}
				}
				// Repository change
				RepoUtils.addToInvoiceItemSettlementList(
						l_currentMethod, p_repoContainer
				);
			}
		}
	}

	// ------------------------------------------------------------------------
	/**
	 * Separate the order total based on MU, MR
	 * 
	 * @param p_settlementRequest
	 * @param p_settlementBucket
	 * @param p_validationErrors
	 */
	private void distinguishOrderLineTotals(
			SettlementRO p_settlementRequest,
			ProcessingContainer p_settlementBucket,
			SettlementValidator p_settlementValidator,
			ValidationErrors p_validationErrors
	)
	{
		for (OrderLine l_orderLine : p_settlementRequest.getOrderLine()) {

			// if !l_orderLine.IsRefundGiftCard
			if (!l_orderLine.isRefundGiftCard()) {
				double l_lineItemTotal = l_orderLine.getOrderLineTotal().doubleValue();
				if (Utils.isMilstarCard(p_settlementRequest) && l_lineItemTotal == 0
						&& null != l_orderLine && null != l_orderLine.getExtended()
						&& StringUtils.isEmpty(l_orderLine.getExtended().getResponsePlan())) {
					l_orderLine.getExtended().setResponsePlan(PLAN_MILITARY_RETAIL);
				}
				p_settlementValidator.validateOrderLines(
						l_orderLine, p_validationErrors, p_settlementBucket
								.isMilstarBucket()
				);

				if (p_settlementBucket.isMilstarBucket()) {
					switch (l_orderLine.getExtended().getResponsePlan()) {
						case PLAN_MILITARY_UNIFORM:
							p_settlementBucket.setTotalMUOrderedAmount(
									p_settlementBucket.getTotalMUOrderedAmount()
											.add(
													l_orderLine
															.getOrderLineTotal()
											)
							);
							break;
						case PLAN_MILITARY_RETAIL:
							p_settlementBucket.setTotalMROrderedAmount(
									p_settlementBucket.getTotalMROrderedAmount()
											.add(
													l_orderLine
															.getOrderLineTotal()
											)
							);
							break;

					}
				}
				p_settlementBucket.setTotalOrderedAmount(
						p_settlementBucket.getTotalOrderedAmount()
								.add(l_orderLine.getOrderLineTotal())
				);

			}
		}
	}

	// ------------------------------------------------------------------------
	/**
	 * Get MU, MR buckets amounts which are already returned
	 * 
	 * @param p_settlementRequest
	 * @param p_settlementBucket
	 * @param p_validationErrors
	 */
	private void distinguishReturnedLineTotals(
			SettlementRO p_settlementRequest,
			ProcessingContainer p_settlementBucket,
			SettlementValidator p_settlementValidator,
			ValidationErrors p_validationErrors
	)
	{
		if (
			Utils.isNull(p_settlementRequest.getClosedInvoices())
		)
		{
			return;
		}

		for (ClosedInvoice l_closedInvoice : p_settlementRequest
				.getClosedInvoices())
		{
			for (InvoiceLine l_currentLine : l_closedInvoice.getInvoiceLine()) {
				if (
					l_closedInvoice.getInvoiceType() != null
							&& l_closedInvoice.getInvoiceType()
									.getInvoiceTypeId() != null
							&& SHIPPED_INVOICE.equalsIgnoreCase(
									l_closedInvoice.getInvoiceType()
											.getInvoiceTypeId()
							)
				)
				{
					double l_lineItemTotal = l_currentLine.getInvoiceLineTotal().doubleValue();
					if (Utils.isMilstarCard(p_settlementRequest) && l_lineItemTotal == 0
							&& null != l_currentLine && null != l_currentLine.getExtended()
							&& StringUtils.isEmpty(l_currentLine.getExtended().getResponsePlan())) {
						l_currentLine.getExtended().setResponsePlan(PLAN_MILITARY_RETAIL);
					}
					p_settlementValidator.validateClosedInvoiceLine(
							l_currentLine, p_validationErrors,
							p_settlementBucket.isMilstarBucket()
					);

					if (p_settlementBucket.isMilstarBucket()) {
						switch (l_currentLine.getExtended().getResponsePlan()) {
							case PLAN_MILITARY_UNIFORM:
								p_settlementBucket.setTotalMUReturnedAmount(
										p_settlementBucket
												.getTotalMUReturnedAmount()
												.add(
														l_currentLine
																.getInvoiceLineTotal()
												)
								);
								break;
							case PLAN_MILITARY_RETAIL:
								p_settlementBucket.setTotalMRReturnedAmount(
										p_settlementBucket
												.getTotalMRReturnedAmount()
												.add(
														l_currentLine
																.getInvoiceLineTotal()
												)
								);
								break;

						}
					}
					p_settlementBucket.setTotalReturnedAmount(
							p_settlementBucket.getTotalReturnedAmount()
									.add(l_currentLine.getInvoiceLineTotal())
					);
				}
			}
		}
	}
	// -------------------------------------------------------------------------
}
// ----------------------------------------------------------------------------
// END OF FILE
// ----------------------------------------------------------------------------