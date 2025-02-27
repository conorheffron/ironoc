import React, { Component } from 'react';
import { Container } from 'reactstrap';

class Footer extends Component {

    constructor(props) {
        super(props);
        this.state = {
          version: '',
          error: null,
        };
    }

    componentDidMount() {
        fetch('/api/application/version')
          .then(response => {
            if (!response.ok) {
              throw new Error('Network response was not ok');
            }
            return response.text();
          })
          .then(data => this.setState({ version: data }))
          .catch(error => this.setState({ error }));
      }

    render() {
        const { version, error } = this.state;

        return (
            <div className="App">
                <Container>
                    <footer><p className="ft">© 2025 by Conor Heffron | {version}</p></footer>
                </Container>
            </div>
          );
    }
}

export default Footer;
