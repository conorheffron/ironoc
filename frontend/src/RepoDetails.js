import React, { Component } from 'react';
import { Button, ButtonGroup, Container, Table } from 'reactstrap';
import AppNavbar from './AppNavbar';
import { Link } from 'react-router-dom';
import logo from './robot-logo.png';

class RepoDetails extends Component {

    constructor(props) {
        super(props);
        this.state = {repoDetailList: [], isLoading: true};
    }

    async componentDidMount() {
        const response = await fetch('/get-repo-detail?username=conorheffron');
        console.log('whasup')
        console.log(response)
        const body = await response.json();
        this.setState({repoDetailList: body, isLoading: false});
    }

    render() {
        const {repoDetailList, isLoading} = this.state;

        if (isLoading) {
            return <p>Loading...</p>;
        }

        const repoList = repoDetailList.map(repo => {
            return <tr key={repo.name}>
                <td><a href={repo.repoUrl} target="_blank">{repo.fullName}</a></td>
                <td>{repo.description}</td>
                <td><a href={repo.appHome} target="_blank">{repo.name}</a></td>
                <td>{repo.topics}</td>
                <td>
                    <ButtonGroup>
                        <Button size="sm" color="secondary" tag={Link} to={"/list-issues?" + repo.name}>List Issues</Button>
                        <Button size="sm" color="primary" tag={Link} to={"/add-issue"}>Add Issue</Button>
                    </ButtonGroup>
                </td>
            </tr>
        });
        return (
                <div>
                    <AppNavbar/><br /><br />
                    <Container fluid>
                        <br />
                        <div className="float-right">
                            <Button color="success" tag={Link} to="/create-repo">Create Repository</Button>
                        </div>
                        <br />
                        <h3>GitHub Projects</h3>
                        <Table className="mt-4">
                            <thead>
                            <tr>
                                <th width="20%">Repository</th>
                                <th width="20%">Description</th>
                                <th width="20%">App URL</th>
                                <th width="20%">Topics</th>
                                <th width="20%">Actions</th>
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
export default RepoDetails;