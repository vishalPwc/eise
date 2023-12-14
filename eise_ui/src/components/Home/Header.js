import React from 'react';
import ClickAwayListener from '@material-ui/core/ClickAwayListener';
import Grow from '@material-ui/core/Grow';
import Paper from '@material-ui/core/Paper';
import Popper from '@material-ui/core/Popper';
import MenuItem from '@material-ui/core/MenuItem';
import MenuList from '@material-ui/core/MenuList';
import { makeStyles } from '@material-ui/core/styles';
import Button from '@material-ui/core/Button';
import IconButton from '@material-ui/core/IconButton';
import MoreVertIcon from '@material-ui/icons/MoreVert';
import { logout } from "../../context/UserAuth";
import { withRouter } from "react-router-dom";

const useStyles = makeStyles(theme => ({
    root: {
        display: 'flex',
    },
    paper: {
        marginRight: theme.spacing(2),
    },
}));

function Header(props) {
    const classes = useStyles();
    const [open, setOpen] = React.useState(false);
    const anchorRef = React.useRef(null);

    const handleToggle = () => {
        setOpen(prevOpen => !prevOpen);
    };

    const handleClose = event => {
        if (anchorRef.current && anchorRef.current.contains(event.target)) {
            return;
        }

        setOpen(false);
    };

    const logOut = () => {
        logout();
        setOpen(false);
         props.history.push("/login");

    };

    function handleListKeyDown(event) {
        if (event.key === 'Tab') {
            event.preventDefault();
            setOpen(false);
        }
    }


    return (
        <div className="header" style={{ display: "flex" }}>
            <div style={{ width: "80%" }}>
                <h2> <img style={{ margin: '15px' }}
                    src={
                        process.env.REACT_APP_PROD_PREFIX !== undefined
                            ? "/" + process.env.REACT_APP_PROD_PREFIX + "/images/logo.png"
                            : "" + "/images/logo_header.jpg"
                    }
                    alt="Exchange eRPA Logo" />
                </h2>
            </div>
            <div style={{ paddingTop: '20px', display: 'flex' }}>
                <i className="fa fa-user-circle" style={{ fontSize: "24px", paddingRight: '10px' }}></i>
                <h4>Jane Smith</h4>
                <div>

                    <IconButton
                        ref={anchorRef}
                        aria-controls={open ? 'menu-list-grow' : undefined}
                        aria-haspopup="true"
                        onClick={handleToggle}
                        style={{ paddingTop: 0}}
                    >
                        <MoreVertIcon />
                    </IconButton>

                    <Popper open={open} anchorEl={anchorRef.current} role={undefined} transition disablePortal>
                        {({ TransitionProps, placement }) => (
                            <Grow
                                {...TransitionProps}
                                style={{ transformOrigin: placement === 'bottom' ? 'center top' : 'center bottom' }}
                            >
                                <Paper>
                                    <ClickAwayListener onClickAway={handleClose}>
                                        <MenuList autoFocusItem={open} id="menu-list-grow" onKeyDown={handleListKeyDown}>
                                            <MenuItem onClick={handleClose}>Profile</MenuItem>
                                            <MenuItem onClick={handleClose}>My account</MenuItem>
                                            <MenuItem onClick={logOut}>Logout</MenuItem>
                                        </MenuList>
                                    </ClickAwayListener>
                                </Paper>
                            </Grow>
                        )}
                    </Popper>
                </div>
            </div>
        </div>
    )
}
export default withRouter(Header);
