import React, { Component } from 'react';
import { Container } from 'reactstrap';
import '.././App.css';
import AppNavbar from '.././AppNavbar';
import { loadCameraRollImages } from '../utils/cameraRollConfig';

class About extends Component {
    constructor(props) {
        super(props);
        this.state = {
            backgroundImages: [],
            backgroundImageIndex: 0
        };

        this.rotateCameraRollImage = this.rotateCameraRollImage.bind(this);
        this.rotationIntervalId = null;
        this.isMountedView = false;
    }

    async componentDidMount() {
        this.isMountedView = true;
        const backgroundImages = await loadCameraRollImages('about');

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
        const {
            link = "https://www.linkedin.com/in/conorheffron",
            imgSrc = "https://static.licdn.com/scds/common/u/img/webpromo/btn_viewmy_160x33.png",
            imgWidth = 160,
            imgHeight = 33,
            imgAlt = "View Conor Heffron's profile on LinkedIn",
            stravaLink = 'https://strava.com/athletes/2582329',
            stravaImgSrc = 'https://badges.strava.com/logo-strava.png',
            stravaImgAlt = 'Strava'
        } = this.props;
        const { backgroundImages, backgroundImageIndex } = this.state;
        const activeBackgroundImage = backgroundImages[backgroundImageIndex];
        const headerStyle = activeBackgroundImage
            ? { backgroundImage: `linear-gradient(rgba(8, 36, 74, 0.6), rgba(8, 36, 74, 0.6)), url(${activeBackgroundImage})` }
            : undefined;

        return (
            <div className="App">
                <AppNavbar />
                <Container>
                    <header
                        data-testid="about-header"
                        className="App-header App-header-camera-roll App-header-camera-roll--about"
                        style={headerStyle}
                    >
                        <br /><br />
                        <a href={link} target="_blank" rel="noreferrer">
                            <img src={imgSrc} width={imgWidth} height={imgHeight} border="0" alt={imgAlt}></img>
                        </a>
                        <p id="my-intro">
                            <br />
                            Welcome, I'm Conor Heffron, a Software Engineer hailing from County Meath, Ireland.
                            With over fourteen years of professional experience, I specialize in writing clean code and
                            developing high-performance applications. As a passionate Full Stack Developer, I am constantly
                            expanding my technical expertise across various tech stacks, languages, frameworks, and tools in the
                            realm of Software Engineering, Data Engineering, & DevOps.
                            <br /><br />
                            I believe in continuous learning & practical skills that can be demonstrated in a positive & collaborative
                            manner (open source is great!). When not learning or working, I like jogging/cycling, music, cooking,
                            pretending to be a <a href="/brews">caffeine connoisseur</a>, & searching for new forms of
                            salsa verde / green sauce!
                            <br /><br />
                            Let's connect and explore exciting
                            opportunities together! See above & beyond for contact details and further information.
                        </p><br />
                        <a className="strava-badge" href={stravaLink} target="_blank" rel="noreferrer" >
                            Follow me on
                            <img className="strava-badge-img" src={stravaImgSrc} alt={stravaImgAlt} />
                        </a><br />
                    </header>
                </Container>
            </div>
        );
    }
}

export default About;
