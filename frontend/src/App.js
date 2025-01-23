import React, { Component } from 'react';
import { BrowserRouter as Router, Route, Switch } from 'react-router-dom';
import './App.css';
import Home from './components/Home';
import CoffeeHome from './components/CoffeeHome';
import NotFound from './components/NotFound';
import About from './components/About';
import RepoDetails from './components/RepoDetails';
import RepoIssues from './components/RepoIssues';
import ControlledCarousel from './components/ControlledCarousel';

class App extends Component {
  render() {
    // Default props
    const {
      forceRefresh = true,
      routes = [
        { path: '/', exact: true, component: Home },
        { path: '/about', exact: true, component: About },
        { path: '/portfolio', exact: true, component: ControlledCarousel },
        { path: '/projects', exact: true, component: RepoDetails },
        { path: '/projects/:id', component: RepoDetails },
        { path: '/issues/:id/:repo', component: RepoIssues },
        { path: '/brews', exact: true, component: CoffeeHome },
        { path: '*', component: NotFound }
      ]
    } = this.props;

    return (
      <Router forceRefresh={forceRefresh}>
        <Switch>
          {routes.map((route, index) => (
            <Route key={index} path={route.path} exact={route.exact} component={route.component} />
          ))}
        </Switch>
      </Router>
    );
  }
}

export default App;
