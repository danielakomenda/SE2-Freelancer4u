<script>
  import axios from "axios";
  import { jwt_token } from "../store";
  import {isAuthenticated, user} from "../store";

  // TODO: Setze hier die URL zu deinem mit Postman erstellten Mock Server
  const api_root = window.location.origin;

  let jobs = [];
  let job = {
    description: null,
    earnings: null,
    jobType: null,
  };

  function getJobs() {
      testPrint()
      var config = {
      method: "get",
      url: api_root + "/api/job",
      headers: { Authorization: "Bearer " + $jwt_token }, // Token wird als Header übergeben
    };

    axios(config)
      .then(function (response) {
        jobs = response.data;
      })
      .catch(function (error) {
        alert("Could not get jobs");
        console.log(error);
      });
  }
  getJobs();

  function createJob() {
    var config = {
      method: "post",
      url: api_root + "/api/job",
      headers: {
        "Content-Type": "application/json",
      },
      data: job,
    };

    axios(config)
      .then(function (response) {
        alert("Job created");
        getJobs();
      })
      .catch(function (error) {
        alert("Could not create Job");
        console.log(error);
      });
  }


function testPrint(){
    console.log("Test-Print")
}



</script>

{#if $isAuthenticated && $user.user_roles && $user.user_roles.includes("admin")}
<h1 class="mt-3">Create Job</h1>
<form class="mb-5">
  <div class="row mb-3">
    <div class="col">
      <label class="form-label" for="description">Description</label>
      <input
        bind:value={job.description}
        class="form-control"
        id="description"
        type="text"
      />
    </div>
  </div>
  <div class="row mb-3">
    <div class="col">
      <label class="form-label" for="type">Type</label>
      <select bind:value={job.jobType} class="form-select" id="type">
        <option value="OTHER">OTHER</option>
        <option value="TEST">TEST</option>
        <option value="IMPLEMENT">IMPLEMENT</option>
        <option value="REVIEW">REVIEW</option>
      </select>
    </div>
    <div class="col">
      <label class="form-label" for="earnings">Earnings</label>
      <input
        bind:value={job.earnings}
        class="form-control"
        id="earnings"
        type="number"
      />
    </div>
  </div>
  <button type="button" class="btn btn-primary" on:click={createJob}
    >Submit</button
  >
</form>

{/if}
<h1>All Jobs</h1>
<table class="table">
  <thead>
    <tr>
      <th scope="col">Description</th>
      <th scope="col">Type</th>
      <th scope="col">Earnings</th>
      <th scope="col">State</th>
      <th scope="col">FreelancerId</th>
    </tr>
  </thead>
  <tbody>
    {#each jobs as job}
      <tr>
        <td>{job.description}</td>
        <td>{job.jobType}</td>
        <td>{job.earnings}</td>
        <td>{job.jobState}</td>
        <td>{job.freelancerId}</td>
      </tr>
    {/each}
  </tbody>
</table>
