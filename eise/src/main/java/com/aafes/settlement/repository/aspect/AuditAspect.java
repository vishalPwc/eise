
package com.aafes.settlement.repository.aspect;

import java.io.IOException;
import java.util.StringJoiner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.aafes.settlement.configuration.jwtconfig.JwtTokenUtil;
import com.aafes.settlement.constant.AuthenticationConstants;
import com.aafes.settlement.constant.Constants;
import com.aafes.settlement.core.request.EISERequestGeneric;
import com.aafes.settlement.core.response.EISEResponseGeneric;
import com.aafes.settlement.repository.AuditAspectLogsRepo;
import com.aafes.settlement.repository.OrderInvoiceDetailsRepo;
import com.aafes.settlement.repository.entity.AuditAspectLogs;
import com.aafes.settlement.repository.entity.OrderInvoiceDetails;
import com.aafes.settlement.util.Utils;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
  
  */

@Aspect

@Component
public class AuditAspect
	implements AuthenticationConstants,
	Constants
{

	/*@Autowired
	private AuditAspectLogsRepo		auditAspectRepo; */
	/* @Autowired
	private OrderInvoiceDetailsRepo	p_orderInvoiceDetailsRepo; */

	private static final Logger		LOGGER = LogManager.getLogger();

	// ------------------------------------------------------------------------
	/**
	 * This method is executed when Settlement Controller is successfully
	 * executed and log data in database
	 * 
	 * @param auditAnnotation
	 * @throws Throwable
	 */

	// @SuppressWarnings("unchecked")

	@Around("execution(* com.aafes.settlement.controller.InvoiceController.processSettlementRequest(..))")
	public Object auditingAround(ProceedingJoinPoint joinPoint)
			throws Throwable,
			Exception
	{
		LOGGER.debug("In Audit method logging request and response");
		AuditAspectLogs l_auditLog = new AuditAspectLogs();
		JwtTokenUtil l_jwtTokenUtil = new JwtTokenUtil();
		String l_token = null;
		String l_username = null;

		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder
				.getRequestAttributes()).getRequest();

		try {
			if (!Utils.isNull(request.getHeader(AUTHORIZATION))) {
				l_token = request.getHeader(AUTHORIZATION).substring(7);

				l_username = l_jwtTokenUtil.getUsernameFromToken(l_token);

				l_auditLog.setUrl(request.getRequestURL().toString());
			}
		} catch (Exception e) {
			LOGGER.error("Invalid Token: " + e.getLocalizedMessage());
		}

		l_auditLog.setCreatedBy(l_username);
		l_auditLog.setUpdatedBy(l_username);
		l_auditLog.setRequestTime(Utils.getUtcDateInString());

		Object l_returnValue = joinPoint.proceed();
		if (!StringUtils.isEmpty(l_auditLog)) {
			Runnable l_runnableTask = () ->
			{

				@SuppressWarnings("unchecked")
				ResponseEntity<Object> l_Obj = (ResponseEntity<
						Object>) l_returnValue;

				EISEResponseGeneric l_respObj = (EISEResponseGeneric) l_Obj
						.getBody();

				/**
				 * Set Status
				 */

				l_auditLog.setStatus(l_respObj.getResponseMessage());

				l_auditLog.setResponseTime(Utils.getUtcDateInString());
				l_auditLog.setCreatedDate(Utils.getCurrentDateTime());
				l_auditLog.setUpdatedDate(Utils.getCurrentDateTime());
				/*
				 * l_auditLog.setUuid( UUID.randomUUID().toString().replace("-",
				 * "") );
				 */
				OrderInvoiceDetails l_orderInvoiceDetails = new OrderInvoiceDetails();

				try {
					Object[] l_args = joinPoint.getArgs();
					l_auditLog.setRequestData(
							convertObjectToString(l_args[0])
					);

					// Repository change
					EISERequestGeneric eiseRequest = (EISERequestGeneric) l_args[0];
					l_auditLog.setUuid(eiseRequest.getUUID());

					l_auditLog.setResponseData(
							convertObjectToString(l_returnValue)
					);

					setRequestType(l_auditLog);
					l_orderInvoiceDetails = setInvoiceData(
							eiseRequest, l_orderInvoiceDetails, l_auditLog
					);

				} catch (Exception e) {
					LOGGER.error(
							"AuditLog Execption:" +
									e.getLocalizedMessage()
					);
				}

				try {
					//auditAspectRepo.save(l_auditLog);
				} catch (Exception l_e) {
					LOGGER.error(
							"Error while saving data AuditLog in DB:" +
									l_e.getLocalizedMessage()
					);
				}
				try {
					if (l_auditLog.getStatus() != SUCCESS) {
						//p_orderInvoiceDetailsRepo.save(l_orderInvoiceDetails);
					}
				} catch (Exception l_e) {
					LOGGER.error(
							"Error while saving data OrderInvoiceDetails in DB:"
									+
									l_e.getLocalizedMessage()
					);
				}
				LOGGER.debug("****************************************");
				LOGGER.debug("RequestData:" + l_auditLog.getRequestData());
				LOGGER.debug("****************************************");
				LOGGER.debug("ResponseData:" + l_auditLog.getResponseData());
				LOGGER.debug("****************************************");
			};
			ExecutorService l_executor = Executors.newSingleThreadExecutor();
			l_executor.execute(l_runnableTask);
			l_executor.shutdown();
		}
		return l_returnValue;
	}

	private OrderInvoiceDetails setInvoiceData(
			EISERequestGeneric l_eiseReq,
			OrderInvoiceDetails l_orderInvoiceDetails,
			AuditAspectLogs l_auditLog
	)
	{
		String l_orderId = "";
		l_orderInvoiceDetails.setOrderDate(
				Utils.getCDTDate()
		);
		l_orderInvoiceDetails.setCreatedDate(
				Utils.getCurrentDate()
		);
		l_orderInvoiceDetails.setCreatedBy(EISE);
		l_orderInvoiceDetails.setTranType(
				l_auditLog.getDescription()
		);

		l_orderInvoiceDetails.setAuditUUID(
				l_auditLog.getUuid()
		);
		switch (l_auditLog.getDescription()) {
			case SHIPPED_INVOICE: {
				l_orderId = l_eiseReq.getSettlementRequest().getOrderId();
				l_orderInvoiceDetails.setTranType(
						SETTLEMENT
				);
				break;
			}
			case RETURN_INVOICE: {
				l_orderId = l_eiseReq.getRefundRequest().getParentOrder()
						.getOrderId();
				l_orderInvoiceDetails.setTranType(
						REFUND
				);
				break;
			}
			case AUTH_REVERSAL_INVOICE: {
				l_orderId = l_eiseReq.getAuthReversalRequest().getOrderId();
				break;
			}
			case EXCHANGE_INVOICE: {
				l_orderId = l_eiseReq.getExchangeRequest().getParentOrder()
						.getOrderId();
				break;
			}
			case ADJUSTMENT_INVOICE: {
				l_orderId = l_eiseReq.getAdjustmentRequest().getOrderId();
				break;
			}
			case CANCELLED_INVOICE: {
				l_orderId = l_eiseReq.getCancelledRequest().getOrderId();
				break;
			}

		}
		l_orderInvoiceDetails.setOrderId(l_orderId);
		// Set amount to -1 for failed requests
		l_orderInvoiceDetails.setAmount(MINUS_ONE_VALUE);
		return l_orderInvoiceDetails;
	}

	// ------------------------------------------------------------------------
	/**
	 * Convert Json-object into Json-String
	 * 
	 * @param p_obj
	 * @return
	 * @throws JsonProcessingException
	 */

	private String convertObjectToString(Object p_obj)
			throws JsonProcessingException
	{
		ObjectMapper l_objMapper = new ObjectMapper();
		String l_jsonStr = l_objMapper.writeValueAsString(p_obj);
		return l_jsonStr;
	}

	// ------------------------------------------------------------------------
	/**
	 * Set Description in Audit Entity
	 * 
	 * @param p_aspectLog
	 * @throws JsonParseException
	 * @throws JsonMappingException
	 * @throws IOException
	 */

	private void setRequestType(
			AuditAspectLogs p_aspectLog
	)
	/*
	 * throws JsonParseException, JsonMappingException, IOException
	 */
	{
		LOGGER.info("Setting description for auditlog");
		ObjectMapper l_objMapper = new ObjectMapper();
		StringJoiner l_description = new StringJoiner(",");

		EISERequestGeneric l_eiseReq = null;
		try {
			l_eiseReq = l_objMapper.readValue(
					p_aspectLog.getRequestData(), EISERequestGeneric.class
			);
		} catch (JsonParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (l_eiseReq != null) {
			if (
				!StringUtils.isEmpty(
						l_eiseReq.getSettlementRequest()
				)
			)
			{
				l_description.add(SHIPPED_INVOICE);
			}
			if (
				!StringUtils.isEmpty(
						l_eiseReq.getRefundRequest()
				)
			)
			{
				l_description.add(RETURN_INVOICE);
			}

			if (
				!StringUtils.isEmpty(l_eiseReq.getAuthReversalRequest())
			)
			{
				l_description.add(AUTH_REVERSAL_INVOICE);
			}

			if (
				!StringUtils.isEmpty(l_eiseReq.getExchangeRequest())
			)
			{
				l_description.add(EXCHANGE_INVOICE);
			}

			if (
				!StringUtils.isEmpty(l_eiseReq.getAdjustmentRequest())
			)
			{
				l_description.add(ADJUSTMENT_INVOICE);
			}

			if (
				!StringUtils.isEmpty(l_eiseReq.getCancelledRequest())
			)
			{
				l_description.add(CANCELLED_INVOICE);
			}
		}

		p_aspectLog.setDescription(l_description.toString());
	}
	// ---------------------------------------------------------------------
}

// -----------------------------------------------------------------------------
// END OF FILE
// ---------------------------------------------------------------------------
