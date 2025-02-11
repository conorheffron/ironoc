import React, { Component } from 'react';
import Button from 'react-bootstrap/Button';
import Spinner from 'react-bootstrap/Spinner';
import { Container } from 'reactstrap';

class LoadingSpinner extends Component {
  render() {
    return (
      <Container fluid>
        <Button variant="primary" disabled>
          <Spinner as="span" animation="grow" size="sm" role="status" aria-hidden="true" />
          Loading...
        </Button>
      </Container>
    );
  }
}

export default LoadingSpinner;
