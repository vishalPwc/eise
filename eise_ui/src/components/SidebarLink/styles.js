import { makeStyles } from "@material-ui/styles";

export default makeStyles(theme => ({
  link: {
    textDecoration: "none",
    paddingBottom: 6.5,
    paddingTop: 6.5,
    "&:hover, &:focus": {
      //backgroundColor: theme.palette.background.light,
      backgroundColor: "#02182f",
      borderRight: "3px solid #bf001d"
    }
  },
  linkActive: {
    backgroundColor: "#02182f",
    borderRight: "3px solid #bf001d"
  },
  linkNested: {
    paddingLeft: 0,
    "&:hover, &:focus": {
      //backgroundColor: "#FFFFFF",
      backgroundColor: "#02172e"
    }
  },
  linkIcon: {
    marginRight: theme.spacing(1),
    //color: theme.palette.text.secondary + "99",
    transition: theme.transitions.create("color"),
    width: 24,
    display: "flex",
    justifyContent: "center",
    // backgroundColor: "#11365e",
    color: "#a8b7c8"
  },
  linkIconActive: {
    // color: theme.palette.primary.main
  },
  ArrowDrop: {
    position: "absolute",
    bottom: 20,
    right: 0,
    color: "#a8b7c8"
  },
  linkText: {
    padding: 0,
    //color: theme.palette.text.secondary + "CC",
    color: "#a8b7c8",
    transition: theme.transitions.create(["opacity", "color"]),
    fontSize: 17,
    fontWeight: "lighter",
    fontStretch: "normal",
    fontStyle: "normal",
    lineHeight: "normal",
    letterSpacing: "normal"
  },

  linkTextHidden: {
    opacity: 0
  },
  nestedList: {
    paddingLeft: theme.spacing(2) + 30
  },
  sectionTitle: {
    marginLeft: theme.spacing(4.5),
    marginTop: theme.spacing(2),
    marginBottom: theme.spacing(2)
  },
  divider: {
    marginTop: theme.spacing(2),
    marginBottom: theme.spacing(4),
    height: 1,
    backgroundColor: "#D8D8D880"
  }
}));
