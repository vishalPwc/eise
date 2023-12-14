/**
 * 
 */
package com.aafes.settlement.validators;

import java.math.BigDecimal;
import java.util.concurrent.atomic.AtomicBoolean;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.aafes.settlement.constant.Constants;
import com.aafes.settlement.constant.ErrorConstants;
import com.aafes.settlement.core.invoice.InvoiceChargeDetail;
import com.aafes.settlement.core.invoice.InvoiceLine;
import com.aafes.settlement.core.invoice.OpenInvoice;
import com.aafes.settlement.core.model.cancelled.CancelledRO;
import com.aafes.settlement.core.order.OrderLine;
import com.aafes.settlement.core.payment.PaymentHeader;
import com.aafes.settlement.core.payment.PaymentMethod;
import com.aafes.settlement.exception.NodeNotFoundException;
import com.aafes.settlement.util.Utils;

// ----------------------------------------------------------------------------

/**
 * Class implements methods to be used for Cancelled Request validation
 */
public class CancelledValidator
	implements
	Validator,
	Constants,
	ErrorConstants
{
	// ------------------------------------------------------------------------

	/** information logger **/
	private static final Logger LOGGER = LogManager
			.getLogger(CancelledValidator.class);

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
	 * check if node is available in request
	 * 
	 * @param p_cancelledRequestNode
	 * @param p_errorObj
	 * @return boolean
	 */
	@Override
	public boolean isNodeAvailable(
			Object p_cancelledRequestNode,
			ValidationErrors p_errorObj
	)
	{

		CancelledRO p_cancelledRequest = (CancelledRO) p_cancelledRequestNode;

		LOGGER.info(
				"Cancelled Request Valdation - isNodeAvailable() "
						+ ":: Check  Payment Header"
		);

		ValidationUtils.checkIfNull(
				p_cancelledRequest.getPaymentHeader(),
				PAYMENT_HEADER, BOPIS_MANDATORY_MISSING, CANCELLED_INVOICE,
				p_errorObj
		);

		LOGGER.info(
				"Exchange Request Valdation - isNodeAvailable() :: "
						+ "Check if there are multiple Payment Headers"
		);

		ValidationUtils.validateLengthOfPaymentHeader(
				p_cancelledRequest.getPaymentHeader(),
				CANCELLED_INVOICE, BOPIS_INVALID_VALUE, p_errorObj
		);

		LOGGER.info(
				"Cancelled Request Valdation - isNodeAvailable() "
						+ ":: Check Order Lines"
		);

		ValidationUtils.checkIfNull(
				p_cancelledRequest.getOrderLine(),
				ORDER_LINE, BOPIS_MANDATORY_MISSING, CANCELLED_INVOICE,
				p_errorObj
		);

		LOGGER.info(
				"Cancelled Request Valdation - isNodeAvailable() "
						+ ":: Check Open Invoices"
		);

		ValidationUtils.checkIfNull(
				p_cancelledRequest.getOpenInvoice(),
				OPEN_INVOICE, BOPIS_MANDATORY_MISSING, CANCELLED_INVOICE,
				p_errorObj
		);

		LOGGER.info(
				"Cancelled Request Valdation - isNodeAvailable() :: "
						+ "Check Order ID"
		);

		ValidationUtils.checkIfNull(
				p_cancelledRequest.getOrderId(), ORDER_ID,
				BOPIS_MANDATORY_MISSING, CANCELLED_INVOICE, p_errorObj
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
				PAYMENT_METHOD, BOPIS_INVALID_VALUE, CANCELLED_INVOICE,
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
				PAYMENT_METHOD_ID, BOPIS_MANDATORY_MISSING, CANCELLED_INVOICE,
				p_validationErrors
		);

		ValidationUtils.checkIfNull(
				p_paymentMethod.getPaymentType(),
				PAYMENT_TYPE, BOPIS_MANDATORY_MISSING, CANCELLED_INVOICE,
				p_validationErrors
		);

		ValidationUtils.checkIfNull(
				p_paymentMethod.getPaymentType().getPaymentTypeId(),
				PAYMENT_TYPE_ID, BOPIS_MANDATORY_MISSING, CANCELLED_INVOICE,
				p_validationErrors
		);

		ValidationUtils.checkIfNull(
				p_paymentMethod.getCurrentSettledAmount(),
				CURRENT_SETTLED_AMT, BOPIS_MANDATORY_MISSING, CANCELLED_INVOICE,
				p_validationErrors
		);

		ValidationUtils.checkIfNull(
				p_paymentMethod.getCardType(), CARD_TYPE,
				BOPIS_MANDATORY_MISSING, CANCELLED_INVOICE, p_validationErrors
		);

		ValidationUtils.checkIfNull(
				p_paymentMethod.getCardType().getCardTypeId(), CARD_TYPE_ID,
				BOPIS_MANDATORY_MISSING, CANCELLED_INVOICE, p_validationErrors
		);

		if (
			p_paymentMethod.getCardType().getCardTypeId()
					.equalsIgnoreCase(MILSTAR_CARD)
		)
		{
			ValidationUtils.checkIfNull(
					p_paymentMethod.getExtended(), PAYMENT_METHOD + "." + EXTENDED,
					BOPIS_MANDATORY_MISSING, CANCELLED_INVOICE,
					p_validationErrors
			);

			ValidationUtils.checkIfNull(
					p_paymentMethod.getExtended().getResponsePlan(),
					PAYMENT_METHOD + "." + EXTENDED + "." + RESPONSE_PLAN, BOPIS_MANDATORY_MISSING,
					CANCELLED_INVOICE, p_validationErrors
			);

			ValidationUtils.validateResponePlan(
					p_paymentMethod, p_validationErrors, p_isMRCardPresent,
					p_isMUCardPresent, BOPIS_PAYMENT_METHOD_DUPLICATED,
					BOPIS_INVALID_PAYMENT_METHOD, CANCELLED_INVOICE
			);
		}
	}

	// ------------------------------------------------------------------------
	/**
	 * This method validates the following required nodes/attributes of
	 * OpenInvoice node:
	 * <ul>
	 * <li>InvoiceId</li>
	 * <li>InvoiceLine</li>
	 * </ul>
	 * 
	 * @param p_currentOpenInvoice
	 * @param p_validationErrors
	 * @throws NodeNotFoundException
	 *             if the required nodes/attributes are not found
	 */
	public void validateOpenInvoice(
			OpenInvoice p_currentOpenInvoice,
			ValidationErrors p_validationErrors
	)
			throws NodeNotFoundException
	{

		ValidationUtils.checkIfNull(
				p_currentOpenInvoice.getInvoiceId(),
				INVOICE_ID, BOPIS_MANDATORY_MISSING, CANCELLED_INVOICE,
				p_validationErrors
		);

		ValidationUtils.validateAtLeastOneInOpenInvoice(
				p_currentOpenInvoice, BOPIS_MANDATORY_MISSING,
				p_validationErrors
		);

		/*
		 * ValidationUtils.checkIfNull(p_currentOpenInvoice.getInvoiceLine(),
		 * INVOICE_LINE, BOPIS_MANDATORY_MISSING, CANCELLED_INVOICE,
		 * p_validationErrors);
		 */
	}

	// ------------------------------------------------------------------------
	/**
	 * This method validates the following required nodes/attributes of
	 * InvoiceLine node:
	 * <ul>
	 * <li>InvoiceLineId</li>
	 * <li>InvoiceLineTotal</li>
	 * </ul>
	 * 
	 * @param p_invoiceLine
	 * @param p_validationErrors
	 * @throws NodeNotFoundException
	 *             if the required nodes/attributes are not found
	 */
	public void validateInvoiceLine(
			InvoiceLine p_invoiceLine, ValidationErrors p_validationErrors,
			boolean p_isMilstarBucket
	)
			throws NodeNotFoundException
	{

		ValidationUtils.checkIfNull(
				p_invoiceLine.getInvoiceLineId(),
				INVOICE_LINE_ID, BOPIS_MANDATORY_MISSING, CANCELLED_INVOICE,
				p_validationErrors
		);

		ValidationUtils.checkIfNull(
				p_invoiceLine.getInvoiceLineTotal(),
				INVOICE_LINE_TOTAL, BOPIS_MANDATORY_MISSING, CANCELLED_INVOICE,
				p_validationErrors
		);

		// Fix for EOR-6867
		/*
		 * ValidationUtils.checkIfAmountValid(p_invoiceLine.getInvoiceLineTotal(
		 * ), INVOICE_LINE_TOTAL, BOPIS_MANDATORY_MISSING, CANCELLED_INVOICE,
		 * p_validationErrors);
		 */

		if (p_isMilstarBucket) {

			ValidationUtils.checkIfNull(
					p_invoiceLine.getExtended(),
					INVOICE_LINE + "." + EXTENDED, BOPIS_MANDATORY_MISSING,
					CANCELLED_INVOICE, p_validationErrors
			);

			ValidationUtils.checkIfNull(
					p_invoiceLine.getExtended().getResponsePlan(),
					INVOICE_LINE + "." + EXTENDED + "." + RESPONSE_PLAN,
					BOPIS_MANDATORY_MISSING,
					CANCELLED_INVOICE, p_validationErrors
			);

			ValidationUtils.validateInvoiceLineReponsePlan(
					p_invoiceLine.getExtended().getResponsePlan(),
					CANCELLED_INVOICE, BOPIS_INVALID_VALUE,
					p_validationErrors
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
					CHARGE_DETAIL_ID, BOPIS_MANDATORY_MISSING,
					CANCELLED_INVOICE,
					p_validationErrors
			);

			ValidationUtils.checkIfNull(
					p_invoiceChargeDetail.getChargeTotal(),
					CHARGE_TOTAL, BOPIS_MANDATORY_MISSING, CANCELLED_INVOICE,
					p_validationErrors
			);

			return true;
		} else {
			return false;
		}
	}

	// ------------------------------------------------------------------------
	public void validateOrderLines(
			OrderLine p_orderLine, ValidationErrors p_validationErrors,
			boolean p_isMilstarBucket
	)
			throws NodeNotFoundException
	{

		ValidationUtils.checkIfNull(
				p_orderLine.getOrderLineTotal(),
				ORDER_LINE_TOTAL, BOPIS_MANDATORY_MISSING, CANCELLED_INVOICE,
				p_validationErrors
		);

		// Fix for EOR-6867
		/*
		 * ValidationUtils.checkIfAmountValid(p_orderLine.getOrderLineTotal(),
		 * ORDER_LINE_TOTAL, BOPIS_MANDATORY_MISSING, CANCELLED_INVOICE,
		 * p_validationErrors);
		 */

		if (p_isMilstarBucket) {

			ValidationUtils.checkIfNull(
					p_orderLine.getExtended(),
					ORDER_LINE + "." + EXTENDED, BOPIS_MANDATORY_MISSING,
					CANCELLED_INVOICE, p_validationErrors
			);

			ValidationUtils.checkIfNull(
					p_orderLine.getExtended().getResponsePlan(),
					ORDER_LINE + "." + EXTENDED + "." + RESPONSE_PLAN,
					BOPIS_MANDATORY_MISSING,
					CANCELLED_INVOICE, p_validationErrors
			);

			ValidationUtils.validateInvoiceLineReponsePlan(
					p_orderLine.getExtended().getResponsePlan(),
					CANCELLED_INVOICE, BOPIS_INVALID_VALUE,
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
				BOPIS_MANDATORY_MISSING, CANCELLED_INVOICE, p_validationErrors
		);

		if (p_isMilstarBucket) {

			ValidationUtils.checkIfNull(
					p_closedInvoiceLine.getExtended(), CLOSED_INVOICE + "."
							+ INVOICE_LINE + "." + EXTENDED,
					BOPIS_MANDATORY_MISSING, CANCELLED_INVOICE,
					p_validationErrors
			);

			ValidationUtils.checkIfNull(
					p_closedInvoiceLine.getExtended().getResponsePlan(),
					CLOSED_INVOICE + "." + INVOICE_LINE + "." + EXTENDED + "."
							+ RESPONSE_PLAN, BOPIS_MANDATORY_MISSING,
					CANCELLED_INVOICE, p_validationErrors
			);

			ValidationUtils.validateInvoiceLineReponsePlan(
					p_closedInvoiceLine.getExtended().getResponsePlan(),
					CANCELLED_INVOICE, BOPIS_INVALID_VALUE, p_validationErrors
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