package com.aafes.settlement.controller;

import java.util.HashMap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.aafes.settlement.Invoice.model.EiseRequest;
import com.aafes.settlement.Invoice.model.ResponseSettlement;
import com.aafes.settlement.configuration.MessagePropertyConfig;
import com.aafes.settlement.constant.ErrorConstants;
import com.aafes.settlement.service.ProcessSettlementService;
import com.aafes.settlement.util.Utils;
import com.aafes.settlement.validators.RequestValidator;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(path = "/api/invoice")
public class InvoiceController {

	private static final Logger		 LOGGER	= LogManager.getLogger();

	@Autowired
	private ProcessSettlementService processSettlementService;
	
	@Autowired
	private MessagePropertyConfig messagePropConf;

	// ------------------------------------------------------------------------
	/**
	 * This method is used for handling all type of
	 * requests (refund/cancellation/shipment) received from MAO.
	 * 
	 * @param p_eiseRequest:
	 *            this is the request object.
	 * @return Response Object
	 */
	// @Audit("Process Cancellation")
	@ApiOperation(value = "Process Settlement")
	@ApiResponses(
			value = { @ApiResponse(code = 200, message = "Success|OK"),
					@ApiResponse(code = 400, message = "Bad Request Found"),
					@ApiResponse(code = 401, message = "not authorized!"),
					@ApiResponse(code = 403, message = "forbidden!!!"),
					@ApiResponse(code = 404, message = "not found!!!"),
					@ApiResponse(
							code = 500,
							message = "internal server error!!!") })
	@PostMapping(
			value = "/processSettlement",
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> processSettlementRequest(
		@RequestBody EiseRequest p_eiseRequest
	) {
		RequestValidator l_requestValidator = new RequestValidator();
		ResponseSettlement l_responseSettlement = new ResponseSettlement();
		if (l_requestValidator.validate(p_eiseRequest)) {
			LOGGER.info(
					"\n"
							+ "                     ---------------------Settlement Request"
							+ " received-----------------------"
			);

			l_responseSettlement = processSettlementService.processSettlement(
					p_eiseRequest
			);

			LOGGER.info(
					"\n -----Settlement Request Processed Successfully------------"
			);
			
			if(l_responseSettlement.getError().isEmpty() || l_responseSettlement.getError() == null)
    			return ResponseEntity.status(HttpStatus.OK).body(
    					l_responseSettlement
    			);
			else
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
						l_responseSettlement
				);
		} else {
			Utils.setFailureInResponseSettlementObj(l_responseSettlement);
			HashMap<String, String> l_error = new HashMap<String, String>();
			l_error.put(ErrorConstants.INVALID_REQUEST, "");
			l_responseSettlement.setError(Utils.getErrors(l_error, messagePropConf));
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
					l_responseSettlement
			);
		}
	}
	// -------------------------------------------------------------------------
}
// -----------------------------------------------------------------------------
// END OF FILE
// -----------------------------------------------------------------------------