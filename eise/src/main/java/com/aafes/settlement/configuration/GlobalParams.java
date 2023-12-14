package com.aafes.settlement.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 
 * This class will read properties from Application.yml
 *
 */
@Component
public class GlobalParams {

	public static String jwtSecretkey;

	public static String jwtAuthurl;

	public static String jwtExtSystem;

	public static String jwtUsername;

	public static String jwtPassword;

	public static String jwtExpiry;

	public static String aesSecret;

	public static int	 reportRecords;

	@Value("${jwt.secret}")
	public void setClientId(String jwtSecretkey) {
		GlobalParams.jwtSecretkey = jwtSecretkey;
	}

	@Value("${jwt.get.token.uri}")
	public void setClientSecret(String jwtAuthurl) {
		GlobalParams.jwtAuthurl = jwtAuthurl;
	}

	@Value("${external.sys}")
	public void setPrinterKey(String jwtExtSystem) {
		GlobalParams.jwtExtSystem = jwtExtSystem;
	}

	@Value("${mao.username}")
	public void setUsername(String jwtUsername) {
		GlobalParams.jwtUsername = jwtUsername;
	}

	@Value("${mao.password}")
	public void setPassword(String jwtPassword) {
		GlobalParams.jwtPassword = jwtPassword;
	}

	@Value("${jwt.expiry}")
	public void setExpiry(String jwtExpiry) {
		GlobalParams.jwtExpiry = jwtExpiry;
	}

	@Value("${aesToken.key}")
	public void setAesSecret(String aesSecret) {
		GlobalParams.aesSecret = aesSecret;
	}

	@Value("${pagination.reportlist}")
	public void setReportpagination(int reportRecords) {
		GlobalParams.reportRecords = reportRecords;
	}
	// ------------------------------------------------------------------------
}
// -----------------------------------------------------------------------------
// END OF FILE
// -----------------------------------------------------------------------------