package com.aafes.settlement.parser;

import java.util.StringJoiner;
import java.util.concurrent.atomic.AtomicBoolean;
// ----------------------------------------------------------------------------
import java.util.stream.Collectors;

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
import com.aafes.settlement.core.model.refund.RefundRO;
import com.aafes.settlement.core.order.OrderLine;
import com.aafes.settlement.core.payment.PaymentHeader;
import com.aafes.settlement.core.payment.PaymentMethod;
import com.aafes.settlement.core.response.PaymentMethodResponse;
import com.aafes.settlement.exception.NodeNotFoundException;
import com.aafes.settlement.repository.RepoContainer;
import com.aafes.settlement.util.ParserUtils;
import com.aafes.settlement.util.RepoUtils;
import com.aafes.settlement.util.Utils;
import com.aafes.settlement.validators.RefundValidator;
import com.aafes.settlement.validators.ValidationErrors;
import com.fasterxml.jackson.databind.ObjectMapper;

// ----------------------------------------------------------------------------
/**
 * This class distinguishes the Refund request into an object of
 * ProcessingContainer and validates the same for mandatory nodes and
 * attributes.
 */
@Service
public class RefundRequestParser
	implements Constants, ErrorConstants
{
	// ------------------------------------------------------------------------
	private static final Logger LOGGER = LogManager
			.getLogger(RefundRequestParser.class);
	// ------------------------------------------------------------------------

	/**
	 * This method calls various distinguishing methods which parse and fill an
	 * object of ProcessingContainer.
	 * 
	 * @param p_refundRequest
	 * @param p_validationErrors
	 * @param p_refundValidator
	 * @return
	 * @throws NodeNotFoundException
	 *             when a method validating respective nodes/attributes does not
	 *             find a mandatory node/attribute or if it has an invalid
	 *             value.
	 */
	public ProcessingContainer refundParser(
			RefundRO p_refundRequest,
			ValidationErrors p_validationErrors,
			RefundValidator p_refundValidator,
			RepoContainer p_repoContainer
	)
			throws NodeNotFoundException
	{
		// create instance of Processing Container
		ProcessingContainer l_refundBucket = new ProcessingContainer();

		LOGGER.debug("Distinguishing PaymentMethod node");
		// iterate over Payment methods and separate them into different buckets
		distinguishPaymentMethods(
				p_refundRequest, l_refundBucket, p_repoContainer,
				p_refundValidator, p_validationErrors
		);

		LOGGER.debug("Distinguishing OpenInvoice.InvoiceLine node");
		// iterate Invoice Lines and separate them into different buckets
		distinguishInvoiceLines(
				p_refundRequest, l_refundBucket, p_repoContainer,
				p_refundValidator, p_validationErrors
		);

		LOGGER.debug("Distinguishing OpenInvoice.InvoiceChargeDetail node");
		// iterate Invoice Lines and separate Shipping charges from each invoice
		// line into different buckets
		distinguishShippingCharges(
				p_refundRequest, l_refundBucket, p_refundValidator,
				p_validationErrors
		);

		LOGGER.debug("Distinguishing OrderLine node");
		// iterate over original order lines to get MU , MR and Order Line
		// Totals
		distinguishOrderLineTotals(
				p_refundRequest, l_refundBucket, p_refundValidator,
				p_validationErrors
		);

		LOGGER.debug("Distinguishing ClosedInvoice node");
		// iterate over closed invoices to get returned MU, MR and Order Line
		// Totals
		distinguishReturnedLineTotals(
				p_refundRequest, l_refundBucket, p_refundValidator,
				p_validationErrors
		);

		return l_refundBucket;
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
	 * @param p_refundRequest
	 * @param p_refundBucket
	 * @param p_refundValidator
	 * @param p_validationErrors
	 */
	private void distinguishPaymentMethods(
			RefundRO p_refundRequest,
			ProcessingContainer p_refundBucket,
			RepoContainer p_repoContainer,
			RefundValidator p_refundValidator,
			ValidationErrors p_validationErrors
	)
	{

		AtomicBoolean l_isMUCardPresent = new AtomicBoolean(false);
		AtomicBoolean l_isMRCardPresent = new AtomicBoolean(false);

		for (PaymentHeader l_paymentHeader : p_refundRequest.getParentOrder()
				.getPaymentHeader())
		{
			p_refundValidator.validatePaymentHeader(
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

				/* Validate payment method for mandatory nodes */
				p_refundValidator.validatePaymentMethod(
						l_currentMethod, p_validationErrors, l_isMUCardPresent,
						l_isMRCardPresent
				);

				if (
					l_currentMethod.getPaymentType().getPaymentTypeId()
							.equalsIgnoreCase(GIFT_CARD)
							&& l_currentMethod.getExtended() != null
							&& l_currentMethod.getExtended()
									.isNewRefundGiftCard()
				)
				{
					ParserUtils.distinguishNewRefundGiftCard(
							p_refundBucket,
							l_currentMethod
					);
				} else {
					p_refundBucket.getPaymentMethodResponses().put(
							l_currentMethod.getPaymentMethodId(),
							new PaymentMethodResponse()
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
							p_refundBucket.setMilstarBucket(true);
							switch (l_currentMethod.getExtended()
									.getResponsePlan())
							{
								case PLAN_MILITARY_UNIFORM:

									p_refundBucket
											.setTotalPaymentUniformSettled(
													p_refundBucket
															.getTotalPaymentUniformSettled()
															.add(
																	l_currentMethod
																			.getCurrentSettledAmount()
															)
											);
									p_refundBucket
											.setMilstarUniformCard(
													l_currentMethod
											);
									p_refundBucket.setTotalPaymentSettled(
											p_refundBucket
													.getTotalPaymentSettled()
													.add(
															l_currentMethod
																	.getCurrentSettledAmount()
													)
									);
									break;
								case PLAN_MILITARY_RETAIL:
									p_refundBucket.setTotalPaymentRetailSettled(
											p_refundBucket
													.getTotalPaymentRetailSettled()
													.add(
															l_currentMethod
																	.getCurrentSettledAmount()
													)
									);

									p_refundBucket.getSettlementCCMethods()
											.add(l_currentMethod);

									p_refundBucket
											.setMilstarRetailCard(
													l_currentMethod
											);
									p_refundBucket.setTotalPaymentSettled(
											p_refundBucket
													.getTotalPaymentSettled()
													.add(
															l_currentMethod
																	.getCurrentSettledAmount()
													)
									);
									break;
							}
						} else {
							p_refundBucket.getSettlementCCMethods()
									.add(l_currentMethod);

							p_refundBucket.setTotalPaymentSettled(
									p_refundBucket
											.getTotalPaymentSettled().add(
													l_currentMethod
															.getCurrentSettledAmount()
											)
							);

						}
					} else if (l_paymentType.equalsIgnoreCase(GIFT_CARD)) {

						/*
						 * Checks if any refund gift cards have been issued
						 * against current gift card. If so then, it deducts the
						 * refunded amount from currentSettleAmount of the
						 * current gift card.
						 */
						if (
							!p_refundBucket.newRefundGCTxns.isEmpty()
									&& p_refundBucket.newRefundGCTxns
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
													p_refundBucket.newRefundGCTxns
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
							p_refundBucket.getSettlementGCMethods()
									.add(l_currentMethod);
							p_refundBucket.setTotalPaymentSettled(
									p_refundBucket
											.getTotalPaymentSettled().add(
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
	 * Method will identify Invoice lines for Mil-uniform and Mil-retail plan
	 * and sum up the total for each of them
	 * 
	 * @param p_refundRequest
	 * @param pRefundContainer
	 * @throws NodeNotFoundException
	 */
	private void distinguishInvoiceLines(
			RefundRO p_refundRequest,
			ProcessingContainer p_refundBucket,
			RepoContainer p_repoContainer,
			RefundValidator p_refundValidator,
			ValidationErrors p_validationErrors
	)
			throws NodeNotFoundException
	{
		StringJoiner l_invoiceIds = new StringJoiner(",");
		OpenInvoice l_openInvoice = null;
		for (OpenInvoice l_currentOpenInvoice : p_refundRequest
				.getOpenInvoice())
		{
			l_openInvoice = l_currentOpenInvoice;
			p_refundValidator.validateOpenInvoice(
					l_currentOpenInvoice,
					p_validationErrors
			);

			for (InvoiceLine l_currentLine : l_currentOpenInvoice
					.getInvoiceLine())
			{
				// if !l_currentLine.IsRefundGiftCard
				if (!l_currentLine.isRefundGiftCard()) {
					p_refundValidator.validateInvoiceLine(
							l_currentLine, p_validationErrors, p_refundBucket
									.isMilstarBucket()
					);

					l_currentLine.setInvoiceId(
							l_currentOpenInvoice.getInvoiceId()
					);
					if (p_refundBucket.isMilstarBucket()) {
						switch (l_currentLine.getExtended().getResponsePlan()) {
							case PLAN_MILITARY_UNIFORM:
								p_refundBucket.getMilitaryUniformLines()
										.add(l_currentLine);
								p_refundBucket.setMilitaryUninformLinesTotal(
										p_refundBucket
												.getMilitaryUninformLinesTotal()
												.add(
														l_currentLine
																.getInvoiceLineTotal()
												)
								);
								break;
							case PLAN_MILITARY_RETAIL:
								p_refundBucket.getMilitaryPromoLines()
										.add(l_currentLine);
								// if promo-plan is null only then add the the
								// given
								// line's invoice total to total.
								if (
									l_currentLine.getExtended()
											.getPromoPlan() == null
								)
								{
									p_refundBucket
											.setMilitaryPromoLinesTotalDouble(
													p_refundBucket
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
					p_refundBucket.getOpenInvoiceLines().add(l_currentLine);
					p_refundBucket.setOpenInvoiceLineTotal(
							p_refundBucket.getOpenInvoiceLineTotal()
									.add(l_currentLine.getInvoiceLineTotal())
					);
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
	 * Filter all the invoice charges = shipping from all open invoices
	 * 
	 * @param p_refundRequest
	 * @param p_refundBucket
	 * @param p_validationErrors
	 * @param p_refundValidator
	 */
	private void distinguishShippingCharges(
			RefundRO p_refundRequest, ProcessingContainer p_refundBucket,
			RefundValidator p_refundValidator,
			ValidationErrors p_validationErrors
	)
	{
		p_refundBucket.getShippingChargeQueue().addAll(
				p_refundRequest.getOpenInvoice().stream()
						.filter(
								l_currentInvoice -> l_currentInvoice
										.getInvoiceChargeDetail() != null
						)
						.flatMap(
								l_currentInvoice -> l_currentInvoice
										.getInvoiceChargeDetail().stream()
										.filter(
												l_chargeDetail -> p_refundValidator
														.validateInvoiceChargeDetail(
																l_chargeDetail,
																p_validationErrors
														)
										)
										/*
										 * .filter( lChargeType -> lChargeType
										 * .getChargeType() != null ) .filter(
										 * lChargeType -> lChargeType
										 * .getChargeType() .getChargeTypeId()
										 * .equals( SHIPPING_CHARGES ) )
										 */
										.filter(
												/*
												 * using this filter to just set
												 * the invoiceId avoiding
												 * another for loop
												 */
												lChargeType -> setChargeInvoiceId(
														p_refundBucket,
														lChargeType,
														l_currentInvoice
												)
										)
						).collect(Collectors.toList())
		);

	}

	// ------------------------------------------------------------------------
	/**
	 * 
	 * @param p_refundBucket
	 * @param pInvoiceChargeDetail
	 * @param pOpenInvoice
	 * @return
	 */
	private boolean setChargeInvoiceId(
			ProcessingContainer p_refundBucket,
			InvoiceChargeDetail pInvoiceChargeDetail,
			OpenInvoice pOpenInvoice
	)
	{
		pInvoiceChargeDetail.setInvoiceId(pOpenInvoice.getInvoiceId());
		p_refundBucket.setInvoiceChargeTotal(
				p_refundBucket.getInvoiceChargeTotal()
						.add(pInvoiceChargeDetail.getChargeTotal())
		);
		return true;
	}

	// ------------------------------------------------------------------------
	/**
	 * Separate the order total based on MU, MR
	 * 
	 * @param p_refundRequest
	 * @param p_refundBucket
	 * @param p_validationErrors
	 * @param p_refundValidator
	 */
	private void distinguishOrderLineTotals(
			RefundRO p_refundRequest, ProcessingContainer p_refundBucket,
			RefundValidator p_refundValidator,
			ValidationErrors p_validationErrors
	)
	{
		for (OrderLine l_orderLine : p_refundRequest.getParentOrder()
				.getOrderLine())
		{
			// if !l_currentLine.IsRefundGiftCard
			if (!l_orderLine.isRefundGiftCard()) {
				p_refundValidator.validateOrderLines(
						l_orderLine, p_validationErrors, p_refundBucket
								.isMilstarBucket()
				);

				if (p_refundBucket.isMilstarBucket()) {
					switch (l_orderLine.getExtended().getResponsePlan()) {
						case PLAN_MILITARY_UNIFORM:
							p_refundBucket.setTotalMUOrderedAmount(
									p_refundBucket.getTotalMUOrderedAmount()
											.add(
													l_orderLine
															.getOrderLineTotal()
											)
							);
							break;
						case PLAN_MILITARY_RETAIL:
							p_refundBucket.setTotalMROrderedAmount(
									p_refundBucket.getTotalMROrderedAmount()
											.add(
													l_orderLine
															.getOrderLineTotal()
											)
							);
							break;

					}
				}
				p_refundBucket.setTotalOrderedAmount(
						p_refundBucket.getTotalOrderedAmount()
								.add(l_orderLine.getOrderLineTotal())
				);

			}
		}
	}

	// ------------------------------------------------------------------------
	/**
	 * Get MU, MR buckets amounts which are already returned
	 * 
	 * @param p_refundRequest
	 * @param p_refundBucket
	 * @param p_refundValidator
	 */
	private void distinguishReturnedLineTotals(
			RefundRO p_refundRequest, ProcessingContainer p_refundBucket,
			RefundValidator p_refundValidator,
			ValidationErrors p_validationErrors
	)
	{
		if (
			Utils.isNull(p_refundRequest.getParentOrder().getClosedInvoices())
		)
		{
			return;
		}
		for (ClosedInvoice l_closedInvoice : p_refundRequest.getParentOrder()
				.getClosedInvoices())
		{
			if (
				l_closedInvoice != null
						&& l_closedInvoice.getInvoiceType() != null
						&& (RETURN_INVOICE.equalsIgnoreCase(
								l_closedInvoice.getInvoiceType()
										.getInvoiceTypeId()
						)
								|| ADJUSTMENT_INVOICE.equalsIgnoreCase(
										l_closedInvoice.getInvoiceType()
												.getInvoiceTypeId()
								))
			)
			{
				for (InvoiceLine l_closedInvoiceLine : l_closedInvoice
						.getInvoiceLine())
				{
					p_refundValidator.validateClosedInvoiceLine(
							l_closedInvoiceLine, p_validationErrors,
							p_refundBucket.isMilstarBucket()
					);

					if (p_refundBucket.isMilstarBucket()) {
						switch (l_closedInvoiceLine.getExtended()
								.getResponsePlan())
						{
							case PLAN_MILITARY_UNIFORM:
								p_refundBucket
										.setTotalMUReturnedAmount(
												p_refundBucket
														.getTotalMUReturnedAmount()
														.add(
																l_closedInvoiceLine
																		.getInvoiceLineTotal()
														)
										);
								break;
							case PLAN_MILITARY_RETAIL:
								p_refundBucket
										.setTotalMRReturnedAmount(
												p_refundBucket
														.getTotalMRReturnedAmount()
														.add(
																l_closedInvoiceLine
																		.getInvoiceLineTotal()
														)
										);
								break;

						}
					}
					p_refundBucket.setTotalReturnedAmount(
							p_refundBucket.getTotalReturnedAmount().add(
									l_closedInvoiceLine.getInvoiceLineTotal()
											.abs()
							)
					);
				}
			}
		}
	}
	// ------------------------------------------------------------------------
}
// ----------------------------------------------------------------------------
// END OF FILE
// ----------------------------------------------------------------------------