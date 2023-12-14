package com.aafes.settlement.controller;
// ---------------------------------------------------------------------------
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

import com.aafes.settlement.core.request.EISERequestGeneric;
import com.aafes.settlement.core.request.EISERequestObject;
import com.aafes.settlement.core.response.EISEResponseGeneric;
import com.aafes.settlement.service.SmartProcessService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

// ---------------------------------------------------------------------------
@RestController
@CrossOrigin(origins = "*")
@RequestMapping(path = "/api/invoice")
public class InvoiceController {
	// ------------------------------------------------------------------------
	private static final Logger	  LOGGER = LogManager.getLogger();
	// ------------------------------------------------------------------------
	@Autowired
	private SmartProcessService	  smartProcessService;
	// ------------------------------------------------------------------------
	/**
	 * This method is used for handling all type of requests
	 * (refund/cancellation/shipment) received from MAO.
	 * 
	 * @param p_eiseRequest:
	 *            this is the request object.
	 * @return Response Object
	 */

	@ApiOperation(value = "Process Settlement")
	@ApiResponses(
			value = { @ApiResponse(code = 200, message = "Success|OK"),
					@ApiResponse(code = 400, message = "Bad Request"),
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
			@RequestBody EISERequestGeneric p_eiseRequest
	)
	{
		EISEResponseGeneric l_eiseResponseGeneric = new EISEResponseGeneric();
		EISERequestObject l_eiseRequestObject = new EISERequestObject();
		LOGGER.info("-------Request received-------");

		l_eiseRequestObject.setRequestdata(p_eiseRequest);

		smartProcessService.processRequest(
				p_eiseRequest, l_eiseResponseGeneric
		);

		LOGGER.info("-------Request processed------");
		return ResponseEntity.status(HttpStatus.OK).body(l_eiseResponseGeneric);
	}
	// ------------------------------------------------------------------------
}
// ----------------------------------------------------------------------------
// END OF FILE
// ----------------------------------------------------------------------------