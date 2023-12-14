package com.aafes.settlement;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.bind.annotation.CrossOrigin;

import com.aafes.settlement.util.Utils;

@SpringBootApplication
@EnableScheduling
@EnableAutoConfiguration(
		exclude = { DataSourceAutoConfiguration.class,
				HibernateJpaAutoConfiguration.class })
@CrossOrigin(origins = "*", allowCredentials = "true")
public class SettlementServiceApplication {

	private static final Logger LOGGER = LogManager.getLogger(
			SettlementServiceApplication.class
	);

	public static void main(
			String[] args
	)
	{
		LOGGER.info(
				"***********************Initializing Application with Spring Initializer***********************"
		);
		new SpringApplicationBuilder(SettlementServiceApplication.class)
				.sources(SettlementServiceApplication.class)
				.properties(Utils.getSpringInitProperties())
				.run(args);
	}
}