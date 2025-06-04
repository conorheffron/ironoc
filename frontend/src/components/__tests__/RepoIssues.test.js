import React from 'react';
import { render, screen, fireEvent, waitFor } from '@testing-library/react';
import RepoIssues from '../RepoIssues';

// Mock react-router
jest.mock('react-router', () => ({
  useParams: jest.fn(),
  useNavigate: jest.fn(),
}));

// Mock react-bootstrap
jest.mock('react-bootstrap', () => ({
  Container: ({ children, ...props }) => <div data-testid="container" {...props}>{children}</div>,
  InputGroup: ({ children, ...props }) => <div data-testid="input-group" {...props}>{children}</div>,
  Form: {
    Control: ({ ...props }) => <input data-testid="form-control" {...props} />,
  },
  Button: ({ children, ...props }) => <button {...props}>{children}</button>,
}));

// Mock AppNavbar and LoadingSpinner
jest.mock('../../AppNavbar', () => () => <div data-testid="navbar">Navbar</div>);
jest.mock('../../LoadingSpinner', () => () => <div data-testid="spinner">Loading...</div>);

// Mock MaterialReactTable and useMaterialReactTable
jest.mock('material-react-table', () => ({
  MaterialReactTable: ({ table }) => (
    <div data-testid="mrt-table">
      {table && table.data && table.data.map((issue, idx) => (
        <div key={idx} data-testid="mrt-row">{issue.title}</div>
      ))}
    </div>
  ),
  useMaterialReactTable: jest.fn((opts) => opts),
}));

// Mock @mui/material theme functions
jest.mock('@mui/material', () => ({
  useTheme: () => ({
    palette: {
      mode: 'light',
      secondary: { main: '#00ff00' }
    }
  }),
  darken: (color, amount) => color + '-darken' + amount,
  lighten: (color, amount) => color + '-lighten' + amount,
}));

import { useParams, useNavigate } from 'react-router';

describe('RepoIssues', () => {
  const mockNavigate = jest.fn();

  beforeEach(() => {
    jest.clearAllMocks();
    useNavigate.mockReturnValue(mockNavigate);
  });

  it('shows loading spinner and navbar initially', () => {
    useParams.mockReturnValue({ id: 'user', repo: 'repo' });
    render(<RepoIssues />);
    expect(screen.getByTestId('navbar')).toBeInTheDocument();
    expect(screen.getByTestId('spinner')).toBeInTheDocument();
  });

  it('fetches and displays issues in the table after loading', async () => {
    useParams.mockReturnValue({ id: 'user', repo: 'repo' });
    global.fetch = jest.fn(() =>
      Promise.resolve({
        json: () => Promise.resolve([
          {
            number: 1,
            state: 'open',
            labels: ['bug'],
            title: 'Test Issue',
            body: 'Body here',
          },
        ]),
      })
    );
    render(<RepoIssues />);
    await waitFor(() =>
      expect(screen.queryByTestId('spinner')).not.toBeInTheDocument()
    );
    expect(screen.getByTestId('mrt-table')).toBeInTheDocument();
  });

  it('renders correct table columns and project/account info', async () => {
    useParams.mockReturnValue({ id: 'octocat', repo: 'hello-world' });
    global.fetch = jest.fn(() =>
      Promise.resolve({
        json: () => Promise.resolve([
          {
            number: 2,
            state: 'closed',
            labels: ['enhancement'],
            title: 'Second Issue',
            body: 'Body 2',
          },
        ]),
      })
    );
    render(<RepoIssues />);
    await waitFor(() =>
      expect(screen.queryByTestId('spinner')).not.toBeInTheDocument()
    );
    expect(screen.getByText(/Issues for project/i)).toBeInTheDocument();
    expect(screen.getByText('hello-world')).toBeInTheDocument();
    expect(screen.getByText('octocat')).toBeInTheDocument();
  });

  it('updates input value and allows search for another repo', async () => {
    useParams.mockReturnValue({ id: 'user', repo: 'repo' });
    global.fetch = jest.fn(() => Promise.resolve({ json: () => Promise.resolve([]) }));
    render(<RepoIssues />);
    await waitFor(() => expect(screen.queryByTestId('spinner')).not.toBeInTheDocument());
    const input = screen.getByTestId('form-control');
    fireEvent.change(input, { target: { value: 'newrepo' } });
    expect(input.value).toBe('newrepo');
  });

  it('navigates on form submit', async () => {
    useParams.mockReturnValue({ id: 'user', repo: 'repo' });
    global.fetch = jest.fn(() => Promise.resolve({ json: () => Promise.resolve([]) }));
    render(<RepoIssues />);
    await waitFor(() => expect(screen.queryByTestId('spinner')).not.toBeInTheDocument());
    const input = screen.getByTestId('form-control');
    const button = screen.getByText(/Search Issues/i);
    fireEvent.change(input, { target: { value: 'newrepo' } });
    fireEvent.click(button);
    expect(mockNavigate).toHaveBeenCalledWith('/issues/user/newrepo', {
      replace: true,
      state: {
        id: 'user',
        repo: 'newrepo',
      },
    });
    expect(mockNavigate).toHaveBeenCalledWith(0);
  });

  it('renders table but no rows if API returns empty list', async () => {
    useParams.mockReturnValue({ id: 'user', repo: 'repo' });
    global.fetch = jest.fn(() => Promise.resolve({ json: () => Promise.resolve([]) }));
    render(<RepoIssues />);
    await waitFor(() => expect(screen.queryByTestId('spinner')).not.toBeInTheDocument());
    expect(screen.getByTestId('mrt-table')).toBeInTheDocument();
    expect(screen.queryAllByTestId('mrt-row').length).toBe(0);
  });

  it('handles missing repo or id without crashing', async () => {
    useParams.mockReturnValue({}); // No id or repo
    global.fetch = jest.fn(() => Promise.resolve({ json: () => Promise.resolve([]) }));
    render(<RepoIssues />);
    await waitFor(() => expect(screen.queryByTestId('spinner')).not.toBeInTheDocument());
    expect(screen.getByTestId('mrt-table')).toBeInTheDocument();
  });
});
