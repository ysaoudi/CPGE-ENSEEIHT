const request = require("supertest")
const app = require("../src/app")
const Task = require("../src/models/task")

const {
    userOneId,
    userOne,
    userTwoId,
    userTwo,
    taskOne,
    taskTwo,
    taskThree,
    setupDatabase
} = require("./fixtures/db")

beforeEach(setupDatabase)

test("Should Create Task for User", async() => {
    const response = await request(app).post("/tasks")
        .set("Authorization", `Bearer ${userOne.tokens[0].token}`)
        .send({
            description : "From Test Suite"
        })
        .expect(201)
    
    const task = await Task.findById(response.body._id)
    expect(task).not.toBeNull()
    expect(task.completed).toBe(false)
})

test("Should GET Tasks for User", async() => {
    const response = await request(app).get("/tasks")
        .set("Authorization", `Bearer ${userOne.tokens[0].token}`)
        .send({
            description : "From Test Suite"
        })
        .expect(200)
    
    expect(response.body.length).toBe(2)
})

test("Should Not Let User Delete Other User's Tasks", async() => {
    await request(app).delete(`/tasks/${taskOne._id}`)
        .set("Authorization", `Bearer ${userTwo.tokens[0].token}`)
        .send()
        .expect(404)
    
    const task = Task.findById(taskOne._id)
    expect(task).not.toBeNull() 
})