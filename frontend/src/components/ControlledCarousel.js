import React, { Component } from 'react';
import Carousel from 'react-bootstrap/Carousel';
import 'bootstrap/dist/css/bootstrap.css';
import '.././App.css';
import { Container } from 'reactstrap';
import AppNavbar from '.././AppNavbar';
import Footer from '.././Footer';
import navy from '.././img/darkblue-bg.png';
import teal from '.././img/teal-bg.png';
import red from '.././img/red-bg.png';

class ControlledCarousel extends Component {

	render() {
        return (
            <div>
                <AppNavbar/>
                    <Container>
                        <Carousel className="App-header">
                            <Carousel.Item interval={500}>
                                <a href="https://github.com/conorheffron/ironoc-db" target="_blank" rel="noreferrer">
                                    <img className="d-block w-100" src={navy} alt="navy1"/>
                                    <Carousel.Caption>
                                        <h1><u>ironoc-db</u></h1>
                                        <h2>Sample Data Manager Service with UI</h2>
                                        <br /><br />
                                        <h4>Tech Stack:</h4>
                                        <h4>Java & Spring Boot, Thymeleaf Templating Engine, & MySQL.</h4>
                                    </Carousel.Caption>
                                </a>
                            </Carousel.Item>
                            <Carousel.Item interval={500}>
                                <a href="https://github.com/conorheffron/booking-sys" target="_blank" rel="noreferrer">
                                    <img className="d-block w-100" src={teal} alt="teal2" />
                                    <Carousel.Caption>
                                        <h1><u>booking-sys</u></h1>
                                        <h2>Sample Reservations & Viewer System</h2>
                                        <br /><br />
                                        <h4>Tech Stack:</h4>
                                        <h4>Python & Django Web App, JavaScript, SQLite3 or MySQL database.</h4>
                                    </Carousel.Caption>
                                </a>
                            </Carousel.Item>
                            <Carousel.Item interval={500}>
                                <a href="https://github.com/conorheffron/nba-stats" target="_blank" rel="noreferrer">
                                    <img className="d-block w-100" src={navy} alt="navy3" />
                                    <Carousel.Caption>
                                        <h1><u>nba-stats</u></h1>
                                        <h2>NBA Analytics (Seasons 2015 - 2023): Player Statistics</h2>
                                        <br /><br />
                                        <h4>Tech Stack:</h4>
                                        <h4>Jupyter Notebooks, Python, Pandas, & Requests / JSON API.</h4>
                                    </Carousel.Caption>
                                </a>
                            </Carousel.Item>
                            <Carousel.Item interval={500}>
                                <a href="https://github.com/conorheffron/cbio-skin-canc" target="_blank" rel="noreferrer">
                                    <img className="d-block w-100" src={red} alt="red4" />
                                    <Carousel.Caption>
                                        <h1><u>cbio-skin-canc</u></h1>
                                        <h2>Skin Cancer Dataset Analysis</h2>
                                        <br /><br />
                                        <h4>Tech Stack:</h4>
                                        <h4>R, dplyr, plotly, knitr, testthat, covr, GIT.</h4>
                                    </Carousel.Caption>
                                </a>
                            </Carousel.Item>
                            <Carousel.Item interval={500}>
                                <a href="https://github.com/conorheffron/gene-expr" target="_blank" rel="noreferrer">
                                    <img className="d-block w-100" src={navy} alt="navy5" />
                                    <Carousel.Caption>
                                        <h1><u>gene-expr</u></h1>
                                        <h2>Breast Cancer Dataset Analysis</h2>
                                        <br /><br />
                                        <h4>Tech Stack:</h4>
                                        <h4>R, ggplot2, dplyr, deseq2-analysis, & R markdown.</h4>
                                    </Carousel.Caption>
                                </a>
                            </Carousel.Item>
                            <Carousel.Item interval={500}>
                                <a href="https://github.com/conorheffron/bio-cell-red-edge" target="_blank" rel="noreferrer">
                                    <img className="d-block w-100" src={red} alt="red6" />
                                    <Carousel.Caption>
                                        <h1><u>bio-cell-red-edge</u></h1>
                                        <h2>Edge Detection of Biological Cell (Image Processing Script)</h2>
                                        <br /><br />
                                        <h4>Tech Stack:</h4>
                                        <h4>Python, sci-kit-image, matplotlib.pyplot, & scipy.ndimage.</h4>
                                    </Carousel.Caption>
                                </a>
                            </Carousel.Item>
                            <Carousel.Item interval={500}>
                                <a href="https://github.com/conorheffron/global-max-sim-matrix" target="_blank" rel="noreferrer">
                                    <img className="d-block w-100" src={teal} alt="teal7" />
                                    <Carousel.Caption>
                                        <h1><u>global-max-sim-matrix</u></h1>
                                        <h2>Compute Global Maximum Similarity Matrix</h2>
                                        <br /><br />
                                        <h4>Tech Stack:</h4>
                                        <h4>R Package, testthat, stringr, & devtools.
                                        </h4>
                                    </Carousel.Caption>
                                </a>
                            </Carousel.Item>
                        </Carousel>
                    </Container>
                <Footer/>
            </div>
        );
    }
}

export default ControlledCarousel;
