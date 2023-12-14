package com.aafes.settlement.validators;

import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.aafes.settlement.constant.Constants;
import com.aafes.settlement.constant.ErrorConstants;
import com.aafes.settlement.core.invoice.OpenInvoice;
import com.aafes.settlement.core.model.reversal.ReversalPaymentMethod;
import com.aafes.settlement.core.payment.PaymentHeader;
import com.aafes.settlement.core.payment.PaymentMethod;
import com.aafes.settlement.exception.NodeNotFoundException;
import com.aafes.settlement.util.Utils;

public class ValidationUtils
	implements Constants, ErrorConstants
{
	// ------------------------------------------------------------------------
	/** information logger **/
	private static final Logger LOGGER = LogManager
			.getLogger(ValidationUtils.class);

	// ------------------------------------------------------------------------
	/**
	 * This method checks if the object passed in parameter is null.
	 * <p>
	 * If true then it throws NodeNotFoundException with error code :
	 * p_errorCode for request : p_requestType.
	 * 
	 * @param p_object
	 * @param p_fieldToBeChecked
	 * @param p_errorCode
	 * @param p_requestType
	 * @param p_errorObj
	 * @throws NodeNotFoundException
	 */
	public static void checkIfNull(
			Object p_object,
			String p_fieldToBeChecked,
			String p_errorCode,
			String p_requestType,
			ValidationErrors p_errorObj
	)
			throws NodeNotFoundException
	{

		if (Utils.isNull(p_object)) {
			p_errorObj.errors.put(p_errorCode, p_fieldToBeChecked);
			p_errorObj.isValid = false;
			LOGGER.error(
					"Required node : " + p_fieldToBeChecked
							+ "not found for " + p_requestType
			);
			throw new NodeNotFoundException(
					"Required Node Not Found: " + p_fieldToBeChecked,
					p_errorObj
			);
		}
	}

	// ------------------------------------------------------------------------
	/**
	 * This method checks if the amount is valid.
	 * 
	 * @param p_amount
	 * @param p_fieldToBeChecked
	 * @param p_errorObj
	 * @throws NodeNotFoundException
	 *             if the amount is invalid
	 */
	public static void checkIfAmountValid(
			BigDecimal p_amount,
			String p_fieldToBeChecked,
			String p_errorCode,
			String p_requestType,
			ValidationErrors p_errorObj
	)
			throws NodeNotFoundException
	{

		if (
			!validateRequiredAmount(
					p_amount, p_fieldToBeChecked,
					p_requestType
			)
		)
		{
			p_errorObj.errors.put(p_errorCode, p_fieldToBeChecked);
			p_errorObj.isValid = false;
			throw new NodeNotFoundException(
					"Node/Attribute : "
							+ p_fieldToBeChecked + " has invalid value: "
							+ p_amount
							+ " for request" + p_requestType, p_errorObj
			);
		}
	}

	// ------------------------------------------------------------------------
	/**
	 * Check if current auth attribute total is greater than 0
	 * 
	 */
	private static boolean validateRequiredAmount(
			BigDecimal p_amount,
			String p_fieldToBeChecked,
			String p_requestType
	)
	{
		LOGGER.debug(
				p_requestType
						+ " Request Valdation - validateRequiredAmount() "
						+ ":: Check "
						+ p_fieldToBeChecked + " attribute value is more than 0"
		);
		// return p_amount > 0;
		return Utils.isValueGreater(p_amount, new BigDecimal(0.00));
	}

	// ------------------------------------------------------------------------
	/**
	 * This method checks if Response plan is correct or not
	 * 
	 * @param p_paymentMethod
	 *            : object of ReversalPaymentMethod class
	 * @param p_validationErrors
	 * @param isMRCardPresent
	 * @param isMUCardPresent
	 */
	public static void validateResponePlan(
			PaymentMethod p_paymentMethod, ValidationErrors p_validationErrors,
			AtomicBoolean isMRCardPresent, AtomicBoolean isMUCardPresent,
			String p_DuplicatePaymentMethodErrorCode,
			String p_InvalidPaymentMethodErrorCode,
			String p_requestType
	)
	{

		if (
			p_paymentMethod.getExtended().getResponsePlan()
					.equals(PLAN_MILITARY_RETAIL)
		)
		{
			if (!isMRCardPresent.get()) {
				isMRCardPresent.set(true);
			} else {

				/*
				 * p_validationErrors.errors.put(
				 * p_DuplicatePaymentMethodErrorCode, p_paymentMethod
				 * .getPaymentMethodId() ); LOGGER.error(
				 * "Duplicate Milstar card (Retail Line): " +
				 * p_paymentMethod.getPaymentMethodId() +
				 * "found for request type" + p_requestType );
				 * 
				 * throw new NodeNotFoundException(
				 * "Duplicate Milstar card (Retail Line) found: " +
				 * p_paymentMethod.getPaymentMethodId(), p_validationErrors );
				 */
			}
		} else if (
			p_paymentMethod.getExtended().getResponsePlan()
					.equals(PLAN_MILITARY_UNIFORM)
		)
		{
			if (!isMUCardPresent.get()) {
				isMUCardPresent.set(true);
			} else {
				/*
				 * p_validationErrors.errors.put(
				 * p_DuplicatePaymentMethodErrorCode, p_paymentMethod
				 * .getPaymentMethodId() ); LOGGER.error(
				 * "Duplicate Milstar card (Retail Line): " +
				 * p_paymentMethod.getPaymentMethodId() +
				 * "found for request type" + p_requestType ); throw new
				 * NodeNotFoundException(
				 * "Duplicate Milstar card (Uniform Line) found: " +
				 * p_paymentMethod.getPaymentMethodId(), p_validationErrors );
				 */
			}
		} else {

			p_validationErrors.errors.put(
					p_InvalidPaymentMethodErrorCode, p_paymentMethod
							.getExtended()
							.getResponsePlan()
			);
			LOGGER.error(
					"Invalid PaymentMethod Plan :"
							+ p_paymentMethod.getExtended()
									.getResponsePlan()
							+ "found for request type" + p_requestType
			);
			throw new NodeNotFoundException(
					"Invalid PaymentMethod Plan :"
							+ p_paymentMethod.getExtended()
									.getResponsePlan(), p_validationErrors
			);
		}

	}

	// ------------------------------------------------------------------------
	/**
	 * This method checks if Response plan is correct or not
	 * 
	 * @param p_paymentMethod
	 *            : object of ReversalPaymentMethod class
	 * @param p_validationErrors
	 * @param p_isMRCardPresent
	 * @param p_isMUCardPresent
	 */
	public static void validateResponePlan(
			ReversalPaymentMethod p_paymentMethod,
			ValidationErrors p_validationErrors,
			AtomicBoolean isMRCardPresent, AtomicBoolean isMUCardPresent,
			String p_DuplicatePaymentMethodErrorCode,
			String p_InvalidPaymentMethodErrorCode,
			String p_requestType
	)
	{

		if (
			p_paymentMethod.getExtended().getResponsePlan()
					.equals(PLAN_MILITARY_RETAIL)
		)
		{
			if (!isMRCardPresent.get()) {
				isMRCardPresent.set(true);
			} else {

				p_validationErrors.errors.put(
						p_DuplicatePaymentMethodErrorCode, p_paymentMethod
								.getPaymentMethodId()
				);
				LOGGER.error(
						"Duplicate Milstar card (Retail Line): "
								+ p_paymentMethod.getPaymentMethodId()
								+ "found for request type" + p_requestType
				);

				throw new NodeNotFoundException(
						"Duplicate Milstar card (Retail Line) found: "
								+ p_paymentMethod.getPaymentMethodId(),
						p_validationErrors
				);
			}
		} else if (
			p_paymentMethod.getExtended().getResponsePlan()
					.equals(PLAN_MILITARY_UNIFORM)
		)
		{
			if (!isMUCardPresent.get()) {
				isMUCardPresent.set(true);
			} else {
				p_validationErrors.errors.put(
						p_DuplicatePaymentMethodErrorCode, p_paymentMethod
								.getPaymentMethodId()
				);
				LOGGER.error(
						"Duplicate Milstar card (Retail Line): "
								+ p_paymentMethod.getPaymentMethodId()
								+ "found for request type" + p_requestType
				);
				throw new NodeNotFoundException(
						"Duplicate Milstar card (Uniform Line) found: "
								+ p_paymentMethod.getPaymentMethodId(),
						p_validationErrors
				);
			}
		} else {

			p_validationErrors.errors.put(
					p_InvalidPaymentMethodErrorCode, p_paymentMethod
							.getExtended()
							.getResponsePlan()
			);
			LOGGER.error(
					"Invalid PaymentMethod Plan :"
							+ p_paymentMethod.getExtended()
									.getResponsePlan()
							+ "found for request type" + p_requestType
			);
			throw new NodeNotFoundException(
					"Invalid PaymentMethod Plan :"
							+ p_paymentMethod.getExtended()
									.getResponsePlan(), p_validationErrors
			);
		}

	}

	// ------------------------------------------------------------------------
	public static void validateAtLeastOneInOpenInvoice(
			OpenInvoice p_currentOpenInvoice, String p_errorCode,
			ValidationErrors p_validationErrors
	)
	{
		if (
			Utils.isNull(p_currentOpenInvoice.getInvoiceLine())
					&& Utils.isNull(
							p_currentOpenInvoice.getInvoiceChargeDetail()
					)
		)
		{
			p_validationErrors.errors
					.put(
							p_errorCode,
							"At least one of " + OPEN_INVOICE + "."
									+ INVOICE_LINE + " or " +
									OPEN_INVOICE + "." + INVOICE_CHARGE_DETAIL
									+ " is mandatory."
					);

			LOGGER.error(
					"At least one of " + OPEN_INVOICE + "." + INVOICE_LINE
							+ " or " +
							OPEN_INVOICE + "." + INVOICE_CHARGE_DETAIL
							+ " is mandatory."
			);

			throw new NodeNotFoundException(
					"At least one of " + OPEN_INVOICE + "." + INVOICE_LINE
							+ " or " +
							OPEN_INVOICE + "." + INVOICE_CHARGE_DETAIL
							+ " is mandatory.", p_validationErrors
			);
		}
	}

	// ------------------------------------------------------------------------
	public static void validateInvoiceLineReponsePlan(
			String p_responsePlan, String p_requestType, String p_errorCode,
			ValidationErrors p_validationErrors
	)
	{

		if (
			!(p_responsePlan.equalsIgnoreCase(PLAN_MILITARY_UNIFORM)
					|| p_responsePlan.equalsIgnoreCase(PLAN_MILITARY_RETAIL))
		)
		{
			p_validationErrors.errors
					.put(
							p_errorCode,
							INVOICE_LINE + "." + EXTENDED + "." + RESPONSE_PLAN
									+ " with value: \'" + p_responsePlan + "\'"
					);

			LOGGER.error(
					"Invalid Invoice/Order line ResponsePlan :" + p_responsePlan
							+ "found for request type" + p_requestType
			);

			throw new NodeNotFoundException(
					"Invalid Invoice/Order line ResponsePlan :"
							+ p_responsePlan, p_validationErrors
			);
		}
	}

	// ------------------------------------------------------------------------
	public static void validateLengthOfPaymentHeader(
			List<PaymentHeader> p_paymentHeader, String p_requestType,
			String p_errorCode, ValidationErrors p_validationErrors
	)
	{
		if (p_paymentHeader.size() > 1) {

			p_validationErrors.errors
					.put(
							p_errorCode,
							"Multiple PaymentHeader nodes found"
					);

			LOGGER.error("Multiple PaymentHeader nodes found");

			throw new NodeNotFoundException(
					"Multiple PaymentHeader nodes found", p_validationErrors
			);
		}
	}
}
// ----------------------------------------------------------------------------
// END OF FILE
// ----------------------------------------------------------------------------