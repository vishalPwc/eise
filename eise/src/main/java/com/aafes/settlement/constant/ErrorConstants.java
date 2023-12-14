package com.aafes.settlement.constant;

// -----------------------------------------------------------------------------
public interface ErrorConstants {

	// -------------------------------------------------------------------------
	/**
	 * This variable indicates the value for 10001constant
	 */
	public static final String INVALID_REQUEST						  = "10001";
	/**
	 * This variable indicates the value for 10002 constant
	 */

	public static final String INTERNAL_ERROR						  = "10002";
	/**
	 * The Error Constant indicates that username,password and externalSystem is
	 * mandatory for JwtAuthentication
	 * 
	 * public static final String JWT_MANDATORY_FIELDS = "90001";
	 */
	/**
	 * Error constant if the key is invalid.
	 */
	public static final String JWT_INVALID_KEY						  = "10003";

	/*------------------Settlement Validation Constants ---------------------*/

	/**
	 * This variable indicates the value for 30001 constant
	 */
	public static final String SETTLEMENT_MANDATORY_MISSING			  = "30001";

	/**
	 * This variable indicates the value for 30002 constant
	 */
	public static final String SETTLEMENT_INVALID_VALUE				  = "30002";

	/**
	 * This variable indicates the value for 30007 constant
	 */
	public static final String SETTLEMENT_INVALID_PAYMENT_METHOD	  = "30007";

	/**
	 * This variable indicates the value for 30010 constant
	 */
	public static final String SETTLEMENT_INVOICE_MORE_THAN_AUTH	  = "30010";

	/**
	 * This variable indicates the value for 30011 constant
	 */
	public static final String SETTLEMENT_PAYMENT_METHOD_DUPLICATED	  = "30011";

	/*----------------- Refund Validation Constants -------------------------*/

	/**
	 * This variable indicates the value for 31001 constant
	 */
	public static final String REFUND_MANDATORY_MISSING				  = "31001";

	/**
	 * This variable indicates the value for 31002 constant
	 */
	public static final String REFUND_INVALID_VALUE					  = "31002";

	/**
	 * This variable indicates the value for 31007 constant
	 */
	public static final String REFUND_INVALID_PAYMENT_METHOD		  = "31007";

	/**
	 * This variable indicates the value for 31010 constant
	 */
	public static final String REFUND_INVOICE_MORE_THAN_SETTLED		  = "31010";

	/**
	 * This variable indicates the value for 31011 constant
	 */
	public static final String REFUND_PAYMENT_METHOD_DUPLICATED		  = "31011";

	/*----------------- Auth Reversal Validation Constants ------------------*/

	/**
	 * This variable indicates the value for 32001 constant
	 */
	public static final String AUTHREVERSAL_MANDATORY_MISSING		  = "32001";

	/**
	 * This variable indicates the value for 32002 constant
	 */
	public static final String AUTHREVERSAL_INVALID_VALUE			  = "32002";

	/**
	 * This variable indicates the value for 32007 constant
	 */
	public static final String AUTHREVERSAL_INVALID_PAYMENT_METHOD	  = "32007";

	/**
	 * This variable indicates the value for 32010 constant
	 */
	public static final String AUTHREVERSAL_INVOICE_MORE_THAN_AUTH	  = "32010";

	/**
	 * This variable indicates the value for 32011 constant
	 */
	public static final String AUTHREVERSAL_PAYMENT_METHOD_DUPLICATED = "32011";

	/*----------------- Exchange Validation Constants -----------------------*/

	/**
	 * This variable indicates the value for 33001 constant
	 */
	public static final String EXCHANGE_MANDATORY_MISSING			  = "33001";

	/**
	 * This variable indicates the value for 33002 constant
	 */
	public static final String EXCHANGE_INVALID_VALUE				  = "33002";

	/**
	 * This variable indicates the value for 33007 constant
	 */
	public static final String EXCHANGE_INVALID_PAYMENT_METHOD		  = "33007";

	/**
	 * This variable indicates the value for 33010 constant
	 */
	public static final String EXCHANGE_INVOICE_MORE_THAN_SETTLED	  = "33010";

	/**
	 * This variable indicates the value for 33011 constant
	 */
	public static final String EXCHANGE_PAYMENT_METHOD_DUPLICATED	  = "33011";

	/*----------------- Adjustment Validation Constants ---------------------*/

	/**
	 * This variable indicates the value for 34001 constant
	 */
	public static final String ADJUSTMENT_MANDATORY_MISSING			  = "34001";

	/**
	 * This variable indicates the value for 34002 constant
	 */
	public static final String ADJUSTMENT_INVALID_VALUE				  = "34002";

	/**
	 * This variable indicates the value for 34005 constant
	 */
	public static final String ADJUSTMENT_INVALID_PLAN				  = "34005";

	/**
	 * This variable indicates the value for 34007 constant
	 */
	public static final String ADJUSTMENT_INVALID_PAYMENT_METHOD	  = "34007";

	/**
	 * This variable indicates the value for 34010 constant
	 */
	public static final String ADJUSTMENT_INVOICE_MORE_THAN_SETTLED	  = "34010";

	/**
	 * This variable indicates the value for 34011 constant
	 */
	public static final String ADJUSTMENT_PAYMENT_METHOD_DUPLICATED	  = "34011";

	/*----------------- BOPIS Cancellation Validation Constants -------------*/

	/**
	 * This variable indicates the value for 35001 constant
	 */
	public static final String BOPIS_MANDATORY_MISSING				  = "35001";

	/**
	 * This variable indicates the value for 35002 constant
	 */
	public static final String BOPIS_INVALID_VALUE					  = "35002";

	/**
	 * This variable indicates the value for 35007 constant
	 */
	public static final String BOPIS_INVALID_PAYMENT_METHOD			  = "35007";

	/**
	 * This variable indicates the value for 35010 constant
	 */
	public static final String BOPIS_INVOICE_MORE_THAN_SETTLED		  = "35010";

	/**
	 * This variable indicates the value for 35011 constant
	 */
	public static final String BOPIS_PAYMENT_METHOD_DUPLICATED		  = "35011";

	/**
	 * The Error Constant indicates that Date fields are invalid
	 */
	public static final String REPORT_INVALID_DATE_FIELDS			  = "36001";

	/**
	 * The Error Constant indicates that Invoice Id is mandatory for fetching
	 * Report details from Database
	 */
	public static final String REPORT_ID_MANDATORY					  = "36002";

	/**
	 * The Error Constant indicates that Invoice Id is invalid
	 */
	public static final String REPORT_ID_INVALID					  = "36003";
	// -------------------------------------------------------------------------
}
// -----------------------------------------------------------------------------
// END OF FILE
// -----------------------------------------------------------------------------