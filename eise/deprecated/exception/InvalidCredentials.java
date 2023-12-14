package com.aafes.settlement.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
public class InvalidCredentials
	extends
	RuntimeException
{

	private static final long serialVersionUID = 1L;
	private String			  field;

	public InvalidCredentials(
			String field
	)
	{
		super(field);
		this.field = field;
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

}
