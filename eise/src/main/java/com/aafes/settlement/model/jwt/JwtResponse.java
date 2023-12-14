package com.aafes.settlement.model.jwt;

import java.io.Serializable;

/**
 * 
 * This Model is used for JWT Response
 *
 */
public class JwtResponse
	implements Serializable
{

	private static final long serialVersionUID = -8091879091924046844L;

	private String			  jwttoken;

	private String			  expiryTime;

	private String			  tokenType;

	/**
	 * @return the jwttoken
	 */
	public String getJwttoken() {
		return jwttoken;
	}

	/**
	 * @param p_jwttoken
	 *            the jwttoken to set
	 */
	public void setJwttoken(String p_jwttoken) {
		jwttoken = p_jwttoken;
	}

	/**
	 * @return the expiryTime
	 */
	public String getExpiryTime() {
		return expiryTime;
	}

	/**
	 * @param p_expiryTime
	 *            the expiryTime to set
	 */
	public void setExpiryTime(String p_expiryTime) {
		expiryTime = p_expiryTime;
	}

	/**
	 * @return the tokenType
	 */
	public String getTokenType() {
		return tokenType;
	}

	/**
	 * @param p_tokenType
	 *            the tokenType to set
	 */
	public void setTokenType(String p_tokenType) {
		tokenType = p_tokenType;
	}

	// -------------------------------------------------------------------------

	// -------------------------------------------------------------------------
}
// -----------------------------------------------------------------------------
// END OF FILE
// -----------------------------------------------------------------------------