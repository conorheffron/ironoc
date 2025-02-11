import React, { Component } from 'react';
import Button from 'react-bootstrap/Button';
import Spinner from 'react-bootstrap/Spinner';
import { Container } from 'reactstrap';

class LoadingSpinner extends Component {
  render() {
    return (
      <Container fluid>
        <Button variant="primary" disabled>
            <Spinner animation="border" variant="secondary" />
            <Spinner animation="border" variant="dark" />
            <Spinner animation="border" variant="danger" />
            <Spinner animation="border" variant="warning" />
            <h1>Loading...</h1>
        </Button>
      </Container>
    );
  }
}

export default LoadingSpinner;
