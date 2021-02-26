const path = require('path')
const express = require('express')
const hbs = require("hbs")
const geocode = require("./utils/geocode.js")
const forecast = require("./utils/forecast.js")

const app = express()
const port = process.env.PORT || 3000

// Define paths for Express config
const publicDirectoryPath = path.join(__dirname, '../public')
const viewsPath = path.join(__dirname, '../templates/views')
const partialsPath = path.join(__dirname, "../templates/partials")

// Setup handlebars engine and views location
app.set('view engine', 'hbs')
app.set('views', viewsPath)
hbs.registerPartials(partialsPath)

// Setup static directory to serve
app.use(express.static(publicDirectoryPath))

app.get('', (req, res) => {
    res.render('index', {
        title: 'Weather',
        name: 'Younes Saoudi'
    })
})

app.get('/about', (req, res) => {
    res.render('about', {
        title: 'About Me',
        name: 'Younes Saoudi'
    })
})

app.get('/help', (req, res) => {
    res.render('help', {
        title : "Help",
        helpText: "Enter location in main page's search form to get  that location's forecast.",
        name: "Younes Saoudi"
    })
})

app.get('/weather', (req, res) => {
    if(!req.query.address){
        return res.send({error : "Search location must be provided!"})
    }

    geocode(req.query.address, (error, {location, latitude, longitude} = {}) => {
        if(error)
            return res.send({error})
    
        forecast(latitude, longitude, (error, {weatherDescription, temperature, feelsLike, humidity} = {}) => {
            if(error)
            return res.send({error})

            res.send({
                location,
                weatherDescription,
                temperature,
                feelsLike,
                humidity
            })    
        })
    })
})


app.get("/help/*", (req, res) => {
    renderErrorPage(res, "Help Article Not Found!")
})

app.get("*", (req, res) => {
    renderErrorPage(res, "Page Not Found")
})

const renderErrorPage = (res, errorMsg) => {
    res.render("404", {
        title : "Error 404",
        errorMessage : errorMsg,
        name : "Younes Saoudi"
    })
}

app.listen(port, () => {
    console.log('Server is up on port' + port)
})