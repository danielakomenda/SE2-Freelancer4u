import { defineConfig } from "cypress";

export default defineConfig({
  e2e: {
    setupNodeEvents(on, config) {
      // implement node event listeners here
    },
    testIsolation: false, // damit nicht bei jedem Test neu angemeldet werden muss
  },
});
