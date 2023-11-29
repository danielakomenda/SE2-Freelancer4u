<script>
  import axios from "axios";
  import { isAuthenticated, user } from "../store"

  const api_root = window.location.origin;

// The Details to the User-Object can be found in the documentation: https://auth0.com/docs/libraries/custom-signup#using-the-api

</script>

{#if $isAuthenticated}

<h1 class="mt-3">Account Details</h1>
<img src="{$user.picture}" alt="User Picture">

<table>
    <tr>
        <td>Name:</td>
        <td>{$user.name}</td>
    </tr>

    <tr>
        <td>Nickname:</td>
        <td>{$user.nickname}</td>
    </tr>

    <tr>
        <td>First Name:</td>
        <td>{$user.given_name}</td>
    </tr>

    <tr>
        <td>Last Name:</td>
        <td>{$user.family_name}</td>
    </tr>
    
    <tr>
        <td>Email:</td>
        <td>{$user.email}</td>
    </tr>

    {#if $user.user_roles && $user.user_roles.length>0}
    <tr>
        <td>Role:</td>
        <td>{$user.user_roles}</td>
    </tr>
    {/if}
</table>

{/if}