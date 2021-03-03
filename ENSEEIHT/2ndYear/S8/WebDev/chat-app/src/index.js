const express = require("express")
const path = require("path")
const http = require("http")
const socketio = require("socket.io")
const Filter = require("bad-words")
const {generateMessage, generateLocationMessage} = require("./utils/messages")
const {
    addUser,
    getUser,
    removeUser,
    getUsersInRoom
} = require("./utils/users")

const port = process.env.PORT
const app = express()
const server = http.createServer(app)
const io = socketio(server)

const publicDirectoryPath = path.join(__dirname, "../public")

app.use(express.static(publicDirectoryPath))

io.on("connection", (socket) => {
    console.log("New WebSocket connection")
     
    socket.on("join", (options, callback) => {
        const {error, user} = addUser({id: socket.id, ...options})

        if(error)
            return callback(error)

        socket.join(user.room)
        socket.emit("message", generateMessage(`Room - ${user.room}`, "Welcome"))
        socket.broadcast.to(user.room).emit("message", generateMessage(`Room - ${user.room}`, `${user.username} has joined the room.`)) //every client in the room but this one receives the message
        io.to(user.room).emit("roomData", {
            room: user.room,
            users: getUsersInRoom(user.room)
        })
        callback()
    })

    socket.on("sendMessage", (msg, callback) => {
        const user = getUser(socket.id)
        if(!user)
            return callback("User doesn't exit.")
        
            const filter = new Filter()

        if(filter.isProfane(msg))
            msg = filter.clean(msg)

        io.to(user.room).emit("message", generateMessage(user.username, msg))
        callback()
    })

    socket.on("sendLocation", ({latitude, longitude}, callback) => {
        const user = getUser(socket.id)
        if(!user)
            return callback("User doesn't exit.")

        io.to(user.room).emit("locationMessage", generateLocationMessage(user.username, latitude, longitude))
        callback()
    })
    
    socket.on("disconnect", () => {
        const user = removeUser(socket.id)
        if(user) {
            io.to(user.room).emit("message", generateMessage(`Room - ${user.room}`, `${user.username} has left the room.`))
            io.to(user.room).emit("roomData", {
                room: user.room,
                users: getUsersInRoom(user.room)
            })
        }
    })
})

server.listen(port, () => {
    console.log("Server is UP on port:", port)
})