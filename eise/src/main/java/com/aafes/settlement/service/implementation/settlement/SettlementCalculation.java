package com.aafes.settlement.service.implementation.settlement;

import java.math.BigDecimal;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import com.aafes.settlement.constant.Constants;
import com.aafes.settlement.core.model.ProcessingContainer;
import com.aafes.settlement.core.payment.PaymentMethod;
import com.aafes.settlement.core.response.EISEResponseGeneric;
import com.aafes.settlement.repository.RepoContainer;
import com.aafes.settlement.service.implementation.BaseCalculation;
import com.aafes.settlement.util.CalculationUtil;

@Service
public class SettlementCalculation
	extends
	BaseCalculation
	implements
	Constants
{

	private static final Logger LOGGER = LogManager
			.getLogger(SettlementCalculation.class);

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
		LOGGER.debug(
				"---- Shippment Request: calculatePaymentTenders(): START---"
		);
		CalculationUtil l_calculationContainer = new CalculationUtil();
		if (p_container.isMilstarBucket()) {
			LOGGER.debug(
					"---- Shippment Request: calculatePaymentTenders(): "
							+ "Request Has Milstar Card---"
			);
			if (p_container.getMilstarUniformCard() != null) {
				super.calculatePreBucketMU(l_calculationContainer, p_container);
				LOGGER.debug(
						"---- Shippment Request:"
								+ "calculatePreBucketMU(): MU Available : ",
						l_calculationContainer
								.getAmountMUAvailableForSettlement()
				);
			}
			if (p_container.getMilstarRetailCard() != null) {
				super.calculatePreBucketMR(l_calculationContainer, p_container);
				LOGGER.debug(
						"---- Shippment Request:"
								+ "calculatePreBucketMU(): MR Available : ",
						l_calculationContainer
								.getAmountMRAvailableForSettlement()
				);
			}
		}
		settlementBusinessLogic(
				l_calculationContainer, p_container,
				p_repoContainer, p_eiseResponseGeneric
		);
		LOGGER.debug(
				"---- Shippment Request: calculatePaymentTenders(): END---"
		);
	}

	// ------------------------------------------------------------------------

	/**
	 * Specific for Shipment invoice, and return current auth amount attribute
	 * 
	 * @param l_currentPaymentMethod
	 * @return BigDecimal
	 */
	@Override
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
	@Override
	protected void settlementBusinessLogic(
			CalculationUtil p_calculationContainer,
			ProcessingContainer p_settlementContainer,
			RepoContainer p_repoContainer,
			EISEResponseGeneric p_eiseResponseGeneric
	)
	{
		LOGGER.info(
				"---- Shippment Request: settlementBusinessLogic(): START---"
		);
		LOGGER.debug(
				"The rule for settlement is Lowest GC followed by highest and lastly Credit Card . In case of Milstar Card attached, after reserving amount for pending shipment/refund items for specific line (Retail or Uniform)"
		);
		super.settlementBusinessLogic(
				p_calculationContainer,
				p_settlementContainer, p_repoContainer, p_eiseResponseGeneric
		);
		LOGGER.info(
				"---- Shippment Request: settlementBusinessLogic(): END---"
		);
		// repo call
	}

	// -------------------------------------------------------------------------
}
// -----------------------------------------------------------------------------
// END OF FILE
// -----------------------------------------------------------------------------