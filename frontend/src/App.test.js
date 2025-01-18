import React from 'react';
import { render, screen, waitFor } from '@testing-library/react';
import '@testing-library/jest-dom';
import Home from './components/Home';
import axios from 'axios';
import App from './App';
import CoffeeCarousel from './components/CoffeeCarousel';
import CoffeeHome from './components/CoffeeHome';
import NotFound from './components/NotFound';

// Mocking axios
jest.mock('axios');

test('renders learn react link', () => {
  render(<Home />);
  const element = screen.getByText(/Home/i);
  expect(element).toBeInTheDocument();
});

// Sample data for testing
const coffeeItems = [
    {
        title: 'Espresso',
        ingredients: ['Water', 'Coffee beans'],
        image: 'https://example.com/espresso.jpg',
    },
    {
        title: 'Cappuccino',
        ingredients: ['Espresso', 'Steamed milk', 'Foam milk'],
        image: 'https://example.com/cappuccino.jpg',
    },
];

describe('CoffeeCarousel', () => {
    test('renders carousel with coffee items', () => {
        render(<CoffeeCarousel items={coffeeItems} />);

        // Check that the carousel items are rendered
        coffeeItems.forEach((item) => {
            expect(screen.getByText(item.title)).toBeInTheDocument();
            expect(screen.getByAltText(item.title)).toBeInTheDocument();
            expect(screen.getByText('Ingredients: ' + item.ingredients.join(', '))).toBeInTheDocument();
        });
    });

    test('renders carousel with correct number of items', () => {
        render(<CoffeeCarousel items={coffeeItems} />);

        // Check that the correct number of carousel items are rendered
        const carouselItems = screen.getAllByRole('img');

        expect(carouselItems.length).toBe(coffeeItems.length);
    });
});

describe('CoffeeHome', () => {
    beforeEach(() => {
        axios.get.mockResolvedValue({ data: coffeeItems });
    });

    test('renders AppNavbar component', () => {
        render(<App />);
        expect(screen.getByRole('banner')).toBeInTheDocument();
    });

    test('displays loading state initially', () => {
        render(<CoffeeHome />);
        expect(screen.getByText('Loading...')).toBeInTheDocument();
    });

    test('renders CoffeeCarousel component with coffee items', async () => {
        render(<CoffeeHome />);

        // Wait for the coffee items to be fetched and rendered
        await waitFor(() => {
            expect(screen.getByText('Espresso')).toBeInTheDocument();
            expect(screen.getByText('Cappuccino')).toBeInTheDocument();
        });
    });
});

describe('NotFound', () => {
    test('renders AppNavbar component', () => {
        render(<NotFound />);
        expect(screen.getByRole('banner')).toBeInTheDocument();
    });

    test('displays 404 error message', () => {
        render(<NotFound />);
        expect(screen.getByText('404 - Page Not Found')).toBeInTheDocument();
    });

    test('displays the apology message', () => {
        render(<NotFound />);
        expect(screen.getByText('Sorry, the page you are looking for could not be found.')).toBeInTheDocument();
    });
});
