package com.aafes.settlement;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.web.WebApplicationInitializer;

import com.aafes.settlement.util.Utils;

public class ServletInitializer
	extends
	SpringBootServletInitializer
	implements
	WebApplicationInitializer
{

	private static final Logger LOGGER = LogManager.getLogger(
			ServletInitializer.class
	);

	/**
	 * Entry point of the Spring boot web application
	 * 
	 * @param application
	 * @return SpringApplicationBuilder
	 */
	@Override
	protected SpringApplicationBuilder configure(
			SpringApplicationBuilder application
	)
	{
		LOGGER.info(
				"***********************Initializing Application with Servlet Initializer***********************"
		);
		return application.sources(SettlementServiceApplication.class)
				.properties(Utils.getSpringInitProperties());
	}

}