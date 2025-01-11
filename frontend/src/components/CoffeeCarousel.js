import React from 'react';
import { Carousel } from 'react-bootstrap';
import '.././App.css';

const CoffeeCarousel = ({ items }) => {
    return (
        <Carousel className="App-header">
            {items.map((item, index) => (
                <Carousel.Item key={index}>
                    <img src={item.image} alt={item.title}/>
                    <Carousel.Caption>
                        <h3>{item.title}</h3>
                        <h5>{item.ingredients.join(', ')}</h5>
                    </Carousel.Caption>
                </Carousel.Item>
            ))}
        </Carousel>
    );
};

export default CoffeeCarousel;