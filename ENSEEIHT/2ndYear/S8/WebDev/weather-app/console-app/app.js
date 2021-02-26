const {logWarning, logError} =  require("./utils/utils.js")
const geocode = require("./utils/geocode.js")
const forecast = require("./utils/forecast.js")

const location = process.argv[2]
if(!location) return logError("Please provide a valid location")

geocode(location, (error, {location, latitude, longitude} = {}) => {
    if(error)
        return logError(error)

    forecast(latitude, longitude, (error, {weatherDescription, temperature}) => {
        if(error)
            return logError(error)
        console.log(location + ".")
        console.log(weatherDescription + ". It is currently " + temperature + " degrees out.")
    })
})