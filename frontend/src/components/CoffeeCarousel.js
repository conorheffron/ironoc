import React, { Component } from 'react';
import { Carousel } from 'react-bootstrap';
import '.././App.css';

// Helper function to validate URLs
function isValidUrl(url) {
    try {
        const parsed = new URL(url);
        return parsed.protocol === "http:" || parsed.protocol === "https:";
    } catch (e) {
        return false;
    }
}

class CoffeeCarousel extends Component {
    render() {
        const { items } = this.props;
        // Only include items that are not null/undefined and have a valid http/https URL
        const validItems = (items || []).filter(
            item => item && isValidUrl(item.image)
        );
        return (
            <Carousel className="App-header">
                {validItems.map((item, index) => (
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
