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
          null
      ];

      render(<CoffeeCarousel items={mockItems} />);

      // Check that the titles are rendered
      expect(screen.getByText('Coffee with no ingredients')).toBeInTheDocument();
      expect(screen.getByText('Coffee with null ingredients')).toBeInTheDocument();

      // Ensure ingredients are NOT rendered
      expect(screen.queryByText('Ingredients:')).not.toBeInTheDocument();
  });

  test('applies carousel label styling class to title and ingredients headings', () => {
    render(<CoffeeCarousel items={coffeeItems} />);

    const titleHeading = screen.getByRole('heading', { level: 3, name: 'Espresso' });
    const ingredientHeading = screen.getAllByRole('heading', { level: 5 })[0];

    expect(titleHeading).toHaveClass('coffee-carousel-label');
    expect(ingredientHeading).toHaveClass('coffee-carousel-label');
  });

  test('translates localized brew titles and ingredients', () => {
      const localizedItems = [
          {
              image: 'https://image1.jpg',
              title: 'Svart Te',
              ingredients: ' Te , Unknown ingredient , Honung ',
          },
      ];

      render(<CoffeeCarousel items={localizedItems} />);

      expect(screen.getByText('Black Tea')).toBeInTheDocument();
      expect(screen.getByAltText('Black Tea')).toBeInTheDocument();
      expect(screen.getByText('Tea, Unknown ingredient, Honey')).toBeInTheDocument();
      expect(screen.queryByText('Svart Te')).not.toBeInTheDocument();
      expect(screen.queryByText('Te , Unknown ingredient , Honung')).not.toBeInTheDocument();
  });
});
