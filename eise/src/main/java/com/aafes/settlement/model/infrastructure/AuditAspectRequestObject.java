package com.aafes.settlement.model.infrastructure;

/**
 *  Request Object From UI to Erpa for Fetching AuditAspectLog 
 *
 */
public class AuditAspectRequestObject {

	//-------------------------------------------------------------------------
	private Integer records;
	
	private String  reqType;

	//-------------------------------------------------------------------------
	/**
	 * @return the records
	 */
	public Integer getRecords() {
		return records;
	}

	/**
	 * @param p_records the records to set
	 */
	public void setRecords(Integer p_records) {
		records = p_records;
	}

	/**
	 * @return the reqType
	 */
	public String getReqType() {
		return reqType;
	}

	/**
	 * @param p_reqType the reqType to set
	 */
	public void setReqType(String p_reqType) {
		reqType = p_reqType;
	}
	//-------------------------------------------------------------------------
}
//-----------------------------------------------------------------------------
//END OF FILE
//-----------------------------------------------------------------------------