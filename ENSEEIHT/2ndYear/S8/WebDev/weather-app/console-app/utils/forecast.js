const request = require("request")

const forecast = (latitude, longitude, callback) => {
    const url = "http://api.weatherstack.com/current?access_key=59ad6ede8444204135032b3ed0506c53&query=" + latitude + "," + longitude
    
    request({ url, json : true }, (error, {body}) => {
        if(error) { 
            callback("Unable to connect to weather service.", undefined)
        } else if (body.error) {
            callback(body.error.info, undefined)
        } else {
            callback(undefined, {
                location : body.location.name,
                country : body.location.country,
                weatherDescription : body.current.weather_descriptions[0],
                temperature : body.current.temperature,
                feelsLike : body.current.feelslike,
                unit : body.request.unit
            })
        }
    })
}

module.exports = forecast