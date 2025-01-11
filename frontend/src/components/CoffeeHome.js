import React, { useEffect, useState } from 'react';
import { Button, Container, InputGroup, Table } from 'reactstrap';
import axios from 'axios';
import AppNavbar from '.././AppNavbar';
import CoffeeCarousel from './CoffeeCarousel';
import Footer from '.././Footer';

function CoffeeHome() {
    const [coffeeItems, setCoffeeItems] = useState([]);

    useEffect(() => {
        axios.get('/coffees')
            .then(response => setCoffeeItems(response.data))
            .catch(error => console.error('Error fetching coffee details:', error));
    }, []);

    return (
        <div className="App">
            <AppNavbar/>
            <Container>
                <h1>Coffee Carousel</h1>
                {coffeeItems.length > 0 ? (
                    <CoffeeCarousel items={coffeeItems} />
                ) : (
                    <div>Loading...</div>
                )}
            </Container>
            <Footer/>
        </div>
    );
}

export default CoffeeHome;