package com.aafes.settlement.parser;

import java.util.StringJoiner;
import java.util.concurrent.atomic.AtomicBoolean;
// -----------------------------------------------------------------------------------
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import com.aafes.settlement.constant.Constants;
import com.aafes.settlement.core.invoice.ClosedInvoice;
import com.aafes.settlement.core.invoice.InvoiceChargeDetail;
import com.aafes.settlement.core.invoice.InvoiceLine;
import com.aafes.settlement.core.invoice.OpenInvoice;
import com.aafes.settlement.core.model.ProcessingContainer;
import com.aafes.settlement.core.model.cancelled.CancelledRO;
import com.aafes.settlement.core.order.OrderLine;
import com.aafes.settlement.core.payment.PaymentHeader;
import com.aafes.settlement.core.payment.PaymentMethod;
import com.aafes.settlement.core.response.PaymentMethodResponse;
import com.aafes.settlement.exception.NodeNotFoundException;
import com.aafes.settlement.repository.RepoContainer;
import com.aafes.settlement.util.ParserUtils;
import com.aafes.settlement.util.RepoUtils;
import com.aafes.settlement.util.Utils;
import com.aafes.settlement.validators.CancelledValidator;
import com.aafes.settlement.validators.ValidationErrors;
import com.fasterxml.jackson.databind.ObjectMapper;

// ----------------------------------------------------------------------------
/**
 * This class distinguishes the BOPIS-cancellation request into an object of
 * ProcessingContainer and validates the same for mandatory nodes and
 * attributes.
 */
@Service
public class CancelledRequestParser
	implements Constants
{
	// ------------------------------------------------------------------------
	private static final Logger LOGGER = LogManager
			.getLogger(CancelledRequestParser.class);

	// ------------------------------------------------------------------------
	/**
	 * This method calls various distinguishing methods which parse and fill an
	 * object of ReversalProcessingContainer.
	 * 
	 * @param p_cancelledRequest
	 * @param p_validationErrors
	 * @return object of of ProcessingContainer
	 * @throws NodeNotFoundException
	 *             when a method validating respective nodes/attributes does not
	 *             find a mandatory node/attribute or if it has an invalid
	 *             value.
	 */
	public ProcessingContainer cancelledParser(
			CancelledRO p_cancelledRequest,
			ValidationErrors p_validationErrors,
			CancelledValidator p_cancelledValidator,
			RepoContainer p_repoContainer
	)
			throws NodeNotFoundException
	{
		// create instance of Cancelled Container
		ProcessingContainer l_cancelBucket = new ProcessingContainer();

		// iterate over Payment methods and separate them to different buckets
		distinguishPaymentMethods(
				p_cancelledRequest, l_cancelBucket, p_repoContainer,
				p_cancelledValidator, p_validationErrors
		);

		// iterate Invoice Lines and Separate them to different Buckets
		distinguishInvoiceLines(
				p_cancelledRequest, l_cancelBucket, p_repoContainer,
				p_cancelledValidator, p_validationErrors
		);

		// iterate Invoice Lines and Separate Shipping charges in each invoice
		// line
		distinguishShippingCharges(
				p_cancelledRequest, l_cancelBucket, p_cancelledValidator,
				p_validationErrors
		);

		// iterate over original order lines to get MU , MR and Order Line
		// Totals
		distinguishOrderLineTotals(
				p_cancelledRequest, l_cancelBucket, p_cancelledValidator,
				p_validationErrors
		);

		// iterate over closed invoices to get returned MU, MR and Order Line
		// totals

		distinguishReturnedLineTotals(
				p_cancelledRequest, l_cancelBucket, p_cancelledValidator,
				p_validationErrors
		);

		return l_cancelBucket;
	}

	// ------------------------------------------------------------------------
	/**
	 * Filter all the invoice charges = shipping from all open invoices
	 * 
	 * @param p_cancelledRequest
	 * @param p_cancelledBucket
	 * @param p_cancelledValidator
	 */
	private void distinguishShippingCharges(
			CancelledRO p_cancelledRequest,
			ProcessingContainer p_cancelledBucket,
			CancelledValidator p_cancelledValidator,
			ValidationErrors p_validationErrors
	)
	{
		p_cancelledBucket.getShippingChargeQueue().addAll(
				p_cancelledRequest
						.getOpenInvoice().stream()
						.filter(
								l_currentInvoice -> l_currentInvoice
										.getInvoiceChargeDetail() != null
						)
						.flatMap(
								l_currentInvoice -> l_currentInvoice
										.getInvoiceChargeDetail().stream()
										.filter(
												l_chargeDetail -> p_cancelledValidator
														.validateInvoiceChargeDetail(
																l_chargeDetail,
																p_validationErrors
														)
										)
										/*
										 * .filter( l_chargeType -> l_chargeType
										 * .getChargeType() .getChargeTypeId()
										 * .equals( SHIPPING_CHARGES ) )
										 */
										.filter(/*
												 * using this filter to just set
												 * the invoiceId avoiding
												 * another for loop
												 */ l_chargeType -> setChargeInvoiceId(
												p_cancelledBucket, l_chargeType,
												l_currentInvoice
										))
						)
						.collect(Collectors.toList())
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
			ProcessingContainer p_cancelledBucket,
			InvoiceChargeDetail pInvoiceChargeDetail,
			OpenInvoice pOpenInvoice
	)
	{
		pInvoiceChargeDetail.setInvoiceId(pOpenInvoice.getInvoiceId());
		p_cancelledBucket
				.setInvoiceChargeTotal(
						p_cancelledBucket.getInvoiceChargeTotal()
								.add(pInvoiceChargeDetail.getChargeTotal())
				);
		return true;
	}

	// ------------------------------------------------------------------------
	/**
	 * Method will identify Invoice lines for Mil-uniform and Mil-retail plan
	 * and sum up the total for each of them
	 * 
	 * @param p_cancelledRequest
	 * @param pCancelledContainer
	 */
	private void distinguishInvoiceLines(
			CancelledRO p_cancelledRequest,
			ProcessingContainer p_cancelledBucket,
			RepoContainer p_repoContainer,
			CancelledValidator p_cancelledValidator,
			ValidationErrors p_validationErrors
	)
	{
		StringJoiner l_invoiceIds = new StringJoiner(",");
		OpenInvoice l_openInvoice = null;
		for (OpenInvoice l_currentOpenInvoice : p_cancelledRequest
				.getOpenInvoice())
		{
			l_openInvoice = l_currentOpenInvoice;
			p_cancelledValidator.validateOpenInvoice(
					l_currentOpenInvoice, p_validationErrors
			);

			for (InvoiceLine l_currentLine : l_currentOpenInvoice
					.getInvoiceLine())
			{
				// if !l_currentLine.IsRefundGiftCard
				if (!l_currentLine.isRefundGiftCard()) {
					p_cancelledValidator.validateInvoiceLine(
							l_currentLine, p_validationErrors,
							p_cancelledBucket.isMilstarBucket()
					);

					l_currentLine.setInvoiceId(
							l_currentOpenInvoice.getInvoiceId()
					);

					if (p_cancelledBucket.isMilstarBucket()) {
						switch (l_currentLine.getExtended().getResponsePlan()) {
							case PLAN_MILITARY_UNIFORM:
								p_cancelledBucket.getMilitaryUniformLines()
										.add(l_currentLine);
								p_cancelledBucket.setMilitaryUninformLinesTotal(
										p_cancelledBucket
												.getMilitaryUninformLinesTotal()
												.add(
														l_currentLine
																.getInvoiceLineTotal()
												)
								);
								break;
							case PLAN_MILITARY_RETAIL:
								p_cancelledBucket.getMilitaryPromoLines()
										.add(l_currentLine);
								// if promo-plan is null only then add the the
								// given
								// line's invoice total to total.
								if (
									l_currentLine.getExtended()
											.getPromoPlan() == null
								)
								{
									p_cancelledBucket
											.setMilitaryPromoLinesTotalDouble(
													p_cancelledBucket
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
					p_cancelledBucket.getOpenInvoiceLines().add(l_currentLine);
					p_cancelledBucket.setOpenInvoiceLineTotal(
							p_cancelledBucket.getOpenInvoiceLineTotal()
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
	 * @param p_cancelledRequest
	 * @param p_cancelledBucket
	 */
	private void distinguishPaymentMethods(
			CancelledRO p_cancelledRequest,
			ProcessingContainer p_cancelledBucket,
			RepoContainer p_repoContainer,
			CancelledValidator p_cancelledValidator,
			ValidationErrors p_validationErrors
	)
	{

		AtomicBoolean l_isMUCardPresent = new AtomicBoolean(false);
		AtomicBoolean l_isMRCardPresent = new AtomicBoolean(false);

		for (PaymentHeader l_paymentHeader : p_cancelledRequest
				.getPaymentHeader())
		{
			p_cancelledValidator.validatePaymentHeader(
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

				p_cancelledBucket.getPaymentMethodResponses().put(
						l_currentMethod.getPaymentMethodId(),
						new PaymentMethodResponse()
				);

				/* Validate payment method for mandatory nodes */
				p_cancelledValidator.validatePaymentMethod(
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
							p_cancelledBucket,
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

							p_cancelledBucket.setMilstarBucket(true);

							switch (l_currentMethod.getExtended()
									.getResponsePlan())
							{
								case PLAN_MILITARY_UNIFORM:

									p_cancelledBucket
											.setTotalPaymentUniformSettled(
													p_cancelledBucket
															.getTotalPaymentUniformSettled()
															.add(
																	l_currentMethod
																			.getCurrentSettledAmount()
															)
											);

									p_cancelledBucket
											.setMilstarUniformCard(
													l_currentMethod
											);

									p_cancelledBucket.setTotalPaymentSettled(
											p_cancelledBucket
													.getTotalPaymentSettled()
													.add(
															l_currentMethod
																	.getCurrentSettledAmount()
													)
									);

									break;
								case PLAN_MILITARY_RETAIL:

									p_cancelledBucket
											.setTotalPaymentRetailSettled(
													p_cancelledBucket
															.getTotalPaymentRetailSettled()
															.add(
																	l_currentMethod
																			.getCurrentSettledAmount()
															)
											);

									/*
									 * p_cancelledBucket.getSettlementCCMethods(
									 * ) .add(l_currentMethod);
									 */

									p_cancelledBucket
											.setMilstarRetailCard(
													l_currentMethod
											);

									p_cancelledBucket.setTotalPaymentSettled(
											p_cancelledBucket
													.getTotalPaymentSettled()
													.add(
															l_currentMethod
																	.getCurrentSettledAmount()
													)
									);

									break;
							}
						} else {

							p_cancelledBucket.getSettlementCCMethods()
									.add(l_currentMethod);

							p_cancelledBucket.setTotalPaymentSettled(
									p_cancelledBucket.getTotalPaymentSettled()
											.add(
													l_currentMethod
															.getCurrentSettledAmount()
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
							p_cancelledBucket.getSettlementGCMethods()
									.add(l_currentMethod);

							p_cancelledBucket.setTotalPaymentSettled(
									p_cancelledBucket.getTotalPaymentSettled()
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
	 * @param p_cancelledRequest
	 * @param p_cancelledBucket
	 */
	private void distinguishOrderLineTotals(
			CancelledRO p_cancelledRequest,
			ProcessingContainer p_cancelledBucket,
			CancelledValidator p_cancelledValidator,
			ValidationErrors p_validationErrors
	)
	{
		for (OrderLine l_currentLine : p_cancelledRequest.getOrderLine()) {
			// if !l_currentLine.IsRefundGiftCard
			if (!l_currentLine.isRefundGiftCard()) {

				p_cancelledValidator.validateOrderLines(
						l_currentLine, p_validationErrors, p_cancelledBucket
								.isMilstarBucket()
				);

				if (p_cancelledBucket.isMilstarBucket()) {

					switch (l_currentLine.getExtended().getResponsePlan()) {
						case PLAN_MILITARY_UNIFORM:
							p_cancelledBucket.setTotalMUOrderedAmount(
									p_cancelledBucket.getTotalMUOrderedAmount()
											.add(
													l_currentLine
															.getOrderLineTotal()
											)
							);
							break;
						case PLAN_MILITARY_RETAIL:
							p_cancelledBucket.setTotalMROrderedAmount(
									p_cancelledBucket.getTotalMROrderedAmount()
											.add(
													l_currentLine
															.getOrderLineTotal()
											)
							);
							break;
					}
				}

				p_cancelledBucket.setTotalOrderedAmount(
						p_cancelledBucket.getTotalOrderedAmount()
								.add(l_currentLine.getOrderLineTotal())
				);
			}
		}
	}

	// ------------------------------------------------------------------------
	/**
	 * Get MU, MR buckets amounts which are already returned
	 * 
	 * @param p_cancelledRequest
	 * @param p_cancelledBucket
	 * @param p_validationErrors
	 * @param p_cancelledValidator
	 */
	private void distinguishReturnedLineTotals(
			CancelledRO p_cancelledRequest,
			ProcessingContainer p_cancelledBucket,
			CancelledValidator p_cancelledValidator,
			ValidationErrors p_validationErrors
	)
	{
		if (p_cancelledRequest.getClosedInvoices() == null) {
			return;
		}
		for (ClosedInvoice l_closedInvoice : p_cancelledRequest
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
					switch (l_currentLine.getExtended().getResponsePlan()) {
						case PLAN_MILITARY_UNIFORM:
							p_cancelledBucket.setTotalMUReturnedAmount(
									p_cancelledBucket.getTotalMUReturnedAmount()
											.add(
													l_currentLine
															.getInvoiceLineTotal()
											)
							);
							break;
						case PLAN_MILITARY_RETAIL:
							p_cancelledBucket.setTotalMRReturnedAmount(
									p_cancelledBucket.getTotalMRReturnedAmount()
											.add(
													l_currentLine
															.getInvoiceLineTotal()
											)
							);
							break;

					}
					p_cancelledBucket.setTotalReturnedAmount(
							p_cancelledBucket.getTotalReturnedAmount().add(
									l_currentLine.getInvoiceLineTotal().abs()
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