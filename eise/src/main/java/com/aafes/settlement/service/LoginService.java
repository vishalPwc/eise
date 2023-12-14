package com.aafes.settlement.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.aafes.settlement.configuration.MessagePropertyConfig;
import com.aafes.settlement.model.RequestObject;
import com.aafes.settlement.model.ResponseObject;
import com.aafes.settlement.model.UserLogin;
import com.aafes.settlement.model.UserMenu;
import com.aafes.settlement.model.userMenu.UserDetail;
import com.aafes.settlement.util.Utils;

/**
 * This service class contains User-menu methods.
 * 
 * @author Logixal Solutions Pvt Ltd
 *
 */
@Service
public class LoginService {

	@Autowired
	private static MessagePropertyConfig messagePropConf;
	@Autowired
	private SAMLInfoService				 samlInfoService;

	private static final Logger			 LOGGER	= LogManager.getLogger(
			LoginService.class
	);

	// -------------------------------------------------------------------------

	/**
	 * This Method will return Userprofile and Menu if appropriate username and
	 * password is sent.
	 * 
	 * This method will be deprecated.
	 * 
	 * @param UserLogin
	 *            p_userLogin
	 * @return
	 */
	public ResponseObject doSimulatedLogin(UserLogin p_userLogin) {

		RequestObject l_reqObj = new RequestObject();
		ResponseObject l_respObj = new ResponseObject();
		Utils.setSuccessInResponseObj(l_reqObj, l_respObj);
		LOGGER.info(
				"doSimulatedLogin. Request obj uuid: " + l_reqObj
						.getUuid()
		);

		try {

			List<UserMenu> l_userMenuList = getListUserMenu();
			l_userMenuList.forEach(l_userMenu ->
			{
				if (
					l_userMenu.getUserDetails().getFirstName()
							.equalsIgnoreCase(
									p_userLogin.getUsername()
							)
							&& l_userMenu.getUserDetails().getPassword()
									.equals(p_userLogin.getPassword())
				)
				{
					l_userMenu.getUserDetails().setPassword(null);
					/*
					 * l_userMenu.setMenu(
					 * roleMenuRepo.findByRoleAndLevelOrderByMenuOrderAsc(
					 * l_userMenu.getUserDetails().getUserRole(), 1 ) );
					 */
					l_respObj.setResponsedata(l_userMenu);
				}
			});

			/**
			 * if username and password is incorrect, this will throw an
			 * exception.
			 **/
			if (StringUtils.isEmpty(l_respObj.getResponsedata())) {
				throw new Exception("User Not Found");
			}

		} catch (Exception l_e) {
			LOGGER.error(
					"doSimulatedLogin. Exception: " + l_e.getMessage()
			);
			Utils.setExceptionInResponseObj(l_respObj, l_e, messagePropConf);

		}

		LOGGER.info(
				"doSimulatedLogin. Response obj for uuid: " + l_respObj
						.getUuid()
		);
		return l_respObj;
	}

	// -------------------------------------------------------------------------
	/**
	 * This Method will return Userprofile and Menu for SAML users based on
	 * JSESSION ID
	 * 
	 * @param p_userLogin
	 * @return
	 */
	public ResponseObject doSamlLoginInfo(UserLogin p_userLogin) {

		LOGGER.info("START doSamlLoginInfo()");

		RequestObject l_reqObj = new RequestObject();
		ResponseObject l_respObj = new ResponseObject();
		Utils.setSuccessInResponseObj(l_reqObj, l_respObj);

		String l_sessionID = p_userLogin.getjSessionId();
		LOGGER.info(
				"doSamlLoginInfo. Request obj uuid: " + l_reqObj
						.getUuid()
		);

		LOGGER.debug(
				"Getting user info for JSESSIONID: " + l_sessionID
		);

		return null;

	}

	// -------------------------------------------------------------------------
	/**
	 * This method will fill the UserProfile and menu in list and will return
	 * that list.
	 * 
	 * 1.Username: 'John' ,Password: 'password' ,Role: 'admin'
	 * 2.Username:'William' ,Password: 'password' ,Role: 'returnAssociate'
	 * 3.Username:'Sophia' ,Password: 'password' ,Role: 'returnSupervisor'
	 * 4.Username:'Mark' ,Password: 'password' ,Role: 'PosAssociate'
	 * 5.Username:'Rose',Password: 'password' ,Role: 'PosSupervisor'
	 * 
	 * @return
	 */
	private List<UserMenu> getListUserMenu() {

		List<UserMenu> l_userMenuList = new ArrayList<UserMenu>();

		// Admin Details
		UserDetail l_userDetailsOne = new UserDetail();
		l_userDetailsOne.setFirstName("John");
		l_userDetailsOne.setLastName("Admin");
		l_userDetailsOne.setStoreName("Dallas HQ");
		l_userDetailsOne.setPassword("password");
		l_userDetailsOne.setUserRole("ADMIN");

		// WarehouseAssociate details
		UserDetail l_userDetailsTwo = new UserDetail();
		l_userDetailsTwo.setFirstName("William");
		l_userDetailsTwo.setLastName("Return Associate");
		l_userDetailsTwo.setStoreName("Dan Daniels");
		l_userDetailsTwo.setPassword("password");
		l_userDetailsTwo.setUserRole("warehouseAssociate");
		l_userDetailsTwo.setFacilityType("DC");
		l_userDetailsTwo.setStoreFacilityCd("AAFES2502896");
		l_userDetailsTwo.setStoreFac7Id(1059902);
		l_userDetailsTwo.setStoreCity("Dallas");

		// WarehouseSupervisor details
		UserDetail l_userDetailsThree = new UserDetail();
		l_userDetailsThree.setFirstName("Sophia");
		l_userDetailsThree.setLastName("Return Supervisor");
		l_userDetailsThree.setStoreName("Dan Daniels");
		l_userDetailsThree.setPassword("password");
		l_userDetailsThree.setUserRole("warehouseSupervisor");
		l_userDetailsThree.setFacilityType("DC");
		l_userDetailsThree.setStoreFacilityCd("AAFES2502896");
		l_userDetailsThree.setStoreFac7Id(1059902);
		l_userDetailsThree.setStoreCity("Dallas");

		// Pos-Associate details
		UserDetail l_userDetailsFour = new UserDetail();
		l_userDetailsFour.setFirstName("Mark");
		l_userDetailsFour.setLastName("POS Associate");
		l_userDetailsFour.setStoreName("Ft Gordon MS");
		l_userDetailsFour.setPassword("password");
		l_userDetailsFour.setUserRole("posAssociate");
		l_userDetailsFour.setFacilityType("POS");
		l_userDetailsFour.setStoreFacilityCd("AAFES1300029");
		l_userDetailsFour.setStoreFac7Id(1030905);
		l_userDetailsFour.setStoreCity("Ft Gordon");

		// POS-Supervisor details
		UserDetail l_userDetailsFive = new UserDetail();
		l_userDetailsFive.setFirstName("Rose");
		l_userDetailsFive.setLastName("POS Supervisor ");
		l_userDetailsFive.setStoreName("Ft Gordon MS");
		l_userDetailsFive.setPassword("password");
		l_userDetailsFive.setUserRole("posSupervisor");
		l_userDetailsFive.setFacilityType("POS");
		l_userDetailsFive.setStoreFacilityCd("AAFES1300029");
		l_userDetailsFive.setStoreFac7Id(1030905);
		l_userDetailsFive.setStoreCity("Ft Gordon");

		// eISE admin
		UserDetail l_userDetailsSix = new UserDetail();
		l_userDetailsSix.setFirstName("Janet");
		l_userDetailsSix.setLastName("EISE Admin ");
		l_userDetailsSix.setStoreName("00");
		l_userDetailsSix.setPassword("password");
		l_userDetailsSix.setUserRole("eiseAdmin");
		l_userDetailsSix.setFacilityType("EISE");

		UserMenu l_userNameOne = new UserMenu();
		l_userNameOne.setUserDetails(l_userDetailsOne);

		UserMenu l_userNameTwo = new UserMenu();
		l_userNameTwo.setUserDetails(l_userDetailsTwo);

		UserMenu l_userNameThree = new UserMenu();
		l_userNameThree.setUserDetails(l_userDetailsThree);

		UserMenu l_userNameFour = new UserMenu();
		l_userNameFour.setUserDetails(l_userDetailsFour);

		UserMenu l_userNameFive = new UserMenu();
		l_userNameFive.setUserDetails(l_userDetailsFive);

		UserMenu l_userNameSix = new UserMenu();
		l_userNameSix.setUserDetails(l_userDetailsSix);

		l_userMenuList.add(l_userNameOne);
		l_userMenuList.add(l_userNameTwo);
		l_userMenuList.add(l_userNameThree);
		l_userMenuList.add(l_userNameFour);
		l_userMenuList.add(l_userNameFive);
		l_userMenuList.add(l_userNameSix);

		return l_userMenuList;
	}
	// -------------------------------------------------------------------------
}
// -----------------------------------------------------------------------------
// END OF FILE
// -----------------------------------------------------------------------------
