<script>
  import axios from "axios";
  import { jwt_token, user, userId } from "../store";
  import { querystring } from "svelte-spa-router";

  const api_root = window.location.origin;

  let currentPage;
  let nrOfPages = 0;
  let defaultPageSize = 4;

  let earningsMin;
  let jobType;

  let freelancers = [];

  let jobs = [];
  let job = {
    description: null,
    earnings: null,
    jobType: null,
  };

  $: {
    if ($jwt_token !== "") {
      let searchParams = new URLSearchParams($querystring);

      if (searchParams.has("page")) {
        currentPage = searchParams.get("page");
      } else {
        currentPage = "1";
      }
      getJobs();
    }
  }


  function getJobs() {
    let query = "?pageSize=" + defaultPageSize + "&pageNumber=" + currentPage;
    if (earningsMin) {
      query += "&min=" + (earningsMin - 1);
    }
    if (jobType && jobType !== "ALL") {
      query += "&type=" + jobType;
    }
    var config = {
      method: "get",
      url: api_root + "/api/job" + query,
      headers: { Authorization: "Bearer " + $jwt_token }, // Token wird als Header übergeben
    };
    axios(config)
      .then(function (response) {
        jobs = response.data.content;
        nrOfPages = response.data.totalPages;
      })
      .catch(function (error) {
        alert("Could not get jobs");
        console.log(error);
      });
  }

  function createJob() {
    var config = {
      method: "post",
      url: api_root + "/api/job",
      headers: {
        "Content-Type": "application/json",
        Authorization: "Bearer " + $jwt_token
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

  function assignToMe(jobId) {
    var config = {
      method: "put",
      url: api_root + "/api/me/assignjob?jobId=" + jobId,
      headers: { Authorization: "Bearer " + $jwt_token },
    };
    axios(config)
      .then(function (response) {
        getJobs();
      })
      .catch(function (error) {
        alert("Could not assign job to me");
        console.log(error);
      });
  }

  function completeMyJob(jobId) {
    var config = {
      method: "put",
      url: api_root + "/api/me/completejob?jobId=" + jobId,
      headers: { Authorization: "Bearer " + $jwt_token },
    };
    axios(config)
      .then(function (response) {
        getJobs();
      })
      .catch(function (error) {
        alert("Could not complete the job");
        console.log(error);
      });
  }


  function assignJobToFreelancer(jobId, freelancerEmail) {
    var config = {
      method: "put",
      url: api_root + "/api/service/assignjob",
      headers: { Authorization: "Bearer " + $jwt_token, "Content-Type": "application/json" },
      data: {
        jobId: jobId,
        freelancerEmail: freelancerEmail,
      },
    };
    axios(config)
      .then(function (response) {
        alert("Assigned job to:" +freelancerEmail)
        getJobs();
      })
      .catch(function (error) {
        alert("Could not assign job to me");
        console.log(error);
      });
  }


  function getFreelancer() {
    var config = {
      method: "get",
      url: api_root + "/api/freelancer/getall",
      headers: { Authorization: "Bearer " + $jwt_token }, // Token wird als Header übergeben
    };
    axios(config)
      .then(function (response) {
        freelancers = response.data;
      })
      .catch(function (error) {
        alert("Could not get freelancer");
        console.log(error);
      });
  }
</script>

{#if $user.user_roles && $user.user_roles.includes("admin")}
  <!-- CREATE JOBS: SICHTBAR FÜR ADMINS -->
  <h1 class="mt-3">Create Job</h1>
  <form class="mb-5">
    <div class="row mb-3">
      <div class="col">
        <label class="form-label" for="description">Description</label>
        <input bind:value={job.description} class="form-control" id="description" type="text" />
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
        <input bind:value={job.earnings} class="form-control" id="earnings" type="number" />
      </div>
    </div>
    <button type="button" class="btn btn-primary" on:click={createJob}>Submit</button>
  </form>
{/if}

<!-- LISTE VON JOBS: SICHTBAR FÜR ALLE USER -->
<h1>All Jobs</h1>

<div class="row my-3">
  <!--FILTER FOR EARNINGS-->
  <div class="col-auto">
    <label for="" class="col-form-label">Earnings: </label>
  </div>
  <div class="col3">
    <input class="form-contol" type="number" id=minEarnings placeholder="min" bind:value={earningsMin} />
  </div>

  <!--FILTER FOR JOBTYPE-->
  <div class="col-auto">
    <label for="" class="col-form-label">Job Type: </label>
  </div>
  <div class="col-3">
    <select bind:value={jobType} class="form-select" id="typefilter" type="text">
      <option value="ALL" />
      <option value="OTHER">OTHER</option>
      <option value="TEST">TEST</option>
      <option value="IMPLEMENT">IMPLEMENT</option>
      <option value="REVIEW">REVIEW</option>
    </select>
  </div>

  <!--APPLY-BUTTON-->
  <div class="col-3">
    <a class="btn btn-primary" href={"#/jobs?page=1&jobType=" + jobType + "&earningsMin=" + earningsMin} role="button"
      >Apply</a
    >
  </div>
</div>

<table class="table">
  <thead>
    <tr>
      <th scope="col">Description</th>
      <th scope="col">Type</th>
      <th scope="col">Earnings</th>
      <th scope="col">State</th>
      <th scope="col">FreelancerId</th>
      <th scope="col">Actions</th>
      {#if $user.user_roles && $user.user_roles.includes("admin")}
        <th scope="col">Beauftragen</th>
      {/if}
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
        <td>
          <!-- JOB-ZUWEISUNG: SICHTBAR FÜR USERs -->
          {#if job.freelancerId === null}
            <button type="button" class="btn btn-primary btn-sm" on:click={() => assignToMe(job.id)}>
              Assign to me
            </button>
          {/if}

          {#if job.jobState === "ASSIGNED" && job.freelancerId === $userId}
            <span class="badge bg-primary">In Progress</span>
            <button type="button" class="btn btn-success btn-sm" on:click={() => completeMyJob(job.id)}>
              Complete Job
            </button>
          {/if}

          {#if job.jobState === "ASSIGNED" && job.freelancerId !== $userId}
            <span class="badge bg-secondary">Already Assigned</span>
          {/if}

          {#if job.jobState === "DONE" && job.freelancerId === $userId}
            <span class="badge bg-success">Done</span>
          {/if}

          {#if job.jobState === "DONE" && job.freelancerId !== $userId}
            <span class="badge bg-secondary">Done</span>
          {/if}
        </td>

        {#if $user.user_roles && $user.user_roles.includes("admin")}
          <!-- JOB-ZUWEISUNG: SICHTBAR FÜR ADMINS -->
          <td>
            {#if job.freelancerId === null}

              <div class="dropdown">

                <button 
                class="btn btn-secondary btn-sm dropdown-toggle" 
                type="button" 
                data-bs-toggle="dropdown" 
                aria-expanded="false"
                on:click={() => getFreelancer()}>
                  Assign Freelancer
              </button>
                {#if freelancers !== null}
                <ul class="dropdown-menu">
                {#each freelancers as freelancer}
                    <li><button class="dropdown-item" type="button" on:click={() => assignJobToFreelancer(job.id, freelancer.email)}>{freelancer.name}</button></li>
                {/each}
                </ul>
                {/if}
              </div>
            {/if}
          </td>
        {/if}
      </tr>
    {/each}
  </tbody>
</table>

<nav>
  <ul class="pagination">
    {#each Array(nrOfPages) as _, i}
      <li class="page-item">
        <a class="page-link" class:active={currentPage == i + 1} href={"#/jobs?page=" + (i + 1)}>{i + 1}</a>
      </li>
    {/each}
  </ul>
</nav>
