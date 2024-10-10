import React, { Component } from 'react';
import { Container } from 'reactstrap';
import '.././App.css';
import AppNavbar from '.././AppNavbar';

class NotFound extends Component {

    render() {
        return (
            <div className="App">
              <AppNavbar/>
              <Container>
                  <header className="App-header">
                    <h1>404 - Page Not Found</h1>
                    <p id="my-intro">Sorry, the page you are looking for could not be found.</p>
                  </header>
              </Container>
            </div>
        );
    }
}

export default NotFound;
