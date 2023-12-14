package com.aafes.settlement.core.model.refund;

import java.util.List;

import com.aafes.settlement.core.invoice.OpenInvoice;
import com.aafes.settlement.core.model.settlement.SettlementRO;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class RefundRO {

	// Reusing the Request object from Settlement
	@JsonProperty("ParentOrder")
	private SettlementRO parentOrder;
	
	@JsonProperty("OpenInvoice")
	private List<OpenInvoice>		openInvoice;

	/**
	 * @return the parentOrder
	 */
	public SettlementRO getParentOrder() {
		return parentOrder;
	}

	/**
	 * @param pParentOrder
	 *            the parentOrder to set
	 */
	public void setParentOrder(SettlementRO pParentOrder) {
		parentOrder = pParentOrder;
	}

	/**
	 * @return the openInvoice
	 */
	public List<OpenInvoice> getOpenInvoice() {
		return openInvoice;
	}

	/**
	 * @param p_openInvoice the openInvoice to set
	 */
	public void setOpenInvoice(List<OpenInvoice> p_openInvoice) {
		openInvoice = p_openInvoice;
	}

}
