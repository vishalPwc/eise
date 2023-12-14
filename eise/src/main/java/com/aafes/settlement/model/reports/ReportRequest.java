package com.aafes.settlement.model.reports;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;

public class ReportRequest {

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
	private LocalDate fromDate;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
	private LocalDate toDate;

	private String	  orderId;

	private String	  orderBy;

	private String	  sortByField;

	private String	  fieldValue;

	/**
	 * @return the fromDate
	 */
	public LocalDate getFromDate() {
		return fromDate;
	}

	/**
	 * @param p_fromDate
	 *            the fromDate to set
	 */
	public void setFromDate(LocalDate p_fromDate) {
		fromDate = p_fromDate;
	}

	/**
	 * @return the toDate
	 */
	public LocalDate getToDate() {
		return toDate;
	}

	/**
	 * @param p_toDate
	 *            the toDate to set
	 */
	public void setToDate(LocalDate p_toDate) {
		toDate = p_toDate;
	}

	/**
	 * @return the orderId
	 */
	public String getOrderId() {
		return orderId;
	}

	/**
	 * @param p_orderId
	 *            the orderId to set
	 */
	public void setOrderId(String p_orderId) {
		orderId = p_orderId;
	}

	public String getOrderBy() {
		return orderBy;
	}

	public void setOrderBy(String orderBy) {
		this.orderBy = orderBy;
	}

	public String getSortByField() {
		return sortByField;
	}

	public void setSortByField(String sortByField) {
		this.sortByField = sortByField;
	}

	public String getFieldValue() {
		return fieldValue;
	}

	public void setFieldValue(String fieldValue) {
		this.fieldValue = fieldValue;
	}

}
