import React, {Fragment, useEffect} from 'react'
import PropTypes from 'prop-types'
import {connect} from "react-redux"
import {deleteAccount, getCurrentProfile} from "../../actions/profile"
import Spinner from "../layout/Spinner"
import DashboardActions from "./DashboardActions"

const Dashboard = ({getCurrentProfile, deleteAccount, auth: {user}, profile: {profile, loading}}) => {
    useEffect(()=> {getCurrentProfile()}, [getCurrentProfile])
    return loading && !profile ?
        <Spinner/> 
        :
        <Fragment>
            <h1 className="large text-primary">Dashboard</h1>
            <p className="lead">
                <i className="fas fa-user"></i> Welcome {user && user.name}
            </p>
            <Fragment>
                    <DashboardActions/>
                    <div className="my-2">
                        <button className="btn btn-danger" onClick={() => deleteAccount()}>
                            <i className="fas fa-user-minus"></i> Delete Account
                        </button>
                    </div>
            </Fragment>
        </Fragment>
}

Dashboard.propTypes = {
    getCurrentProfile: PropTypes.func.isRequired,
    deleteAccount: PropTypes.func.isRequired,
    auth: PropTypes.object.isRequired,
    profile: PropTypes.object.isRequired
}

const mapStateToProps = state => ({
    auth: state.auth,
    profile: state.profile
})

export default connect(mapStateToProps, {getCurrentProfile, deleteAccount})(Dashboard)
