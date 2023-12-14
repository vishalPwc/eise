package com.aafes.settlement.service;

import java.util.HashMap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.aafes.settlement.Invoice.model.EiseRequest;
import com.aafes.settlement.Invoice.model.ResponseObject;
import com.aafes.settlement.Invoice.model.ResponseSettlement;
import com.aafes.settlement.configuration.MessagePropertyConfig;
import com.aafes.settlement.constant.ErrorConstants;
import com.aafes.settlement.util.Utils;

@Service
@Qualifier
public class ProcessSettlementService
	implements
	ErrorConstants
{

	@Autowired
	private MessagePropertyConfig		messagePropConf;

	private static final Logger			LOGGER = LogManager
			.getLogger(ProcessSettlementService.class);

	@Autowired
	private CancSettlementService		cancSettlementService;

	@Autowired
	private ShipSettlementService		shipSettlementService;

	@Autowired
	private RetrunsSettlementService	retrunsSettlementService;

	@Autowired
	private AdjustmentSettlementService	adjustmentSettlementService;

	@Autowired
	private ExchangeSettlementService	exchangeSettlementService;

	// -------------------------------------------------------------------------

	/**
	 * This method will separate the request based on its type and route it to
	 * respective services
	 * 
	 * @param p_requestObject
	 *            : contains Request of refund/cancellation/shipment
	 * @return ResponseObject
	 */
	public ResponseSettlement processSettlement(
		EiseRequest p_eiseRequest
	) {
		ResponseObject l_respObj = new ResponseObject();
		ResponseSettlement l_responseSettlement = new ResponseSettlement();

		if (!Utils.isNull(l_responseSettlement)) {

			Utils.setSuccessInResponseSettlementObj(l_responseSettlement);

			if (p_eiseRequest.getSettlementRequest() != null) {
				LOGGER.info("-------- Shipping request received -------");
				shipSettlementService.processInvoiceSettlement(
						p_eiseRequest.getSettlementRequest(),
						l_responseSettlement
				);

			}
			if (p_eiseRequest.getReturnRequest() != null) {
				LOGGER.info("-------- Return request received -------");
				retrunsSettlementService.processOrderlineRetruns(
						p_eiseRequest.getReturnRequest(),
						l_responseSettlement
				);
			}
			// separate logic for cancel

			if (p_eiseRequest.getAdjustmentRequest() != null) {
				adjustmentSettlementService.processAdjustmentRequest(
						p_eiseRequest.getAdjustmentRequest(),
						l_responseSettlement
				);
			}

			if (p_eiseRequest.getExchangeRequest() != null) {
				exchangeSettlementService.processExchangeRequest(
						p_eiseRequest.getExchangeRequest(), l_responseSettlement
				);
			}

			if (p_eiseRequest.getAuthReversalRequest() != null) {
				LOGGER.info("-------- Return request received -------");
				cancSettlementService.processOrderlineCancellation(
						p_eiseRequest.getAuthReversalRequest(),
						l_responseSettlement
				);
			}

			return l_responseSettlement;
		} else {
			Utils.setFailureInResponseObj(l_respObj);
			HashMap<String, String> l_errors = new HashMap<String, String>();
			l_errors.put(INVALID_REQUEST, "");
			l_responseSettlement.setError(Utils.getErrors(l_errors,messagePropConf));
			return l_responseSettlement;
		}

	}
	// -------------------------------------------------------------------------
}
// -----------------------------------------------------------------------------
// END OF FILE
// -----------------------------------------------------------------------------