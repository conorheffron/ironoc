import { screen, render } from '@testing-library/react';
import CoffeeCarousel from '../CoffeeCarousel';
import ControlledCarousel from '../ControlledCarousel';

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
      expect(screen.getByText(item.ingredients.join(', '))).toBeInTheDocument();
    });
  });

  test('renders carousel with correct number of items', () => {
    render(<CoffeeCarousel items={coffeeItems} />);

    // Check that the correct number of carousel items are rendered
    const carouselItems = screen.getAllByRole('img');

    expect(carouselItems.length).toBe(coffeeItems.length);
  });

  test('does not render ingredients if item.ingredients is null or an empty array', () => {
      const mockItems = [
          {
              image: 'http://image1.jpg',
              title: 'Coffee with no ingredients',
              ingredients: [],
          },
          {
              image: 'https://image2.jpg',
              title: 'Coffee with null ingredients',
              ingredients: null,
          },
      ];

      render(<CoffeeCarousel items={mockItems} />);

      // Check that the titles are rendered
      expect(screen.getByText('Coffee with no ingredients')).toBeInTheDocument();
      expect(screen.getByText('Coffee with null ingredients')).toBeInTheDocument();

      // Ensure ingredients are NOT rendered
      expect(screen.queryByText('Ingredients:')).not.toBeInTheDocument();
  });
});
