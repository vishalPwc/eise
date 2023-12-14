package com.aafes.settlement.service.implementation.reversal;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import com.aafes.settlement.constant.ErrorConstants;
import com.aafes.settlement.core.invoice.OpenAuthReversal;
import com.aafes.settlement.core.model.reversal.ReversalPaymentMethod;
import com.aafes.settlement.core.model.reversal.ReversalProcessingContainer;
import com.aafes.settlement.core.payment.PaymentTransaction;
import com.aafes.settlement.core.response.EISEResponseGeneric;
import com.aafes.settlement.core.response.InvoiceResponse;
import com.aafes.settlement.core.response.PaymentMethodResponse;
import com.aafes.settlement.exception.NodeNotFoundException;
import com.aafes.settlement.repository.RepoContainer;
import com.aafes.settlement.service.implementation.BaseCalculation;
import com.aafes.settlement.util.CalculationUtil;
import com.aafes.settlement.util.RepoUtils;
import com.aafes.settlement.util.Utils;
import com.aafes.settlement.validators.ValidationErrors;

/**
 * Reusing Settlement Calculation and overriding few of the methods to
 * Accommodate changes for refunds. e.g. Changing calculation to consider
 * attribute Current Settled Amount instead of current Auth
 * 
 */
@Service
public class ReversalCalculation
	extends
	BaseCalculation
{
	// ------------------------------------------------------------------------
	private static final Logger LOGGER = LogManager
			.getLogger(ReversalCalculation.class);

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
			ReversalProcessingContainer p_container,
			RepoContainer p_repoContainer,
			EISEResponseGeneric p_eiseResponseGeneric
	)
	{
		LOGGER.debug(
				"---- Auth Reversal Request: calculatePaymentTenders(): START---"
		);

		LOGGER.debug(
				"The rule for Auth Reversal is reverse on CC, followed by Highest GC."
						+ "In Case of Milstar Card, priority is (MU, MR, CC, Highest GC)"
		);

		CalculationUtil l_calculationContainer = new CalculationUtil();
		if (p_container.isMilstarBucket()) {
			LOGGER.debug(
					"---- Auth Reversal Request: calculatePaymentTenders(): "
							+ "Request Has Milstar Card---"
			);
			// if (
			// p_container.getMilstarUniformMethod() != null
			// && p_container
			// .getMilstarUniformMethod()
			// .getCurrentAuthAmount() > IS_FINISHED
			// )

			if (
				p_container.getMilstarUniformMethod() != null
						&& isValueGreater(
								p_container.getMilstarUniformMethod()
										.getCurrentAuthAmount(), IS_FINISHED
						)
			)
			{
				this.calculatePreBucketMU(l_calculationContainer, p_container);
				LOGGER.debug(
						"---- Auth Reversal Request:"
								+ "calculatePreBucketMU(): MU Available : ",
						l_calculationContainer
								.getAmountMUAvailableForSettlement()
				);
			}
			if (
				p_container.getMilstarRetailMethod() != null
						// && p_container.getMilstarRetailMethod()
						// .getCurrentAuthAmount() > IS_FINISHED) {
						&& isValueGreater(
								p_container.getMilstarRetailMethod()
										.getCurrentAuthAmount(), IS_FINISHED
						)
			)
			{
				this.calculatePreBucketMR(l_calculationContainer, p_container);
				LOGGER.debug(
						"---- Auth Reversal Request:"
								+ "calculatePreBucketMU(): MR Available : ",
						l_calculationContainer
								.getAmountMRAvailableForSettlement()
				);
			}
		}
		reversalBusinessLogic(
				l_calculationContainer, p_container,
				p_repoContainer, p_eiseResponseGeneric
		);
	}

	// ------------------------------------------------------------------------
	/**
	 * Set MU Amount which is available for Reversing current Invoice is (Total
	 * Settled - Already returned MU)
	 * 
	 * @param p_calculationContainer
	 * @param p_reversalContainer
	 */

	protected void calculatePreBucketMU(
			CalculationUtil p_calculationContainer,
			ReversalProcessingContainer p_reversalContainer
	)
	{
		BigDecimal preBucketMU = (p_reversalContainer.getTotalMUOrderedAmount()
				.subtract(p_reversalContainer.getTotalMUReturnedAmount()));
		// MU Amount which is available for settling current Invoice is (Total
		// Auth - pre bucket MU)
		/*
		 * p_calculationContainer.setAmountMUAvailableForSettlement(
		 * p_reversalContainer.getTotalPaymentUniformAuth() - preBucketMU );
		 */
		// set avaible auth after pre-bucket onto card
		p_reversalContainer.getMilstarUniformMethod()
				.setCurrentAuthAmount(
						p_reversalContainer
								.getTotalPaymentUniformAuth().subtract(
										preBucketMU
								)
				);
		// set the Uniform card into methods available to reverse current
		// invoice
		if (
			Utils.isValueGreater(
					p_reversalContainer.getMilstarUniformMethod()
							.getCurrentAuthAmount(), ZERO_VALUE
			)
		)
		{
			p_reversalContainer.getReversalPaymentMethods()
					.add(p_reversalContainer.getMilstarUniformMethod());
		}
	}

	// ------------------------------------------------------------------------
	/**
	 * Set MU Amount which is available for Reversing current Invoice is (Total
	 * Settled - Already returned MU)
	 * 
	 * @param p_calculationContainer
	 * @param p_reversalContainer
	 */
	@Deprecated
	protected void calculatePreBucketMU_deprecated(
			CalculationUtil p_calculationContainer,
			ReversalProcessingContainer p_reversalContainer
	)
	{

		if (
			Utils.isValueGreater(
					p_reversalContainer.getMilstarUniformMethod()
							.getCurrentAuthAmount(), ZERO_VALUE
			)
		)
		{
			p_reversalContainer.getReversalPaymentMethods()
					.add(p_reversalContainer.getMilstarUniformMethod());
		}
	}

	// -------------------------------------------------------------------------
	/**
	 * Complete Auth Reversal Invoice Business Logic against Highest CC first
	 * followed by Highest Gift Card
	 * 
	 * @param p_calculationContainer
	 * @param p_reversalContainer
	 * @param p_repoContainer
	 * @param p_eiseResponseGeneric
	 */
	protected void reversalBusinessLogic(
			CalculationUtil p_calculationContainer,
			ReversalProcessingContainer p_reversalContainer,
			RepoContainer p_repoContainer,
			EISEResponseGeneric p_eiseResponseGeneric
	)
	{
		LOGGER.info(
				"---- Auth Reversal Request: reversalBusinessLogic(): START---"
		);
		PaymentMethodResponse l_paymentResponse = new PaymentMethodResponse();
		List<InvoiceResponse> l_authReversalTenderResponse = new ArrayList<
				InvoiceResponse>();
		InvoiceResponse l_authReversedResponse = new InvoiceResponse();
		BigDecimal l_totalInvoiceToSettle = p_reversalContainer
				.getOpenInvoiceLineTotal();
		ReversalPaymentMethod l_HighestCard = (ReversalPaymentMethod) p_reversalContainer
				.getReversalPaymentMethods().poll();

		if (l_HighestCard == null) {
			LOGGER.error(
					"No amount in any payment method available after pre-bucketing"
			);

			ValidationErrors l_errors = new ValidationErrors();

			l_errors.errors.put(
					ErrorConstants.AUTHREVERSAL_INVOICE_MORE_THAN_AUTH,
					"No amount in any payment method available after pre-bucketing"
			);

			throw new NodeNotFoundException(l_errors);
		}
		BigDecimal l_currentCardBalance = getCardBalance(l_HighestCard);

		UNTIL_ALL_INVOICE_REVERSED:

		while (isValueGreater(l_totalInvoiceToSettle, IS_SETTLED)) {
			// while (l_totalInvoiceToSettle > IS_SETTLED) {
			if (l_currentCardBalance == IS_FINISHED) {
				/**
				 * Get New Payment Method & Balance for Settling Invoice Line.
				 * If all GC are Finished, will take CC
				 */
				l_HighestCard = (ReversalPaymentMethod) p_reversalContainer
						.getReversalPaymentMethods().poll();
				if (l_HighestCard != null) {
					l_currentCardBalance = getCardBalance(l_HighestCard);

					LOGGER.debug(
							"Now moving to next payment method " + l_HighestCard
									.getPaymentMethodId()
									+ "to settle the remaining amount"
					);

				} else {
					l_currentCardBalance = new BigDecimal(0.00);
				}

				// getPaymentMethodResponses().add(lPaymentResponse);
				l_paymentResponse = null;
				l_paymentResponse = new PaymentMethodResponse();

			}
			/**
			 * do this when there is some balance of invoice line needs to be
			 * settled against second payment method
			 */
			// if (p_calculationContainer
			// .getRemainingInvoiceAmountToSettle() > IS_SETTLED) {
			if (
				isValueGreater(
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
									.getRemainingOpenAuthToReverse(),
							l_HighestCard, l_paymentResponse, p_repoContainer
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
					p_calculationContainer.getPaymentMethodResponses()
							.add(l_paymentResponse);
					// load new payment into response
					l_paymentResponse = null;
					l_paymentResponse = new PaymentMethodResponse();
					// if not available get next GC or CC
					continue UNTIL_ALL_INVOICE_REVERSED;
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

			ITERATE_OPEN_AUTH_REQUESTS: while (
				!p_reversalContainer
						.getOpenAuthLines().isEmpty()
			)
			{
				OpenAuthReversal l_currentAuthReversal = p_reversalContainer
						.getOpenAuthLines().poll();
				BigDecimal l_currentLineAmount = l_currentAuthReversal
						.getAmount();
				LOGGER.debug(
						"Now we are doing calculation for PaymentTransactionId : "
								+ l_currentAuthReversal.getPaymentTransId()
								+ " for $" + l_currentLineAmount
				);

				// check availability of line amount on payment method
				if (
					checkAuthAvailability(
							l_currentCardBalance,
							l_currentLineAmount, l_currentAuthReversal,
							l_HighestCard, l_paymentResponse, p_repoContainer
					)
				)
				{
					p_calculationContainer.setRemainingInvoiceAmountToSettle(
							l_currentLineAmount.subtract(l_currentCardBalance)
					);
					// l_totalInvoiceToSettle -= l_currentCardBalance;
					l_totalInvoiceToSettle = l_totalInvoiceToSettle
							.subtract(l_currentCardBalance);

					l_currentCardBalance = IS_FINISHED;
					p_calculationContainer.setRemainingOpenAuthToReverse(
							l_currentAuthReversal
					);
					p_calculationContainer.getPaymentMethodResponses()
							.add(l_paymentResponse);
					// load new payment into response
					l_paymentResponse = null;
					l_paymentResponse = new PaymentMethodResponse();
					// if not available get next GC or CC
					LOGGER.debug(
							"More $ " + p_calculationContainer
									.getRemainingInvoiceAmountToSettle()
									+ " remaining to be settled for "
									+ l_currentAuthReversal.getPaymentTransId()
					);
					break ITERATE_OPEN_AUTH_REQUESTS;
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

		if (!Utils.isNull(l_paymentResponse.getPaymentMethodId())) {
			p_calculationContainer.getPaymentMethodResponses()
					.add(l_paymentResponse);
		}
		l_authReversedResponse.setPaymentMethod(
				p_calculationContainer.getPaymentMethodResponses()
		);
		l_authReversalTenderResponse.add(l_authReversedResponse);
		setGenericResponseTender(
				p_eiseResponseGeneric,
				l_authReversalTenderResponse
		);
		LOGGER.info(
				"---- Auth Reversal Request: reversalBusinessLogic(): END---"
		);
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
	 * @param pOpenAuthReversal
	 * @param pHighestCard
	 * @param p_paymentResponse
	 * @param p_repoContainer
	 * @return boolean
	 */
	protected boolean checkAuthAvailability(
			BigDecimal pCurrentGCBalance,
			BigDecimal pCurrentLineAmount,
			OpenAuthReversal pOpenAuthReversal,
			ReversalPaymentMethod pHighestCard,
			PaymentMethodResponse p_paymentResponse,
			RepoContainer p_repoContainer
	)
	{
		boolean lSkipTONextPayment = false;
		// if (pCurrentLineAmount <= pCurrentGCBalance) {
		if (isValueLesserOrEqual(pCurrentLineAmount, pCurrentGCBalance)) {

			LOGGER.debug(
					"Amount to be calculated :  $" + pCurrentLineAmount
							+ " is less than or equal to balance amount $"
							+ pCurrentGCBalance + " in " + pHighestCard
									.getPaymentMethodId()
			);

			// take amount from GC and subtract from GC remaining
			generatePaymentResponse(
					pOpenAuthReversal, pHighestCard,
					p_paymentResponse, pCurrentLineAmount, p_repoContainer
			);
			// s} else if (pCurrentLineAmount > pCurrentGCBalance) {
		} else if (isValueGreater(pCurrentLineAmount, pCurrentGCBalance)) {
			// take whole PaymentMethod balance and get next GC
			generatePaymentResponse(
					pOpenAuthReversal, pHighestCard,
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
	 * Overload for Invoice Charge Type
	 * 
	 * @param pAuthReversal
	 * @param p_currentCard
	 * @param p_paymentResponse
	 * @param pSettledAmount
	 * @param p_repoContainer
	 */
	protected void generatePaymentResponse(
			OpenAuthReversal pAuthReversal,
			ReversalPaymentMethod p_currentCard,
			PaymentMethodResponse p_paymentResponse,
			BigDecimal pSettledAmount,
			RepoContainer p_repoContainer
	)
	{
		PaymentTransaction l_paymentTransaction = new PaymentTransaction();
		l_paymentTransaction.setPaymentAmount(pSettledAmount);
		l_paymentTransaction
				.setPaymentTransactionId(pAuthReversal.getPaymentTransId());
		p_paymentResponse
				.setPaymentMethodId(p_currentCard.getPaymentMethodId());
		p_paymentResponse.getPaymentTransaction().add(l_paymentTransaction);
		LOGGER.debug(
				"---- Auth Reversal generatePaymentResponse: reversalBusinessLogic():: authId: ",
				pAuthReversal.getPaymentTransId()
		);

		LOGGER.debug(
				pAuthReversal.getPaymentTransId()
						+ " is calculated against $" + p_currentCard
								.getPaymentMethodId() + " for $"
						+ pSettledAmount
		);

		RepoUtils.addToInvoiceItemDetailsList(
				pAuthReversal,
				p_currentCard,
				pSettledAmount,
				p_repoContainer
		);
	}

	// -----------------------------------------------------------------------------------
	/**
	 * Set MR Amount which is available for settling current Invoice is (Total
	 * Settled - Already returned MR)
	 * 
	 * @param p_calculationContainer
	 * @param p_reversalContainer
	 */
	protected void calculatePreBucketMR(
			CalculationUtil p_calculationContainer,
			ReversalProcessingContainer p_reversalContainer
	)
	{
		BigDecimal preBucketMR = (p_reversalContainer.getTotalMROrderedAmount()
				.subtract(p_reversalContainer.getTotalMRReturnedAmount()));
		// MR Amount which is available for settling current Invoice is Total
		// set avaible auth after pre-bucket onto card
		p_reversalContainer.getMilstarRetailMethod()
				.setCurrentAuthAmount(
						p_reversalContainer
								.getTotalPaymentRetailAuth().subtract(
										preBucketMR
								)
				);
		// set the Uniform card into methods available to reverse current
		// invoice
		if (
			Utils.isValueGreater(
					p_reversalContainer.getMilstarRetailMethod()
							.getCurrentAuthAmount(), ZERO_VALUE
			)
		)
		{
			p_reversalContainer.getReversalPaymentMethods()
					.add(p_reversalContainer.getMilstarRetailMethod());
		}
	}

	// -----------------------------------------------------------------------------------
	/**
	 * Set MR Amount which is available for settling current Invoice is (Total
	 * Settled - Already returned MR)
	 * 
	 * @param p_calculationContainer
	 * @param p_reversalContainer
	 */
	@Deprecated
	protected void calculatePreBucketMR_deprecated(
			CalculationUtil p_calculationContainer,
			ReversalProcessingContainer p_reversalContainer
	)
	{

		if (
			Utils.isValueGreater(
					p_reversalContainer.getMilstarRetailMethod()
							.getCurrentAuthAmount(), ZERO_VALUE
			)
		)
		{
			p_reversalContainer.getReversalPaymentMethods()
					.add(p_reversalContainer.getMilstarRetailMethod());
		}
	}

	// ------------------------------------------------------------------------
	/**
	 * Specific for Shipment invoice, and return current Settled amount
	 * attribute
	 * 
	 * @param l_currentPaymentMethod
	 * @return BigDecimal
	 */
	protected BigDecimal
			getCardBalance(ReversalPaymentMethod l_currentPaymentMethod)
	{
		return l_currentPaymentMethod.getCurrentAuthAmount();
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
			EISEResponseGeneric p_genericResponse,
			List<InvoiceResponse> p_InvoiceResponses
	)
	{
		p_genericResponse.setAuthReversalTender(p_InvoiceResponses);
	}
}
// -----------------------------------------------------------------------------
// END OF FILE
// -----------------------------------------------------------------------------