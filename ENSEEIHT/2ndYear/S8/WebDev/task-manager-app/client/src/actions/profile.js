import axios from "axios"
import {setAlert} from "./alert"

import {
    GET_PROFILE,
    ERROR_PROFILE,
    CLEAR_PROFILE,
    DELETE_ACCOUNT
} from "./types"

export const getCurrentProfile = () => async dispatch => {
    try {
        const res = await axios.get("/users/me")
        try {
            const response = await axios.get("/users/me/avatar", {responseType:"arraybuffer"})
            const avatar = Buffer.from(response.data, 'binary').toString('base64')
            res.data.avatar = avatar
        } catch (e) {
            dispatch({
                type: GET_PROFILE,
                payload: res.data
            })
        }
        
        dispatch({
            type: GET_PROFILE,
            payload: res.data
        })
    } catch (error)  {
        const err = error.response.data.error
        if(err){
            dispatch({
                type: ERROR_PROFILE,
                payload: {msg: err, status: error.response.status}
            })
        }
    }
}

export const updateProfilePicture = (formData) => async dispatch => {
    let data = new FormData();
    data.append('avatar', formData.avatar, formData.avatar.fileName);

    axios.post("users/me/avatar", data, {
        headers: {
            'accept': 'application/json',
            'Accept-Language': 'en-US,en;q=0.8',
            'Content-Type': `multipart/form-data; boundary=${data._boundary}`,
        }
    })
    .then((response) => {
        dispatch(setAlert("Image Uploaded", "success"))
    }).catch((error) => {
        console.log(error)
    });
}

// Update profile
export const updateProfile = (formData, history) => async dispatch => {
    try {
        
        // if(formData.avatar){
        //     // console.log("avatar", formData.avatar)
        //     // // const avatarBuffer = formData.avatar.split(",")[1]
        //     // // console.log("avatar", avatarBuffer)
        //     // await axios.post("users/me/avatar", formData, {headers:{'content-type': 'multipart/form-data'}})
        // }
        var clone = Object.assign({}, formData);
        delete clone.avatar;
        delete clone.avatarURL
        //delete formData.avatar
        let config = {
            headers: {
                "Content-Type" : "application/json"
            }
        }
        const res = await axios.patch("/users/me", clone, config)

        dispatch({
            type: GET_PROFILE,
            payload: res.data
        })
        dispatch(setAlert("Profile Updated", "success"))

    } catch (error) {
        console.log(error)
        // dispatch(setAlert(error), "danger")
        // dispatch({
        //         type: ERROR_PROFILE,
        //         payload: {msg: error, status: error.response.status}
        //     })
    }
}

// Delete account
export const deleteAccount = () => async dispatch => {
    if(window.confirm("Are you sure? This operation cannot be undone.")){
        try {
            await axios.delete("/users/me")
            dispatch({type: CLEAR_PROFILE})
            dispatch({type: DELETE_ACCOUNT})
            dispatch(setAlert("Your account has been permanently deleted.", "danger"))
        } catch (error) {
            const err = error.response.data.error
            if(err){
                dispatch({
                    type: ERROR_PROFILE,
                    payload: {msg: err, status: error.response.status}
                })
            }
        }
    }
}