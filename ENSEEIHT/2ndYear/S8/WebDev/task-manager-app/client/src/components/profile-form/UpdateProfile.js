import React, {Fragment, useState, useEffect} from 'react'
import {Link, withRouter} from "react-router-dom"
import PropTypes from 'prop-types'
import {connect} from "react-redux"
import {updateProfile, getCurrentProfile, updateProfilePicture} from '../../actions/profile'
// import axios from "axios"

const UpdateProfile = ({profile:{profile, loading}, updateProfile, getCurrentProfile, updateProfilePicture, history}) => {
    let blankPicture = "https://www.gravatar.com/avatar/d415f0e30c471dfdd9bc4f827329ef48?s=200&r=pg&d=mm"
    const [formData, setFormData] = useState({
        name:"",
        age: "",
        email: "",
        password:"",
        avatar: ""
    })

    useEffect( ()=> {
        if(profile) {
            getCurrentProfile()
            setFormData({
                name: loading || !profile.name? "" : profile.name,
                age: loading || !profile.age? "" : profile.age,
                email: loading || !profile.email? "" : profile.email,
                avatar: null,
                avatarURL: loading || !profile.avatar? blankPicture : `data:image/png;base64,${profile.avatar}`,
                password: ""
            })
        }
    }, [loading, getCurrentProfile])

    const {name, age, email, password, avatar, avatarURL} = formData

    const onChange = e => {
        if(e)
            setFormData({...formData, [e.target.name]: e.target.value})
    }
    const onSetDate = e => setFormData({...formData, age:  Math.abs(Math.round(((new Date().getTime() - new Date(e.target.value).getTime())/(1000*60*60*24*365.25)))) })

    const onSubmit = async e => {
        e.preventDefault()
        if(!password)
            delete formData.password
        if(!age)
            delete formData.age
        if(!avatar)
            delete formData.avatar

        await updateProfilePicture(formData)
        await updateProfile(formData, history)
        console.log("formData again", formData.avatar)
    }
    
    const handleChange = (event) => {
        setFormData({...formData, avatar : event.target.files[0], avatarURL:URL.createObjectURL(event.target.files[0])})
    }

    return (
        <Fragment>
            <h1 className="large text-primary">
                Update Your Profile
            </h1>
            <small>* = required field</small>
            <form className="form" onSubmit= {e => onSubmit(e)}>
                <div className="form-group">
                    <ul>
                        <li><h4>Upload a profile picture</h4></li>
                        <li><img
                                className="round-img my-4"
                                src={avatarURL || blankPicture}
                                alt=""
                            />
                        </li>
                        <li><input type="file" id="img" name="img" accept="image/png" onChange={handleChange}/></li>
                    </ul>
                </div>
                <div className="form-group">
                    <h4>Name</h4>
                    <input type="text" placeholder={name} name="name" value={name} readOnly/>
                </div>
                <div className="form-group">
                    <h4>Email</h4>
                    <input type="text" placeholder={email} name="email" value={email} onChange={e=>onChange(e)}/>
                    <small className="form-text">Enter your email</small>
                </div>
                <div className="form-group">
                    <h4>Password</h4>
                    <input type="password" autoComplete="new-password" name="password" value={password} onChange={e=>onChange(e)}/>
                    <small className="form-text">Enter your new password</small>
                </div>
                <div className="form-group">
                    <h4>* Date of Birth</h4>
                    <input type="date" name="from"  onChange={e=>onSetDate(e)}/>
                    <small className="form-text">Enter your age</small>
                </div>
                <div className="form-group">
                    <h4>Age</h4>
                    <input type="text" placeholder={age} name="age" value={age} readOnly/>
                </div>
                
                <input type="submit" className="btn btn-primary my-1" />
                <Link className="btn btn-light my-1" to="/dashboard">Go Back</Link>
            </form>
        </Fragment>
    )
}

UpdateProfile.propTypes = {
    updateProfile: PropTypes.func.isRequired,
    getCurrentProfile: PropTypes.func.isRequired,
    updateProfilePicture: PropTypes.func.isRequired,
    profile: PropTypes.object.isRequired
}

const mapStatesToProps = state => ({
    profile: state.profile
})

export default connect(mapStatesToProps, {updateProfile, getCurrentProfile, updateProfilePicture})(withRouter(UpdateProfile))
