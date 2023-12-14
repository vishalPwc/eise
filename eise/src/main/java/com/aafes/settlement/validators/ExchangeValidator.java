package com.aafes.settlement.validators;

import java.math.BigDecimal;
import java.util.concurrent.atomic.AtomicBoolean;

// ----------------------------------------------------------------------------
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.aafes.settlement.constant.Constants;
import com.aafes.settlement.constant.ErrorConstants;
import com.aafes.settlement.core.invoice.InvoiceChargeDetail;
import com.aafes.settlement.core.invoice.InvoiceLine;
import com.aafes.settlement.core.invoice.OpenInvoice;
import com.aafes.settlement.core.model.exchange.ExchangeRO;
import com.aafes.settlement.core.model.settlement.SettlementRO;
import com.aafes.settlement.core.order.OrderLine;
import com.aafes.settlement.core.payment.PaymentHeader;
import com.aafes.settlement.core.payment.PaymentMethod;
import com.aafes.settlement.exception.NodeNotFoundException;
import com.aafes.settlement.util.Utils;

// ----------------------------------------------------------------------------

/**
 * Class implements methods to be used for Exchange Request validation
 */
public class ExchangeValidator
	implements Validator, Constants, ErrorConstants
{
	// ------------------------------------------------------------------------
	/** information logger **/
	private static final Logger LOGGER = LogManager
			.getLogger(SettlementValidator.class);

	// ------------------------------------------------------------------------
	/**
	 * 
	 */
	@Override
	public ValidationErrors validateRequest(Object p_requestObject) {
		ValidationErrors l_errorObj = new ValidationErrors();
		isNodeAvailable(p_requestObject, l_errorObj);
		return l_errorObj;
	}

	// ------------------------------------------------------------------------
	/**
	 * This method checks if the primary nodes are available in the request
	 * 
	 * @param p_requestNode
	 * @param p_errorObj
	 * @return boolean
	 */
	@Override
	public boolean
			isNodeAvailable(Object p_requestNode, ValidationErrors p_errorObj)
	{
		ExchangeRO l_exchangeRequest = (ExchangeRO) p_requestNode;

		LOGGER.info(
				"Exchange Request Valdation - isNodeAvailable() :: "
						+ "Check Parent Order Node"
		);

		ValidationUtils.checkIfNull(
				l_exchangeRequest.getParentOrder(),
				PARENT_ORDER, EXCHANGE_MANDATORY_MISSING, EXCHANGE_INVOICE,
				p_errorObj
		);

		SettlementRO l_parentNodeRo = l_exchangeRequest.getParentOrder();

		LOGGER.info(
				"Exchange Request Valdation - isNodeAvailable() :: "
						+ "Check Payment Header"
		);

		ValidationUtils.checkIfNull(
				l_parentNodeRo.getPaymentHeader(),
				PAYMENT_HEADER, EXCHANGE_MANDATORY_MISSING, EXCHANGE_INVOICE,
				p_errorObj
		);

		LOGGER.info(
				"Exchange Request Valdation - isNodeAvailable() :: "
						+ "Check if there are multiple Payment Headers"
		);

		ValidationUtils.validateLengthOfPaymentHeader(
				l_parentNodeRo.getPaymentHeader(), EXCHANGE_INVOICE,
				EXCHANGE_INVALID_VALUE, p_errorObj
		);

		LOGGER.info(
				"Refund Request Valdation - isNodeAvailable() :: "
						+ "Check if there are multiple Payment Headers"
		);

		ValidationUtils.validateLengthOfPaymentHeader(
				l_parentNodeRo.getPaymentHeader(), EXCHANGE_INVOICE,
				EXCHANGE_INVALID_VALUE, p_errorObj
		);

		LOGGER.info(
				"Exchange Request Valdation - isNodeAvailable() :: "
						+ "Check Order Lines"
		);
		ValidationUtils.checkIfNull(
				l_parentNodeRo.getOrderLine(), ORDER_LINE,
				EXCHANGE_MANDATORY_MISSING, EXCHANGE_INVOICE, p_errorObj
		);

		LOGGER.info(
				"Exchange Request Valdation - isNodeAvailable() :: "
						+ "Check Open Invoices"
		);
		ValidationUtils.checkIfNull(
				l_exchangeRequest.getOpenInvoice(),
				OPEN_INVOICE, EXCHANGE_MANDATORY_MISSING, EXCHANGE_INVOICE,
				p_errorObj
		);

		LOGGER.info(
				"Exchange Request Valdation - isNodeAvailable() ::"
						+ " Check Order ID"
		);
		ValidationUtils.checkIfNull(
				l_parentNodeRo.getOrderId(), ORDER_ID,
				EXCHANGE_MANDATORY_MISSING, EXCHANGE_INVOICE, p_errorObj
		);

		// Commented to fix EOR-3234
		/*
		 * LOGGER.info( "Exchange Request Validation - isNodeAvailable() :: " +
		 * "Check Closed Invoices"5 );
		 * 
		 * ValidationUtils.checkIfNull(
		 * l_exchangeRequest.getParentOrder().getClosedInvoices() ,
		 * "ClosedInvoices" , EXCHANGE_MANDATORY_MISSING , EXCHANGE_INVOICE ,
		 * p_errorObj );
		 */

		return p_errorObj.isValid;
	}

	// ------------------------------------------------------------------------
	/**
	 * This method validates if the PaymentMethod node of PaymentHeader node is
	 * null or empty.
	 * 
	 * @param p_paymentHeader
	 * @param p_validationErrors
	 * @throws NodeNotFoundException
	 */
	public void validatePaymentHeader(
			PaymentHeader p_paymentHeader,
			ValidationErrors p_validationErrors
	)
			throws NodeNotFoundException
	{
		ValidationUtils.checkIfNull(
				p_paymentHeader.getPaymentMethod(),
				PAYMENT_METHOD, EXCHANGE_MANDATORY_MISSING, EXCHANGE_INVOICE,
				p_validationErrors
		);
	}

	// ------------------------------------------------------------------------
	/**
	 * This method validates the attributes/nodes of InvoiceLine node.
	 * 
	 * @param p_invoiceLine
	 * @param p_validationErrors
	 * @throws NodeNotFoundException
	 */
	public void validateInvoiceLine(
			InvoiceLine p_invoiceLine, ValidationErrors p_validationErrors,
			boolean p_isMilstarBucket
	)
			throws NodeNotFoundException
	{

		ValidationUtils.checkIfNull(
				p_invoiceLine.getInvoiceLineId(),
				INVOICE_LINE_ID, EXCHANGE_MANDATORY_MISSING, EXCHANGE_INVOICE,
				p_validationErrors
		);

		ValidationUtils.checkIfNull(
				p_invoiceLine.getInvoiceLineTotal(),
				INVOICE_LINE_TOTAL, EXCHANGE_MANDATORY_MISSING,
				EXCHANGE_INVOICE, p_validationErrors
		);

		// Fix for EOR-6867
		/*
		 * ValidationUtils.checkIfAmountValid(p_invoiceLine.getInvoiceLineTotal(
		 * ), INVOICE_LINE_TOTAL, EXCHANGE_MANDATORY_MISSING, EXCHANGE_INVOICE,
		 * p_validationErrors);
		 */

		if (p_isMilstarBucket) {

			ValidationUtils.checkIfNull(
					p_invoiceLine.getExtended(),
					INVOICE_LINE + "." + EXTENDED, EXCHANGE_MANDATORY_MISSING,
					EXCHANGE_INVOICE, p_validationErrors
			);

			ValidationUtils.checkIfNull(
					p_invoiceLine.getExtended().getResponsePlan(),
					INVOICE_LINE + "." + EXTENDED + "." + RESPONSE_PLAN,
					EXCHANGE_MANDATORY_MISSING,
					EXCHANGE_INVOICE, p_validationErrors
			);

			ValidationUtils.validateInvoiceLineReponsePlan(
					p_invoiceLine.getExtended().getResponsePlan(),
					EXCHANGE_INVOICE, EXCHANGE_INVALID_VALUE,
					p_validationErrors
			);
		}

	}

	// ------------------------------------------------------------------------
	/**
	 * This method validates the attributes/nodes of OpenInvoice node.
	 * 
	 * @param p_currentOpenInvoice
	 * @param p_validationErrors
	 * @throws NodeNotFoundException
	 */
	public void validateOpenInvoice(
			OpenInvoice p_currentOpenInvoice,
			ValidationErrors p_validationErrors
	)
			throws NodeNotFoundException
	{
		ValidationUtils.checkIfNull(
				p_currentOpenInvoice.getInvoiceId(),
				INVOICE_ID, EXCHANGE_MANDATORY_MISSING, EXCHANGE_INVOICE,
				p_validationErrors
		);

		/*
		 * ValidationUtils.validateAtLeastOneInOpenInvoice(
		 * p_currentOpenInvoice, EXCHANGE_INVOICE, EXCHANGE_MANDATORY_MISSING,
		 * p_validationErrors);
		 */

		ValidationUtils.checkIfNull(
				p_currentOpenInvoice.getInvoiceLine(),
				INVOICE_LINE, EXCHANGE_MANDATORY_MISSING, EXCHANGE_INVOICE,
				p_validationErrors
		);
	}

	// ------------------------------------------------------------------------
	/**
	 * This method validates the following required nodes/attributes of
	 * PaymentMethod node:
	 * <ul>
	 * <li>PaymentMethodId
	 * <li>PaymentType
	 * <li>PaymentTypeId
	 * <li>CardType
	 * <li>CardTypeId
	 * <li>CurrentSettledAmount
	 * <p>
	 * If the CardType = Military Star, following attributes become mandatory
	 * <li>Extended
	 * <li>Extended.ResponsePlan
	 * </ul>
	 * 
	 * @param p_paymentMethod
	 * @param p_validationErrors
	 * @throws NodeNotFoundException
	 *             when the first attribute/node is found null.
	 */
	public void validatePaymentMethod(
			PaymentMethod p_paymentMethod, ValidationErrors p_validationErrors,
			AtomicBoolean p_isMUCardPresent, AtomicBoolean p_isMRCardPresent
	)
			throws NodeNotFoundException
	{

		ValidationUtils.checkIfNull(
				p_paymentMethod.getPaymentMethodId(),
				PAYMENT_METHOD_ID, EXCHANGE_MANDATORY_MISSING, EXCHANGE_INVOICE,
				p_validationErrors
		);

		ValidationUtils.checkIfNull(
				p_paymentMethod.getPaymentType(),
				PAYMENT_TYPE, EXCHANGE_MANDATORY_MISSING, EXCHANGE_INVOICE,
				p_validationErrors
		);

		ValidationUtils.checkIfNull(
				p_paymentMethod.getPaymentType().getPaymentTypeId(),
				PAYMENT_TYPE_ID, EXCHANGE_MANDATORY_MISSING, EXCHANGE_INVOICE,
				p_validationErrors
		);

		ValidationUtils.checkIfNull(
				p_paymentMethod.getCurrentSettledAmount(),
				CURRENT_SETTLED_AMT, EXCHANGE_MANDATORY_MISSING,
				EXCHANGE_INVOICE, p_validationErrors
		);

		ValidationUtils.checkIfNull(
				p_paymentMethod.getCardType(), CARD_TYPE,
				EXCHANGE_MANDATORY_MISSING, EXCHANGE_INVOICE,
				p_validationErrors
		);

		ValidationUtils.checkIfNull(
				p_paymentMethod.getCardType().getCardTypeId(), CARD_TYPE_ID,
				EXCHANGE_MANDATORY_MISSING, EXCHANGE_INVOICE,
				p_validationErrors
		);

		if (
			p_paymentMethod.getCardType().getCardTypeId()
					.equalsIgnoreCase(MILSTAR_CARD)
		)
		{
			ValidationUtils.checkIfNull(
					p_paymentMethod.getExtended(), PAYMENT_METHOD + "." + EXTENDED,
					EXCHANGE_MANDATORY_MISSING, EXCHANGE_INVOICE,
					p_validationErrors
			);

			ValidationUtils.checkIfNull(
					p_paymentMethod.getExtended().getResponsePlan(),
					PAYMENT_METHOD + "." + EXTENDED + "." + RESPONSE_PLAN, EXCHANGE_MANDATORY_MISSING,
					EXCHANGE_INVOICE, p_validationErrors
			);

			ValidationUtils.validateResponePlan(
					p_paymentMethod, p_validationErrors, p_isMRCardPresent,
					p_isMUCardPresent, EXCHANGE_PAYMENT_METHOD_DUPLICATED,
					EXCHANGE_INVALID_PAYMENT_METHOD, EXCHANGE_INVOICE
			);

		}
	}

	// ------------------------------------------------------------------------
	/**
	 * This method validates
	 * <ol>
	 * <li>ChargeDetailId
	 * <li>ChargeTotal
	 * </ol>
	 * attribute of InvoiceChargeDetails when the ChargeTypeId = Shipping
	 * 
	 * @param p_invoiceChargeDetail
	 * @param p_validationErrors
	 * @return true : if both the attributes are valid <br>
	 *         false : if the ChargeTypeId != Shipping
	 * @throws NodeNotFoundException
	 *             if any of the two attributes are invalid
	 */
	public boolean validateInvoiceChargeDetail(
			InvoiceChargeDetail p_invoiceChargeDetail,
			ValidationErrors p_validationErrors
	)
			throws NodeNotFoundException
	{

		if (
			p_invoiceChargeDetail != null
					&& p_invoiceChargeDetail.getChargeType() != null
					&& p_invoiceChargeDetail.getChargeType()
							.getChargeTypeId() != null
					&& p_invoiceChargeDetail.getChargeType().getChargeTypeId()
							.equals(
									SHIPPING_CHARGES
							)
		)
		{
			ValidationUtils.checkIfNull(
					p_invoiceChargeDetail.getChargeDetailId(),
					CHARGE_DETAIL_ID, EXCHANGE_MANDATORY_MISSING,
					EXCHANGE_INVOICE,
					p_validationErrors
			);

			ValidationUtils.checkIfNull(
					p_invoiceChargeDetail.getChargeTotal(),
					CHARGE_TOTAL, EXCHANGE_MANDATORY_MISSING, EXCHANGE_INVOICE,
					p_validationErrors
			);

			return true;
		} else {
			return false;
		}
	}

	// ------------------------------------------------------------------------
	/**
	 * This method validates the following required nodes/attributes of
	 * OrderLine node:
	 * <ul>
	 * <li>OrderLineTotal</li>
	 * </ul>
	 * If its a Milstar bucket:
	 * <ul>
	 * <li>Extended</li>
	 * <li>Extended.ResponsePlan</li>
	 * </ul>
	 * 
	 * @param p_orderLine
	 * @param p_validationErrors
	 * @param p_isMilstarBucket
	 * @throws NodeNotFoundException
	 */
	public void validateOrderLines(
			OrderLine p_orderLine, ValidationErrors p_validationErrors,
			boolean p_isMilstarBucket
	)
			throws NodeNotFoundException
	{

		ValidationUtils.checkIfNull(
				p_orderLine.getOrderLineTotal(),
				ORDER_LINE_TOTAL, EXCHANGE_MANDATORY_MISSING, EXCHANGE_INVOICE,
				p_validationErrors
		);

		// Fix for EOR-6867
		/*
		 * ValidationUtils.checkIfAmountValid(p_orderLine.getOrderLineTotal(),
		 * ORDER_LINE_TOTAL, EXCHANGE_INVALID_VALUE, EXCHANGE_INVOICE,
		 * p_validationErrors);
		 */

		if (p_isMilstarBucket) {

			ValidationUtils.checkIfNull(
					p_orderLine.getExtended(),
					ORDER_LINE + "." + EXTENDED, EXCHANGE_MANDATORY_MISSING,
					EXCHANGE_INVOICE, p_validationErrors
			);

			ValidationUtils.checkIfNull(
					p_orderLine.getExtended().getResponsePlan(),
					ORDER_LINE + "." + EXTENDED + "." + RESPONSE_PLAN,
					EXCHANGE_MANDATORY_MISSING,
					EXCHANGE_INVOICE, p_validationErrors
			);

			ValidationUtils.validateInvoiceLineReponsePlan(
					p_orderLine.getExtended().getResponsePlan(),
					EXCHANGE_INVOICE, EXCHANGE_INVALID_VALUE,
					p_validationErrors
			);
		}
	}

	// ------------------------------------------------------------------------
	/**
	 * This method validates the following required nodes/attributes of
	 * ClosedInvoice.InvoiceLine node:
	 * <ul>
	 * <li>InvoiceLineTotal
	 * </ul>
	 * If its a Milstar Bucket:
	 * <ul>
	 * <li>Extended
	 * <li>Extended.ResponsePlan
	 * </ul>
	 * 
	 * @param p_closedInvoiceLine
	 * @param p_validationErrors
	 * @param p_isMilstarBucket
	 * @throws NodeNotFoundException
	 */
	public void validateClosedInvoiceLine(
			InvoiceLine p_closedInvoiceLine,
			ValidationErrors p_validationErrors, boolean p_isMilstarBucket
	)
			throws NodeNotFoundException
	{

		ValidationUtils.checkIfNull(
				p_closedInvoiceLine.getInvoiceLineTotal(), CLOSED_INVOICE + "."
						+ INVOICE_LINE + "." + INVOICE_LINE_TOTAL,
				EXCHANGE_MANDATORY_MISSING, EXCHANGE_INVOICE, p_validationErrors
		);

		if (p_isMilstarBucket) {

			ValidationUtils.checkIfNull(
					p_closedInvoiceLine.getExtended(), CLOSED_INVOICE + "."
							+ INVOICE_LINE + "." + EXTENDED,
					EXCHANGE_MANDATORY_MISSING, EXCHANGE_INVOICE,
					p_validationErrors
			);

			ValidationUtils.checkIfNull(
					p_closedInvoiceLine.getExtended().getResponsePlan(),
					CLOSED_INVOICE + "." + INVOICE_LINE + "." + EXTENDED + "."
							+ RESPONSE_PLAN, EXCHANGE_MANDATORY_MISSING,
					EXCHANGE_INVOICE, p_validationErrors
			);

			ValidationUtils.validateInvoiceLineReponsePlan(
					p_closedInvoiceLine.getExtended().getResponsePlan(),
					EXCHANGE_INVOICE, EXCHANGE_INVALID_VALUE, p_validationErrors
			);

		}

	}

	// ------------------------------------------------------------------------
	/**
	 * Check if total of Open Invoice total is less than remaining current
	 * authorized amount of payment methods
	 */
	@Override
	public boolean compareAmount(
			BigDecimal pCurrentSettled,
			BigDecimal pOpenInvoiceAmount
	)
	{
		// return pOpenInvoiceAmount <= pCurrentSettled;
		return Utils.isValueLesserOrEqual(pOpenInvoiceAmount, pCurrentSettled);
	}
	// ------------------------------------------------------------------------
}
// END OF FILE