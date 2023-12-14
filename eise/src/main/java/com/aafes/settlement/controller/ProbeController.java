package com.aafes.settlement.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@CrossOrigin(origins = "*")
public class ProbeController {

	/**
	 * This API will return server status
	 * 
	 * @return ResponseEntity
	 */
	@ApiOperation(value = "Get Server Status")
	@ApiResponses(
			value = {
					@ApiResponse(code = 200, message = "Success|OK"),
					@ApiResponse(code = 400, message = "Bad Request"),
					@ApiResponse(code = 401, message = "not authorized!"),
					@ApiResponse(code = 403, message = "forbidden!!!"),
					@ApiResponse(code = 404, message = "not found!!!"),
					@ApiResponse(
							code = 500,
							message = "internal server error!!!") })
	@GetMapping(
			value = "/probe",
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> getServerStatus() {

		return ResponseEntity.status(HttpStatus.OK).body(null);
	}
}
