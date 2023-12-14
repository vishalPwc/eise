import React from "react";
import { setLoginInfo, getLoginInfo } from "./UserAuth";

const localState = getLoginInfo();

// An object called messages
export const profile = {
  userdetails: {
    firstName:  localState !=null ? localState.userDetails.firstName : '',
    lastName:   localState !=null ? localState.userDetails.lastName : '',
    group:      localState !=null ? localState.userDetails.userRole : '',
    store:      localState !=null ? localState.userDetails.storeName : '',
    menu:       localState !=null ? localState.menu : []
  }
};

export const setProfileData = loginResponse => {
  let userInfo = loginResponse.data
  profile.userdetails.firstName = userInfo.userDetails.firstName;
  profile.userdetails.lastName = userInfo.userDetails.lastName;
  profile.userdetails.group = userInfo.userDetails.lastName;
  profile.userdetails.store = userInfo.userDetails.storeName;
  profile.userdetails.menu = userInfo.menu;
  setLoginInfo(loginResponse)
};
//const profile = JSON.parse(localStorage.getItem("userObj"));
// The GlobalContext itself
// - this needs to match the Consumer props
export const GlobalContext = React.createContext({
  userprofile: profile.userdetails, // Message Object
});
