const request = require("supertest")
const app = require("../src/app")
const User = require("../src/models/user")

const {userOneId, userOne, setupDatabase} = require("./fixtures/db")

beforeEach(setupDatabase)

test("Should Sign Up a New User", async () => {
    const response = await request(app).post("/users").send({
        name : "Hamid",
        email: "hamida@saoudi.com",
        password: "mypasS123!"
    }).expect(201)

    const user = await User.findById(response.body.user._id)

    expect(user).not.toBeNull()
    expect(response.body).toMatchObject({
        user: {
            name : "Hamid",
            email: "hamida@saoudi.com"
        },
        token: user.tokens[0].token
    })

    expect(user.password).not.toBe("mypasS123!")
})

test("Should Log In Existing User", async () => {
    const response = await request(app).post("/users/login").send({
        email: userOne.email,
        password: userOne.password
    }).expect(200)

    const user = await User.findById(userOneId)

    expect(user).not.toBeNull()
    expect(response.body.token).toBe(user.tokens[1].token)
})

test("Should Not Log In Nonexistent Users", async () => {
    await request(app).post("/users/login").send({
        email: "baduser@task.app.com",
        password: "failtest123!"
    }).expect(400)

    await request(app).post("/users/login").send({
        email: userOne.email,
        password: "failtest123!"
    }).expect(400)
    
    await request(app).post("/users/login").send({
        email: "baduser@task.app.com",
        password: userOne.password
    }).expect(400)
})

test("Should GET Exisiting User's Profile", async () => {
    await request(app).get("/users/me")
        .set("Authorization", `Bearer ${userOne.tokens[0].token}`)
        .send()
        .expect(200)
})

test("Should Not GET Profile for Unauthenticated User", async () => {
    await request(app).get("/users/me")
        .send()
        .expect(401)
})

test("Should DELETE Exisiting User's Profile", async () => {
    await request(app).delete("/users/me")
        .set("Authorization", `Bearer ${userOne.tokens[0].token}`)
        .send()
        .expect(200)

    const user = await User.findById(userOneId)

    expect(user).toBeNull()
})

test("Should Not DELETE Profile for Unauthenticated User", async () => {
    await request(app).delete("/users/me")
        .send()
        .expect(401)
    
    const user = await User.findById(userOneId)
    expect(user).not.toBeNull()
})

test("Should Upload Avatar Picture", async () => {
    await request(app).post("/users/me/avatar")
        .set("Authorization", `Bearer ${userOne.tokens[0].token}`)
        .attach("avatar", "tests/fixtures/profile-pic.png")
        .expect(200)
    
    const user = await User.findById(userOneId)
    expect(user.avatar).toEqual(expect.any(Buffer))
})

test("Should Update Valid User Fields", async () => {
    await request(app).patch("/users/me")
        .set("Authorization", `Bearer ${userOne.tokens[0].token}`)
        .send({name : "TestUser1Modified"})
        .expect(200)
    
    const user = await User.findById(userOneId)
    expect(user.name).toBe("TestUser1Modified")
})

test("Should Not Update Invalid User Fields", async () => {
    await request(app).patch("/users/me")
        .set("Authorization", `Bearer ${userOne.tokens[0].token}`)
        .send({invalidField : "AnyValue"})
        .expect(400)
    
    const user = await User.findById(userOneId)
    expect(user.invalidField).toBeUndefined()
})

test("Should Not Update Unauthenticated User's User Fields", async () => {
    await request(app).patch("/users/me")
        .send({name : "TestUser1Modified"})
        .expect(401)
    
    const user = await User.findById(userOneId)
    expect(user.name).not.toBe("TestUser1Modified")
})