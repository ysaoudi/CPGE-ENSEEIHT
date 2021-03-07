import React from 'react'
import PropTypes from 'prop-types'

const ProfileTop = ({profile:{name, email, age, avatar}}) => {
    
    avatar = avatar? `data:image/png;base64,${avatar}` : "https://www.gravatar.com/avatar/d415f0e30c471dfdd9bc4f827329ef48?s=200&r=pg&d=mm";
    return (
        <div className="profile-top bg-primary p-2">
            <img
                className="round-img my-1"
                src={avatar}
                alt=""
            />
            <h1 className="large">{name}</h1>
            <p className="lead">{age} years old</p>
            <p><a href={`mailto:${email}`} className="secondary-link">{email}</a></p>
        </div>
    )
}

ProfileTop.propTypes = {
    profile : PropTypes.object.isRequired
}

export default ProfileTop
