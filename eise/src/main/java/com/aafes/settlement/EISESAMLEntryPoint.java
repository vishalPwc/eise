package com.aafes.settlement;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.opensaml.common.xml.SAMLConstants;
import org.opensaml.ws.transport.http.HttpServletRequestAdapter;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.saml.SAMLEntryPoint;
import org.springframework.security.saml.context.SAMLMessageContext;
import org.springframework.security.saml.websso.WebSSOProfileOptions;

public class EISESAMLEntryPoint
	extends
	SAMLEntryPoint
{

	private static final Logger LOGGER = LogManager.getLogger(
			EISESAMLEntryPoint.class
	);

	/**
	 * This method is an Entry point of the SAML and we are setting Relying
	 * State value with the Redirect url which is receiving from React UI
	 * 
	 * @param context
	 * @param exception
	 * @return WebSSOProfileOptions
	 */
	@Override
	protected WebSSOProfileOptions getProfileOptions(
			SAMLMessageContext context, AuthenticationException exception
	)
	{

		LOGGER.info("START getProfileOptions()");
		WebSSOProfileOptions ssoProfileOptions;
		if (defaultOptions != null) {
			ssoProfileOptions = defaultOptions.clone();
		} else {
			ssoProfileOptions = new WebSSOProfileOptions();
		}

		ssoProfileOptions.setBinding(SAMLConstants.SAML2_POST_BINDING_URI);
		ssoProfileOptions.setIncludeScoping(false);
		ssoProfileOptions.setProxyCount(null);
		HttpServletRequestAdapter httpServletRequestAdapter = (HttpServletRequestAdapter) context
				.getInboundMessageTransport();

		String myRedirectUrl = httpServletRequestAdapter.getParameterValue(
				"redirectUrl"
		);

		LOGGER.debug("Redirect url from UI::" + myRedirectUrl);

		if (myRedirectUrl != null) {
			ssoProfileOptions.setRelayState(myRedirectUrl);
		}

		LOGGER.info("END getProfileOptions()");
		return ssoProfileOptions;
	}
}