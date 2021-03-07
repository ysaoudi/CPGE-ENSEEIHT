import { Fragment, useEffect} from 'react';
import './App.css';
import { BrowserRouter as Router, Route, Switch } from 'react-router-dom'
import Navbar from "./components/layout/Navbar"
import Landing from "./components/layout/Landing"
import Login from "./components/auth/Login"
import Register from "./components/auth/Register"
import Alert from "./components/layout/Alert"
import Dashboard from "./components/dashboard/Dashboard"
import UpdateProfile from "./components/profile-form/UpdateProfile"
import PrivateRoute from "./components/Routing/PrivateRoute"
import Profile from "./components/profile/Profile"
import Tasks from "./components/tasks/Tasks"

//Redux
import { Provider } from "react-redux"
import store from "./store"
import {loadUser} from "./actions/auth"
import setAuthToken from "./utils/utils"

if( localStorage.token) {
    setAuthToken(localStorage.token)
}

const App = () => {
    useEffect( () => {
        store.dispatch(loadUser())
    }, [])

    return (<Provider store={store}>
                <Router>
                    <Fragment>
                        <Navbar />
                        <Route exact path="/" component={Landing} />
                        <section className="container">
                            <Alert/>
                            <Switch>
                                <Route exact path="/register" component={Register}/>
                                <Route exact path="/login" component={Login}/>
                                <PrivateRoute exact path="/dashboard" component={Dashboard}/>
                                <PrivateRoute exact path="/profile" component={Profile}/>
                                <PrivateRoute exact path="/update-profile" component={UpdateProfile}/>
                                <PrivateRoute exact path="/tasks" component={Tasks}/>
                            </Switch>
                        </section>
                    </Fragment>
                </Router>
            </Provider>)
};

export default App;
