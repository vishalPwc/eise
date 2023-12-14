import React from "react";
import ShoppingBasketIcon from "@material-ui/icons/ShoppingBasket";
import ViewComfyIcon from "@material-ui/icons/ViewComfy";
import StorageIcon from "@material-ui/icons/Storage";
import LabelIcon from '@material-ui/icons/Label';
import SearchIcon from "@material-ui/icons/Search";
import ArrowDropDownIcon from "@material-ui/icons/ArrowDropDown";
import ArrowBackIcon from "@material-ui/icons/ArrowBack";
import SwapHorizIcon from '@material-ui/icons/SwapHoriz';
import InputIcon from '@material-ui/icons/Input';
import SubdirectoryArrowRightIcon from '@material-ui/icons/SubdirectoryArrowRight';
import AssignmentReturnedIcon from '@material-ui/icons/AssignmentReturned';

function getInitials(name) {
    return name !== undefined
      ? name == null ? '' : name
          .replace(/\s+/, " ")
          .split(" ")
          .slice(0, 1)
          .map(v => (v && v !== undefined ? v[0].toUpperCase() : ""))
          .join("")
      : (name = "");
  }

function getNamePart(name){
    return name.slice(1, name.length)
}

const approveData = [
    {
      value: "Approve",
      label: "Approve"
    },
    {
      value: "Reject",
      label: "Reject"
    }
  ];

function currencyFormat(num) {
    return '$ ' + num.toFixed(2).replace(/(\d)(?=(\d{3})+(?!\d))/g, '$1,')
 }
 const stateData = [
  {
      "name": "Alabama",
      "abbreviation": "AL"
  },
  {
      "name": "Alaska",
      "abbreviation": "AK"
  },
  {
      "name": "American Samoa",
      "abbreviation": "AS"
  },
  {
      "name": "Arizona",
      "abbreviation": "AZ"
  },
  {
      "name": "Arkansas",
      "abbreviation": "AR"
  },
  {
      "name": "California",
      "abbreviation": "CA"
  },
  {
      "name": "Colorado",
      "abbreviation": "CO"
  },
  {
      "name": "Connecticut",
      "abbreviation": "CT"
  },
  {
      "name": "Delaware",
      "abbreviation": "DE"
  },
  {
      "name": "District Of Columbia",
      "abbreviation": "DC"
  },
  {
      "name": "Federated States Of Micronesia",
      "abbreviation": "FM"
  },
  {
      "name": "Florida",
      "abbreviation": "FL"
  },
  {
      "name": "Georgia",
      "abbreviation": "GA"
  },
  {
      "name": "Guam",
      "abbreviation": "GU"
  },
  {
      "name": "Hawaii",
      "abbreviation": "HI"
  },
  {
      "name": "Idaho",
      "abbreviation": "ID"
  },
  {
      "name": "Illinois",
      "abbreviation": "IL"
  },
  {
      "name": "Indiana",
      "abbreviation": "IN"
  },
  {
      "name": "Iowa",
      "abbreviation": "IA"
  },
  {
      "name": "Kansas",
      "abbreviation": "KS"
  },
  {
      "name": "Kentucky",
      "abbreviation": "KY"
  },
  {
      "name": "Louisiana",
      "abbreviation": "LA"
  },
  {
      "name": "Maine",
      "abbreviation": "ME"
  },
  {
      "name": "Marshall Islands",
      "abbreviation": "MH"
  },
  {
      "name": "Maryland",
      "abbreviation": "MD"
  },
  {
      "name": "Massachusetts",
      "abbreviation": "MA"
  },
  {
      "name": "Michigan",
      "abbreviation": "MI"
  },
  {
      "name": "Minnesota",
      "abbreviation": "MN"
  },
  {
      "name": "Mississippi",
      "abbreviation": "MS"
  },
  {
      "name": "Missouri",
      "abbreviation": "MO"
  },
  {
      "name": "Montana",
      "abbreviation": "MT"
  },
  {
      "name": "Nebraska",
      "abbreviation": "NE"
  },
  {
      "name": "Nevada",
      "abbreviation": "NV"
  },
  {
      "name": "New Hampshire",
      "abbreviation": "NH"
  },
  {
      "name": "New Jersey",
      "abbreviation": "NJ"
  },
  {
      "name": "New Mexico",
      "abbreviation": "NM"
  },
  {
      "name": "New York",
      "abbreviation": "NY"
  },
  {
      "name": "North Carolina",
      "abbreviation": "NC"
  },
  {
      "name": "North Dakota",
      "abbreviation": "ND"
  },
  {
      "name": "Northern Mariana Islands",
      "abbreviation": "MP"
  },
  {
      "name": "Ohio",
      "abbreviation": "OH"
  },
  {
      "name": "Oklahoma",
      "abbreviation": "OK"
  },
  {
      "name": "Oregon",
      "abbreviation": "OR"
  },
  {
      "name": "Palau",
      "abbreviation": "PW"
  },
  {
      "name": "Pennsylvania",
      "abbreviation": "PA"
  },
  {
      "name": "Puerto Rico",
      "abbreviation": "PR"
  },
  {
      "name": "Rhode Island",
      "abbreviation": "RI"
  },
  {
      "name": "South Carolina",
      "abbreviation": "SC"
  },
  {
      "name": "South Dakota",
      "abbreviation": "SD"
  },
  {
      "name": "Tennessee",
      "abbreviation": "TN"
  },
  {
      "name": "Texas",
      "abbreviation": "TX"
  },
  {
      "name": "Utah",
      "abbreviation": "UT"
  },
  {
      "name": "Vermont",
      "abbreviation": "VT"
  },
  {
      "name": "Virgin Islands",
      "abbreviation": "VI"
  },
  {
      "name": "Virginia",
      "abbreviation": "VA"
  },
  {
      "name": "Washington",
      "abbreviation": "WA"
  },
  {
      "name": "West Virginia",
      "abbreviation": "WV"
  },
  {
      "name": "Wisconsin",
      "abbreviation": "WI"
  },
  {
      "name": "Wyoming",
      "abbreviation": "WY"
  }
]


  const Components = {
    ShoppingBasketIcon: ShoppingBasketIcon,
    ViewComfyIcon: ViewComfyIcon,
    StorageIcon: StorageIcon,
    SearchIcon: SearchIcon,
    ArrowDropDownIcon: ArrowDropDownIcon,
    LabelIcon: LabelIcon,
    ArrowBackIcon:ArrowBackIcon,
    SwapHorizIcon:SwapHorizIcon,
    InputIcon:InputIcon,
    SubdirectoryArrowRightIcon:SubdirectoryArrowRightIcon,
    AssignmentReturnedIcon:AssignmentReturnedIcon
  };
  const getMenuIcon = iconName => {
    if(iconName === undefined){
      return "";
    }
    let TempComponent = Components[iconName];
    //console.log(TempComponent);
    return <TempComponent />;
  };

  const showConsole = (p_text) => {
    console.log(p_text);
    return false;
  };

  const getLongDate = (d1) => {
    debugger;
    let year = d1.getFullYear();
    let month = (1 + d1.getMonth()).toString();
    month = month.length > 1 ? month : '0' + month;
    let day = d1.getDate().toString();
    day = day.length > 1 ? day : '0' + day;
    let hours = d1.getHours();
    let mins = d1.getMinutes();
    return month + '/' + day + '/' + year + ' ' + hours + ':' + mins ;
}

 

const getShortDate = (d1) => {
    let year = d1.getFullYear();
    let month = (1 + d1.getMonth()).toString();
    month = month.length > 1 ? month : '0' + month;
    let day = d1.getDate().toString();
    day = day.length > 1 ? day : '0' + day;
    return  day + '-' + month + '-' + year;
}
  
export {
    getInitials,
    getNamePart,
    approveData,
    currencyFormat,
    stateData,
    getMenuIcon,
    showConsole,
    getLongDate,
    getShortDate
  }