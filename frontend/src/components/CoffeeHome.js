import React, { useEffect, useState } from 'react';
import { Button, Container, InputGroup, Table } from 'reactstrap';
import axios from 'axios';
import AppNavbar from '.././AppNavbar';
import CoffeeCarousel from './CoffeeCarousel';
import Footer from '.././Footer';
import LoadingSpinner from '.././LoadingSpinner';

function CoffeeHome() {
    const [coffeeItems, setCoffeeItems] = useState([]);

    useEffect(() => {
        axios.get('/coffees-graph-ql')
            .then(response => setCoffeeItems(response.data))
            .catch(error => console.error('Error fetching coffee details:', error));
    }, []);

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

export default CoffeeHome;