package com.aafes.settlement.sso;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.saml.SAMLCredential;
import org.springframework.security.saml.userdetails.SAMLUserDetailsService;

/**
 * Consider implementing your own {@link UserDetailsService} to check user
 * permissions against a persistent storage and load your own
 * {@link UserDetails} implementation.
 */
public class EiseSAMLUserDetailsService
	implements SAMLUserDetailsService
{
	/**
	 * This Method will return SAMLUserDetials by using the SAMLCredentials
	 * 
	 * @param credential
	 * @return Object
	 */
	@Override
	public Object loadUserBySAML(SAMLCredential credential)
			throws UsernameNotFoundException
	{
		return new SAMLUserDetails(credential);
	}
}
