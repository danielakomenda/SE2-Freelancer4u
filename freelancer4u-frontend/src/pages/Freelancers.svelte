<script>
    import axios from "axios";
    import { jwt_token } from "../store";


    const api_root = window.location.origin;
  
    let freelancers = [];
    let freelancer = {
      name: null,
      email: null,
    };
  
    function getFreelancer() {
      var config = {
        method: "get",
        url: api_root + "/api/freelancer",
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
    getFreelancer();
  
    function createFreelancer() {
      var config = {
        method: "post",
        url: api_root + "/api/freelancer",
        headers: {
          "Content-Type": "application/json",
        },
        data: freelancer,
      };
  
      axios(config)
        .then(function (response) {
          alert("Freelancer created");
          getFreelancer();
        })
        .catch(function (error) {
          alert("Could not create Freelancer");
          console.log(error);
        });
    }
  </script>
  
  
  <h1 class="mt-3">Create Freelancer</h1>
  <form class="mb-5">
    <div class="row mb-3">
      <div class="col">
        <label class="form-label" for="name">Name</label>
        <input
          bind:value={freelancer.name}
          class="form-control"
          id="name"
          type="text"
        />
      </div>
    </div>
    <div class="row mb-3">

      <div class="col">
        <label class="form-label" for="email">Email</label>
        <input
          bind:value={freelancer.email}
          class="form-control"
          id="email"
          type="email"
        />
      </div>
    </div>
    
    <button type="button" class="btn btn-primary" on:click={createFreelancer}
      >Submit</button
    >
  </form>
  
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

  