package com.aafes.settlement.core.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ErrorsResponse {

	@JsonProperty("errorCode")
	private String mErrorCode;

	@JsonProperty("errorMessage")
	private String mErrorMessage;

	/**
	 * @return the errorCode
	 */
	public String getErrorCode() {
		return mErrorCode;
	}

	/**
	 * @param pErrorCode
	 *            the errorCode to set
	 */
	public void setErrorCode(String pErrorCode) {
		mErrorCode = pErrorCode;
	}

	/**
	 * @return the errorMessage
	 */
	public String getErrorMessage() {
		return mErrorMessage;
	}

	/**
	 * @param pErrorMessage
	 *            the errorMessage to set
	 */
	public void setErrorMessage(String pErrorMessage) {
		mErrorMessage = pErrorMessage;
	}

}
