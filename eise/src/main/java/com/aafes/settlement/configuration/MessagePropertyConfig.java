package com.aafes.settlement.configuration;

//-----------------------------------------------------------------------------
import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

//-----------------------------------------------------------------------------
@Configuration
@PropertySource("classpath:messages.properties")
@ConfigurationProperties(prefix = "msg")
public class MessagePropertyConfig {

	private Map<String, String> format;

	// -------------------------------------------------------------------------
	/**
	 * @return the format
	 */
	public Map<String, String> getFormat() {
		return format;
	}

	// -------------------------------------------------------------------------
	/**
	 * @param p_format the format to set
	 */
	public void setFormat(Map<String, String> p_format) {
		format = p_format;
	}
	// -------------------------------------------------------------------------
}
//-----------------------------------------------------------------------------
//END OF FILE
//-----------------------------------------------------------------------------