import React, { Component } from 'react';
import { gql } from '@apollo/client';
import { withApollo } from '@apollo/client/react/hoc'; // HOC for injecting Apollo Client
import Carousel from 'react-bootstrap/Carousel';
import 'bootstrap/dist/css/bootstrap.css';
import '../App.css';
import AppNavbar from '../AppNavbar';
import red from '../img/red-bg.png';
import { Container } from 'reactstrap';
import LoadingSpinner from '.././LoadingSpinner';

// Define the GraphQL query to fetch donate items
export const GET_DONATE_ITEMS = gql`
    query {
        donateItems {
            donate
            link
            img
            alt
            name
            overview
            founded
            phone
        }
    }
`;

class Donate extends Component {
    constructor(props) {
        super(props);
        this.state = {
            donateItems: [],
            loading: true,
            error: null,
        };
    }

    async componentDidMount() {
        const { client } = this.props; // Injected Apollo Client
        try {
            const { data } = await client.query({
                query: GET_DONATE_ITEMS,
            });
            this.setState({ donateItems: data.donateItems, loading: false });
        } catch (error) {
            this.setState({ donateItems: [], error: error.message, loading: false });
        }
    }

    render() {
        const { donateItems, loading, error } = this.state;

        if (loading) {
            return (
                <div className="App">
                    <AppNavbar />
                    <Container>
                        <br />
                        <br />
                        <br />
                        <LoadingSpinner />
                    </Container>
                </div>
            );
        }

        if (error) {
            return (
                <div className="App">
                    <AppNavbar />
                    <Container>
                        <br />
                        <br />
                        <br />
                        <p>Error loading data: {error}</p>
                    </Container>
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
            </div>
        );
    }
}

// Wrap the component with Apollo Client
export default withApollo(Donate);
