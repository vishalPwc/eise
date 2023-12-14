const TOKEN_KEY = "jwt-else";
const USER_KEY = "userObj";

export const setLoginInfo = loginResponse => {
  //debugger;
  localStorage.setItem(TOKEN_KEY, loginResponse.uuid);
  localStorage.setItem(USER_KEY, JSON.stringify(loginResponse.data));
  localStorage.setItem("isAuthenticated", true);
};

export const getLoginInfo = loginResponse => {
  return JSON.parse(localStorage.getItem(USER_KEY));
};

export const logout = authresponse => {
  localStorage.removeItem(TOKEN_KEY);
  localStorage.removeItem(USER_KEY);
  localStorage.removeItem("isAuthenticated");
  return;
};

export const isLogin = () => {
  if (localStorage.getItem(TOKEN_KEY)) {
    return true;
  }
  return false;
};

export const getLoginToken = () => {
  if (localStorage.getItem(TOKEN_KEY)) {
    return localStorage.getItem(TOKEN_KEY);
  }

  return "";
};

export const setuserdetails = userdata => {
  return userdata;
};
