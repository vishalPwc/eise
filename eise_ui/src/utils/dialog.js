import React from 'react';
import { withStyles } from '@material-ui/core/styles';
//import { makeStyles } from '@material-ui/core/styles';

import Button from '@material-ui/core/Button';
import Dialog from '@material-ui/core/Dialog';
import MuiDialogTitle from '@material-ui/core/DialogTitle';
import MuiDialogContent from '@material-ui/core/DialogContent';
import MuiDialogActions from '@material-ui/core/DialogActions';
import IconButton from '@material-ui/core/IconButton';
import CloseIcon from '@material-ui/icons/Close';
import Typography from '@material-ui/core/Typography';
import CheckCircleRoundedIcon from '@material-ui/icons/CheckCircleRounded';
import { red } from '@material-ui/core/colors';
import Slide from '@material-ui/core/Slide';

const styles = theme => ({
  root: {
    margin: 0,
    padding: theme.spacing(2),
  },
  closeButton: {
    position: 'absolute',
    right: theme.spacing(1),
    top: theme.spacing(1),
    color: theme.palette.grey[500],
  },
  iconHover: {
    '&:hover': {
      color: red[800],
    },
  },  
});

const DialogTitle = withStyles(styles)(props => {
  const { children, classes, onClose } = props;
  return (
    <MuiDialogTitle disableTypography className={classes.root}>
      <Typography variant="h6">{children}</Typography>
      {onClose ? (
        <IconButton aria-label="close" className={classes.closeButton} onClick={onClose}>
          <CloseIcon />
        </IconButton>
      ) : null}
    </MuiDialogTitle>
  );
});

const DialogContent = withStyles(theme => ({
  root: {
    padding: theme.spacing(2),
  },
}))(MuiDialogContent);

const DialogActions = withStyles(theme => ({
  root: {
    margin: 0,
    padding: theme.spacing(1),
  },
}))(MuiDialogActions);

const Transition = React.forwardRef(function Transition(props, ref) {
    return <Slide direction="up" ref={ref} {...props} />;
  });

export default function CustomizedDialogs(props) {
 const {dialogcontent,dialogtitle,dialogopen} = props;
 const [open, setOpen] = React.useState(dialogopen);
 //const classes = useStyles();


  const handleClickOpen = () => {
    setOpen(true);
  };
  const handleClose = () => {
    setOpen(false);
    props.dialogfunc()
  };
  //handleClickOpen(dialogopen)  
  return (
    <div>
      {<Button variant="outlined" color="secondary" onClick={handleClickOpen}>
        Open dialog
      </Button>}
      <Dialog onClose={handleClose} aria-labelledby="customized-dialog-title" 
       fullWidth='true'
       maxWidth='sm'
       TransitionComponent={Transition}
        open={open}>
        <DialogTitle id="customized-dialog-title" onClose={handleClose}>
        <CheckCircleRoundedIcon  color="error" style={{ fontSize: 30}} ></CheckCircleRoundedIcon> 
        <span>{dialogtitle}</span>
        </DialogTitle>
        
        <DialogContent dividers>
          <Typography gutterBottom>
            {dialogcontent}
          </Typography>
          <Typography gutterBottom>
              <br></br>
              <br></br>

          </Typography>
        </DialogContent>
        <DialogActions>
          <Button onClick={handleClose} color="primary">
            Save changes
          </Button>
        </DialogActions>
      </Dialog>
    </div>
  );
}