package com.aafes.settlement.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.aafes.settlement.model.ResponseObject;
import com.aafes.settlement.model.UserLogin;
import com.aafes.settlement.service.LoginService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * 
 * @author Logixal Solutions Pvt Ltd
 *
 */
@RestController
@CrossOrigin(origins = "*")
@RequestMapping(path = "/api/login")
public class LoginController {

	@Autowired
	LoginService loginService;

	// -------------------------------------------------------------------------

	/**
	 * This api will return simulated login response containing user-details and
	 * menu.
	 * 
	 * Api details: url : /api/login/doSimulatedLogin method: POST
	 * 
	 * @param userLogin
	 * @return
	 */
	@ApiOperation(value = "Get Userprofile and Menu")
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
	@PostMapping(
			value = "/doSimulatedLogin",
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> doSimulatedLogin(
			@RequestBody UserLogin userLogin
	)
	{
		ResponseObject response = loginService.doSimulatedLogin(userLogin);
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}
	// -------------------------------------------------------------------------

}
// -----------------------------------------------------------------------------
// END OF FILE
// -----------------------------------------------------------------------------
