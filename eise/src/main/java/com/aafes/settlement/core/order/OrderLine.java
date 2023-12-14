package com.aafes.settlement.core.order;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class OrderLine {

	@JsonProperty("OrderLineId")
	private String			  orderLineId;
	@JsonProperty("Quantity")
	private BigDecimal		  quantity;
	@JsonProperty("ItemId")
	private String			  itemId;
	@JsonProperty("FulfillmentStatus")
	private String			  fulfillmentStatus;
	@JsonProperty("IsCancelled")
	private Boolean			  isCancelled;
	@JsonProperty("MinFulfillmentStatusId")
	private String			  minFulfillmentStatusId;
	@JsonProperty("MaxFulfillmentStatusId")
	private String			  maxFulfillmentStatusId;
	@JsonProperty("OrderLineTotal")
	private BigDecimal		  orderLineTotal;
	@JsonProperty("Extended")
	private OrderLineExtended extended;

	@JsonProperty("IsRefundGiftCard")
	private boolean			  isRefundGiftCard;

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
	 * @return the fulfillmentStatus
	 */
	public String getFulfillmentStatus() {
		return fulfillmentStatus;
	}

	/**
	 * @param fulfillmentStatus
	 *            the fulfillmentStatus to set
	 */
	public void setFulfillmentStatus(String fulfillmentStatus) {
		this.fulfillmentStatus = fulfillmentStatus;
	}

	/**
	 * @return the isCancelled
	 */
	public Boolean getIsCancelled() {
		return isCancelled;
	}

	/**
	 * @param isCancelled
	 *            the isCancelled to set
	 */
	public void setIsCancelled(Boolean isCancelled) {
		this.isCancelled = isCancelled;
	}

	/**
	 * @return the minFulfillmentStatusId
	 */
	public String getMinFulfillmentStatusId() {
		return minFulfillmentStatusId;
	}

	/**
	 * @param minFulfillmentStatusId
	 *            the minFulfillmentStatusId to set
	 */
	public void setMinFulfillmentStatusId(String minFulfillmentStatusId) {
		this.minFulfillmentStatusId = minFulfillmentStatusId;
	}

	/**
	 * @return the maxFulfillmentStatusId
	 */
	public String getMaxFulfillmentStatusId() {
		return maxFulfillmentStatusId;
	}

	/**
	 * @param maxFulfillmentStatusId
	 *            the maxFulfillmentStatusId to set
	 */
	public void setMaxFulfillmentStatusId(String maxFulfillmentStatusId) {
		this.maxFulfillmentStatusId = maxFulfillmentStatusId;
	}

	/**
	 * @return the orderLineTotal
	 */
	public BigDecimal getOrderLineTotal() {
		return orderLineTotal;
	}

	/**
	 * @param orderLineTotal
	 *            the orderLineTotal to set
	 */
	public void setOrderLineTotal(BigDecimal orderLineTotal) {
		this.orderLineTotal = orderLineTotal;
		// setInterimOrderLineTotal(orderLineTotal);
	}

	/**
	 * @return the extended
	 */
	public OrderLineExtended getExtended() {
		return extended;
	}

	/**
	 * @param extended
	 *            the extended to set
	 */
	public void setExtended(OrderLineExtended extended) {
		this.extended = extended;
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

}
