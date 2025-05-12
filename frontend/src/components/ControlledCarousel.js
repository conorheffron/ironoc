import React, { Component } from 'react';
import Carousel from 'react-bootstrap/Carousel';
import 'bootstrap/dist/css/bootstrap.css';
import '.././App.css';
import { Container } from 'reactstrap';
import AppNavbar from '.././AppNavbar';
import navy from '.././img/darkblue-bg.png';
import teal from '.././img/teal-bg.png';
import red from '.././img/red-bg.png';
import LoadingSpinner from '.././LoadingSpinner';

class ControlledCarousel extends Component {
    constructor(props) {
        super(props);
            this.state = {
            portfolioItems: [],
            loading: true
        };
    }

    async componentDidMount() {
        const response = await fetch("/api/portfolio-items");
        const body = await response.json();
        this.setState({ portfolioItems: body, loading: false });
    }

    render() {
        const { portfolioItems, loading} = this.state;

        if (loading) {
            return (
                <div className="App">
                    <AppNavbar />
                    <Container>
                        <br /><br /><br />
                        <LoadingSpinner />
                    </Container>
                </div>
            );
        }

        const handleColor = (color) => {
            switch (color) {
                case "red":
                    return red;
                case "teal":
                    return teal;
                case "navy":
                    return navy;
                default:
                    return red;
                    }
            };

        return (
            <div>
                <AppNavbar />
                <Container>
                    <Carousel className="App-header">
                        {portfolioItems.map((item, index) => (
                            <Carousel.Item key={index} interval={500}>
                                <a href={item.link} target="_blank" rel="noreferrer">
                                    <img className="d-block w-100" src={handleColor(item.img)} alt={item.alt} />
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
