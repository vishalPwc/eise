import { makeStyles } from "@material-ui/styles";
import { green } from '@material-ui/core/colors';


export default makeStyles(theme => ({
    mainHeader: {
        color: "#0c3a69",
        borderBottom: "3px solid red",
        width: "31%",
        fontWeight: '550'
    },
    highlight: {
        fontSize: "55px",
        color: "#ba2b38"
    },
    borderBottom: {
        top: "-17px",
        position: "relative",
        marginBottom: '0'
    },
    subContainer:{
        position: 'relative',          
        cursor: "pointer",        
        paddingLeft: "1px",         
        marginTop: "15px" ,
        // display: 'none'
        width: '103%'
        
    },
    subContainerdropdown:{
        position: 'relative',        
        cursor: "pointer",         
        paddingLeft: "5px",         
        marginTop: "16px" ,       
        width:'127px'
    },
    
    iconContainer:{
        height: '100%',
        position: 'absolute',
        pointerEvents: 'none',
        padding: "4px",
        right: '0',
        display: 'flex',
        alignItems: 'center',
        justifyContent: 'center'
    },
    p30:{
        padding: '30px',
        fontSize: '20px',
        textAlign: 'center'
    },
    paperMain:{
        marginTop: "5%",
        minHeight: "82%",
        position: "relative",
        left: "42px",
        width: "120%"
    },
    detailLabel:{
        fontWeight: "600",
        fontSize: "17px",
        color:  '#09386c'
    },
    headerMainBox:{
        backgroundColor: "#ffffff",
         padding: "0 10px"
    },
    fs16: {
        fontSize: '16px'
    },
    // success or failure
    failedtext:{
        fontSize: '16px',
        color:'red'
    },
    successtext:{
        fontSize: '16px',
        color:'#009900'
    },
    gridMainP:{
         padding: "10px 10px 4px 10px",
    },
    eiseAlignCenter:{
        position: 'relative',
        left: '117px'
    },
    cardBlock: {
        padding: "4px 0px 4px 0px"
        },
    formControl: {
        margin: theme.spacing(1),
        minWidth: 120,
    },
    selectEmpty: {
        marginTop: theme.spacing(2),
    },
    arrowBlock:{
        textAlign: "Center",
        position: "relative",
        maxWidth: "3.333333%"
    },
    arrow: {
        position: "absolute",
        top: "44%",
        right: "21px",
        fontSize: "34px"
    },
    detailTable: {
        boxShadow: 'none'
    },
    detailTableHead: {
        backgroundColor: "#ffffff !important",
        color: "#a09d9d",
        border: 0
    },
    detailTableBody: {
        border: 0
    },
    cardHeader:{
        fontWeight: "500", fontSize: "16px"
    },
    headerBox: {
        textAlign: "center",
        borderBottom: "1px solid #000",
        lineHeight: "0.1em",
        margin: "10px 0 20px"

    },
    clrGreen: {
        color: "#06f706",
        fontSize: "18px"
    },
    clrRed: {
        color: "red",
        fontSize: "18px"
    },
    searchicon: {
        verticalAlign: "middle",
        position: "relative",
        left: "0px"
      },
    
    invoicesummarytable:{
         width: "96%",
         overflowx: "auto",       
    },
    iconPanelcolorbalck: {
        color: "black"
      },
    panelHeaderclass: {
        color: " #000000",
        background: " #f5f6f6",
        minHeight: " 55px !important",
        fontSize: "16px",
        fontWeight: "bold",        
    },
    
    paneldatadetail:{        
        padding:"5px 21px 5px 0px"
    },
    
    selectedcell: {
        cursor: "pointer",
        backgroundColor: "#d4d8e9 !important",
        color:"#09386c",         
    },
    
    table: {
        position: 'relative',
        borderCollapse: 'collapse' 
    },
    orange: {
        backgroundColor: "#dc143c",
        width: 30,
        height: 30
    },
    
    amountcell:{
        fontSize:"15px"
    },
    tablerowdata:{
        // padding:'6px 4px 4px 5px'
        padding:'6px 4px 4px '
    },
    currentpage:{
        padding: 4,
        border: "1px solid #c1c1c1",
         width: "36px",
         textAlign : "center",
         position: 'relative',
        top: '15px'
    },
   
    totalpage:{
        padding: 4,
         width: "36px",
         textAlign : "center",
         position: 'relative',
        top: '15px'
    },

    // loading image
    buttonProgress: {
        color: green[500],
        position: "absolute",
        top: "50%",
        left: "50%",
        marginTop: -12,
        marginLeft: -12,
      },

      // loading image
      buttonSuccess: {
        backgroundColor: green[500],
        "&:hover": {
          backgroundColor: green[700],
        },
      },
      containerrow:{
        width: '113%'
      },   
       
    paymentListRow:{
        backgroundColor: '#808080'
    },
    cellAlign:{
        fontSize: '14px'
    },
    tableRowHead:{
        width: "198px", paddingLeft: '9px !important'
    },
    tableHeadingBg:{
        backgroundColor: " #637c97"
    },
    dividerVer:{
        width: '3px',
        // height: '71%',
        // position: 'relative',
        // left: '238px',
        // left: '269px',
        // bottom: '48px'
        display:"none"
    },
    doneicon:{
        color: 'green',
        // border: '1px solid'
    },
    cancelicon:{
        color: 'red',
        // border: '1px solid'       
    },
    showField: {
        display: "block",
      },
      hideField: {
        display: "none"
      },
      selectdateheading:{
        width: '141px',
        marginLeft: theme.spacing(1),
        marginRight: theme.spacing(1),
      },
      inputtext:{
          color:'grey'
      },
      inputchangecolor:{
          color: 'red'
      }
    
}));