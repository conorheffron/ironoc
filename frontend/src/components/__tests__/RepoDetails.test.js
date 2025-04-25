import { render, screen, waitFor } from '@testing-library/react';
import App from '../../App';
import { createMemoryHistory } from 'history';
import { Router } from 'react-router';
import RepoDetails from '../RepoDetails';
import LoadingSpinner from '../../LoadingSpinner';

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
