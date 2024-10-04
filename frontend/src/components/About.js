import React, { Component } from 'react';
import { Container } from 'reactstrap';
import '.././App.css';
import AppNavbar from '.././AppNavbar';

class About extends Component {

  render() {
    return (
        <div className="App">
            <AppNavbar/>
            <Container fluid>
                <header className="App-header">
                        <a href="https://www.linkedin.com/in/conorheffron" target="_blank" rel="noreferrer">
                            <img src="https://static.licdn.com/scds/common/u/img/webpromo/btn_viewmy_160x33.png"
                                width="160" height="33" border="0" alt="View Conor Heffron's profile on LinkedIn"></img>
                        </a>
                        <br/>
                        <p id="my-intro">Welcome, I'm Conor Heffron, a Software Engineer hailing from County Meath, Ireland.
                        With over ten years of professional experience, I specialize in writing clean code and
                        developing high-performance applications. As a passionate Full Stack Developer, I am constantly
                        expanding my technical expertise across various tech stacks, languages, frameworks, and
                        tools in the realm of Software, Data, and DevOps. Let's connect and explore exciting
                        opportunities together! See above & below for contact details and further information.</p>
                        <br />
                        <iframe height='160' width='300' frameborder='0' allowtransparency='true' scrolling='no'
                            title='strava-badge'
                            src='https://www.strava.com/athletes/2582329/activity-summary/a106933e6b6e42ffb4dcf37d2b5fe047af61329a'>
                        </iframe>
                </header>
            </Container>
        </div>
    );
  }
}

export default About;