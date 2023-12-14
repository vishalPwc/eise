import React from "react";

import { Route,  Redirect } from "react-router-dom";
import { isLogin } from "../../context/UserAuth";

export default function PrivateRoute({ component, ...rest }) {
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