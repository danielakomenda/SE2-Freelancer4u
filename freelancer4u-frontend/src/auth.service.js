import createAuth0Client from "@auth0/auth0-spa-js";
import { user, jwt_token } from "./store";
import { push } from "svelte-spa-router";
import axios from "axios";
import config from "./auth.config";

let auth0Client;

async function createClient() {
    console.log("Checkpoint 1")
  auth0Client = await createAuth0Client({
    domain: config.auth0_domain,
    client_id: config.auth0_client_id,
  });
}

async function loginWithPopup() {
  try {
      console.log("Checkpoint 2")
    await createClient();
    await auth0Client.loginWithPopup();
    user.set(await auth0Client.getUser());
    const claims = await auth0Client.getIdTokenClaims();
    const id_token = await claims.__raw;
    jwt_token.set(id_token);
    console.log(id_token);
  } catch (e) {
    console.error(e);
  }
}

// Auth0 signup endpoint documentation: see https://auth0.com/docs/libraries/custom-signup#using-the-api
      // you can set any of these properties as well if needed
      // username: "johndoe", // if not provided, email will be used as username for login. if provided, username has to be validated (must not already exist)
      // given_name: "John",
      // family_name: "Doe",
      // nickname: "Johnny", // if not provided, the part before the @ of the e-mail address will be used
      // name: "John Doe",
      // picture: "http://example.org/jdoe.png",
    
      // wait 2 seconds. Explanation: The user roles are set automatically on signup,
      // but we have to wait a short amount of time to make sure that the roles are
      // stored in the database of auth0. Otherwise the roles may not be in the
      // userinfo object on the first login.
      function signup(email, password, firstName = null, lastName = null) {
        var options = {
          method: "post",
          url: `https://${config.auth0_domain}/dbconnections/signup`,
          headers: { "Content-Type": "application/json" },
          data: {
            client_id: config.auth0_client_id,
            email: email,
            password: password,
            connection: "Username-Password-Authentication",
          },
        };
      
        if (firstName && firstName.length > 0) {
          options.data.given_name = firstName;
        }
      
        if (lastName && lastName.length > 0) {
          options.data.family_name = lastName;
        }
      
        axios(options)
          .then((response) => {
            // Handle successful signup
            console.log("Signup successful:", response.data);
            // Consider handling roles or additional logic here
            login(email, password, true);
          })
          .catch(function (error) {
            // Handle signup failure
            console.error("Signup failed:", error.response ? error.response.data : error.message);
            alert("Signup failed. Check console for details.");
          });
      }
      

      function login(username, password, redirectToHome = false) {
        var options = {
          method: "post",
          url: `https://${config.auth0_domain}/oauth/token`,
          headers: { "Content-Type": "application/json" },
          data: {
            grant_type: "password",
            username: username,
            password: password,
            audience: `https://${config.auth0_domain}/api/v2/`,
            scope: "openid profile email",
            client_id: config.auth0_client_id,
          },
        };
      
        axios(options)
          .then((response) => {
            const { id_token, access_token } = response.data;
            jwt_token.set(id_token);
            console.log("Login successful:", id_token);
            getUserInfo(access_token);
            if (redirectToHome) {
              setTimeout(() => {
                push("/");
              }, 500);
            }
          })
          .catch(function (error) {
            console.error("Login failed:", error.response ? error.response.data : error.message);
            alert("Login failed. Check console for details.");
          });
      }
      

function getUserInfo(access_token) {
  var options = {
    method: "get",
    url: `https://${config.auth0_domain}/oauth/userinfo`,
    headers: {
      "Content-Type": "application/json",
      Authorization: "Bearer " + access_token,
    },
  };

  axios(options)
    .then((response) => {
      const userInfo = response.data;
      user.set(userInfo);
    })
    .catch(function (error) {
      alert("getUserInfo failed");
      console.log(error);
    });
}

async function logout() {
  try {
    user.set({});
    jwt_token.set("");
    await auth0Client.logout({ returnTo: window.location.origin });
  } catch (e) {
    //console.error(e);
  }
  push("/"); // return to main page
}

const auth = {
  signup,
  login,
  loginWithPopup,
  logout,
};

export default auth;
