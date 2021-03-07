import {combineReducers} from "redux"
import alert from "./alert"
import auth from "./auth"
import profile from "./profile"
import task from "./task.js"
export default combineReducers({
    alert,
    auth,
    profile,
    task
})