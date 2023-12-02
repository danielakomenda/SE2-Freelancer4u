<script>
  import axios from "axios";
  import { jwt_token, user } from "../store";
  import { querystring } from "svelte-spa-router";

  const api_root = window.location.origin;

  let currentPage;
  let nrOfPages = 0;
  let defaultPageSize = 4;

  let freelancers = [];
  let freelancer = {
    name: null,
    email: null,
  };

  $: {
    if ($jwt_token !== "") {
      let searchParams = new URLSearchParams($querystring);

      if (searchParams.has("page")) {
        currentPage = searchParams.get("page");
      } else {
        currentPage = "1";
      }
      getFreelancer();
    }
  }

  function getFreelancer() {
    let query = "?pageSize=" + defaultPageSize + "&pageNumber=" + currentPage;
    var config = {
      method: "get",
      url: api_root + "/api/freelancer/getall" + query,
      headers: { Authorization: "Bearer " + $jwt_token }, // Token wird als Header übergeben
    };
    axios(config)
      .then(function (response) {
        freelancers = response.data.content;
        nrOfPages = response.data.totalPages;
      })
      .catch(function (error) {
        alert("Could not get freelancer");
        console.log(error);
      });
  }

  function createFreelancer() {
    var config = {
      method: "post",
      url: api_root + "/api/freelancer/create",
      headers: {
        "Content-Type": "application/json",
        Authorization: "Bearer " + $jwt_token,
      },
      data: freelancer,
    };
    axios(config)
      .then(function (response) {
        alert("Freelancer created");
        getFreelancer();
      })
      .catch(function (error) {
        alert("Could not create Freelancer. Email already exists.");
        console.log(error);
      });
  }

  function validateEmailAndCreateFreelancer() {
    var config = {
      method: "get",
      url: "https://disify.com/api/email/" + freelancer.email,
    };
    axios(config)
      .then(function (response) {
        if (response.data.format && !response.data.disposable && response.data.dns) {
          createFreelancer();
        } else {
          alert("Email " + freelancer.email + " is not valid.");
        }
      })
      .catch(function (error) {
        alert("Could not validate email");
        console.log(error);
      });
  }
</script>

<!-- CREATE FREELANCER: SICHTBAR FÜR ALLE USER -->
<h1 class="mt-3">Create Freelancer</h1>
<form class="mb-5">
  <div class="row mb-3">
    <div class="col">
      <label class="form-label" for="name">Name</label>
      <input bind:value={freelancer.name} class="form-control" id="name" type="text" />
    </div>
  </div>
  <div class="row mb-3">
    <div class="col">
      <label class="form-label" for="email">Email</label>
      <input bind:value={freelancer.email} class="form-control" id="email" type="email" />
    </div>
  </div>

  <button type="button" class="btn btn-primary" on:click={validateEmailAndCreateFreelancer}>Submit</button>
</form>

{#if $user.user_roles && $user.user_roles.includes("admin")}
  <!-- LISTE VON FREELANCER: SICHTBAR FÜR ADMINS -->
  <h1>All Freelancers</h1>
  <table class="table">
    <thead>
      <tr>
        <th scope="col">Name</th>
        <th scope="col">E-Mail</th>
      </tr>
    </thead>
    <tbody>
      {#each freelancers as freelancer}
        <tr>
          <td>{freelancer.name}</td>
          <td>{freelancer.email}</td>
        </tr>
      {/each}
    </tbody>
  </table>

  <nav>
    <ul class="pagination">
      {#each Array(nrOfPages) as _, i}
        <li class="page-item">
          <a class="page-link" class:active={currentPage == i + 1} href={"#/freelancers?page=" + (i + 1)}>{i + 1}</a>
        </li>
      {/each}
    </ul>
  </nav>
{/if}
