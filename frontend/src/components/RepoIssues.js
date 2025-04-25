import React, { Component } from 'react';
import { Button, Container, InputGroup, Table } from 'reactstrap';
import '.././App.css';
import Form from 'react-bootstrap/Form';
import AppNavbar from '.././AppNavbar';
import LoadingSpinner from '.././LoadingSpinner';
import { useParams, useNavigate } from 'react-router';

// Helper function to inject `params` and `navigate` into class-based components
function withRouter(Component) {
    return (props) => {
        const params = useParams();
        const navigate = useNavigate();
        return <Component {...props} params={params} navigate={navigate} />;
    };
}

class RepoIssues extends Component {
    constructor(props) {
        super(props);
        this.state = {
            repoIssueList: [],
            isLoading: true,
            value: ''
        };

        this.handleChange = this.handleChange.bind(this);
        this.onSubmit = this.onSubmit.bind(this);
    }

    handleChange(event) {
        this.setState({ value: event.target.value });
    }

    onSubmit(event) {
        event.preventDefault();
        const { value } = this.state;
        const { id } = this.props.params;
        this.props.navigate(`/issues/${id}/${value}`, {
            replace: true,
            state: {
                id: id,
                repo: value
            }
        });
        this.props.navigate(0)
    }

    async componentDidMount() {
        const { id, repo } = this.props.params;
        if (id && repo) {
            const response = await fetch(`/api/get-repo-issue/${id}/${repo}/`);
            const body = await response.json();
            this.setState({ repoIssueList: body, isLoading: false });
        } else {
            this.setState({ isLoading: false });
        }
    }

    render() {
        const { repoIssueList = [], isLoading = true, value = '' } = this.state;
        const { id = '', repo = '' } = this.props.params;

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

        const repoList = repoIssueList.map(issue => {
            const issueLink = `https://github.com/${id}/${repo}/issues/${issue.number}/`;
            return (
                <tr key={issue.number}>
                    <td className="table-info">
                        <a href={issueLink} target="_blank" rel="noreferrer"><b><i>{issue.number}</i></b></a>
                    </td>
                    <td><b>{issue.title}</b></td>
                    <td>{issue.body}</td>
                </tr>
            );
        });

        const projectLink = `/projects/${id}/`;

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
                            onChange={this.handleChange}
                        />
                        <Button color="primary" variant="outline-secondary" id="button-addon2" onClick={this.onSubmit}>Search Issues</Button>
                    </InputGroup>
                    <h3 className="table-headers">Issues for project <b>{repo}</b> and account <a href={projectLink}><b>{id}</b></a></h3>
                    <Table striped hover bordered>
                        <thead>
                            <tr className="table-secondary">
                                <th width="3%">Issue No.</th>
                                <th width="27%">Title</th>
                                <th width="70%">Description</th>
                            </tr>
                        </thead>
                        <tbody>{repoList}</tbody>
                    </Table>
                </Container>
            </div>
        );
    }
}

export default withRouter(RepoIssues);
