import React, { Component } from 'react';
import { Container } from 'reactstrap';
import '.././App.css';
import AppNavbar from '.././AppNavbar';

class Home extends Component {
    constructor(props) {
        super(props);
        const {
            className = 'App',
            headerClassName = 'App-header',
            introId = 'my-intro',
            welcomeMessage = 'Welcome to my personal portfolio site.'
        } = props;

        this.state = {
            className,
            headerClassName,
            introId,
            welcomeMessage
        };
    }

    render() {
        const { className, headerClassName, introId, welcomeMessage } = this.state;

        return (
            <div className={className}>
                <AppNavbar />
                <Container>
                    <header className={headerClassName}>
                        <br /><br />
                        <p id={introId}>
                            {welcomeMessage}<br /><br />
                            Please use the navigation bar to view different features such as <a href="/donate">donate</a>&nbsp;
                            to one of my preferred charities (select anywhere on tile to go directly to online donation page),&nbsp;
                            <a href="/about">about</a> me, my&nbsp;<a href="https://linktr.ee/conorheffron" target="_blank"
                            rel="noopener noreferrer">link tree</a>, a <a href="/portfolio">carousel</a>&nbsp;that scrolls
                            through highlighted projects & the GitHub project manager (PM) tool which is built against the iRonoc API.
                            <br /><br />
                            If you like what you see, please sponsor my open source work.
                            <br /><br />
                            <iframe src="https://github.com/sponsors/conorheffron/card" title="Sponsor conorheffron"
                            height="180" width="300" >
                            </iframe>
                            <br /><br />
                            The GitHub PM tool allows you to view & navigate the backlog of issues & bugs for a given project
                            repository for the corresponding user or organisation account. There is an option to search by user ID
                            or to drill down to a specific repository name via search or 'List Issues' icon in the 'Actions' column
                            of the <a href="/projects/conorheffron">projects view</a>.
                            <br /><br />
                            The ironoc API is documented with &nbsp;<a href="/swagger-ui-ironoc.html" target="_blank" rel="noopener noreferrer">Open API</a>
                            &nbsp; & sample GET requests that return raw JSON responses are available for demonstration
                            purposes only i.e. &nbsp;<a href="/api/get-repo-issue/conorheffron/ironoc/" target="_blank" rel="noopener noreferrer">Issues JSON Sample</a>.
                        </p>
                    </header>
                </Container>
            </div>
        );
    }
}

export default Home;
