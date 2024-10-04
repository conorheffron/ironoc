import React, { Component } from 'react';
import { Button, Container, InputGroup, Table } from 'reactstrap';
import '.././App.css';
import Form from 'react-bootstrap/Form';
import AppNavbar from '.././AppNavbar';

class RepoIssues extends Component {

    constructor(props) {
        super(props);
        this.state = {repoIssueList: [], isLoading: true, value: ''};

        this.handleChange = this.handleChange.bind(this);
        this.onSubmit = this.onSubmit.bind(this);
    }

    handleChange(event) {
        const {issues, isL, value} = this.state;
        this.setState({repoIssueList: issues, isLoading: isL, value: event.target.value});
    }

    onSubmit(event){
        let repo = this.state.value;
        let id = this.props.match.params.id;
        this.props.history.push(`/issues/${id}/${repo}`, {
          id: id,
          repo: repo
        });
    }

    async componentDidMount() {
        const response = await fetch(`/get-repo-issue/${this.props.match.params.id}/${this.props.match.params.repo}/`);
        const body = await response.json();
        console.log(body)
        this.setState({repoIssueList: body, isLoading: false});
    }

    render() {
        const {repoIssueList, isLoading} = this.state;
        if (isLoading) {
            return <p>Loading...</p>;
        }
        const repoList = repoIssueList.map(issue => {
            return <tr key={issue.number}>
                <td><b><i>{issue.number}</i></b></td>
                <td><b>{issue.title}</b></td>
                <td>{issue.body}</td>
            </tr>
        });
        return (
                <div>
                    <AppNavbar/><br /><br />
                    <Container fluid>
                        <br />
                        <InputGroup className="mb-3">
                            <Form.Control placeholder="Enter Project Name..." aria-label="Enter Project Name..."
                                aria-describedby="basic-addon2" type="text" value={this.state.value}
                                onChange={e => this.setState({ value: e.target.value })} />
                            <Button color="primary" variant="outline-secondary" id="button-addon2"
                                onClick={this.onSubmit}>Search Issues</Button>
                        </InputGroup>
                        <h3>Issues for project <b>{this.props.match.params.repo}</b> and account <b>{this.props.match.params.id}</b></h3>
                        <Table className="mt-4">
                            <thead>
                                <tr>
                                    <th width="5%">Issue No.</th>
                                    <th width="25%">Title</th>
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

export default RepoIssues;
