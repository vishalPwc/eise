package com.aafes.settlement.core.model.reversal;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ReversalPaymentHeader {

	@JsonProperty("PK")
	private String						pK;

	@JsonProperty("OrderId")
	private String						orderId;

	@JsonProperty("PaymentGroupId")
	private String						paymentGroupId;

	@JsonProperty("PaymentMethod")
	private List<ReversalPaymentMethod>	paymentMethod = null;

	/**
	 * @return the pK
	 */
	public String getpK() {
		return pK;
	}

	/**
	 * @param pPK
	 *            the pK to set
	 */
	public void setpK(String pPK) {
		pK = pPK;
	}

	/**
	 * @return the orderId
	 */
	public String getOrderId() {
		return orderId;
	}

	/**
	 * @param pOrderId
	 *            the orderId to set
	 */
	public void setOrderId(String pOrderId) {
		orderId = pOrderId;
	}

	/**
	 * @return the paymentGroupId
	 */
	public String getPaymentGroupId() {
		return paymentGroupId;
	}

	/**
	 * @param pPaymentGroupId
	 *            the paymentGroupId to set
	 */
	public void setPaymentGroupId(String pPaymentGroupId) {
		paymentGroupId = pPaymentGroupId;
	}

	/**
	 * @return the paymentMethod
	 */
	public List<ReversalPaymentMethod> getPaymentMethod() {
		return paymentMethod;
	}

	/**
	 * @param pPaymentMethod
	 *            the paymentMethod to set
	 */
	public void setPaymentMethod(List<ReversalPaymentMethod> pPaymentMethod) {
		paymentMethod = pPaymentMethod;
	}
}
