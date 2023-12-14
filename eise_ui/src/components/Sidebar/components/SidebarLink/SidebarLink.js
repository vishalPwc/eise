import React, { useState } from "react";
import {
  Collapse,
  Divider,
  List,
  ListItem,
  ListItemIcon,
  ListItemText,
  Typography
} from "@material-ui/core";
import { Inbox as InboxIcon } from "@material-ui/icons";
import { Link } from "react-router-dom";
import classnames from "classnames";
import SubdirectoryArrowRightIcon from "@material-ui/icons/SubdirectoryArrowRight";

// styles
import useStyles from "./styles";
import { getMenuIcon } from "../../../../utils/commonutils";
// components
//import Dot from "../Dot";

export default function SidebarLink({
  route,
  menuIcon,
  menuKey,
  children,
  childMenu,
  hasChildren,
  location,
  isSidebarOpened,
  nested,
  type,
  parentClick
}) {
  var classes = useStyles();

  // const Components = {
  //   ShoppingBasketIcon: ShoppingBasketIcon,
  //   ViewComfyIcon: ViewComfyIcon,
  //   StorageIcon: StorageIcon,
  //   SearchIcon: SearchIcon,
  //   ArrowDropDownIcon: ArrowDropDownIcon,
  //   LabelIcon: LabelIcon
  // };

  // const getMenuIcon = iconName => {

  //   let TempComponent = Components[iconName];

  //   return <TempComponent />;
  // };
  // const MaterialIcon = ({ icon }) => {

  //  let iconName = icon.replace(/Icon$/, '')
  //  //iconName = icon;
  //  let resolved = require(`@material-ui/icons/${iconName}`).default

  //  if (!resolved) {
  //  //throw Error(`Could not find material-ui-icons/${iconName}`)
  //  }

  //  return React.createElement(resolved)
  // }
  // local
  var [isOpen, setIsOpen] = useState(false);
  let link = "/app/" + route;
  var isLinkActive =
    link &&
    (location.pathname === link || location.pathname.indexOf(link) !== -1);

  if (type === "title") {
    return (
      <Typography
        className={classnames(classes.linkText, classes.sectionTitle, {
          [classes.linkTextHidden]: !isSidebarOpened
        })}
      >
        {menuKey}
      </Typography>
    );
  }

  if (type === "divider") return <Divider className={classes.divider} />;

  if (hasChildren === "N")
    return (
      <ListItem
        button
        component={link && Link}
        to={link}
        className={classes.link}
        classes={{
          root: classnames(classes.linkRoot, {
            [classes.linkActive]: isLinkActive && !nested,
            [classes.linkNested]: nested
          })
        }}
        disableRipple
        onClick={parentClick}
      >
        <ListItemIcon
          className={classnames(classes.linkIcon, {
            [classes.linkIconActive]: isLinkActive
          })}
        >
          {nested ? <SubdirectoryArrowRightIcon /> : getMenuIcon(menuIcon)}
        </ListItemIcon>
        <ListItemText
          classes={{
            primary: classnames(classes.linkText, {
              [classes.linkTextActive]: isLinkActive,
              [classes.linkTextHidden]: !isSidebarOpened
            })
          }}
          primary={menuKey}
        />
      </ListItem>
    );

  return (
    <>
      <ListItem
        button
        component={link && Link}
        onClick={toggleCollapse}
        className={classes.link}
        // to={link}
        disableRipple
        //   onClick={parentClick}
      >
        <ListItemIcon
          className={classnames(classes.linkIcon, {
            [classes.linkIconActive]: isLinkActive
          })}
        >
          {menuIcon ? getMenuIcon(menuIcon) : <InboxIcon />}
        </ListItemIcon>
        <ListItemText
          classes={{
            primary: classnames(classes.linkText, {
              [classes.linkTextActive]: isLinkActive,
              [classes.linkTextHidden]: !isSidebarOpened
            })
          }}
          primary={menuKey}
        />
      </ListItem>
      {/* <div className={classes.ArrowDrop}>
        <ArrowDropDownIcon />
      </div> */}
      {hasChildren === "Y" && (
        <Collapse
          in={isOpen && isSidebarOpened}
          timeout="auto"
          unmountOnExit
          className={classes.nestedList}
        >
          <List component="div" disablePadding onClick={parentClick}>
            {childMenu.map(childrenLink => (
              <SidebarLink
                key={childrenLink && childrenLink.link}
                location={location}
                isSidebarOpened={isSidebarOpened}
                classes={classes}
                nested
                {...childrenLink}
              />
            ))}
          </List>
        </Collapse>
      )}
    </>
  );

  // ###########################################################

  function toggleCollapse(e) {
    if (isSidebarOpened) {
      e.preventDefault();
      setIsOpen(!isOpen);
    } else {
      parentClick();
    }
  }
}
