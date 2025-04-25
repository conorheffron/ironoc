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

//    test('handles input change', () => {
//        render(
//            <MemoryRouter>
//                <RepoIssues />
//            </MemoryRouter>
//        );
//
//        const input = screen.getByPlaceholderText(/Enter Project Name/i);
//        fireEvent.change(input, { target: { value: 'newRepo' } });
//
//        expect(input.value).toBe('newRepo');
//    });

//    test('navigates on form submission', () => {
//        render(
//            <MemoryRouter>
//                <RepoIssues />
//            </MemoryRouter>
//        );
//
//        const input = screen.getByPlaceholderText(/Enter Project Name/i);
//        const button = screen.getByText(/Search Issues/i);
//
//        fireEvent.change(input, { target: { value: 'newRepo' } });
//        fireEvent.click(button);
//
//        expect(mockNavigate).toHaveBeenCalledWith('/issues/testUser/newRepo', {
//            state: {
//                id: 'testUser',
//                repo: 'newRepo',
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
//        await waitFor(() => expect(fetch).toHaveBeenCalledTimes(1));
//
//        expect(screen.getByText(/Issue No./i)).toBeInTheDocument();
//        expect(screen.getByText(/Title/i)).toBeInTheDocument();
//        expect(screen.getByText(/Description/i)).toBeInTheDocument();
//    });
});
