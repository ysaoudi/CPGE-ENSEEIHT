import React from 'react'
import {Link, Redirect} from "react-router-dom"
import {connect} from "react-redux"
import PropTypes from 'prop-types'

const Landing = ({isAuthenticated}) => {
    if(isAuthenticated){
        return <Redirect to="/dashboard"/>
    }

    return (
        <section className="landing">
            <div className="dark-overlay">
                <div className="landing-inner">
                <h1 className="x-large">Task Manager</h1>
                <p className="lead">
                    Create and Manage Your Tasks Efficiently. Never Miss a Deadline.
                </p>
                <div className="buttons">
                    <Link to="register" className="btn btn-primary">Sign Up</Link>
                    <Link to="login" className="btn btn-light">Login</Link>
                </div>
                </div>
            </div>
            <footer>
                <h2 class="self-promotion">
                    <a href="https://bit.ly/ysprojects"> Portfolio - </a>
                    <a href="https://linkedin.com/in/ysaoudi"> LinkedIn - </a>
                    <a href="https://github.com/ysaoudi"> Github  </a>
                </h2>
                <p>Created by Younes Saoudi</p>
            </footer>
        </section>
    )
}

Landing.propTypes = {
    isAuthenticated: PropTypes.bool
}

const mapStateToProps = state => ({
    isAuthenticated: state.auth.isAuthenticated
})

export default connect(mapStateToProps)(Landing)