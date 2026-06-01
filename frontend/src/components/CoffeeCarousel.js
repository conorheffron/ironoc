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

const brewTitleTranslations = {
    'Svart Te': 'Black Tea',
    'Islatte': 'Iced Latte',
    'Islatte Mocha': 'Iced Mocha',
    'Frapino Mocka': 'Frapino Mocha',
    'Apelsinjuice': 'Orange Juice',
    'Lemonad': 'Lemonade',
};

const ingredientTranslations = {
    'Ångad mjölk': 'Steamed milk',
    'Karamellsirap': 'Caramel syrup',
    'Hett vatten': 'Hot water',
    'Choklad': 'Chocolate',
    'Mjölk': 'Milk',
    'Te': 'Tea',
    'Ingefära': 'Ginger',
    'Kardemumma': 'Cardamom',
    'Kanel': 'Cinnamon',
    'Matcha-pulver': 'Matcha powder',
    'Socker*': 'Sugar*',
    'Kaffe': 'Coffee',
    'Is': 'Ice',
    'Sirap': 'Syrup',
    'Vispgrädde*': 'Whipped cream*',
    'Karamellsås': 'Caramel sauce',
    'Färska Apelsiner': 'Fresh oranges',
    'Citronsaft': 'Lemon juice',
    'Kolsyrat vatten': 'Sparkling water',
    'Honung': 'Honey',
};

function translateValue(value, translations) {
    const trimmedValue = typeof value === 'string' ? value.trim() : value;
    return translations[trimmedValue] || trimmedValue;
}

function translateIngredients(ingredients) {
    if (Array.isArray(ingredients)) {
        return ingredients
            .map((ingredient) => translateValue(ingredient, ingredientTranslations))
            .join(', ');
    }
    if (typeof ingredients === 'string') {
        return ingredients
            .split(',')
            .map((ingredient) => translateValue(ingredient, ingredientTranslations))
            .join(', ');
    }
    return ingredients;
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
                {validItems.map((item, index) => {
                    const translatedTitle = translateValue(item.title, brewTitleTranslations);
                    return (
                    <Carousel.Item key={index}>
                        <img src={item.image} alt={translatedTitle} />
                        <Carousel.Caption>
                            <h3>{translatedTitle}</h3>
                            {item.ingredients && item.ingredients.length > 0 && (
                                <h5>
                                    <b>Ingredients:</b>{' '}
                                    {translateIngredients(item.ingredients)}
                                </h5>
                            )}
                        </Carousel.Caption>
                    </Carousel.Item>
                    );
                })}
            </Carousel>
        );
    }
}

export default CoffeeCarousel;
