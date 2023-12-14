//import { BASE_URL } from "../httpconstants";
import _config from "../../utils/_config";
import axios from "axios";
export const authenticateUser = (p_user, p_password) => {
  let userData = {};
  userData.username = p_user;
  userData.password = p_password;

  return new Promise((resolve, reject) => {
    let base_url = "";
    base_url = _config.BASE_URL;
    let full_url = base_url + "/api/login/doSimulatedLogin";
    axios
      .post(full_url, userData)
      .then(response => {
        resolve({ users: response.data });
      })
      .catch(response => {
        reject("Invalid UserName or Password");
      });
  });
};

//Using for SAML login
export const authenticateRedirect = (p_user, p_password) => {

  const l_redirectUrl = window && window.location && window.location.hostname && window.location.origin;
 // alert("login service redirect url ->"+l_redirectUrl);
	 let base_url = "";
    base_url = _config.BASE_URL;
//	alert("SAML auth Base url->"+base_url);
    let full_url = base_url + "/saml/login?redirectUrl="+l_redirectUrl;
  setTimeout(() => window.location.replace(full_url), 4000);
};

//Using to get the Userdetails by using j_sessionid after successful authentication from SAML
export const getLoginInfo = (p_jsessionID) => {
 let userData = {};
  //userData.username = p_user;
  //userData.password = "password";
  userData.jSessionId = p_jsessionID;
  debugger;
  return new Promise((resolve, reject) => {
    let base_url = "";
    base_url = _config.BASE_URL;
	//alert("SAML Login Base url->"+base_url);
    debugger;
    let full_url = base_url+  "/service/login/doSamlLoginInfo";
    //alert(full_url);
    debugger;
    axios
      .post(full_url, userData)
      .then(response => {
        resolve({ users: response.data });
      })
      .catch(response => {
        reject("Invalid JSESSIONID");
        alert("Invalid JSESSIONID");
      });
  });
};
