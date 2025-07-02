import React from 'react';
import { Container } from 'reactstrap';
import '../App.css';
import AppNavbar from '../AppNavbar';

function NotFound({ 
  title = "404 - Page Not Found", 
  message = "Sorry, the page you are looking for could not be found." 
}) {
  return (
    <div className="App">
      <AppNavbar />
      <Container>
        <header className="App-header">
          <h1>{title}</h1>
          <p id="my-intro">{message}</p>
        </header>
      </Container>
    </div>
  );
}

export default NotFound;
