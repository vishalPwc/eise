package com.aafes.settlement.util;

// ----------------------------------------------------------------------------
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.transaction.annotation.Transactional;

import com.aafes.settlement.constant.Constants;
import com.aafes.settlement.core.invoice.InvoiceChargeDetail;
import com.aafes.settlement.core.invoice.InvoiceLine;
import com.aafes.settlement.core.invoice.OpenAuthReversal;
import com.aafes.settlement.core.invoice.OpenInvoice;
import com.aafes.settlement.core.model.adjustment.AdjustmentOpenInvoice;
import com.aafes.settlement.core.model.reversal.ReversalPaymentMethod;
import com.aafes.settlement.core.payment.PaymentMethod;
import com.aafes.settlement.repository.OrderInvoiceDetailsRepo;
import com.aafes.settlement.repository.RepoContainer;
import com.aafes.settlement.repository.entity.InvoiceChargeDetails;
import com.aafes.settlement.repository.entity.InvoiceItemDetails;
import com.aafes.settlement.repository.entity.InvoicePaymentMethods;
import com.aafes.settlement.repository.entity.OrderInvoiceDetails;

// ----------------------------------------------------------------------------
public class RepoUtils {
	// ------------------------------------------------------------------------
	private static final Logger LOGGER = LogManager.getLogger(RepoUtils.class);

	// ------------------------------------------------------------------------
	/**
	 * This method is saves data in all the tables
	 * 
	 * @param p_repoContainer
	 * @param p_orderInvoiceDetailsRepo
	 */
	@Transactional(rollbackFor = Exception.class)
	public static void saveData(
			RepoContainer p_repoContainer,
			OrderInvoiceDetailsRepo p_orderInvoiceDetailsRepo
	)
	{
		try {
			List<OrderInvoiceDetails> l_orderInvoiceDetails = p_repoContainer
					.getOrderInvoiceData();
			enrichItemDetailsData(p_repoContainer);
			if (
				!p_repoContainer.getTransactionType()
						.equals(Constants.AUTH_REVERSAL_INVOICE)
			)
			{
				enrichChargeDetailsData(p_repoContainer);
			}
			p_orderInvoiceDetailsRepo.saveAll(l_orderInvoiceDetails);
			LOGGER.info(
					"---- Added data to ORDER_INVOICE_DETAILS table ----"
			);
		} catch (Exception ex) {
			LOGGER.error(ex.getMessage());
			LOGGER.info(
					"---- Failed to Add data to ORDER_INVOICE_DETAILS table ----"
			);
		}
	}

	// ------------------------------------------------------------------------
	public static void enrichItemDetailsData(
			RepoContainer p_repoContainer
	)
	{
		List<InvoicePaymentMethods> l_invoicePaymentMethods = p_repoContainer
				.getInvoicePaymentMethodsList();

		for (InvoicePaymentMethods l_invoicePaymentMethod : l_invoicePaymentMethods) {

			// For each PaymentMethod find the matching Settled invoice lines
			// Update the invoice line list in the PaymentMethod
			List<InvoiceItemDetails> l_invoiceItemDetailsFilteredList = p_repoContainer
					.getInvoiceItemDetailsList()
					.stream().filter(
							l_invoiceDetails -> l_invoiceDetails
									.getPaymentMethodId() == l_invoicePaymentMethod
											.getPaymentMethodId()
					)
					.collect(Collectors.toList());

			// Update the PaymentMethod reference in each invoiceLine
			for (InvoiceItemDetails l_invoiceItemDetails : l_invoiceItemDetailsFilteredList) {
				l_invoiceItemDetails.setInvoicePaymentMethod(
						l_invoicePaymentMethod
				);
			}

			l_invoicePaymentMethod.setInvoiceItemDetailsList(
					l_invoiceItemDetailsFilteredList
			);

		}
	}

	// ------------------------------------------------------------------------
	/**
	 * This method stores data in the container for INVOICE_ITEM_SETTLEMENT
	 * table. *
	 * 
	 * @param l_currentMethod
	 * @param p_repoBucket
	 */
	public static void addToInvoiceItemSettlementList(
			PaymentMethod l_currentMethod,
			RepoContainer p_repoBucket
	)
	{
		String l_payId = l_currentMethod.getPaymentMethodId();
		String l_payType = l_currentMethod.getPaymentType().getPaymentTypeId();
		String l_cardType = l_currentMethod.getCardType().getCardTypeId();
		String l_responsePlan = l_currentMethod.getExtended().getResponsePlan();
		String l_currentSettled = l_currentMethod.getCurrentSettledAmount()
				.toString();
		String l_currentAuth = l_currentMethod.getCurrentAuthAmount()
				.toString();
		if (
			(l_currentMethod.getExtended() != null
					&& l_currentMethod.getExtended().isNewRefundGiftCard())
		)
		{
			LOGGER.debug(
					"Skipped PaymentMethod: "
							+ l_currentMethod.getPaymentMethodId()
							+ " from adding to ProcessingContainer InvoiceItemSettlementList "
			);
		} else {
			InvoicePaymentMethods l_invoicePaymentMethodsDao = new InvoicePaymentMethods();
			l_invoicePaymentMethodsDao.setPaymentMethodId(l_payId);
			l_invoicePaymentMethodsDao.setPaymentMethodType(l_payType);
			l_invoicePaymentMethodsDao.setCardType(l_cardType);
			l_invoicePaymentMethodsDao.setResponsePlan(l_responsePlan);
			l_invoicePaymentMethodsDao.setCurrentSettled(
					new BigDecimal(l_currentSettled)
			);
			l_invoicePaymentMethodsDao.setCurrentAuth(
					new BigDecimal(l_currentAuth)
			);
			p_repoBucket.getInvoicePaymentMethodsList().add(
					l_invoicePaymentMethodsDao
			);
			LOGGER.debug(
					"Added PaymentMethod: "
							+ l_currentMethod.getPaymentMethodId()
							+ " to ProcessingContainer InvoiceItemSettlementList "
			);
		}
	}

	// ----------------------------------------------------------------------------
	/**
	 * This method store data in the container for ORDER_INVOICE_DETAILS table.
	 * 
	 * @param p_openInvoice
	 * @param p_repoContainer
	 */
	public static void addToOrderInvoiceData(
			OpenInvoice p_openInvoice,
			RepoContainer p_repoContainer
	)
	{
		OrderInvoiceDetails l_orderInvoiceDetails = new OrderInvoiceDetails();
		List<InvoicePaymentMethods> invoicePaymentMethodsList = p_repoContainer
				.getInvoicePaymentMethodsList();
		l_orderInvoiceDetails.setOrderId(p_openInvoice.getOrderId());
		l_orderInvoiceDetails.setInvoiceId(
				p_openInvoice.getInvoiceId()
		);
		l_orderInvoiceDetails.setAmount(
				p_openInvoice.getInvoiceTotal().abs()
		);
		// ToDo
		// Change getCurrentDate to a Date from p_openInvoice
		// l_orderInvoiceDetails.setOrderDate(Utils.getCurrentDate());
		l_orderInvoiceDetails.setOrderDate(Utils.getCDTDate()); // CST/CDT date
		l_orderInvoiceDetails.setCreatedDate(Utils.getCurrentDate()); // UTC
																	  // date
		l_orderInvoiceDetails.setCreatedBy(Constants.EISE);
		l_orderInvoiceDetails.setTranType(
				p_repoContainer.getTransactionType()
		);
		l_orderInvoiceDetails.setAuditUUID(p_repoContainer.getAuditUUID());
		for (InvoicePaymentMethods invoicePaymentMethodsDao : invoicePaymentMethodsList) {
			invoicePaymentMethodsDao.setInvoiceId(p_openInvoice.getInvoiceId());
			invoicePaymentMethodsDao.setOrderId(p_openInvoice.getOrderId());
			invoicePaymentMethodsDao.setOrderInvoiceDetails(
					l_orderInvoiceDetails
			);
		}
		l_orderInvoiceDetails.setInvoicePaymentMethodsList(
				invoicePaymentMethodsList
		);

		p_repoContainer.getOrderInvoiceData().add(
				l_orderInvoiceDetails
		);

	}

	// ------------------------------------------------------------------------
	/**
	 * 
	 * @param p_invoiceCharge
	 * @param p_currentCard
	 * @param p_settledAmount
	 * @param p_repoContainer
	 */
	public static void addToInvoiceChargeDetailsList(
			InvoiceChargeDetail p_invoiceCharge,
			PaymentMethod p_currentCard,
			BigDecimal p_settledAmount,
			RepoContainer p_repoContainer
	)
	{
		InvoiceChargeDetails l_invoiceChargeDetailsDao = new InvoiceChargeDetails();
		l_invoiceChargeDetailsDao.setChargeDetailsId(
				p_invoiceCharge.getChargeDetailId()
		);
		l_invoiceChargeDetailsDao.setChargeType(
				p_invoiceCharge.getChargeType().getChargeTypeId()
		);
		l_invoiceChargeDetailsDao.setPaymentAmount(p_settledAmount);
		l_invoiceChargeDetailsDao.setPaymentMethodId(
				p_currentCard.getPaymentMethodId()
		);
		p_repoContainer.getInvoiceChargeDetailsList().add(
				l_invoiceChargeDetailsDao
		);
	}

	// ------------------------------------------------------------------------
	public static void addToInvoiceItemDetailsList(
			InvoiceLine pInvoiceLine,
			PaymentMethod p_currentCard,
			BigDecimal pSettledAmount,
			RepoContainer p_repoContainer
	)
	{
		InvoiceItemDetails l_invoiceItemDetailsDao = new InvoiceItemDetails();
		l_invoiceItemDetailsDao.setTranType(
				p_repoContainer.getTransactionType()
		);
		l_invoiceItemDetailsDao.setInvoiceId(pInvoiceLine.getInvoiceId());
		l_invoiceItemDetailsDao.setInvoiceLineId(
				pInvoiceLine.getInvoiceLineId()
		);
		l_invoiceItemDetailsDao.setInvoiceItemId(pInvoiceLine.getItemId());
		l_invoiceItemDetailsDao.setPaymentAmount(pSettledAmount);
		l_invoiceItemDetailsDao.setResponsePlan(
				pInvoiceLine.getExtended() != null ? pInvoiceLine.getExtended()
						.getResponsePlan() : ""
		);
		l_invoiceItemDetailsDao.setPromoPlan(
				pInvoiceLine.getExtended() != null ? pInvoiceLine.getExtended()
						.getPromoPlan() : ""
		);
		l_invoiceItemDetailsDao.setQuantity(pInvoiceLine.getQuantity());
		l_invoiceItemDetailsDao.setPaymentMethodId(
				p_currentCard.getPaymentMethodId()
		);
		p_repoContainer.getInvoiceItemDetailsList().add(
				l_invoiceItemDetailsDao
		);

	}

	// ------------------------------------------------------------------------
	/**
	 * @param p_repoContainer
	 */

	// Repository change
	// public static void enrichItemsData(
	// ProcessingContainer p_settlementContainer
	// )
	// {
	// List<InvoicePaymentMethods> l_invoicePaymentMethods =
	// p_settlementContainer
	// .getInvoicePaymentMethodsList();
	//
	// for (InvoicePaymentMethods l_InvoicePaymentDao : l_invoicePaymentMethods)
	// {
	//
	// // For each PaymentMethod find the matching Settled invoice lines
	// // Update the invoice line list in the paymentmethod
	// List<InvoiceItemDetails> l_invoiceItemDetailsFilteredList =
	// p_settlementContainer
	// .getInvoiceItemDetailsList()
	// .stream().filter(
	// l_invoiceDetails -> l_invoiceDetails
	// .getPaymentMethodId() == l_InvoicePaymentDao
	// .getPaymentMethodId()
	// )
	// .collect(Collectors.toList());
	//
	// // For each PaymentMethod find the matching Settled invoice charges
	// // Update the invoice charge list in the paymentmethod
	// List<InvoiceChargeDetails> l_invoiceChargeDetailsFilteredList =
	// p_settlementContainer
	// .getInvoiceChargeDetailsList()
	// .stream().filter(
	// l_invoiceDetails -> l_invoiceDetails
	// .getPaymentMethodId() == l_InvoicePaymentDao
	// .getPaymentMethodId()
	// )
	// .collect(Collectors.toList());
	//
	// // Update the PaymentMethod reference in each invoiceLine
	// for (InvoiceItemDetails l_invoiceItemDetails :
	// l_invoiceItemDetailsFilteredList) {
	// l_invoiceItemDetails.setInvoicePaymentMethod(
	// l_InvoicePaymentDao
	// );
	// }
	//
	// l_InvoicePaymentDao.setInvoiceItemDetailsList(
	// l_invoiceItemDetailsFilteredList
	// );
	//
	// // Update the PaymentMethod reference in each invoiceCharge
	// for (InvoiceChargeDetails l_invoiceChargeDetails :
	// l_invoiceChargeDetailsFilteredList) {
	// l_invoiceChargeDetails.setInvoicePaymentMethod(
	// l_InvoicePaymentDao
	// );
	// }
	//
	// l_InvoicePaymentDao.setInvoiceChargeDetailsList(
	// l_invoiceChargeDetailsFilteredList
	// );
	// }
	// }

	// Repository change
	public static void enrichChargeDetailsData(
			RepoContainer p_repoContainer
	)
	{
		List<InvoicePaymentMethods> l_invoicePaymentMethods = p_repoContainer
				.getInvoicePaymentMethodsList();

		for (InvoicePaymentMethods l_InvoicePaymentDao : l_invoicePaymentMethods) {

			// For each PaymentMethod find the matching Settled invoice charges
			// Update the invoice charge list in the paymentmethod
			List<InvoiceChargeDetails> l_invoiceChargeDetailsFilteredList = p_repoContainer
					.getInvoiceChargeDetailsList()
					.stream().filter(
							l_invoiceDetails -> l_invoiceDetails
									.getPaymentMethodId() == l_InvoicePaymentDao
											.getPaymentMethodId()
					)
					.collect(Collectors.toList());

			// Update the PaymentMethod reference in each invoiceCharge
			for (InvoiceChargeDetails l_invoiceChargeDetails : l_invoiceChargeDetailsFilteredList) {
				l_invoiceChargeDetails.setInvoicePaymentMethod(
						l_InvoicePaymentDao
				);
			}

			l_InvoicePaymentDao.setInvoiceChargeDetailsList(
					l_invoiceChargeDetailsFilteredList
			);
		}
	}

	// -----AuthReversal

	// ----------------------------------------------------------------------------

	// Repository change
	/**
	 * This method store data in the container for INVOICE_ITEM_SETTLEMENT
	 * table. *
	 * 
	 * @param l_currentMethod
	 * @param p_settlementBucket
	 */
	public static void addToInvoiceItemSettlementList(
			ReversalPaymentMethod l_currentMethod,
			RepoContainer p_container
	)
	{
		String l_payId = l_currentMethod.getPaymentMethodId();
		String l_payType = l_currentMethod.getPaymentType().getPaymentTypeId();
		String l_cardType = l_currentMethod.getCardType().getCardTypeId();
		String l_responsePlan = l_currentMethod.getExtended().getResponsePlan();
		String l_currentSettled = l_currentMethod.getCurrentSettledAmount()
				.toString();
		String l_currentAuth = l_currentMethod.getCurrentAuthAmount()
				.toString();
		if (
			(l_currentMethod.getExtended() != null
					&& l_currentMethod.getExtended().isNewRefundGiftCard())
		)
		{
			LOGGER.debug(
					"Skipped PaymentMethod: "
							+ l_currentMethod.getPaymentMethodId()
							+ " from adding to ProcessingContainer InvoiceItemSettlementList "
			);
		} else {
			InvoicePaymentMethods l_invoicePaymentMethodsDao = new InvoicePaymentMethods();
			l_invoicePaymentMethodsDao.setPaymentMethodId(l_payId);
			l_invoicePaymentMethodsDao.setPaymentMethodType(l_payType);
			l_invoicePaymentMethodsDao.setCardType(l_cardType);
			l_invoicePaymentMethodsDao.setResponsePlan(l_responsePlan);
			l_invoicePaymentMethodsDao.setCurrentSettled(
					new BigDecimal(l_currentSettled)
			);
			l_invoicePaymentMethodsDao.setCurrentAuth(
					new BigDecimal(l_currentAuth)
			);
			p_container.getInvoicePaymentMethodsList().add(
					l_invoicePaymentMethodsDao
			);
			LOGGER.debug(
					"Added PaymentMethod: "
							+ l_currentMethod.getPaymentMethodId()
							+ " to ProcessingContainer InvoiceItemSettlementList "
			);
		}
	}

	// Repository change
	/**
	 * This method store data in the container for ORDER_INVOICE_DETAILS table.
	 * 
	 * @param p_openInvoice
	 * @param p_settlementBucket
	 */
	public static void addToOrderInvoiceData(
			OpenAuthReversal p_openInvoice,
			RepoContainer p_container,
			String p_orderId
	)
	{
		// TODO create separate InvoiceItemSettlement instance for each
		// OrderInvoiceDetails instance
		OrderInvoiceDetails l_orderInvoiceDetails = new OrderInvoiceDetails();
		List<InvoicePaymentMethods> invoicePaymentMethodsList = p_container
				.getInvoicePaymentMethodsList();
		l_orderInvoiceDetails.setOrderId(p_orderId);
		l_orderInvoiceDetails.setInvoiceId(
				p_openInvoice.getPaymentTransId()
		);
		l_orderInvoiceDetails.setAmount(
				p_openInvoice.getAmount().abs()
		);
		// Change getCurrentDate to a Date from p_openInvoice
		l_orderInvoiceDetails.setOrderDate(Utils.getCDTDate());
		l_orderInvoiceDetails.setCreatedDate(Utils.getCurrentDate());
		l_orderInvoiceDetails.setCreatedBy("eISE");
		l_orderInvoiceDetails.setTranType(
				p_container.getTransactionType()
		);
		l_orderInvoiceDetails.setAuditUUID(p_container.getAuditUUID());
		for (InvoicePaymentMethods invoicePaymentMethodsDao : invoicePaymentMethodsList) {
			// invoiceItemSettlementDao.setOrderId(p_openInvoice.getOrderId());
			invoicePaymentMethodsDao.setInvoiceId(
					p_openInvoice.getPaymentTransId()
			);
			invoicePaymentMethodsDao.setOrderId(p_orderId);
			invoicePaymentMethodsDao.setOrderInvoiceDetails(
					l_orderInvoiceDetails
			);
		}
		l_orderInvoiceDetails.setInvoicePaymentMethodsList(
				invoicePaymentMethodsList
		);

		p_container.getOrderInvoiceData().add(
				l_orderInvoiceDetails
		);

	}

	// // Repository change
	// public static void addToInvoiceChargeDetailsList(
	// InvoiceChargeDetail pInvoiceCharge,
	// ReversalPaymentMethod p_currentCard,
	// BigDecimal pSettledAmount,
	// ProcessingContainer p_container
	// )
	// {
	// InvoiceChargeDetails l_invoiceChargeDetailsDao = new
	// InvoiceChargeDetails();
	// l_invoiceChargeDetailsDao.setChargeDetailsId(
	// pInvoiceCharge.getChargeDetailId()
	// );
	// l_invoiceChargeDetailsDao.setChargeType(
	// pInvoiceCharge.getChargeType().getChargeTypeId()
	// );
	// l_invoiceChargeDetailsDao.setPaymentAmount(pSettledAmount);
	// l_invoiceChargeDetailsDao.setPaymentMethodId(
	// p_currentCard.getPaymentMethodId()
	// );
	// p_container.getInvoiceChargeDetailsList().add(
	// l_invoiceChargeDetailsDao
	// );
	// }

	// Repository change
	public static void addToInvoiceItemDetailsList(
			OpenAuthReversal pInvoiceLine,
			ReversalPaymentMethod p_currentCard,
			BigDecimal pSettledAmount,
			RepoContainer p_container
	)
	{
		InvoiceItemDetails l_invoiceItemDetailsDao = new InvoiceItemDetails();
		l_invoiceItemDetailsDao.setTranType(
				p_container.getTransactionType()
		);
		l_invoiceItemDetailsDao.setInvoiceId(pInvoiceLine.getPaymentTransId());
		// l_invoiceItemDetailsDao.setInvoiceLineId(
		// pInvoiceLine.getInvoiceLineId()
		// );
		// l_invoiceItemDetailsDao.setInvoiceItemId(pInvoiceLine.getItemId());
		l_invoiceItemDetailsDao.setPaymentAmount(pSettledAmount);
		// l_invoiceItemDetailsDao.setResponsePlan(
		// pInvoiceLine.getExtended() != null ? pInvoiceLine.getExtended()
		// .getResponsePlan() : ""
		// );
		// l_invoiceItemDetailsDao.setPromoPlan(
		// pInvoiceLine.getExtended() != null ? pInvoiceLine.getExtended()
		// .getPromoPlan() : ""
		// );
		// l_invoiceItemDetailsDao.setQuantity(pInvoiceLine.getQuantity());
		l_invoiceItemDetailsDao.setPaymentMethodId(
				p_currentCard.getPaymentMethodId()
		);
		p_container.getInvoiceItemDetailsList().add(
				l_invoiceItemDetailsDao
		);

	}

	// Repository change
	/**
	 * @param p_repoContainer
	 */
	@Transactional(rollbackFor = Exception.class)
	public static void saveReversalData(
			RepoContainer p_repoContainer,
			OrderInvoiceDetailsRepo orderInvoiceDetailsRepo
	)
	{
		try {
			List<OrderInvoiceDetails> l_orderInvoiceDetails = p_repoContainer
					.getOrderInvoiceData();
			enrichItemDetailsData(p_repoContainer);
			// enrichChargeDetailsData(l_settlementContainer);
			orderInvoiceDetailsRepo.saveAll(l_orderInvoiceDetails);
			LOGGER.info(
					"---- Added data to ORDER_INVOICE_DETAILS table ----"
			);
		} catch (Exception ex) {
			LOGGER.info(
					"---- Failed to Add data to ORDER_INVOICE_DETAILS table ----"
			);
		}
	}

	// -------------- Adjustment -------------------------------------------
	// public static void addToOrderInvoiceData(
	// AdjustmentInvoiceLine p_adjustmentInvoiceLine
	// , RepoContainer p_repoContainer
	// , String p_orderId
	// ) {
	// OrderInvoiceDetails l_orderInvoiceDetails = new OrderInvoiceDetails();
	// List<InvoicePaymentMethods> invoicePaymentMethodsList = p_repoContainer
	// .getInvoicePaymentMethodsList();
	// l_orderInvoiceDetails.setOrderId(p_orderId);
	// l_orderInvoiceDetails.setInvoiceId(
	// p_adjustmentInvoiceLine.getInvoiceId()
	// );
	// l_orderInvoiceDetails.setAmount(
	// p_adjustmentInvoiceLine.getInvoiceLineTotal()
	// );
	// // Change getCurrentDate to a Date from p_openInvoice
	// l_orderInvoiceDetails.setOrderDate(Utils.getCurrentDate());
	// l_orderInvoiceDetails.setCreatedDate(Utils.getCurrentDate());
	// l_orderInvoiceDetails.setCreatedBy("eISE");
	// l_orderInvoiceDetails.setTranType(
	// p_repoContainer.getTransactionType()
	// );
	// l_orderInvoiceDetails.setAuditUUID(p_repoContainer.getAuditUUID());
	// for (InvoicePaymentMethods invoicePaymentMethodsDao :
	// invoicePaymentMethodsList) {
	// // invoiceItemSettlementDao.setOrderId(p_openInvoice.getOrderId());
	// invoicePaymentMethodsDao.setInvoiceId(p_adjustmentInvoiceLine.getInvoiceId());
	// invoicePaymentMethodsDao.setOrderInvoiceDetails(
	// l_orderInvoiceDetails
	// );
	// }
	// l_orderInvoiceDetails.setInvoicePaymentMethodsList(
	// invoicePaymentMethodsList
	// );
	//
	// p_repoContainer.getOrderInvoiceData().add(
	// l_orderInvoiceDetails
	// );
	// }

	public static void addToOrderInvoiceData(
			AdjustmentOpenInvoice p_adjustmentInvoiceLine,
			RepoContainer p_repoContainer
	)
	{
		OrderInvoiceDetails l_orderInvoiceDetails = new OrderInvoiceDetails();
		List<InvoicePaymentMethods> invoicePaymentMethodsList = p_repoContainer
				.getInvoicePaymentMethodsList();
		l_orderInvoiceDetails.setOrderId(p_adjustmentInvoiceLine.getOrderId());
		l_orderInvoiceDetails.setInvoiceId(
				p_adjustmentInvoiceLine.getInvoiceId()
		);
		l_orderInvoiceDetails.setAmount(
				p_adjustmentInvoiceLine.getInvoiceTotal().abs()
		);
		// Change getCurrentDate to a Date from p_openInvoice
		l_orderInvoiceDetails.setOrderDate(Utils.getCDTDate());
		l_orderInvoiceDetails.setCreatedDate(Utils.getCurrentDate());
		l_orderInvoiceDetails.setCreatedBy("eISE");
		l_orderInvoiceDetails.setTranType(
				p_repoContainer.getTransactionType()
		);
		l_orderInvoiceDetails.setAuditUUID(p_repoContainer.getAuditUUID());
		for (InvoicePaymentMethods invoicePaymentMethodsDao : invoicePaymentMethodsList) {
			// invoiceItemSettlementDao.setOrderId(p_openInvoice.getOrderId());
			invoicePaymentMethodsDao.setInvoiceId(
					p_adjustmentInvoiceLine.getInvoiceId()
			);
			invoicePaymentMethodsDao.setOrderId(
					p_adjustmentInvoiceLine.getOrderId()
			);
			invoicePaymentMethodsDao.setOrderInvoiceDetails(
					l_orderInvoiceDetails
			);
		}
		l_orderInvoiceDetails.setInvoicePaymentMethodsList(
				invoicePaymentMethodsList
		);

		p_repoContainer.getOrderInvoiceData().add(
				l_orderInvoiceDetails
		);
	}
}
// ----------------------------------------------------------------------------
// END OF FILE
// ----------------------------------------------------------------------------
