// import axios from "axios";
import _config from "../../utils/_config";
import api from  '../../utils/httputils';

export const getInvoiceSummary = (searchdata, page) => { 
  return new Promise((resolve, reject) => {  
    let base_url = "";
    base_url = _config.BASE_URL;    
    let full_url = base_url + "/api/report/getReport/" + page;    
    api
      .post(full_url, searchdata)
      .then(response => {
        resolve({
          responseCode: response.data.responseCode,          
          result: response.data.data,
          error : response.data.errors          
        });
      })
      .catch(response => {
        reject("Api call failed!");
      });
  });
};


  export const getInvoiceDetails = p_id => {  
    return new Promise((resolve, reject) => {
      let base_url = "";
      base_url = _config.BASE_URL;    
      let full_url = base_url + "/api/report/getReportDetails/" + p_id;   
      api
        .get(full_url)
        .then(response => {
          resolve({
            responseCode: response.data.responseCode,
            result: response.data.data,
            error : response.data.errors
          });
        })
        .catch(response => {
          reject("Api call failed!");
        });
    });
};

// download
export const getAuditLog = (auditUUID) => { 
  return new Promise((resolve, reject) => { 
    let base_url = "";
    base_url = _config.BASE_URL;   
    let full_url = base_url + "/api/infrastructure/getAuditLog/" + auditUUID;    
    // axios
    api
      .get(full_url)
      .then(response => {
        resolve({
          responseCode: response.data.responseCode,          
          result: response.data.data,
          error : response.data.errors        
        });
      })
      .catch(response => {
        reject("Api call failed!");
      });
  });
};
 
