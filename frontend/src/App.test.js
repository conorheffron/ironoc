import React from 'react';
import { render, screen, waitFor } from '@testing-library/react';
import '@testing-library/jest-dom';
import Home from './components/Home';
import App from './App';
import { createMemoryHistory } from 'history';
import { Router } from 'react-router-dom';
import LoadingSpinner from './LoadingSpinner';
import AppNavBar from './AppNavbar';
import About from './components/About';
import Footer from './Footer';

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
