import React, { Component } from 'react';
import Carousel from 'react-bootstrap/Carousel';
import 'bootstrap/dist/css/bootstrap.css';
import '.././App.css';
import { Container } from 'reactstrap';
import AppNavbar from '.././AppNavbar';
import navy from './img/darkblue-bg.png';
import teal from './img/teal-bg.png';
import red from './img/red-bg.png';

class ControlledCarousel extends Component {

	render() {
        return (
            <div>
                <AppNavbar/>
                <header className="App-header">
                    <Container fluid>

                        <Carousel>
                            <Carousel.Item interval={500}>
                                <a href="https://github.com/conorheffron/ironoc-db" target="_blank" rel="noreferrer">
                                    <Carousel.Caption>
                                        <br />
                                        <h1>ironoc-db</h1>
                                        <h2>Sample Data Manager Service with UI</h2>
                                        <br /><br />
                                        <h3>Tech Stack:</h3>
                                        <h4>Java & Spring Boot, Thymeleaf Templating Engine, & MySQL.</h4>
                                    </Carousel.Caption>
                                    <img className="d-block w-100" src={navy} alt="Image One"/>
                                </a>
                            </Carousel.Item>
                            <Carousel.Item interval={500}>
                                <a href="https://github.com/conorheffron/booking-sys" target="_blank" rel="noreferrer">
                                    <img className="d-block w-100" src={teal} alt="Image Two" />
                                    <Carousel.Caption>
                                        <br />
                                        <h1>booking-sys</h1>
                                        <h2>Sample Reservations & Viewing System (Python / Django Web App)</h2>
                                        <br /><br />
                                        <h3>Tech Stack:</h3>
                                        <h4>Python & Django, SQLite3 or MySQL database.</h4>
                                    </Carousel.Caption>
                                </a>
                            </Carousel.Item>
                            <Carousel.Item interval={500}>
                                <a href="https://github.com/cph33/nba-stats" target="_blank" rel="noreferrer">
                                    <Carousel.Caption>
                                        <br />
                                        <h1>nba-stats</h1>
                                        <h2>NBA Analytics (Data for seasons 2015 - 2023): Player Statistics</h2>
                                        <br /><br />
                                        <h3>Tech Stack:</h3>
                                        <h4>Jupyter Notebooks, Python, Pandas, & Requests / JSON API.</h4>
                                    </Carousel.Caption>
                                    <img className="d-block w-100" src={navy} alt="Image Three" />
                                </a>
                            </Carousel.Item>
                            <Carousel.Item interval={500}>
                                <a href="https://github.com/conorheffron/cbio-skin-canc" target="_blank" rel="noreferrer">
                                    <Carousel.Caption>
                                        <br />
                                        <h1>cbio-skin-canc</h1>
                                        <h2>Skin Cancer Dataset Analysis (R Programming)</h2>
                                        <br /><br />
                                        <h3>Tech Stack:</h3>
                                        <h4>R, dplyr, plotly, knitr, testthat, covr, GIT.</h4>
                                    </Carousel.Caption>
                                    <img className="d-block w-100" src={red} alt="Image Four" />
                                </a>
                            </Carousel.Item>
                            <Carousel.Item interval={500}>
                                <a href="https://github.com/conorheffron/gene-expr" target="_blank" rel="noreferrer">
                                    <Carousel.Caption>
                                        <br />
                                        <h1>gene-expr</h1>
                                        <h2>Breast Cancer Dataset Analysis (R Programming)</h2>
                                        <br /><br />
                                        <h3>Tech Stack:</h3>
                                        <h4>R, ggplot2, dplyr, deseq2-analysis, & R markdown.</h4>
                                    </Carousel.Caption>
                                    <img className="d-block w-100" src={navy} alt="Image Five" />
                                </a>
                            </Carousel.Item>
                            <Carousel.Item interval={500}>
                                <a href="https://github.com/conorheffron/bio-cell-red-edge" target="_blank" rel="noreferrer">
                                    <Carousel.Caption>
                                        <br />
                                        <h1>bio-cell-red-edge</h1>
                                        <h2>Edge Detection of Biological Cell (Image Processing Python Script)</h2>
                                        <br /><br />
                                        <h3>Tech Stack:</h3>
                                        <h4>Python, sci-kit-image, matplotlib.pyplot, & scipy.ndimage.</h4>
                                    </Carousel.Caption>
                                    <img className="d-block w-100" src={red} alt="Image Six" />
                                </a>
                            </Carousel.Item>
                            <Carousel.Item interval={500}>
                                <a href="https://github.com/conorheffron/global-max-sim-matrix" target="_blank" rel="noreferrer">
                                    <Carousel.Caption>
                                        <br />
                                        <h1>global-max-sim-matrix</h1>
                                        <h2>Compute Global Maximum Similarity Matrix (R Package)</h2>
                                        <br /><br />
                                        <h3>Tech Stack:</h3>
                                        <h4>R Package, testthat, stringr, & devtools.
                                        </h4>
                                    </Carousel.Caption>
                                    <img className="d-block w-100" src={teal} alt="Image Seven" />
                                </a>
                            </Carousel.Item>
                        </Carousel>
                    </Container>
                    </header>
            </div>
        );
    }
}

export default ControlledCarousel;
