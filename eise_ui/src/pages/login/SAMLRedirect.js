import React, { useState, useEffect } from "react";
import { Grid, Button, TextField, Typography } from "@material-ui/core";
import { withRouter } from "react-router-dom";
import AccountCircle from "@material-ui/icons/AccountCircle";
import InputAdornment from "@material-ui/core/InputAdornment";
import LockIcon from "@material-ui/icons/Lock";
// styles
import useStyles from "./styles";
// context
import { setProfileData } from "../../context/GlobalContext";
import { authenticateUser } from "../../services/profile/loginservice";
import { authenticateRedirect } from "../../services/profile/loginservice";
//import Skeleton from "@material-ui/lab/Skeleton";
import CircularProgress from "@material-ui/core/CircularProgress";
function SAMLRedirect(props) {
  var [isLoading, setIsLoading] = React.useState(false);

  useEffect(() => {
	 const fnloginUser = async () => {
     //debugger
    setIsLoading(true);
	   let authresponse;
    authresponse = await authenticateRedirect();
    // debugger;
    // if(authresponse.users){
    //   setIsLoading(false);
    // }   
  };
  fnloginUser();
  }, []);
 
  return  (
   <div>
  { isLoading ? (
    <Grid
     
      style={{ background: "white", textAlign: "center" }}
    >
      <div style={{ width:"100px !important",height:"100px !important",marginTop:"250px" }}>
        
        <Typography variant="h1" color="primary">
            <CircularProgress/><br/>
            Logging you into Settlement Application
        </Typography>
      </div>
    </Grid>
  ) : null
    }
   </div> 
  )
}

export default withRouter(SAMLRedirect);
