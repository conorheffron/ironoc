import React, { Component } from 'react';
import { BrowserRouter as Router, Route, Switch } from 'react-router-dom';
import './App.css';
import Home from './components/Home';
import NotFound from './components/NotFound';
import About from './components/About';
import RepoDetails from './components/RepoDetails';
import RepoIssues from './components/RepoIssues';
import ControlledCarousel from './components/ControlledCarousel';

class App extends Component {
  render() {
    return (
        <Router forceRefresh={true}>
          <Switch>
            <Route path='/' exact={true} component={Home}/>
            <Route path='/about' exact={true} component={About}/>
            <Route path='/portfolio' exact={true} component={ControlledCarousel}/>
            <Route path='/projects' exact={true} component={RepoDetails}/>
            <Route path='/projects/:id' component={RepoDetails}/>
            <Route path='/issues/:id/:repo' component={RepoIssues}/>
            <Route path="*" component={NotFound} />
          </Switch>
        </Router>
    )
  }
}

export default App;