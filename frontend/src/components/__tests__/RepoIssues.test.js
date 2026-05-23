import React from 'react';
import { render, screen, fireEvent, waitFor } from '@testing-library/react';
import RepoIssues from '../RepoIssues';

// Track useMaterialReactTable call opts for assertions (must start with 'mock' for Jest hoisting)
const mockUseMRTOpts = [];

// Mock react-router
jest.mock('react-router', () => ({
  useParams: jest.fn(),
  useNavigate: jest.fn(),
}));

// Mock react-bootstrap
jest.mock('react-bootstrap', () => ({
  Container: ({ children, fluid, ...props }) => (
    <div data-testid="container" data-fluid={fluid ? 'true' : undefined} {...props}>
      {children}
    </div>
  ),
  InputGroup: ({ children, ...props }) => <div data-testid="input-group" {...props}>{children}</div>,
  Form: {
    Control: ({ ...props }) => <input data-testid="form-control" {...props} />,
  },
  Button: ({ children, ...props }) => <button {...props}>{children}</button>,
}));

// Mock AppNavbar and LoadingSpinner
jest.mock('../../AppNavbar', () => () => <div data-testid="navbar">Navbar</div>);
jest.mock('../../LoadingSpinner', () => () => <div data-testid="spinner">Loading...</div>);

// Mock MaterialReactTable and useMaterialReactTable.
// useMaterialReactTable is a plain function (not jest.fn) so React 19 concurrent mode
// commits re-renders correctly. Calls are tracked via mockUseMRTOpts for assertions.
// columnFilters from initialState are applied to table.data so MaterialReactTable
// only receives the filtered rows, allowing DOM-level assertions on visibility.
jest.mock('material-react-table', () => ({
  MaterialReactTable: ({ table }) => (
    <div data-testid="mrt-table">
      {(table?.data ?? []).map((issue, idx) => (
        <div key={idx} data-testid="mrt-row">{issue.title}</div>
      ))}
    </div>
  ),
  useMaterialReactTable: (opts) => {
    mockUseMRTOpts.push(opts);
    const filters = opts?.initialState?.columnFilters ?? [];
    let data = opts?.data ?? [];
    filters.forEach((filter) => {
      data = data.filter((row) => {
        const val = row[filter.id];
        return Array.isArray(filter.value) ? filter.value.includes(val) : val === filter.value;
      });
    });
    return { ...opts, data };
  },
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
  const mockIssuesResponse = [];

  beforeEach(() => {
    mockUseMRTOpts.length = 0;
    jest.clearAllMocks();
    useNavigate.mockReturnValue(mockNavigate);
    global.fetch = jest.fn(() =>
      Promise.resolve({
        json: () => Promise.resolve(mockIssuesResponse),
      })
    );
    window.fetch = global.fetch;
  });

  it('shows loading spinner and navbar initially', () => {
    useParams.mockReturnValue({ id: 'user', repo: 'repo' });
    global.fetch = jest.fn(() => new Promise(() => {}));
    window.fetch = global.fetch;
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
    window.fetch = global.fetch;
    render(<RepoIssues />);
    await waitFor(() =>
      expect(screen.queryByTestId('spinner')).not.toBeInTheDocument()
    );
    expect(screen.getByTestId('mrt-table')).toBeInTheDocument();
    expect(screen.getByText('Test Issue')).toBeInTheDocument();
  });

  it('defaults to open issues while hiding state and description columns', async () => {
    useParams.mockReturnValue({ id: 'user', repo: 'repo' });
    render(<RepoIssues />);
    await waitFor(() =>
      expect(screen.queryByTestId('spinner')).not.toBeInTheDocument()
    );

    expect(mockUseMRTOpts).toContainEqual(
      expect.objectContaining({
        initialState: expect.objectContaining({
          showColumnFilters: true,
          columnFilters: [{ id: 'state', value: ['open'] }],
          columnVisibility: {
            state: false,
            body: false,
          },
        }),
      })
    );
  });

  it('renders open issues and excludes closed issues by default', async () => {
    useParams.mockReturnValue({ id: 'user', repo: 'repo' });
    global.fetch = jest.fn(() =>
      Promise.resolve({
        json: () => Promise.resolve([
          { number: 1, state: 'open', labels: [], title: 'Open Issue', body: '' },
          { number: 2, state: 'closed', labels: [], title: 'Closed Issue', body: '' },
        ]),
      })
    );
    window.fetch = global.fetch;
    render(<RepoIssues />);
    await waitFor(() =>
      expect(screen.getByText('Open Issue')).toBeInTheDocument()
    );
    expect(screen.queryByText('Closed Issue')).not.toBeInTheDocument();
  });

  it('navigates on form submit', async () => {
    useParams.mockReturnValue({ id: 'user', repo: 'repo' });
    global.fetch = jest.fn(() => Promise.resolve({ json: () => Promise.resolve([]) }));
    window.fetch = global.fetch;
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
});
