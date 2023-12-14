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
import com.aafes.settlement.core.model.adjustment.AdjustmentInvoiceLine;
import com.aafes.settlement.core.model.adjustment.AdjustmentOpenInvoice;
import com.aafes.settlement.core.model.adjustment.AdjustmentRO;
import com.aafes.settlement.core.order.OrderLine;
import com.aafes.settlement.core.payment.PaymentHeader;
import com.aafes.settlement.core.payment.PaymentMethod;
import com.aafes.settlement.exception.NodeNotFoundException;
import com.aafes.settlement.util.Utils;

// ----------------------------------------------------------------------------

/**
 * Class implements methods to be used for Adjustment Request validation
 */
public class AdjustmentValidator
	implements
	Validator,
	Constants,
	ErrorConstants
{
	// ------------------------------------------------------------------------
	/** information logger **/
	private static final Logger LOGGER = LogManager
			.getLogger(AdjustmentValidator.class);

	// ------------------------------------------------------------------------
	/**
	 * Validate Adjustment Request Node Before Processing All required Nodes
	 * will be checked for availability
	 * 
	 * @param p_requestObject
	 * @return ValidationErrors
	 */
	@Override
	public ValidationErrors validateRequest(Object p_requestObject) {
		ValidationErrors l_errorObj = new ValidationErrors();
		isNodeAvailable(p_requestObject, l_errorObj);
		return l_errorObj;
	}

	// ------------------------------------------------------------------------
	/**
	 * check if node is available in request
	 * 
	 * @param p_adjustmentRequestNode
	 * @param p_errorObj
	 * @return boolean
	 */
	@Override
	public boolean isNodeAvailable(
			Object p_adjustmentRequestNode, ValidationErrors p_errorObj
	)
	{
		AdjustmentRO p_adjustmentRequest = (AdjustmentRO) p_adjustmentRequestNode;

		LOGGER.info(
				"Adjustment Request Valdation - isNodeAvailable() :: "
						+ "Check  Payment Header"
		);
		ValidationUtils.checkIfNull(
				p_adjustmentRequest.getPaymentHeader(),
				PAYMENT_HEADER, ADJUSTMENT_MANDATORY_MISSING,
				ADJUSTMENT_INVOICE, p_errorObj
		);

		LOGGER.info(
				"Adjustment Request Valdation - isNodeAvailable() :: "
						+ "Check if there are multiple Payment Headers"
		);

		ValidationUtils.validateLengthOfPaymentHeader(
				p_adjustmentRequest.getPaymentHeader(), ADJUSTMENT_INVOICE,
				ADJUSTMENT_INVALID_VALUE, p_errorObj
		);

		LOGGER.info(
				"Adjustment Request Valdation - isNodeAvailable() :: "
						+ "Check Order Lines"
		);
		ValidationUtils.checkIfNull(
				p_adjustmentRequest.getOrderLine(),
				ORDER_LINE, ADJUSTMENT_MANDATORY_MISSING, ADJUSTMENT_INVOICE,
				p_errorObj
		);

		LOGGER.info(
				"Adjustment Request Valdation - isNodeAvailable() :: "
						+ "Check Open Invoices"
		);
		ValidationUtils.checkIfNull(
				p_adjustmentRequest.getOpenInvoice(),
				OPEN_INVOICE, ADJUSTMENT_MANDATORY_MISSING, ADJUSTMENT_INVOICE,
				p_errorObj
		);

		LOGGER.info(
				"Adjustment Request Valdation - isNodeAvailable() :: "
						+ "Check Order ID"
		);
		ValidationUtils.checkIfNull(
				p_adjustmentRequest.getOrderId(), ORDER_ID,
				ADJUSTMENT_MANDATORY_MISSING, ADJUSTMENT_INVOICE, p_errorObj
		);

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
				PAYMENT_METHOD, ADJUSTMENT_MANDATORY_MISSING,
				ADJUSTMENT_INVOICE,
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
			AdjustmentInvoiceLine p_invoiceLine,
			ValidationErrors p_validationErrors, boolean p_isMilstarBucket
	)
			throws NodeNotFoundException
	{

		ValidationUtils.checkIfNull(
				p_invoiceLine.getInvoiceLineId(),
				INVOICE_LINE_ID, ADJUSTMENT_MANDATORY_MISSING,
				ADJUSTMENT_INVOICE, p_validationErrors
		);

		ValidationUtils.checkIfNull(
				p_invoiceLine.getInvoiceLineTotal(),
				INVOICE_LINE_TOTAL, ADJUSTMENT_MANDATORY_MISSING,
				ADJUSTMENT_INVOICE, p_validationErrors
		);

		if (p_isMilstarBucket) {

			ValidationUtils.checkIfNull(
					p_invoiceLine.getExtended(),
					INVOICE_LINE + "." + EXTENDED, ADJUSTMENT_MANDATORY_MISSING,
					ADJUSTMENT_INVOICE, p_validationErrors
			);

			ValidationUtils.checkIfNull(
					p_invoiceLine.getExtended().getResponsePlan(),
					INVOICE_LINE + "." + EXTENDED + "." + RESPONSE_PLAN,
					ADJUSTMENT_MANDATORY_MISSING,
					ADJUSTMENT_INVOICE, p_validationErrors
			);

			ValidationUtils.validateInvoiceLineReponsePlan(
					p_invoiceLine.getExtended().getResponsePlan(),
					ADJUSTMENT_INVOICE, ADJUSTMENT_INVALID_VALUE,
					p_validationErrors
			);
		}
		/*
		 * Not required as it will always be negative and is explicitly changed
		 * to positive in the caller method.
		 */
		/*
		 * ValidationUtils.checkIfAmountValid(
		 * p_invoiceLine.getInvoiceLineTotal() , INVOICE_LINE_TOTAL ,
		 * ADJUSTMENT_MANDATORY_MISSING , ADJUSTMENT_INVOICE ,
		 * p_validationErrors );
		 */
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
			AdjustmentOpenInvoice p_currentOpenInvoice,
			ValidationErrors p_validationErrors
	)
			throws NodeNotFoundException
	{
		ValidationUtils.checkIfNull(
				p_currentOpenInvoice.getInvoiceId(),
				INVOICE_ID, ADJUSTMENT_MANDATORY_MISSING, ADJUSTMENT_INVOICE,
				p_validationErrors
		);

		ValidationUtils.checkIfNull(
				p_currentOpenInvoice.getInvoiceLine(),
				INVOICE_LINE, ADJUSTMENT_MANDATORY_MISSING, ADJUSTMENT_INVOICE,
				p_validationErrors
		);
	}

	// ------------------------------------------------------------------------
	/**
	 * This method validates the attributes/nodes of OpenInvoice node.
	 * 
	 * @param p_currentOpenInvoice
	 * @param p_validationErrors
	 * @throws NodeNotFoundException
	 */
	public boolean validateInvoiceChargeDetails(
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
					&& APPEASEMENT_CHARGE.equalsIgnoreCase(
							p_invoiceChargeDetail.getChargeType()
									.getChargeTypeId()
					)
		)
		{
			ValidationUtils.checkIfNull(
					p_invoiceChargeDetail.getChargeDetailId(),
					CHARGE_DETAIL_ID, ADJUSTMENT_MANDATORY_MISSING,
					ADJUSTMENT_INVOICE, p_validationErrors
			);

			ValidationUtils.checkIfNull(
					p_invoiceChargeDetail.getChargeTotal(),
					CHARGE_TOTAL, ADJUSTMENT_MANDATORY_MISSING,
					ADJUSTMENT_INVOICE, p_validationErrors
			);

			return true;
		} else {
			return false;
		}
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
				PAYMENT_METHOD_ID, ADJUSTMENT_MANDATORY_MISSING,
				ADJUSTMENT_INVOICE, p_validationErrors
		);

		ValidationUtils.checkIfNull(
				p_paymentMethod.getPaymentType(),
				PAYMENT_TYPE, ADJUSTMENT_MANDATORY_MISSING, ADJUSTMENT_INVOICE,
				p_validationErrors
		);

		ValidationUtils.checkIfNull(
				p_paymentMethod.getPaymentType().getPaymentTypeId(),
				PAYMENT_TYPE_ID, ADJUSTMENT_MANDATORY_MISSING,
				ADJUSTMENT_INVOICE, p_validationErrors
		);

		ValidationUtils.checkIfNull(
				p_paymentMethod.getCurrentSettledAmount(),
				CURRENT_SETTLED_AMT, ADJUSTMENT_MANDATORY_MISSING,
				ADJUSTMENT_INVOICE, p_validationErrors
		);

		ValidationUtils.checkIfNull(
				p_paymentMethod.getCardType(), CARD_TYPE,
				ADJUSTMENT_MANDATORY_MISSING, ADJUSTMENT_INVOICE,
				p_validationErrors
		);

		ValidationUtils.checkIfNull(
				p_paymentMethod.getCardType().getCardTypeId(), CARD_TYPE_ID,
				ADJUSTMENT_MANDATORY_MISSING, ADJUSTMENT_INVOICE,
				p_validationErrors
		);

		if (
			p_paymentMethod.getCardType().getCardTypeId()
					.equalsIgnoreCase(MILSTAR_CARD)
		)
		{
			ValidationUtils.checkIfNull(
					p_paymentMethod.getExtended(), PAYMENT_METHOD + "." + EXTENDED,
					ADJUSTMENT_MANDATORY_MISSING, ADJUSTMENT_INVOICE,
					p_validationErrors
			);

			ValidationUtils.checkIfNull(
					p_paymentMethod.getExtended().getResponsePlan(),
					PAYMENT_METHOD + "." + EXTENDED + "." + RESPONSE_PLAN,
					ADJUSTMENT_MANDATORY_MISSING, ADJUSTMENT_INVOICE,
					p_validationErrors
			);

			ValidationUtils.validateResponePlan(
					p_paymentMethod, p_validationErrors, p_isMRCardPresent,
					p_isMUCardPresent, ADJUSTMENT_PAYMENT_METHOD_DUPLICATED,
					ADJUSTMENT_INVALID_PAYMENT_METHOD, ADJUSTMENT_INVOICE
			);
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
				ORDER_LINE_TOTAL, ADJUSTMENT_MANDATORY_MISSING,
				ADJUSTMENT_INVOICE,
				p_validationErrors
		);

		/*
		 * EOR-6867. This check is not required. Reverting to checkIfAmountValid
		 * from validateIfAmountIsPositive
		 */

		/*
		 * ValidationUtils.checkIfAmountValid( p_orderLine.getOrderLineTotal(),
		 * ORDER_LINE_TOTAL, AUTHREVERSAL_INVALID_VALUE, AUTH_REVERSAL_INVOICE,
		 * p_validationErrors);
		 */

		if (p_isMilstarBucket) {

			ValidationUtils.checkIfNull(
					p_orderLine.getExtended(),
					ORDER_LINE + "." + EXTENDED, ADJUSTMENT_MANDATORY_MISSING,
					ADJUSTMENT_INVOICE, p_validationErrors
			);

			ValidationUtils.checkIfNull(
					p_orderLine.getExtended().getResponsePlan(),
					ORDER_LINE + "." + EXTENDED + "." + RESPONSE_PLAN,
					ADJUSTMENT_MANDATORY_MISSING,
					ADJUSTMENT_INVOICE, p_validationErrors
			);

			ValidationUtils.validateInvoiceLineReponsePlan(
					p_orderLine.getExtended().getResponsePlan(),
					ADJUSTMENT_INVOICE, ADJUSTMENT_INVALID_VALUE,
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
				ADJUSTMENT_MANDATORY_MISSING, ADJUSTMENT_INVOICE,
				p_validationErrors
		);

		if (p_isMilstarBucket) {

			ValidationUtils.checkIfNull(
					p_closedInvoiceLine.getExtended(), CLOSED_INVOICE + "."
							+ INVOICE_LINE + "." + EXTENDED,
					ADJUSTMENT_MANDATORY_MISSING, ADJUSTMENT_INVOICE,
					p_validationErrors
			);

			ValidationUtils.checkIfNull(
					p_closedInvoiceLine.getExtended().getResponsePlan(),
					CLOSED_INVOICE + "." + INVOICE_LINE + "." + EXTENDED + "."
							+ RESPONSE_PLAN, ADJUSTMENT_MANDATORY_MISSING,
					ADJUSTMENT_INVOICE, p_validationErrors
			);

			ValidationUtils.validateInvoiceLineReponsePlan(
					p_closedInvoiceLine.getExtended().getResponsePlan(),
					ADJUSTMENT_INVOICE, ADJUSTMENT_INVALID_VALUE,
					p_validationErrors
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
			BigDecimal pCurrentAuthorized,
			BigDecimal pOpenInvoiceAmount
	)
	{

		// return pOpenInvoiceAmount <= pCurrentAuthorized;
		return Utils.isValueLesserOrEqual(
				pOpenInvoiceAmount,
				pCurrentAuthorized
		);
	}
	// ------------------------------------------------------------------------
}
// ----------------------------------------------------------------------------
// END OF FILE
// ----------------------------------------------------------------------------