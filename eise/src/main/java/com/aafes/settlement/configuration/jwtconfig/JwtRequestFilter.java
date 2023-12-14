package com.aafes.settlement.configuration.jwtconfig;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.aafes.settlement.constant.AuthenticationConstants;
import com.aafes.settlement.constant.Constants;
import com.aafes.settlement.constant.ErrorConstants;
import com.aafes.settlement.service.JwtUserDetailsService;

import io.jsonwebtoken.ExpiredJwtException;

/**
 * This JwtRequestFilter class extends the Spring Web Filter
 * OncePerRequestFilter class.For any incoming request this Filter class gets
 * executed. It checks if the request has a valid JWT token. If it has a valid
 * JWT Token then it sets the Authentication in the context, to specify that the
 * current user is authenticated.
 *
 */
@Component
public class JwtRequestFilter
	extends
	OncePerRequestFilter
	implements AuthenticationConstants, Constants, ErrorConstants
{

	@Autowired
	private JwtUserDetailsService jwtUserDetailsService;

	@Autowired
	private JwtTokenUtil		  jwtTokenUtil;

	private AuthenticationManager authManager = null;

	private static final Logger	  LOGGER	  = LogManager.getLogger(
			JwtRequestFilter.class
	);

	// ------------------------------------------------------------------------

	public void setAuthenticationManagerBean(AuthenticationManager authManager)
			throws Exception
	{
		this.authManager = authManager;
	}

	public AuthenticationManager getAuthenticationManagerBean()
			throws Exception
	{
		return this.authManager;
	}

	/**
	 * This method will Flters the every request
	 * 
	 * @param request
	 * @param response
	 * @param chain
	 * @throws ServletException
	 * @throws IOException
	 */
	@Override
	protected void doFilterInternal(
			HttpServletRequest request, HttpServletResponse response,
			FilterChain chain
	)
			throws ServletException,
			IOException
	{
		boolean isApiUrl = false;
		// Exclude getToken, login and report API calls from
		// JWT token verification.
		// For other /api calls token verification is done
		if (
			request.getRequestURI().startsWith("/eise/api")
					&&
					!request.getRequestURI().startsWith(
							"/eise/api/authenticate"
					) &&
					!request.getRequestURI().startsWith(
							"/eise/api/login"
					) &&
					!request.getRequestURI().startsWith(
							"/eise/api/report"
					) &&
					!request.getRequestURI().startsWith(
							"/eise/api/infrastructure"
					)
		)
		{
			isApiUrl = true;
		}
		final String requestTokenHeader = request.getHeader(AUTHORIZATION);

		String username = null;
		String jwtToken = null;
		if (isApiUrl) {
			// JWT Token is in the form "Bearer token". Remove Bearer word and
			// get
			// only the Token
			if (
				requestTokenHeader != null && requestTokenHeader.startsWith(
						BEARER
				)
			)
			{
				jwtToken = requestTokenHeader.substring(7);

				try {
					username = jwtTokenUtil.getUsernameFromToken(jwtToken);
				} catch (IllegalArgumentException e) {
					LOGGER.error("Unable to get JWT Token");
					response.sendError(
							HttpServletResponse.SC_UNAUTHORIZED,
							"Unable to get JWT Token"
					);
				} catch (ExpiredJwtException e) {
					LOGGER.error("JWT Token has expired");
					response.sendError(
							HttpServletResponse.SC_UNAUTHORIZED,
							"JWT Token has expired"
					);
				} catch (Exception e) {
					response.sendError(
							HttpServletResponse.SC_UNAUTHORIZED,
							e.getMessage()
					);
				}
			} else {
				LOGGER.error("JWT Token does not begin with Bearer String");
				response.sendError(
						HttpServletResponse.SC_UNAUTHORIZED,
						"JWT Token does not begin with Bearer String"
				);
			}
		}

		// Once we get the token validate it.
		if (
			username != null && SecurityContextHolder.getContext()
					.getAuthentication() == null
		)
		{

			UserDetails userDetails = this.jwtUserDetailsService
					.loadUserByUsername(username);

			// if token is valid configure Spring Security to manually set
			// authentication
			if (jwtTokenUtil.validateToken(jwtToken, userDetails)) {

				UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
						userDetails, null, userDetails.getAuthorities()
				);
				usernamePasswordAuthenticationToken
						.setDetails(
								new WebAuthenticationDetailsSource()
										.buildDetails(request)
						);
				// After setting the Authentication in the context, we specify
				// that the current user is authenticated. So it passes the
				// Spring Security Configurations successfully.
				SecurityContextHolder.getContext().setAuthentication(
						usernamePasswordAuthenticationToken
				);
			} else {
				response.sendError(
						HttpServletResponse.SC_UNAUTHORIZED, "Invalid Token"
				);
			}
		}
		chain.doFilter(request, response);

	}
	// ----------------------------------------------------------------------
}
