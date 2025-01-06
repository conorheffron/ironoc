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
                    <br /><br />
                    <a href="/"><img src={logo} className="App-logo" alt="iRonoc"/></a>
                    <p id="my-intro"> Welcome to my personal portfolio site.<br />
                    Please use the navigation bar to view different features such as about me, my link tree, a carousel 
                    that scrolls through highlighted projects & the GitHub project manager (PM) tool which is built 
                    against the iRonoc API.
                    <br /><br />
                    The GitHub PM tool allows you to view & navigate the backlog of issues & bugs for a given project
                    repository for the corresponding user or organisation account. There is an option to search by user ID
                    or to drill down to a specific repository name via search or 'List Issues' icon in the 'Actions' column 
                    of the <a href="https://www.ironoc.net/projects/conorheffron">projects component view</a>.
                    <br /><br />
                    The ironoc API is documented with <a href="https://www.ironoc.net/swagger-ui-ironoc.html">Open API</a> 
                    & sample GET requests that return raw JSON responses are available for demonstration purposes only i.e. 
                    <a href="https://www.ironoc.net/get-repo-issue/conorheffron/ironoc/">Issues JSON Sample</a>.</p>
                  </header>
              </Container>
            </div>
        );
    }
}

export default Home;
