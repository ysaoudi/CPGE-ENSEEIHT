const mongoose = require("mongoose")
const validator = require("validator")
const bcrypt = require("bcryptjs")
const jwt = require("jsonwebtoken")
const Task = require("./task")

const userSchema = new mongoose.Schema( {
    name: {type: String, required : true, trim: true},
    email : {
        type: String,
        required: true,
        validate(value) {
            if(!validator.isEmail(value)) {
                throw new Error("Invalid e-mail!")
            }
        },
        trim : true,
        lowercase : true,
        unique : true
    },
    age: {
        type: Number,
        default: 0,
        validate(value) {
            if (value < 0) {
                throw new Error("Age must be positive!")
            }
        }
    },
    password : {
        type: String,
        required: true,
        trim: true,
        minLength : 6,
        validate(value) {
            if(value.toLowerCase().includes("password"))
                throw new Error("Erroneous password!")
        }
    },
    tokens : [{
        token : { type: String, required: true}
    }],
    avatar: {type: Buffer}
}, {
    timestamps: true
})

userSchema.virtual("tasks", {
    ref: "Task",
    localField : "_id",
    foreignField: "owner"
})

userSchema.statics.findByCredentials= async (email, password) => {
    const user = await User.findOne({email})
    
    if(!user) {
        throw new Error("Unable to login!")
    }

    const bIsMatch = await bcrypt.compare(password, user.password)
    if(!bIsMatch) {
        throw new Error("Unable to login")
    }
    return user
}

// Generating Authentification Token
userSchema.methods.generateAuthToken = async function () {
    const token = jwt.sign({_id: this._id.toString()}, "generatetoken")
    this.tokens = this.tokens.concat({token})
    await this.save()
    return token
}

// Hiding Private Data
userSchema.methods.toJSON = function() {
    const user = this.toObject()
    delete user.password
    delete user.tokens
    delete user.avatar
    return user
}

// Hash plain text password before saving
userSchema.pre("save", async function (next) {
    if(this.isModified("password")) {
        this.password = await bcrypt.hash(this.password, 8)
    }

    next()
})

//Delete user tasks when user is removed
userSchema.pre("remove", async function (next) {
    await Task.deleteMany({owner: this._id})
    next()
})

const User = mongoose.model("User", userSchema)

module.exports = User