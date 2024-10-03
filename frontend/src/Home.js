import React, { Component } from 'react';
import './App.css';
import AppNavbar from './AppNavbar';
import logo from './robot-logo.png';
import './App.css';

class Home extends Component {

    render() {
        return (
            <div className="App">
              <AppNavbar/>
              <header className="App-header">
                <img src={logo} className="App-logo" alt="logo"/>
              </header>
            </div>
        );
    }
}

export default Home;