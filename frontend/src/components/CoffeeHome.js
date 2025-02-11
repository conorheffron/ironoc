import React, { Component } from 'react';
import { Container } from 'reactstrap';
import axios from 'axios';
import AppNavbar from '.././AppNavbar';
import CoffeeCarousel from './CoffeeCarousel';
import Footer from '.././Footer';
import LoadingSpinner from '.././LoadingSpinner';

class CoffeeHome extends Component {
    constructor(props) {
        super(props);
        this.state = {
            coffeeItems: []
        };
    }

    componentDidMount() {
        axios.get('/api/coffees-graph-ql')
            .then(response => this.setState({ coffeeItems: response.data }))
            .catch(error => console.error('Error fetching coffee details:', error));
    }

    render() {
        const { coffeeItems } = this.state;

        return (
            <div className="App">
                <AppNavbar/>
                <Container>
                    {coffeeItems.length > 0 ? (
                        <>
                        <br /><br />
                        <CoffeeCarousel items={coffeeItems} />
                        </>
                    ) : (
                        <>
                        <br /><br /><br />
                        <LoadingSpinner/>
                        </>
                    )}
                </Container>
                <Footer/>
            </div>
        );
    }
}

export default CoffeeHome;
