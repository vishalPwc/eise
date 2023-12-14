package com.aafes.settlement.parser;

import java.util.StringJoiner;
import java.util.concurrent.atomic.AtomicBoolean;
// -----------------------------------------------------------------------------
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import com.aafes.settlement.constant.Constants;
import com.aafes.settlement.core.invoice.ClosedInvoice;
import com.aafes.settlement.core.invoice.InvoiceChargeDetail;
import com.aafes.settlement.core.invoice.InvoiceLine;
import com.aafes.settlement.core.model.ProcessingContainer;
import com.aafes.settlement.core.model.adjustment.AdjustmentInvoiceLine;
import com.aafes.settlement.core.model.adjustment.AdjustmentOpenInvoice;
import com.aafes.settlement.core.model.adjustment.AdjustmentProcessingContainer;
import com.aafes.settlement.core.model.adjustment.AdjustmentRO;
import com.aafes.settlement.core.order.OrderLine;
import com.aafes.settlement.core.payment.PaymentHeader;
import com.aafes.settlement.core.payment.PaymentMethod;
import com.aafes.settlement.core.response.PaymentMethodResponse;
import com.aafes.settlement.exception.NodeNotFoundException;
import com.aafes.settlement.repository.RepoContainer;
import com.aafes.settlement.util.ParserUtils;
import com.aafes.settlement.util.RepoUtils;
import com.aafes.settlement.util.Utils;
import com.aafes.settlement.validators.AdjustmentValidator;
import com.aafes.settlement.validators.ValidationErrors;
import com.fasterxml.jackson.databind.ObjectMapper;

// ----------------------------------------------------------------------------
/**
 * This class distinguishes the Adjustment request into an object of
 * AdjustmentProcessingContainer and validates the same for mandatory nodes and
 * attributes.
 */
@Service
public class AdjustmentRequestParser
	implements Constants
{
	// ------------------------------------------------------------------------
	private static final Logger LOGGER = LogManager
			.getLogger(AdjustmentRequestParser.class);

	// ------------------------------------------------------------------------
	/**
	 * This method calls various distinguishing methods which parse and fill an
	 * object of AdjustmentProcessingContainer.
	 * 
	 * @param p_adjustmentRequest
	 * @param p_validationErrors
	 * @param p_adjustmentValidator
	 * @return
	 * @throws NodeNotFoundException
	 *             when a method validating respective nodes/attributes does not
	 *             find a mandatory node/attribute or if it has an invalid
	 *             value.
	 */
	public AdjustmentProcessingContainer adjustmentParser(
			AdjustmentRO p_adjustmentRequest,
			ValidationErrors p_validationErrors,
			AdjustmentValidator p_adjustmentValidator,
			RepoContainer p_repoContainer
	)
			throws NodeNotFoundException
	{

		// create instance of Adjustment Container
		AdjustmentProcessingContainer l_adjustmentBucket = new AdjustmentProcessingContainer();

		LOGGER.debug("Distinguishing PaymentMethod node");
		// iterate Payment methods and separate them into different buckets
		distinguishPaymentMethods(
				p_adjustmentRequest, l_adjustmentBucket, p_repoContainer,
				p_validationErrors, p_adjustmentValidator
		);

		LOGGER.debug("Distinguishing OpenInvoice.InvoiceLine node");
		// iterate Invoice Lines and separate them into different buckets
		distinguishInvoiceLine(
				p_adjustmentRequest, l_adjustmentBucket, p_repoContainer,
				p_validationErrors, p_adjustmentValidator
		);

		LOGGER.debug("Distinguishing OpenInvoice.InvoiceChargeDetail node");
		// iterate Invoice Lines and Separate Shipping charges in each invoice
		// line
		distinguishShippingCharges(
				p_adjustmentRequest, l_adjustmentBucket, p_validationErrors,
				p_adjustmentValidator
		);

		LOGGER.debug("Distinguishing OrderLine node");
		// iterate over original order lines to get MU , MR and Order Line
		// Totals
		distinguishOrderLineTotals(
				p_adjustmentRequest, l_adjustmentBucket, p_validationErrors,
				p_adjustmentValidator
		);

		LOGGER.debug("Distinguishing ClosedInvoice node");
		// iterate over closed invoices to get returned MU, MR and Order Line
		// totals
		// and for MU & MR payment method amount is considered
		distinguishReturnedLineTotals(
				p_adjustmentRequest, l_adjustmentBucket, p_validationErrors,
				p_adjustmentValidator
		);

		// enrichOrderDetails(p_adjustmentRequest, l_adjustmentBucket);
		return l_adjustmentBucket;
	}

	/**
	 * Filter all the invoice charges = shipping from all open invoices
	 * 
	 * @param p_adjustmentRequest
	 * @param p_adjustmentBucket
	 */
	private void distinguishShippingCharges(
			AdjustmentRO p_adjustmentRequest,
			AdjustmentProcessingContainer p_adjustmentBucket,
			ValidationErrors p_validationErrors,
			AdjustmentValidator p_adjustmentValidator
	)
			throws NodeNotFoundException
	{
		p_adjustmentBucket.getShippingChargeQueue().addAll(
				p_adjustmentRequest
						.getOpenInvoice().stream()
						.filter(
								l_currentInvoice -> l_currentInvoice
										.getInvoiceChargeDetail() != null
						)
						.flatMap(
								l_currentInvoice -> l_currentInvoice
										.getInvoiceChargeDetail().stream()
										.filter(
												l_chargeDetail -> p_adjustmentValidator
														.validateInvoiceChargeDetails(
																l_chargeDetail,
																p_validationErrors
														)
										)
										.filter(
												l_chargeType -> l_chargeType
														.getChargeType()
														.getChargeTypeId()
														.equals(
																APPEASEMENT_CHARGE
														)
										)
										.filter(/*
												 * using this filter to just set
												 * the invoiceId avoiding
												 * another for loop
												 */ l_chargeType -> setChargeInvoiceId(
												p_adjustmentBucket,
												l_chargeType,
												l_currentInvoice
										))
						)
						.collect(Collectors.toList())
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
			AdjustmentProcessingContainer p_adjustmentBucket,
			InvoiceChargeDetail pInvoiceChargeDetail,
			AdjustmentOpenInvoice pOpenInvoice
	)
	{
		pInvoiceChargeDetail.setInvoiceId(pOpenInvoice.getInvoiceId());
		p_adjustmentBucket.setInvoiceChargeTotal(
				p_adjustmentBucket
						.getInvoiceChargeTotal().add(
								pInvoiceChargeDetail.getChargeTotal()
						)

		);
		return true;
	}

	// ------------------------------------------------------------------------
	/**
	 * Method will identify Invoice lines Charges for Mil-uniform and Mil-retail
	 * plan and sum up the total for each of them
	 * 
	 * @param p_adjustmentRequest
	 * @param p_adjustmentBucket
	 * @param p_validationErrors
	 */
	private void distinguishInvoiceLine(
			AdjustmentRO p_adjustmentRequest,
			AdjustmentProcessingContainer p_adjustmentBucket,
			RepoContainer p_repoContainer, ValidationErrors p_validationErrors,
			AdjustmentValidator p_adjustmentValidator
	)
	{
		StringJoiner l_invoiceIds = new StringJoiner(",");
		AdjustmentOpenInvoice l_openInvoice = null;
		for (AdjustmentOpenInvoice l_currentOpenInvoice : p_adjustmentRequest
				.getOpenInvoice())
		{
			l_openInvoice = l_currentOpenInvoice;
			p_adjustmentValidator.validateOpenInvoice(
					l_currentOpenInvoice, p_validationErrors
			);

			for (AdjustmentInvoiceLine l_currentLine : l_currentOpenInvoice
					.getInvoiceLine())
			{
				// if !l_currentLine.IsRefundGiftCard
				if (!l_currentLine.isRefundGiftCard()) {
					p_adjustmentValidator.validateInvoiceLine(
							l_currentLine, p_validationErrors,
							p_adjustmentBucket
									.isMilstarBucket()
					);

					l_currentLine.setInvoiceId(
							l_currentOpenInvoice.getInvoiceId()
					);

					if (l_currentLine.getInvoiceLineChargeDetail() != null) {
						for (InvoiceChargeDetail l_currentCharge : l_currentLine
								.getInvoiceLineChargeDetail())
						{
							l_currentCharge.setInvoiceLineId(
									l_currentLine.getInvoiceLineId()
							);

							l_currentCharge.setInvoiceId(
									l_currentOpenInvoice.getInvoiceId()
							);

							if (p_adjustmentBucket.isMilstarBucket()) {

								switch (l_currentLine.getExtended()
										.getResponsePlan())
								{
									case PLAN_MILITARY_UNIFORM:
										l_currentCharge.setPlanNumber(
												PLAN_MILITARY_UNIFORM
										);
										p_adjustmentBucket
												.getMilitaryUniformCharge()
												.add(l_currentCharge);
										p_adjustmentBucket
												.getMilitaryUniformLine()
												.add(l_currentLine);
										p_adjustmentBucket
												.setMilitaryUninformLinesTotal(
														p_adjustmentBucket
																.getMilitaryUninformLinesTotal()
																.add(
																		l_currentLine
																				.getInvoiceLineTotal()
																)
												);
										break;
									case PLAN_MILITARY_RETAIL:
										l_currentCharge
												.setPlanNumber(
														PLAN_MILITARY_RETAIL
												);
										p_adjustmentBucket
												.getMilitaryRetailCharge()
												.add(l_currentCharge);
										p_adjustmentBucket
												.getMilitaryPromoLine()
												.add(l_currentLine);
										// if promo-plan is null only then add
										// the
										// the given line's InvoiceTotal to
										// Total.
										if (
											l_currentLine.getExtended()
													.getPromoPlan() == null
										)
										{
											p_adjustmentBucket
													.setMilitaryPromoLinesTotalDouble(
															p_adjustmentBucket
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
							p_adjustmentBucket.getOpenChargeDetails()
									.add(l_currentCharge);
							p_adjustmentBucket.setOpenInvoiceLineTotal(
									p_adjustmentBucket.getOpenInvoiceLineTotal()
											.add(
													l_currentCharge
															.getChargeTotal()
											)
							);

						}
					}
				}
			}
			l_invoiceIds.add(l_currentOpenInvoice.getInvoiceId());
		}
		// Create copy of OpenInvoice object
		try {

			ObjectMapper objectMapper = new ObjectMapper();

			AdjustmentOpenInvoice l_openInvoiceCopy = objectMapper
					.readValue(
							objectMapper.writeValueAsString(l_openInvoice),
							AdjustmentOpenInvoice.class
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
	 * Distinguish different payment based on following
	 * <ol>
	 * <li>Milstar Uniform
	 * <li>Milstar Retail
	 * <li>Credit Card
	 * <li>Gift Card
	 * </ol>
	 * 
	 * @param p_adjustmentRequest
	 * @param p_adjustmentBucket
	 */
	private void distinguishPaymentMethods(
			AdjustmentRO p_adjustmentRequest,
			AdjustmentProcessingContainer p_adjustmentBucket,
			RepoContainer p_repoContainer,
			ValidationErrors p_validationErrors,
			AdjustmentValidator p_adjustmentValidator
	)
	{

		AtomicBoolean l_isMUCardPresent = new AtomicBoolean(false);
		AtomicBoolean l_isMRCardPresent = new AtomicBoolean(false);

		for (PaymentHeader l_paymentHeader : p_adjustmentRequest
				.getPaymentHeader())
		{
			p_adjustmentValidator.validatePaymentHeader(
					l_paymentHeader,
					p_validationErrors
			);
			/*
			 * The RefundGiftCards would be distinguished here into a HashMap to
			 * process it further against their parent Gift Card. Refer EOR-1729
			 * for more info.
			 */
			for (PaymentMethod l_currentMethod : l_paymentHeader
					.getPaymentMethod())
			{

				LOGGER.info(
						"Validating PaymentMethod: "
								+ l_currentMethod.getPaymentMethodId()
								+ " for mandatory nodes and attributes"
				);

				p_adjustmentBucket.getPaymentMethodResponses().put(
						l_currentMethod.getPaymentMethodId(),
						new PaymentMethodResponse()
				);
				/* Validate payment method for mandatory nodes */

				p_adjustmentValidator.validatePaymentMethod(
						l_currentMethod, p_validationErrors, l_isMUCardPresent,
						l_isMRCardPresent
				);

				if (
					l_currentMethod.getExtended() != null && l_currentMethod
							.getExtended().isNewRefundGiftCard()
				)
				{
					ParserUtils.distinguishNewRefundGiftCard(
							p_adjustmentBucket,
							l_currentMethod
					);
				}
			}

			for (PaymentMethod l_currentMethod : l_paymentHeader
					.getPaymentMethod())
			{
				/*
				 * To compare using CurrentSettledAmount while adding
				 * l_currentMethod to PQ
				 */
				l_currentMethod.setConsiderCurrentSettled(true);

				/*
				 * Deduct the ProcessedAmount of 'Return-Credit' transactions
				 * from the currentSettledAmount of the currentPaymentMethod.
				 */
				ParserUtils.deductReturnedTransactions(l_currentMethod);

				if (
					Utils.isValueGreater(
							l_currentMethod.getCurrentSettledAmount(),
							ZERO_VALUE
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
							p_adjustmentBucket.setMilstarBucket(true);
							switch (l_currentMethod.getExtended()
									.getResponsePlan())
							{
								case PLAN_MILITARY_UNIFORM:
									p_adjustmentBucket
											.setTotalPaymentUniformAuth(
													p_adjustmentBucket
															.getTotalPaymentUniformAuth()
															.add(
																	l_currentMethod
																			.getCurrentAuthAmount()
															)
											);
									p_adjustmentBucket
											.setTotalPaymentUniformSettled(
													p_adjustmentBucket
															.getTotalPaymentUniformSettled()
															.add(
																	l_currentMethod
																			.getCurrentSettledAmount()
															)
											);
									p_adjustmentBucket
											.setMilstarUniformCard(
													l_currentMethod
											);

									p_adjustmentBucket.setTotalPaymentSettled(
											p_adjustmentBucket
													.getTotalPaymentSettled()
													.add(
															l_currentMethod
																	.getCurrentSettledAmount()
													)
									);

									/*
									 * p_adjustmentBucket.getSettlementCCMethods
									 * () .add(l_currentMethod);
									 */

									break;
								case PLAN_MILITARY_RETAIL:
									p_adjustmentBucket
											.setTotalPaymentRetailAuth(
													p_adjustmentBucket
															.getTotalPaymentRetailAuth()
															.add(
																	l_currentMethod
																			.getCurrentAuthAmount()
															)
											);
									p_adjustmentBucket
											.setTotalPaymentRetailSettled(
													p_adjustmentBucket
															.getTotalPaymentRetailSettled()
															.add(
																	l_currentMethod
																			.getCurrentSettledAmount()
															)
											);

									/*
									 * p_adjustmentBucket.getSettlementCCMethods
									 * () .add(l_currentMethod);
									 */

									p_adjustmentBucket
											.setMilstarRetailCard(
													l_currentMethod
											);

									p_adjustmentBucket.setTotalPaymentSettled(
											p_adjustmentBucket
													.getTotalPaymentSettled()
													.add(
															l_currentMethod
																	.getCurrentSettledAmount()
													)
									);
									break;
							}
						} else {
							p_adjustmentBucket.getSettlementCCMethods()
									.add(l_currentMethod);

							p_adjustmentBucket.setTotalPaymentSettled(
									p_adjustmentBucket.getTotalPaymentSettled()
											.add(
													l_currentMethod
															.getCurrentSettledAmount()
											)
							);
						}
					} else if (l_paymentType.equalsIgnoreCase(GIFT_CARD)) {
						/*
						 * Checks if refund gift cards have been issued against
						 * current gift card. If so then, it deducts the
						 * refunded amount from currentSettleAmount of the
						 * current gift card.
						 */
						if (
							!p_adjustmentBucket.newRefundGCTxns.isEmpty()
									&& p_adjustmentBucket.newRefundGCTxns
											.containsKey(
													l_currentMethod
															.getPaymentMethodId()
											)
						)
						{
							l_currentMethod.setCurrentSettledAmount(
									l_currentMethod
											.getCurrentSettledAmount()
											.subtract(
													p_adjustmentBucket.newRefundGCTxns
															.get(
																	l_currentMethod
																			.getPaymentMethodId()
															)
											)
							);
						}

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
							p_adjustmentBucket.getSettlementGCMethods()
									.add(l_currentMethod);

							p_adjustmentBucket.setTotalPaymentSettled(
									p_adjustmentBucket.getTotalPaymentSettled()
											.add(
													l_currentMethod
															.getCurrentSettledAmount()
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
	 * @param p_adjustmentRequest
	 * @param p_adjustmentBucket
	 * @param p_adjustmentValidator
	 * @param p_validationErrors
	 */
	private void distinguishOrderLineTotals(
			AdjustmentRO p_adjustmentRequest,
			ProcessingContainer p_adjustmentBucket,
			ValidationErrors p_validationErrors,
			AdjustmentValidator p_adjustmentValidator
	)
	{
		for (OrderLine l_currentLine : p_adjustmentRequest.getOrderLine()) {
			// if !l_currentLine.IsRefundGiftCard
			if (!l_currentLine.isRefundGiftCard()) {

				p_adjustmentValidator.validateOrderLines(
						l_currentLine, p_validationErrors, p_adjustmentBucket
								.isMilstarBucket()
				);

				if (p_adjustmentBucket.isMilstarBucket()) {
					switch (l_currentLine.getExtended().getResponsePlan()) {
						case PLAN_MILITARY_UNIFORM:
							p_adjustmentBucket.setTotalMUOrderedAmount(
									p_adjustmentBucket.getTotalMUOrderedAmount()
											.add(
													l_currentLine
															.getOrderLineTotal()
											)
							);
							break;
						case PLAN_MILITARY_RETAIL:
							p_adjustmentBucket.setTotalMROrderedAmount(
									p_adjustmentBucket.getTotalMROrderedAmount()
											.add(
													l_currentLine
															.getOrderLineTotal()
											)
							);
							break;

					}
				}
				p_adjustmentBucket.setTotalOrderedAmount(
						p_adjustmentBucket.getTotalOrderedAmount()
								.add(l_currentLine.getOrderLineTotal())
				);
			}
		}
	}

	// ------------------------------------------------------------------------
	/**
	 * Get MU, MR buckets amounts which are already returned
	 * 
	 * @param p_adjustmentRequest
	 * @param p_adjustmentBucket
	 * @param p_adjustmentValidator
	 * @param p_validationErrors
	 */
	private void distinguishReturnedLineTotals(
			AdjustmentRO p_adjustmentRequest,
			ProcessingContainer p_adjustmentBucket,
			ValidationErrors p_validationErrors,
			AdjustmentValidator p_adjustmentValidator
	)
	{
		if (p_adjustmentRequest.getClosedInvoices() == null) {
			return;
		}

		for (ClosedInvoice l_closedInvoice : p_adjustmentRequest
				.getClosedInvoices())
		{
			if (
				l_closedInvoice != null
						&& l_closedInvoice.getInvoiceType() != null
						&& RETURN_INVOICE.equalsIgnoreCase(
								l_closedInvoice.getInvoiceType()
										.getInvoiceTypeId()
						)
			)
			{
				for (InvoiceLine l_currentLine : l_closedInvoice
						.getInvoiceLine())
				{
					p_adjustmentValidator.validateClosedInvoiceLine(
							l_currentLine, p_validationErrors,
							p_adjustmentBucket.isMilstarBucket()
					);

					if (p_adjustmentBucket.isMilstarBucket()) {
						switch (l_currentLine.getExtended().getResponsePlan()) {
							case PLAN_MILITARY_UNIFORM:
								p_adjustmentBucket.setTotalMUReturnedAmount(
										p_adjustmentBucket
												.getTotalMUReturnedAmount()
												.add(
														l_currentLine
																.getInvoiceLineTotal()
												)
								);
								break;
							case PLAN_MILITARY_RETAIL:
								p_adjustmentBucket.setTotalMRReturnedAmount(
										p_adjustmentBucket
												.getTotalMRReturnedAmount()
												.add(
														l_currentLine
																.getInvoiceLineTotal()
												)
								);
								break;
						}
					}
					p_adjustmentBucket.setTotalReturnedAmount(
							p_adjustmentBucket.getTotalReturnedAmount().add(
									l_currentLine.getInvoiceLineTotal().abs()
							)
					);
				}
			}
		}
	}
	// ------------------------------------------------------------------------
}
// -----------------------------------------------------------------------------
// END OF FILE
// -----------------------------------------------------------------------------