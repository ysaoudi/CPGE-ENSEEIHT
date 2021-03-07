const Task = require("../models/task")
const express = require("express")
const auth = require("../middleware/auth")
const router = express.Router()


// -------- TASKS --------

router.post("/tasks", auth, async (req, res) => {
    const task = new Task({...req.body, owner: req.user._id})

    try {
        await task.save()
        res.status(201).send(task)
    } catch (error) {
        res.status(400).send(error)
    }
})

router.get("/tasks", auth, async (req, res) => {
    const match = {}
    const sort = {}
    
    if(req.query.completed) match.completed = req.query.completed === 'true'
    
    if(req.query.sortBy){
        const parts = req.query.sortBy.split(":")
        sort[parts[0]] = parts[1] === "desc" ? -1 : 1
    }

    try{
        //const tasks = await Task.find({owner: req.user._id})
        await req.user.populate({
            path:"tasks",
            match,
            options:{ // PAGINATION & SORTING
                limit: parseInt(req.query.limit), //automatically ignored by mongoose if it isn't set in url
                skip: parseInt(req.query.skip),  //automatically ignored by mongoose if it isn't set in url
                sort
            }
        }).execPopulate()
        res.send(req.user.tasks)
    } catch(error) {
        res.status(500).send(error)
    }
})

router.get("/tasks/:id", auth, async (req, res) => {
    const _id = req.params.id

    try{
        const task = await Task.findOne({_id, owner: req.user._id})

        if(!task) 
            return res.status(404).send()

        res.send(task)
    } catch(error) {
        res.status(500).send(error)
    }

})

router.patch("/tasks/:id", auth, async (req, res) => {
    const updates = Object.keys(req.body)

    const allowedUpdates = ["description", "completed"]
    
    const bIsOperationValid = updates.every((update) => {
        return allowedUpdates.includes(update)
    })
    
    if(!bIsOperationValid)
        return res.status(400).send({error: "Invalid Update Request"})
    
        
    try{
        const task = await Task.findOne({_id: req.params.id, owner:req.user._id})
        
        if(!task)
            return res.status(404).send({error: "Task not found"})
        
        updates.forEach( (update) => task[update] = req.body[update]) 
        await task.save()
        res.send(task)
    } catch(error) {
        res.status(400).send(error)
    }
})

router.delete("/tasks/:id", auth, async (req, res) => {
    const _id = req.params.id

    try{
        const task = await Task.findOneAndDelete({_id, owner: req.user._id})

        if(!task)
            return res.status(404).send()

        res.send(task)
    } catch(error) {
        res.status(500).send(error)
    }
})

module.exports = router