package com.aafes.settlement.sso;

public class SAMLUserInfo {
 private boolean enabled;
 private String password;
 private String username;
 private boolean accountNonExpired;
 private boolean accountNonLocked;
 private boolean credentialsNonExpired;


 public boolean getEnabled() {
  return enabled;
 }

 public String getPassword() {
  return password;
 }

 public String getUsername() {
  return username;
 }

 public boolean getAccountNonExpired() {
  return accountNonExpired;
 }

 public boolean getAccountNonLocked() {
  return accountNonLocked;
 }

 public boolean getCredentialsNonExpired() {
  return credentialsNonExpired;
 }


 public void setEnabled(boolean enabled) {
  this.enabled = enabled;
 }

 public void setPassword(String password) {
  this.password = password;
 }

 public void setUsername(String username) {
  this.username = username;
 }

 public void setAccountNonExpired(boolean accountNonExpired) {
  this.accountNonExpired = accountNonExpired;
 }

 public void setAccountNonLocked(boolean accountNonLocked) {
  this.accountNonLocked = accountNonLocked;
 }

 public void setCredentialsNonExpired(boolean credentialsNonExpired) {
  this.credentialsNonExpired = credentialsNonExpired;
 }

}