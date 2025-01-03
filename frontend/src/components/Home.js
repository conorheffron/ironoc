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
                    <p id="my-intro"> Welcome to my personal portfolio site.<br />
                    Please use the navigation bar to view different features such as a project carousel that scrolls
                    through highlighted projects & the GitHub project manager (PM) tool.
                    <br /><br />
                    The GitHub PM tool allows you to view & navigate the backlog of issues & bugs for a given project
                    repository for the corresponding user or organisation account.
                    <br /><br />
                    The ironoc API is documented with Open API & sample GET requests that return raw JSON responses are
                    available for demonstration purposes only.</p>
                  </header>
              </Container>
            </div>
        );
    }
}

export default Home;
