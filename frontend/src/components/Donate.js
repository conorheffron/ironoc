import React from 'react';
import { gql } from '@apollo/client';
import { useQuery } from '@apollo/client/react';
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

function Donate() {
    const { loading, error, data } = useQuery(GET_DONATE_ITEMS);
    const donateItems = data?.donateItems || [];

    if (loading) {
        return (
            <div className="App">
                <AppNavbar />
                <Container>
                    <div className="mt-5">
                        <LoadingSpinner />
                    </div>
                </Container>
            </div>
        );
    }

    if (error) {
        return (
            <div className="App">
                <AppNavbar />
                <Container>
                    <p className="mt-5">Error loading data: {error.message}</p>
                </Container>
            </div>
        );
    }

    return (
        <div className="App">
            <AppNavbar />
            <Container>
                <Carousel className="App-header">
                    {donateItems.map((item, index) => (
                        <Carousel.Item key={index} interval={500}>
                            <a href={item.donate} target="_blank" rel="noreferrer">
                                <img className="d-block w-100" src={red} alt={item.alt} />
                                <Carousel.Caption>
                                    <h1 className="mb-3">
                                        <span style={{ textDecoration: 'underline' }}>{item.name}</span>
                                    </h1>
                                    <p>
                                        <b>Contact & Help by Phone: </b><span dangerouslySetInnerHTML={{ __html: item.phone }} />
                                    </p>
                                    <p>
                                        <b>Home page: </b><a href={item.link} target="_blank" rel="noreferrer">{item.link}</a>
                                    </p>
                                    <p className="overview-text">
                                        <b>Overview:</b> Founded in {item.founded}, {item.overview}
                                    </p>
                                </Carousel.Caption>
                            </a>
                        </Carousel.Item>
                    ))}
                </Carousel>
            </Container>
        </div>
    );
}

export default Donate;
