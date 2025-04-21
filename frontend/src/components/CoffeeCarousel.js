import React, { Component } from 'react';
import { Carousel } from 'react-bootstrap';
import '.././App.css';

class CoffeeCarousel extends Component {
    render() {
        const { items } = this.props;

        return (
            <Carousel className="App-header">
                {items.map((item, index) => (
                    <Carousel.Item key={index}>
                        <img src={item.image} alt={item.title} />
                        <Carousel.Caption>
                            <h3>{item.title}</h3>
                            {item.ingredients && item.ingredients.length > 0 && (
                                <h5>
                                    <b>Ingredients:</b>{' '}
                                    {Array.isArray(item.ingredients)
                                        ? item.ingredients.join(', ')
                                        : item.ingredients}
                                </h5>
                            )}
                        </Carousel.Caption>
                    </Carousel.Item>
                ))}
            </Carousel>
        );
    }
}

export default CoffeeCarousel;
