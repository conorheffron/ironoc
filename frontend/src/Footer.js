import React, { Component } from 'react';
import { Container } from 'reactstrap';

class Footer extends Component {

    render() {
        return (
            <div className="App">
                <Container>
                    <footer><p className="ft">© 2017 by Conor Heffron</p></footer>
                </Container>
            </div>
          );
    }
}

export default Footer;
