import Home from "./pages/Home.svelte";
import Freelancers from "./pages/Freelancers.svelte";
import Jobs from "./pages/Jobs.svelte";
import Signup from "./pages/Signup.svelte";
import Account from "./pages/Account.svelte";

export default {
    '/': Home,
    '/home': Home,
    '/freelancers': Freelancers,
    '/jobs': Jobs,
    '/signup': Signup,
    '/account': Account
}