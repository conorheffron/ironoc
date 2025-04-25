import { render, screen, waitFor } from '@testing-library/react';
import App from '../../App';
import { Router, MemoryRouter } from 'react-router';
import Donate from '../Donate';
import LoadingSpinner from '../../LoadingSpinner';

// Mock API response
const mockDonateItems = [
  {
    donate: 'https://example.com/donate1',
    alt: 'Item 1 Alt Text',
    name: 'Item 1',
    phone: '+123456789',
    link: 'https://example.com/home1',
    founded: '2001',
    overview: 'This is an overview of Item 1.',
  },
  {
    donate: 'https://example.com/donate2',
    alt: 'Item 2 Alt Text',
    name: 'Item 2',
    phone: '+987654321',
    link: 'https://example.com/home2',
    founded: '1999',
    overview: 'This is an overview of Item 2.',
  },
];

describe('Donate', () => {
  beforeEach(() => {
    jest.spyOn(global, 'fetch').mockResolvedValue({
      json: jest.fn().mockResolvedValue(mockDonateItems)
    });
  });

  afterEach(() => {
    jest.restoreAllMocks();
  });

  test('renders AppNavbar component', () => {
    render(<App />);
    expect(screen.getByRole('banner')).toBeInTheDocument();
  });

  test('renders loading state initially', () => {
    render(<Donate />);
    expect(screen.getByText('Loading...')).toBeInTheDocument();
  });

  test('displays charity options after fetching data', async () => {
    const { container } = render(
        <MemoryRouter>
          <Donate />
        </MemoryRouter>
      );

    // Wait for the component to finish loading
    await waitFor(() => {
      expect(screen.queryByText('Loading...')).not.toBeInTheDocument();
    });

    // Check that repo details are displayed
    mockDonateItems.forEach(item => {
      expect(screen.getByText(item.name)).toBeInTheDocument();
      expect(screen.getByText(new RegExp(`Founded in ${item.founded}`))).toBeInTheDocument();
      expect(screen.getByText(item.phone)).toBeInTheDocument();
      expect(screen.getByText(new RegExp(item.link))).toBeInTheDocument();
    });

    // Verify that the component does not show the loading spinner
    expect(container.querySelector('.LoadingSpinner')).not.toBeInTheDocument();
  });
});
