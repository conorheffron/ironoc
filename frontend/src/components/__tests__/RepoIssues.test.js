import { render, screen, waitFor } from '@testing-library/react';
import App from '../../App';
import { createMemoryHistory } from 'history';
import { Router } from 'react-router-dom';
import RepoIssues from '../RepoIssues';
import LoadingSpinner from '../../LoadingSpinner';

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
