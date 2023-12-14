package com.aafes.settlement.exception;

public class Errors {

	/**
	 * The key which will be looked-up in message.properties
	 */
	private String errorCode;
	/**
	 * The value of errorCode from message.properties
	 */
	private String errorMessage;

	public Errors(String p_errorCode, String p_errorMessage) {
		super();
		errorCode = p_errorCode;
		errorMessage = p_errorMessage;
	}

	/**
	 * @return the errorCode
	 */
	public String getErrorCode() {
		return errorCode;
	}

	/**
	 * @param p_errorCode
	 *            the errorCode to set
	 */
	public void setErrorCode(String p_errorCode) {
		errorCode = p_errorCode;
	}

	/**
	 * @return the errorMessage
	 */
	public String getErrorMessage() {
		return errorMessage;
	}

	/**
	 * @param p_errorMessage
	 *            the errorMessage to set
	 */
	public void setErrorMsg(String p_errorMessage) {
		errorMessage = p_errorMessage;
	}

}