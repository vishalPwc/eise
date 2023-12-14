package com.aafes.settlement.service;
// -----------------------------------------------------------------------------

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aafes.settlement.Invoice.model.OpenAuthReversal;
import com.aafes.settlement.Invoice.model.ResponseSettlement;
import com.aafes.settlement.Invoice.model.ResponseTender;
import com.aafes.settlement.Invoice.model.SettlementRequest;
import com.aafes.settlement.configuration.MessagePropertyConfig;
import com.aafes.settlement.constant.Constants;

// -----------------------------------------------------------------------------

@Service
public class CancSettlementService
	implements Constants
{
	// -------------------------------------------------------------------------

	@Autowired
	private MessagePropertyConfig messagePropConf;

	private static final Logger	  LOGGER = LogManager
			.getLogger(CancSettlementService.class);

	@Autowired
	private ShipSettlementService shipSettlementService;

	@Autowired
	AuthReversalService			  authReversalService;
	// -------------------------------------------------------------------------

	/**
	 * This method will return list of Disposition Codes
	 * 
	 * @param l_respObj
	 * 
	 * @return
	 */
	public void processOrderlineCancellation(
			SettlementRequest p_authReversalRequest,
			ResponseSettlement p_responseSettlement
	)
	{
		ResponseTender l_authReversalTender = new ResponseTender();
		List<ResponseTender> l_authReversalTenderList = new ArrayList<
				ResponseTender>();
		LinkedHashMap<String, Float> l_authPlan = new LinkedHashMap<String,
				Float>();
		try {

			l_authPlan = shipSettlementService.groupOrderLineByAuth(
					p_authReversalRequest
			);
			LOGGER.info("EACH AUTH TOTAL IS :: " + l_authPlan);
			processOpenAuthReversal(
					p_authReversalRequest, l_authPlan, l_authReversalTender
			);

			l_authReversalTenderList.add(l_authReversalTender);
			p_responseSettlement.setAuthReversalTender(
					l_authReversalTenderList
			);

		} catch (Exception e) {
			e.printStackTrace();

			// set error
		}
	}

	// -------------------------------------------------------------------------
	/**
	 * 
	 * @param p_authReversalRequest
	 * @param l_authPlan
	 * @param l_authReversalTender
	 */
	private void processOpenAuthReversal(
			SettlementRequest p_authReversalRequest, LinkedHashMap<String,
					Float> p_authPlan, ResponseTender p_authReversalTender
	)
	{
		List<OpenAuthReversal> l_openAuthReversalList = p_authReversalRequest
				.getOpenAuthReversal();

		for (OpenAuthReversal openAuthReversal : l_openAuthReversalList) {

			authReversalService.processAuthReversal(
					openAuthReversal, p_authReversalTender,
					p_authPlan, p_authReversalRequest
			);
		}

	}

	// -------------------------------------------------------------------------
}
// -----------------------------------------------------------------------------
// END OF FILE
// -----------------------------------------------------------------------------