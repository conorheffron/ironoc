import React from 'react';
import { render, screen, fireEvent, waitFor } from '@testing-library/react';
import { MemoryRouter } from 'react-router';
import RepoDetails from '../RepoDetails';

let mockNavigate = jest.fn();
let mockParams = { id: 'conorheffron' };

jest.mock('react-router', () => ({
    ...jest.requireActual('react-router'),
    useParams: () => mockParams,
    useNavigate: () => mockNavigate,
}));

describe('RepoDetails Component', () => {
    beforeEach(() => {
        mockNavigate = jest.fn();
        // Default to conorheffron, can be changed per test
        mockParams = { id: 'conorheffron' };
        global.fetch = jest.fn(() =>
            Promise.resolve({
                json: () => Promise.resolve([]),
            })
        );
    });

    afterEach(() => {
        jest.clearAllMocks();
        delete global.fetch;
    });

    test('renders loading spinner initially', () => {
        render(
            <MemoryRouter>
                <RepoDetails />
            </MemoryRouter>
        );
        expect(screen.getByText(/loading/i)).toBeInTheDocument();
    });

    test('fetches and displays repo details isConor=True', async () => {
        global.fetch = jest.fn(() =>
            Promise.resolve({
                json: () =>
                    Promise.resolve([
                        {
                            name: 'ironoc',
                            repoUrl: 'https://github.com/conorheffron/ironoc',
                            fullName: 'conorheffron/ironoc',
                            description: 'Ironoc framework',
                            appHome: 'https://ironoc.com',
                            topics: 'nodejs, serverless',
                            issueCount: 37,
                        },
                    ]),
            })
        );
        render(
            <MemoryRouter>
                <RepoDetails />
            </MemoryRouter>
        );
        await waitFor(() => expect(fetch).toHaveBeenCalledTimes(1));
        expect(screen.getByText('conorheffron/ironoc')).toBeInTheDocument();
        expect(screen.getByText('Ironoc framework')).toBeInTheDocument();
        expect(screen.getByText('nodejs, serverless')).toBeInTheDocument();

        expect(screen.queryByRole('columnheader', { name: /Issues Count/i })).toBeInTheDocument();
        expect(screen.getByText('37')).toBeInTheDocument();
    });

    test('handles input change', async () => {
        global.fetch = jest.fn(() =>
            Promise.resolve({
                json: () =>
                    Promise.resolve([
                        {
                            name: 'ironoc-db',
                            repoUrl: 'https://github.com/conorheffron/ironoc-db',
                            fullName: 'conorheffron/ironoc-db',
                            description: 'Ironoc database',
                            appHome: 'https://ironocdb.com',
                            topics: 'database, nodejs',
                            issueCount: 1,
                        },
                    ]),
            })
        );
        render(
            <MemoryRouter>
                <RepoDetails />
            </MemoryRouter>
        );
        await waitFor(() => expect(screen.queryByText(/loading/i)).not.toBeInTheDocument());

        const input = screen.getByPlaceholderText(/Enter GitHub User ID/i);
        fireEvent.change(input, { target: { value: 'newUser' } });
        expect(input.value).toBe('newUser');
    });

    test('displays table headers correctly for ronocdev when isConor=false and hasRepoWithIssues=true', async () => {
        mockParams = { id: 'ronocdev' };
        global.fetch = jest.fn(() =>
            Promise.resolve({
                json: () =>
                    Promise.resolve([
                        {
                            name: 'ronoc-packages',
                            repoUrl: 'https://github.com/ronocdev/ronoc-packages',
                            fullName: 'ronocdev/ronoc-packages',
                            description: 'Ronoc NPM packages',
                            appHome: 'https://ronoc.dev',
                            topics: 'npm, packages',
                            issueCount: 146,
                        },
                    ]),
            })
        );
        render(
            <MemoryRouter>
                <RepoDetails />
            </MemoryRouter>
        );
        await waitFor(() => expect(screen.queryByText(/loading/i)).not.toBeInTheDocument());

        expect(screen.getByRole('columnheader', { name: /Repository/i })).toBeInTheDocument();
        expect(screen.getByRole('columnheader', { name: /Description/i })).toBeInTheDocument();
        expect(screen.getByRole('columnheader', { name: /App URL/i })).toBeInTheDocument();
        expect(screen.getByRole('columnheader', { name: /Topics/i })).toBeInTheDocument();
        expect(screen.getByRole('columnheader', { name: /Actions/i })).toBeInTheDocument();

        expect(screen.getByText('ronocdev/ronoc-packages')).toBeInTheDocument();
        expect(screen.getByText('Ronoc NPM packages')).toBeInTheDocument();
        expect(screen.getByText('npm, packages')).toBeInTheDocument();

        expect(screen.queryByRole('columnheader', { name: /Issues Count/i })).not.toBeInTheDocument();
        expect(screen.queryByText('146')).not.toBeInTheDocument();
    });

    test('displays table headers correctly for elastictester when isConor=false and hasRepoWithIssues=false', async () => {
        mockParams = { id: 'elastictester' };
        global.fetch = jest.fn(() =>
            Promise.resolve({
                json: () =>
                    Promise.resolve([
                        {
                            name: 'elastic-tester',
                            repoUrl: 'https://github.com/elastictester/elastic-tester',
                            fullName: 'elastictester/elastic-tester',
                            description: 'Elastic search tester',
                            appHome: 'https://elastictester.io',
                            topics: 'elastic, test',
                        },
                    ]),
            })
        );
        render(
            <MemoryRouter>
                <RepoDetails />
            </MemoryRouter>
        );
        await waitFor(() => expect(screen.queryByText(/loading/i)).not.toBeInTheDocument());
        await waitFor(() => expect(fetch).toHaveBeenCalledTimes(1));
        expect(screen.getByRole('columnheader', { name: /Repository/i })).toBeInTheDocument();
        expect(screen.getByRole('columnheader', { name: /Description/i })).toBeInTheDocument();
        expect(screen.getByRole('columnheader', { name: /App URL/i })).toBeInTheDocument();
        expect(screen.getByRole('columnheader', { name: /Topics/i })).toBeInTheDocument();
        expect(screen.queryByRole('columnheader', { name: /Actions/i })).toBeInTheDocument();

        expect(screen.queryByRole('columnheader', { name: /Issues Count/i })).not.toBeInTheDocument();
    });

    test('navigates on form submission', async () => {
        global.fetch = jest.fn(() =>
            Promise.resolve({
                json: () =>
                    Promise.resolve([
                        {
                            name: 'ironoc-db',
                            repoUrl: 'https://github.com/conorheffron/ironoc-db',
                            fullName: 'conorheffron/ironoc-db',
                            description: 'Ironoc database',
                            appHome: 'https://ironocdb.com',
                            topics: 'database, nodejs',
                            issueCount: 1,
                        },
                    ]),
            })
        );
        render(
            <MemoryRouter>
                <RepoDetails />
            </MemoryRouter>
        );
        await waitFor(() => expect(screen.queryByText(/loading/i)).not.toBeInTheDocument());

        const input = screen.getByRole('textbox', { name: /Enter GitHub User ID/i });
        const button = screen.getByRole('button', { name: /Search Projects/i });
        fireEvent.change(input, { target: { value: 'newUser' } });
        fireEvent.click(button);
        expect(mockNavigate).toHaveBeenCalledWith('/projects/newUser', {
            replace: true,
            state: { id: 'newUser' },
        });
    });
});
