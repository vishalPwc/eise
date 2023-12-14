package com.aafes.settlement.model.jwt;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;

/**
 * 
 * This Model is used for JWT request
 *
 */
public class KeyRequest
	implements Serializable
{
	private static final long serialVersionUID = 5926468583005150707L;

	private String			  username;

	private String			  password;

	private String			  externalSystem;


	// ------------------------------------------------------------------------
	// need default constructor for JSON Parsing
	public KeyRequest() {

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

	
	// -------------------------------------------------------------------------
}
// -----------------------------------------------------------------------------
// END OF FILE
// -----------------------------------------------------------------------------