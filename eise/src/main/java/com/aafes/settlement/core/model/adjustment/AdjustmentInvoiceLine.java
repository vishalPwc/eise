/**
 * 
 */
package com.aafes.settlement.core.model.adjustment;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;

import com.aafes.settlement.core.invoice.InvoiceChargeDetail;
import com.aafes.settlement.core.invoice.InvoiceLineExtended;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 *
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class AdjustmentInvoiceLine
	implements
	Comparator<AdjustmentInvoiceLine>
{

	@JsonProperty("InvoiceLineId")
	private String					  invoiceLineId;

	@JsonProperty("Extended")
	private InvoiceLineExtended		  extended;

	@JsonProperty("InvoiceLineChargeDetail")
	private List<InvoiceChargeDetail> invoiceLineChargeDetail;

	@JsonProperty("InvoiceId")
	private String					  invoiceId;

	@JsonProperty("InvoiceLineTotal")
	private BigDecimal				  invoiceLineTotal;

	@JsonProperty("IsRefundGiftCard")
	private boolean					  isRefundGiftCard;

	/**
	 * compare Invoice Line amount to set priority
	 * 
	 * @param pO1
	 * @param pO2
	 * @return
	 */
	@Override
	public int compare(AdjustmentInvoiceLine pO1, AdjustmentInvoiceLine pO2) {
		// overriding the method to set the priority
		return pO1.getInvoiceLineTotal().compareTo((pO2.getInvoiceLineTotal()));
		// if (pO1.getInvoiceLineTotal() < pO2.getInvoiceLineTotal())
		// return -1;
		// else
		// return 1;
	}

	/**
	 * @return the invoiceLineId
	 */
	public String getInvoiceLineId() {
		return invoiceLineId;
	}

	/**
	 * @param pInvoiceLineId
	 *            the invoiceLineId to set
	 */
	public void setInvoiceLineId(String pInvoiceLineId) {
		invoiceLineId = pInvoiceLineId;
	}

	/**
	 * @return the extended
	 */
	public InvoiceLineExtended getExtended() {
		return extended;
	}

	/**
	 * @param pExtended
	 *            the extended to set
	 */
	public void setExtended(InvoiceLineExtended pExtended) {
		extended = pExtended;
	}

	/**
	 * @return the invoiceLineChargeDetail
	 */
	public List<InvoiceChargeDetail> getInvoiceLineChargeDetail() {
		return invoiceLineChargeDetail;
	}

	/**
	 * @param pInvoiceLineChargeDetail
	 *            the invoiceLineChargeDetail to set
	 */
	public void setInvoiceLineChargeDetail(
			List<InvoiceChargeDetail> pInvoiceLineChargeDetail
	)
	{
		invoiceLineChargeDetail = pInvoiceLineChargeDetail;
	}

	/**
	 * @return the invoiceId
	 */
	public String getInvoiceId() {
		return invoiceId;
	}

	/**
	 * @return the invoiceLineTotal
	 */
	public BigDecimal getInvoiceLineTotal() {
		return invoiceLineTotal;
	}

	/**
	 * @param pInvoiceLineTotal
	 *            the invoiceLineTotal to set
	 */
	public void setInvoiceLineTotal(BigDecimal pInvoiceLineTotal) {
		invoiceLineTotal = pInvoiceLineTotal;
	}

	/**
	 * @param pInvoiceId
	 *            the invoiceId to set
	 */
	public void setInvoiceId(String pInvoiceId) {
		invoiceId = pInvoiceId;
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
