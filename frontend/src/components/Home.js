import React, { Component } from 'react';
import { Container } from 'reactstrap';
import '.././App.css';
import AppNavbar from '.././AppNavbar';
import { loadCameraRollImages } from '../utils/cameraRollConfig';

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
            welcomeMessage,
            backgroundImages: [],
            backgroundImageIndex: 0
        };

        this.rotateCameraRollImage = this.rotateCameraRollImage.bind(this);
        this.rotationIntervalId = null;
        this.isMountedView = false;
    }

    async componentDidMount() {
        this.isMountedView = true;
        const backgroundImages = await loadCameraRollImages('home');

        if (this.isMountedView) {
            this.setState({ backgroundImages, backgroundImageIndex: 0 });

            if (backgroundImages.length > 1) {
                this.rotationIntervalId = setInterval(this.rotateCameraRollImage, 10000);
            }
        }
    }

    componentWillUnmount() {
        this.isMountedView = false;

        if (this.rotationIntervalId) {
            clearInterval(this.rotationIntervalId);
        }
    }

    rotateCameraRollImage() {
        this.setState((prevState) => ({
            backgroundImageIndex: (prevState.backgroundImageIndex + 1) % prevState.backgroundImages.length
        }));
    }

    render() {
        const { className, headerClassName, introId, welcomeMessage, backgroundImages, backgroundImageIndex } = this.state;
        const activeBackgroundImage = backgroundImages[backgroundImageIndex];
        const headerStyle = activeBackgroundImage
            ? { backgroundImage: `linear-gradient(rgba(10, 34, 80, 0.55), rgba(10, 34, 80, 0.55)), url(${activeBackgroundImage})` }
            : undefined;

        return (
            <div className={className}>
                <AppNavbar />
                <Container>
                    <header
                        data-testid="home-header"
                        className={`${headerClassName} App-header-camera-roll App-header-camera-roll--home`}
                        style={headerStyle}
                    >
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
