import React from 'react'
import {connect} from "react-redux"
import PropTypes from 'prop-types'

const Alert = ({alerts}) => 
    alerts !== null 
    && alerts.length > 0 
    && (
            <div key={alerts[alerts.length - 1].id} className={`alert alert-${alerts[alerts.length - 1].alertType}`}>
                {alerts[alerts.length - 1].msg}
            </div>
        )

Alert.propTypes = {
    alerts: PropTypes.array.isRequired,
}

const mapStateToProps = state => ({
    alerts: state.alert
})

export default connect(mapStateToProps)(Alert)
