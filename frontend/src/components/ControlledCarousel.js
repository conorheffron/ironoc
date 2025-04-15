import React, { Component } from 'react';
import Carousel from 'react-bootstrap/Carousel';
import 'bootstrap/dist/css/bootstrap.css';
import '.././App.css';
import { Container } from 'reactstrap';
import AppNavbar from '.././AppNavbar';
import navy from '.././img/darkblue-bg.png';
import teal from '.././img/teal-bg.png';
import red from '.././img/red-bg.png';

class ControlledCarousel extends Component {
    render() {
        const {
            items = [
                {
                    link: "https://github.com/conorheffron/ironoc-db",
                    img: navy,
                    alt: "navy1",
                    title: "ironoc-db",
                    description: "Sample Data Manager Service with UI",
                    techStack: "Java & Spring Boot, Thymeleaf Templating Engine, & MySQL.",
                },
                {
                    link: "https://github.com/conorheffron/booking-sys",
                    img: teal,
                    alt: "teal2",
                    title: "booking-sys",
                    description: "Sample Reservations & Viewer System",
                    techStack: "Python & Django Web App, JavaScript, SQLite3 or MySQL database.",
                },
                {
                    link: "https://github.com/conorheffron/nba-stats",
                    img: navy,
                    alt: "navy3",
                    title: "nba-stats",
                    description: "NBA Analytics (Seasons 2015 - 2023): Player Statistics",
                    techStack: "Jupyter Notebooks, Python, Pandas, & Requests / JSON API.",
                },
                {
                    link: "https://github.com/conorheffron/cbio-skin-canc",
                    img: red,
                    alt: "red4",
                    title: "cbio-skin-canc",
                    description: "Skin Cancer Dataset Analysis",
                    techStack: "R, dplyr, plotly, knitr, testthat, covr, GIT.",
                },
                {
                    link: "https://github.com/conorheffron/gene-expr",
                    img: navy,
                    alt: "navy5",
                    title: "gene-expr",
                    description: "Breast Cancer Dataset Analysis",
                    techStack: "R, ggplot2, dplyr, deseq2-analysis, & R markdown.",
                },
                {
                    link: "https://github.com/conorheffron/bio-cell-red-edge",
                    img: red,
                    alt: "red6",
                    title: "bio-cell-red-edge",
                    description: "Edge Detection of Biological Cell (Image Processing Script)",
                    techStack: "Python, sci-kit-image, matplotlib.pyplot, & scipy.ndimage.",
                },
                {
                    link: "https://github.com/conorheffron/global-max-sim-matrix",
                    img: teal,
                    alt: "teal7",
                    title: "global-max-sim-matrix",
                    description: "Compute Global Maximum Similarity Matrix",
                    techStack: "R Package, testthat, stringr, & devtools.",
                },
            ]
        } = this.props;

        return (
            <div>
                <AppNavbar />
                <Container>
                    <Carousel className="App-header">
                        {items.map((item, index) => (
                            <Carousel.Item key={index} interval={500}>
                                <a href={item.link} target="_blank" rel="noreferrer">
                                    <img className="d-block w-100" src={item.img} alt={item.alt} />
                                    <Carousel.Caption>
                                        <h1><u>{item.title}</u></h1>
                                        <h2>{item.description}</h2>
                                        <br /><br />
                                        <h4><b>Tech Stack:</b></h4>
                                        <h4>{item.techStack}</h4>
                                    </Carousel.Caption>
                                </a>
                            </Carousel.Item>
                        ))}
                    </Carousel>
                </Container>
            </div>
        );
    }
}

export default ControlledCarousel;
