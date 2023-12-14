import { makeStyles } from "@material-ui/styles";
import { fade } from "@material-ui/core/styles/colorManipulator";

export default makeStyles(theme => ({
  logotype: {},
  appBar: {
    width: "100%",
    backgroundImage:
      'url("/"+process.env.REACT_APP_ROUTER_BASE+"/images/logo.png")',
    zIndex: theme.zIndex.drawer + 1,
    transition: theme.transitions.create(["margin"], {
      easing: theme.transitions.easing.sharp,
      duration: theme.transitions.duration.leavingScreen
    })
  },
  toolbar: {
    paddingLeft: theme.spacing(5),
    paddingRight: theme.spacing(5),
    minHeight: 84,
    background: "#fcfcfc"
  },
  labelalignright: { textAlign: "right" },

  labelName: {
    fontFamily: "Roboto",
    fontSize: "18px !important",
    fontWeight: "400 !important",
    fontStretch: "normal",
    fontStyle: "normal",
    lineHeight: "normal",
    color: "#09386c",
    letterSpacing: "normal"
  },
  labelstore: {
    fontWeight: "100 !important",
    fontSize: "16px !important",
    fontStretch: "normal",
    color: "#09386c",
    lineHeight: "normal",
    letterSpacing: "normal"
  },
  hide: {
    display: "none"
  },
  grow: {
    flexGrow: 1
  },
  paddingleft0: {
    paddingLeft: "0 !important"
  },
  search: {
    position: "relative",
    borderRadius: 25,
    paddingLeft: theme.spacing(2.5),
    width: 36,
    backgroundColor: fade(theme.palette.common.black, 0),
    transition: theme.transitions.create(["background-color", "width"]),
    "&:hover": {
      cursor: "pointer",
      backgroundColor: fade(theme.palette.common.black, 0.08)
    }
  },
  searchFocused: {
    backgroundColor: fade(theme.palette.common.black, 0.08),
    width: "100%",
    [theme.breakpoints.up("md")]: {
      width: 260
    }
  },
  searchIcon: {
    width: 36,
    right: 0,
    height: "100%",
    position: "absolute",
    display: "flex",
    alignItems: "center",
    justifyContent: "center",
    transition: theme.transitions.create("right"),
    "&:hover": {
      cursor: "pointer"
    }
  },
  searchIconOpened: {
    right: theme.spacing(1.25)
  },
  inputRoot: {
    color: "inherit",
    width: "100%"
  },
  inputInput: {
    height: 36,
    padding: 0,
    paddingRight: 36 + theme.spacing(1.25),
    width: "100%"
  },
  messageContent: {
    display: "flex",
    flexDirection: "column"
  },
  headerMenu: {
    marginTop: theme.spacing(7)
  },
  // headerMenuList: {
  //   display: "flex",
  //   flexDirection: "column"
  // },
  headerMenuItem: {
    "&:hover, &:focus": {
      backgroundColor: theme.palette.primary.main,
      color: "white"
    }
  },
  headerMenuButton: {
    marginLeft: theme.spacing(2),
    color: "white",
    padding: "2px",
    background: "#09386c !important",
    margin: "0px 5px 4px 0px",
    " &:hover": { backgroundColor: "#09386c !important" }
  },
  headerMenuButtonCollapse: {
    marginRight: theme.spacing(2)
  },
  headerIcon: {
    fontSize: 15,
    color: "white"
  },
  headerIconCollapse: {
    color: "white"
  },
  // profileMenu: {
  //   minWidth: 265
  // },
  // profileMenuUser: {
  //   display: "flex",
  //   flexDirection: "column",
  //   padding: theme.spacing(2)
  // },
  profileMenuItem: {
    color: theme.palette.text.hint
  },
  profileMenuIcon: {
    marginRight: theme.spacing(2),
    color: theme.palette.text.hint
  },
  profileMenuLink: {
    fontSize: 16,
    textDecoration: "none",
    "&:hover": {
      cursor: "pointer"
    }
  },
  messageNotification: {
    height: "auto",
    display: "flex",
    alignItems: "center",
    "&:hover, &:focus": {
      backgroundColor: theme.palette.background.light
    }
  },
  messageNotificationSide: {
    display: "flex",
    flexDirection: "column",
    alignItems: "center",
    marginRight: theme.spacing(2)
  },
  messageNotificationBodySide: {
    alignItems: "flex-start",
    marginRight: 0
  },
  sendMessageButton: {
    margin: theme.spacing(4),
    marginTop: theme.spacing(2),
    marginBottom: theme.spacing(2),
    textTransform: "none"
  },
  sendButtonIcon: {
    marginLeft: theme.spacing(2)
  },
  h6: {
    fontSize: 18
  },
  root: {
    padding: '2px 4px',
    display: 'flex',
    alignItems: 'center',
    width: 400,
  },
  searchroot: {
    padding: '2px 4px',
    display: 'flex',
    alignItems: 'center',
    width: 400,
  },
  input: {
    marginLeft: theme.spacing(1),
    flex: 1,
  },
  iconButton: {
    padding: 10,
  },
  divider: {
    height: 28,
    margin: 4,
  },
}));
