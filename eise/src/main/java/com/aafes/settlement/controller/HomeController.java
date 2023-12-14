package com.aafes.settlement.controller;

import java.util.Arrays;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.aafes.settlement.model.userMenu.UserDetail;
import com.aafes.settlement.service.SAMLInfoService;
import com.aafes.settlement.sso.SAMLUserDetails;
import com.aafes.settlement.util.Utils;
import com.github.ulisesbocchio.spring.boot.security.saml.annotation.SAMLUser;

@RestController
@CrossOrigin(origins = "*")
public class HomeController {
	@Autowired
	private SAMLInfoService		samlInfoService;

	private static final Logger	LOGGER = LogManager.getLogger(
			HomeController.class
	);

	@Value("${eisesso.redirecturl}")
	private String				redirecURL;

	/**
	 * This API will redirects the SSO user to the Welcome page.
	 * 
	 * @param user
	 * @param model
	 * @return String
	 */

	@RequestMapping("/landing")
	public String landing(@SAMLUser SAMLUserDetails user, Model model) {

		LOGGER.info(
				"**********************************************************************"
		);
		LOGGER.info(
				"******************Settle Landing page DATA from ADFS******************"
		);

		Map<String, String> userMap = user.getSamlAttributes();

		LOGGER.debug("User Name :" + user.getUsername());
		LOGGER.debug("User Details :  {}", userMap);

		LOGGER.info(
				"**********************************************************************"
		);

		StringBuilder mapAsString = new StringBuilder("{");
		for (String key : userMap.keySet()) {
			mapAsString.append(
					key + "=" + userMap.get(key) +
							", "
			);
		}
		mapAsString.delete(
				mapAsString.length() - 2,
				mapAsString.length()
		).append("}"); // return mapAsString.toString();

		StringBuilder sb = new StringBuilder();
		sb.append("Welcome to eISE, " + user.getUsername() + ".");
		sb.append("\n");
		sb.append(System.lineSeparator());
		sb.append("       -AND-       ");
		sb.append("Claim details are :: " + mapAsString.toString());
		sb.append(System.lineSeparator());

		return sb.toString();

		// return "Welcome to eRPA,"+user.getUsername();
	}

	/**
	 * This API will redirects the authenticated User's JSESSIONID to the UI
	 * PostLogin page.
	 * 
	 * @param p_request
	 * @param p_response
	 * @param p_samlUser
	 * @return ResponseEntity
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping("/home")
	public ResponseEntity setSAMLUserDetails(
			HttpServletRequest p_request, HttpServletResponse p_response,
			@SAMLUser SAMLUserDetails p_samlUser
	)
	{

		LOGGER.info("START home Controller");
		HttpHeaders headers = new HttpHeaders();
		// TODO Remove SOP statements
		try {

			LOGGER.info(
					"******************************************************"
			);
			LOGGER.info("******************DATA from ADFS******************");

			Map<String, String> l_userMap = p_samlUser.getSamlAttributes();

			LOGGER.debug("User Name :" + p_samlUser.getUsername());
			LOGGER.debug("User Details :  {}", l_userMap);

			UserDetail l_userDetail = p_samlUser.getUserDetails();
			String ss = p_request.getSession().getId();

			// samlInfoService.setSAMLInfo(ss, l_userDetail);
			// samlInfoService.saveSAMLUserDetails(ss, l_userDetail);

			// System.out.println(redirecURL);
			String l_redirectURL = l_userDetail.getRedirectURL();

			LOGGER.debug("Redirect URL from RelayState::" + l_redirectURL);

			if (Utils.isNull(l_redirectURL))
				l_redirectURL = redirecURL;

			LOGGER.debug("Redirecting to hostname::" + l_redirectURL);

			System.out.println("Redirect URL::" + redirecURL);

			String l_postLoginredirectURL = l_redirectURL
					+ "/eise/postlogin?ssid=" + ss;
			System.out.println(l_postLoginredirectURL);
			headers.add("Location", l_postLoginredirectURL);
			headers.setAccessControlAllowCredentials(true);
			headers.setAccessControlAllowHeaders(Arrays.asList("*"));

			System.out.println(headers.getAccessControlAllowOrigin());

			headers.setAccessControlAllowOrigin("*");
			LOGGER.info(
					"*******************************************************"
			);
		} catch (Exception e) {
			LOGGER.error(
					"Exception occured in Home Controller::" + e.getMessage(), e
			);
			e.printStackTrace();

		}
		LOGGER.info("END home Controller");
		return new ResponseEntity(headers, HttpStatus.FOUND);
	}

}
