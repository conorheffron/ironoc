import React, { Component } from 'react';
import { Container } from 'reactstrap';
import '.././App.css';
import AppNavbar from '.././AppNavbar';
import Footer from '.././Footer';
import logo from '.././img/robot-logo.png';

class Home extends Component {

    render() {
        return (
            <div className="App">
              <AppNavbar/>
              <Container>
                  <header className="App-header">
                    <a href="/"><img src={logo} className="App-logo" alt="iRonoc"/></a>
                    <p id="my-intro">Web Application Development | Software & Data Engineering | DevOps</p>
                  </header>
              </Container>
              <Footer/>
            </div>
        );
    }
}

export default Home;
