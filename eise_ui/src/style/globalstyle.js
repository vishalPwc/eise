import { makeStyles } from "@material-ui/styles";

export default makeStyles(theme => ({
  headerclass: {
    background: theme.palette.background.pagebar, //"#09386c",
    color: theme.palette.text.pagetext,
    minHeight: "55px !important"
  },
  content: {
    marginTop: theme.spacing(3)
  },
  heading: {
    //marginLeft:"5px"
    marginLeft: theme.spacing(1),
    fontWeight: "bold"
  },
  iconPanel: { color: "white" },
  fontweightnormal: { fontWeight: "normal" },
  fontGray: { color: "#8a8888" }
}));
