import { makeStyles } from "@material-ui/styles";

export default makeStyles(theme => ({
  pageTitleContainer: {
    display: "flex",
    justifyContent: "space-between",
    marginBottom: theme.spacing(1),
    marginTop: theme.spacing(3),
    borderBottom: "solid #dedada 2px"
  },
  pageTitleFirst: {
    color: "#bf001d",
    fontSize: "42px !important",
    fontWeight: "500 !important",
    fontStretch: "normal",
    fontStyle: "normal",
    lineHeight: "normal",
    borderBottom: "2px solid #bf001d"
  },
  pageTitlePart: {
    color: "#09386c"
  },
  typo: {
    color: theme.palette.text.hint
  },

  button: {
    boxShadow: theme.customShadows.widget,
    textTransform: "none",
    "&:active": {
      boxShadow: theme.customShadows.widgetWide
    }
  }
}));
