import React, {Fragment, useEffect, useState} from 'react'
import PropTypes from 'prop-types'
import {connect} from "react-redux"
import Spinner from "../layout/Spinner"
import TaskItem from "./TaskItem"
import {getTasks} from "../../actions/task"
import TaskForm from './TaskForm'

const Tasks = ({getTasks, task: {tasks, loading}}) => {
    const [formData, setFormData] = useState({
        sort : null,
        order: "asc",
        limit: 10,
        page: 1,
        completed: null
    })
    const {sort, order, limit, page, completed} = formData

    useEffect(()=> {
        getTasks(formData)
    }, [formData, getTasks])
    
    //completed=false
    //sortBy=createdAt:desc
    //limit=3&skip=3

    const onChange = (e) => {
        if(e.target.name === "sort" && e.target.value === "")
            setFormData({...formData, order:"asc"})
        setFormData({...formData, [e.target.name] : e.target.value})
    }


    return (
        loading? <Spinner/> : (
            <Fragment>
                <h1 className="large text-primary">Tasks</h1>
                <TaskForm/>
                <div className="sortbar form-group my-txt-right">
                        <form>
                            <small className="text-white" for="page">Page: </small>
                            <input className="form-group my-txt-left" type="number" id="page" placeholder="1" name="page" min="1" max="100" value={page} onChange={onChange}/>
                        </form>
                        
                        <form>
                        <small className="text-white" for="limit">Limit per page: </small>
                            <select className="form-group my-txt-left" name="limit" value={limit} onChange={onChange}>
                                <option value="" disabled>Limit per page</option>
                                <option value="5">5</option>
                                <option value="10">10</option>
                                <option value="15">15</option>
                                <option value="25">25</option>
                                <option value="50">50</option>
                            </select>
                        </form>
                        <form>
                            <small className="text-white" for="completed">Filter: </small>
                            <select name="completed" value={completed} onChange={onChange}>
                                <option value="" selected> No filters selected</option>
                                <option value="false">Not Completed</option>
                                <option value="true">Completed</option>
                            </select>
                        </form>
                        <form>
                            <small className="text-white" for="sort">Sorting: </small>
                            <select name="sort" value={sort} onChange={onChange}>
                                <option value="" selected> No sorting selected</option>
                                <option value="createdAt">Creation Date</option>
                                <option value="description">Description</option>
                            </select>
                        </form>
                        <form>
                            <small className="text-white" for="order">Order: </small>
                            <select name="order" value={order} onChange={onChange}>
                                <option value="" disabled>Sorting order</option>
                                <option value="asc">Asc</option>
                                <option value="desc">Desc</option>
                            </select>      
                        </form>
                        
                </div>
                <div className="tasks">
                    {tasks.map((task) => (
                        <TaskItem key={task._id} task={task}/>
                    ))}
                </div>
            </Fragment>
        )
    )
}

Tasks.propTypes = {
    getTasks: PropTypes.func.isRequired,
    task: PropTypes.object.isRequired
}

const mapStateToProps = state => ({
    task: state.task
})

export default connect(mapStateToProps, {getTasks})(Tasks)
