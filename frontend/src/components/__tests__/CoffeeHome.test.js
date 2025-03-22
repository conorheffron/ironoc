import { screen, render, waitFor } from '@testing-library/react';
import axios from 'axios';
import App from '../../App';
import CoffeeHome from '../CoffeeHome';

// Mocking axios
jest.mock('axios');

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

describe('CoffeeHome', () => {
  beforeEach(() => {
    axios.get.mockResolvedValueOnce({ data: coffeeItems });
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
