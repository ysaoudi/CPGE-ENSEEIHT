const chalk = require("chalk")

const logWarning = function(str) {console.log(chalk.yellow.bold.inverse("WARNING:") + chalk.yellow.underline(" " + str))}
const logError = function(str) {console.log(chalk.red.bold.inverse("ERROR:") + chalk.red.bold.underline(" " +str))}
module.exports = {logWarning: logWarning, logError: logError}