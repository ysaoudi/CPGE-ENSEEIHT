const express = require("express")
const path = require("path")
const http = require("http")
const socketio = require("socket.io")

const port = process.env.PORT
const app = express()
const server = http.createServer(app)
const io = socketio(server)

const publicDirectoryPath = path.join(__dirname, "../public")

app.use(express.static(publicDirectoryPath))

io.on("connection", (socket) => {
    console.log("New WebSocket connection")
    
    socket.emit("message", "Welcome")
    
    socket.broadcast.emit("message", "A new user has joined the chat.") //every client but this one receives the message
    
    socket.on("sendMessage", (msg) => {
        io.emit("message", msg)
    })
    socket.on("disconnect", () => {
        io.emit("message", "A user has left the chat.")
    })
})

server.listen(port, () => {
    console.log("Server is UP on port:", port)
})