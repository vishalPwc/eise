import { makeStyles } from "@material-ui/styles";

const drawerWidth = 260;

export default makeStyles(theme => ({
  menuButton: {
    marginLeft: 12,
    marginRight: 36,
    outline: "none"
  },
  hide: {
    display: "none"
  },
  drawer: {
    width: drawerWidth,
    flexShrink: 0,
    whiteSpace: "nowrap"
  },
  drawerOpen: {
    width: drawerWidth,
    scrollbarWidth: "thin",
    transition: theme.transitions.create("width", {
      easing: theme.transitions.easing.sharp,
      duration: theme.transitions.duration.enteringScreen
    }),
    backgroundColor: "#09386c",
    paddingTop: 20,
    // position: "absolute", //postion to change fixed
    minHeight: "100vh"
  },
  drawerClose: {
    float: "right",
    transition: theme.transitions.create("width", {
      easing: theme.transitions.easing.sharp,
      duration: theme.transitions.duration.leavingScreen
    }),
    overflowX: "hidden",
    width: theme.spacing(7) + 30,
    [theme.breakpoints.down("sm")]: {
      width: drawerWidth
    },
    backgroundColor: "#11365e",
    color: "white",
    paddingTop: 20
  },
  toolbar: {
    ...theme.mixins.toolbar,
    [theme.breakpoints.down("sm")]: {
      display: "none"
    }
  },
  content: {
    flexGrow: 1
    // padding: theme.spacing(3)
  },
  navIcon : {
    padding: "10px 5px",
textAlign: "center"
  },
  headerMenuButtoncust: {
    color: "#7298c1",
    justifyContent: "unset",
    padding: "10px 5px",
    "&:focus": {
      outline: "unset !important"
    }
  },
  menuiconcross: {
    justifyContent: "flex-end",
    borderRadius: "unset"
  },
  menuiconcenter: {
    justifyContent: "center",
    borderRadius: "unset"
  },
  mobileBackButton: {
    marginTop: theme.spacing(0.5),
    marginLeft: theme.spacing(3),
    [theme.breakpoints.only("sm")]: {
      marginTop: theme.spacing(0.625)
    },
    [theme.breakpoints.up("md")]: {
      display: "none"
    }
  }
}));
