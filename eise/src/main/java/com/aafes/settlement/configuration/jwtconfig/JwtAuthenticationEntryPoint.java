package com.aafes.settlement.configuration.jwtconfig;

import java.io.IOException;
import java.io.Serializable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.aafes.settlement.constant.AuthenticationConstants;

/**
 * This class will extend Spring's AuthenticationEntryPoint class and override
 * its method commence. It rejects every unauthenticated request.
 */
@Component
public class JwtAuthenticationEntryPoint
	implements AuthenticationEntryPoint, Serializable, AuthenticationConstants
{

	// -----------------------------------------------------------------------
	private static final long serialVersionUID = -7858869558953243875L;

	/**
	 * This method will Authenticate the every request with JWT token
	 * 
	 * @param request
	 * @param response
	 * @param authException
	 * @throws IOException
	 */
	@Override
	public void commence(
			HttpServletRequest request, HttpServletResponse response,
			AuthenticationException authException
	)
			throws IOException
	{
		response.sendError(HttpServletResponse.SC_UNAUTHORIZED, UNAUTHORIZED);
	}
	// ----------------------------------------------------------------------
}
// ----------------------------------------------------------------------------
// END OF FILE
// ----------------------------------------------------------------------------