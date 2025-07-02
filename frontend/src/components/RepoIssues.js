import React, { useState, useEffect, useMemo } from 'react';
import { Container, InputGroup, Form, Button } from 'react-bootstrap';
import AppNavbar from '../AppNavbar';
import LoadingSpinner from '../LoadingSpinner';
import { useParams, useNavigate } from 'react-router';
import {
  MaterialReactTable,
  useMaterialReactTable,
  type MRT_ColumnDef,
} from 'material-react-table';
import { darken, lighten, useTheme } from '@mui/material';

const issueTags = [
  'enhancement',
  'bug',
  'java',
  'java-be',
  'javascript',
  'release',
  'ui',
  'frontend',
  'infra',
  'dependencies',
  'python',
  'python-code',
];

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

  const handleChange = (event) => setValue(event.target.value);

  const onSubmit = (event) => {
    event.preventDefault();
    navigate(`/issues/${id}/${value}`, {
      replace: true,
      state: {
        id: id,
        repo: value,
      },
    });
    navigate(0);
  };

  const projectLink = `/projects/${id}/`;

  // MaterialReactTable columns
  const columns = useMemo(
    () => [
      {
        accessorKey: 'number',
        header: 'Issue No.',
        Cell: ({ cell }) => {
          const issueLink = `https://github.com/${id}/${repo}/issues/${cell.getValue()}/`;
          return (
            <a href={issueLink} target="_blank" rel="noreferrer">
              <b>
                <i>{cell.getValue()}</i>
              </b>
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
    ],
    [id, repo]
  );

  const theme = useTheme();

  //light or dark green
  const baseBackgroundColor =
    theme.palette.mode === 'dark'
      ? 'rgba(3, 44, 43, 1)'
      : 'rgba(244, 255, 233, 1)';

  const table = useMaterialReactTable({
    columns,
    data: repoIssueList,
    enableFacetedValues: true,
    enableStickyHeader: true,
    initialState: { showColumnFilters: true },
    muiTablePaperProps: {
      elevation: 0,
      sx: {
        borderRadius: '0',
      },
    },
    muiTableBodyProps: {
      sx: (theme) => ({
        '& tr:nth-of-type(odd):not([data-selected="true"]):not([data-pinned="true"]) > td': {
          backgroundColor: darken(baseBackgroundColor, 0.1),
        },
        '& tr:nth-of-type(odd):not([data-selected="true"]):not([data-pinned="true"]):hover > td': {
          backgroundColor: darken(baseBackgroundColor, 0.2),
        },
        '& tr:nth-of-type(even):not([data-selected="true"]):not([data-pinned="true"]) > td': {
          backgroundColor: lighten(baseBackgroundColor, 0.1),
        },
        '& tr:nth-of-type(even):not([data-selected="true"]):not([data-pinned="true"]):hover > td': {
          backgroundColor: darken(baseBackgroundColor, 0.2),
        },
      }),
    },
    mrtTheme: (theme) => ({
      baseBackgroundColor: baseBackgroundColor,
      draggingBorderColor: theme.palette.secondary.main,
    }),
  });

  if (isLoading) {
    return (
      <div className="App">
        <AppNavbar />
        <Container>
          <br />
          <br />
          <br />
          <LoadingSpinner />
        </Container>
      </div>
    );
  }

  return (
    <div>
      <AppNavbar />
      <br />
      <br />
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
          <Button
            variant="outline-secondary"
            id="button-addon2"
            onClick={onSubmit}
          >
            Search Issues
          </Button>
        </InputGroup>
        <h3 className="table-headers">
          Issues for project <b>{repo}</b> and account{' '}
          <a href={projectLink}>
            <b>{id}</b>
          </a>
        </h3>
        <MaterialReactTable table={table} />
      </Container>
    </div>
  );
};

export default RepoIssues;
