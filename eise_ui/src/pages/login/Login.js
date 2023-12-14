import React, { useState } from "react";
import { Grid, Button } from "@material-ui/core";
import { withRouter } from "react-router-dom";
import AccountCircle from "@material-ui/icons/AccountCircle";
import LockIcon from "@material-ui/icons/Lock";
import PersonIcon from '@material-ui/icons/Person';
// styles
import useStyles from "./styles";
// context
import { setProfileData } from "../../context/GlobalContext";
import { authenticateUser } from "../../services/profile/loginservice";
//import Skeleton from "@material-ui/lab/Skeleton";
import CircularProgress from "@material-ui/core/CircularProgress";
import TextField from "@material-ui/core/TextField";
import InputAdornment from "@material-ui/core/InputAdornment";
import { InputBase } from "@material-ui/core";

function Login(props) {
  var classes = useStyles();

  // global
  //var userDispatch = useUserDispatch();

  // local
  var [isLoading, setIsLoading] = useState(false);
  // var [error, setError] = useState(null);
  var [loginValue, setLoginValue] = useState("");
  var [passwordValue, setPasswordValue] = useState("");

  const loginUser = async () => {
    setIsLoading(true);
    let authresponse = await authenticateUser(loginValue, passwordValue);
    if (authresponse.users.responseMessage === "SUCCESS") {
      setProfileData(authresponse.users);
      setIsLoading(false);
      props.history.push("/app/invoiceSummary");
    } else {
      alert("Invalid Username and Password");
    }
  };
  return isLoading ? (
    <Grid className={classes.container} style={{ background: "white" }}>
      <div className={classes.formContainer}>
        <CircularProgress />
      </div>
    </Grid>
  ) : (

      <Grid className={classes.container} style={{ background: "white" }}>

        <div className={classes.formContainer}>

          <div className={classes.form}>
            <div className="logo-img" style={{ paddingLeft: "25px", paddingBottom: '20px' }}>
              <img src={process.env.PUBLIC_URL + "/images/login-logo.jpg"} alt="logo" />
            </div>
            <div style={{ display: "flex", border: '1px solid #11365d', borderRadius: '7px', marginBottom: '30px' }}>
              <div style={{ padding: "15px", backgroundColor: '#11365d', color: '#ffffff' }}>
                <PersonIcon />
              </div>
              <InputBase
                className={classes.input}
                variant="outlined"
                required
                id="email"
                placeholder="Username"
                name="email"
                autoComplete="email"
                value={loginValue}
                onChange={e => setLoginValue(e.target.value)}
                type="email"
                fullWidth
              />

            </div>

            <div style={{ display: "flex", border: '1px solid #11365d', borderRadius: '7px', marginBottom: '30px' }}>
              <div style={{ padding: "15px", backgroundColor: '#11365d', color: '#ffffff' }}>
                <LockIcon />
              </div>
              <InputBase
                className={classes.input}
                required
                placeholder="password"
                id="password"
                value={passwordValue}
                onChange={e => setPasswordValue(e.target.value)}
                name="password"
                autoComplete="password"
                type="password"
                fullWidth
              />

            </div>
            <Button
              disabled={loginValue.length === 0 || passwordValue.length === 0}
              onClick={() => loginUser()}
              variant="contained"
              color="primary"
              size="large"
              style={{ background: "#11375e", color: "white", width: "100%" }}
            >
              Log In
          </Button>
          </div>
        </div>
      </Grid>
    );
}

export default withRouter(Login);
