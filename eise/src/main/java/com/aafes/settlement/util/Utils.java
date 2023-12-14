package com.aafes.settlement.util;

// ----------------------------------------------------------------------------
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Clob;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.TimeZone;

import javax.sql.rowset.serial.SerialClob;

import com.aafes.settlement.core.model.settlement.SettlementRO;
import com.aafes.settlement.core.payment.PaymentHeader;
import com.aafes.settlement.core.payment.PaymentMethod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;

import com.aafes.settlement.configuration.GlobalParams;
import com.aafes.settlement.configuration.MessagePropertyConfig;
import com.aafes.settlement.constant.Constants;
import com.aafes.settlement.constant.ErrorConstants;
import com.aafes.settlement.core.response.EISEResponseGeneric;
import com.aafes.settlement.exception.Errors;
import com.aafes.settlement.model.RequestObject;
import com.aafes.settlement.model.ResponseObject;
import com.fasterxml.jackson.databind.ObjectMapper;

// ----------------------------------------------------------------------------

public class Utils {

	private static final Logger LOGGER = LogManager.getLogger(Utils.class);

	/**
	 * This method converts object to JSON
	 * 
	 * @param p_obj
	 * @return String
	 */
	public static String convertToJson(Object p_obj) {
		ObjectMapper l_mapper = new ObjectMapper();
		String l_json = null;
		try {
			l_json = l_mapper.writeValueAsString(p_obj);
		} catch (IOException e) {
			e.printStackTrace();
		}

		return l_json;
	}
	// ------------------------------------------------------------------------

	/**
	 * 
	 * @return current time-stamp in String
	 */
	public static String getCurrentDateInString() {

		return LocalDateTime.now().toString();
	}

	// -------------------------------------------------------------------------
	/**
	 * This method will return Date-Time for UTC timezone
	 * 
	 * @return
	 */
	public static Date getCurrentDate() {

		// UTC TIMEZONE

		Calendar l_calender = Calendar.getInstance();
		TimeZone l_timezone = l_calender.getTimeZone();
		int l_offset = l_timezone.getRawOffset();
		if (l_timezone.inDaylightTime(new Date())) {
			l_offset = l_offset + l_timezone.getDSTSavings();
		}
		int l_offsetHrs = l_offset / 1000 / 60 / 60;
		int l_offsetMins = l_offset / 1000 / 60 % 60;
		l_calender.add(Calendar.HOUR_OF_DAY, (-l_offsetHrs));
		l_calender.add(Calendar.MINUTE, (-l_offsetMins));
		return l_calender.getTime();

		// Local Timezone
		/*
		 * Date l_dateNow = new Date(); return l_dateNow;
		 */
	}

	// ------------------------------------------------------------------------
	/**
	 * This method convert current date to CST/CDT format
	 * 
	 * @return LocalDateTime
	 */
	public static LocalDateTime getCDTDate() {
		Instant nowUtc = Instant.now();
		ZoneId l_cdtZone = ZoneId.of(Constants.CDT_TIME_ZONE);
		ZonedDateTime l_cdtZoneDateTime = ZonedDateTime.ofInstant(
				nowUtc, l_cdtZone
		);
		LOGGER.info(
				"CST/CDT current date :" + l_cdtZoneDateTime.toLocalDateTime()
						.toString()
		);
		return l_cdtZoneDateTime.toLocalDateTime();
	}

	// ------------------------------------------------------------------------
	/**
	 * This method converts object to Clob
	 * 
	 * @param object
	 * @return Clob
	 * @throws Exception
	 */
	public static Clob objToClob(Object p_obj)
			throws Exception
	{

		ObjectMapper l_objMapper = new ObjectMapper();
		String l_jsonStr = l_objMapper.writeValueAsString(p_obj);
		Clob l_clob = new SerialClob(l_jsonStr.toCharArray());
		return l_clob;
	}

	// ------------------------------------------------------------------------
	/**
	 * This method sets the errors in ResponseObject
	 */
	public static List<Errors> getErrors(
			Map<String, String> p_messages,
			MessagePropertyConfig p_messagePropConf
	)
	{
		List<Errors> l_errorList = new ArrayList<Errors>();
		String[] l_msgFormatValues = null;
		Errors l_error = null;
		String l_errorCode = null;
		for (Entry<String, String> msg : p_messages.entrySet()) {

			/* Check if multiple terms are to be replaced in error message */
			if (msg.getValue().contains(",")) {
				l_msgFormatValues = msg.getValue().split(",");
			} else {
				l_msgFormatValues = new String[1];
				l_msgFormatValues[0] = msg.getValue();
			}

			l_errorCode = msg.getKey();
			if (p_messagePropConf.getFormat().containsKey(l_errorCode)) {

				l_error = new Errors(
						l_errorCode,
						MessageFormat.format(
								p_messagePropConf.getFormat().get(l_errorCode),
								(Object[]) l_msgFormatValues
						)
				);
			} else {
				l_error = new Errors(
						l_errorCode, p_messagePropConf.getFormat()
								.get(ErrorConstants.INTERNAL_ERROR)
				);
			}
			l_errorList.add(l_error);
		}
		return l_errorList;
	}

	// ------------------------------------------------------------------------
	/**
	 * This method sets the Success responseCode,message and uuid in
	 * ResponseObject
	 *
	 * @param p_reqObj
	 * @param p_respObj
	 */
	public static void setSuccessInEISEResponseObj(
			EISEResponseGeneric p_eiseResponseGeneric
	)
	{
		p_eiseResponseGeneric.setResponseCode("200");
		p_eiseResponseGeneric.setResponseMessage(Constants.SUCCESS);
	}

	// ------------------------------------------------------------------------
	/**
	 * This method sets the Success responseCode,message and uuid in
	 * ResponseObject
	 *
	 * @param p_reqObj
	 * @param p_respObj
	 */
	public static void setSuccessInResponseObj(
			RequestObject p_requestObject,
			ResponseObject p_responseObject
	)
	{
		p_responseObject.setUuid(p_requestObject.getUuid());
		p_responseObject.setResponseCode(0);
		p_responseObject.setResponseMessage(Constants.SUCCESS);
	}

	// -------------------------------------------------------------------------

	/**
	 * This method sets the failure responseCode, message in ResponseObject
	 * 
	 * @param p_responseObject
	 * 
	 * @param p_respObj
	 */
	public static void
			setFailureInResponseObj(ResponseObject p_responseObject)
	{
		p_responseObject.setResponseCode(1);
		p_responseObject.setResponseMessage(Constants.FAILURE);

	}
	// -------------------------------------------------------------------------

	/**
	 * This method sets the failure responseCode, message in ResponseSettlement
	 * : p_responseSettlement
	 * 
	 * @param p_responseObject
	 * 
	 * @param p_respObj
	 */
	public static void setFailureInEISEResponseObj(
			EISEResponseGeneric p_eiseResponseGeneric
	)
	{
		p_eiseResponseGeneric.setResponseCode("400");
		p_eiseResponseGeneric.setResponseMessage(Constants.FAILURE);

	}

	// -------------------------------------------------------------------------
	/**
	 * This method will return Date-Time for UTC timezone
	 * 
	 * @return
	 */
	public static Date getCurrentDateTime() {

		// UTC TIMEZONE

		Calendar l_calender = Calendar.getInstance();
		TimeZone l_timezone = l_calender.getTimeZone();
		int l_offset = l_timezone.getRawOffset();
		if (l_timezone.inDaylightTime(new Date())) {
			l_offset = l_offset + l_timezone.getDSTSavings();
		}
		int l_offsetHrs = l_offset / 1000 / 60 / 60;
		int l_offsetMins = l_offset / 1000 / 60 % 60;
		l_calender.add(Calendar.HOUR_OF_DAY, (-l_offsetHrs));
		l_calender.add(Calendar.MINUTE, (-l_offsetMins));
		return l_calender.getTime();

		// Local Timezone
		// Date l_dateNow = new Date();
		// return l_dateNow;
	}

	// -------------------------------------------------------------------------
	/**
	 * In case of a runtime exception, this method sets the value of the
	 * exception message in response object
	 * 
	 * @param p_respObj
	 * @param p_e
	 * @param p_messagePropConf
	 */
	public static void setExceptionInEISEResponseObj(
			EISEResponseGeneric p_eiseResponseGeneric,
			Exception p_e,
			MessagePropertyConfig p_messagePropConf
	)
	{
		Utils.setFailureInEISEResponseObj(p_eiseResponseGeneric);

		HashMap<String, String> errors = new HashMap<String, String>();
		errors.put(
				ErrorConstants.INTERNAL_ERROR,
				p_e.getMessage() != null ? p_e.getMessage() : ""
		);

		p_eiseResponseGeneric
				.setErrors(Utils.getErrors(errors, p_messagePropConf));
	}

	// -------------------------------------------------------------------------
	/**
	 * In case of a runtime exception, this method sets the value of the
	 * exception message in response object
	 * 
	 * @param p_respObj
	 * @param p_e
	 * @param p_messagePropConf
	 */
	public static void setExceptionInResponseObj(
			ResponseObject p_responseObject,
			Exception p_e,
			MessagePropertyConfig p_messagePropConf
	)
	{

		Utils.setFailureInResponseObj(p_responseObject);
		HashMap<String, String> errors = new HashMap<String, String>();
		errors.put(ErrorConstants.INTERNAL_ERROR, p_e.getMessage());
		p_responseObject.setErrors(Utils.getErrors(errors, p_messagePropConf));
	}

	// ------------------------------------------------------------------------

	// ------------------------------------------------------------------------
	/**
	 * Checks if the given param is null or empty for instances of: String,
	 * String[],integer . Returns false by default.
	 * 
	 * @param p_object
	 * @return true if null or empty; false by default, not an instance of the
	 *         above, or otherwise
	 */
	public static boolean isNull(Object p_object) {
		boolean l_result = false;
		if (p_object == null) {
			l_result = true;
		} else if (p_object instanceof String) {
			if (((String) p_object).isEmpty()) {
				l_result = true;
			}
		} else if (p_object instanceof String[]) {
			if (((String) p_object).length() == 0) {
				l_result = true;
			}
		} else if (p_object instanceof Integer) {
			if ((int) p_object == 0) {
				l_result = true;
			}
		} else if (p_object instanceof Float) {
			if ((float) p_object == 0) {
				l_result = true;
			}
		} else if (p_object instanceof List) {
			if (((List<?>) p_object).isEmpty()) {
				l_result = true;
			}
		}
		return l_result;
	}

	// ------------------------------------------------------------------------
	/**
	 * This method will return utc date in ISO-Date-Pattern Format without
	 * Seperator.
	 * 
	 * @return
	 */
	public static String getUtcDateInString() {

		Date l_date = getCurrentDate();

		SimpleDateFormat l_simpleDateFormat = new SimpleDateFormat(
				Constants.ISO_TIME_FORMAT
		);

		String l_dateString = l_simpleDateFormat.format(l_date);
		return l_dateString;
	}

	// ------------------------------------------------------------------------

	/**
	 * This method generates encrypted key for MAO
	 * 
	 * @param p_extSystem
	 * @param p_userName
	 * @param p_password
	 * @param p_secret
	 * @return
	 */
	public static String getEncryptedMaoKey(
			String p_extSystem,
			String p_userName,
			String p_password,
			String p_secret
	)
	{
		String l_key = p_extSystem + Constants.MAO_TOKEN_SEPARATOR + p_userName
				+ Constants.MAO_TOKEN_SEPARATOR + p_password;
		String l_encryptedKey = AESEncryptDecryptUtil.encrypt(l_key, p_secret);
		return l_encryptedKey;

	}
	// ------------------------------------------------------------------------

	/**
	 * This method decrypts the key sent from MAO for accessing getToken API
	 * 
	 * @param p_encryptedKey
	 * @param p_secret
	 * @return
	 */
	public static String
			getDecryptedMaoKey(String p_encryptedKey, String p_secret)
	{
		String l_decryptedKey = AESEncryptDecryptUtil.decrypt(
				p_encryptedKey,
				p_secret
		);
		return l_decryptedKey;
	}

	/**
	 * This method checks if the mao key is valid against the application.yml
	 * 
	 * @param p_maoKey
	 * @return
	 */
	public static boolean validateMaoKey(String p_maoKey) {
		String[] keyItems = p_maoKey.split(Constants.MAO_TOKEN_SEPARATOR);
		String extSystem = keyItems[0];
		String username = keyItems[1];
		String password = keyItems[2];
		boolean isValid = false;
		if (!isNull(extSystem) && !isNull(username) && !isNull(password)) {
			if (
				extSystem.equals(GlobalParams.jwtExtSystem)
						&& username.equals(GlobalParams.jwtUsername)
						&& password.equals(GlobalParams.jwtPassword)
			)
			{
				isValid = true;
			}

		}
		return isValid;
	}

	// ------------------------------------------------------------------------

	/**
	 * This method will return INitial properties for the Springboot
	 * 
	 * @return Properties :Initial properties for Spring
	 */
	public static Properties getSpringInitProperties() {

		LOGGER.info("Loding Spring Initial properties");
		Properties props = new Properties();
		String eiseHome = System.getProperty("EISE_HOME");
		String profile = System.getProperty("EISE_PROFILE");
		LOGGER.debug("EISE_HOME From Runtime argument::" + eiseHome);
		LOGGER.debug("Spring Profile From Runtime argument::" + profile);

		if (profile != null && !"".equals(profile)) {
			props.put("spring.profiles.active", profile);
		} else {
			props.put("spring.profiles.active", "STAGE_ONE");
		}
		LOGGER.info("Properties ::  {}", props);
		return props;
	}

	// --------------------------------------------------------------------------
	/**
	 * This method compare values for Greaterthan
	 * 
	 * @param p_01
	 * 
	 * @param p_02
	 * 
	 * @return boolean
	 */
	public static boolean isValueGreater(BigDecimal p_01, BigDecimal p_02) {

		boolean l_isGreater = false;
		if (p_01.compareTo(p_02) == 1) {
			l_isGreater = true;
		}
		return l_isGreater;
	}

	// --------------------------------------------------------------------------
	/**
	 * This method compare values for Lessthan
	 * 
	 * @param p_01
	 * 
	 * @param p_02
	 * 
	 * @return boolean
	 */
	public static boolean
			isValueLesserOrEqual(BigDecimal p_01, BigDecimal p_02)
	{

		boolean l_isLesser = false;
		if (p_01.compareTo(p_02) <= 0) {
			l_isLesser = true;
		}
		return l_isLesser;
	}

	// -------------------------------------------------------------------------
	/**
	 * 
	 * @param p_page
	 * @param p_records
	 * @param p_sort
	 * @return
	 */
	public static Pageable getPageable(
			int p_page, int p_records, String p_sort
	)
	{

		Pageable pageable = null;

		if (!Utils.isNull(p_sort)) {
			pageable = PageRequest.of(
					p_page, p_records, Sort.by(Direction.DESC, p_sort)
			);

		} else {
			pageable = PageRequest.of(
					p_page, p_records
			);
		}

		return pageable;
	}

	// --------------------------------------------------------------------------
	/**
	 * 
	 * @param input
	 * @return
	 */
	public static Date getStartTime(final Date input) {
		final Calendar cal = Calendar.getInstance();
		cal.setTime(input);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		return cal.getTime();
	}

	// --------------------------------------------------------------------------
	/**
	 * 
	 * @param input
	 * @return
	 */
	public static Date getEndTime(final Date input) {
		final Calendar cal = Calendar.getInstance();
		cal.setTime(input);
		cal.set(Calendar.HOUR_OF_DAY, 23);
		cal.set(Calendar.MINUTE, 59);
		cal.set(Calendar.SECOND, 59);
		cal.set(Calendar.MILLISECOND, 999);
		return cal.getTime();
	}
	// --------------------------------------------------------------------------

	public static Date getUTCTime(final Date date) {

		// UTC TIMEZONE
		Calendar l_calender = Calendar.getInstance();
		Calendar l_cal = Calendar.getInstance();
		l_cal.setTime(date);

		l_calender.set(Calendar.DATE, l_cal.get(Calendar.DATE));
		l_calender.set(Calendar.MONTH, l_cal.get(Calendar.MONTH));
		l_calender.set(Calendar.YEAR, l_cal.get(Calendar.YEAR));

		// l_calender.set(date.getYear(), date.getMonth(), date.getDate());
		/*
		 * l_calender.set(Calendar.DATE, date.getDate());
		 * l_calender.set(Calendar.MONTH, date.getMonth());
		 * l_calender.set(Calendar.YEAR, 2020);
		 */

		TimeZone l_timezone = l_calender.getTimeZone();
		int l_offset = l_timezone.getRawOffset();
		if (l_timezone.inDaylightTime(date)) {
			l_offset = l_offset + l_timezone.getDSTSavings();
		}
		int l_offsetHrs = l_offset / 1000 / 60 / 60;
		int l_offsetMins = l_offset / 1000 / 60 % 60;
		// l_calender.setTime(date);
		l_calender.add(Calendar.HOUR_OF_DAY, (-l_offsetHrs));
		l_calender.add(Calendar.MINUTE, (-l_offsetMins));
		return l_calender.getTime();

		// Local Timezone
		/*
		 * Date l_dateNow = new Date(); return l_dateNow;
		 */
	}

	/**
	 * Method returns milstar card used in the order or not.
	 *
	 * @param p_settlementRequest
	 * @return boolean
	 */
	public static boolean isMilstarCard(SettlementRO p_settlementRequest) {
		boolean l_isMilstarCard = Boolean.FALSE;
		for (PaymentHeader l_payementHeader :p_settlementRequest.getPaymentHeader()) {
			for (PaymentMethod l_payementMethod : l_payementHeader.getPaymentMethod()) {
				if (l_payementMethod.getCardType().getCardTypeId().equalsIgnoreCase(Constants.MILSTAR_CARD)) {
					l_isMilstarCard = Boolean.TRUE;
				}
			}
		}
		return l_isMilstarCard;
	}
}
// ----------------------------------------------------------------------------
// END OF FILE
// ----------------------------------------------------------------------------