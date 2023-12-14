package com.aafes.settlement.model;

import com.aafes.settlement.model.userMenu.UserDetail;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 
 * @author Logixal Solutions Pvt Ltd
 *
 */
public class UserMenu {

	@JsonProperty("userDetails")
	private UserDetail userDetails;

	// -------------------------------------------------------------------------
	/**
	 * @return the userDetails
	 */
	public UserDetail getUserDetails() {
		return userDetails;
	}

	/**
	 * @param p_userDetails
	 *            the userDetails to set
	 */
	public void setUserDetails(UserDetail p_userDetails) {
		userDetails = p_userDetails;
	}

}
