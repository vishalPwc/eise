package com.aafes.settlement.model;

/**
 * This pojo model contains username and password fields required for simulated
 * login
 * 
 * @author Logixal Solutions Pvt Ltd
 *
 */
public class UserLogin {

	private String username;

	private String password;

	private String jSessionId;

	// -------------------------------------------------------------------------
	/**
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @param p_username
	 *            the username to set
	 */
	public void setUsername(String p_username) {
		username = p_username;
	}

	/**
	 * @param p_password
	 *            the password to set
	 */
	public void setPassword(String p_password) {
		password = p_password;
	}

	/**
	 * @return the jSessionId
	 */
	public String getjSessionId() {
		return jSessionId;
	}

	/**
	 * @param jSessionId
	 *            the jSessionId to set
	 */
	public void setjSessionId(String jSessionId) {
		this.jSessionId = jSessionId;
	}

}
// -----------------------------------------------------------------------------
// END OF FILE
// -----------------------------------------------------------------------------
