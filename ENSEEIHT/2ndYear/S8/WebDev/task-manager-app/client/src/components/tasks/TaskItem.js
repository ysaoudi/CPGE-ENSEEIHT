import React, { useState, useEffect} from 'react'
import PropTypes from 'prop-types'
import {withRouter} from "react-router-dom"
import Moment from "react-moment"
import { connect } from "react-redux"
import {deleteTask, editTask} from "../../actions/task"

const TaskItem = ({ deleteTask, editTask, auth, task: { _id, description, completed, createdAt, updatedAt, owner, history } }) => {

    const [formData, setFormData] = useState({
        _id,
        description,
        completed,
        bHasBeenUpdated: false
    })

    useEffect(()=> {
        if(formData.bHasBeenUpdated)
            editTask(formData)
    }, [formData, editTask])

    const onChange = (e) => {
        e.target.readOnly = true
        setFormData({...formData, description: e.target.value, bHasBeenUpdated: true})
        //editTask({_id, description: e.target.value, completed}, history)

    }
    const onChangeCheckbox = () => {
        //console.log("Former value:", formData.completed)
        setFormData({...formData, completed: !completed, bHasBeenUpdated: true})
        //editTask({_id, completed: !completed, description}, history)
        //console.log("New value:", formData.completed)
    }
    return (
        //<Link to={`/task/${_id}`}>
        <div className="post bg-white p-1 my-1">
            <div>
                <button className="btn btn-primary">
                    Done <input 
                            type="checkbox" 
                            id="completed2" 
                            name="completed" 
                            value={formData.completed} 
                            defaultChecked={formData.completed} 
                            onClick={onChangeCheckbox}
                        />
                </button>
                {!auth.loading && owner === auth.user._id 
                    && (<button className="btn btn-danger my-btn" onClick={() => deleteTask(_id)}>
                            <i className="fas fa-times"></i>
                        </button>)
                }
            </div>
            <div>
                <textarea className="my-5"
                cols="70"
                rows="7"
                readOnly
                placeholder="Description..."
                onClick={(e)=>{e.target.readOnly = false}}
                onKeyPress={(e) => {
                    if(e.charCode === 13 && e.target.value !== formData.description)
                        onChange(e)
                }}
                onBlur={(e) => {
                    if(e.target.value !== formData.description)
                        onChange(e)
                }}
                >
                    {description}
                </textarea>
                
                <p className="post-date my-txt-right">
                    Created on <Moment format="DD/MM/YYYY">{createdAt}</Moment>
                </p>
                
            </div>
        </div>
        //</Link>
    )
}

TaskItem.propTypes = {
    task: PropTypes.object.isRequired,
    auth: PropTypes.object.isRequired,
    deleteTask: PropTypes.func.isRequired
}

const mapStateToProps = state => ({
    auth: state.auth
})

export default connect(mapStateToProps, {deleteTask, editTask})(withRouter(TaskItem))
