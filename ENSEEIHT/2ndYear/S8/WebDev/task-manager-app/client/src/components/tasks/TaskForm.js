import React, {useState} from 'react'
import PropTypes from 'prop-types'
import {connect} from "react-redux"
import { addTask} from "../../actions/task"

const TaskForm = ({addTask}) => {
    const [formData, setFormData] = useState({
        description: "",
        completed: false
    })
    const {description, completed} = formData
    const onChange = e => setFormData({...formData, completed: e.target.checked})
    const onSetDescription = (e) => {
        setFormData({...formData, description: e.target.value})
    }
    return (
        <div className="post-form">
        <div className="bg-primary p">
          <h3>Create a Task</h3>
        </div>
        <form className="form my-1" onSubmit={e => {
            e.preventDefault()
            addTask({description, completed})
        }}>
          <textarea
            name="text"
            cols="30"
            rows="5"
            placeholder="Description..."
            onChange={e => onSetDescription(e)}
            required
          ></textarea>
          Done <input type="checkbox" className="btn btn-primary my-chckbx" id="completed" name="completed" value={completed} onClick={e => onChange(e)}/>
          <input type="submit" className="btn btn-dark my-1" value="Submit" />
        </form>
      </div>
    )
}

TaskForm.propTypes = {
    addTask: PropTypes.func.isRequired
}

export default connect(null, {addTask})(TaskForm)
