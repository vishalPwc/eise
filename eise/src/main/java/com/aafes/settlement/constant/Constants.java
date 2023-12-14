package com.aafes.settlement.constant;

import java.math.BigDecimal;

public interface Constants {
	/**
	 * This variable indicates the value for EISE constant
	 */
	public static final String	   EISE					 = "eISE";
	/**
	 * This variable indicates the value for Shipment constant
	 */
	public static final String	   SHIPPED_INVOICE		 = "Shipment";
	/**
	 * This variable indicates the value for Return constant
	 */
	public static final String	   RETURN_INVOICE		 = "Return";
	/**
	 * This variable indicates the value for AuthReversal constant
	 */
	public static final String	   AUTH_REVERSAL_INVOICE = "AuthReversal";
	/**
	 * This variable indicates the value for Exchange constant
	 */
	public static final String	   EXCHANGE_INVOICE		 = "Exchange";
	/**
	 * This variable indicates the value for Uneven Exchange constant
	 */
	public static final String	   UNEVEN_EXCHANGE_INVOICE= "UnevenExchange";
	/**
	 * This variable indicates the value for Adjustment constant
	 */
	public static final String	   ADJUSTMENT_INVOICE	 = "Adjustment";
	/**
	 * This variable indicates the value for Cancellation constant
	 */
	public static final String	   CANCELLED_INVOICE	 = "Cancellation";
	/**
	 * This variable indicates the value for Shipping constant
	 */
	public static final String	   SHIPPING_CHARGES		 = "Shipping";
	/**
	 * This variable indicates the value for Appeasement constant
	 */
	public static final String	   APPEASEMENT_CHARGE	 = "Appeasement";
	/**
	 * This variable indicates the value for Settlement constant
	 */
	public static final String	   SETTLEMENT			 = "Settlement";
	/**
	 * This variable indicates the value for Refund constant
	 */
	public static final String	   REFUND				 = "Refund";
	/**
	 * This variable indicates the value for Return Credit constant
	 */
	public static final String	   RETURN_CREDIT		 = "Return Credit";
	/**
	 * This variable indicates the value for 20001 constant
	 */
	public static final String	   PLAN_MILITARY_UNIFORM = "20001";
	/**
	 * This variable indicates the value for 10001 constant
	 */
	public static final String	   PLAN_MILITARY_RETAIL	 = "10001";
	/**
	 * This variable indicates the value for Milstar constant
	 */
	public static final String	   MILSTAR_CARD			 = "Milstar";
	/**
	 * This variable indicates the value for Credit Card constant
	 */
	public static final String	   CREDIT_CARD			 = "Credit Card";
	/**
	 * This variable indicates the value for Gift Card constant
	 */
	public static final String	   GIFT_CARD			 = "Gift Card";
	/**
	 * This variable indicates the value for SUCCESS constant
	 */
	public static final String	   SUCCESS				 = "SUCCESS";
	/**
	 * This variable indicates the value for FAILURE constant
	 */
	public static final String	   FAILURE				 = "FAILURE";
	/**
	 * The variable indicates the default ISO time format without separator
	 * value for the Utils.
	 */
	public static final String	   ISO_TIME_FORMAT		 = "yyyy-MM-dd HH:mm:ss";
	/**
	 * The variable indicates the default date format without separator value
	 * for the Utils.
	 */
	public static final String	   ISO_DATE_FORMAT		 = "yyyy-MM-dd";
	/**
	 * This variable is the CDT time zone id
	 */
	public static final String	   CDT_TIME_ZONE		 = "CST6CDT";
	/**
	 * This variable is the mao token separator
	 */
	public static final String	   MAO_TOKEN_SEPARATOR	 = ":";
	/**
	 * This variable is IS_SETTLED BigDecimal initialization
	 */
	public static final BigDecimal IS_SETTLED			 = new BigDecimal(0.0);
	/**
	 * This variable is IS_FINISHED BigDecimal initialization
	 */
	public static final BigDecimal IS_FINISHED			 = new BigDecimal(0.0);
	/**
	 * This variable is ZERO_SETTLED BigDecimal initialization
	 */
	public static final BigDecimal ZERO_SETTLED			 = new BigDecimal(0.0);
	/**
	 * This variable is ZERO_VALUE BigDecimal initialization
	 */

	public static final BigDecimal ZERO_VALUE			 = new BigDecimal(0.00);
	/**
	 * This variable is MINUS_ONE_VALUE BigDecimal initialization
	 */

	public static final BigDecimal MINUS_ONE_VALUE		 = new BigDecimal(-1.0);

	// ----------------------Validation Constants------------------------------

	/**
	 * This variable indicates the value for FAILURE constant
	 */
	public static final String	   ORDER_ID				 = "OrderId";

	/**
	 * This variable indicates the value for PaymentHeader constant
	 */

	public static final String	   PAYMENT_HEADER		 = "PaymentHeader";

	/**
	 * This variable indicates the value for PaymentMethod constant
	 */

	public static final String	   PAYMENT_METHOD		 = "PaymentMethod";

	/**
	 * This variable indicates the value for OrderLine constant
	 */

	public static final String	   ORDER_LINE			 = "OrderLine";

	/**
	 * This variable indicates the value for OrderLineTotal constant
	 */

	public static final String	   ORDER_LINE_TOTAL		 = "OrderLineTotal";

	/**
	 * This variable indicates the value for OrderTotal constant
	 */

	public static final String	   ORDER_TOTAL		 = "OrderTotal";

	/**
	 * This variable indicates the value for ParentOrder constant
	 */

	public static final String	   PARENT_ORDER			 = "ParentOrder";

	/**
	 * This variable indicates the value for OpenInvoice constant
	 */

	public static final String	   OPEN_INVOICE			 = "OpenInvoice";

	/**
	 * This variable indicates the value for ClosedInvoice constant
	 */

	public static final String	   CLOSED_INVOICE		 = "ClosedInvoice";

	/**
	 * This variable indicates the value for OpenAuthReversal constant
	 */

	public static final String	   OPEN_AUTH_REVERSAL	 = "OpenAuthReversal";

	/**
	 * This variable indicates the value for InvoiceId constant
	 */

	public static final String	   INVOICE_ID			 = "InvoiceId";

	/**
	 * This variable indicates the value for InvoiceType constant
	 */

	public static final String	   INVOICE_TYPE			 = "InvoiceType";

	/**
	 * This variable indicates the value for InvoiceTypeId constant
	 */

	public static final String	   INVOICE_TYPE_ID		 = "InvoiceTypeId";

	/**
	 * This variable indicates the value for InvoiceLine constant
	 */

	public static final String	   INVOICE_LINE			 = "InvoiceLine";

	/**
	 * This variable indicates the value for InvoiceLineId constant
	 */

	public static final String	   INVOICE_LINE_ID		 = "InvoiceLineId";

	/**
	 * This variable indicates the value for InvoiceLineTotal constant
	 */

	public static final String	   INVOICE_LINE_TOTAL	 = "InvoiceLineTotal";

	/**
	 * This variable indicates the value for PaymentMethodId constant
	 */

	public static final String	   PAYMENT_METHOD_ID	 = "PaymentMethodId";

	/**
	 * This variable indicates the value for PaymentType constant
	 */

	public static final String	   PAYMENT_TYPE			 = "PaymentType";

	/**
	 * This variable indicates the value for PaymentTypeId constant
	 */

	public static final String	   PAYMENT_TYPE_ID		 = "PaymentTypeId";

	/**
	 * This variable indicates the value for CardType constant
	 */

	public static final String	   CARD_TYPE			 = "CardType";

	/**
	 * This variable indicates the value for CardTypeId constant
	 */

	public static final String	   CARD_TYPE_ID			 = "CardTypeId";

	/**
	 * This variable indicates the value for CurrentAuthAmount constant
	 */

	public static final String	   CURRENT_AUTH_AMT		 = "CurrentAuthAmount";

	/**
	 * This variable indicates the value for CurrentSettledAmount constant
	 */

	public static final String	   CURRENT_SETTLED_AMT	 = "CurrentSettledAmount";

	/**
	 * This variable indicates the value for Extended constant
	 */

	public static final String	   EXTENDED				 = "Extended";

	/**
	 * This variable indicates the value for ResponsePlan constant
	 */

	public static final String	   RESPONSE_PLAN		 = "ResponsePlan";

	/**
	 * This variable indicates the value for Amount constant
	 */
	public static final String	   AMOUNT				 = "Amount";

	/**
	 * This variable indicates the value for PaymentTransactionId constant
	 */

	public static final String	   PAYMENT_TXN_ID		 = "PaymentTransactionId";

	/**
	 * This variable indicates the value for InvoiceChargeDetail constant
	 */

	public static final String	   INVOICE_CHARGE_DETAIL = "InvoiceChargeDetail";

	/**
	 * This variable indicates the value for ChargeDetailId constant
	 */

	public static final String	   CHARGE_DETAIL_ID		 = "ChargeDetailId";

	/**
	 * This variable indicates the value for ChargeType constant
	 */

	public static final String	   CHARGE_TYPE			 = "ChargeType";

	/**
	 * This variable indicates the value for ChargeTypeId constant
	 */

	public static final String	   CHARGE_TYPE_ID		 = "ChargeTypeId";

	/**
	 * This variable indicates the value for ChargeTotal constant
	 */

	public static final String	   CHARGE_TOTAL			 = "ChargeTotal";

	/**
	 * This variable indicates the value for pageNumber constant
	 */

	public static final String	   PAGE_NUMBER			 = "pageNumber";

	/**
	 * This variable indicates the value for Oldest First constant
	 */

	public static final String	   REPORT_ORDERBY_ASC	 = "Oldest First";

	// ------------------------------------------------------------------------
}
// ----------------------------------------------------------------------------
// END OF FILE
// ----------------------------------------------------------------------------