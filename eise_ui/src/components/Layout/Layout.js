import React from "react";
import { Route, Switch, withRouter } from "react-router-dom";
import classnames from "classnames";
import PostLogin from "../../pages/login/PostLogin";

// styles
import useStyles from "./styles";

// components
import Header from "../Header/Header";
import Sidebar from "../Sidebar/Sidebar";

// pages
import { useLayoutState } from "../../context/LayoutContext";
import InvoiceSummary from "../invoiceSummary/InvoiceSummary";

function Layout(props) {
  var classes = useStyles();

  // global
  var layoutState = useLayoutState();

  return (
    <div className={classes.root}>
      <>
        <Header history={props.history} />
        <Sidebar />
        <div
          className={classnames(classes.content, {
            [classes.contentShift]: layoutState.isSidebarOpened
          })}
        >
          <div className={classes.fakeToolbar} />
          <Switch>
            <Route exact path="/app/invoiceSummary" component={InvoiceSummary} />
            {/* <Route exact path="/invoiceDetails" component={invoiceDetails} /> */}	
            <Route path="/app/postlogin" component ={PostLogin}/>
          
          </Switch>
        </div>
      </>
    </div>
  );
}

export default withRouter(Layout);
