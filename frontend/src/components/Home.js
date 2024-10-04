import React, { Component } from 'react';
import { Container } from 'reactstrap';
import '.././App.css';
import AppNavbar from '.././AppNavbar';
import logo from './img/robot-logo.png';

class Home extends Component {

    render() {
        return (
            <div className="App">
              <AppNavbar/>
              <Container fluid>
                  <header className="App-header">
                    <img src={logo} className="App-logo" alt="logo"/>
                    <h1>iRonoc</h1>
                    <p>Web Application Development | Software Engineering | Data Engineering | Cloud Deployments | DevOps</p>
                  </header>
              </Container>
            </div>
        );
    }
}

export default Home;
