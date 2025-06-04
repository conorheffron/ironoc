import React, { useState, useEffect, useMemo } from 'react';
import { Container, InputGroup, Form, Button } from 'react-bootstrap';
import AppNavbar from '../AppNavbar';
import LoadingSpinner from '../LoadingSpinner';
import { useParams, useNavigate } from 'react-router';
import { MaterialReactTable, useMaterialReactTable } from 'material-react-table';

const RepoIssues = () => {
    const params = useParams();
    const navigate = useNavigate();
    const { id = '', repo = '' } = params;

    const [repoIssueList, setRepoIssueList] = useState([]);
    const [isLoading, setIsLoading] = useState(true);
    const [value, setValue] = useState('');

    useEffect(() => {
        const fetchIssues = async () => {
            if (id && repo) {
                const response = await fetch(`/api/get-repo-issue/${id}/${repo}/`);
                const body = await response.json();
                setRepoIssueList(body);
            }
            setIsLoading(false);
        };
        fetchIssues();
    }, [id, repo]);

    const handleChange = event => setValue(event.target.value);

    const onSubmit = event => {
        event.preventDefault();
        navigate(`/issues/${id}/${value}`, {
            replace: true,
            state: {
                id: id,
                repo: value
            }
        });
        navigate(0);
    };

    const projectLink = `/projects/${id}/`;

    const issueTags = ['enhancement', 'bug', 'java', 'javascript', 'release', 'ui', 'frontend', 'infra', 'dependencies']

    // MaterialReactTable columns
    const columns = useMemo(() => [
        {
            accessorKey: 'number',
            header: 'Issue No.',
            Cell: ({ cell }) => {
                const issueLink = `https://github.com/${id}/${repo}/issues/${cell.getValue()}/`;
                return (
                    <a href={issueLink} target="_blank" rel="noreferrer">
                        <b><i>{cell.getValue()}</i></b>
                    </a>
                );
            },
            size: 40,
        },
        {
            accessorKey: 'state',
            header: 'State',
            filterVariant: 'multi-select',
            size: 40,
        },
        {
            accessorKey: 'labels',
            header: 'Labels',
            filterVariant: 'multi-select',
            filterSelectOptions: issueTags,
            Cell: ({ cell }) => <b>{cell.getValue().join(', ')}</b>,
            size: 100,
        },
        {
            accessorKey: 'title',
            header: 'Title',
            size: 150,
            Cell: ({ cell }) => <b>{cell.getValue()}</b>,
        },
        {
            accessorKey: 'body',
            header: 'Description',
            size: 200,
        },
    ], [id, repo]);

    const table = useMaterialReactTable({
        columns,
        data: repoIssueList,
        enableFacetedValues: true,
        enableStickyHeader: true,
        initialState: { showColumnFilters: true },
    });

    if (isLoading) {
        return (
            <div className="App">
                <AppNavbar />
                <Container>
                    <br /><br /><br />
                    <LoadingSpinner />
                </Container>
            </div>
        );
    }

    return (
        <div>
            <AppNavbar /><br /><br />
            <Container fluid>
                <br />
                <InputGroup className="mb-3">
                    <Form.Control
                        placeholder="Enter Project Name... Example: ironoc-db"
                        aria-label="Enter Project Name..."
                        aria-describedby="basic-addon2"
                        type="text"
                        value={value}
                        onChange={handleChange}
                    />
                    <Button color="primary" variant="outline-secondary" id="button-addon2" onClick={onSubmit}>Search Issues</Button>
                </InputGroup>
                <h3 className="table-headers">
                    Issues for project <b>{repo}</b> and account <a href={projectLink}><b>{id}</b></a>
                </h3>
                <MaterialReactTable table={table} />
            </Container>
        </div>
    );
};

export default RepoIssues;
