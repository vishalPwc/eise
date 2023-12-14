const BASE_URL = "http://192.168.15.141:8002";
//const BASE_URL = "http://localhost:8002";
//const AUTH_HEADER   = "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiI1ZDY2NjZjZGE4N2E3ZjRkMDhkMzBjNmIiLCJmdWxsIjp7Il9pZCI6IjVkNjY2NmNkYTg3YTdmNGQwOGQzMGM2YiIsInVzZXJuYW1lIjoidXNlcjk5IiwiZmlyc3ROYW1lIjoidXNlcjk5IiwibGFzdE5hbWUiOiJ1c2VyOTkiLCJ1c2VydHlwZSI6IkJVU0lORVNTQURNSU4iLCJjcmVhdGVkRGF0ZSI6IjIwMTktMDgtMjhUMTE6MzQ6MzcuMDkyWiIsIl9fdiI6MH0sInR5cGUiOiJCVVNJTkVTU0FETUlOIiwiaWF0IjoxNTY2OTkyNzM4fQ.B11NK3QKkQx8YTKIQo-nrdEkiZzf--2OT35W_E1aS1c";
//const AUTH_HEADER_1 = "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiI1Y2Q5NTA0YTk4NmFhNTFmNDA4ODk2MTUiLCJmdWxsIjp7Il9pZCI6IjVjZDk1MDRhOTg2YWE1MWY0MDg4OTYxNSIsImZpcnN0TmFtZSI6InNhY2hpbiIsImxhc3ROYW1lIjoicmFqd2FkZSIsInVzZXJuYW1lIjoidXNlcjIiLCJjcmVhdGVkRGF0ZSI6IjIwMTktMDUtMTNUMTE6MDg6NTguMDMwWiIsIl9fdiI6MH0sImlhdCI6MTU2NDY3OTcxOH0.5I02gKqrIn-0Tm594Zdi6Lj4H0xwJnYx8Ur5Ji6djI4"

// function setAuthorizationHeader(token) {
//     let auth = localStorage.getItem(TOKEN_KEY);
//     let config = {
//         headers: {
//             'Authorization': "Bearer " + auth
//         }
//     }
//     return config;
//   }

//   function setUploadAuthorizationHeader(token) {
//     let auth = localStorage.getItem(TOKEN_KEY);
//     let config = {
//         headers: {
//             'Authorization': "Bearer " + auth
//         }
//     }
//     return config;
//   }
module.exports = {
  BASE_URL
  // AUTH_HEADER,
  // setAuthorizationHeader,
  // setUploadAuthorizationHeader
};
