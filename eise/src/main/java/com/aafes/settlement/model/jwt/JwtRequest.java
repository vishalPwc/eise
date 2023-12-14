package com.aafes.settlement.model.jwt;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;

/**
 * 
 * This Model is used for JWT request
 *
 */
public class JwtRequest
	implements Serializable
{
	private static final long serialVersionUID = 5926468583005150707L;

	@ApiModelProperty(hidden = true)
	private String			  username;

	@ApiModelProperty(hidden = true)
	private String			  password;

	@ApiModelProperty(hidden = true)
	private String			  externalSystem;

	private String			  key;

	// ------------------------------------------------------------------------
	// need default constructor for JSON Parsing
	public JwtRequest() {

	}

	public String getUsername() {
		return this.username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * @return the externalSystem
	 */
	public String getExternalSystem() {
		return externalSystem;
	}

	/**
	 * @param p_externalSystem
	 *            the externalSystem to set
	 */
	public void setExternalSystem(String p_externalSystem) {
		externalSystem = p_externalSystem;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}
	// -------------------------------------------------------------------------
}
// -----------------------------------------------------------------------------
// END OF FILE
// -----------------------------------------------------------------------------