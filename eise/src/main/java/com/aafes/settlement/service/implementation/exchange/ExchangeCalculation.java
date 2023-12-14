/**
 * 
 */
package com.aafes.settlement.service.implementation.exchange;

import java.math.BigDecimal;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import com.aafes.settlement.core.model.ProcessingContainer;
import com.aafes.settlement.core.payment.PaymentMethod;
import com.aafes.settlement.core.response.EISEResponseGeneric;
import com.aafes.settlement.core.response.InvoiceResponse;
import com.aafes.settlement.repository.RepoContainer;
import com.aafes.settlement.service.implementation.BaseCalculation;
import com.aafes.settlement.util.CalculationUtil;

@Service
public class ExchangeCalculation
	extends
	BaseCalculation
{
	private static final Logger LOGGER = LogManager
			.getLogger(ExchangeCalculation.class);

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
			ProcessingContainer p_container,
			RepoContainer p_repoContainer,
			EISEResponseGeneric p_eiseResponseGeneric
	)
	{
		CalculationUtil l_calculationContainer = new CalculationUtil();
		if (p_container.isMilstarBucket()) {
			if (p_container.getMilstarUniformCard() != null)
				this.calculatePreBucketMU(l_calculationContainer, p_container);
			if (p_container.getMilstarRetailCard() != null)
				this.calculatePreBucketMR(l_calculationContainer, p_container);
		}
		settlementBusinessLogic(
				l_calculationContainer, p_container,
				p_repoContainer, p_eiseResponseGeneric
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
				p_refundContainer.getTotalPaymentUniformSettled().subtract(
						p_refundContainer.getTotalMUReturnedAmount()
				)
		);
	}

	// -------------------------------------------------------------------------
	/**
	 * Complete Settlement Invoice Business Logic against Lowest GC first
	 * followed by Highest Gift Card Includes Items which are pending for
	 * settlement after MU or MR
	 * 
	 * @param p_calculationContainer
	 * @param p_settlementContainer
	 * @param p_repoContainer
	 * @param p_eiseResponseGeneric
	 */
	@Override
	protected void settlementBusinessLogic(
			CalculationUtil p_calculationContainer,
			ProcessingContainer p_settlementContainer,
			RepoContainer p_repoContainer,
			EISEResponseGeneric p_eiseResponseGeneric) {

		LOGGER.debug(
				"The rule for Exchange is Lowest GC followed by highest and lastly Credit Card ."+
				"In case of Milstar Card attached, after reserving amount for pending shipment/refund"+
				"items for specific line (Retail or Uniform)");
		super.settlementBusinessLogic(
				p_calculationContainer,
				p_settlementContainer, p_repoContainer, p_eiseResponseGeneric);
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
				p_refundContainer.getTotalPaymentRetailSettled().subtract(
						p_refundContainer.getTotalMRReturnedAmount()
				)
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
	 * To set response in exchange tender
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
		p_genericResponse.setExchangeTender(p_InvoiceResponses);
	}
}
