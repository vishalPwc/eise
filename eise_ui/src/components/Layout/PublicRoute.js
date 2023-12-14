import React from "react";

import { Route,  Redirect } from "react-router-dom";
import { isLogin } from "../../context/UserAuth";

export default function PublicRoute({ component, ...rest }) {
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