import React, { Component } from 'react';
import { Button, Container, InputGroup, Table } from 'reactstrap';
import '.././App.css';
import Form from 'react-bootstrap/Form';
import AppNavbar from '.././AppNavbar';
import Footer from '.././Footer';
import LoadingSpinner from '.././LoadingSpinner';

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
        const { match: { params: { id } }, history } = this.props;
        history.push(`/issues/${id}/${value}`, {
            id: id,
            repo: value
        });
    }

    async componentDidMount() {
        const { match: { params: { id, repo } } } = this.props;
        const response = await fetch(`/api/get-repo-issue/${id}/${repo}/`);
        const body = await response.json();
        this.setState({ repoIssueList: body, isLoading: false });
    }

    render() {
        const { repoIssueList = [], isLoading = true, value = '' } = this.state;
        const { match: { params: { id = '', repo = '' } } } = this.props;

        if (isLoading) {
            return (
                <div className="App">
                    <AppNavbar />
                    <Container>
                        <br /><br /><br />
                        <LoadingSpinner />
                    </Container>
                    <Footer />
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
                <Footer />
            </div>
        );
    }
}

export default RepoIssues;
