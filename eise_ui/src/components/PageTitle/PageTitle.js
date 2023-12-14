import React from "react";
import { Button, Typography } from "@material-ui/core";

// styles
import useStyles from "./styles";

// components
//import { Typography } from "../Wrappers/Wrappers";

// import getInitials from "../../utils/getInitials";
// import getNamePart from "../../utils/getNamePart";
import { getInitials, getNamePart } from "../../utils/commonutils";

export default function PageTitle(props) {
  var classes = useStyles();

  return (
    <div className={classes.pageTitleContainer}>
      <Typography className={classes.pageTitleFirst} variant="h1">
        {getInitials(props.title)}
        <span size="sm" className={classes.pageTitlePart}>
          {getNamePart(props.title)}
        </span>
      </Typography>
      {props.button && (
        <Button
          classes={{ root: classes.button }}
          variant="contained"
          size="large"
          color="secondary"
        >
          {props.button}
        </Button>
      )}
    </div>
  );
}
