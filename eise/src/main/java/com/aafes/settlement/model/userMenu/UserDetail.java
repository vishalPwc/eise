package com.aafes.settlement.model.userMenu;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * This Model Pojo contains user-details fields
 * 
 * @author Logixal Solutions Pvt Ltd
 *
 */
public class UserDetail {

	@JsonProperty("userName")
	private String userName;

	@JsonProperty("userId")
	private String userId;

	@JsonProperty("firstName")
	private String firstName;

	@JsonProperty("lastName")
	private String lastName;

	@JsonProperty("email")
	private String email;

	@JsonProperty("storeName")
	private String storeName;

	@JsonProperty("userRole")
	private String userRole;

	@JsonProperty("password")
	private String password;

	@JsonProperty("facilitytype")
	private String facilityType;

	@JsonProperty("storeFacilityCd")
	private String storeFacilityCd;

	@JsonProperty("storeFac7Id")
	private int	   storeFac7Id;

	@JsonProperty("storeCity")
	private String storeCity;

	@JsonProperty("redirectURL")
	private String redirectURL;

	@JsonProperty("parentDCId")
	private int	   parentDCId;

	// -------------------------------------------------------------------------
	/**
	 * @return the firstName
	 */
	public String getFirstName() {
		return firstName;
	}

	/**
	 * @return the lastName
	 */
	public String getLastName() {
		return lastName;
	}

	/**
	 * @return the storeName
	 */
	public String getStoreName() {
		return storeName;
	}

	/**
	 * @return the userRole
	 */
	public String getUserRole() {
		return userRole;
	}

	/**
	 * @param p_firstName
	 *            the firstName to set
	 */
	public void setFirstName(String p_firstName) {
		firstName = p_firstName;
	}

	/**
	 * @param p_lastName
	 *            the lastName to set
	 */
	public void setLastName(String p_lastName) {
		lastName = p_lastName;
	}

	/**
	 * @param p_storeName
	 *            the storeName to set
	 */
	public void setStoreName(String p_storeName) {
		storeName = p_storeName;
	}

	/**
	 * @param p_userRole
	 *            the userRole to set
	 */
	public void setUserRole(String p_userRole) {
		userRole = p_userRole;
	}

	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @param p_password
	 *            the password to set
	 */
	public void setPassword(String p_password) {
		password = p_password;
	}

	/**
	 * @return the facilityType
	 */
	public String getFacilityType() {
		return facilityType;
	}

	/**
	 * @param p_facilityType
	 *            the facilityType to set
	 */
	public void setFacilityType(String p_facilityType) {
		facilityType = p_facilityType;
	}

	/**
	 * @return the storeFacilityCd
	 */
	public String getStoreFacilityCd() {
		return storeFacilityCd;
	}

	/**
	 * @param p_storeFacilityCd
	 *            the storeFacilityCd to set
	 */
	public void setStoreFacilityCd(String p_storeFacilityCd) {
		storeFacilityCd = p_storeFacilityCd;
	}

	/**
	 * @return the storeFac7Id
	 */
	public int getStoreFac7Id() {
		return storeFac7Id;
	}

	/**
	 * @param p_storeFac7Id
	 *            the storeFac7Id to set
	 */
	public void setStoreFac7Id(int p_storeFac7Id) {
		storeFac7Id = p_storeFac7Id;
	}

	/**
	 * @return the storeCity
	 */
	public String getStoreCity() {
		return storeCity;
	}

	/**
	 * @param p_storeCity
	 *            the storeCity to set
	 */
	public void setStoreCity(String p_storeCity) {
		storeCity = p_storeCity;
	}

	/**
	 * @return the userName
	 */
	public String getUserName() {
		return userName;
	}

	/**
	 * @param userName
	 *            the userName to set
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}

	/**
	 * @return the userId
	 */
	public String getUserId() {
		return userId;
	}

	/**
	 * @param userId
	 *            the userId to set
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}

	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * @param email
	 *            the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * @return the redirectURL
	 */
	public String getRedirectURL() {
		return redirectURL;
	}

	/**
	 * @param redirectURL
	 *            the redirectURL to set
	 */
	public void setRedirectURL(String redirectURL) {
		this.redirectURL = redirectURL;
	}

	/**
	 * @return the parentDCId
	 */
	public int getParentDCId() {
		return parentDCId;
	}

	/**
	 * @param parentDCId
	 *            the parentDCId to set
	 */
	public void setParentDCId(int parentDCId) {
		this.parentDCId = parentDCId;
	}

	@Override
	public String toString() {

		return "User Details[ UserName:" + userName + ", UserID:" + userId
				+ ", FirstName:" + firstName + ", LastName:" + lastName
				+ ", Email:" + email + ", UserRole:" + userRole + ", Fac7ID:"
				+ storeFac7Id + ", Parent DC ID:" + parentDCId
				+ ", FacilityType:" + facilityType + ", Redirect URL:"
				+ redirectURL + " ]";

	}
}
// -----------------------------------------------------------------------------
// END OF FILE
// -----------------------------------------------------------------------------
