import React, { Component } from 'react';
import { Container, Table } from 'reactstrap';
import AppNavbar from './AppNavbar';

class RepoIssues extends Component {

    constructor(props) {
        super(props);
        this.state = {repoIssueList: [], isLoading: true};
    }

    async componentDidMount() {
        const response = await fetch(`/get-repo-issue/conorheffron/${this.props.match.params.repo}/`);
        console.log(response)
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
                <td>{issue.number}</td>
                <td>{issue.title}</td>
                <td>{issue.body}</td>
            </tr>
        });
        return (
                <div>
                    <AppNavbar/><br /><br />
                    <Container fluid>
                        <br />
                        <h3>GitHub <b>{this.props.match.params.repo}</b> Issues</h3>
                        <Table className="mt-4">
                            <thead>
                            <tr>
                                <th width="5%">Issue No.</th>
                                <th width="25%">Title</th>
                                <th width="70%">Description</th>
                            </tr>
                            </thead>
                            <tbody>
                            {repoList}
                            </tbody>
                        </Table>
                    </Container>
                </div>
            );
    }
}
export default RepoIssues;