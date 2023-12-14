import React, { useState, useEffect, useContext } from "react";
import { Drawer, IconButton, List } from "@material-ui/core";
import ClearIcon from "@material-ui/icons/Clear";
import {
  Menu as MenuIcon,
  ArrowBack as ArrowBackIcon
} from "@material-ui/icons";
// import LabelIcon from '@material-ui/icons/Label';

import { useTheme } from "@material-ui/styles";
import { withRouter } from "react-router-dom";
import classNames from "classnames";

// styles
import useStyles from "./styles";

// components
import SidebarLink from "./components/SidebarLink/SidebarLink";

// context
import {
  useLayoutState,
  useLayoutDispatch,
  toggleSidebar
} from "../../context/LayoutContext";

import { GlobalContext } from "../../context/GlobalContext";
import ReceiptIcon from '@material-ui/icons/Receipt';
// const structure = [
//   {
//     menuKey: "Order Returns",
//     menuIcon: "HomeIcon",
//     hasChild: false,
//     route: "/orders",
//     isDisabled: false,
//     children: null
//   },
//   {
//     menuKey: "Pallet",
//     menuIcon: "TableIcon",
//     hasChild: false,
//     route: "/pallet",
//     isDisabled: false,
//     children: null
//   }
// ];

function Sidebar({ location }) {
  // const userMenu = JSON.parse(localStorage.getItem("userObj"));
  // let structure = [];
  // if (userMenu != undefined) {
  //   structure = userMenu.menu;
  // }
  // const userMenu = JSON.parse(localStorage.getItem("userObj"));

  const userContext = useContext(GlobalContext);

  let menudetails = [];
  if (userContext.userprofile.menu !== undefined) {
    menudetails = userContext.userprofile.menu;
  }

  var classes = useStyles();
  var theme = useTheme();
  var layoutState = useLayoutState();
  //structure1();
  // global
  var { isSidebarOpened } = useLayoutState();
  var layoutDispatch = useLayoutDispatch();

  // local
  var [isPermanent, setPermanent] = useState(true);

  useEffect(function() {
    window.addEventListener("resize", handleWindowWidthChange);
    handleWindowWidthChange();
    return function cleanup() {
      window.removeEventListener("resize", handleWindowWidthChange);
    };
  });
  const sentCloseRequest = () => {
    toggleSidebar(layoutDispatch);
  };
  return (
    <Drawer
      variant={isPermanent ? "permanent" : "temporary"}
      className={classNames(classes.drawer, {
        [classes.drawerOpen]: isSidebarOpened,
        [classes.drawerClose]: !isSidebarOpened
      })}
      classes={{
        paper: classNames({
          [classes.drawerOpen]: isSidebarOpened,
          [classes.drawerClose]: !isSidebarOpened
        })
      }}
      open={isSidebarOpened}
      // style={{ position: "absolute" }}
    >
      <div className={classes.toolbar} />
      <div className={classes.mobileBackButton}>
        <IconButton onClick={() => toggleSidebar(layoutDispatch)}>
          <ArrowBackIcon
            classes={{
              root: classNames(classes.headerIcon, classes.headerIconCollapse)
            }}
          />
        </IconButton>
      </div>

      {layoutState.isSidebarOpened ? (
        <IconButton
          // color="inherit"
          onClick={() => toggleSidebar(layoutDispatch)}
          className={classNames(
            classes.headerMenuButtoncust,
            classes.headerMenuButtonCollapse,
            classes.menuiconcross
          )}
        >
          <ClearIcon
            classes={{
              root: classNames(classes.headerIcon, classes.headerIconCollapse)
            }}
          />
        </IconButton>
      ) : (
        <>
        <IconButton
          // color="inherit"
          onClick={() => toggleSidebar(layoutDispatch)}
          className={classNames(
            classes.headerMenuButtoncust,
            classes.headerMenuButtonCollapse,
            classes.menuiconcenter
          )}
        >
          <MenuIcon
            classes={{
              root: classNames(classes.headerIcon, classes.headerIconCollapse)
            }}
          />
           
        </IconButton>
        <IconButton
        // color="inherit"
        onClick={() => toggleSidebar(layoutDispatch)}
        className={classNames(
          classes.headerMenuButtoncust,
          classes.headerMenuButtonCollapse,
          classes.menuiconcenter
        )}
      >
        < ReceiptIcon
          classes={{
            root: classNames(classes.headerIcon, classes.headerIconCollapse)
          }}
        />
         
      </IconButton>
      </>
      )}
     

      <List className={classes.sidebarList}>
        {menudetails.map(menu => (
          <SidebarLink
            key={menu.route}
            location={location}
            isSidebarOpened={isSidebarOpened}
            {...menu}
            parentClick={sentCloseRequest}
          />
        ))}
      </List>
    </Drawer>
  );

  // ##################################################################
  function handleWindowWidthChange() {
    var windowWidth = window.innerWidth;
    var breakpointWidth = theme.breakpoints.values.md;
    var isSmallScreen = windowWidth < breakpointWidth;

    if (isSmallScreen && isPermanent) {
      setPermanent(true);
    } else if (!isSmallScreen && !isPermanent) {
      setPermanent(true);
    }
  }
}

export default withRouter(Sidebar);
