package com.aafes.settlement.exception;

import com.aafes.settlement.validators.ValidationErrors;

public class NodeNotFoundException
	extends
	RuntimeException
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String errorMsg;

	public ValidationErrors errors;
	/**
	 * @return the errorMsg
	 */
	public String getErrorMsg() {
		return errorMsg;
	}

	/**
	 * @param p_errorMsg the errorMsg to set
	 */
	public void setErrorMsg(String p_errorMsg) {
		errorMsg = p_errorMsg;
	}
	
	public NodeNotFoundException(String p_msg) {
		errorMsg = p_msg;	
	}
	
	public NodeNotFoundException(ValidationErrors p_errors) {
		errors = p_errors;	
	}
	
	public NodeNotFoundException(String p_msg, ValidationErrors p_errors) {
		errorMsg = p_msg;
		errors = p_errors;
	}
}
