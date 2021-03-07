import {
    ADD_TASK,
    DELETE_TASK,
    EDIT_TASK,
    GET_TASKS,
    TASK_ERROR
} from "../actions/types"

const initialState = {
    tasks : [],
    task : null,
    loading: true,
    error: {}
}

function taskReducer(state = initialState, action) {
    const {type, payload} = action
    switch(type){
        case GET_TASKS:
            return {
                ...state,
                tasks: payload,
                loading: false
            }
        case ADD_TASK:
            return {
                ...state,
                tasks: [...state.tasks, payload],
                loading: false
            }
        case EDIT_TASK:
            state.tasks.forEach(task => {
                if(task._id === payload._id)
                    task = payload
            })
            return {
                ...state,
                tasks: state.tasks,
                loading: false
            }
        case DELETE_TASK:
            return {
                ...state,
                tasks: state.tasks.filter(task => task._id !== payload),
                loading: false
            }
        case TASK_ERROR:
            return {
                ...state,
                error:payload,
                loading: false
            }
        default:
            return state
    }
}

export default taskReducer