const express = require("express")
const router = express.Router()
const auth = require("../middleware/auth")
const User = require("../models/user")
const multer = require("multer")
const sharp = require("sharp")
const {sendWelcomeEmail, sendCancelationEmail} = require("../emails/account")

// -------- USERS --------

router.post("/users", async (req, res) => {
    const user = new User(req.body)

    try {
        await user.save()
        sendWelcomeEmail(user.email, user.name)
        const token = await user.generateAuthToken()
        res.status(201).send({user, token})
    } catch (error) {
        res.status(400).send({error: "User already exists!"})
    }
})

router.post("/users/login", async (req, res) => {
    try {
        const user = await User.findByCredentials(req.body.email, req.body.password)
        const token = await user.generateAuthToken()
        res.send({user, token})
    } catch (error) {
        res.status(400).send({error: "Invalid credentials"})
    }
})

router.post("/users/logout", auth, async (req, res) => {
    try {
        req.user.tokens = req.user.tokens.filter((token) => token.token !== req.token)
        await req.user.save()
        res.send()
    } catch(error) {
        res.status(500).send({error: "Error 500"})
    }
})

router.post("/users/logoutAll", auth, async (req, res) => {
    try {
        req.user.tokens = []
        await req.user.save()
        res.send()
    } catch(error) {
        res.status(500).send({error: "Error 500"})
    }
})

router.get("/users/me", auth, async (req, res) => {
    try {
        res.send(req.user)
    } catch (error){
        res.status(401).send({error: "Error 401"})
    }
})

router.patch("/users/me", auth, async (req, res) => {
    const updates = Object.keys(req.body)
    const allowedUpdates = ["name", "email", "password", "age"]
    const bIsOperationValid = updates.every((update) => allowedUpdates.includes(update))

    if(!bIsOperationValid) return res.status(400).send({error: "Invalid Update Request"})

    try{
        updates.forEach( (update) => req.user[update] = req.body[update]) 
        await req.user.save()
        res.send(req.user)
    } catch(error) {
        res.status(400).send({error: "Error 400"})
    }
})

router.delete("/users/me", auth, async (req, res) => {
    try{
        await req.user.remove()
        sendCancelationEmail(req.user.email, req.user.name)
        res.send(req.user)
    } catch(error) {
        res.status(500).send({error: "Error 500"})
    }
})

const upload = multer({
    limits: {
        fileSize: 1000000 // 1Mb
    },
    fileFilter(req, file, cb) {
        if(!file.originalname.match(/\.(png|jpg|jpeg)$/))
            return cb(new Error("Please upload an image!"))
        cb(undefined, true)
    }
})


router.post("/users/me/avatar", auth, upload.single("avatar"), async (req, res) => {
    //console.log("file", req.file)
    const buffer = await sharp(req.file.buffer).resize({width: 250, height: 250}).png().toBuffer()

    req.user.avatar = buffer
    await req.user.save()
    res.send()
}, (error, req, res, next) => {
    console.log(error)
    res.status(400).send({error: "Error 400"})
})


router.delete("/users/me/avatar", auth, async (req, res) => {
    req.user.avatar = undefined
    await req.user.save()
    res.send()
})

router.get("/users/me/avatar", auth, async (req, res) => {
    try {
        if(!req.user.avatar){
            throw new Error()
        }

        res.set("Content-Type", "image/png")
        res.send(req.user.avatar)
    } catch (error) {
        res.status(404).send({error: "Error 404"})
    }
})

router.get("/users/:id/avatar", async (req, res) => {
    try {
        const user = await User.findById(req.params.id)
        if(!user || !user.avatar){
            throw new Error()
        }

        res.set("Content-Type", "image/png")
        res.send(user.avatar)
    } catch (error) {
        res.status(404).send({error: "Error 404"})
    }
})

module.exports = router