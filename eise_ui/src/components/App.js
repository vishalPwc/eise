import React from "react";
import invoiceDetails from './Home/InvoiceDetails'
import { Route, Switch, Redirect, BrowserRouter } from "react-router-dom";


// pages
// import Error from "../pages/error/Error";
import Login from "../pages/login/Login";
import { isLogin } from "../context/UserAuth";
import Home from '../components/Home'
// context
//import { useUserState } from "../context/UserContext";
//import { UserProvider } from "../context/UserProvider";
import { GlobalContext, profile } from "../context/GlobalContext";
import { SnackbarProvider } from 'notistack';
import InvoiceSummary from "./invoiceSummary/InvoiceSummary";
import Layout from "./Layout/Layout";

import PostLogin from "../pages/login/PostLogin";
import SAMLRedirect from "../pages/login/SAMLRedirect";

export default function App() {
  // global
  //var { isAuthenticated } = useUserState();
  const userstate = {
    userprofile: profile.userdetails, // Message Object
    menudetails: profile.menu // Messa
  };
  return (
    // <UserProvider>
    <GlobalContext.Provider value={userstate}>
      <SnackbarProvider>
        {/* basename={process.env.REACT_APP_ROUTER_BASE || ""} */}
        <BrowserRouter basename={process.env.REACT_APP_ROUTER_BASE || ""}>
          <Switch>
            {<Route exact path="/" render={() => <Redirect to="/autologin" />} />}
            <PublicRoute path="/login" component={Login} />
            <PublicRoute path="/autologin" component={SAMLRedirect} />
            <PublicRoute path="/postlogin" component={PostLogin} />

            <Route exact path="/home" component={Home} />
            <PrivateRoute path="/app" component={Layout} />
       
            {/* <Route component={Login} /> */}
          </Switch>
        </BrowserRouter>
      </SnackbarProvider>
    </GlobalContext.Provider>
    // </UserProvider>
  );

  // ############################################

  function PrivateRoute({ component, ...rest }) {
    console.log();
    return (
      <Route
        {...rest}
        render={props =>
          isLogin ? (
            React.createElement(component, props)
          ) : (
              <Redirect
                to={{
                  pathname: "/login",
                  state: {
                    from: props.location
                  }
                }}
              />
            )
        }
      />
    );
  }

  function PublicRoute({ component, ...rest }) {
    return (
      <Route
        {...rest}
        render={props =>
          !isLogin ? (
            <Redirect
              to={{
                pathname: "/"
              }}
            />
          ) : (
              React.createElement(component, props)
            )
        }
      />
    );
  }
}
