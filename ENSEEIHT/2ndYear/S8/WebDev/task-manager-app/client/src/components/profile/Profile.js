import React, {Fragment, useEffect} from 'react'
import PropTypes from 'prop-types'
import {connect} from "react-redux"
import Spinner from "../layout/Spinner"
import {getCurrentProfile} from "../../actions/profile"
import { Link } from 'react-router-dom'
import ProfileTop from "./ProfileTop"

const Profile = ({getCurrentProfile, profile: {profile, loading}, auth}) => {
    useEffect(()=> {getCurrentProfile()}, [getCurrentProfile])

    return (
        <Fragment>
            {(!profile || loading) ?
                <Spinner/> 
                : 
                <Fragment>
                    <Link to="/dashboard" className="btn btn-light">Back To Dashboard</Link>
                    {auth.isAuthenticated && !auth.loading && (<Link to="/update-profile" className="btn btn-dark">Edit Profile</Link>)}
                    <div className="profile-grid my-1">
                        <ProfileTop profile={profile}/>
                    </div>
                </Fragment>
            }
        </Fragment>
    )
}

Profile.propTypes = {
    getCurrentProfile: PropTypes.func.isRequired,
    profile: PropTypes.object.isRequired,
    auth: PropTypes.object.isRequired
}

const mapStateToProps = state => ({
    profile: state.profile,
    auth: state.auth
})

export default connect(mapStateToProps, {getCurrentProfile})(Profile)
