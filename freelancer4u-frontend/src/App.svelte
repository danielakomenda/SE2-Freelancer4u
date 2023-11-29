<script>
	import Router from "svelte-spa-router";
	import routes from "./routes";
    import {isAuthenticated, user} from "./store";
    import auth from "./auth.service";
</script>

<div id="app">
	<nav class="navbar navbar-expand-lg bg-light">
		<div class="container-fluid">
			<a class="navbar-brand" href="#/">Freelancer4U</a>
			<button
				class="navbar-toggler"
				type="button"
				data-bs-toggle="collapse"
				data-bs-target="#navbarNav"
				aria-controls="navbarNav"
				aria-expanded="false"
				aria-label="Toggle navigation"
			>
				<span class="navbar-toggler-icon" />
			</button>
			<div class="collapse navbar-collapse" id="navbarNav">
				<ul class="navbar-nav me-auto mb-2 mb-lg-0">
                    {#if $isAuthenticated && $user.user_roles && $user.user_roles.includes("admin")}
					<li class="nav-item">
						<a
							class="nav-link"
							aria-current="page"
							href="#/freelancers">Freelancers</a
						>
					</li>
                    {/if}
                    
                    {#if $isAuthenticated}
					<li class="nav-item">
						<a class="nav-link" href="#/jobs">Jobs</a>
					</li>
                    {/if}
                    {#if $isAuthenticated}
					<li class="nav-item">
						<a class="nav-link" href="#/account">Account</a>
					</li>
                    {/if}
				</ul>
                <div class="d-flex">
                    {#if $isAuthenticated}
                    <span class="navbar-text me-2">
                        {$user.name}
                    </span>
                    <button
                    type="button"
                    class="btn btn-primary"
                    on:click={auth.logout}>Log Out</button>
                    {/if}
                </div>
			</div>
		</div>
	</nav>

	<div class="container">
		<Router {routes} />
	</div>
</div>