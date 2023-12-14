package com.aafes.settlement.parser;

import java.util.StringJoiner;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
// -----------------------------------------------------------------------------------
import org.springframework.stereotype.Service;

import com.aafes.settlement.constant.Constants;
import com.aafes.settlement.core.invoice.ClosedInvoice;
import com.aafes.settlement.core.invoice.InvoiceChargeDetail;
import com.aafes.settlement.core.invoice.InvoiceLine;
import com.aafes.settlement.core.invoice.OpenInvoice;
import com.aafes.settlement.core.model.ProcessingContainer;
import com.aafes.settlement.core.model.exchange.ExchangeRO;
import com.aafes.settlement.core.order.OrderLine;
import com.aafes.settlement.core.payment.PaymentHeader;
import com.aafes.settlement.core.payment.PaymentMethod;
import com.aafes.settlement.core.response.PaymentMethodResponse;
import com.aafes.settlement.exception.NodeNotFoundException;
import com.aafes.settlement.repository.RepoContainer;
import com.aafes.settlement.util.ParserUtils;
import com.aafes.settlement.util.RepoUtils;
import com.aafes.settlement.util.Utils;
import com.aafes.settlement.validators.ExchangeValidator;
import com.aafes.settlement.validators.ValidationErrors;
import com.fasterxml.jackson.databind.ObjectMapper;

// -----------------------------------------------------------------------------
/**
 * This class distinguishes the Exchange request into an object of
 * ProcessingContainer and validates the same for mandatory nodes and
 * attributes.
 */
@Service
public class ExchangeRequestParser
	implements Constants
{
	// ------------------------------------------------------------------------
	private static final Logger LOGGER = LogManager
			.getLogger(ExchangeRequestParser.class);

	// ------------------------------------------------------------------------
	/**
	 * This method calls various distinguishing methods which parse and fill an
	 * object of ProcessingContainer.
	 * 
	 * @param p_exchangeRequest
	 * @param p_validationErrors
	 * @return
	 * @throws NodeNotFoundException
	 *             when a method validating respective nodes/attributes does not
	 *             find a mandatory node/attribute or if it has an invalid
	 *             value.
	 */
	public ProcessingContainer exchangeParser(
			ExchangeRO p_exchangeRequest,
			ValidationErrors p_validationErrors,
			ExchangeValidator p_exchangeValidator,
			RepoContainer p_repoContainer
	)
			throws NodeNotFoundException
	{
		// create instance of ProcessingContainer
		ProcessingContainer l_exchangeBucket = new ProcessingContainer();

		LOGGER.debug("Distinguishing PaymentMethod node");
		// iterate over Payment methods and separate them to different buckets
		distinguishPaymentMethods(
				p_exchangeRequest, l_exchangeBucket, p_repoContainer,
				p_validationErrors, p_exchangeValidator
		);

		LOGGER.debug("Distinguishing OpenInvoice.InvoiceLine node");
		// iterate Invoice Lines and Separate them to different Buckets
		distinguishInvoiceLines(
				p_exchangeRequest, l_exchangeBucket, p_repoContainer,
				p_validationErrors, p_exchangeValidator
		);

		// get shipping charge in invoice detail
		// iterate Invoice Lines and Separate SHipping charges in each invoice
		// line
		distinguishShippingCharges(
				p_exchangeRequest, l_exchangeBucket, p_validationErrors,
				p_exchangeValidator
		);

		// iterate over original order lines to get MU , MR and Order Line
		// Totals
		distinguishOrderLineTotals(
				p_exchangeRequest, l_exchangeBucket, p_validationErrors,
				p_exchangeValidator
		);
		// iterate over closed invoices to get returned MU, MR and Order Line
		// totals
		distinguishReturnedLineTotals(
				p_exchangeRequest, l_exchangeBucket, p_validationErrors,
				p_exchangeValidator
		);
		return l_exchangeBucket;
	}

	// ------------------------------------------------------------------------
	/**
	 * Filter all the invoice charges = shipping from all open invoices
	 * 
	 * @param p_exchangeRequest
	 * @param p_exchangeBucket
	 * @param p_exchangeValidator
	 * @param p_validationErrors
	 */
	protected void distinguishShippingCharges(
			ExchangeRO p_exchangeRequest, ProcessingContainer p_exchangeBucket,
			ValidationErrors p_validationErrors,
			ExchangeValidator p_exchangeValidator
	)
	{
		p_exchangeBucket.getShippingChargeQueue().addAll(
				p_exchangeRequest.getOpenInvoice().stream()
						.filter(
								l_currentInvoice -> l_currentInvoice
										.getInvoiceChargeDetail() != null
						)
						.flatMap(
								l_currentInvoice -> l_currentInvoice
										.getInvoiceChargeDetail().stream()
										.filter(
												l_chargeDetail -> p_exchangeValidator
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
												l_chargeType -> setChargeInvoiceId(
														p_exchangeBucket,
														l_chargeType,
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
	 * @param p_invoiceChargeDetail
	 * @param p_openInvoice
	 * @return
	 */
	private boolean setChargeInvoiceId(
			ProcessingContainer p_exchangeBucket,
			InvoiceChargeDetail p_invoiceChargeDetail,
			OpenInvoice p_openInvoice
	)
	{
		p_invoiceChargeDetail.setInvoiceId(p_openInvoice.getInvoiceId());
		p_exchangeBucket.setInvoiceChargeTotal(
				p_exchangeBucket.getInvoiceChargeTotal()
						.add(p_invoiceChargeDetail.getChargeTotal())
		);
		return true;
	}

	// ------------------------------------------------------------------------
	/**
	 * Method will identify Invoice lines for Mil-uniform and Mil-retail plan
	 * and sum up the total for each of them
	 * 
	 * @param p_exchangeRequest
	 * @param p_exchangeBucket
	 * @param p_repoContainer
	 * @param p_validationErrors
	 * @param p_exchangeValidator
	 */
	protected void distinguishInvoiceLines(
			ExchangeRO p_exchangeRequest, ProcessingContainer p_exchangeBucket,
			RepoContainer p_repoContainer, ValidationErrors p_validationErrors,
			ExchangeValidator p_exchangeValidator
	)
	{
		StringJoiner l_invoiceIds = new StringJoiner(",");
		OpenInvoice l_openInvoice = null;
		for (OpenInvoice l_currentOpenInvoice : p_exchangeRequest
				.getOpenInvoice())
		{
			l_openInvoice = l_currentOpenInvoice;
			p_exchangeValidator.validateOpenInvoice(
					l_currentOpenInvoice,
					p_validationErrors
			);
			for (InvoiceLine l_currentLine : l_currentOpenInvoice
					.getInvoiceLine())
			{
				// if !l_currentLine.IsRefundGiftCard
				if (!l_currentLine.isRefundGiftCard()) {
					p_exchangeValidator.validateInvoiceLine(
							l_currentLine, p_validationErrors, p_exchangeBucket
									.isMilstarBucket()
					);

					l_currentLine.setInvoiceId(
							l_currentOpenInvoice.getInvoiceId()
					);

					if (p_exchangeBucket.isMilstarBucket()) {
						switch (l_currentLine.getExtended().getResponsePlan()) {
							case PLAN_MILITARY_UNIFORM:
								p_exchangeBucket.getMilitaryUniformLines()
										.add(l_currentLine);
								p_exchangeBucket.setMilitaryUninformLinesTotal(
										p_exchangeBucket
												.getMilitaryUninformLinesTotal()
												.add(
														l_currentLine
																.getInvoiceLineTotal()
												)
								);
								break;
							case PLAN_MILITARY_RETAIL:
								p_exchangeBucket.getMilitaryPromoLines()
										.add(l_currentLine);
								// if promo-plan is null only then add the the
								// given
								// line's invoice total to total.
								if (
									l_currentLine.getExtended()
											.getPromoPlan() == null
								)
								{
									p_exchangeBucket
											.setMilitaryPromoLinesTotalDouble(
													p_exchangeBucket
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

					p_exchangeBucket.getOpenInvoiceLines().add(l_currentLine);
					p_exchangeBucket.setOpenInvoiceLineTotal(
							p_exchangeBucket.getOpenInvoiceLineTotal()
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
	 * Distinguish different payment based on following
	 * <ol>
	 * <li>Milstar Uniform
	 * <li>Milstar Retail
	 * <li>Credit Card
	 * <li>Gift Card
	 * </ol>
	 * 
	 * @param p_exchangeRequest
	 * @param p_exchangeBucket
	 * @param p_validationErrors
	 * @param p_exchangeValidator
	 * @throws NodeNotFoundException
	 */
	protected void distinguishPaymentMethods(
			ExchangeRO p_exchangeRequest,
			ProcessingContainer p_exchangeBucket,
			RepoContainer p_repoContainer,
			ValidationErrors p_validationErrors,
			ExchangeValidator p_exchangeValidator
	)
			throws NodeNotFoundException
	{

		AtomicBoolean l_isMUCardPresent = new AtomicBoolean(false);
		AtomicBoolean l_isMRCardPresent = new AtomicBoolean(false);

		for (PaymentHeader l_paymentHeader : p_exchangeRequest.getParentOrder()
				.getPaymentHeader())
		{
			p_exchangeValidator.validatePaymentHeader(
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

				p_exchangeBucket.getPaymentMethodResponses().put(
						l_currentMethod.getPaymentMethodId(),
						new PaymentMethodResponse()
				);

				/* Validate payment method for mandatory nodes */
				p_exchangeValidator.validatePaymentMethod(
						l_currentMethod, p_validationErrors, l_isMUCardPresent,
						l_isMRCardPresent
				);

				if (
					l_currentMethod.getExtended() != null && l_currentMethod
							.getExtended().isNewRefundGiftCard()
				)
				{
					ParserUtils.distinguishNewRefundGiftCard(
							p_exchangeBucket,
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

				LOGGER.info(
						"Validating PaymentMethod: "
								+ l_currentMethod.getPaymentMethodId()
								+ " for mandatory nodes and attributes"
				);

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
							p_exchangeBucket.setMilstarBucket(true);
							switch (l_currentMethod.getExtended()
									.getResponsePlan())
							{
								case PLAN_MILITARY_UNIFORM:
									p_exchangeBucket.setTotalPaymentUniformAuth(
											p_exchangeBucket
													.getTotalPaymentUniformAuth()
													.add(
															l_currentMethod
																	.getCurrentAuthAmount()
													)
									);
									p_exchangeBucket
											.setTotalPaymentUniformSettled(
													p_exchangeBucket
															.getTotalPaymentUniformSettled()
															.add(
																	l_currentMethod
																			.getCurrentSettledAmount()
															)
											);
									p_exchangeBucket
											.setMilstarUniformCard(
													l_currentMethod
											);

									p_exchangeBucket
											.setTotalPaymentSettled(
													p_exchangeBucket
															.getTotalPaymentSettled()
															.add(
																	l_currentMethod
																			.getCurrentSettledAmount()
															)
											);

									break;
								case PLAN_MILITARY_RETAIL:
									p_exchangeBucket.setTotalPaymentRetailAuth(
											p_exchangeBucket
													.getTotalPaymentRetailAuth()
													.add(
															l_currentMethod
																	.getCurrentAuthAmount()
													)
									);
									p_exchangeBucket
											.setTotalPaymentRetailSettled(
													p_exchangeBucket
															.getTotalPaymentRetailSettled()
															.add(
																	l_currentMethod
																			.getCurrentSettledAmount()
															)
											);

									p_exchangeBucket.getSettlementCCMethods()
											.add(l_currentMethod);

									p_exchangeBucket
											.setMilstarRetailCard(
													l_currentMethod
											);

									p_exchangeBucket
											.setTotalPaymentSettled(
													p_exchangeBucket
															.getTotalPaymentSettled()
															.add(
																	l_currentMethod
																			.getCurrentSettledAmount()
															)
											);

									break;
							}
						} else {
							p_exchangeBucket.getSettlementCCMethods()
									.add(l_currentMethod);
							p_exchangeBucket.setTotalPaymentSettled(
									p_exchangeBucket
											.getTotalPaymentSettled().add(
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
							!p_exchangeBucket.newRefundGCTxns.isEmpty()
									&& p_exchangeBucket.newRefundGCTxns
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
													p_exchangeBucket.newRefundGCTxns
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
							p_exchangeBucket.getSettlementGCMethods()
									.add(l_currentMethod);
							p_exchangeBucket.setTotalPaymentSettled(
									p_exchangeBucket
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
	 * Separate the order total based on MU, MR
	 * 
	 * @param p_exchangeRequest
	 * @param p_exchangeBucket
	 * @param p_exchangeValidator
	 * @param p_validationErrors
	 */
	protected void distinguishOrderLineTotals(
			ExchangeRO p_exchangeRequest, ProcessingContainer p_exchangeBucket,
			ValidationErrors p_validationErrors,
			ExchangeValidator p_exchangeValidator
	)
	{
		for (OrderLine l_currentLine : p_exchangeRequest.getParentOrder()
				.getOrderLine())
		{
			// if !l_currentLine.IsRefundGiftCard
			if (!l_currentLine.isRefundGiftCard()) {
				p_exchangeValidator.validateOrderLines(
						l_currentLine, p_validationErrors, p_exchangeBucket
								.isMilstarBucket()
				);

				if (p_exchangeBucket.isMilstarBucket()) {
					switch (l_currentLine.getExtended().getResponsePlan()) {
						case PLAN_MILITARY_UNIFORM:
							p_exchangeBucket.setTotalMUOrderedAmount(
									p_exchangeBucket.getTotalMUOrderedAmount()
											.add(
													l_currentLine
															.getOrderLineTotal()
											)
							);
							break;
						case PLAN_MILITARY_RETAIL:
							p_exchangeBucket.setTotalMROrderedAmount(
									p_exchangeBucket.getTotalMROrderedAmount()
											.add(
													l_currentLine
															.getOrderLineTotal()
											)
							);
							break;

					}
				}
				p_exchangeBucket.setTotalOrderedAmount(
						p_exchangeBucket.getTotalOrderedAmount()
								.add(l_currentLine.getOrderLineTotal())
				);
			}
		}
	}

	// ------------------------------------------------------------------------
	/**
	 * Get MU, MR buckets amounts which are already returned
	 * 
	 * @param p_exchangeRequest
	 * @param p_exchangeBucket
	 * @param p_exchangeValidator
	 * @param p_validationErrors
	 */
	protected void distinguishReturnedLineTotals(
			ExchangeRO p_exchangeRequest, ProcessingContainer p_exchangeBucket,
			ValidationErrors p_validationErrors,
			ExchangeValidator p_exchangeValidator
	)
	{
		if (
			Utils.isNull(p_exchangeRequest.getParentOrder().getClosedInvoices())
		)
		{
			return;
		}
		for (ClosedInvoice l_closedInvoice : p_exchangeRequest.getParentOrder()
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
				for (InvoiceLine l_currentLine : l_closedInvoice
						.getInvoiceLine())
				{
					p_exchangeValidator.validateClosedInvoiceLine(
							l_currentLine, p_validationErrors, p_exchangeBucket
									.isMilstarBucket()
					);

					if (p_exchangeBucket.isMilstarBucket()) {
						switch (l_currentLine.getExtended().getResponsePlan()) {
							case PLAN_MILITARY_UNIFORM:
								p_exchangeBucket.setTotalMUReturnedAmount(
										p_exchangeBucket
												.getTotalMUReturnedAmount()
												.add(
														l_currentLine
																.getInvoiceLineTotal()
												)
								);
								break;
							case PLAN_MILITARY_RETAIL:
								p_exchangeBucket.setTotalMRReturnedAmount(
										p_exchangeBucket
												.getTotalMRReturnedAmount()
												.add(
														l_currentLine
																.getInvoiceLineTotal()
												)
								);
								break;
						}
					}
					p_exchangeBucket.setTotalReturnedAmount(
							p_exchangeBucket.getTotalReturnedAmount()
									.add(l_currentLine.getInvoiceLineTotal())
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