const mongoose = require("mongoose")
const validator = require("validator")

const User = mongoose.model("User", {
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
        lowercase : true
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
    }
})

module.exports = User