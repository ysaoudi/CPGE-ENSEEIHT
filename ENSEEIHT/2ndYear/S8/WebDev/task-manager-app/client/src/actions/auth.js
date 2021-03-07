import axios from "axios"
import {setAlert} from "./alert"
import {
    REGISTER_SUCCESS,
    REGISTER_FAIL,
    USER_LOADED,
    AUTH_ERROR,
    LOGIN_SUCESS,
    LOGIN_FAIL,
    LOGOUT,
    CLEAR_PROFILE
} from "./types"
import setAuthToken from "../utils/utils"
// Load user
export const loadUser = () => async dispatch => {
    if(localStorage.token) {
        setAuthToken(localStorage.token)
    }
    try {
        const res = await axios.get("/users/me")

        dispatch({
            type: USER_LOADED,
            payload: res.data
        })
    } catch (err) {
        console.log(err)
        dispatch({
            type: AUTH_ERROR
        })
    }
}


// Register user

export const register = ({name, email, password}) => async dispatch => {
    const config = {
        headers: {
            "Content-Type": "application/json"
        }
    }

    const body = JSON.stringify({name, email, password})

    try {
        const res = await axios.post("/users", body, config)
        dispatch({
            type: REGISTER_SUCCESS,
            payload: res.data
        })
        dispatch(loadUser())
    } catch (error){
        const err = error.response.data.error
        if(err){
            dispatch(setAlert(err), "danger")
        }
        dispatch({
            type: REGISTER_FAIL
        }, "danger")
    }
}

// Log in user

export const login = (email, password) => async dispatch => {
    const config = {
        headers: {
            "Content-Type": "application/json"
        }
    }

    const body = JSON.stringify({email, password})

    try {
        const res = await axios.post("/users/login", body, config)
        dispatch({
            type: LOGIN_SUCESS,
            payload: res.data
        })

        dispatch(loadUser())

    } catch (error){
        const err = error.response.data.error
        if(err){
            dispatch(setAlert(err), "danger")
        }
        dispatch({
            type: LOGIN_FAIL
        }, "danger")
    }
}

// logout

export const logout = () => dispatch => {
    dispatch({type:CLEAR_PROFILE})
    dispatch({type:LOGOUT})
}