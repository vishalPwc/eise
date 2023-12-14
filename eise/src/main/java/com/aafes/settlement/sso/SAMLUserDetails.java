package com.aafes.settlement.sso;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.opensaml.saml2.core.Attribute;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.saml.SAMLCredential;
import org.springframework.security.saml.userdetails.SAMLUserDetailsService;

import com.aafes.settlement.model.userMenu.UserDetail;
import com.aafes.settlement.util.Utils;

/**
 * A default Implementation of {@link UserDetails} for Spring Boot Security
 * SAML. This simple implementation hardly covers all security aspects since
 * it's mostly hardcoded. I.E. accounts are never locked, expired, or disabled,
 * and always eturn the same granted authority "ROLE_USER". Consider
 * implementing your own {@link UserDetails} and {@link SAMLUserDetailsService}.
 */
public class SAMLUserDetails
	implements UserDetails
{
	/**
	 * 
	 */
	private static final long	serialVersionUID = 1L;

	private static final Logger	LOGGER			 = LogManager.getLogger(
			SAMLUserDetails.class
	);

	private SAMLCredential		samlCredential;

	public SAMLUserDetails(SAMLCredential samlCredential) {
		this.samlCredential = samlCredential;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return Collections.singletonList(
				new SimpleGrantedAuthority("ROLE_USER")
		);
	}

	@Override
	public String getPassword() {
		return "";
	}

	@Override
	public String getUsername() {
		return samlCredential.getNameID().getValue();
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

	public String getAttribute(String name) {
		String value = samlCredential.getAttributeAsString(name);
		return value != null ? value : "";
	}

	public String[] getAttributeArray(String name) {
		return samlCredential.getAttributeAsStringArray(name);
	}

	/**
	 * This Method will return All the Claim attributes as a MAP
	 * 
	 * @return Map
	 */
	public Map<String, String> getSamlAttributes() {

		Map<String, String> mappings = new HashMap<>();
		for (Attribute l_attribute : samlCredential.getAttributes()) {
			String l_claimName = l_attribute.getName();

			String l_claimValue = samlCredential.getAttributeAsString(
					l_claimName
			);

			LOGGER.debug(
					"Attribiute Name ,Value :: " + l_claimName + " = "
							+ samlCredential.getAttributeAsString(l_claimName)
			);

			if (!Utils.isNull(l_claimValue))
				mappings.put(
						l_claimName, samlCredential.getAttributeAsString(
								l_claimName
						)
				);

			/*
			 * if (names.contains(name)) { mappings.put(name,
			 * samlCredential.getAttributeAsString(name)); }
			 */
		}
		return mappings;
	}

	/**
	 * This Method will set the claims in UserDetail Object and Returns the
	 * UserDetail object
	 * 
	 * @return UserDetail
	 */
	public UserDetail getUserDetails() {
		LOGGER.info("START getUserDetails()");
		UserDetail l_userDetail = new UserDetail();
		l_userDetail.setUserName(getUsername());
		l_userDetail.setUserId(
				samlCredential.getAttributeAsString("User.UserId")
		);
		l_userDetail.setFirstName(
				samlCredential.getAttributeAsString("User.FirstName")
		);
		l_userDetail.setLastName(
				samlCredential.getAttributeAsString("User.LastName")
		);
		l_userDetail.setEmail(
				samlCredential.getAttributeAsString(
						"http://schemas.xmlsoap.org/ws/2005/05/identity/claims/emailaddress"
				)
		);
		l_userDetail.setUserRole(
				samlCredential.getAttributeAsString(
						"http://schemas.microsoft.com/ws/2008/06/identity/claims/role"
				)
		);

		l_userDetail.setRedirectURL(samlCredential.getRelayState());

		LOGGER.debug("User Details From ADFS::" + l_userDetail.toString());
		LOGGER.info("END getUserDetails()");
		return l_userDetail;
	}

	/**
	 * This Method will return All the Claim attributes as a MAP
	 * 
	 * @return Map
	 */
	public Map<String, String> getAttributes() {
		return samlCredential.getAttributes().stream()
				.collect(Collectors.toMap(Attribute::getName, this::getValue));
	}

	/**
	 * This Method will return All the Claim attributes as a MAP
	 * 
	 * @return Map
	 */
	public Map<String, String[]> getAttributesArrays() {
		return samlCredential.getAttributes().stream()
				.collect(
						Collectors.toMap(
								Attribute::getName, this::getValueArray
						)
				);
	}

	private String getValue(Attribute attribute) {
		return getAttribute(attribute.getName());
	}

	private String[] getValueArray(Attribute attribute) {
		return getAttributeArray(attribute.getName());
	}
}
