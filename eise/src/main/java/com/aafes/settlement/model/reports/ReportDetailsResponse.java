package com.aafes.settlement.model.reports;

import java.util.Optional;

import com.aafes.settlement.repository.entity.OrderInvoiceDetails;

/**
 * Response Object for Report details : Erpa to Ui
 *
 */
public class ReportDetailsResponse {

	private Optional<OrderInvoiceDetails> orderInvoiceDetails;

	/**
	 * @return the orderInvoiceDetails
	 */
	public Optional<OrderInvoiceDetails> getOrderInvoiceDetails() {
		return orderInvoiceDetails;
	}

	/**
	 * @param orderInvoiceDetails
	 *            the orderInvoiceDetails to set
	 */
	public void setOrderInvoiceDetails(
			Optional<OrderInvoiceDetails> orderInvoiceDetails
	)
	{
		this.orderInvoiceDetails = orderInvoiceDetails;
	}

}
// ----------------------------------------------------------------------------
// END OF FILE
// ----------------------------------------------------------------------------