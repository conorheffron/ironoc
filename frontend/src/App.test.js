import React from 'react';
import { render, screen, act, waitFor, fireEvent } from '@testing-library/react';
import '@testing-library/jest-dom';
import Home from './components/Home';
import axios from 'axios';
import App from './App';
import CoffeeCarousel from './components/CoffeeCarousel';
import CoffeeHome from './components/CoffeeHome';
import Donate from './components/Donate';
import NotFound from './components/NotFound';
import { createMemoryHistory } from 'history';
import { Router, MemoryRouter } from 'react-router-dom';
import RepoDetails from './components/RepoDetails';
import RepoIssues from './components/RepoIssues';
import LoadingSpinner from './LoadingSpinner';
import AppNavBar from './AppNavbar';
import Footer from './Footer';

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

const mockRepoDetails = [
  {
    name: 'repo1',
    fullName: 'User/repo1',
    repoUrl: 'https://github.com/User/repo1',
    description: 'Description of repo1',
    appHome: 'https://repo1.com',
    topics: ['topic1', 'topic2']
  },
  {
    name: 'repo2',
    fullName: 'User/repo2',
    repoUrl: 'https://github.com/User/repo2',
    description: 'Description of repo2',
    appHome: 'https://repo2.com',
    topics: ['topic3', 'topic4']
  }
];

const mockRepoIssues = [
  {
    number: 1,
    title: 'Issue 1',
    body: 'https://github.com/User/repo/issues/1'
  },
  {
    number: 2,
    title: 'Issue 2',
    body: 'https://github.com/User/repo/issues/2'
  }
];

const donateItems = [
    {
        donate: "https://www.jackandjill.ie/professionals/ways-to-donate/",
        link: "https://www.jackandjill.ie",
        img: "red1.png",
        alt: "red1",
        name: "The Jack and Jill Children’s Foundation",
        overview: `The Jack and Jill Children’s Foundation is a nationwide charity that funds and provides in-home nursing care
                   and respite support for children with severe to profound neurodevelopmental delay, up to the age of 6.
                   This may include children with brain injury, genetic diagnosis, cerebral palsy and undiagnosed conditions.
                   Another key part of our service is end-of-life care for all children up to the age of 6, irrespective of diagnosis.`,
        founded: 1997,
        phone: "+353 (045) 894 538"
    },
    {
        donate: "https://vi.ie/supporting-us/donate-now/",
        link: "https://linktr.ee/vision_ireland",
        img: "red2.png",
        alt: "red2",
        name: "Vision Ireland, the new name for NCBI",
        overview: `Vision Ireland, the name for NCBI is Ireland’s national charity working for the rising number of people affected by sight loss.
                   Our practical and emotional advice and support help 8,000 people and their families confidently face their futures every year.`,
        founded: 1931,
        phone: "+353 (0)1 830 7033"
    }
    // Add more items as needed
];

describe('Home', () => {
  test('renders Home component', async () => {
    await act(async () => {
      render(<Home />);
    });
    const element = screen.getByText(/Home/i);
    expect(element).toBeInTheDocument();
  });
});

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
});

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

describe('RepoDetails', () => {
  beforeEach(() => {
    jest.spyOn(global, 'fetch').mockResolvedValue({
      json: jest.fn().mockResolvedValue(mockRepoDetails)
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
    render(<RepoDetails match={{ params: { id: 'User' } }} />);
    expect(screen.getByText('Loading...')).toBeInTheDocument();
  });

  test('displays repo details after fetching data', async () => {
    const history = createMemoryHistory();
    const { container } = render(
      <Router history={history}>
        <RepoDetails match={{ params: { id: 'User' } }} />
      </Router>
    );

    // Wait for the component to finish loading
    await waitFor(() => {
      expect(screen.queryByText('Loading...')).not.toBeInTheDocument();
    });

    // Check that repo details are displayed
    mockRepoDetails.forEach(repo => {
      expect(screen.getByText(repo.fullName)).toBeInTheDocument();
      expect(screen.queryByText(repo.repoUrl)).not.toBeInTheDocument();
      expect(screen.getByText(repo.description)).toBeInTheDocument();
      expect(screen.queryByText(repo.appHome)).not.toBeInTheDocument();

      repo.topics.forEach(topic => {
        // Select the <td> element
        const tdElement = screen.getByText((content, element) => {
          // Ensure the element is a <td>
          return element.tagName.toLowerCase() === 'td' && content.includes(topic);
        });
        // Check if the <td> contains the expected text
        expect(tdElement).toBeInTheDocument();
        expect(tdElement).toHaveTextContent(topic);
      });
    });

    // Verify that the component does not show the loading spinner
    expect(container.querySelector('.LoadingSpinner')).not.toBeInTheDocument();
  });
});

describe('RepoIssues', () => {
  beforeEach(() => {
    jest.spyOn(global, 'fetch').mockResolvedValue({
      json: jest.fn().mockResolvedValue(mockRepoIssues)
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
    render(<RepoIssues match={{ params: { id: 'User', repo: 'ironoc-test' } }} />);
    expect(screen.getByText('Loading...')).toBeInTheDocument();
  });

  test('displays repo issues after fetching data', async () => {
    const history = createMemoryHistory();
    const { container } = render(
      <Router history={history}>
        <RepoIssues match={{ params: { id: 'conors-id', repo: 'ironoc-testing' } }} />
      </Router>
    );

    // Wait for the component to finish loading
    await waitFor(() => {
      expect(screen.queryByText('Loading...')).not.toBeInTheDocument();
    });

    expect(screen.queryByText('conors-id')).toBeInTheDocument();
    expect(screen.queryByText('ironoc-testing')).toBeInTheDocument();

    // Check that repo issues are displayed
    mockRepoIssues.forEach(issue => {
      expect(screen.getByText(issue.title)).toBeInTheDocument();
      expect(screen.getByText(issue.body)).toBeInTheDocument();
      expect(screen.getByText(issue.number)).toBeInTheDocument();
    });

    // Verify that the component does not show the loading spinner
    expect(container.querySelector('.LoadingSpinner')).not.toBeInTheDocument();
  });
});

describe('LoadingSpinner', () => {
  test('renders LoadingSpinner component correctly', () => {
    // Render the component
    const { getByRole, getByText } = render(<LoadingSpinner />);

    // Check if the button is present and disabled
    const button = getByRole('button');
    expect(button).toBeInTheDocument();
    expect(button).toBeDisabled();

    // Check if the spinner is present
    const spinner = getByRole('button');
    expect(spinner).toBeInTheDocument();

    // Check if the button contains the text "Loading..."
    const loadingText = getByText('Loading...');
    expect(loadingText).toBeInTheDocument();
  });
});

describe('AppNavBar', () => {
  test('renders AppNavBar component correctly', () => {
    // Render the component
    render(<AppNavBar />);

    // Check if the Navbar is present
    const navbar = screen.getByRole('navigation');
    expect(navbar).toBeInTheDocument();

    // Check if the logo image is present
    const logo = screen.getByAltText('');
    expect(logo).toBeInTheDocument();
    expect(logo).toHaveAttribute('src', 'robot-logo.png');

    // Check if the "Brews" dropdown is present
    const brewsDropdown = screen.getByText('Brews');
    expect(brewsDropdown).toBeInTheDocument();

    // Check if the "Portfolio" dropdown is present
    const portfolioDropdown = screen.getByText('Portfolio');
    expect(portfolioDropdown).toBeInTheDocument();

    // Check if the "About" dropdown is present
    const aboutDropdown = screen.getByText('About');
    expect(aboutDropdown).toBeInTheDocument();

    // Check if the "GitHub PM" dropdown is present
    const githubPMDropdown = screen.getByText('GitHub PM');
    expect(githubPMDropdown).toBeInTheDocument();

    // Check if the "GitHub API" dropdown is present
    const githubAPIDropdown = screen.getByText('GitHub API');
    expect(githubAPIDropdown).toBeInTheDocument();

    // Check if the "GitHub Projects" dropdown is present
    const githubProjectsDropdown = screen.getByText('GitHub Projects');
    expect(githubProjectsDropdown).toBeInTheDocument();

    // Check if the "GitHub Projects" dropdown is present
    const donateDropdown = screen.getByText('Charity Options');
    expect(donateDropdown).toBeInTheDocument();

    // Check if the "Home" link is present
    const homeLink = screen.getByText('Home');
    expect(homeLink).toBeInTheDocument();
    expect(homeLink.closest('a')).toHaveAttribute('href', '/');
  });
});

describe('App Component Routing', () => {
  test("sends the user to about", () => {
    const history = createMemoryHistory({ initialEntries: ["/about"] });
    render(
      <Router history={history}>
        <App />
      </Router>
    );
    expect(history.location.pathname).toBe("/about");
  });
});

describe('Donate Component', () => {
    test('renders without crashing', () => {
        render(<Donate items={donateItems} />);
        expect(screen.getByText('The Jack and Jill Children’s Foundation')).toBeInTheDocument();
        expect(screen.getByText('Vision Ireland, the new name for NCBI')).toBeInTheDocument();
    });

    test('renders all carousel items', () => {
        render(<Donate items={donateItems} />);
        donateItems.forEach(item => {
            expect(screen.getByText(item.name)).toBeInTheDocument();
            expect(screen.getByText(new RegExp(`Founded in ${item.founded}`))).toBeInTheDocument();
            expect(screen.getByText(item.phone)).toBeInTheDocument();
            expect(screen.getByText(new RegExp(item.link))).toBeInTheDocument();
        });
    });

    test('renders images with correct alt text', () => {
        render(<Donate items={donateItems} />);
        donateItems.forEach(item => {
            expect(screen.getByAltText(item.alt)).toBeInTheDocument();
        });
    });
});

describe('Footer Component', () => {
  beforeEach(() => {
    // Mock the fetch API
      global.fetch = jest.fn(() =>
        Promise.resolve({
          ok: true,
          text: () => Promise.resolve('2.2.0'),
        })
      );
  });

  afterEach(() => {
    fetch.mockClear();
  });

  test('renders without crashing', () => {
    render(<Footer />);
    expect(screen.getByText(/© 2025 by Conor Heffron/)).toBeInTheDocument();
  });

  test('initial state is set correctly', () => {
    const { container } = render(<Footer />);
    const footerText = container.querySelector('.ft');
    expect(footerText).toHaveTextContent('© 2025 by Conor Heffron |');
  });

  test('fetches and displays the version', async () => {
    render(<Footer />);
    await waitFor(() => expect(fetch).toHaveBeenCalledTimes(1));
    await waitFor(() => expect(screen.getByText('© 2025 by Conor Heffron | 2.2.0')).toBeInTheDocument());
  });

  test('handles fetch error', async () => {
    fetch.mockImplementationOnce(() =>
      Promise.reject(new Error('Network response was not ok'))
    );

    render(<Footer />);
    await waitFor(() => expect(fetch).toHaveBeenCalledTimes(1));
    await waitFor(() => expect(screen.getByText('© 2025 by Conor Heffron |')).toBeInTheDocument());
  });
});
