import { render, screen, waitFor, act } from '@testing-library/react';
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
  },
  {
    link: "https://example.com/custom-project",
    img: "red",
    alt: "red3",
    title: "custom-project",
    description: "Sample custom project",
    techStack: "React, JavaScript"
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

  test('renders AppNavbar component', async () => {
    await act(async () => {
      render(<App />);
    });
    expect(screen.getByRole('banner')).toBeInTheDocument();
  });

  test('renders loading state initially', () => {
    global.fetch.mockReset();
    global.fetch.mockImplementation(() => new Promise(() => {}));
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

  test('uses github snapshot images for github links and fallback image for non-github links', async () => {
    render(
      <MemoryRouter>
        <ControlledCarousel />
      </MemoryRouter>
    );

    const githubImage = await screen.findByAltText('navy1');
    expect(githubImage).toHaveAttribute(
      'src',
      'https://opengraph.githubassets.com/1/conorheffron/ironoc-db'
    );

    const fallbackImage = await screen.findByAltText('red3');
    expect(fallbackImage.getAttribute('src')).toContain('red-bg');
  });

  test('uses item.img directly when it is a valid absolute URL, taking precedence over GitHub snapshot', async () => {
    const absoluteImgUrl = 'https://example.com/my-project-image.png';
    const itemsWithAbsoluteImg = [
      {
        link: 'https://github.com/conorheffron/ironoc-db',
        img: absoluteImgUrl,
        alt: 'absolute-img',
        title: 'absolute-img-project',
        description: 'Project with absolute img URL',
        techStack: 'React'
      }
    ];
    jest.spyOn(global, 'fetch').mockResolvedValueOnce({
      json: jest.fn().mockResolvedValue(itemsWithAbsoluteImg)
    });

    render(
      <MemoryRouter>
        <ControlledCarousel />
      </MemoryRouter>
    );

    const img = await screen.findByAltText('absolute-img');
    expect(img).toHaveAttribute('src', absoluteImgUrl);
  });
});
