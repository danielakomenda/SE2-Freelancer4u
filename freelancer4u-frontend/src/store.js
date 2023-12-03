import { writable, derived } from "svelte/store";
import axios from 'axios';

// user 
export const user = writable({});
let sessionUser = sessionStorage.getItem("user");

if (sessionUser) {
    user.set(JSON.parse(sessionUser));
} else {
    // create the key "user" in the session storage if it doesn't exist yet
    sessionStorage.setItem("user", {});
}
// update the user in the sessionStorage on changes
user.subscribe(user => sessionStorage.setItem("user", JSON.stringify(user)));


// isAuthenticated: we assume that users are authenticated if the property "user.name" exists
export const isAuthenticated = derived(
    user,
    $user => $user && $user.name
);

// jwt_token and myFreelancerId
export const jwt_token = writable("");
export const userId = writable(null);
export const userEmail = writable("");

let sessionToken = sessionStorage.getItem("jwt_token");
if (sessionToken) {
    jwt_token.set(sessionToken);
} else {
    // create the key "jwt_token" in the session storage if it doesn't exist yet
    sessionStorage.setItem("jwt_token", "");
}


// Get UserID
jwt_token.subscribe(jwt_token => {
    sessionStorage.setItem("jwt_token", jwt_token);
    if (jwt_token === "") {
        userId.set(null)
    } else {
        var config = {
            method: "get",
            url: window.location.origin + "/api/me/freelancer",
            headers: { Authorization: "Bearer " + jwt_token }, // Token wird als Header übergeben
        };
        axios(config)
            .then(function (response) {
                userId.set(response.data.id);
            })
            .catch(function (error) {
                alert("Could not get freelancer associated to current user");
                console.log(error);
            });
    }
});
