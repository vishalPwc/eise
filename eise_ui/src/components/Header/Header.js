import React, { useState, useContext, useEffect } from "react";
import { AppBar, Toolbar, IconButton, Menu } from "@material-ui/core";
import { Person as AccountIcon } from "@material-ui/icons";
// styles
import useStyles from "./styles";
//images
// import applogo from "./logo.png";

// components
import { Typography } from "../Wrappers/Wrappers";
import { logout } from "../../context/UserAuth";
import Grid from "@material-ui/core/Grid";
import classNames from "classnames";
import { GlobalContext } from "../../context/GlobalContext";


export default function Header(props) {
  var classes = useStyles();
  const usercontext = useContext(GlobalContext);
  var [profileMenu, setProfileMenu] = useState(null);

  useEffect(() => {
    const printerFetchData = async () => {

    };
    printerFetchData();
  }, []);
  

 
  
  // let fnsignOut = () => {
  //   logout();
  //   props.history.push("/login");
  // };

  return (
    <AppBar position="fixed" className={classes.appBar}>
      <Toolbar className={classes.toolbar}>
        <Grid container spacing={3}>
          <Grid
            item
            xs={4}
            classes={{
              root: classNames(classes.paddingleft0, classes.logohome),
            }}
          >
        <img
              src={
                process.env.REACT_APP_PROD_PREFIX !== undefined
                  ? "/" + process.env.REACT_APP_PROD_PREFIX + "/images/logo_header.jpg"
                  : "" + "/images/logo_header.jpg"
              }
              alt="Exchange eISE Logo"
              className={classes.googleIcon}
            />
          </Grid>
          <Grid item xs={4}>
           
          </Grid>
          <Grid item xs={4}>
            <Typography
              variant="h6"
              weight="medium"
              classes={{
                root: classNames(classes.labelalignright, classes.labelName),
              }}
            >
              <IconButton
                aria-haspopup="true"
                color="inherit"
                className={classes.headerMenuButton}
                aria-controls="profile-menu"
               // onClick={(e) => setProfileMenu(e.currentTarget)}
              >
                <AccountIcon classes={{ root: classes.headerIcon }} />
              </IconButton>
              {"\u00A0"}
              {"\u00A0"}
              {usercontext.userprofile.firstName}{" "}
              {usercontext.userprofile.lastName}
            </Typography>
            <Typography
              variant="h6"
              weight="medium"
              classes={{
                root: classNames(classes.labelalignright, classes.labelstore),
              }}
            >
              {/* <GlobalContext.Consumer>
                  {({ userprofile, menudetails }) => <>{userprofile.store}</>}
                </GlobalContext.Consumer> */}
              
            </Typography>
            <Typography variant="h6"></Typography>
          </Grid>
        </Grid>
        <Menu
          id="profile-menu"
          open={Boolean(profileMenu)}
          anchorEl={profileMenu}
          onClose={() => setProfileMenu(null)}
          className={classes.headerMenu}
          classes={{ paper: classes.profileMenu }}
          disableAutoFocusItem
        >
          <div className={classes.profileMenuUser}>
            <Typography variant="h4">
              {/* {userData.userDetails.firstName} */}
            </Typography>
            <Typography variant="h6">
              {/* {userData.userDetails.storeName} */}
            </Typography>
            {/* <Typography onClick={() => fnsignOut()} style={{ color: "red" }}>
              Sign Out
            </Typography> */}
          </div>
        </Menu>
      </Toolbar>
      {/* <GlobalContext.Consumer>
        {({ userprofile, menudetails }) => (
          <div>
            {userprofile.username}
            <br></br>
            {userprofile.group}
          </div>
        )}
      </GlobalContext.Consumer> */}
    </AppBar>
  );
}
