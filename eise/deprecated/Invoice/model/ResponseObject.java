package com.aafes.settlement.Invoice.model;

import java.util.List;

import com.aafes.settlement.exception.Errors;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ResponseObject {

	@JsonProperty("uuid")
	private String		 uuid;

	private int			 responseCode;

	private String		 responseMessage;

	private List<Errors> errors;

	@JsonProperty("data")
	private Object		 responsedata;

	/**
	 * @return the uuid
	 */
	public String getUuid() {
		return uuid;
	}

	/**
	 * @param uuid
	 *            the uuid to set
	 */
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	/**
	 * @return the responseCode
	 */
	public int getResponseCode() {
		return responseCode;
	}

	/**
	 * @param responseCode
	 *            the responseCode to set
	 */
	public void setResponseCode(int responseCode) {
		this.responseCode = responseCode;
	}

	/**
	 * @return the responseMessage
	 */
	public String getResponseMessage() {
		return responseMessage;
	}

	/**
	 * @param responseMessage
	 *            the responseMessage to set
	 */
	public void setResponseMessage(String responseMessage) {
		this.responseMessage = responseMessage;
	}

	/**
	 * @return the errors
	 */
	public List<Errors> getErrors() {
		return errors;
	}

	/**
	 * @param p_errors
	 *            the errors to set
	 */
	public void setErrors(List<Errors> p_errors) {
		errors = p_errors;
	}

	/**
	 * @return the responsedata
	 */
	public Object getResponsedata() {
		return responsedata;
	}

	/**
	 * @param responsedata
	 *            the responsedata to set
	 */
	public void setResponsedata(Object responsedata) {
		this.responsedata = responsedata;
	}

}
