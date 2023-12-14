package com.aafes.settlement.service;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.aafes.settlement.configuration.GlobalParams;
import com.aafes.settlement.configuration.MessagePropertyConfig;
import com.aafes.settlement.constant.Constants;
import com.aafes.settlement.constant.ErrorConstants;
import com.aafes.settlement.model.RequestObject;
import com.aafes.settlement.model.ResponseObject;
import com.aafes.settlement.model.reports.ReportDetailsResponse;
import com.aafes.settlement.model.reports.ReportRequest;
import com.aafes.settlement.repository.entity.InvoiceChargeDetails;
import com.aafes.settlement.repository.entity.InvoiceItemDetails;
import com.aafes.settlement.repository.entity.InvoicePaymentMethods;
import com.aafes.settlement.repository.entity.OrderInvoiceDetails;
import com.aafes.settlement.util.Utils;

// ----------------------------------------------------------------------------
/**
 * This Service contains method to fetch Report List, Report Details
 */
@Service
public class ReportService
	implements Constants, ErrorConstants
{

}
// ----------------------------------------------------------------------------
// END OF FILE
// ----------------------------------------------------------------------------