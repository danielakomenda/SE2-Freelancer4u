import Home from "./pages/Home.svelte";
import Freelancers from "./pages/Freelancers.svelte";
import Jobs from "./pages/Jobs.svelte";

export default {
    '/': Home,
    '/home': Home,
    '/freelancers': Freelancers,

    '/jobs': Jobs
}