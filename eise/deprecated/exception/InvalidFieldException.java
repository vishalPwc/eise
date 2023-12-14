package com.aafes.settlement.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
public class InvalidFieldException
	extends
	RuntimeException
{

	private static final long serialVersionUID = 1L;
	private String			  field;
	private String			  encrypt;

	public InvalidFieldException(
			String field
	)
	{
		super(field);
		this.field = field;
	}

	public InvalidFieldException(
			String field, String encrypt
	)
	{
		super(field);
		this.field = field;
		this.encrypt = encrypt;
	}

	/**
	 * @return the field
	 */
	public String getField() {
		return field;
	}

	/**
	 * @param field
	 *            the field to set
	 */
	public void setField(
			String field
	)
	{
		this.field = field;
	}

	/**
	 * @return the encrypt
	 */
	public String getEncrypt() {
		return encrypt;
	}

	/**
	 * @param encrypt
	 *            the encrypt to set
	 */
	public void setEncrypt(
			String encrypt
	)
	{
		this.encrypt = encrypt;
	}

}
