/**
 * 
 */
package com.aafes.settlement.service.implementation;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.aafes.settlement.constant.Constants;
import com.aafes.settlement.core.invoice.InvoiceChargeDetail;
import com.aafes.settlement.core.invoice.InvoiceLine;
import com.aafes.settlement.core.model.ProcessingContainer;
import com.aafes.settlement.core.payment.PaymentMethod;
import com.aafes.settlement.core.response.EISEResponseGeneric;
import com.aafes.settlement.core.response.InvoiceChargeDetailResponse;
import com.aafes.settlement.core.response.InvoiceLineResponse;
import com.aafes.settlement.core.response.InvoiceResponse;
import com.aafes.settlement.core.response.PaymentMethodResponse;
import com.aafes.settlement.repository.RepoContainer;
import com.aafes.settlement.util.CalculationUtil;
import com.aafes.settlement.util.RepoUtils;
import com.aafes.settlement.util.Utils;

/**
 */
public class BaseCalculation
	implements Constants
{

	private static final Logger LOGGER = LogManager
			.getLogger(BaseCalculation.class);

	// ------------------------------------------------------------------------
	/**
	 * Set MU Amount which is available for settling current Invoice is (Total
	 * Auth - pre bucket MU)
	 * 
	 * @param p_calculationContainer
	 * @param pSettlementContainer
	 */
	protected void calculatePreBucketMU(
			CalculationUtil p_calculationContainer,
			ProcessingContainer p_settlementContainer
	)
	{
		BigDecimal preBucketMU = (p_settlementContainer
				.getTotalMUOrderedAmount()
				.subtract(
						p_settlementContainer.getMilitaryUninformLinesTotal()
				)
				.subtract(
						// p_settlementContainer.getTotalPaymentUniformSettled()
						p_settlementContainer.getTotalMUReturnedAmount()
				));
		// MU Amount which is available for settling current Invoice is (Total
		// Auth - pre bucket MU)
		p_calculationContainer
				.setAmountMUAvailableForSettlement(
						p_settlementContainer
								.getTotalPaymentUniformAuth().subtract(
										preBucketMU
								)
				);
	}

	// ------------------------------------------------------------------------
	/**
	 * Set MU Amount which is available for settling current Invoice is (Total
	 * Auth - pre bucket MU)
	 * 
	 * @param p_calculationContainer
	 * @param p_settlementContainer
	 */
	protected void calculatePreBucketMR(
			CalculationUtil p_calculationContainer,
			ProcessingContainer p_settlementContainer
	)
	{
		BigDecimal preBucketMR = (p_settlementContainer
				.getTotalMROrderedAmount()
				.subtract(
						p_settlementContainer
								.getMilitaryPromoLinesTotalDouble()
				)
				.subtract(
						// p_settlementContainer.getTotalPaymentRetailSettled()
						p_settlementContainer.getTotalMRReturnedAmount()
				));
		// MR Amount which is available for settling current Invoice is (Total
		// Auth - pre bucket MR)
		p_calculationContainer
				.setAmountMRAvailableForSettlement(
						p_settlementContainer
								.getTotalPaymentRetailAuth().subtract(
										preBucketMR
								)
				);
	}

	// -------------------------------------------------------------------------
	/**
	 * Specific for Shipment invoice, and return current auth amount attribute
	 * 
	 * @param l_currentPaymentMethod
	 * @return BigDecimal
	 */
	protected BigDecimal getCardBalance(PaymentMethod l_currentPaymentMethod) {

		return l_currentPaymentMethod.getCurrentAuthAmount();
	}

	// -------------------------------------------------------------------------
	/**
	 * Complete Settlement Invoice Business Logic against Lowest GC first
	 * followed by Highest Gift Card Includes Items which are pending for
	 * settlement after MU or MR
	 * 
	 * @param pLineReaminingForSettlement
	 * @param pAmountRemainingForSettlement
	 * @param pUsedPaymentMethod
	 */
	protected void settlementBusinessLogic(
			CalculationUtil p_calculationContainer,
			ProcessingContainer p_settlementContainer,
			RepoContainer p_repoContainer,
			EISEResponseGeneric p_eiseResponseGeneric
	)
	{
		LOGGER.debug(
				"---- Base Calculation: settlementBusinessLogic(): START---"
		);
		List<InvoiceResponse> l_settlementTenderResponse = new ArrayList<
				InvoiceResponse>();
		InvoiceResponse l_settledInvoiceResponse = new InvoiceResponse();

		BigDecimal l_totalInvoiceToSettle = p_settlementContainer
				.getOpenInvoiceLineTotal();
		PaymentMethod l_lowestCard = getNextAvailablePayment(
				p_settlementContainer
		);
		BigDecimal l_currentCardBalance = getCardBalance(l_lowestCard);

		UNTIL_INVOICE_IS_SETTLED:
		// while (l_totalInvoiceToSettle > IS_SETTLED) {
		while (isValueGreater(l_totalInvoiceToSettle, IS_SETTLED)) {
			// if (l_currentCardBalance <= IS_FINISHED) {
			if (isValueLesserOrEqual(l_currentCardBalance, IS_FINISHED)) {
				/**
				 * Get New Payment Method & Balance for Settling Invoice Line.
				 * If all GC are Finished, will take CC
				 */
				l_lowestCard = getNextAvailablePayment(p_settlementContainer);
				l_currentCardBalance = getCardBalance(l_lowestCard);
				LOGGER.debug(
						"Now moving to next payment method " + l_lowestCard
								.getPaymentMethodId()
								+ "to settle the remaining amount"
				);
				// getPaymentMethodResponses().add(lPaymentResponse);
			}
			PaymentMethodResponse l_paymentResponse = p_settlementContainer
					.getPaymentMethodResponses()
					.get(l_lowestCard.getPaymentMethodId());
			/**
			 * do this when there is some balance of invoice line needs to be
			 * settled against second payment method
			 */
			// if (p_calculationContainer
			// .getRemainingInvoiceAmountToSettle() > IS_SETTLED) {
			if (
				Utils.isValueGreater(
						p_calculationContainer
								.getRemainingInvoiceAmountToSettle(),
						IS_SETTLED
				)
			)
			{
				// check availability of line amount on payment method
				if (
					checkAuthAvailability(
							l_currentCardBalance,
							p_calculationContainer
									.getRemainingInvoiceAmountToSettle(),
							p_calculationContainer
									.getRemainingInvoiceLineToSettle(),
							l_lowestCard, l_paymentResponse,
							p_repoContainer
					)
				)
				{
					p_calculationContainer.setRemainingInvoiceAmountToSettle(
							p_calculationContainer
									.getRemainingInvoiceAmountToSettle()
									.subtract(l_currentCardBalance)
					);
					// l_totalInvoiceToSettle -= l_currentCardBalance;
					l_totalInvoiceToSettle = l_totalInvoiceToSettle
							.subtract(l_currentCardBalance);

					l_currentCardBalance = IS_FINISHED;
					continue UNTIL_INVOICE_IS_SETTLED;
				} else {

					// l_currentCardBalance -= p_calculationContainer
					// .getRemainingInvoiceAmountToSettle();
					l_currentCardBalance = l_currentCardBalance
							.subtract(
									p_calculationContainer
											.getRemainingInvoiceAmountToSettle()
							);

					// l_totalInvoiceToSettle -= p_calculationContainer
					// .getRemainingInvoiceAmountToSettle();
					l_totalInvoiceToSettle = l_totalInvoiceToSettle
							.subtract(
									p_calculationContainer
											.getRemainingInvoiceAmountToSettle()
							);

				}
			}
			INVOICE_ITERATE: while (
				!p_settlementContainer
						.getOpenInvoiceLines().isEmpty()
			)
			{
				InvoiceLine l_invoiceLineToSettle = p_settlementContainer
						.getOpenInvoiceLines().poll();

				BigDecimal l_currentLineAmount = l_invoiceLineToSettle
						.getInvoiceLineTotal();
				LOGGER.debug(
						"Now we are doing calculation for InvoiceLineID : "
								+ l_invoiceLineToSettle.getInvoiceLineId()
								+ " for $" + l_currentLineAmount
				);
				/**
				 * check if item is MU OR MR && have milstar payment methods
				 * 
				 * go inside block only if AUTH available on MU or MR is more
				 * than 0 after pre-bucketing
				 */
				// if (p_calculationContainer
				// .getAmountMRAvailableForSettlement() > IS_SETTLED
				// || p_calculationContainer
				// .getAmountMUAvailableForSettlement() > IS_SETTLED)
				if (
					Utils.isValueGreater(
							p_calculationContainer
									.getAmountMUAvailableForSettlement(),
							IS_SETTLED
					) &&
							isLineUniformItem(
									p_settlementContainer,
									l_invoiceLineToSettle
							) &&
							p_settlementContainer.isMilstarBucket()
				)
				{

					l_totalInvoiceToSettle = l_totalInvoiceToSettle.subtract(
							settleAgainstMUCard(
									p_settlementContainer, p_repoContainer,
									p_calculationContainer,
									l_invoiceLineToSettle
							)
					);
					break INVOICE_ITERATE;
				}

				if (
					Utils.isValueGreater(
							p_calculationContainer
									.getAmountMRAvailableForSettlement(),
							IS_SETTLED
					) &&
							isLineRetailItem(
									p_settlementContainer,
									l_invoiceLineToSettle
							) &&
							p_settlementContainer.isMilstarBucket()
				)
				{

					l_totalInvoiceToSettle = l_totalInvoiceToSettle.subtract(
							settleAgainstMRCard(
									p_settlementContainer, p_repoContainer,
									p_calculationContainer,
									l_invoiceLineToSettle
							)
					);

					break INVOICE_ITERATE;
				}
				// check availability of line amount on payment method
				if (
					checkAuthAvailability(
							l_currentCardBalance,
							l_currentLineAmount, l_invoiceLineToSettle,
							l_lowestCard, l_paymentResponse,
							p_repoContainer
					)
				)
				{
					p_calculationContainer.setRemainingInvoiceAmountToSettle(
							// l_currentLineAmount - l_currentCardBalance
							l_currentLineAmount.subtract(l_currentCardBalance)
					);
					// l_totalInvoiceToSettle -= l_currentCardBalance;
					// l_totalInvoiceToSettle = this.subtractDouble(
					// l_totalInvoiceToSettle, l_currentCardBalance);
					l_totalInvoiceToSettle = l_totalInvoiceToSettle
							.subtract(l_currentCardBalance);
					l_currentCardBalance = IS_FINISHED;
					p_calculationContainer.setRemainingInvoiceLineToSettle(
							l_invoiceLineToSettle
					);
					LOGGER.debug(
							"More $ " + p_calculationContainer
									.getRemainingInvoiceAmountToSettle()
									+ "  remaining to be settled for "
									+ l_invoiceLineToSettle.getInvoiceLineId()
					);

					break INVOICE_ITERATE;
				} else {
					// l_currentCardBalance -= l_currentLineAmount;
					l_currentCardBalance = l_currentCardBalance
							.subtract(l_currentLineAmount);

					// l_totalInvoiceToSettle -= l_currentLineAmount;
					l_totalInvoiceToSettle = l_totalInvoiceToSettle
							.subtract(l_currentLineAmount);

				}
			}

		}

		// if (p_settlementContainer.getShippingCharge() > IS_SETTLED) {
		if (
			Utils.isValueGreater(
					p_settlementContainer.getShippingCharge(),
					IS_SETTLED
			)
		)
		{
			settleShippingCharges(
					p_settlementContainer, p_repoContainer,
					p_calculationContainer,
					l_lowestCard, l_currentCardBalance
			);
		}

		createResponse(
				p_calculationContainer, p_settlementContainer,
				p_eiseResponseGeneric, l_settlementTenderResponse,
				l_settledInvoiceResponse
		);
		// enrichItemDetailsData(p_settlementContainer);
		LOGGER.debug(
				"---- Base Calculation: settlementBusinessLogic(): END---"
		);

	}
	// -------------------------------------------------------------------------

	/**
	 * 
	 * @param p_calculationContainer
	 * @param p_settlementContainer
	 * @param p_eiseResponseGeneric
	 * @param p_settlementTenderResponse
	 * @param p_settledInvoiceResponse
	 */
	protected void createResponse(
			CalculationUtil p_calculationContainer,
			ProcessingContainer p_settlementContainer,
			EISEResponseGeneric p_eiseResponseGeneric,
			List<InvoiceResponse> p_settlementTenderResponse,
			InvoiceResponse p_settledInvoiceResponse
	)
	{
		LOGGER.debug("---- Base Calculation: createResponse(): START---");
		// add response from Map created on Container
		p_settledInvoiceResponse.setPaymentMethod(
				p_settlementContainer
						.getPaymentMethodResponses().entrySet().stream()
						.filter(
								l_paymentMethod -> l_paymentMethod
										.getKey() != null
						)
						.map(Map.Entry::getValue)
						.filter(
								l_paymentMethod -> l_paymentMethod
										.getPaymentMethodId() != null
						)
						.collect(Collectors.toList())
		);
		p_settlementTenderResponse.add(p_settledInvoiceResponse);
		setGenericResponseTender(
				p_eiseResponseGeneric,
				p_settlementTenderResponse
		);
		LOGGER.debug("---- Base Calculation: createResponse(): END---");
	}

	// -------------------------------------------------------------------------
	/**
	 * To set response in settlement tender
	 * 
	 * @param p_genericResponse
	 * @param p_InvoiceResponses
	 */
	protected void setGenericResponseTender(
			EISEResponseGeneric p_genericResponse,
			List<InvoiceResponse> p_InvoiceResponses
	)
	{
		p_genericResponse.setSettlementTender(p_InvoiceResponses);
	}

	// -------------------------------------------------------------------------
	/**
	 * To get next available Lowest CC on Order
	 * 
	 * @param p_settlementContainer
	 * @return PaymentMethod
	 */
	protected PaymentMethod
			getNextAvailablePayment(ProcessingContainer p_settlementContainer)
	{
		return p_settlementContainer.getSettlementGCMethods().isEmpty()
				? (p_settlementContainer.getSettlementCCMethods().isEmpty()
						? (p_settlementContainer.getMilstarRetailCard() != null
								? p_settlementContainer.getMilstarRetailCard()
								: p_settlementContainer.getMilstarUniformCard())
						: getNextAvailableCC(p_settlementContainer))
				: getNextAvailableGC(p_settlementContainer);
	}

	// -------------------------------------------------------------------------
	/**
	 * To get next available Lowest CC on Order
	 * 
	 * @param p_settlementContainer
	 * @return PaymentMethod
	 */
	protected PaymentMethod
			getNextAvailableCC(ProcessingContainer p_settlementContainer)
	{
		return p_settlementContainer.getSettlementCCMethods().pop();
	}

	// -------------------------------------------------------------------------
	/**
	 * To get next available Lowest CC on Order
	 * 
	 * @param p_settlementContainer
	 * @return PaymentMethod
	 */
	protected PaymentMethod
			getNextAvailableGC(ProcessingContainer p_settlementContainer)
	{
		return p_settlementContainer.getSettlementGCMethods().poll();
	}

	// -------------------------------------------------------------------------
	/**
	 * check if line amount is available in remaining auth of payment method,
	 * return remaining auth to be settled on next card
	 * 
	 * @param pCurrentGCBalance
	 * @param pCurrentLineAmount
	 * @param pInvoiceLineToSettle
	 * @param pLowestGC
	 * @param p_paymentResponse
	 * @return boolean
	 */
	protected boolean checkAuthAvailability(
			BigDecimal pCurrentGCBalance,
			BigDecimal pCurrentLineAmount,
			InvoiceLine pInvoiceLineToSettle,
			PaymentMethod pLowestGC,
			PaymentMethodResponse p_paymentResponse,
			RepoContainer p_repoContainer
	)
	{
		boolean lSkipTONextPayment = false;
		// if (pCurrentLineAmount <= pCurrentGCBalance) {
		if (Utils.isValueLesserOrEqual(pCurrentLineAmount, pCurrentGCBalance)) {

			LOGGER.debug(
					"Amount to be calculated : $" + pCurrentLineAmount
							+ " is less than or equal to balance amount $"
							+ pCurrentGCBalance + " in " + pLowestGC
									.getPaymentMethodId()
			);
			// take amount from GC and subtract from GC remaining
			generatePaymentResponse(
					pInvoiceLineToSettle, pLowestGC,
					p_paymentResponse, pCurrentLineAmount, p_repoContainer
			);
			// } else if (pCurrentLineAmount > pCurrentGCBalance) {
		} else if (
			Utils.isValueGreater(
					pCurrentLineAmount,
					pCurrentGCBalance
			)
		)
		{
			// take whole PaymentMethod balance and get next GC
			generatePaymentResponse(
					pInvoiceLineToSettle, pLowestGC,
					p_paymentResponse, pCurrentGCBalance, p_repoContainer
			);
			lSkipTONextPayment = true;
		}
		return lSkipTONextPayment;
	}

	// ------------------------------------------------------------------------
	/**
	 * check if line amount is available in remaining auth of payment method,
	 * return remaining auth to be settled on next card
	 * 
	 * Overload for Invoice CHarge Detail
	 * 
	 * @param pCurrentGCBalance
	 * @param pCurrentLineAmount
	 * @param pInvoiceLineToSettle
	 * @param pLowestGC
	 * @param p_paymentResponse
	 * @return boolean
	 */
	protected boolean checkAuthAvailability(
			BigDecimal pCurrentGCBalance,
			BigDecimal pCurrentLineAmount,
			InvoiceChargeDetail pInvoiceChargeToSettle,
			PaymentMethod pLowestGC,
			PaymentMethodResponse p_paymentResponse,
			RepoContainer p_repoContainer
	)
	{
		boolean lSkipTONextPayment = false;
		// if (pCurrentLineAmount <= pCurrentGCBalance) {
		if (Utils.isValueLesserOrEqual(pCurrentLineAmount, pCurrentGCBalance)) {
			LOGGER.debug(
					"Amount to be calculated : $" + pCurrentLineAmount
							+ " is less than or equal to balance amount $"
							+ pCurrentGCBalance + " in " + pLowestGC
									.getPaymentMethodId()
			);
			// take amount from GC and subtract from GC remaining
			generatePaymentResponse(
					pInvoiceChargeToSettle, pLowestGC,
					p_paymentResponse, pCurrentLineAmount, p_repoContainer
			);
			// } else if (pCurrentLineAmount > pCurrentGCBalance) {
		} else if (isValueGreater(pCurrentLineAmount, pCurrentGCBalance)) {
			// take whole PaymentMethod balance and get next GC
			generatePaymentResponse(
					pInvoiceChargeToSettle, pLowestGC,
					p_paymentResponse, pCurrentGCBalance, p_repoContainer
			);
			lSkipTONextPayment = true;
		}
		return lSkipTONextPayment;
	}

	// ------------------------------------------------------------------------
	/**
	 * Generate response heirachy and returns new payment method if former
	 * payment method auth is FINISHED
	 * 
	 * @param pInvoiceLine
	 * @param p_currentCard
	 * @param p_paymentResponse
	 * @param pSettledAmount
	 * @param p_repoContainer
	 */
	protected void generatePaymentResponse(
			InvoiceLine pInvoiceLine,
			PaymentMethod p_currentCard,
			PaymentMethodResponse p_paymentResponse,
			BigDecimal pSettledAmount,
			RepoContainer p_repoContainer
	)
	{
		LOGGER.debug(
				"---- Base Calculation: generatePaymentResponse() for Invoice Line id:",
				pInvoiceLine.getInvoiceLineId()
		);
		InvoiceLineResponse lInvoiceLineResponse = new InvoiceLineResponse();
		lInvoiceLineResponse.setInvoiceId(pInvoiceLine.getInvoiceId());
		lInvoiceLineResponse.setInvoiceLineId(pInvoiceLine.getInvoiceLineId());
		lInvoiceLineResponse.setPaymentAmount(pSettledAmount);
		p_paymentResponse
				.setPaymentMethodId(p_currentCard.getPaymentMethodId());
		p_paymentResponse.getInvoiceLine().add(lInvoiceLineResponse);
		LOGGER.debug(
				pInvoiceLine.getInvoiceLineId()
						+ " is calculated against $" + p_currentCard
								.getPaymentMethodId() + " for $"
						+ pSettledAmount
		);
		// Repository change
		RepoUtils.addToInvoiceItemDetailsList(
				pInvoiceLine, p_currentCard, pSettledAmount,
				p_repoContainer
		);

		LOGGER.debug(
				"---- Base Calculation: generatePaymentResponse() for Invoice Line: END---"
		);

	}

	// ------------------------------------------------------------------------
	/**
	 * Generate response heirachy and returns new payment method if former
	 * payment method auth is FINISHED
	 * 
	 * Overload for Invoice Charge Type
	 * 
	 * @param pInvoiceLine
	 * @param p_currentCard
	 * @param p_paymentResponse
	 * @param pSettledAmount
	 * @param p_repoContainer
	 */
	protected void generatePaymentResponse(
			InvoiceChargeDetail pInvoiceCharge,
			PaymentMethod p_currentCard,
			PaymentMethodResponse p_paymentResponse,
			BigDecimal pSettledAmount,
			RepoContainer p_repoContainer
	)
	{
		LOGGER.debug(
				"---- Base Calculation: generatePaymentResponse() for Invoice Charges: ",
				pInvoiceCharge.getChargeDetailId()
		);
		InvoiceChargeDetailResponse lInvoiceLineResponse = new InvoiceChargeDetailResponse();
		lInvoiceLineResponse.setInvoiceId(pInvoiceCharge.getInvoiceId());
		lInvoiceLineResponse
				.setChargeDetailId(pInvoiceCharge.getChargeDetailId());
		lInvoiceLineResponse.setPaymentAmount(pSettledAmount);
		p_paymentResponse
				.setPaymentMethodId(p_currentCard.getPaymentMethodId());
		p_paymentResponse.getInvoiceCharge().add(lInvoiceLineResponse);

		LOGGER.debug(
				pInvoiceCharge.getChargeDetailId()
						+ " is calculated against $" + p_currentCard
								.getPaymentMethodId() + " for $"
						+ pSettledAmount
		);
		// Repository change
		RepoUtils.addToInvoiceChargeDetailsList(
				pInvoiceCharge,
				p_currentCard, pSettledAmount, p_repoContainer
		);

		LOGGER.debug(
				"---- Base Calculation: generatePaymentResponse() for Invoice Charges: END---"
		);

	}

	// ------------------------------------------------------------------------
	/**
	 * check if current item is Milstar uniform Item
	 * 
	 * @param p_settlementContainer
	 * @param pMULine
	 * @return boolean
	 */
	protected boolean isLineUniformItem(
			ProcessingContainer p_settlementContainer,
			InvoiceLine pMULine
	)
	{
		if (!p_settlementContainer.getMilitaryUniformLines().contains(pMULine))
			return false;
		return true;
	}

	// ------------------------------------------------------------------------
	/**
	 * check if current item is Milstar Retail Item
	 * 
	 * @param p_settlementContainer
	 * @param pRetailLine
	 * @return boolean
	 */
	protected boolean isLineRetailItem(
			ProcessingContainer p_settlementContainer,
			InvoiceLine pRetailLine
	)
	{
		if (
			!p_settlementContainer.getMilitaryPromoLines()
					.contains(pRetailLine)
		)
			return false;
		return true;
	}

	// ------------------------------------------------------------------------
	/**
	 * Settled the line amount against MU card and carry forward the reamining
	 * amount and line information to global objects defined in class and return
	 * how much is settled
	 * 
	 * @param p_settlementContainer
	 * @param p_repoContainer
	 * @param p_calculationContainer
	 * @param p_invoiceLineMU
	 * @return BigDecimal
	 */
	protected BigDecimal settleAgainstMUCard(
			ProcessingContainer p_settlementContainer,
			RepoContainer p_repoContainer,
			CalculationUtil p_calculationContainer,
			InvoiceLine p_invoiceLineMU
	)
	{

		BigDecimal lSettledAmount = IS_SETTLED;
		PaymentMethod lUniformCard = p_settlementContainer
				.getMilstarUniformCard();
		if (p_settlementContainer.getMilstarUniformCard() == null) {
			p_calculationContainer.setRemainingInvoiceAmountToSettle(
					p_invoiceLineMU.getInvoiceLineTotal()
			);
			p_calculationContainer
					.setRemainingInvoiceLineToSettle(p_invoiceLineMU);
			return ZERO_SETTLED;
		}
		// if (p_invoiceLineMU.getInvoiceLineTotal() <= p_calculationContainer
		// .getAmountMUAvailableForSettlement()) {
		if (
			Utils.isValueLesserOrEqual(
					p_invoiceLineMU.getInvoiceLineTotal(),
					p_calculationContainer.getAmountMUAvailableForSettlement()
			)
		)
		{
			/**
			 * Generate Uniform Card Response for Uniform card for invoice line
			 */
			generatePaymentResponse(
					p_invoiceLineMU, lUniformCard,
					p_settlementContainer.getPaymentMethodResponses()
							.get(lUniformCard.getPaymentMethodId()),
					p_invoiceLineMU.getInvoiceLineTotal(),
					p_repoContainer
			);
			// p_calculationContainer.setAmountMUAvailableForSettlement(
			// p_calculationContainer.getAmountMUAvailableForSettlement()
			// - p_invoiceLineMU.getInvoiceLineTotal());
			p_calculationContainer.setAmountMUAvailableForSettlement(
					p_calculationContainer.getAmountMUAvailableForSettlement()
							.subtract(p_invoiceLineMU.getInvoiceLineTotal())
			);

			lSettledAmount = p_invoiceLineMU.getInvoiceLineTotal();
			// } else if (p_invoiceLineMU
			// .getInvoiceLineTotal() > p_calculationContainer
			// .getAmountMUAvailableForSettlement()) {
		} else if (
			isValueGreater(
					p_invoiceLineMU.getInvoiceLineTotal(),
					p_calculationContainer.getAmountMUAvailableForSettlement()
			)
		)
		{
			/**
			 * Generate Uniform Card Response for Uniform card for invoice line
			 */
			generatePaymentResponse(
					p_invoiceLineMU, lUniformCard,
					p_settlementContainer.getPaymentMethodResponses()
							.get(lUniformCard.getPaymentMethodId()),
					p_calculationContainer.getAmountMUAvailableForSettlement(),
					p_repoContainer
			);
			// p_calculationContainer.setRemainingInvoiceAmountToSettle(
			// p_invoiceLineMU.getInvoiceLineTotal()
			// - p_calculationContainer
			// .getAmountMUAvailableForSettlement());
			p_calculationContainer.setRemainingInvoiceAmountToSettle(
					p_invoiceLineMU.getInvoiceLineTotal()
							.subtract(
									p_calculationContainer
											.getAmountMUAvailableForSettlement()
							)
			);
			p_calculationContainer
					.setRemainingInvoiceLineToSettle(p_invoiceLineMU);
			lSettledAmount = p_calculationContainer
					.getAmountMUAvailableForSettlement();
			p_calculationContainer
					.setAmountMUAvailableForSettlement(IS_SETTLED);
		}
		return lSettledAmount;
	}

	// ------------------------------------------------------------------------
	/**
	 * Method to Settle Invoice Chargees against CC or GC
	 * 
	 * @param p_settlementContainer
	 * @param p_repoContainer
	 * @param p_calculationContainer
	 * @param p_currentCard
	 * @param p_remainingCardBalance
	 */
	protected void settleShippingCharges(
			ProcessingContainer p_settlementContainer,
			RepoContainer p_repoContainer,
			CalculationUtil p_calculationContainer,
			PaymentMethod p_currentCard,
			BigDecimal p_remainingCardBalance
	)
	{

		BigDecimal l_currentCardBalance = p_remainingCardBalance;
		BigDecimal l_totalChargesToSettle = p_settlementContainer
				.getInvoiceChargeTotal();
		InvoiceChargeDetail lTempChargeHolder = new InvoiceChargeDetail();
		BigDecimal lLeftOverAmount = IS_SETTLED;
		UNTIL_CHARGES_ARE_SETTLED:
		// while (l_totalChargesToSettle > IS_SETTLED) {
		while (isValueGreater(l_totalChargesToSettle, IS_SETTLED)) {

			// if (l_currentCardBalance == IS_FINISHED) {
			if (isValueLesserOrEqual(l_currentCardBalance, IS_FINISHED)) {
				/**
				 * Get New Payment Method & Balance for Settling Invoice Line.
				 * If all GC are Finished, will take CC
				 */
				p_currentCard = getNextAvailablePayment(p_settlementContainer);
				l_currentCardBalance = getCardBalance(p_currentCard);
				// getPaymentMethodResponses().add(lPaymentResponse);

			}
			PaymentMethodResponse p_paymentResponse = p_settlementContainer
					.getPaymentMethodResponses()
					.get(p_currentCard.getPaymentMethodId());
			/**
			 * do this when there is some balance of invoice line needs to be
			 * settled against second payment method
			 */
			// if (lLeftOverAmount > IS_SETTLED) {
			if (isValueGreater(lLeftOverAmount, IS_SETTLED)) {
				// check availaibility of line amount on payment method
				if (
					checkAuthAvailability(
							l_currentCardBalance, lLeftOverAmount,
							lTempChargeHolder, p_currentCard, p_paymentResponse,
							p_repoContainer
					)
				)
				{
					// lLeftOverAmount = lLeftOverAmount
					// - l_currentCardBalance;
					// l_totalChargesToSettle -= l_currentCardBalance;
					// lLeftOverAmount = this.subtractDouble(lLeftOverAmount,
					// l_currentCardBalance);
					lLeftOverAmount = lLeftOverAmount
							.subtract(l_currentCardBalance);
					// l_totalChargesToSettle = this.subtractDouble(
					// l_totalChargesToSettle, l_currentCardBalance);
					l_totalChargesToSettle = l_totalChargesToSettle
							.subtract(l_currentCardBalance);
					l_currentCardBalance = IS_FINISHED;
					// if not available get next GC or CC
					continue UNTIL_CHARGES_ARE_SETTLED;
				} else {
					// l_currentCardBalance -= p_calculationContainer
					// .getRemainingInvoiceAmountToSettle();
					// l_totalChargesToSettle -= p_calculationContainer
					// .getRemainingInvoiceAmountToSettle();
					// l_currentCardBalance = this.subtractDouble(
					// l_currentCardBalance, p_calculationContainer
					// .getRemainingInvoiceAmountToSettle());
					/*
					 * l_currentCardBalance = l_currentCardBalance
					 * .subtract(p_calculationContainer
					 * .getRemainingInvoiceAmountToSettle());
					 */
					l_currentCardBalance = l_currentCardBalance
							.subtract(lLeftOverAmount);
					// l_totalChargesToSettle = this.subtractDouble(
					// l_totalChargesToSettle, p_calculationContainer
					// .getRemainingInvoiceAmountToSettle());
					/*
					 * l_totalChargesToSettle = l_totalChargesToSettle
					 * .subtract(p_calculationContainer
					 * .getRemainingInvoiceAmountToSettle());
					 */
					l_totalChargesToSettle = l_totalChargesToSettle
							.subtract(lLeftOverAmount);

				}
			}

			SHIPPING_CHARGE_ITERATE: while (
				!p_settlementContainer
						.getShippingChargeQueue().isEmpty()
			)
			{
				InvoiceChargeDetail lChargeToSettle = p_settlementContainer
						.getShippingChargeQueue().poll();
				BigDecimal l_currentLineAmount = lChargeToSettle
						.getChargeTotal();

				// check availability of line amount on payment method
				if (
					checkAuthAvailability(
							l_currentCardBalance,
							l_currentLineAmount, lChargeToSettle, p_currentCard,
							p_paymentResponse, p_repoContainer
					)
				)
				{
					// lLeftOverAmount = l_currentLineAmount
					// - l_currentCardBalance;
					// l_totalChargesToSettle -= l_currentCardBalance;
					lLeftOverAmount = l_currentLineAmount
							.subtract(l_currentCardBalance);
					l_totalChargesToSettle = l_totalChargesToSettle
							.subtract(l_currentCardBalance);
					l_currentCardBalance = IS_FINISHED;
					lTempChargeHolder = lChargeToSettle;
					// if not available get next GC or CC
					break SHIPPING_CHARGE_ITERATE;
				} else {
					// l_currentCardBalance -= l_currentLineAmount;
					l_currentCardBalance = l_currentCardBalance
							.subtract(l_currentLineAmount);
					// l_totalChargesToSettle -= l_currentLineAmount;
					l_totalChargesToSettle = l_totalChargesToSettle
							.subtract(l_currentLineAmount);
				}
			}
		}
	}

	// -----------------------------------------------------------------------------------
	/**
	 * Settled the line amount against MR card and carry forward the reamining
	 * amount and line information to global objects defined in class
	 * 
	 * @param p_settlementContainer
	 * @param p_repoContainer
	 * @param p_calculationContainer
	 * @param p_invoiceLineMR
	 * @return BigDecimal
	 */
	protected BigDecimal settleAgainstMRCard(
			ProcessingContainer p_settlementContainer,
			RepoContainer p_repoContainer,
			CalculationUtil p_calculationContainer,
			InvoiceLine p_invoiceLineMR
	)
	{

		BigDecimal lSettledAmount = ZERO_SETTLED;
		PaymentMethod lRetailCard = p_settlementContainer
				.getMilstarRetailCard();
		if (p_settlementContainer.getMilstarRetailCard() == null) {
			p_calculationContainer.setRemainingInvoiceAmountToSettle(
					p_invoiceLineMR.getInvoiceLineTotal()
			);
			p_calculationContainer
					.setRemainingInvoiceLineToSettle(p_invoiceLineMR);
			return ZERO_SETTLED;
		}
		// if (p_invoiceLineMR.getInvoiceLineTotal() <= p_calculationContainer
		// .getAmountMRAvailableForSettlement()) {
		if (
			Utils.isValueLesserOrEqual(
					p_invoiceLineMR.getInvoiceLineTotal(),
					p_calculationContainer.getAmountMRAvailableForSettlement()
			)
		)
		{
			generatePaymentResponse(
					p_invoiceLineMR, lRetailCard,
					p_settlementContainer.getPaymentMethodResponses()
							.get(lRetailCard.getPaymentMethodId()),
					p_invoiceLineMR.getInvoiceLineTotal(),
					p_repoContainer
			);

			// p_calculationContainer.setAmountMRAvailableForSettlement(
			// p_calculationContainer.getAmountMRAvailableForSettlement()
			// - p_invoiceLineMR.getInvoiceLineTotal());
			p_calculationContainer.setAmountMRAvailableForSettlement(
					p_calculationContainer.getAmountMRAvailableForSettlement()
							.subtract(p_invoiceLineMR.getInvoiceLineTotal())
			);
			lSettledAmount = p_invoiceLineMR.getInvoiceLineTotal();
			// } else if (p_calculationContainer
			// .getAmountMRAvailableForSettlement() <= IS_SETTLED) {
		} else if (
			Utils.isValueLesserOrEqual(
					p_calculationContainer.getAmountMRAvailableForSettlement(),
					IS_SETTLED
			)
		)
		{
			p_calculationContainer.setRemainingInvoiceAmountToSettle(
					p_invoiceLineMR.getInvoiceLineTotal()
			);
			p_calculationContainer
					.setRemainingInvoiceLineToSettle(p_invoiceLineMR);
			lSettledAmount = ZERO_SETTLED;
			p_calculationContainer
					.setAmountMRAvailableForSettlement(IS_SETTLED);

			// } else if (p_invoiceLineMR
			// .getInvoiceLineTotal() > p_calculationContainer
			// .getAmountMRAvailableForSettlement()) {
		} else if (
			isValueGreater(
					p_invoiceLineMR.getInvoiceLineTotal(),
					p_calculationContainer.getAmountMRAvailableForSettlement()
			)
		)
		{
			generatePaymentResponse(
					p_invoiceLineMR, lRetailCard,
					p_settlementContainer.getPaymentMethodResponses()
							.get(lRetailCard.getPaymentMethodId()),
					p_calculationContainer.getAmountMRAvailableForSettlement(),
					p_repoContainer
			);

			// p_calculationContainer.setRemainingInvoiceAmountToSettle(
			// p_invoiceLineMR.getInvoiceLineTotal()
			// - p_calculationContainer
			// .getAmountMRAvailableForSettlement());
			p_calculationContainer.setRemainingInvoiceAmountToSettle(
					p_invoiceLineMR.getInvoiceLineTotal()
							.subtract(
									p_calculationContainer
											.getAmountMRAvailableForSettlement()
							)
			);
			p_calculationContainer
					.setRemainingInvoiceLineToSettle(p_invoiceLineMR);
			lSettledAmount = p_calculationContainer
					.getAmountMRAvailableForSettlement();
			p_calculationContainer
					.setAmountMRAvailableForSettlement(IS_SETTLED);
		}
		return lSettledAmount;
	}

	/*
	 * public void enrichItemDetailsData( ProcessingContainer
	 * p_settlementContainer ) { List<InvoiceItemSettlement>
	 * l_invoiceItemSettlement = p_settlementContainer
	 * .getInvoiceItemSettlementList(); // For each PaymentMethod find the
	 * matching Settled invoice lines // Update the invoice line list in the
	 * paymentmethod // Also update the PaymentMethod reference in each
	 * invoiceLine for (InvoiceItemSettlement l_settlementDao :
	 * l_invoiceItemSettlement) { // String l_paymentId =
	 * l_settlementDao.getPaymentMethodId(); List<InvoiceItemDetails>
	 * l_invoiceItemDetailsFilteredList = p_settlementContainer
	 * .getInvoiceItemDetailsList() .stream().filter( l_invoiceDetails ->
	 * l_invoiceDetails .getPaymentMethodId() == l_settlementDao
	 * .getPaymentMethodId() ) .collect(Collectors.toList());
	 * System.out.println( "filtered list l_invoiceItemDetailsFilteredList:" +
	 * l_invoiceItemDetailsFilteredList.size() + "-" +
	 * l_invoiceItemDetailsFilteredList.toString() );
	 * l_settlementDao.setInvoiceItemDetailsList(
	 * l_invoiceItemDetailsFilteredList ); }
	 * 
	 * }
	 */

	// -------------------------------------------------------------------------
	/**
	 * This method compare values for Greaterthan
	 * 
	 * @param p_01
	 * 
	 * @param p_02
	 * 
	 * @return boolean
	 */
	public static boolean isValueGreater(BigDecimal p_01, BigDecimal p_02) {

		boolean l_isGreater = false;
		if (p_01.compareTo(p_02) == 1) {
			l_isGreater = true;
		}
		return l_isGreater;
	}

	// -------------------------------------------------------------------------
	/**
	 * This method compare values for LessthanorEqual
	 * 
	 * @param p_01
	 * 
	 * @param p_02
	 * 
	 * @return boolean
	 */
	public static boolean
			isValueLesserOrEqual(BigDecimal p_01, BigDecimal p_02)
	{

		boolean l_isLesser = false;
		if (p_01.compareTo(p_02) <= 0) {
			l_isLesser = true;
		}
		return l_isLesser;
	}

	// -------------------------------------------------------------------------
	/**
	 * This method compare values for Equal
	 * 
	 * @param p_01
	 * 
	 * @param p_02
	 * 
	 * @return boolean
	 */
	public static boolean isValueEqual(BigDecimal p_01, BigDecimal p_02) {

		boolean l_isEqual = false;

		if (p_01.compareTo(p_02) == 0) {
			l_isEqual = true;
		}
		return l_isEqual;
	}
	// -------------------------------------------------------------------------
}