/**
 * 
 */
package com.aafes.settlement.service.implementation.exchange;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import com.aafes.settlement.core.model.ProcessingContainer;
import com.aafes.settlement.core.response.EISEResponseGeneric;
import com.aafes.settlement.core.response.InvoiceLineResponse;
import com.aafes.settlement.core.response.InvoiceResponse;
import com.aafes.settlement.core.response.PaymentMethodResponse;
import com.aafes.settlement.repository.RepoContainer;
import com.aafes.settlement.util.CalculationUtil;

@Service
public class UnevenExchangeCalculation
	extends
	ExchangeCalculation
{
	private static final Logger LOGGER = LogManager
			.getLogger(UnevenExchangeCalculation.class);

	// ------------------------------------------------------------------------
	/**
	 * Public method exposed to calculate payment tenders and generate
	 * settlement response
	 * 
	 * This can potentially be moved to BaseClass.
	 * 
	 * @param p_container
	 * @param p_repoContainer
	 * @param p_eiseResponseGeneric
	 */
	@Override
	public void calculatePaymentTenders(
			ProcessingContainer p_container,
			RepoContainer p_repoContainer,
			EISEResponseGeneric p_eiseResponseGeneric
	)
	{
		super.calculatePaymentTenders(p_container, p_repoContainer, p_eiseResponseGeneric);
	}
	/**
	 * 
	 */
	@Override
	protected void createResponse(
			CalculationUtil p_calculationContainer,
			ProcessingContainer p_unevenExContainer,
			EISEResponseGeneric p_eiseResponseGeneric,
			List<InvoiceResponse> p_unevenExTenderResponse,
			InvoiceResponse p_unevenExInvoiceResponse
	)
	{
		LOGGER.debug("---- UnevenExchange Calculation: createResponse(): START---");
		// add response from Map created on Container
		p_unevenExInvoiceResponse.setPaymentMethod(
				p_unevenExContainer
						.getPaymentMethodResponses().entrySet().stream()
						.filter(
								l_paymentMethod -> l_paymentMethod
										.getKey() != null
						)
						.map(Map.Entry::getValue)
						.filter(
								l_paymentMethod -> l_paymentMethod
										.getPaymentMethodId() != null
						)
						.collect(Collectors.toList())
		);
		p_unevenExTenderResponse.add(p_unevenExInvoiceResponse);
		setGenericResponseTender(
				p_eiseResponseGeneric,
				p_unevenExTenderResponse
		);
		if(isValueLesserOrEqual(p_unevenExContainer.getUnevenRefundAmountTotal(),ZERO_VALUE))
			doExtendedCalculation(p_calculationContainer, p_unevenExContainer, p_eiseResponseGeneric, 
										p_unevenExTenderResponse, p_unevenExInvoiceResponse);
		LOGGER.debug("---- UnevenExchange Calculation: createResponse(): END---");
	}

	/**
	 * To set response in exchange tender
	 * 
	 * @param p_genericResponse
	 * @param p_InvoiceResponses
	 */
	@Override
	protected void setGenericResponseTender(
			EISEResponseGeneric p_genericResponse,
			List<InvoiceResponse> p_InvoiceResponses
	)
	{
		p_genericResponse.setExchangeTender(p_InvoiceResponses);

	}

	/**
	 *  Private method to set refund response tender for 
	 * 	Uneven Exchanges with New item price < exchanged item
	 * @param p_genericResponse
	 * @param p_InvoiceResponses
	 */
	private void setRefundResponseTender(
			EISEResponseGeneric p_genericResponse,
			List<InvoiceResponse> p_InvoiceResponses
	){
		p_genericResponse.setRefundTender(p_InvoiceResponses);
	}

	/**
	 *  This method will do extended calculation for uneven exchanges and generates
	 * 	refund tended in case amount of new item is lesser than exchanged item
	 * 	or OrderTotal is in negative value
	 * @param p_calculationContainer
	 * @param p_unevenExContainer
	 * @param p_eiseResponseGeneric
	 * @param p_unevenExTenderResponse
	 * @param p_unevenExInvoiceResponse
	 */
	private void doExtendedCalculation(
		CalculationUtil p_calculationContainer,
		ProcessingContainer p_unevenExContainer,
		EISEResponseGeneric p_eiseResponseGeneric,
		List<InvoiceResponse> p_unevenExTenderResponse,
		InvoiceResponse p_unevenExInvoiceResponse
	)
	{
		BigDecimal totalAmountToRefund = p_unevenExContainer.getUnevenRefundAmountTotal().abs();
		List<InvoiceResponse> l_refundResponse = new ArrayList<InvoiceResponse>();
		InvoiceResponse l_invoiceRes = new InvoiceResponse();
		List<PaymentMethodResponse> l_payemntresponse  = new ArrayList<PaymentMethodResponse>();
		//UNTIL_REFUND_IN_FULL:
		while(isValueGreater(totalAmountToRefund, IS_SETTLED)){
			
			for(PaymentMethodResponse currentPaymentMethod: 
					p_unevenExTenderResponse.get(0).getPaymentMethod()){
						PaymentMethodResponse l_paymemt = new PaymentMethodResponse();
						List<InvoiceLineResponse> l_listinvoice = new ArrayList<InvoiceLineResponse>();
						for(InvoiceLineResponse currentInvoice : currentPaymentMethod.getInvoiceLine()){
							InvoiceLineResponse l_invoiceLine = new InvoiceLineResponse(currentInvoice.getInvoiceId(), 
																currentInvoice.getInvoiceLineId());
							if(isValueLesserOrEqual(totalAmountToRefund,currentInvoice.getPaymentAmount())){
								l_paymemt.setPaymentMethodId(currentPaymentMethod.getPaymentMethodId());
								l_invoiceLine.setPaymentAmount(totalAmountToRefund);
								totalAmountToRefund = IS_SETTLED;
								l_listinvoice.add(l_invoiceLine);
								break;
							}else{
								l_paymemt.setPaymentMethodId(currentPaymentMethod.getPaymentMethodId());
								l_invoiceLine.setPaymentAmount(currentInvoice.getPaymentAmount());
								totalAmountToRefund = totalAmountToRefund.subtract(currentInvoice.getPaymentAmount());					
							}
							l_listinvoice.add(l_invoiceLine);
						}
				l_paymemt.setInvoiceLine(l_listinvoice);		
				l_payemntresponse.add(l_paymemt);
			}
			l_invoiceRes.setPaymentMethod(l_payemntresponse);
			l_refundResponse.add(l_invoiceRes);
		}
		setRefundResponseTender(p_eiseResponseGeneric, l_refundResponse);
	}
}
