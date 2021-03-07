import React from 'react'
import {Link} from "react-router-dom"

const DashboardActions = () => {
    return (
        <div className="dash-buttons">
            <Link to="/profile" className="btn btn-light">
                <i className="fas fa-user text-primary"></i> View Profile
            </Link>
            <Link to="/update-profile" className="btn btn-light">
                <i className="fas fa-user-edit text-primary"></i> Edit Profile
            </Link>
            <Link to="/tasks" className="btn btn-light">
                <i className="fas fa-tasks text-primary"></i> Add Task
            </Link>
      </div>
    )
}

export default DashboardActions
