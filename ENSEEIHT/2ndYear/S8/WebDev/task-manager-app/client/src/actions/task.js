import axios from "axios"
import {setAlert} from "./alert"
import {
    ADD_TASK,
    DELETE_TASK,
    EDIT_TASK,
    GET_TASKS,
    TASK_ERROR
} from "./types"

//Get tasks

export const getTasks = (query) => async dispatch => {
    try {
        //completed=false
        //sortBy=createdAt:desc
        //limit=3&skip=3
        const config = {
            params: {
                sortyBy: "createdAt:asc"
            }
        }
        if(query.sort)
            config.params.sortBy = `${query.sort}:${query.order? query.order : "asc"}`
        if (query.completed)
            config.params.completed = query.completed
        if(query.limit)
            config.params.limit = parseInt(query.limit)
        if(query.page) 
            config.params.skip = parseInt((query.limit? query.limit : 10)*(query.page - 1))
        

        const res = await axios.get("/tasks", config)
        dispatch({
            type: GET_TASKS,
            payload: res.data
        })
    } catch (error) {
        const err = error.response.data.error
        if(err){
            dispatch({
                type: TASK_ERROR,
                payload: {msg: err, status: error.response.status}
            })
        }
    }
}

// Delete task

export const deleteTask= (id) => async dispatch => {
    try {
        await axios.delete(`/tasks/${id}`)
        dispatch({
            type: DELETE_TASK,
            payload: id
        })

        dispatch(setAlert("Task Removed", "danger"))
    } catch (error) {
        const err = error.response.data.error
        if(err){
            dispatch({
                type: TASK_ERROR,
                payload: {msg: err, status: error.response.status}
            })
        }
    }
}

// Add task

export const addTask= (formData) => async dispatch => {
    const config = {
        headers: {
            "Content-Type" : "application/json"
        }
    }

    try {
        const res = await axios.post(`/tasks`, formData, config)
        dispatch({
            type: ADD_TASK,
            payload: res.data
        })

        dispatch(setAlert("Task Created", "success"))
    } catch (error) {
        const err = error.response.data.error
        if(err){
            dispatch({
                type: TASK_ERROR,
                payload: {msg: err, status: error.response.status}
            })
        }
    }
}

export const editTask = (formData, history) => async dispatch => {
    try {
        const config = {
            headers: {
                "Content-Type" : "application/json"
            }
        }
        const task = {
            completed : formData.completed,
            description: formData.description
        }
        const res = await axios.patch(`/tasks/${formData._id}`, task, config)
        dispatch({
            type: EDIT_TASK,
            payload: res.data
        })

        dispatch(setAlert("Task Updated", "success"))
    } catch (error) {
        const err = error.response.data.error
        if(err){
            dispatch({
                type: TASK_ERROR,
                payload: {msg: err, status: error.response.status}
            })
        }
    }
}