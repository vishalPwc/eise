/**
 * 
 */
package com.aafes.settlement.util;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.aafes.settlement.core.response.EISEResponseGeneric;
import com.aafes.settlement.core.response.InvoiceLineResponse;
import com.aafes.settlement.core.response.InvoiceResponse;
import com.aafes.settlement.core.response.PaymentMethodResponse;

/**
 * UTIL class to check the duplicates in the response tender and combine them
 * into 1 single entry while summing up the payment amount.
 * 
 * FIX for ECOMM - 27929
 * 
 * @author
 *
 */
public class CheckResponseDuplicates {

	private static final Logger LOGGER = LogManager.getLogger(
			CheckResponseDuplicates.class
	);

	/**
	 * 
	 * @param pERG
	 */
	public static void settlementResponse(EISEResponseGeneric pERG) {
		LOGGER.info(
				"---- CheckResponseDuplicates :: For Settlement Request payment tender START----"
		);
		List<PaymentMethodResponse> lNewPaymentList = new ArrayList<>();

		for (InvoiceResponse lLoopedInvoicRes : pERG.getSettlementTender()) {
			mergeDuplicates(lLoopedInvoicRes, lNewPaymentList);
			lLoopedInvoicRes.getPaymentMethod().clear();
			lLoopedInvoicRes.setPaymentMethod(lNewPaymentList);
			// lIlr.clear();
		}
		LOGGER.info(
				"---- CheckResponseDuplicates :: For Settlement Request payment tender END----"
		);
	}

	/**
	 * 
	 * @param pERG
	 */
	public static void adjustmentResponse(EISEResponseGeneric pERG) {
		LOGGER.info(
				"---- CheckResponseDuplicates :: For Adjustment Request payment tender START----"
		);
		List<PaymentMethodResponse> lNewPaymentList = new ArrayList<>();

		for (InvoiceResponse lLoopedInvoicRes : pERG.getAdjustmentTender()) {
			mergeDuplicates(lLoopedInvoicRes, lNewPaymentList);
			lLoopedInvoicRes.getPaymentMethod().clear();
			lLoopedInvoicRes.setPaymentMethod(lNewPaymentList);
			// lIlr.clear();
		}
		LOGGER.info(
				"---- CheckResponseDuplicates :: For Adjustment Request payment tender END----"
		);
	}

	/**
	 * 
	 * @param pERG
	 */
	public static void cancellationResponse(EISEResponseGeneric pERG) {
		LOGGER.info(
				"---- CheckResponseDuplicates :: For Cancellation Request payment tender START----"
		);
		List<PaymentMethodResponse> lNewPaymentList = new ArrayList<>();

		for (InvoiceResponse lLoopedInvoicRes : pERG.getCancelledTender()) {
			mergeDuplicates(lLoopedInvoicRes, lNewPaymentList);
			lLoopedInvoicRes.getPaymentMethod().clear();
			lLoopedInvoicRes.setPaymentMethod(lNewPaymentList);
			// lIlr.clear();
		}
		LOGGER.info(
				"---- CheckResponseDuplicates :: For Cancellation Request payment tender END----"
		);
	}

	/**
	 * 
	 * @param pINR
	 * @param lNewPaymentList
	 */
	private static void mergeDuplicates(
			InvoiceResponse pINR, List<PaymentMethodResponse> lNewPaymentList
	)
	{
		LOGGER.info(
				"---- mergeDuplicates :: Start----"
		);
		for (PaymentMethodResponse lLoppedPaymentRes : pINR
				.getPaymentMethod())
		{
			List<InvoiceLineResponse> lNewInvoiceResList = new ArrayList<>();
			PaymentMethodResponse lNewPaymentRes = new PaymentMethodResponse();
			lNewPaymentRes.setPaymentMethodId(
					lLoppedPaymentRes.getPaymentMethodId()
			);
			lNewPaymentRes.setInvoiceCharge(
					lLoppedPaymentRes.getInvoiceCharge()
			);
			lLoppedPaymentRes.getInvoiceLine()
					.stream()
					.collect(
							Collectors
									.groupingBy(
											ilr -> new InvoiceLineResponse(
													ilr.getInvoiceId(),
													ilr
															.getInvoiceLineId()
											),
											Collectors.reducing(
													BigDecimal.ZERO,
													ilr -> ilr
															.getPaymentAmount(),
													BigDecimal::add
											)
									)
					).forEach((uniqueInvoiceResponse, v) ->
					{
						uniqueInvoiceResponse.setPaymentAmount(v);
						lNewInvoiceResList.add(uniqueInvoiceResponse);
					});
			lNewPaymentRes.setInvoiceLine(lNewInvoiceResList);
			lNewPaymentList.add(lNewPaymentRes);
			// System.out.println(upgradeInvoiceLines);
			// }

		}
		LOGGER.info(
				"---- mergeDuplicates :: END----"
		);
	}

}
