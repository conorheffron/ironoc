import React from 'react';
import { render, screen, fireEvent, waitFor } from '@testing-library/react';
import { MemoryRouter } from 'react-router';
import RepoIssues from '../RepoIssues';

jest.mock('react-router', () => ({
    ...jest.requireActual('react-router'),
    useParams: jest.fn(),
    useNavigate: jest.fn(),
}));

describe('RepoIssues Component', () => {
    const mockNavigate = jest.fn();
    const mockParams = { id: 'testUser', repo: 'testRepo' };

    beforeEach(() => {
        require('react-router').useNavigate.mockReturnValue(mockNavigate);
        require('react-router').useParams.mockReturnValue(mockParams);

        global.fetch = jest.fn(() =>
            Promise.resolve({
                json: () =>
                    Promise.resolve([
                        {
                            number: 1,
                            title: 'Test Issue Title',
                            body: 'Test issue body description',
                        },
                    ]),
            })
        );
    });

    afterEach(() => {
        jest.clearAllMocks();
    });

    test('renders loading spinner initially', () => {
        render(
            <MemoryRouter>
                <RepoIssues />
            </MemoryRouter>
        );

        expect(screen.getByText(/loading/i)).toBeInTheDocument();
    });

    test('fetches and displays repo issues', async () => {
        render(
            <MemoryRouter>
                <RepoIssues />
            </MemoryRouter>
        );

        await waitFor(() => expect(fetch).toHaveBeenCalledTimes(1));

        expect(screen.getByText(/Test Issue Title/i)).toBeInTheDocument();
        expect(screen.getByText(/Test issue body description/i)).toBeInTheDocument();
        expect(screen.getByText(/1/i)).toBeInTheDocument();
    });

    test('handles input change', async () => {
        render(
            <MemoryRouter>
                <RepoIssues />
            </MemoryRouter>
        );

        await waitFor(() => expect(fetch).toHaveBeenCalledTimes(1));

        const input = screen.getByPlaceholderText(/Enter Project Name/i);
        fireEvent.change(input, { target: { value: 'newRepo' } });

        expect(input.value).toBe('newRepo');
    });

    test('navigates on form submission', async () => {
        render(
            <MemoryRouter>
                <RepoIssues />
            </MemoryRouter>
        );

        await waitFor(() => expect(fetch).toHaveBeenCalledTimes(1));

        const input = screen.getByPlaceholderText(/Enter Project Name/i);
        const button = screen.getByText(/Search Issues/i);

        fireEvent.change(input, { target: { value: 'newRepo' } });
        fireEvent.click(button);

        expect(mockNavigate).toHaveBeenCalledWith('/issues/testUser/newRepo', {
            replace: true,
            state: {
                id: 'testUser',
                repo: 'newRepo',
            },
        });
    });

    test('displays table headers correctly', async () => {
        render(
            <MemoryRouter>
                <RepoIssues />
            </MemoryRouter>
        );

        // Wait for the loading spinner to disappear
        await waitFor(() => expect(screen.queryByText(/loading/i)).not.toBeInTheDocument());

        // Use getByRole for specific headers
        const issueNoHeader = screen.getByRole('columnheader', { name: /Issue No./i });
        const titleHeader = screen.getByRole('columnheader', { name: /Title/i });
        const descriptionHeader = screen.getByRole('columnheader', { name: /Description/i });

        // Assert that headers are in the document
        expect(issueNoHeader).toBeInTheDocument();
        expect(titleHeader).toBeInTheDocument();
        expect(descriptionHeader).toBeInTheDocument();
    });
});
