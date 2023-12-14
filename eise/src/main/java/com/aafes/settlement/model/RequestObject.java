package com.aafes.settlement.model;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.boot.jackson.JsonComponent;

import com.fasterxml.jackson.annotation.JsonProperty;

@JsonComponent
public class RequestObject {

	@JsonProperty("uuid")
	private String				uuid;

	private int					returnCode;

	private String				returnMessage;

	@JsonProperty("data")
	private Object				requestdata;

	@JsonProperty("reqdata")
	private Object				requestBodydata;

	private Map<String, Object>	requestParamdata = new HashMap<String,
			Object>();

	private String				serviceType;

	private Class<?>			className;

	private String				protocol;

	private String				destinationName;

	/**
	 * @param uuid
	 */
	public RequestObject() {
		super();
		this.uuid = UUID.randomUUID().toString().replace("-", "");
	}

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
	 * @return the returnCode
	 */
	public int getReturnCode() {
		return returnCode;
	}

	/**
	 * @param returnCode
	 *            the returnCode to set
	 */
	public void setReturnCode(int returnCode) {
		this.returnCode = returnCode;
	}

	/**
	 * @return the returnMessage
	 */
	public String getReturnMessage() {
		return returnMessage;
	}

	/**
	 * @param returnMessage
	 *            the returnMessage to set
	 */
	public void setReturnMessage(String returnMessage) {
		this.returnMessage = returnMessage;
	}

	/**
	 * @return the requestdata
	 */
	public Object getRequestdata() {
		return requestdata;
	}

	/**
	 * @param p_requestdata
	 *            the requestdata to set
	 */
	public void setRequestdata(Object p_requestdata) {
		requestdata = p_requestdata;
	}

	/**
	 * @return the serviceType
	 */
	public String getServiceType() {
		return serviceType;
	}

	/**
	 * @param serviceType
	 *            the serviceType to set
	 */
	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	}

	/**
	 * @return the className
	 */
	public Class<?> getClassName() {
		return className;
	}

	/**
	 * @param className
	 *            the className to set
	 */
	public void setClassName(Class<?> className) {
		this.className = className;
	}

	/**
	 * @return the requestBodydata
	 */
	public Object getRequestBodydata() {
		return requestBodydata;
	}

	/**
	 * @param requestBodydata
	 *            the requestBodydata to set
	 */
	public void setRequestBodydata(Object requestBodydata) {
		this.requestBodydata = requestBodydata;
	}

	/**
	 * @return the protocol
	 */
	public String getProtocol() {
		return protocol;
	}

	/**
	 * @param protocol
	 *            the protocol to set
	 */
	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}

	/**
	 * @return the destinationName
	 */
	public String getDestinationName() {
		return destinationName;
	}

	/**
	 * @param destinationName
	 *            the destinationName to set
	 */
	public void setDestinationName(String destinationName) {
		this.destinationName = destinationName;
	}

	/**
	 * @return the requestParamdata
	 */
	public Map<String, Object> getRequestParamdata() {
		return requestParamdata;
	}

	/**
	 * @param p_requestParamdata
	 *            the requestParamdata to set
	 */
	public void setRequestParamdata(Map<String, Object> p_requestParamdata) {
		requestParamdata = p_requestParamdata;
	}

}
