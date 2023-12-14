package com.aafes.settlement.repository.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Table;

@Entity
@Table(name = "SAML_USER_INFO")
public class SamlUserInfoEntity
	extends
	BaseEntity
{

	@Column(name = "SESSION_ID")
	private String sessionId;

	@Column(name = "USER_INFO", columnDefinition = "CLOB NOT NULL")
	@Lob
	private String userInfo;

	// -------------------------------------------------------------------------
	/**
	 * @return the sessionId
	 */
	public String getSessionId() {
		return sessionId;
	}

	/**
	 * @param p_sessionId
	 *            the sessionId to set
	 */
	public void setSessionId(String p_sessionId) {
		sessionId = p_sessionId;
	}

	/**
	 * @return the userInfo
	 */
	public String getUserInfo() {
		return userInfo;
	}

	/**
	 * @param p_userInfo
	 *            the userInfo to set
	 */
	public void setUserInfo(String p_userInfo) {
		userInfo = p_userInfo;
	}

	// -------------------------------------------------------------------------
}
