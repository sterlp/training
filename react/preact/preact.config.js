import { join } from "path";

/**
 * https://github.com/preactjs/preact-cli/wiki/Config-Recipes
 *
 * @param {import('preact-cli').Config} config - Original webpack config
 * @param {import('preact-cli').Env} env - Current environment info
 * @param {import('preact-cli').Helpers} helpers - Object with useful helpers for working with the webpack config
 */
export default (config, env, helpers) => {
  config.resolve.alias.src = join(process.cwd(), "src");
};
