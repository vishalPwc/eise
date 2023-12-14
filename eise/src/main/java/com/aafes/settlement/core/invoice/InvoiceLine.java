package com.aafes.settlement.core.invoice;

import java.math.BigDecimal;
// -----------------------------------------------------------------------------------
import java.util.Comparator;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

// -----------------------------------------------------------------------------------
@JsonIgnoreProperties(ignoreUnknown = true)
public class InvoiceLine
	implements Comparator<InvoiceLine>
{
	// -----------------------------------------------------------------------------------
	@JsonProperty("InvoiceLineId")
	private String					  invoiceLineId;
	@JsonProperty("ItemId")
	private String					  itemId;
	@JsonProperty("OrderLineId")
	private String					  orderLineId;
	@JsonProperty("Quantity")
	private BigDecimal				  quantity;
	@JsonProperty("OrderedQuantity")
	private BigDecimal				  orderedQuantity;
	@JsonProperty("InvoiceLineTotal")
	private BigDecimal				  invoiceLineTotal;
	@JsonProperty("Extended")
	private InvoiceLineExtended		  extended;
	@JsonProperty("InvoiceId")
	private String					  invoiceId;

	@JsonProperty("IsRefundGiftCard")
	private boolean					  isRefundGiftCard;

	@JsonProperty("InvoiceLineChargeDetail")
	private List<InvoiceChargeDetail> invoiceLineChargeDetail;

	// -----------------------------------------------------------------------------------

	/**
	 * @return the invoiceLineChargeDetail
	 */
	public List<InvoiceChargeDetail> getInvoiceLineChargeDetail() {
		return invoiceLineChargeDetail;
	}

	/**
	 * @param invoiceLineChargeDetail
	 *            the invoiceLineChargeDetail to set
	 */
	public void setInvoiceLineChargeDetail(
			List<InvoiceChargeDetail> invoiceLineChargeDetail
	)
	{
		this.invoiceLineChargeDetail = invoiceLineChargeDetail;
	}

	/**
	 * @return the extended
	 */
	public InvoiceLineExtended getExtended() {
		return extended;
	}

	/**
	 * @param extended
	 *            the extended to set
	 */
	public void setExtended(InvoiceLineExtended extended) {
		this.extended = extended;
	}

	/**
	 * @return the invoiceLineId
	 */
	public String getInvoiceLineId() {
		return invoiceLineId;
	}

	/**
	 * @param invoiceLineId
	 *            the invoiceLineId to set
	 */
	public void setInvoiceLineId(String invoiceLineId) {
		this.invoiceLineId = invoiceLineId;
	}

	/**
	 * @return the itemId
	 */
	public String getItemId() {
		return itemId;
	}

	/**
	 * @param itemId
	 *            the itemId to set
	 */
	public void setItemId(String itemId) {
		this.itemId = itemId;
	}

	/**
	 * @return the orderLineId
	 */
	public String getOrderLineId() {
		return orderLineId;
	}

	/**
	 * @param orderLineId
	 *            the orderLineId to set
	 */
	public void setOrderLineId(String orderLineId) {
		this.orderLineId = orderLineId;
	}

	/**
	 * @return the quantity
	 */
	public BigDecimal getQuantity() {
		return quantity;
	}

	/**
	 * @param quantity
	 *            the quantity to set
	 */
	public void setQuantity(BigDecimal quantity) {
		this.quantity = quantity;
	}

	/**
	 * @return the orderedQuantity
	 */
	public BigDecimal getOrderedQuantity() {
		return orderedQuantity;
	}

	/**
	 * @param orderedQuantity
	 *            the orderedQuantity to set
	 */
	public void setOrderedQuantity(BigDecimal orderedQuantity) {
		this.orderedQuantity = orderedQuantity;
	}

	/**
	 * @return the invoiceLineTotal
	 */
	public BigDecimal getInvoiceLineTotal() {
		return invoiceLineTotal;
	}

	/**
	 * @return the isRefundGiftCard
	 */
	public boolean isRefundGiftCard() {
		return isRefundGiftCard;
	}

	/**
	 * @param isRefundGiftCard
	 *            the isRefundGiftCard to set
	 */
	public void setRefundGiftCard(boolean isRefundGiftCard) {
		this.isRefundGiftCard = isRefundGiftCard;
	}

	/**
	 * @param invoiceLineTotal
	 *            the invoiceLineTotal to set
	 */
	public void setInvoiceLineTotal(BigDecimal invoiceLineTotal) {
		if (invoiceLineTotal != null)
			this.invoiceLineTotal = invoiceLineTotal.abs();
		else
			this.invoiceLineTotal = invoiceLineTotal;
	}

	/**
	 * compare Invoice Line amount to set priority
	 * 
	 * @param pO1
	 * @param pO2
	 * @return
	 */
	@Override
	public int compare(InvoiceLine pO1, InvoiceLine pO2) {
		// overriding the method to set the priority
		return pO1.getInvoiceLineTotal().compareTo((pO2.getInvoiceLineTotal()));
		// if (pO1.getInvoiceLineTotal() < pO2.getInvoiceLineTotal())
		// return -1;
		// else
		// return 1;
	}

	/**
	 * @return the invoiceId
	 */
	public String getInvoiceId() {
		return invoiceId;
	}

	/**
	 * @param pInvoiceId
	 *            the invoiceId to set
	 */
	public void setInvoiceId(String pInvoiceId) {
		invoiceId = pInvoiceId;
	}
}
// END OF FILE