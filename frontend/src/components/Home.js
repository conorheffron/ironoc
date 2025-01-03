import React, { Component } from 'react';
import { Container } from 'reactstrap';
import '.././App.css';
import AppNavbar from '.././AppNavbar';
import logo from '.././img/robot-logo.png';

class Home extends Component {

    render() {
        return (
            <div className="App">
              <AppNavbar/>
              <Container>
                  <header className="App-header">
                    <a href="/"><img src={logo} className="App-logo" alt="iRonoc"/></a>
                    <p id="my-intro">Web Application Development | Software & Data Engineering | DevOps<br />
                    Welcome to my personal portfolio site. 
                    Please use the navigation bar to view different features such as a project carousel that scrolls through highlighted projects & the GitHub project manager (PM) tool that I have implemented. 
                    This tool allows you to view & navigate GitHub user/organisation projects and the assiciated backlog of tickets/bugs.
                    The API is documented with Open API / Swagger & sample GET requests that return raw JSON responses are available for demonstration purposes only.</p>
                  </header>
              </Container>
            </div>
        );
    }
}

export default Home;
