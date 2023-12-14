package com.aafes.settlement.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.aafes.settlement.constant.Constants;

/**
 * This Class contains API for fetching data from database. 1.getAuditLog
 * 2.getAuditLoglist 3.getBaseUrl 4.getAdapterMap
 */
@RestController
@CrossOrigin(origins = "*")
@RequestMapping(path = "/api/infrastructure")
public class InfrastructureController
	implements Constants
{

	// -------------------------------------------------------------------------
}
// -----------------------------------------------------------------------------
// END OF FILE
// -----------------------------------------------------------------------------