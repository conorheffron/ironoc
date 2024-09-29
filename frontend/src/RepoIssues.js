import React, { Component } from 'react';
import { Button, ButtonGroup, Container, Table } from 'reactstrap';
import AppNavbar from './AppNavbar';
import { Link } from 'react-router-dom';
import logo from './robot-logo.png';
import { useParams } from 'react-router-dom';

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
                        <div className="float-right">
                            <Button color="success" tag={Link} to="/create-issue">Create Issue</Button>
                        </div>
                        <br />
                        <h3>GitHub Projects</h3>
                        <Table className="mt-4">
                            <thead>
                            <tr>
                                <th width="33%">Issue No.</th>
                                <th width="33%">Title</th>
                                <th width="33%">Description</th>
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