import React from 'react';
import { render, screen, act, waitFor, fireEvent } from '@testing-library/react';
import '@testing-library/jest-dom';
import Home from './components/Home';
import axios from 'axios';
import App from './App';
import CoffeeCarousel from './components/CoffeeCarousel';
import CoffeeHome from './components/CoffeeHome';
import NotFound from './components/NotFound';
import { createMemoryHistory } from 'history';
import { Router } from 'react-router-dom';
import RepoDetails from './components/RepoDetails';
import RepoIssues from './components/RepoIssues';

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
]

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
      })
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
      })
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
