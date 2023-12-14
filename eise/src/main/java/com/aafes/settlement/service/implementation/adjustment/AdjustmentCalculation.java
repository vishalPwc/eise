package com.aafes.settlement.service.implementation.adjustment;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import com.aafes.settlement.core.invoice.InvoiceChargeDetail;
import com.aafes.settlement.core.model.ProcessingContainer;
import com.aafes.settlement.core.model.adjustment.AdjustmentProcessingContainer;
import com.aafes.settlement.core.payment.PaymentMethod;
import com.aafes.settlement.core.response.EISEResponseGeneric;
import com.aafes.settlement.core.response.InvoiceChargeDetailResponse;
import com.aafes.settlement.core.response.InvoiceResponse;
import com.aafes.settlement.core.response.PaymentMethodResponse;
import com.aafes.settlement.repository.RepoContainer;
import com.aafes.settlement.service.implementation.BaseCalculation;
import com.aafes.settlement.util.CalculationUtil;
import com.aafes.settlement.util.RepoUtils;

/**
 * Reusing Settlement Calculation and overriding few of the methods to
 * Accommodate changes for refunds. e.g. Changing calculation to consider
 * attribute Current Settled Amount instead of current Auth
 */
@Service
public class AdjustmentCalculation
	extends
	BaseCalculation
{
	// ------------------------------------------------------------------------
	private static final Logger LOGGER = LogManager
			.getLogger(AdjustmentCalculation.class);

	// ------------------------------------------------------------------------
	/**
	 * Public method exposed to calculate payment tenders and generate
	 * settlement response
	 * 
	 * This can potentially be moved to BaseClass.
	 * 
	 * @param p_container
	 * @param p_repoContainer
	 * @param p_eiseResponseGeneric
	 */
	public void calculatePaymentTenders(
			AdjustmentProcessingContainer p_container,
			RepoContainer p_repoContainer,
			EISEResponseGeneric p_eiseResponseGeneric
	)
	{
		LOGGER.debug(
				"---- Adjustment Request: calculatePaymentTenders(): START---"
		);
		CalculationUtil l_calculationContainer = new CalculationUtil();
		if (p_container.isMilstarBucket()) {
			LOGGER.debug(
					"---- Adjustment Request: calculatePaymentTenders(): "
							+ "Request Has Milstar Card---"
			);
			if (p_container.getMilstarUniformCard() != null) {
				this.calculatePreBucketMU(l_calculationContainer, p_container);
				LOGGER.debug(
						"---- Adjustment Request:"
								+ "calculatePreBucketMU(): MU Available : ",
						l_calculationContainer
								.getAmountMUAvailableForSettlement()
				);
			}
			if (p_container.getMilstarRetailCard() != null) {
				this.calculatePreBucketMR(l_calculationContainer, p_container);
				LOGGER.debug(
						"---- Adjustment Request:"
								+ "calculatePreBucketMR(): MR Available : ",
						l_calculationContainer
								.getAmountMRAvailableForSettlement()
				);
			}
		}
		LOGGER.debug(
				"The rule for adjustment is Lowest GC followed by highest and lastly Credit Card and In case of Milstar Card attached, after reserving amount for pending items for specific line (Retail or Uniform)"
		);
		adjustmentBusinessLogic(
				l_calculationContainer, p_container, p_repoContainer,
				p_eiseResponseGeneric
		);
		LOGGER.debug(
				"---- Adjustment Request: calculatePaymentTenders(): END---"
		);
	}

	// ------------------------------------------------------------------------
	/**
	 * Set MU Amount which is available for refunding current Invoice is (Total
	 * Settled - Already returned MU)
	 * 
	 * @param p_calculationContainer
	 * @param p_refundContainer
	 */
	@Override
	protected void calculatePreBucketMU(
			CalculationUtil p_calculationContainer,
			ProcessingContainer p_refundContainer
	)
	{
		// MU Amount which is available for refunding current Invoice is (Total
		// Settled - Returned MU)
		p_calculationContainer.setAmountMUAvailableForSettlement(
				p_refundContainer.getTotalPaymentUniformSettled()
						.subtract(p_refundContainer.getTotalMUReturnedAmount())
		);
	}

	// -------------------------------------------------------------------------
	/**
	 * Complete Settlement Invoice Business Logic against Lowest GC first
	 * followed by Highest Gift Card Includes Items which are pending for
	 * settlement after MU or MR
	 * 
	 * @param p_calculationContainer
	 * @param p_adjustmentContainer
	 * @param p_repoContainer
	 * @param p_eiseResponseGeneric
	 */
	protected void adjustmentBusinessLogic(
			CalculationUtil p_calculationContainer,
			AdjustmentProcessingContainer p_adjustmentContainer,
			RepoContainer p_repoContainer,
			EISEResponseGeneric p_eiseResponseGeneric
	)
	{
		List<InvoiceResponse> l_adjustmentTenderResponse = new ArrayList<
				InvoiceResponse>();
		InvoiceResponse l_adjustmentInvoiceResponse = new InvoiceResponse();
		BigDecimal l_totalInvoiceToSettle = p_adjustmentContainer
				.getOpenInvoiceLineTotal();
		PaymentMethod l_lowestCard = getNextAvailablePayment(
				p_adjustmentContainer
		);
		BigDecimal l_currentCardBalance = getCardBalance(l_lowestCard);

		// venkat

		String l_chargePlan = "";

		// UNTIL_CHARGE_IS_SETTLED: while (
		// l_totalInvoiceToSettle > IS_SETTLED
		// )
		UNTIL_CHARGE_IS_SETTLED: while (
			isValueGreater(l_totalInvoiceToSettle, IS_SETTLED)
		)
		{
			// if (l_currentCardBalance == IS_FINISHED) {
			if (isValueEqual(l_currentCardBalance, IS_FINISHED)) {
				/**
				 * Get New Payment Method & Balance for Settling Invoice Line.
				 * If all GC are Finished, will take CC
				 */
				l_lowestCard = getNextAvailablePayment(p_adjustmentContainer);

				// l_lowestCard =
				// getNextAvailablePayment(p_adjustmentContainer,l_chargePlan);

				l_currentCardBalance = getCardBalance(l_lowestCard);
				LOGGER.debug(
						"Now moving to next payment method " + l_lowestCard
								.getPaymentMethodId()
								+ "to settle the remaining amount"
				);
			}
			PaymentMethodResponse l_paymentResponse = p_adjustmentContainer
					.getPaymentMethodResponses().get(
							l_lowestCard.getPaymentMethodId()
					);
			/**
			 * do this when there is some balance of invoice line needs to be
			 * settled against second payment method
			 */
			if (
				isValueGreater(
						p_calculationContainer
								.getRemainingInvoiceAmountToSettle(), IS_SETTLED
				)
			)
			{
				// check availability of line amount on payment method
				if (
					checkAuthAvailability(
							l_currentCardBalance, p_calculationContainer
									.getRemainingInvoiceAmountToSettle(),
							p_calculationContainer
									.getRemainingInvoiceChargeToSettle(),
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
					l_totalInvoiceToSettle = l_totalInvoiceToSettle.subtract(
							l_currentCardBalance
					);
					// l_totalInvoiceToSettle = this.subtractDouble(
					// l_totalInvoiceToSettle,
					// l_currentCardBalance
					// );
					l_currentCardBalance = IS_FINISHED;

					if (
						l_currentCardBalance == IS_FINISHED && l_lowestCard
								.getCardType().getCardTypeId().equals(
										MILSTAR_CARD
								)
					)
					{
						if (
							l_lowestCard.getExtended().getResponsePlan()
									.equals(PLAN_MILITARY_RETAIL)
						)
						{
							p_adjustmentContainer.setMilstarRetailCard(null);
						}

						else {
							p_adjustmentContainer.setMilstarUniformCard(null);
						}
					}

					continue UNTIL_CHARGE_IS_SETTLED;
				} else {

					l_currentCardBalance = l_currentCardBalance.subtract(
							p_calculationContainer
									.getRemainingInvoiceAmountToSettle()
					);

					l_totalInvoiceToSettle = l_totalInvoiceToSettle.subtract(
							p_calculationContainer
									.getRemainingInvoiceAmountToSettle()
					);
					// l_currentCardBalance = this.subtractDouble(
					// l_currentCardBalance,
					// p_calculationContainer
					// .getRemainingInvoiceAmountToSettle()
					// );
					//
					// l_totalInvoiceToSettle = this.subtractDouble(
					// l_totalInvoiceToSettle,
					// p_calculationContainer
					// .getRemainingInvoiceAmountToSettle()
					// );
				}
			}
			ITERATE_INVOICE_LINE_CHARGES: while (
				!p_adjustmentContainer
						.getOpenChargeDetails().isEmpty()
			)
			{
				InvoiceChargeDetail l_invoiceChargeToSettle = p_adjustmentContainer
						.getOpenChargeDetails()
						.poll();

				l_chargePlan = l_invoiceChargeToSettle.getPlanNumber();

				BigDecimal l_currentChargeAmount = l_invoiceChargeToSettle
						.getChargeTotal();

				LOGGER.debug(
						"Now we are doing calculation for invoice  with ID : "
								+ l_invoiceChargeToSettle.getChargeDetailId()
								+ " for $" + l_currentChargeAmount
				);

				/**
				 * check if item is MU OR MR && have milstar payment methods
				 * 
				 * go inside block only if AUTH available on MU or MR is more
				 * than 0 after pre-bucketing
				 */
				if (
					(l_lowestCard.getCardType()
							.getCardTypeId() == MILSTAR_CARD)
							&& (isValueGreater(
									p_calculationContainer
											.getAmountMRAvailableForSettlement(),
									IS_SETTLED
							)
									|| isValueGreater(
											p_calculationContainer
													.getAmountMUAvailableForSettlement(),
											IS_SETTLED
									))
				)
					if (
						isLineUniformItem(
								l_invoiceChargeToSettle
						) && p_adjustmentContainer.isMilstarBucket()

					)
					{
						// settle against MU Card first
						l_totalInvoiceToSettle = l_totalInvoiceToSettle
								.subtract(
										settleAgainstMUCard(
												p_adjustmentContainer,
												p_repoContainer,
												p_calculationContainer,
												l_invoiceChargeToSettle
										)
								);
						// l_totalInvoiceToSettle = this.subtractDouble(
						// l_totalInvoiceToSettle,
						// settleAgainstMUCard(
						// p_adjustmentContainer,
						// p_calculationContainer,
						// l_invoiceChargeToSettle
						// )
						// );

						break ITERATE_INVOICE_LINE_CHARGES;
					} else if (
						isLineRetailItem(
								l_invoiceChargeToSettle
						) && p_adjustmentContainer.isMilstarBucket()
					)
					{
						l_totalInvoiceToSettle = l_totalInvoiceToSettle
								.subtract(
										settleAgainstMRCard(
												p_adjustmentContainer,
												p_repoContainer,
												p_calculationContainer,
												l_invoiceChargeToSettle
										)
								);
						// l_totalInvoiceToSettle = this.subtractDouble(
						// l_totalInvoiceToSettle,
						// settleAgainstMRCard(
						// p_adjustmentContainer,
						// p_calculationContainer,
						// l_invoiceChargeToSettle
						// )
						// );

						break ITERATE_INVOICE_LINE_CHARGES;
					}
				// check availability of line amount on payment method
				if (
					checkAuthAvailability(
							l_currentCardBalance, l_currentChargeAmount,
							l_invoiceChargeToSettle, l_lowestCard,
							l_paymentResponse, p_repoContainer
					)
				)
				{
					p_calculationContainer.setRemainingInvoiceAmountToSettle(
							l_currentChargeAmount
									.subtract(l_currentCardBalance)
					);
					l_totalInvoiceToSettle = l_totalInvoiceToSettle.subtract(
							l_currentCardBalance
					);
					// l_totalInvoiceToSettle = this.subtractDouble(
					// l_totalInvoiceToSettle,
					// l_currentCardBalance
					// );

					l_currentCardBalance = IS_FINISHED;
					p_calculationContainer.setRemainingInvoiceChargeToSettle(
							l_invoiceChargeToSettle
					);

					LOGGER.debug(
							"More $ " + p_calculationContainer
									.getRemainingInvoiceAmountToSettle()
									+ "  remaining to be settled for "
									+ l_invoiceChargeToSettle
											.getChargeDetailId()
					);
					break ITERATE_INVOICE_LINE_CHARGES;
				} else {
					l_currentCardBalance = l_currentCardBalance.subtract(
							l_currentChargeAmount
					);
					// l_currentCardBalance = this.subtractDouble(
					// l_currentCardBalance,
					// l_currentChargeAmount
					// );

					l_totalInvoiceToSettle = l_totalInvoiceToSettle.subtract(
							l_currentChargeAmount
					);
					// l_totalInvoiceToSettle = this.subtractDouble(
					// l_totalInvoiceToSettle,
					// l_currentChargeAmount
					// );

				}
			}

		}

		if (
			isValueGreater(
					p_adjustmentContainer.getInvoiceChargeTotal(), IS_SETTLED
			)
		)
			settleShippingCharges(
					p_adjustmentContainer, p_repoContainer,
					p_calculationContainer, l_lowestCard,
					l_currentCardBalance
			);

		createResponse(
				p_calculationContainer, p_adjustmentContainer,
				p_eiseResponseGeneric,
				l_adjustmentTenderResponse, l_adjustmentInvoiceResponse
		);
		LOGGER.debug(
				"---- Base Calculation: adjustmentBusinessLogic(): END---"
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
	 * @param p_settledAmount
	 * @param p_repoContainer
	 */
	@Override
	protected void generatePaymentResponse(
			InvoiceChargeDetail pInvoiceCharge, PaymentMethod p_currentCard,
			PaymentMethodResponse p_paymentResponse, BigDecimal p_settledAmount,
			RepoContainer p_repoContainer
	)
	{
		InvoiceChargeDetailResponse lInvoiceChargeResponse = new InvoiceChargeDetailResponse();
		p_paymentResponse.setInvoiceLine(null);
		lInvoiceChargeResponse.setInvoiceId(pInvoiceCharge.getInvoiceId());
		lInvoiceChargeResponse.setChargeDetailId(
				pInvoiceCharge.getChargeDetailId()
		);

		lInvoiceChargeResponse.setPaymentAmount(p_settledAmount);
		p_paymentResponse.setPaymentMethodId(
				p_currentCard.getPaymentMethodId()
		);
		if (pInvoiceCharge.getInvoiceLineId() != null) {
			lInvoiceChargeResponse.setInvoiceLineId(
					pInvoiceCharge.getInvoiceLineId()
			);
			p_paymentResponse.getInvoiceLineCharge().add(
					lInvoiceChargeResponse
			);
		} else {
			p_paymentResponse.getInvoiceCharge().add(lInvoiceChargeResponse);
		}

		LOGGER.debug(
				pInvoiceCharge.getChargeDetailId()
						+ " is calculated against $" + p_currentCard
								.getPaymentMethodId() + " for $"
						+ p_settledAmount
		);

		// Repository change
		RepoUtils.addToInvoiceChargeDetailsList(
				pInvoiceCharge,
				p_currentCard, p_settledAmount, p_repoContainer
		);

	}

	// -----------------------------------------------------------------------------------
	/**
	 * Settled the line amount against MR card and carry forward the reamining
	 * amount and line information to global objects defined in class
	 * 
	 * @param p_adjustmentContainer
	 * @param p_repoContainer
	 * @param p_calculationContainer
	 * @param p_invoiceLineMR
	 * @return BigDecimal
	 */
	protected BigDecimal settleAgainstMRCard(
			AdjustmentProcessingContainer p_adjustmentContainer,
			RepoContainer p_repoContainer,
			CalculationUtil p_calculationContainer,
			InvoiceChargeDetail p_invoiceLineMR
	)
	{
		BigDecimal lSettledAmount = ZERO_SETTLED;
		PaymentMethod lRetailCard = p_adjustmentContainer
				.getMilstarRetailCard();
		if (p_adjustmentContainer.getMilstarRetailCard() == null) {
			p_calculationContainer.setRemainingInvoiceAmountToSettle(
					p_invoiceLineMR
							.getChargeTotal()
			);
			p_calculationContainer.setRemainingInvoiceChargeToSettle(
					p_invoiceLineMR
			);
			return ZERO_SETTLED;
		}
		if (
			isValueLesserOrEqual(
					p_invoiceLineMR
							.getChargeTotal(), p_calculationContainer
									.getAmountMRAvailableForSettlement()
			)
		)
		{
			generatePaymentResponse(
					p_invoiceLineMR, p_adjustmentContainer
							.getMilstarRetailCard(), p_adjustmentContainer
									.getPaymentMethodResponses().get(
											lRetailCard
													.getPaymentMethodId()
									), p_invoiceLineMR
											.getChargeTotal(),
					p_repoContainer

			);

			p_calculationContainer.setAmountMRAvailableForSettlement(
					p_calculationContainer.getAmountMRAvailableForSettlement()
							.subtract(p_invoiceLineMR.getChargeTotal())
			);
			lSettledAmount = p_invoiceLineMR
					.getChargeTotal();
		} else if (
			isValueLesserOrEqual(
					p_calculationContainer
							.getAmountMRAvailableForSettlement(), IS_SETTLED
			)
		)
		{
			p_calculationContainer.setRemainingInvoiceAmountToSettle(
					p_invoiceLineMR.getChargeTotal()
			);
			p_calculationContainer.setRemainingInvoiceChargeToSettle(
					p_invoiceLineMR
			);
			lSettledAmount = ZERO_SETTLED;
			p_calculationContainer.setAmountMRAvailableForSettlement(
					IS_SETTLED
			);

		} else if (
			isValueGreater(
					p_invoiceLineMR.getChargeTotal(), p_calculationContainer
							.getAmountMRAvailableForSettlement()
			)
		)
		{
			generatePaymentResponse(
					p_invoiceLineMR, p_adjustmentContainer
							.getMilstarRetailCard(), p_adjustmentContainer
									.getPaymentMethodResponses().get(
											lRetailCard
													.getPaymentMethodId()
									),
					p_calculationContainer.getAmountMRAvailableForSettlement(),
					p_repoContainer
			);

			p_calculationContainer.setRemainingInvoiceAmountToSettle(
					p_invoiceLineMR.getChargeTotal()
							.subtract(
									p_calculationContainer
											.getAmountMRAvailableForSettlement()
							)
			);
			p_calculationContainer.setRemainingInvoiceChargeToSettle(
					p_invoiceLineMR
			);
			lSettledAmount = p_calculationContainer
					.getAmountMRAvailableForSettlement();
			p_calculationContainer.setAmountMRAvailableForSettlement(
					IS_SETTLED
			);
		}
		return lSettledAmount;
	}

	// ------------------------------------------------------------------------
	/**
	 * Settled the line amount against MU card and carry forward the reamining
	 * amount and line information to global objects defined in class and return
	 * how much is settled
	 * 
	 * @param p_adjustmentContainer
	 * @param p_repoContainer
	 * @param p_calculationContainer
	 * @param p_invoiceLineMU
	 * @return BigDecimal
	 */
	protected BigDecimal settleAgainstMUCard(
			AdjustmentProcessingContainer p_adjustmentContainer,
			RepoContainer p_repoContainer,
			CalculationUtil p_calculationContainer,
			InvoiceChargeDetail p_invoiceLineMU
	)
	{
		BigDecimal lSettledAmount = ZERO_SETTLED;
		PaymentMethod lUniformCard = p_adjustmentContainer
				.getMilstarUniformCard();
		// return if MU card is already Settled
		if (p_adjustmentContainer.getMilstarUniformCard() == null) {
			p_calculationContainer.setRemainingInvoiceAmountToSettle(
					p_invoiceLineMU
							.getChargeTotal()
			);
			p_calculationContainer.setRemainingInvoiceChargeToSettle(
					p_invoiceLineMU
			);
			return ZERO_SETTLED;
		}
		if (
			isValueLesserOrEqual(
					p_invoiceLineMU.getChargeTotal(), p_calculationContainer
							.getAmountMUAvailableForSettlement()
			)
		)
		{
			generatePaymentResponse(
					p_invoiceLineMU, p_adjustmentContainer
							.getMilstarUniformCard(), p_adjustmentContainer
									.getPaymentMethodResponses().get(
											lUniformCard
													.getPaymentMethodId()
									), p_invoiceLineMU
											.getChargeTotal(),
					p_repoContainer
			);
			p_calculationContainer.setAmountMUAvailableForSettlement(
					p_calculationContainer.getAmountMUAvailableForSettlement()
							.subtract(p_invoiceLineMU.getChargeTotal())
			);
			lSettledAmount = p_invoiceLineMU
					.getChargeTotal();
		} else if (
			isValueGreater(
					p_invoiceLineMU.getChargeTotal(), p_calculationContainer
							.getAmountMUAvailableForSettlement()
			)
		)
		{
			generatePaymentResponse(
					p_invoiceLineMU, p_adjustmentContainer
							.getMilstarUniformCard(), p_adjustmentContainer
									.getPaymentMethodResponses().get(
											lUniformCard
													.getPaymentMethodId()
									),
					p_calculationContainer.getAmountMUAvailableForSettlement(),
					p_repoContainer
			);
			p_calculationContainer.setRemainingInvoiceAmountToSettle(
					p_invoiceLineMU.getChargeTotal()
							.subtract(
									p_calculationContainer
											.getAmountMUAvailableForSettlement()
							)
			);
			p_calculationContainer.setRemainingInvoiceChargeToSettle(
					p_invoiceLineMU
			);
			lSettledAmount = p_calculationContainer
					.getAmountMUAvailableForSettlement();
			p_calculationContainer.setAmountMUAvailableForSettlement(
					IS_SETTLED
			);
		}
		return lSettledAmount;
	}

	// ------------------------------------------------------------------------
	/**
	 * Set MR Amount which is available for settling current Invoice is (Total
	 * Settled - Already returned MR)
	 * 
	 * @param p_calculationContainer
	 * @param p_refundContainer
	 */
	@Override
	protected void calculatePreBucketMR(
			CalculationUtil p_calculationContainer,
			ProcessingContainer p_refundContainer
	)
	{
		// MU Amount which is available for refunding current Invoice is (Total
		// Settled - Returned MR)
		p_calculationContainer.setAmountMRAvailableForSettlement(
				p_refundContainer.getTotalPaymentRetailSettled()
						.subtract(p_refundContainer.getTotalMRReturnedAmount())
		);
	}

	// ------------------------------------------------------------------------
	/**
	 * Specific for Shipment invoice, and return current Settled amount
	 * attribute
	 * 
	 * @param l_currentPaymentMethod
	 * @return BigDecimal
	 */
	@Override
	protected BigDecimal getCardBalance(PaymentMethod l_currentPaymentMethod) {
		return l_currentPaymentMethod.getCurrentSettledAmount();
	}

	// -------------------------------------------------------------------------
	/**
	 * To set response in settlement tender
	 * 
	 * @param p_genericResponse
	 * @param p_InvoiceResponses
	 */
	@Override
	protected void setGenericResponseTender(
			EISEResponseGeneric p_genericResponse, List<
					InvoiceResponse> p_InvoiceResponses
	)
	{
		p_genericResponse.setAdjustmentTender(p_InvoiceResponses);
	}

	// ------------------------------------------------------------------------
	/**
	 * check if current item is Milstar uniform Item
	 * 
	 * @param p_invoiceCharge
	 * @return boolean
	 */
	protected boolean isLineUniformItem(
			InvoiceChargeDetail p_invoiceCharge
	)
	{
		if (
			p_invoiceCharge.getPlanNumber().equalsIgnoreCase(
					PLAN_MILITARY_UNIFORM
			)
		)

			return true;
		return false;
	}

	// ------------------------------------------------------------------------
	/**
	 * check if current item is Milstar Retail Item
	 * 
	 * @param p_invoiceCharge
	 * @return boolean
	 */
	protected boolean isLineRetailItem(
			InvoiceChargeDetail p_invoiceCharge
	)
	{
		if (
			p_invoiceCharge.getPlanNumber().equalsIgnoreCase(
					PLAN_MILITARY_RETAIL
			)
		)
			return true;
		return false;
	}

	/**
	 * To get next available Lowest CC on Order
	 * 
	 * @param p_settlementContainer
	 * @return PaymentMethod
	 */
	protected PaymentMethod
			getNextAvailablePayment(
					ProcessingContainer p_settlementContainer,
					String p_chargePlan
			)
	{
		return p_settlementContainer.getSettlementGCMethods().isEmpty()
				? (p_settlementContainer.getSettlementCCMethods().isEmpty()
						? (p_settlementContainer.getMilstarRetailCard() != null
								&& PLAN_MILITARY_RETAIL.equals(p_chargePlan)
										? p_settlementContainer
												.getMilstarRetailCard()
										: p_settlementContainer
												.getMilstarUniformCard())
						: getNextAvailableCC(p_settlementContainer))
				: getNextAvailableGC(p_settlementContainer);
	}

	// ------------------------------------------------------------------------
	// -------------------------------------------------------------------------
}
// -----------------------------------------------------------------------------
// END OF FILE
// -----------------------------------------------------------------------------