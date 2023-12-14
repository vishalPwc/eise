import React, { useState, useEffect } from "react";
import { useRouteMatch } from 'react-router-dom';
import { getInvoiceSummary, getInvoiceDetails, getAuditLog } from "../../services/invoicelist/invoicelist";
import Table from "@material-ui/core/Table";
import TableBody from "@material-ui/core/TableBody";
import TableCell from "@material-ui/core/TableCell";
import TableContainer from "@material-ui/core/TableContainer";
import TableHead from "@material-ui/core/TableHead";
import TableRow from "@material-ui/core/TableRow";
import Paper from "@material-ui/core/Paper";
// import moment from "moment";
import PageTitle from "../../components/PageTitle/PageTitle";
import { InputBase, Button } from "@material-ui/core";
import Divider from '@material-ui/core/Divider';
import Grid from '@material-ui/core/Grid';
import DoubleArrowIcon from '@material-ui/icons/DoubleArrow';
import SearchIcon from '@material-ui/icons/Search';
import { KeyboardDatePicker, MuiPickersUtilsProvider } from '@material-ui/pickers';
import 'date-fns';
import DateFnsUtils from '@date-io/date-fns';
import classNames from "classnames";
import useStyles from "./styles";
import { getShortDate, getInitials, currencyFormat, extractError } from "../../utils/commonutils";
import { TablePagination } from "@material-ui/core";
import { makeStyles } from '@material-ui/core/styles';
import Box from '@material-ui/core/Box';
import Collapse from '@material-ui/core/Collapse';
import IconButton from '@material-ui/core/IconButton';
import KeyboardArrowDownIcon from '@material-ui/icons/KeyboardArrowDown';
import KeyboardArrowUpIcon from '@material-ui/icons/KeyboardArrowUp';
// import { withSnackbar } from "notistack";
// loading image
import clsx from "clsx";
import CircularProgress from "@material-ui/core/CircularProgress";
// download icon
import GetAppIcon from '@material-ui/icons/GetApp';
// success icon
import DoneIcon from '@material-ui/icons/Done';
// failure icon
import ClearIcon from '@material-ui/icons/Clear';
import Select from '@material-ui/core/Select';
import InputLabel from '@material-ui/core/InputLabel';
import TextField from '@material-ui/core/TextField';

// Show Snackbar
import Snackbar from '@material-ui/core/Snackbar';
import MuiAlert from '@material-ui/lab/Alert';

// Show Snackbar
function Alert(props) {
    return <MuiAlert elevation={6} variant="filled" {...props} />;
}

const useRowStyles = makeStyles({
    root: {
        '& > *': {
            borderBottom: 'unset',
            backgroundColor: '#d4d8e9',
        },
    },
    tableRowCell: {
        width: "50%",
        paddingLeft: '9px !important'
    },
    paymentListRow: {
        backgroundColor: '#808080'
    },
    focusiconbutton: {
        "&:focus": {
            color: 'blue'
        }
    }
});


function Row(props) {
    const { row } = props;
    const [open, setOpen] = React.useState(false);
    const classes = useRowStyles();


    return (
        <React.Fragment>
            <TableRow className={classes.root}>
                <TableCell component="th" scope="row"
                    className={classes.tableRowCell}
                > {row.paymentMethodId}
                </TableCell>
                <TableCell align="left">{row.cardType}</TableCell>
                <TableCell align="right">
                    {currencyFormat(row.total)}
                </TableCell>

                <TableCell style={{ width: "2px" }}>
                    <IconButton aria-label="expand row" size="small" onClick={() => setOpen(!open)}
                        className={classes.focusiconbutton}>
                        {open ? <KeyboardArrowUpIcon /> : <KeyboardArrowDownIcon />}
                    </IconButton>
                </TableCell>
            </TableRow>
            {open === true && (

                <TableRow className={classes.rootOne}>
                    {/* <TableCell colSpan={5} > */}
                    <TableCell colSpan={4} >
                        {/* <TableCell colSpan={3} > */}
                        <Collapse in={open} timeout="auto" unmountOnExit>
                            <Box align="center"
                                boxShadow={3}>
                                {row.invoiceItemDetailsList.length === 0
                                    ? ""
                                    :
                                    <Table size="small" aria-label="purchases"
                                        style={{ width: '90%', align: "center", marginTop: "10px", marginBottom: "10px" }}>
                                        <TableHead
                                            className={classes.paymentListRow}
                                        >
                                            <TableRow >
                                                <TableCell style={{ fontSize: '14px' }}>Invoice ID</TableCell>
                                                <TableCell style={{ fontSize: '14px' }}>Invoice Line ID</TableCell>
                                                <TableCell align="left" style={{ fontSize: '14px' }}>Item ID</TableCell>
                                                <TableCell align="right" style={{ fontSize: '14px' }}>Amount</TableCell>

                                            </TableRow>
                                        </TableHead>
                                        <TableBody >
                                            {row.invoiceItemDetailsList.map((historyRow) => (
                                                <TableRow key={historyRow.date}>
                                                    <TableCell style={{ fontSize: '13px' }}>{historyRow.invoiceId}</TableCell>
                                                    <TableCell component="th" scope="row" style={{ fontSize: '13px' }}>
                                                        {historyRow.invoiceLineId === undefined
                                                            ? '--'
                                                            : historyRow.invoiceLineId}
                                                    </TableCell>

                                                    <TableCell align="left" style={{ fontSize: '13px' }} >
                                                        {historyRow.invoiceItemId === undefined
                                                            ? '--'
                                                            : historyRow.invoiceItemId
                                                        }
                                                    </TableCell>
                                                    <TableCell align="right" style={{ fontSize: '13px' }}>
                                                        {currencyFormat(historyRow.paymentAmount)}
                                                    </TableCell>
                                                </TableRow>
                                            ))}
                                        </TableBody>
                                    </Table>
                                }
                                {/* charge detail */}
                                {row.invoiceChargeDetailsList.length === 0
                                    ? ""
                                    :
                                    <Table size="small" aria-label="purchases" style={{ width: '90%', align: "center", marginTop: "10px", marginBottom: "10px" }}>
                                        <TableHead
                                            className={classes.paymentListRow} >
                                            <TableRow>
                                                <TableCell
                                                    // className={classes.cellAlign} 
                                                    style={{ fontSize: '14px' }}
                                                >Charge Detail ID</TableCell>
                                                <TableCell align="center"
                                                    //className={classes.cellAlign} 
                                                    style={{ fontSize: '14px' }}
                                                >Charge Type</TableCell>
                                                <TableCell align="right"
                                                    style={{ fontSize: '14px' }}
                                                //className={classes.cellAlign} 
                                                >Amount</TableCell>
                                            </TableRow>
                                        </TableHead>
                                        <TableBody>
                                            {row.invoiceChargeDetailsList.map((historyRow) => (
                                                <TableRow key={historyRow.date}>
                                                    <TableCell component="th" scope="row" style={{ fontSize: '13px' }}>
                                                        {historyRow.chargeDetailsId}
                                                    </TableCell>
                                                    <TableCell align="center" style={{ fontSize: '13px' }}>{historyRow.chargeType}</TableCell>
                                                    <TableCell align="right" style={{ fontSize: '13px' }} >
                                                        {currencyFormat(historyRow.paymentAmount)}
                                                    </TableCell>
                                                </TableRow>
                                            ))}
                                        </TableBody>
                                    </Table>
                                }
                            </Box>
                        </Collapse>
                    </TableCell>
                </TableRow>
            )}
        </React.Fragment>
    );
}


export default function InvoiceSummary(props) {

    const match = useRouteMatch()
    const classes = useStyles();
    // selected row    
    const [selectedId, setSelectedId] = React.useState([]);
    const [invoiceList, setInvoiceList] = useState([]);
    const [invoiceDetails, setInvoiceDetails] = useState({
        invoicePaymentMethodsList: []
    });
    //   const [searchText, SetSearch] = useState(''); 
    const [orderId, setOrderId] = useState();
    const [selectedFromDate, setSelectedFromDate] = React.useState(new Date(Date.now() - (5 * 24 * 60 * 60 * 1000))
    );
    const [selectedToDate, setSelectedToDate] = useState(new Date());
    const [selectedRowData, setRowState] = useState(undefined);
    const [expanded, setExpanded] = React.useState(false);

    // Show Snackbar 
    const [open, setOpen] = React.useState(false);
    const [orderIdOpen, setOrderIdOpen] = React.useState(false);

    // sorting
    const [orderBy, setOrderBy] = React.useState('');

    // pagination
    const [rowsPerPage, setRowsPerPage] = useState(10);
    // pagination 2
    const [currentPage, setCurrentPage] = React.useState(0);
    const [totalPages, setTotalPages] = React.useState();
    const [totalRecords, setTotalRecords] = React.useState();

    // loading image
    const [loading, setLoading] = React.useState(false);
    const [success, setSuccess] = React.useState(false);
    const timer = React.useRef();
    // laoding image
    const buttonClassname = clsx({
        [classes.buttonSuccess]: success,
    });

    // Show snackbar
    const [state, setState] = React.useState({
        vertical: 'top',
        horizontal: 'right',
    });

    const { vertical, horizontal } = state;

    // pagination
    const handleChangePage = (event, page) => {
        // pagination 
        let searchdata = {};
        // add order by   
        if (orderBy !== undefined || orderBy !== "") {
            searchdata.orderBy = orderBy
        }
        // loadInvoiceList(searchdata, page);
        loadInvoiceList(setSearchData(orderBy), page);
    };

    const handleFromDateChange = (date) => {
        setSelectedFromDate(date);
    };

    const handleToDateChange = (date) => {
        setSelectedToDate(date)
    };

    function selectRow(row) {
        // selected row  
        setSelectedId(row.id);
        setRowState(row);
        loadInvoiceDetails(row.id)
    };

    // Add new function for search data
    function setSearchData(p_orderby) {

        let searchdata = {};
        if (orderId !== undefined && orderId !== "") {
            searchdata.orderId = orderId;
        } else if (selectedFromDate !== undefined && selectedToDate !== undefined) {
            searchdata.fromDate = getShortDate(selectedFromDate);
            searchdata.toDate = getShortDate(selectedToDate);
            searchdata.orderBy = p_orderby;
        }
        return searchdata;
    }

    function searchByCriteria(p_orderby) {
        // pagination 2
        loadInvoiceList(setSearchData(p_orderby), 0);

    };

    async function loadInvoiceList(searchdata, page) {
        if (!loading) {
            setLoading(true);
            try {
                let invoicelistdata = await getInvoiceSummary(searchdata, page); //pagination  

                if (invoicelistdata.responseCode === 1) {
                    if (searchdata.orderBy === 'orderId') {
                        setOrderIdOpen(true)
                    } else {
                        setOpen(true);
                    }
                    setState({ vertical: 'top', horizontal: 'right' });
                }
                else {
                    setInvoiceList(invoicelistdata.result.content);
                    setTotalPages(invoicelistdata.result.totalPages);
                    setCurrentPage(invoicelistdata.result.number);
                    setTotalRecords(invoicelistdata.result.totalElements);
                    // loading image
                    timer.current = setTimeout(() => {
                        setLoading(false);
                    }, 500);
                }
            }
            catch (err) {
                //console.log(err);    
            }
        }
    }

    async function loadInvoiceDetails(p_id) {
        try {
            let invoicedetaildata = await getInvoiceDetails(p_id);
            setInvoiceDetails(invoicedetaildata.result.orderInvoiceDetails);
        }
        catch (err) {
            // console.log(err);
        }
    }

    // function for download
    async function downloadRequestResponse(auditUUID, orderId) {
        try {
            let auditReqResponse = await getAuditLog(auditUUID);
            let a = document.createElement("a");
            let content = JSON.stringify(auditReqResponse, null, 2);
            const file = new Blob([content], { type: "text/json" });
            a.href = URL.createObjectURL(file);
            a.download = orderId + ".json";
            a.click();
            a.remove();
        } catch (err) {
            //console.log("error :"+err);
        }
    }

    const handleChange = (panel) => (event, isExpanded) => {
        setExpanded(isExpanded ? panel : false);
    };

    function populateValue(targetValue) {
        setOrderId(targetValue)
    };


    function searchTableData(data) {
        setOrderId(data);
    };

    const selectDropDown = (p_orderby, event) => {
        if (orderId !== undefined || orderId !== "") {
            setOrderId("");
        }
        setOrderBy(p_orderby);
    }
    // Show snackbar
    const handleClose = (event, reason) => {
        // if (reason === 'clickaway') {
        //   return;
        // }

        setOpen(false);
        setLoading(false);
    };

    const handleOrderIdClose = (event, reason) => {
        // if (reason === 'clickaway') {
        //   return;
        // }

        setOrderIdOpen(false);
        setLoading(false);
    };


    useEffect(() => {
        const invoiceListData = async () => {
            // pagination 2
            loadInvoiceList(setSearchData(orderBy), currentPage);
        };
        invoiceListData();
    }, []);


    return (
        <>
            <PageTitle title="Invoice Summary" button="" />
            <br />
            <Grid container xs={12} spacing={1}>
                <Grid item xs={12} sm={6}>
                    <Grid container spacing={1}>
                        {/* <Grid item xs={6} sm={4}> */}
                        {/* add new drop down */}
                        <Grid item xs={6} sm={2}>
                            <div className={classes.subContainerdropdown}>
                                <InputLabel shrink htmlFor="age-native-label-placeholder">
                                    Order/Search by
                                        </InputLabel>
                                <Select
                                    native
                                    value={orderBy}
                                    name={"orderBy"}
                                    onChange={e => selectDropDown(e.target.value)}
                                    inputProps={{ 'aria-label': 'Without label' }}
                                >
                                    <optgroup label="Order By">
                                        <option value="Newest First">Newest First</option>
                                        <option value="Oldest First">Oldest First</option>
                                    </optgroup>
                                    <optgroup label="Search By">
                                        <option value="orderId">Order Id</option>
                                    </optgroup>
                                </Select>
                            </div>
                        </Grid>

                        <Grid item xs={6} sm={3}>
                            <div className={classes.selectdateheading}>
                                <MuiPickersUtilsProvider utils={DateFnsUtils}>
                                    <KeyboardDatePicker
                                        disableToolbar
                                        variant="dialog"
                                        // disableFuture={true}
                                        format="MM/dd/yyyy"
                                        margin="normal"
                                        id="date-picker-inline"
                                        label="Select from date"
                                        value={selectedFromDate}
                                        name="toDate"
                                        // name="fromDate"
                                        onChange={handleFromDateChange}
                                        KeyboardButtonProps={{
                                            'aria-label': 'change date',
                                        }}
                                        className={classes.selectdateheading}
                                        disabled={orderBy === 'orderId'
                                            ? true
                                            : false
                                        }
                                    />
                                </MuiPickersUtilsProvider>
                            </div>
                        </Grid>

                        <Grid item xs={6} sm={3}>
                            <div className={classes.selectdateheading}>
                                <MuiPickersUtilsProvider utils={DateFnsUtils}>
                                    <KeyboardDatePicker
                                        disableToolbar
                                        // disableFuture={true}
                                        variant="dialog"
                                        format="MM/dd/yyyy"
                                        margin="normal"
                                        id="date-picker-inline"
                                        label="Select to date"
                                        value={selectedToDate}
                                        name="toDate"
                                        onChange={handleToDateChange}
                                        KeyboardButtonProps={{
                                            'aria-label': 'change date',
                                        }}
                                        disabled={orderBy === 'orderId'
                                            ? true
                                            : false
                                        }
                                    />
                                </MuiPickersUtilsProvider>
                            </div>
                        </Grid>

                        <Grid item xs={6} sm={2}>
                            <div className={classes.subContainer}>
                                <TextField
                                    label="Order Id"
                                    disabled={
                                        orderBy === 'orderId'
                                            ? false
                                            : true
                                    }
                                    onChange={e => searchTableData(e.target.value)}
                                    value={orderId}
                                    name="orderId"
                                    style={{ height: '13px' }}
                                />
                            </div>
                        </Grid>

                        <Grid item xs={6} sm={2}>
                            <div>
                                <Button
                                    onClick={() => searchByCriteria(orderBy)}
                                    variant="contained"
                                    color="primary"
                                    size="small"
                                    disabled={loading === true ? true : false}     // loading image
                                    className={buttonClassname}   // loading image                                   
                                    style={{
                                        width: "58%",
                                        marginTop: "30px",
                                        position: 'relative',
                                        left: '16px'
                                    }}
                                >
                                    <SearchIcon className={classes.searchicon} />
                                    {/* Search */}
                                    {/* loading image */}
                                    {loading && (
                                        <CircularProgress
                                            size={24}
                                            className={classes.buttonProgress}
                                        />
                                    )}
                                </Button>
                                <Snackbar open={open} autoHideDuration={6000}
                                    onClose={handleClose}
                                    anchorOrigin={{ vertical, horizontal }}
                                    key={vertical + horizontal}
                                >
                                    <Alert onClose={handleClose} severity="error" >
                                        'To Date' should not be less than 'From Date'
                                        </Alert>
                                </Snackbar>

                                <Snackbar open={orderIdOpen} autoHideDuration={6000}
                                    onClose={handleOrderIdClose}
                                    anchorOrigin={{ vertical, horizontal }}
                                    key={vertical + horizontal}
                                >
                                    <Alert onClose={handleOrderIdClose} severity="error" >
                                        Please enter Order Id
                                        </Alert>
                                </Snackbar>
                            </div>
                        </Grid>
                    </Grid>
                    <div className={classes.invoicesummarytable}>

                        <TableContainer component={Paper}>
                            <Table className={classes.table} size="small" aria-label="simple table">
                                <TableHead
                                    className={classes.tableHeadingBg}
                                >
                                    <TableRow>
                                        <TableCell className={classes.tablerowdata}>Order ID</TableCell>
                                        <TableCell align="left" className={classes.tablerowdata}>Type</TableCell>
                                        <TableCell align="center" className={classes.tablerowdata}>Date (CT)</TableCell>
                                        <TableCell align="right" className={classes.tablerowdata}>Amount
                                                </TableCell>
                                        <TableCell className={classes.tablerowdata} align="right">Download</TableCell>
                                        <TableCell className={classes.tablerowdata} align="right">Status</TableCell>
                                    </TableRow>
                                </TableHead>
                                {/* added 'no records found' */}
                                {invoiceList.length === 0
                                    ?
                                    <>
                                        <TableRow>
                                            <TableCell colSpan="10" align="center">No records found</TableCell>
                                        </TableRow>
                                    </>
                                    :
                                    <TableBody>
                                        {invoiceList &&
                                            invoiceList.map(row => (

                                                <TableRow onClick={() => selectRow(row)}
                                                    hover
                                                    key={row.requestId}
                                                    className={row.id === selectedId ? classes.selectedcell : ''}
                                                >
                                                    <TableCell className={classes.tablerowdata}>
                                                        {row.orderId}
                                                    </TableCell>
                                                    <TableCell className={classes.tablerowdata}>
                                                        {row.tranType}
                                                    </TableCell>
                                                    <TableCell align="center" className={classes.tablerowdata}>
                                                        {row.orderDate}
                                                        {/* {moment(row.orderDate).format("YYYY-MM-DD HH:mm:ss")} */}
                                                    </TableCell>

                                                    <TableCell align="right" className={classes.tablerowdata}>
                                                        {row.failed === false ?
                                                            <>
                                                                {currencyFormat(row.amount)}
                                                            </>
                                                            : "--"
                                                        }
                                                    </TableCell>

                                                    <TableCell align="right">
                                                        <GetAppIcon
                                                            onClick={() => downloadRequestResponse(row.auditUUID, row.orderId)}
                                                        />
                                                    </TableCell>
                                                    <TableCell align="right">
                                                        {row.failed === false
                                                            ?
                                                            <DoneIcon className={classes.doneicon} />
                                                            :
                                                            <ClearIcon className={classes.cancelicon} />
                                                        }
                                                    </TableCell>

                                                </TableRow>
                                            ))}
                                    </TableBody>
                                }
                            </Table>
                        </TableContainer>

                        <Divider></Divider>
                        <TablePagination
                            backIconButtonProps={{
                                "aria-label": "Previous Page",
                            }}
                            component="div"
                            count={totalRecords}
                            nextIconButtonProps={{
                                "aria-label": "Next Page",
                            }}
                            onChangePage={handleChangePage}
                            page={currentPage}
                            rowsPerPage={rowsPerPage}
                            rowsPerPageOptions={[10]}
                        />
                    </div>
                </Grid>

                <Grid item xs={1} sm={1} className={classes.arrowBlock}>
                    <DoubleArrowIcon className={classes.arrow} />
                </Grid>

                <Grid item xs={12} sm={5}>
                    {
                        selectedRowData
                            && invoiceDetails ?
                            <div className={classes.containerrow}>
                                <>
                                    <Grid container>
                                        <Grid item xs={12} sm={6} className={classes.gridMainP}>
                                            <div >
                                                <label className={classes.detailLabel}>Order ID:</label>
                                                <span className={classes.fs16}> {invoiceDetails.orderId}</span>
                                            </div>
                                            <div>
                                                <label className={classes.detailLabel}>Type:</label>
                                                <span className={classes.fs16}> {selectedRowData.tranType}</span>
                                            </div>
                                            {/* <div>
                                                    <label className={classes.detailLabel}>Invoice ID:</label> 
                                                    <span className={classes.fs16}> {invoiceDetails.invoiceId}</span>
                                                </div> */}
                                        </Grid>

                                        <Grid item xs={12} sm={6} className={classNames(
                                            classes.gridMainP,
                                            classes.eiseAlignCenter,
                                        )} >
                                            <div>
                                                <label className={classes.detailLabel}> Date:</label>
                                                <span className={classes.fs16}>{" "}
                                                    {/* {moment(invoiceDetails.orderDate).format("YYYY-MM-DD HH:mm:ss")} */}
                                                    {invoiceDetails.orderDate}
                                                </span>
                                            </div>

                                            <div>
                                                <label className={classes.detailLabel}>Status:</label>

                                                {/* <span className={classes.fs16}> */}
                                                <span
                                                    className={selectedRowData.failed === true ?
                                                        classes.failedtext : classes.successtext}>{" "}
                                                    {selectedRowData.failed === true ? 'Failed' : 'Success'}
                                                </span>
                                            </div>
                                        </Grid>
                                    </Grid>
                                    <Divider />

                                    <Grid container>
                                        <Grid item xs={12} sm={12} className={classes.gridMainP}>
                                            <TableContainer component={Paper}>
                                                {selectedRowData.failed === true
                                                    ? " "
                                                    :
                                                    <Table aria-label="collapsible table">
                                                        <TableHead
                                                            className={classes.tableHeadingBg}
                                                        >
                                                            <TableRow>
                                                                <TableCell
                                                                    className={classes.tableRowHead}
                                                                    align="left">
                                                                    Payment ID</TableCell>
                                                                <TableCell align="left">Card</TableCell>
                                                                <TableCell align="right">Amount</TableCell>
                                                                <TableCell style={{ width: "2px" }}></TableCell>
                                                            </TableRow>
                                                        </TableHead>

                                                        {invoiceDetails.invoicePaymentMethodsList === undefined ||
                                                            invoiceDetails.invoicePaymentMethodsList.length === 0
                                                            ?
                                                            <>
                                                                <TableRow>
                                                                    <TableCell colSpan="10" align="center">No records found in Payment Method List</TableCell>
                                                                </TableRow>
                                                            </>
                                                            :
                                                            <TableBody>
                                                                {
                                                                    invoiceDetails &&
                                                                    invoiceDetails.invoicePaymentMethodsList
                                                                        .filter((item) => item.total !== 0)
                                                                        .map((tenderrow, index) => {

                                                                            tenderrow.invoiceItemDetailsList =
                                                                                tenderrow.invoiceItemDetailsList !== undefined
                                                                                    ? tenderrow.invoiceItemDetailsList
                                                                                    : []


                                                                            tenderrow.invoiceChargeDetailsList =
                                                                                tenderrow.invoiceChargeDetailsList !== undefined
                                                                                    ? tenderrow.invoiceChargeDetailsList
                                                                                    : []

                                                                            return (
                                                                                <>
                                                                                    <Row key={tenderrow.id} row={tenderrow === undefined ? [] : tenderrow} />
                                                                                </>
                                                                            )
                                                                        })}
                                                            </TableBody>
                                                        }

                                                    </Table>
                                                }
                                            </TableContainer>

                                        </Grid>
                                    </Grid>
                                    {/* <Divider />  */}
                                </>
                            </div> :
                            <div className={classes.p30}>
                                <h5>
                                    Click on a row to get more detail
                                    </h5>
                            </div>
                    }
                </Grid>
            </Grid>
        </>
    );
}
