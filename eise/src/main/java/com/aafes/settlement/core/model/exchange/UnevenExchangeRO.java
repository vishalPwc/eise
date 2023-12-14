package com.aafes.settlement.core.model.exchange;

import java.math.BigDecimal;
import java.util.List;

import com.aafes.settlement.core.order.OrderLine;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class UnevenExchangeRO extends ExchangeRO {

	@JsonProperty("OrderLine")
	private List<OrderLine>			orderLine;

	@JsonProperty("OrderTotal")
	private BigDecimal				orderTotal;

	
	/**
	 * @return the orderLine
	 */
	public List<OrderLine> getOrderLine() {
		return orderLine;
	}

	/**
	 * @param orderLine
	 *            the orderLine to set
	 */
	public void setOrderLine(List<OrderLine> orderLine) {
		this.orderLine = orderLine;
	}

	/**
	 * 
	 * @return
	 */
	public BigDecimal getOrderTotal() {
		return orderTotal;
	}

	/**
	 * 
	 * @param orderTotal
	 */
	public void setOrderTotal(BigDecimal orderTotal) {
		this.orderTotal = orderTotal;
	}
}
