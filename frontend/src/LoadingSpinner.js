import Button from 'react-bootstrap/Button';
import Spinner from 'react-bootstrap/Spinner';
import { Container } from 'reactstrap';
import AppNavbar from './AppNavbar';

function LoadingSpinner() {
  return (
        <Container fluid>
            <Button variant="primary" disabled>
            <Spinner as="span" animation="grow" size="sm" role="status" aria-hidden="true"/>Loading...</Button>
        </Container>
  );
}

export default LoadingSpinner;
