package com.aafes.settlement.service;
// ----------------------------------------------------------------------------

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aafes.settlement.configuration.MessagePropertyConfig;
import com.aafes.settlement.constant.Constants;
import com.aafes.settlement.constant.ErrorConstants;
import com.aafes.settlement.core.invoice.InvoiceLine;
import com.aafes.settlement.core.invoice.OpenInvoice;
import com.aafes.settlement.core.model.adjustment.AdjustmentInvoiceLine;
import com.aafes.settlement.core.model.adjustment.AdjustmentOpenInvoice;
import com.aafes.settlement.core.model.adjustment.AdjustmentRO;
import com.aafes.settlement.core.payment.PaymentHeader;
import com.aafes.settlement.core.payment.PaymentMethod;
import com.aafes.settlement.core.request.EISERequestGeneric;
import com.aafes.settlement.core.response.EISEResponseGeneric;
import com.aafes.settlement.core.response.InvoiceResponse;
import com.aafes.settlement.core.response.PaymentMethodResponse;
import com.aafes.settlement.exception.NodeNotFoundException;
import com.aafes.settlement.service.implementation.adjustment.AdjustmentImpl;
import com.aafes.settlement.service.implementation.cancelled.CancelledImpl;
import com.aafes.settlement.service.implementation.exchange.ExchangeImpl;
import com.aafes.settlement.service.implementation.exchange.UnevenExchangeImpl;
import com.aafes.settlement.service.implementation.refund.RefundImpl;
import com.aafes.settlement.service.implementation.reversal.ReversalImpl;
import com.aafes.settlement.service.implementation.settlement.SettlementImpl;
import com.aafes.settlement.util.Utils;
import com.aafes.settlement.validators.RequestValidator;
import com.aafes.settlement.validators.ValidationErrors;

// ----------------------------------------------------------------------------
@Service
public class SmartProcessService
	implements Constants, ErrorConstants
{
	// ------------------------------------------------------------------------
	@Autowired
	private MessagePropertyConfig messagePropConf;

	private static final Logger	  LOGGER = LogManager
			.getLogger(SmartProcessService.class);

	@Autowired
	private SettlementImpl		  settlementImpl;

	@Autowired
	private RefundImpl			  refundImpl;

	@Autowired
	private ExchangeImpl		  exchangeImpl;

	@Autowired
	private UnevenExchangeImpl		  unevenExchangeImpl;

	@Autowired
	private CancelledImpl		  cancelledImpl;

	@Autowired
	private AdjustmentImpl		  adjustmentImpl;

	@Autowired
	private ReversalImpl		  reversalImpl;

	// ------------------------------------------------------------------------
	/**
	 * This method will separate the request based on its type and route it to
	 * respective services
	 * 
	 * @param p_eiseRequestObject
	 *            : contains Request of refund/cancellation/shipment
	 * @return ResponseObject
	 */
	// @Transactional(rollbackFor = Exception.class)
	public void processRequest(
			EISERequestGeneric p_eiseRequestGeneric,
			EISEResponseGeneric p_eiseResponseGeneric
	)
	{
		try {
			RequestValidator l_requestValidator = new RequestValidator();
			ValidationErrors l_validationErrors = l_requestValidator
					.validateRequest(p_eiseRequestGeneric);

			if (l_validationErrors.isValid) {
				Utils.setSuccessInEISEResponseObj(p_eiseResponseGeneric);

				// Repository change
				String l_uuid = UUID.randomUUID().toString().replace(
						"-", ""
				);
				p_eiseRequestGeneric.setUUID(l_uuid);

				if (p_eiseRequestGeneric.getSettlementRequest() != null) {

					LOGGER.info(
							"------- Shipping request received with OrderId: " +
									p_eiseRequestGeneric.getSettlementRequest()
											.getOrderId()
									+ " -------"
					);

					settlementImpl.doCalculation(
							p_eiseRequestGeneric
									.getSettlementRequest(),
							p_eiseResponseGeneric,
							l_uuid
					);

					if (
						p_eiseRequestGeneric.getSettlementRequest()
								.isAdjustmentInvoicePresent() && SUCCESS
										.equalsIgnoreCase(
												p_eiseResponseGeneric
														.getResponseMessage()
										)
					)
					{

						AdjustmentRO l_adjustmentRO = createAdjustmentRequest(
								p_eiseRequestGeneric,
								p_eiseResponseGeneric
						);

						adjustmentImpl.doCalculation(
								l_adjustmentRO,
								p_eiseResponseGeneric,
								l_uuid
						);
					}

					// Repo call
					// saveDataInRepo(p_eiseRequestGeneric,
					// p_eiseResponseGeneric, SETTLEMENT);

				}

				if (p_eiseRequestGeneric.getRefundRequest() != null) {
					LOGGER.info(
							"-------- Refund request received with OrderId :"
									+ p_eiseRequestGeneric.getRefundRequest()
											.getParentOrder().getOrderId()
									+ " -------"
					);
					refundImpl.doCalculation(
							p_eiseRequestGeneric.getRefundRequest(),
							p_eiseResponseGeneric,
							l_uuid
					);
				}

				if (p_eiseRequestGeneric.getExchangeRequest() != null) {
					LOGGER.info(
							"-------- Exchange request received with OrderId: "
									+ p_eiseRequestGeneric.getExchangeRequest()
											.getParentOrder().getOrderId()
									+ " -------"
					);
					exchangeImpl.doCalculation(
							p_eiseRequestGeneric.getExchangeRequest(),
							p_eiseResponseGeneric,
							l_uuid
					);
				}
				if (p_eiseRequestGeneric.getUnevenExchangeRequest() != null) {
					LOGGER.info(
							"-------- UnEven Exchange request received with OrderId: "
									+ p_eiseRequestGeneric.getUnevenExchangeRequest()
											.getParentOrder().getOrderId()
									+ " -------"
					);
					unevenExchangeImpl.doCalculation(
							p_eiseRequestGeneric.getUnevenExchangeRequest(),
							p_eiseResponseGeneric,
							l_uuid
					);
				}
				if (p_eiseRequestGeneric.getCancelledRequest() != null) {
					LOGGER.info(
							"----- BOPIS CANCELLED request received with OrderId: "
									+ p_eiseRequestGeneric.getCancelledRequest()
											.getOrderId()
									+ " -------"
					);
					cancelledImpl.doCalculation(
							p_eiseRequestGeneric.getCancelledRequest(),
							p_eiseResponseGeneric,
							l_uuid
					);
				}
				if (p_eiseRequestGeneric.getAdjustmentRequest() != null) {
					/*
					 * LOGGER.info(
					 * "-------- Adjustment request received with OrderId: " +
					 * p_eiseRequestGeneric .getAdjustmentRequest().getOrderId()
					 * + "-------" );
					 */
					adjustmentImpl.doCalculation(
							p_eiseRequestGeneric.getAdjustmentRequest(),
							p_eiseResponseGeneric,
							l_uuid
					);
				}
				if (p_eiseRequestGeneric.getAuthReversalRequest() != null) {
					LOGGER.info(
							"-------- Auth Reversal request received with OrderId: "
									+ p_eiseRequestGeneric
											.getAuthReversalRequest()
											.getOrderId()
									+ "-------"
					);
					reversalImpl.doCalculation(
							p_eiseRequestGeneric.getAuthReversalRequest(),
							p_eiseResponseGeneric,
							l_uuid
					);
				}
			} else {
				Utils.setFailureInEISEResponseObj(p_eiseResponseGeneric);

				l_validationErrors.errors.put(
						ErrorConstants.INVALID_REQUEST,
						""
				);

				p_eiseResponseGeneric.setErrors(
						Utils
								.getErrors(
										l_validationErrors.errors,
										messagePropConf
								)
				);
			}
		} catch (NodeNotFoundException l_e) {
			LOGGER.error(l_e.getErrorMsg());
			Utils.setFailureInEISEResponseObj(p_eiseResponseGeneric);
			p_eiseResponseGeneric.setErrors(
					Utils.getErrors(l_e.errors.errors, messagePropConf)
			);
		} catch (Exception l_e) {
			l_e.printStackTrace();
			LOGGER.error("Unexpected Error:", l_e);
			Utils.setExceptionInEISEResponseObj(
					p_eiseResponseGeneric, l_e,
					messagePropConf
			);

		}
	}

	/**
	 * This method will creates a Adjustment request from the EISERequestGeneric
	 * if Settlement and Adjustments are in same request
	 * 
	 * @param p_eiseRequestGeneric
	 * @return p_eiseResponseGeneric
	 */

	private AdjustmentRO createAdjustmentRequest(
			EISERequestGeneric p_eiseRequestGeneric,
			EISEResponseGeneric p_eiseResponseGeneric
	)
			throws Exception
	{

		AdjustmentRO l_adjustmentRO = new AdjustmentRO();

		if (p_eiseResponseGeneric.getSettlementTender() != null) {

			List<AdjustmentOpenInvoice> l_adjustmentOpenInvoiceList = new ArrayList<
					AdjustmentOpenInvoice>();
			for (OpenInvoice l_openInvoice : p_eiseRequestGeneric
					.getSettlementRequest().getOpenInvoice())
			{

				List<AdjustmentInvoiceLine> l_adjustmentInvoiceLineList = new ArrayList<
						AdjustmentInvoiceLine>();

				AdjustmentOpenInvoice l_adjustmentOpenInvoice = new AdjustmentOpenInvoice();

				for (InvoiceLine l_invoiceLine : l_openInvoice
						.getInvoiceLine())
				{

					AdjustmentInvoiceLine l_adjustmentInvoiceLine = new AdjustmentInvoiceLine();

					BeanUtils.copyProperties(
							l_adjustmentInvoiceLine, l_invoiceLine
					);
					l_adjustmentInvoiceLineList.add(l_adjustmentInvoiceLine);
				}

				BeanUtils.copyProperties(
						l_adjustmentOpenInvoice, l_openInvoice
				);

				l_adjustmentOpenInvoice.setInvoiceLine(
						l_adjustmentInvoiceLineList
				);
				l_adjustmentOpenInvoiceList.add(l_adjustmentOpenInvoice);
			}

			BeanUtils.copyProperties(
					l_adjustmentRO, p_eiseRequestGeneric.getSettlementRequest()
			);
			l_adjustmentRO.setOpenInvoice(l_adjustmentOpenInvoiceList);

			// Setting payment methods
			InvoiceResponse l_invoiceResponse = p_eiseResponseGeneric
					.getSettlementTender().get(0);

			Map<String, Double> l_paymentMethodAmountMap = new HashMap<
					String, Double>();

			for (PaymentMethodResponse l_paymentMethodResponse : l_invoiceResponse
					.getPaymentMethod())
			{

				Double sum = l_paymentMethodResponse.getInvoiceLine().stream()
						.map(
								x -> x.getPaymentAmount()
										.doubleValue()
						)
						.collect(
								Collectors.summingDouble(
										Double::doubleValue
								)
						);

				l_paymentMethodAmountMap.put(
						l_paymentMethodResponse.getPaymentMethodId(),
						sum
				);

			}

			// set payment

			List<PaymentMethod> l_paymentMethodList = new ArrayList<
					PaymentMethod>();
			List<PaymentHeader> l_paymentHeaderList = new ArrayList<
					PaymentHeader>();
			PaymentHeader l_paymentHeader = new PaymentHeader();

			l_adjustmentRO.getPaymentHeader().stream().forEach(
					paymentHeader ->
					{
						paymentHeader.getPaymentMethod().stream()
								.forEach(i ->
								{
									if (
										l_paymentMethodAmountMap.get(
												i.getPaymentMethodId()
										) != null
									)
								{
										i.setCurrentSettledAmount(
												i.getCurrentSettledAmount().add(
														new BigDecimal(
																l_paymentMethodAmountMap
																		.get(
																				i.getPaymentMethodId()
																		)
														).setScale(
																2,
																BigDecimal.ROUND_UP
														)
												)
										);
										l_paymentMethodList.add(i);
									}
								});
						l_paymentHeader.setPaymentMethod(l_paymentMethodList);
					}
			);
			l_paymentHeaderList.add(l_paymentHeader);

			l_adjustmentRO.setPaymentHeader(l_paymentHeaderList);
		}
		return l_adjustmentRO;
	}
	// ------------------------------------------------------------------------
	/*
	 * private void saveDataInRepo( EISERequestGeneric p_eiseRequestGeneric ,
	 * EISEResponseGeneric p_eiseResponseGeneric , String p_requestType ) {
	 * switch (p_requestType) { case SETTLEMENT: // insert into
	 * OrderInvoiceDetails - 1 // insert into InvoiceItemDetails - loop //
	 * insert into InvoiceItemSettlement - loop // insert into
	 * InvoiceChargeDetails - loop break; case ADJUSTMENT_INVOICE:
	 * 
	 * break; default: break; } }
	 */
	// ------------------------------------------------------------------------
	/*
	 * private void saveDataInOrderInvoiceDetails( String p_orderId , String
	 * p_requestType , BigDecimal p_orderAmount ) {
	 * 
	 * OrderInvoiceDetails orderInvoiceData = new OrderInvoiceDetails();
	 * orderInvoiceData.setOrderId(p_orderId);
	 * orderInvoiceData.setTranUUID("222222");
	 * orderInvoiceData.setTranType(p_requestType);
	 * orderInvoiceData.setCreatedDate(Utils.getCurrentDate());
	 * orderInvoiceData.setAmount(p_orderAmount);
	 * orderInvoiceData.setAuditUUID("3333333"); }
	 */
	// ------------------------------------------------------------------------
}
// ----------------------------------------------------------------------------
// END OF FILE
// ----------------------------------------------------------------------------