import React, { Component } from 'react';
import './App.css';
import Home from './Home';
import About from './About';
import { BrowserRouter as Router, Route, Switch } from 'react-router-dom';
import RepoDetails from './RepoDetails';

class App extends Component {
  render() {
    return (
        <Router forceRefresh={true}>
          <Switch>
            <Route path='/' exact={true} component={Home}/>
            <Route path='/about' exact={true} component={About}/>
            <Route path='/projects' exact={true} component={RepoDetails}/>
            <Route path="*" component={Home} />
          </Switch>
        </Router>
    )
  }
}

export default App;