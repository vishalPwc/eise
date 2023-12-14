import axios from "axios";

function setAuthorizationHeader(p_isBlob) {
  return {};

  const token = localStorage.getItem('Erpa-Usertoken');
  let config = {}
  // alert(p_isBlob);
  if(p_isBlob == true) {
    config.responseType= "blob"
  }
  config.headers = {
       'Erpa-Usertoken': token 
      
  }
  return config;
}
// add interceptor

// axios.interceptors.request.use(request =>{
  
//   console.log(request)
//  return request
// }, error =>{
 
//   return Promise.reject(error);
// })

// axios.interceptors.response.use(response =>{
 
//   console.log(response)
//  return response
// }, error =>{
 
//   return Promise.reject(error);
// })

const api = (() => {
  const post = (fullUrl, data, p_timeout, p_isBlob) => {
    return axios.post(fullUrl, data, setAuthorizationHeader(p_isBlob));
  };

  const get = (fullUrl, p_timeout, p_isBlob) => {
     return axios
        .get(fullUrl, setAuthorizationHeader(p_isBlob))
  }; 
  return {
    get,
    post,
  };
})();


export default api;
