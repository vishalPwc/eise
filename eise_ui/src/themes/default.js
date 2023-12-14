import tinycolor from "tinycolor2";

const primary = "#09386c";
const secondary = "#FF5C93";
const warning = "#FFC260";
const success = "#3CD4A0";
const info = "#9013FE";
const pagebar = "#09386c";

const lightenRate = 7.5;
const darkenRate = 15;

export default {
  palette: {
    primary: {
      main: primary,
      light: tinycolor(primary)
        .lighten(lightenRate)
        .toHexString(),
      dark: tinycolor(primary)
        .darken(darkenRate)
        .toHexString()
    },
    secondary: {
      main: secondary,
      light: tinycolor(secondary)
        .lighten(lightenRate)
        .toHexString(),
      dark: tinycolor(secondary)
        .darken(darkenRate)
        .toHexString(),
      contrastText: "#FFFFFF"
    },
    warning: {
      main: warning,
      light: tinycolor(warning)
        .lighten(lightenRate)
        .toHexString(),
      dark: tinycolor(warning)
        .darken(darkenRate)
        .toHexString()
    },
    success: {
      main: success,
      light: tinycolor(success)
        .lighten(lightenRate)
        .toHexString(),
      dark: tinycolor(success)
        .darken(darkenRate)
        .toHexString()
    },
    info: {
      main: info,
      light: tinycolor(info)
        .lighten(lightenRate)
        .toHexString(),
      dark: tinycolor(info)
        .darken(darkenRate)
        .toHexString()
    },
    text: {
      primary: "#4A4A4A",
      secondary: "#6E6E6E",
      hint: "#B9B9B9",
      pagetext: "#FFFFFF"
    },
    background: {
      default: "#ffffff",
      light: "#F3F5FF",
      pagebar: pagebar
    },
    common: {
      black: "#000",
      white: "#FFF"
    },
    icon: {
      white: "#FFF"
    }
  },
  customShadows: {
    widget:
      "0px 3px 11px 0px #E8EAFC, 0 3px 3px -2px #B2B2B21A, 0 1px 8px 0 #9A9A9A1A",
    widgetDark:
      "0px 3px 18px 0px #4558A3B3, 0 3px 3px -2px #B2B2B21A, 0 1px 8px 0 #9A9A9A1A",
    widgetWide:
      "0px 12px 33px 0px #E8EAFC, 0 3px 3px -2px #B2B2B21A, 0 1px 8px 0 #9A9A9A1A"
  },
  overrides: {
    MuiBackdrop: {
      root: {
        backgroundColor: "#4A4A4A1A"
      }
    },
    MuiMenu: {
      paper: {
        boxShadow:
          "0px 3px 11px 0px #E8EAFC, 0 3px 3px -2px #B2B2B21A, 0 1px 8px 0 #9A9A9A1A"
      }
    },
    MuiSelect: {
      icon: {
        color: "#B9B9B9"
      }
    },
    MuiListItem: {
      root: {
        "&$selected": {
          backgroundColor: "#F3F5FF !important",
          "&:focus": {
            backgroundColor: "#F3F5FF"
          }
        }
      },
      button: {
        "&:hover, &:focus": {
          backgroundColor: "#F3F5FF"
        }
      }
    },
    MuiTouchRipple: {
      child: {
        backgroundColor: "white"
      }
    },
    MuiTableRow: {
      root: {
        // height: 56
        height: 46
      }
    },
    MuiTableHead: {
      root: {
        //  backgroundColor: "red !important"
      }
    },
    MuiInputBase: {
      root: {
        // display: "none"
      },
      adornedEnd: {
      //  display: "none"
      }
    },
    MuiTableCell: {
      root: {
        borderBottom: "1px solid rgba(224, 224, 224, .5)",
        padding:0
      },
      head: {
        fontSize: "17px",
        // backgroundColor: " #ffffff !important",
        //backgroundColor: " #637c97 !important",
        // color: "#000000",
        color: "#ffffff",
        fontWeight: "normal",
        lineHeight: "normal"
      },
      body: {
        fontSize: "0.95rem"
      }
    },
    MuiInputAdornment: {
      root: {
       // display: "none"
      }
      //  display: "none"
    },
    MuiTableSortLabel: {
      root: {
        "&:hover": {
          color: "white"
        },
        "&:focus": {
          color: "white"
        },
        MuiTableSortLabel: {
          "&:active": { color: "white" }
        }
      }
    },

    MTableToolbar: {
      title: {
        color: "#09386c !important"
      }
    },
    MuiStepper: {
      root: {
        paddingTop: 8,
        paddingBottom: 16,
        paddingLeft: 0,
        paddingRight: 0,
        backgroundColor: "#f6f7ff"
      }
    },
    MuiExpansionPanelSummary: {
      root: {
        background: "white"
      },
      content: {
        margin: "12px 0 !important"
      },
      // minwidth: "196%"
    }
  }
};
