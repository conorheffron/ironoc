import React, { Component } from 'react';
import Carousel from 'react-bootstrap/Carousel';
import 'bootstrap/dist/css/bootstrap.css';
import '../App.css';
import AppNavbar from '../AppNavbar';
import Footer from '../Footer';
import red from '../img/red-bg.png';
import { Container } from 'reactstrap';
import LoadingSpinner from '.././LoadingSpinner';

class Donate extends Component {

    constructor(props) {
        super(props);
            this.state = {
            donateItems: [],
            loading: true
        };
    }

    async componentDidMount() {
        const response = await fetch("/api/donate-items");
        const body = await response.json();
        this.setState({ donateItems: body, loading: false });
    }

    render() {
        const { donateItems, loading} = this.state;

        if (loading) {
            return (
                <div className="App">
                    <AppNavbar />
                    <Container>
                        <br /><br /><br />
                        <LoadingSpinner />
                    </Container>
                    <Footer />
                </div>
            );
        }

        return (
            <div>
                <AppNavbar />
                <Container>
                    <Carousel className="App-header">
                        {donateItems.map((item, index) => (
                            <Carousel.Item key={index} interval={500}>
                                <a href={item.donate} target="_blank" rel="noreferrer">
                                    <img className="d-block w-100" src={red} alt={item.alt} />
                                    <Carousel.Caption>
                                        <h1>
                                            <u>{item.name}</u>
                                        </h1><br />
                                        <h8>
                                            <b>Contact & Help by Phone: </b><span dangerouslySetInnerHTML={{ __html: item.phone }} />
                                        </h8><br />
                                        <h7>
                                            <b>Home page: </b><a href={item.link} target="_blank" rel="noreferrer">{item.link}</a>
                                        </h7><br /><br />
                                        <h11 className="overview-text">
                                            <b>Overview:</b> Founded in {item.founded}, {item.overview}
                                        </h11>
                                    </Carousel.Caption>
                                </a>
                            </Carousel.Item>
                        ))}
                    </Carousel>
                </Container>
                <Footer />
            </div>
        );
    }
}

export default Donate;
