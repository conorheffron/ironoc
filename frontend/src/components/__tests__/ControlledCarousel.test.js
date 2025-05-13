import { render, screen, waitFor } from '@testing-library/react';
import App from '../../App';
import { Router, MemoryRouter } from 'react-router';
import ControlledCarousel from '../ControlledCarousel';
import LoadingSpinner from '../../LoadingSpinner';

// Mock API response
const mockPortfolioItems = [
  {
    link: "https://github.com/conorheffron/ironoc-db",
    img: "navy",
    alt: "navy1",
    title: "ironoc-db",
    description: "Sample Data Manager Service with UI",
    techStack: "Java & Spring Boot, Thymeleaf Templating Engine, & MySQL."
  },
  {
    link: "https://github.com/conorheffron/booking-sys",
    img: "teal",
    alt: "teal2",
    title: "booking-sys",
    description: "Sample Reservations & Viewer System",
    techStack: "Python & Django Web App, JavaScript, SQLite3 or MySQL database."
  }
];

describe('Portfolio Controlled Carousel', () => {
  beforeEach(() => {
    jest.spyOn(global, 'fetch').mockResolvedValue({
      json: jest.fn().mockResolvedValue(mockPortfolioItems)
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
    render(<ControlledCarousel />);
    expect(screen.getByText('Loading...')).toBeInTheDocument();
  });

  test('displays portfolio items after fetching data', async () => {
    const { container } = render(
        <MemoryRouter>
          <ControlledCarousel />
        </MemoryRouter>
      );

    // Wait for the component to finish loading
    await waitFor(() => {
      expect(screen.queryByText('Loading...')).not.toBeInTheDocument();
    });

    // Check that portfolio items are displayed
    mockPortfolioItems.forEach(item => {
      expect(screen.getByText(item.title)).toBeInTheDocument();
      expect(screen.getByText(item.description)).toBeInTheDocument();
      expect(screen.getByText(item.techStack)).toBeInTheDocument();
    });

    // Verify that the component does not show the loading spinner
    expect(container.querySelector('.LoadingSpinner')).not.toBeInTheDocument();
  });
});

