import React, { Component } from 'react';
import { Container } from 'reactstrap';
import '.././App.css';
import AppNavbar from '.././AppNavbar';
import Footer from '.././Footer';

class NotFound extends Component {
    render() {
        const {
            title = "404 - Page Not Found",
            message = "Sorry, the page you are looking for could not be found."
        } = this.props;

        return (
            <div className="App">
                <AppNavbar />
                <Container>
                    <header className="App-header">
                        <h1>{title}</h1>
                        <p id="my-intro">{message}</p>
                    </header>
                </Container>
                <Footer />
            </div>
        );
    }
}

export default NotFound;
