import React from 'react';
import { render, screen, fireEvent, waitFor } from '@testing-library/react';
import { MemoryRouter } from 'react-router';
import RepoDetails from '../RepoDetails';

jest.mock('react-router', () => ({
    ...jest.requireActual('react-router'),
    useParams: jest.fn(),
    useNavigate: jest.fn(),
}));

describe('RepoDetails Component', () => {
    const mockNavigate = jest.fn();
    const mockParams = { id: 'testUser' };

    beforeEach(() => {
        require('react-router').useNavigate.mockReturnValue(mockNavigate);
        require('react-router').useParams.mockReturnValue(mockParams);

        global.fetch = jest.fn(() =>
            Promise.resolve({
                json: () =>
                    Promise.resolve([
                        {
                            name: 'testRepo',
                            repoUrl: 'https://github.com/testRepo',
                            fullName: 'testUser/testRepo',
                            description: 'Test repository description',
                            appHome: 'https://example.com',
                            topics: 'topic1, topic2',
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
                <RepoDetails />
            </MemoryRouter>
        );

        expect(screen.getByText(/loading/i)).toBeInTheDocument();
    });

    test('fetches and displays repo details', async () => {
        render(
            <MemoryRouter>
                <RepoDetails />
            </MemoryRouter>
        );

        await waitFor(() => expect(fetch).toHaveBeenCalledTimes(1));

        expect(screen.getByText('testUser/testRepo')).toBeInTheDocument();
        expect(screen.getByText('Test repository description')).toBeInTheDocument();
        expect(screen.getByText('topic1, topic2')).toBeInTheDocument();
    });


    test('handles input change', async () => {
        render(
            <MemoryRouter>
                <RepoDetails />
            </MemoryRouter>
        );

        // Wait for the loading spinner to disappear
        await waitFor(() => expect(screen.queryByText(/loading/i)).not.toBeInTheDocument());

        const input = screen.getByPlaceholderText(/Enter GitHub User ID/i);
        fireEvent.change(input, { target: { value: 'newUser' } });

        expect(input.value).toBe('newUser');
    });

    test('displays table headers correctly', async () => {
        render(
            <MemoryRouter>
                <RepoDetails />
            </MemoryRouter>
        );

        // Wait for the loading spinner to disappear
        await waitFor(() => expect(screen.queryByText(/loading/i)).not.toBeInTheDocument());

        // Assert that table headers are displayed correctly using roles
        const repositoryHeader = screen.getByRole('columnheader', { name: /Repository/i });
        const descriptionHeader = screen.getByRole('columnheader', { name: /Description/i });
        const appUrlHeader = screen.getByRole('columnheader', { name: /App URL/i });
        const topicsHeader = screen.getByRole('columnheader', { name: /Topics/i });
        const issuesCount = screen.getByRole('columnheader', { name: /Issues Count/i });
        const actionsHeader = screen.getByRole('columnheader', { name: /Actions/i });

        expect(repositoryHeader).toBeInTheDocument();
        expect(descriptionHeader).toBeInTheDocument();
        expect(appUrlHeader).toBeInTheDocument();
        expect(topicsHeader).toBeInTheDocument();
        expect(issuesCount).toBeInTheDocument();
        expect(actionsHeader).toBeInTheDocument();
    });

    test('navigates on form submission', async () => { // Make the function async
        render(
            <MemoryRouter>
                <RepoDetails />
            </MemoryRouter>
        );

        // Wait for the loading spinner to disappear
        await waitFor(() => expect(screen.queryByText(/loading/i)).not.toBeInTheDocument());

        // Use getByRole to target the input field and button
        const input = screen.getByRole('textbox', { name: /Enter GitHub User ID/i });
        const button = screen.getByRole('button', { name: /Search Projects/i });

        // Simulate user input and form submission
        fireEvent.change(input, { target: { value: 'newUser' } });
        fireEvent.click(button);

        // Assert that navigation was triggered with the correct arguments
        expect(mockNavigate).toHaveBeenCalledWith('/projects/newUser', {
            replace: true,
            state: { id: 'newUser' },
        });
    });
});
