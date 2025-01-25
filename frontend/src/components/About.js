import React, { Component } from 'react';
import { Container } from 'reactstrap';
import '.././App.css';
import AppNavbar from '.././AppNavbar';
import Footer from '.././Footer';

class About extends Component {
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

        return (
            <div className="App">
                <AppNavbar />
                <Container>
                    <header className="App-header">
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
                        <a className="strava-badge" href={stravaLink} target="_blank">
                            Follow me on
                            <img className="strava-badge-img" src={stravaImgSrc} alt={stravaImgAlt} />
                        </a><br />
                    </header>
                </Container>
                <Footer />
            </div>
        );
    }
}

export default About;
