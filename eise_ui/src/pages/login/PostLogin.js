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
import { authenticateUser1,getLoginInfo } from "../../services/profile/loginservice";
//import Skeleton from "@material-ui/lab/Skeleton";
import CircularProgress from "@material-ui/core/CircularProgress";
function PostLogin(props) {
  var [isLoading, setIsLoading] = React.useState(false);
  const [firstName, setFirstName] = React.useState("");
  let jID = props.location.search.split("=")
  let actualParam = jID[1];
  //let firstName = ""l
debugger;
  useEffect(() => {
  //_cofib.baad== 
    console.log(props);
	 const fnloginUser = async () => {
    
    setIsLoading(true); 
	   let authresponse;
    authresponse = await getLoginInfo(actualParam);
   
    if (authresponse.users.responseMessage === "SUCCESS") {
      //setProfileData(authresponse.users.data);

      setProfileData(authresponse.users);
      let firstName = authresponse.users.data.userDetails.firstName;
      setFirstName(firstName)
    //   localStorage.setItem(
    //     "initialfacilitytype",
    //     authresponse.users.data.userDetails.facilitytype
    //   );
    //   localStorage.setItem(
    //     "storefac7",
    //     authresponse.users.data.userDetails.storeFac7Id
    //   );
	  
	  //   let facilityType = authresponse.users.data.userDetails.facilitytype;
	  
	  //  if (facilityType === 'DC') {
    //      localStorage.setItem(
    //         "parentDCId",
    //         authresponse.users.data.userDetails.storeFac7Id
    //      );
    //   };

      // debugger;
      // try {
      //     let firstName = authresponse.users.data.userDetails.firstName;
      //     setFirstName(firstName)
      //     console.log(authresponse.users.data.userDetails.firstName);
      // } catch (error) {
      //     firstName ="";
      // }
      
      setTimeout(() => setUserData(authresponse), 4000);
      // setIsLoading(false);
      // setProfileData(authresponse.users);
      // localStorage.setItem(
      //   "initialfacilitytype",
      //   JSON.stringify(authresponse.users.data.userDetails.facilitytype)
      // );
      
      // props.history.push("/app/search");
    } else {
      alert("Invalid JSESSION ID");
    }
  };
  fnloginUser();
  }, []);
 
  const setUserData=(authresponse)=>{
    setTimeout(() => console.log(), 4000);
    setIsLoading(false);
    setProfileData(authresponse.users);
   
    props.history.push("/app/invoiceSummary");
  }
  return  (
    <div>
    { isLoading ? (
      <Grid
       
        style={{ background: "white", textAlign: "center" }}
      >
       <div style={{ width:"100px !important",height:"100px !important",marginTop:"250px" }}>
        
        
        <Typography variant="h1" color="primary">
            <CircularProgress/><br/>
            Welcome to eISE,  {firstName}
        </Typography>
      </div>
      </Grid>
    ) : null
      }
     </div>
  )
}

export default withRouter(PostLogin);
