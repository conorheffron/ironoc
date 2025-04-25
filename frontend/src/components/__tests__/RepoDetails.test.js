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

//    test('handles input change', () => {
//        render(
//            <MemoryRouter>
//                <RepoDetails />
//            </MemoryRouter>
//        );
//
//        const input = screen.getByPlaceholderText(/Enter GitHub User ID/i);
//        fireEvent.change(input, { target: { value: 'newUser' } });
//
//        expect(input.value).toBe('newUser');
//    });

//    test('navigates on form submission', () => {
//        render(
//            <MemoryRouter>
//                <RepoDetails />
//            </MemoryRouter>
//        );
//
//        const input = screen.getByPlaceholderText(/Enter GitHub User ID/i);
//        const button = screen.getByText(/Search Projects/i);
//
//        fireEvent.change(input, { target: { value: 'newUser' } });
//        fireEvent.click(button);
//
//        expect(mockNavigate).toHaveBeenCalledWith('/projects/newUser', {
//            state: {
//                id: 'newUser',
//            },
//        });
//    });

//    test('displays table headers correctly', async () => {
//        render(
//            <MemoryRouter>
//                <RepoIssues />
//            </MemoryRouter>
//        );
//
//        // Wait for the loading spinner to disappear
//        await waitFor(() => expect(screen.queryByText(/loading/i)).not.toBeInTheDocument());
//
//        // Use getByRole for specific headers
//        const issueNoHeader = screen.getByRole('columnheader', { name: /Issue No./i });
//        const titleHeader = screen.getByRole('columnheader', { name: /Title/i });
//        const descriptionHeader = screen.getByRole('columnheader', { name: /Description/i });
//
//        // Assert that headers are in the document
//        expect(issueNoHeader).toBeInTheDocument();
//        expect(titleHeader).toBeInTheDocument();
//        expect(descriptionHeader).toBeInTheDocument();
//    });
});
