<script>
  import { isAuthenticated, user } from "../store";
  import auth from "../auth.service";

  let email = "";
  let password = "";
  let firstname = "";
  let lastname = "";
  let signupForm;

  $: if ($isAuthenticated) {
    // reset the input values as soon as the user is registered.
    email = "";
    password = "";
  }
  function signup() {
    // form validation with bootstrap: see https://getbootstrap.com/docs/5.3/forms/validation/
    if (signupForm.checkValidity()) {
      auth.signup(email, password, firstname, lastname);
    }
    signupForm.classList.add("was-validated");
  }
</script>

{#if $isAuthenticated}
  <h1>Welcome {$user.nickname}!</h1>
{:else}
  <div class="container mt-5">
    <div class="row justify-content-center">
      <div class="col-md-6">
        <div class="card">
          <div class="card-header">Sign up</div>
          <div class="card-body">
            <form
              on:submit|preventDefault={signup}
              bind:this={signupForm}
              class="needs-validation"
              novalidate
            >
              <div class="mb-3">
                <label for="username" class="form-label">E-Mail</label>
                <input
                  bind:value={email}
                  type="email"
                  class="form-control"
                  id="username"
                  name="username"
                  required
                />
                <div class="invalid-feedback">
                  Please provide an e-mail address.
                </div>
              </div>
              <div class="mb-3">
                <label for="first-name" class="form-label">First Name</label>
                <input
                  bind:value={firstname}
                  type="text"
                  class="form-control"
                  id="first-name"
                  name="first-name"
                />
              </div>
              <div class="mb-3">
                <label for="last-name" class="form-label">Last Name</label>
                <input
                  bind:value={lastname}
                  type="text"
                  class="form-control"
                  id="last-name"
                  name="last-name"
                />
              </div>
              <div class="mb-3">
                <label for="password" class="form-label">Password</label>
                <input
                  bind:value={password}
                  type="password"
                  class="form-control"
                  id="password"
                  name="password"
                  required
                />
                <div class="invalid-feedback">Please choose a password.</div>
              </div>
              <button type="submit" class="btn btn-primary">Sign up</button>
            </form>
          </div>
        </div>
      </div>
    </div>
  </div>
{/if}
