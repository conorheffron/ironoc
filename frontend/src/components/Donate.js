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
import { trackClickOut } from '../utils/activityTracker';

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

// Define the GraphQL subscription to listen for newly added charities in real-time
export const DONATE_ADDED_SUBSCRIPTION = gql`
    subscription {
        donateItemsSubscription {
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
        this.subscription = null;
    }

    async componentDidMount() {
        const { client } = this.props; // Injected Apollo Client
        try {
            // 1. Initial query fetch of the charity options
            const { data } = await client.query({
                query: GET_DONATE_ITEMS,
            });
            this.setState({ donateItems: data.donateItems, loading: false });

            // 2. Real-time subscription hook to listen to WebSocket pushes
            this.subscription = client.subscribe({
                query: DONATE_ADDED_SUBSCRIPTION,
            }).subscribe({
                next: ({ data: subscriptionData }) => {
                    if (subscriptionData && subscriptionData.donateItemsSubscription) {
                        this.setState((prevState) => {
                            const newCharity = subscriptionData.donateItemsSubscription;
                            // Check for duplicates to prevent duplicate cards
                            const exists = prevState.donateItems.some(
                                (item) => item.name === newCharity.name
                            );
                            if (!exists) {
                                return { donateItems: [...prevState.donateItems, newCharity] };
                            }
                            return null;
                        });
                    }
                },
                error: (err) => console.error("Real-time charity sync error:", err)
            });
        } catch (error) {
            this.setState({ donateItems: [], error: error.message, loading: false });
        }
    }

    componentWillUnmount() {
        // Clean up the WebSocket subscription channel on component unmount
        if (this.subscription) {
            this.subscription.unsubscribe();
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
            <div className="App">
                <AppNavbar />
                <Container>
                    <Carousel className="App-header">
                        {donateItems.map((item, index) => (
                            <Carousel.Item key={index} interval={500} className="donate-carousel-item">
                                <img className="d-block w-100 donate-carousel-bg" src={red} alt={item.alt} />

                                <Carousel.Caption className="donate-carousel-caption">
                                    <h1 className="donate-title mb-3">
                                        <span style={{ textDecoration: 'underline' }}>{item.name}</span>
                                    </h1>

                                    <p className="donate-phone">
                                        <b>Contact & Help by Phone: </b>
                                        <span dangerouslySetInnerHTML={{ __html: item.phone }} />
                                    </p>

                                    <p className="donate-link">
                                        <b>Home page: </b>
                                        <a
                                            href={item.link}
                                            target="_blank"
                                            rel="noreferrer"
                                            onClick={() => trackClickOut('charity', item.link)}
                                        >
                                            {item.link}
                                        </a>
                                    </p>

                                    <p className="overview-text donate-overview">
                                        <b>Overview:</b> Founded in {item.founded}, {item.overview}
                                    </p>

                                    <p className="donate-action">
                                        <a
                                            href={item.donate}
                                            target="_blank"
                                            rel="noreferrer"
                                            onClick={() => trackClickOut('charity', item.donate)}
                                        >
                                            Donate here
                                        </a>
                                    </p>
                                </Carousel.Caption>
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
