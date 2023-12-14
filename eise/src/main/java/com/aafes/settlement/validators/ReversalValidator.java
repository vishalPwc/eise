/**
 * 
 */
package com.aafes.settlement.validators;

import java.math.BigDecimal;
import java.util.concurrent.atomic.AtomicBoolean;

// ----------------------------------------------------------------------------
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.aafes.settlement.constant.Constants;
import com.aafes.settlement.constant.ErrorConstants;
import com.aafes.settlement.core.invoice.InvoiceLine;
import com.aafes.settlement.core.invoice.OpenAuthReversal;
import com.aafes.settlement.core.model.reversal.ReversalPaymentMethod;
import com.aafes.settlement.core.model.reversal.ReversalRO;
import com.aafes.settlement.core.order.OrderLine;
import com.aafes.settlement.exception.NodeNotFoundException;
import com.aafes.settlement.util.Utils;

// ----------------------------------------------------------------------------

/**
 * Class implements methods to be used for Reversal Request validation
 */
public class ReversalValidator
	implements Validator, Constants, ErrorConstants
{
	// ------------------------------------------------------------------------
	/** information logger **/
	private static final Logger LOGGER = LogManager
			.getLogger(ReversalValidator.class);

	// ------------------------------------------------------------------------
	/**
	 * Validate Reversal Request Node Before Processing All required Nodes will
	 * be checked for availability
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
	 * @param p_reversalRequestNode
	 * @param p_errorObj
	 * @return boolean
	 */
	@Override
	public boolean isNodeAvailable(
			Object p_reversalRequestNode,
			ValidationErrors p_errorObj
	)
	{
		ReversalRO p_reversalRequest = (ReversalRO) p_reversalRequestNode;

		LOGGER.info(
				"Reversal Request Valdation - isNodeAvailable() :: "
						+ "Check  Payment Header"
		);
		ValidationUtils.checkIfNull(
				p_reversalRequest.getPaymentHeader(),
				PAYMENT_HEADER, AUTHREVERSAL_MANDATORY_MISSING,
				AUTH_REVERSAL_INVOICE, p_errorObj
		);

		LOGGER.info(
				"Reversal Request Valdation - isNodeAvailable() :: "
						+ "Check if there are multiple Payment Headers"
		);

		if (p_reversalRequest.getPaymentHeader().size() > 1) {
			p_errorObj.errors
					.put(
							AUTHREVERSAL_INVALID_VALUE,
							"Multiple PaymentHeader nodes found"
					);
			LOGGER.error("Multiple PaymentHeader nodes found");
			throw new NodeNotFoundException(
					"Multiple PaymentHeader nodes found", p_errorObj
			);
		}

		LOGGER.info(
				"Reversal Request Valdation - isNodeAvailable() :: "
						+ "Check Order Lines"
		);
		ValidationUtils.checkIfNull(
				p_reversalRequest.getOrderLine(),
				ORDER_LINE, AUTHREVERSAL_MANDATORY_MISSING,
				AUTH_REVERSAL_INVOICE, p_errorObj
		);

		LOGGER.info(
				"Reversal Request Valdation - isNodeAvailable() :: "
						+ "Check OpenAuthReversal Request"
		);
		ValidationUtils.checkIfNull(
				p_reversalRequest.getOpenAuthReversal(),
				OPEN_AUTH_REVERSAL, AUTHREVERSAL_MANDATORY_MISSING,
				AUTH_REVERSAL_INVOICE, p_errorObj
		);

		LOGGER.info(
				"Reversal Request Valdation - isNodeAvailable() :: "
						+ "Check Order ID"
		);
		ValidationUtils.checkIfNull(
				p_reversalRequest.getOrderId(), ORDER_ID,
				AUTHREVERSAL_MANDATORY_MISSING, AUTH_REVERSAL_INVOICE,
				p_errorObj
		);

		return p_errorObj.isValid;
	}

	// ------------------------------------------------------------------------

	/**
	 * Method will validate Auth reversal request
	 * 
	 * @param p_currentOpenInvoice
	 * @param p_validationErrors
	 * @return throws NodeNotFoundException
	 */

	public void validateOpenAuth(
			OpenAuthReversal p_currentOpenInvoice,
			ValidationErrors p_validationErrors
	)
			throws NodeNotFoundException
	{
		ValidationUtils.checkIfNull(
				p_currentOpenInvoice.getAmount(), AMOUNT,
				AUTHREVERSAL_MANDATORY_MISSING, AUTH_REVERSAL_INVOICE,
				p_validationErrors
		);

		ValidationUtils.checkIfNull(
				p_currentOpenInvoice.getPaymentTransId(),
				PAYMENT_TXN_ID, AUTHREVERSAL_MANDATORY_MISSING,
				AUTH_REVERSAL_INVOICE, p_validationErrors
		);
	}

	// ------------------------------------------------------------------------

	/**
	 * Method will validate the payment method
	 * 
	 * @param p_paymentMethod
	 * @param p_validationErrors
	 * @param p_isMUCardPresent
	 * @param p_isMRCardPresent
	 * @return throws NodeNotFoundException
	 */

	public void validatePaymentMethod(
			ReversalPaymentMethod p_paymentMethod,
			ValidationErrors p_validationErrors,
			AtomicBoolean p_isMUCardPresent, AtomicBoolean p_isMRCardPresent
	)
			throws NodeNotFoundException
	{

		ValidationUtils.checkIfNull(
				p_paymentMethod.getPaymentMethodId(),
				PAYMENT_METHOD_ID, AUTHREVERSAL_MANDATORY_MISSING,
				AUTH_REVERSAL_INVOICE, p_validationErrors
		);

		ValidationUtils.checkIfNull(
				p_paymentMethod.getPaymentType(),
				PAYMENT_TYPE, AUTHREVERSAL_MANDATORY_MISSING,
				AUTH_REVERSAL_INVOICE, p_validationErrors
		);

		ValidationUtils.checkIfNull(
				p_paymentMethod.getPaymentType().getPaymentTypeId(),
				PAYMENT_TYPE_ID, AUTHREVERSAL_MANDATORY_MISSING,
				AUTH_REVERSAL_INVOICE, p_validationErrors
		);

		ValidationUtils.checkIfNull(
				p_paymentMethod.getCurrentAuthAmount(),
				CURRENT_AUTH_AMT, AUTHREVERSAL_MANDATORY_MISSING,
				AUTH_REVERSAL_INVOICE, p_validationErrors
		);

		if (
			p_paymentMethod.getCardType().getCardTypeId()
					.equalsIgnoreCase(MILSTAR_CARD)
		)
		{
			ValidationUtils.checkIfNull(
					p_paymentMethod.getExtended(), PAYMENT_METHOD + "." + EXTENDED,
					AUTHREVERSAL_MANDATORY_MISSING, AUTH_REVERSAL_INVOICE,
					p_validationErrors
			);

			ValidationUtils.checkIfNull(
					p_paymentMethod.getExtended().getResponsePlan(),
					PAYMENT_METHOD + "." + EXTENDED + "." + RESPONSE_PLAN,
					AUTHREVERSAL_MANDATORY_MISSING, AUTH_REVERSAL_INVOICE,
					p_validationErrors
			);

			ValidationUtils.validateResponePlan(
					p_paymentMethod, p_validationErrors, p_isMRCardPresent,
					p_isMUCardPresent, AUTHREVERSAL_PAYMENT_METHOD_DUPLICATED,
					AUTHREVERSAL_INVALID_PAYMENT_METHOD, AUTH_REVERSAL_INVOICE
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
				ORDER_LINE_TOTAL, AUTHREVERSAL_MANDATORY_MISSING,
				AUTH_REVERSAL_INVOICE,
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
					ORDER_LINE + "." + EXTENDED, AUTHREVERSAL_MANDATORY_MISSING,
					AUTH_REVERSAL_INVOICE, p_validationErrors
			);

			ValidationUtils.checkIfNull(
					p_orderLine.getExtended().getResponsePlan(),
					ORDER_LINE + "." + EXTENDED + "." + RESPONSE_PLAN,
					AUTHREVERSAL_MANDATORY_MISSING,
					AUTH_REVERSAL_INVOICE, p_validationErrors
			);

			ValidationUtils.validateInvoiceLineReponsePlan(
					p_orderLine.getExtended().getResponsePlan(),
					AUTH_REVERSAL_INVOICE, AUTHREVERSAL_INVALID_VALUE,
					p_validationErrors
			);
		}
	}

	// ------------------------------------------------------------------------
	public void validateClosedInvoiceLines(
			InvoiceLine p_currentLine, ValidationErrors p_validationErrors,
			boolean p_isMilstarBucket
	)
	{
		ValidationUtils.checkIfNull(
				p_currentLine.getInvoiceLineTotal(), CLOSED_INVOICE + "."
						+ INVOICE_LINE + "." + INVOICE_LINE_TOTAL,
				AUTHREVERSAL_MANDATORY_MISSING, AUTH_REVERSAL_INVOICE,
				p_validationErrors
		);

		if (p_isMilstarBucket) {

			ValidationUtils.checkIfNull(
					p_currentLine.getExtended(), CLOSED_INVOICE + "."
							+ INVOICE_LINE + "." + EXTENDED,
					AUTHREVERSAL_MANDATORY_MISSING, AUTH_REVERSAL_INVOICE,
					p_validationErrors
			);

			ValidationUtils.checkIfNull(
					p_currentLine.getExtended().getResponsePlan(),
					CLOSED_INVOICE + "." + INVOICE_LINE + "." + EXTENDED + "."
							+ RESPONSE_PLAN, AUTHREVERSAL_MANDATORY_MISSING,
					AUTH_REVERSAL_INVOICE, p_validationErrors
			);

			ValidationUtils.validateInvoiceLineReponsePlan(
					p_currentLine.getExtended().getResponsePlan(),
					AUTH_REVERSAL_INVOICE, AUTHREVERSAL_INVALID_VALUE,
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