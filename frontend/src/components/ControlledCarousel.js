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

const GITHUB_REPO_URL_REGEX = /^https?:\/\/(?:www\.)?github\.com\/([^/]+)\/([^/?#]+)/i;
const GITHUB_OPENGRAPH_BASE_URL = "https://opengraph.githubassets.com/1";

const isValidUrl = (url) => {
    try {
        const parsed = new URL(url);
        return parsed.protocol === "http:" || parsed.protocol === "https:";
    } catch (e) {
        return false;
    }
};

const getFallbackImage = (color) => {
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

const getGithubRepositoryImage = (link) => {
    if (!link || !isValidUrl(link)) {
        return null;
    }

    const match = link.match(GITHUB_REPO_URL_REGEX);
    if (!match) {
        return null;
    }

    return `${GITHUB_OPENGRAPH_BASE_URL}/${match[1]}/${match[2]}`;
};

const getPortfolioItemImage = (item) => {
    if (isValidUrl(item?.img)) {
        return item.img;
    }

    return getGithubRepositoryImage(item?.link) || getFallbackImage(item?.img);
};

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

        return (
            <div className="App">
                <AppNavbar />
                <Container>
                    <Carousel className="App-header">
                        {portfolioItems.map((item, index) => (
                            <Carousel.Item key={index} interval={500}>
                                <a href={item.link} target="_blank" rel="noreferrer">
                                    <img
                                        className="d-block w-100"
                                        src={getPortfolioItemImage(item)}
                                        alt={item.alt}
                                        referrerPolicy="no-referrer"
                                        onError={(event) => {
                                            event.currentTarget.onerror = null;
                                            event.currentTarget.src = getFallbackImage(item?.img);
                                        }}
                                    />
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
