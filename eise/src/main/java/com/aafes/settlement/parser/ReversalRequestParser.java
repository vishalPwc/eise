package com.aafes.settlement.parser;

import java.util.StringJoiner;
import java.util.concurrent.atomic.AtomicBoolean;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import com.aafes.settlement.constant.Constants;
import com.aafes.settlement.core.invoice.ClosedInvoice;
import com.aafes.settlement.core.invoice.InvoiceLine;
import com.aafes.settlement.core.invoice.OpenAuthReversal;
import com.aafes.settlement.core.model.reversal.ReversalPaymentHeader;
import com.aafes.settlement.core.model.reversal.ReversalPaymentMethod;
import com.aafes.settlement.core.model.reversal.ReversalProcessingContainer;
import com.aafes.settlement.core.model.reversal.ReversalRO;
import com.aafes.settlement.core.order.OrderLine;
import com.aafes.settlement.exception.NodeNotFoundException;
import com.aafes.settlement.repository.RepoContainer;
import com.aafes.settlement.util.RepoUtils;
import com.aafes.settlement.util.Utils;
import com.aafes.settlement.validators.ReversalValidator;
import com.aafes.settlement.validators.ValidationErrors;
import com.fasterxml.jackson.databind.ObjectMapper;

// ----------------------------------------------------------------------------
/**
 * This class distinguishes the Auth-reversal request into an object of
 * ReversalProcessingContainer and validates the same for mandatory nodes and
 * attributes.
 */
@Service
public class ReversalRequestParser
	implements Constants
{
	private static final Logger LOGGER = LogManager
			.getLogger(ReversalRequestParser.class);

	// ------------------------------------------------------------------------
	/**
	 * This method calls various distinguishing methods which parse and fill an
	 * object of ReversalProcessingContainer.
	 * 
	 * @param p_reversalRequest
	 * @param p_validationErrors
	 * @param p_reversalValidator
	 * @return object of of ReversalProcessingContainer
	 * @throws NodeNotFoundException
	 *             when a method validating respective nodes/attributes does not
	 *             find a mandatory node/attribute or if it has an invalid
	 *             value.
	 */
	public ReversalProcessingContainer reversalParser(
			ReversalRO p_reversalRequest,
			ValidationErrors p_validationErrors,
			RepoContainer p_repoContainer,
			ReversalValidator p_reversalValidator
	)
			throws NodeNotFoundException
	{
		// create an instance of Reversal Container
		ReversalProcessingContainer l_reversalBucket = new ReversalProcessingContainer();

		// iterate over Payment methods and separate them to different buckets
		distinguishPaymentMethods(
				p_reversalRequest, l_reversalBucket,
				p_repoContainer, p_validationErrors, p_reversalValidator
		);

		// iterate Invoice Lines and Separate them to different Buckets
		distinguishAuthLines(
				p_reversalRequest, l_reversalBucket,
				p_repoContainer, p_validationErrors, p_reversalValidator
		);

		// iterate over original order lines to get MU , MR and Order Line
		// Totals
		distinguishOrderLineTotals(
				p_reversalRequest, l_reversalBucket, p_validationErrors,
				p_reversalValidator
		);

		// iterate over closed invoices to get returned MU, MR and Order Line
		// totals
		distinguishShippedLineTotals(
				p_reversalRequest, l_reversalBucket, p_validationErrors,
				p_reversalValidator
		);

		// distinguishReturnedLineTotals(p_reversalRequest,
		// l_reversalBucket);
		return l_reversalBucket;
	}

	// ------------------------------------------------------------------------
	/**
	 * Method will identify Invoice lines for Mil-uniform and Mil-retail plan
	 * and sum up the total for each of them
	 * 
	 * @param p_reversalRequest
	 * @param pReversalContainer
	 * @param p_validationErrors
	 */
	private void distinguishAuthLines(
			ReversalRO p_reversalRequest,
			ReversalProcessingContainer p_reversalBucket,
			RepoContainer p_repoContainer,
			ValidationErrors p_validationErrors,
			ReversalValidator p_reversalValidator
	)
	{
		StringJoiner l_invoiceIds = new StringJoiner(",");
		OpenAuthReversal l_openAuthReversal = null;
		String l_orderId = "";
		for (OpenAuthReversal l_currentOpenAuth : p_reversalRequest
				.getOpenAuthReversal())
		{
			l_openAuthReversal = l_currentOpenAuth;
			p_reversalValidator.validateOpenAuth(
					l_currentOpenAuth,
					p_validationErrors
			);
			p_reversalBucket.getOpenAuthLines().add(l_currentOpenAuth);
			p_reversalBucket.setOpenInvoiceLineTotal(
					p_reversalBucket.getOpenInvoiceLineTotal()
							.add(l_currentOpenAuth.getAmount())
			);
			// Repository change
			l_invoiceIds.add(l_currentOpenAuth.getPaymentTransId());
			l_orderId = p_reversalRequest.getOrderId();
		}
		// Create copy of OpenAuthReversal object
		try {

			ObjectMapper objectMapper = new ObjectMapper();

			OpenAuthReversal l_openAuthReversalCopy = objectMapper
					.readValue(
							objectMapper.writeValueAsString(l_openAuthReversal),
							OpenAuthReversal.class
					);
			l_openAuthReversalCopy.setPaymentTransId(l_invoiceIds.toString());
			RepoUtils.addToOrderInvoiceData(
					l_openAuthReversalCopy, p_repoContainer, l_orderId
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
	 * Distinguish different payment based on following 1) Milstar Uniform 2)
	 * Milstar Retail 3) Credit Card 4) Gift Card
	 * 
	 * @param p_reversalRequest
	 * @param p_reversalBucket
	 * @param p_validationErrors
	 */
	private void distinguishPaymentMethods(
			ReversalRO p_reversalRequest,
			ReversalProcessingContainer p_reversalBucket,
			RepoContainer p_repoContainer,
			ValidationErrors p_validationErrors,
			ReversalValidator p_reversalValidator
	)
			throws NodeNotFoundException
	{

		AtomicBoolean l_isMUCardPresent = new AtomicBoolean(false);
		AtomicBoolean l_isMRCardPresent = new AtomicBoolean(false);

		for (ReversalPaymentHeader l_reversalPaymentHeader : p_reversalRequest
				.getPaymentHeader())
		{
			for (ReversalPaymentMethod l_currentMethod : l_reversalPaymentHeader
					.getPaymentMethod())
			{
				p_reversalValidator.validatePaymentMethod(
						l_currentMethod, p_validationErrors, l_isMUCardPresent,
						l_isMRCardPresent
				);

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
							p_reversalBucket.setMilstarBucket(true);
							switch (l_currentMethod.getExtended()
									.getResponsePlan())
							{
								case PLAN_MILITARY_UNIFORM:

									p_reversalBucket.setTotalPaymentUniformAuth(
											p_reversalBucket
													.getTotalPaymentUniformAuth()
													.add(
															l_currentMethod
																	.getCurrentAuthAmount()
													)
									);
									p_reversalBucket
											.setMilstarUniformCard(
													l_currentMethod
											);
									p_reversalBucket.setTotalPaymentAuth(
											p_reversalBucket
													.getTotalPaymentAuth()
													.add(
															l_currentMethod
																	.getCurrentAuthAmount()
													)
									);

									break;

								case PLAN_MILITARY_RETAIL:

									p_reversalBucket.setTotalPaymentRetailAuth(
											p_reversalBucket
													.getTotalPaymentRetailAuth()
													.add(
															l_currentMethod
																	.getCurrentAuthAmount()
													)
									);

									p_reversalBucket
											.setMilstarRetailCard(
													l_currentMethod
											);

									p_reversalBucket.setTotalPaymentAuth(
											p_reversalBucket
													.getTotalPaymentAuth()
													.add(
															l_currentMethod
																	.getCurrentAuthAmount()
													)
									);

									break;
							}
						} else {
							p_reversalBucket.getReversalPaymentMethods()
									.add(l_currentMethod);

							p_reversalBucket.setTotalPaymentAuth(
									p_reversalBucket
											.getTotalPaymentAuth()
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
							p_reversalBucket.getReversalPaymentMethods()
									.add(l_currentMethod);

							p_reversalBucket.setTotalPaymentAuth(
									p_reversalBucket
											.getTotalPaymentAuth()
											.add(
													l_currentMethod
															.getCurrentAuthAmount()
											)
							);
						}

					}
				}
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
	 * @param p_reversalRequest
	 * @param p_reversalBucket
	 * @param p_reversalValidator
	 * @param p_validationErrors
	 */
	private void distinguishOrderLineTotals(
			ReversalRO p_reversalRequest,
			ReversalProcessingContainer p_reversalBucket,
			ValidationErrors p_validationErrors,
			ReversalValidator p_reversalValidator
	)
	{
		for (OrderLine l_currentLine : p_reversalRequest.getOrderLine()) {
			// if !l_currentLine.IsRefundGiftCard
			if (!l_currentLine.isRefundGiftCard()) {
				p_reversalValidator.validateOrderLines(
						l_currentLine, p_validationErrors, p_reversalBucket
								.isMilstarBucket()
				);

				if (p_reversalBucket.isMilstarBucket()) {
					switch (l_currentLine.getExtended().getResponsePlan()) {
						case PLAN_MILITARY_UNIFORM:
							p_reversalBucket.setTotalMUOrderedAmount(
									p_reversalBucket.getTotalMUOrderedAmount()
											.add(
													l_currentLine
															.getOrderLineTotal()
											)
							);
							break;
						case PLAN_MILITARY_RETAIL:

							if (
								!Utils.isNull(
										l_currentLine.getExtended()
												.getPromoPlan()
								)
							)
							{
								p_reversalBucket.setTotalMROrderedAmount(
										p_reversalBucket
												.getTotalMROrderedAmount().add(
														l_currentLine
																.getOrderLineTotal()
												)
								);
							}
							break;

					}
				}
				p_reversalBucket.setTotalOrderedAmount(
						p_reversalBucket.getTotalOrderedAmount()
								.add(l_currentLine.getOrderLineTotal())
				);
			}
		}
	}

	// ------------------------------------------------------------------------
	/**
	 * Get MU, MR buckets amounts which are already returned
	 * 
	 * @param p_reversalRequest
	 * @param p_reversalBucket
	 * 
	 *            TO-DO add logic to get Closed invoices which are returned
	 *            successfully
	 * @param p_validationErrors
	 */
	private void distinguishShippedLineTotals(
			ReversalRO p_reversalRequest,
			ReversalProcessingContainer p_reversalBucket,
			ValidationErrors p_validationErrors,
			ReversalValidator p_reversalValidator
	)
	{
		if (p_reversalRequest.getClosedInvoices() == null) {
			return;
		}
		for (ClosedInvoice l_closedInvoice : p_reversalRequest
				.getClosedInvoices())
		{
			if (
				l_closedInvoice != null
						&& l_closedInvoice.getInvoiceType() != null
						&& (l_closedInvoice.getInvoiceType().getInvoiceTypeId()
								.equalsIgnoreCase(SHIPPED_INVOICE))
			)
			{
				for (InvoiceLine l_currentLine : l_closedInvoice
						.getInvoiceLine())
				{

					p_reversalValidator
							.validateClosedInvoiceLines(
									l_currentLine, p_validationErrors,
									p_reversalBucket.isMilstarBucket()
							);

					if (p_reversalBucket.isMilstarBucket()) {

						switch (l_currentLine.getExtended().getResponsePlan()) {
							case PLAN_MILITARY_UNIFORM:
								p_reversalBucket.setTotalMUReturnedAmount(
										p_reversalBucket
												.getTotalMUReturnedAmount()
												.add(
														l_currentLine
																.getInvoiceLineTotal()
												)
								);
								break;
							case PLAN_MILITARY_RETAIL:
								if (
									!Utils.isNull(
											l_currentLine.getExtended()
													.getPromoPlan()
									)
								)
								{
									p_reversalBucket.setTotalMRReturnedAmount(
											p_reversalBucket
													.getTotalMRReturnedAmount()
													.add(
															l_currentLine
																	.getInvoiceLineTotal()
													)
									);
								}
								break;

						}
					}
					p_reversalBucket.setTotalReturnedAmount(
							p_reversalBucket.getTotalReturnedAmount()
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