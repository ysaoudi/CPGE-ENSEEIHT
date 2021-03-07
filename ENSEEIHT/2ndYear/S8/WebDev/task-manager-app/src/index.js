const express = require("express")
const path = require("path")
const app = require("./app")
const port = process.env.PORT

if(process.env.NODE_ENV === "production"){
    app.use(express.static(path.join(__dirname, '../client/build')))

    app.get('*', function(_, res) {
        res.sendFile(path.join(__dirname, '../client/build/index.html'), function(err) {
            if (err) {
            res.status(500).send(err)
            }
        })
    })
}

app.listen(port, () => {
    console.log("Server is UP on port:", port)
})