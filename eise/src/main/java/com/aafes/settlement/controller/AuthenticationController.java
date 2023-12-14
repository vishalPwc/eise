package com.aafes.settlement.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.aafes.settlement.model.RequestObject;
import com.aafes.settlement.model.ResponseObject;
import com.aafes.settlement.model.jwt.JwtRequest;
import com.aafes.settlement.service.AuthenticationService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * This class conatins API to generate JWT Token
 */
@RestController
@CrossOrigin
@RequestMapping(path = "/api/authenticate")
public class AuthenticationController {

	@Autowired
	private AuthenticationService authService;

	@ApiOperation(value = "Generate JWT Token")
	@ApiResponses(
			value = { @ApiResponse(code = 200, message = "Success|OK"),
					@ApiResponse(code = 400, message = "Bad Request"),
					@ApiResponse(code = 401, message = "not authorized!"),
					@ApiResponse(code = 403, message = "forbidden!!!"),
					@ApiResponse(code = 404, message = "not found!!!"),
					@ApiResponse(
							code = 500,
							message = "internal server error!!!") })
	/**
	 * This API will create a Authentication token
	 * 
	 * @param authenticationRequest
	 * @return ResponseEntity
	 * @throws Exception
	 */
	@RequestMapping(value = "/getToken", method = RequestMethod.POST)
	public ResponseEntity<?> createAuthenticationToken(
			@RequestBody JwtRequest authenticationRequest
	)
			throws Exception
	{
		RequestObject l_reqObj = new RequestObject();
		l_reqObj.setRequestdata(authenticationRequest);
		ResponseObject l_respObject = authService.generateToken(l_reqObj);

		return ResponseEntity.status(HttpStatus.OK).body(l_respObject);
	}
	// -----------------------------------------------------------------------

	/**
	 * This API will return a Key to generate a Authentication token
	 * 
	 * @return ResponseEntity
	 * @throws Exception
	 */
	// @ApiIgnore
	@ApiOperation(value = "Generate Key")
	@ApiResponses(
			value = { @ApiResponse(code = 200, message = "Success|OK"),
					@ApiResponse(code = 400, message = "Bad Request"),
					@ApiResponse(code = 401, message = "not authorized!"),
					@ApiResponse(code = 403, message = "forbidden!!!"),
					@ApiResponse(code = 404, message = "not found!!!"),
					@ApiResponse(
							code = 500,
							message = "internal server error!!!") })
	@RequestMapping(value = "/getKey", method = RequestMethod.GET)
	public ResponseEntity<?> createAuthenticationKey(

	)
			throws Exception
	{
		RequestObject l_reqObj = new RequestObject();
		// l_reqObj.setRequestdata(authenticationRequest);
		ResponseObject l_respObject = authService.generateKey(l_reqObj);

		return ResponseEntity.status(HttpStatus.OK).body(l_respObject);
	}

}
// -----------------------------------------------------------------------------
// END OF FILE
// -----------------------------------------------------------------------------
