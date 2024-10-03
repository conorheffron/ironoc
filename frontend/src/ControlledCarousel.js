import React, { Component } from 'react';
import Carousel from 'react-bootstrap/Carousel';
import 'bootstrap/dist/css/bootstrap.css';
import { Container } from 'reactstrap';
import AppNavbar from './AppNavbar';
import darkblue from './bg-img/darkblue-bg.png';
import teal from './bg-img/teal-bg.png';
import red from './bg-img/red-bg.png';

class ControlledCarousel extends Component {
	render() {
        return (
            <div>
                <AppNavbar/><br /><br />
                <Container fluid>
                    <br />
                    <div >
                    <Carousel>
                        <Carousel.Item interval={1500}>
                            <a href="https://github.com/conorheffron/ironoc-db" target="_blank" rel="noreferrer">
                                <Carousel.Caption>
                                    <h1>ironoc-db</h1>
                                    <h2>Sample Data Manager Service with UI (Thymeleaf Templating Engine)</h2>
                                </Carousel.Caption>
                                <img className="d-block w-100" src={darkblue} alt="Image One"/>
                            </a>
                        </Carousel.Item>
                        <Carousel.Item interval={500}>
                            <a href="https://github.com/conorheffron/booking-sys" target="_blank" rel="noreferrer">
                                <img className="d-block w-100" src={teal} alt="Image Two" />
                                <Carousel.Caption>
                                    <h1>booking-sys</h1>
                                    <h2>Sample Reservations & Viewing System (Python / Django Web App)</h2>
                                </Carousel.Caption>
                            </a>
                        </Carousel.Item>
                        <Carousel.Item interval={500}>
                            <a href="https://github.com/cph33/nba-stats" target="_blank" rel="noreferrer">
                                <Carousel.Caption>
                                    <h1>nba-stats</h1>
                                    <h2>NBA Analytics in Python (Data for seasons 2015 - 2023): Player Statistics</h2>
                                </Carousel.Caption>
                                <img className="d-block w-100" src={red} alt="Image Three" />
                            </a>
                        </Carousel.Item>
                        <Carousel.Item interval={500}>
                            <a href="https://github.com/conorheffron/cbio-skin-canc" target="_blank" rel="noreferrer">
                                <Carousel.Caption>
                                    <h1>cbio-skin-canc</h1>
                                    <h2>Skin Cancer Dataset Analysis (R Programming)</h2>
                                </Carousel.Caption>
                                <img className="d-block w-100" src={darkblue} alt="Image Four" />
                            </a>
                        </Carousel.Item>
                        <Carousel.Item interval={500}>
                            <a href="https://github.com/conorheffron/gene-expr" target="_blank" rel="noreferrer">
                                <Carousel.Caption>
                                    <h1>gene-expr</h1>
                                    <h2>Breast Cancer Dataset Analysis (R Programming)</h2>
                                </Carousel.Caption>
                                <img className="d-block w-100" src={teal} alt="Image Five" />
                            </a>
                        </Carousel.Item>
                        <Carousel.Item interval={500}>
                            <a href="https://github.com/conorheffron/bio-cell-red-edge" target="_blank" rel="noreferrer">
                                <Carousel.Caption>
                                    <h1>bio-cell-red-edge</h1>
                                    <h2>Edge Detection of Biological Cell (Image Processing Python Script)</h2>
                                </Carousel.Caption>
                                <img className="d-block w-100" src={red} alt="Image Six" />
                            </a>
                        </Carousel.Item>
                        <Carousel.Item interval={500}>
                            <a href="https://github.com/conorheffron/global-max-sim-matrix" target="_blank" rel="noreferrer">
                                <Carousel.Caption>
                                    <h1>global-max-sim-matrix</h1>
                                    <h2>Compute Global Maximum Similarity Matrix (R Package)</h2>
                                </Carousel.Caption>
                                <img className="d-block w-100" src={darkblue} alt="Image Seven" />
                            </a>
                        </Carousel.Item>
                    </Carousel>
                    </div>
                </Container>
            </div>
        );
    }
}

export default ControlledCarousel;
