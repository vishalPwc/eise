package com.aafes.settlement.service;

import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import com.aafes.settlement.configuration.GlobalParams;
import com.aafes.settlement.configuration.MessagePropertyConfig;
import com.aafes.settlement.configuration.jwtconfig.JwtRequestFilter;
import com.aafes.settlement.configuration.jwtconfig.JwtTokenUtil;
import com.aafes.settlement.constant.AuthenticationConstants;
import com.aafes.settlement.constant.Constants;
import com.aafes.settlement.constant.ErrorConstants;
import com.aafes.settlement.model.RequestObject;
import com.aafes.settlement.model.ResponseObject;
import com.aafes.settlement.model.jwt.JwtRequest;
import com.aafes.settlement.model.jwt.JwtResponse;
import com.aafes.settlement.model.jwt.KeyResponse;
import com.aafes.settlement.util.Utils;

/**
 * This class contains method to generate JWT Token
 *
 */
@Service
public class AuthenticationService
	implements ErrorConstants, AuthenticationConstants
{

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private JwtTokenUtil		  jwtTokenUtil;

	@Autowired
	private JwtRequestFilter	  jwtfilter;

	@Autowired
	private UserDetailsService	  jwtInMemoryUserDetailsService;

	@Autowired
	private MessagePropertyConfig messagePropConf;

	private static final Logger	  LOGGER = LogManager.getLogger(
			AuthenticationService.class
	);

	/**
	 * This method generates JWT token
	 * 
	 * @param p_requestObject
	 * @return
	 */
	public ResponseObject generateToken(RequestObject p_requestObject) {
		LOGGER.error(
				"Generating token  for RequestObject: " + p_requestObject
						.getUuid()
		);

		ResponseObject l_respobject = new ResponseObject();
		JwtRequest authenticationRequest = (JwtRequest) p_requestObject
				.getRequestdata();
		JwtResponse l_jwtResponse = new JwtResponse();
		try {
			setAuthenticationRequest(authenticationRequest);
			if (validate(l_respobject, authenticationRequest)) {

				authenticationManager = jwtfilter
						.getAuthenticationManagerBean();
				authenticate(
						authenticationRequest.getUsername(),
						authenticationRequest
								.getPassword()
				);

				/*
				 * final UserDetails userDetails = jwtInMemoryUserDetailsService
				 * .loadUserByUsername( authenticationRequest.getUsername() );
				 */

				Long l_expiryTime = TimeUnit.MILLISECONDS.toMinutes(
						Integer.parseInt(GlobalParams.jwtExpiry) * 1000
				);
				String l_token = jwtTokenUtil.generateToken(
						authenticationRequest.getUsername()
				);

				l_jwtResponse.setJwttoken(l_token);
				l_jwtResponse.setExpiryTime(
						l_expiryTime.toString() + " minutes"
				);
				l_jwtResponse.setTokenType(BEARER);
				l_respobject.setResponsedata(l_jwtResponse);
				Utils.setSuccessInResponseObj(p_requestObject, l_respobject);
				LOGGER.info(
						"Generated token for RequestObject: " + p_requestObject
								.getUuid()
				);
			}
		} catch (Exception l_e) {
			Utils.setExceptionInResponseObj(
					l_respobject, l_e, messagePropConf
			);
			LOGGER.error(
					"Generating token Failure Exception: " + l_e
							.getMessage()
			);
		}
		return l_respobject;
	}

	// ------------------------------------------------------------------------

	/**
	 * This method generates JWT token
	 * 
	 * @param p_requestObject
	 * @return
	 */
	public ResponseObject generateKey(RequestObject p_requestObject) {
		LOGGER.error(
				"Generating token  for RequestObject: " + p_requestObject
						.getUuid()
		);

		ResponseObject l_respobject = new ResponseObject();
		// KeyRequest l_keyRequest = (KeyRequest) p_requestObject
		// .getRequestdata();
		KeyResponse l_keyResponse = new KeyResponse();
		try {
			String l_secret = GlobalParams.aesSecret;
			// String l_extSystem = l_keyRequest.getExternalSystem();
			// String l_userName = l_keyRequest.getUsername();
			// String l_password = l_keyRequest.getPassword();
			String l_extSystem = GlobalParams.jwtExtSystem;
			String l_userName = GlobalParams.jwtUsername;
			String l_password = GlobalParams.jwtPassword;

			byte[] decodedBytes = Base64.getDecoder().decode(
					l_password
			);
			String decodedString = new String(decodedBytes);
			// byte[] decodedBytes =
			/*
			 * Base64.getDecoder().decode(l_password); String decodedString =
			 * new String(decodedBytes);
			 */
			String l_key = Utils.getEncryptedMaoKey(
					l_extSystem,
					l_userName,
					decodedString,
					l_secret
			);
			l_keyResponse.setKey(l_key);
			l_respobject.setResponsedata(l_keyResponse);
			Utils.setSuccessInResponseObj(p_requestObject, l_respobject);
			LOGGER.info(
					"Generated key for RequestObject: " + p_requestObject
							.getUuid()
			);

		} catch (Exception l_e) {
			Utils.setExceptionInResponseObj(
					l_respobject, l_e, messagePropConf
			);
			LOGGER.error(
					"Generating key Failure Exception: " + l_e
							.getMessage()
			);
		}
		return l_respobject;
	}
	// ------------------------------------------------------------------------

	/**
	 * This method validates mandatory field for generating jwt token
	 * 
	 * @param p_respObj
	 * @param authenticationRequest
	 * @return
	 */
	private boolean validate(
			ResponseObject p_respObj, JwtRequest authenticationRequest
	)
	{
		boolean l_validate = true;

		if (
			authenticationRequest.getUsername() == null || authenticationRequest
					.getPassword() == null || authenticationRequest
							.getExternalSystem() == null
		)
		{
			Map<String, String> l_error = new HashMap<String, String>();
			//l_error.put(JWT_MANDATORY_FIELDS, "");
			l_error.put(JWT_INVALID_KEY, "");
			p_respObj.setErrors(Utils.getErrors(l_error,messagePropConf));
			
			l_validate = false;
		} else if (
			!authenticationRequest.getExternalSystem().equals(
					GlobalParams.jwtExtSystem
			)
		)
		{
			Map<String, String> l_error = new HashMap<String, String>();
			l_error.put(JWT_INVALID_KEY, "");
			p_respObj.setErrors(Utils.getErrors(l_error,messagePropConf));;
			l_validate = false;
		}

		if (!l_validate) {
			Utils.setFailureInResponseObj(p_respObj);
		}

		return l_validate;
	}

	// ------------------------------------------------------------------------
	/**
	 * This method authenticates username and password
	 * 
	 * @param username
	 * @param password
	 * @throws Exception
	 */
	private void authenticate(
			String username, String password
	)
			throws Exception
	{

		authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(
						username, password
				)
		);
	}
	// ------------------------------------------------------------------------

	public void setAuthenticationRequest(JwtRequest authenticationRequest)
			throws Exception
	{
		String l_decryptedKey = null;
		String[] keyItems = null;
		String extSystem = null;
		String username = null;
		String password = null;
		try {
			l_decryptedKey = Utils.getDecryptedMaoKey(
					authenticationRequest.getKey(),
					GlobalParams.aesSecret
			);
			keyItems = l_decryptedKey.split(Constants.MAO_TOKEN_SEPARATOR);
			extSystem = keyItems[0];
			username = keyItems[1];
			password = keyItems[2];
			if (!Utils.isNull(extSystem)) {
				authenticationRequest.setExternalSystem(extSystem);
			}
			if (!Utils.isNull(username)) {
				authenticationRequest.setUsername(username);
			}
			if (!Utils.isNull(password)) {
				authenticationRequest.setPassword(password);
			}
		} catch (Exception p_ex) {
			LOGGER.error(
					"Error in setAuthenticationRequest() : " +
							p_ex.getMessage()
			);
			throw new Exception("Invalid key");
		}
	}
	// ------------------------------------------------------------------------
}
// ----------------------------------------------------------------------------
// END OF FILE
// ----------------------------------------------------------------------------