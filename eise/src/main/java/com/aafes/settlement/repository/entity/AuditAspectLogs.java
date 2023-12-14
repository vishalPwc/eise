
package com.aafes.settlement.repository.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@Entity

@Table(name = "AUDIT_ASPECT_LOG")
public class AuditAspectLogs
	extends
	BaseEntity
{

	// ------------------------------------------------------------------------

	/*
	 * @Id
	 * 
	 * @GeneratedValue(strategy = GenerationType.IDENTITY)
	 * 
	 * @Column(name = "ID", unique = true) private int auditAspectId;
	 */

	@Column(name = "DESCRIPTION")
	private String description;

	@Column(name = "REQUEST_TIME")
	private String requestTime;

	@Column(name = "RESPONSE_TIME")
	private String responseTime;

	@Column(name = "STATUS")
	private String status;

	@Column(name = "UUID")
	private String uuid;

	@Column(name = "REQUEST_DATA")
	@Lob
	private String requestData;

	@Column(name = "RESPONSE_DATA")
	@Lob
	private String responseData;

	@Column(name = "REQ_TYPE")
	private String reqType;

	@Column(name = "URL")
	private String url;

	@Column(name = "CREATED_BY", updatable = false)
	private String createdBy;

	@Column(name = "CREATED_DATE", updatable = false)
	private Date   createdDate;

	@Column(name = "UPDATED_BY")
	private String updatedBy;

	@Column(name = "UPDATED_DATE")
	private Date   updatedDate;

	@Transient
	@JsonSerialize
	@JsonDeserialize
	private Object request;

	@Transient
	@JsonSerialize
	@JsonDeserialize
	private Object response;

	// ------------------------------------------------------------------------

	/**
	 * @return the description
	 */

	public String getDescription() {
		return description;
	}

	/**
	 * @param p_description
	 *            the description to set
	 */

	public void setDescription(String p_description) {
		description = p_description;
	}

	/**
	 * @return the requestTime
	 */

	public String getRequestTime() {
		return requestTime;
	}

	/**
	 * @param p_requestTime
	 *            the requestTime to set
	 */

	public void setRequestTime(String p_requestTime) {
		requestTime = p_requestTime;
	}

	/**
	 * @return the responseTime
	 */

	public String getResponseTime() {
		return responseTime;
	}

	/**
	 * @param p_responseTime
	 *            the responseTime to set
	 */

	public void setResponseTime(String p_responseTime) {
		responseTime = p_responseTime;
	}

	/**
	 * @return the status
	 */

	public String getStatus() {
		return status;
	}

	/**
	 * @param p_status
	 *            the status to set
	 */

	public void setStatus(String p_status) {
		status = p_status;
	}

	/**
	 * @return the uuid
	 */

	public String getUuid() {
		return uuid;
	}

	/**
	 * @param p_uuid
	 *            the uuid to set
	 */

	public void setUuid(String p_uuid) {
		uuid = p_uuid;
	}

	/**
	 * @return the requestData
	 */

	public String getRequestData() {
		return requestData;
	}

	/**
	 * @param p_requestData
	 *            the requestData to set
	 */

	public void setRequestData(String p_requestData) {
		requestData = p_requestData;
	}

	/**
	 * @return the responseData
	 */

	public String getResponseData() {
		return responseData;
	}

	/**
	 * @param p_responseData
	 *            the responseData to set
	 */

	public void setResponseData(String p_responseData) {
		responseData = p_responseData;
	}

	/**
	 * @return the reqType
	 */
	public String getReqType() {
		return reqType;
	}

	/**
	 * @param p_reqType
	 *            the reqType to set
	 */
	public void setReqType(String p_reqType) {
		reqType = p_reqType;
	}

	/**
	 * @return the url
	 */

	public String getUrl() {
		return url;
	}

	/**
	 * @param p_url
	 *            the url to set
	 */

	public void setUrl(String p_url) {
		url = p_url;
	}

	/**
	 * @return the createdBy
	 */

	public String getCreatedBy() {
		return createdBy;
	}

	/**
	 * @param p_createdBy
	 *            the createdBy to set
	 */

	public void setCreatedBy(String p_createdBy) {
		createdBy = p_createdBy;
	}

	/**
	 * @return the createdDate
	 */

	public Date getCreatedDate() {
		return createdDate;
	}

	/**
	 * @param p_createdDate
	 *            the createdDate to set
	 */

	public void setCreatedDate(Date p_createdDate) {
		createdDate = p_createdDate;
	}

	/**
	 * @return the updatedBy
	 */

	public String getUpdatedBy() {
		return updatedBy;
	}

	/**
	 * @param p_updatedBy
	 *            the updatedBy to set
	 */

	public void setUpdatedBy(String p_updatedBy) {
		updatedBy = p_updatedBy;
	}

	/**
	 * @return the updatedDate
	 */

	public Date getUpdatedDate() {
		return updatedDate;
	}

	/**
	 * @param p_updatedDate
	 *            the updatedDate to set
	 */
	public void setUpdatedDate(Date p_updatedDate) {
		updatedDate = p_updatedDate;
	}

	/**
	 * @return the request
	 */
	public Object getRequest() {
		return request;
	}

	/**
	 * @param p_request
	 *            the request to set
	 */
	public void setRequest(Object p_request) {
		request = p_request;
	}

	/**
	 * @return the response
	 */
	public Object getResponse() {
		return response;
	}

	/**
	 * @param p_response
	 *            the response to set
	 */
	public void setResponse(Object p_response) {
		response = p_response;
	}
	// -----------------------------------------------------------------------
}
