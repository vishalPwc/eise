
const dev = {
   BASE_URL: "https://cdc-eagle-ecreturns-app.ecomint.aafes.com:3003/eise",
 };

const prod = {
  BASE_URL: "https://cdc-eagle-ecreturns-app.ecomint.aafes.com:3003/eise",
};

const eagle_hqs = {
  BASE_URL: "https://hqs-eagle-ecsettle-app.ecomint.aafes.com/eise",
 };

 const eagle_cdc = {
  BASE_URL: "https://cdc-eagle-ecsettle-app.ecomint.aafes.com/eise",
 };

 const fox_hqs = {
  BASE_URL: "https://hqs-fox-ecsettle-app.ecomint.aafes.com/eise",
 };

 const fox_cdc = {
  BASE_URL: "https://cdc-fox-ecsettle-app.ecomint.aafes.com/eise",
 };

 const goat_hqs = {
  BASE_URL: "https://hqs-goat-ecsettle-app.ecomint.aafes.com/eise",
 };

 const goat_cdc = {
  BASE_URL: "https://cdc-goat-ecsettle-app.ecomint.aafes.com/eise",
 };

 const hawk_hqs = {
  BASE_URL: "https://hqs-hawk-ecsettle-app.ecomint.aafes.com/eise",
 };

 const hawk_cdc = {
  BASE_URL: "https://cdc-hawk-ecsettle-app.ecomint.aafes.com/eise",
 };

 const perf_hqs = {
  BASE_URL: "https://hqs-perf-ecsettle-app.ecomint.aafes.com/eise",
 };

 const perf_cdc = {
  BASE_URL: "https://cdc-perf-ecsettle-app.ecomint.aafes.com/eise",
 };

 const prod_hqs = {
  BASE_URL: "https://hqs-prod-ecsettle-app.ecomint.aafes.com/eise",
 };

 const prod_cdc = {
  BASE_URL: "https://cdc-prod-ecsettle-app.ecomint.aafes.com/eise",
 };

 const patch_hqs = {
  BASE_URL: "https://hqs-patch-ecsettle-app.ecomint.aafes.com/eise",
 };

 const patch_cdc = {
  BASE_URL: "https://cdc-patch-ecsettle-app.ecomint.aafes.com/eise",
 };


console.log(window);
const hostname = window && window.location && window.location.hostname;

const APACHE_ENV =  window.UI_ENV;

let config;

if (APACHE_ENV === 'dev') {
  config = dev;
 } else if (APACHE_ENV === 'prod') {
  config = prod;
 } 
 else if (APACHE_ENV === 'eagle_hqs') {
  config = eagle_hqs;
 }
 else if (APACHE_ENV === 'eagle_cdc') {
  config = eagle_cdc;
 }
 else if (APACHE_ENV === 'fox_hqs') {
  config = fox_hqs;
 }
 else if (APACHE_ENV === 'fox_cdc') {
  config = fox_cdc;
 }
 else if (APACHE_ENV === 'goat_hqs') {
  config = goat_hqs;
 }
 else if (APACHE_ENV === 'goat_cdc') {
  config = goat_cdc;
 }
 else if (APACHE_ENV === 'hawk_hqs') {
  config = hawk_hqs;
 }
 else if (APACHE_ENV === 'hawk_cdc') {
  config = hawk_cdc;
 }
 else if (APACHE_ENV === 'perf_hqs') {
  config = perf_hqs;
 }
 else if (APACHE_ENV === 'perf_cdc') {
  config = perf_cdc;
 }
 else if (APACHE_ENV === 'prod_hqs') {
  config = prod_hqs;
 }
 else if (APACHE_ENV === 'prod_cdc') {
  config = prod_cdc;
 }
 else if (APACHE_ENV === 'patch_hqs') {
  config = patch_hqs;
 }
 else if (APACHE_ENV === 'patch_cdc') {
  config = patch_cdc;
 }else {
  //config = dev;
  alert("ENV Details not found. Please specify the ENV in Apache");
 }

export default {

  // Add common config values here
  TOKEN_KEY: "jwt-LOGADM",
  MAX_ATTACHMENT_SIZE: 5000000,
  ...config
};
