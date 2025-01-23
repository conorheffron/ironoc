import React, { Component } from 'react';
import { Button, ButtonGroup, Container, InputGroup, Table } from 'reactstrap';
import '.././App.css';
import Form from 'react-bootstrap/Form';
import AppNavbar from '.././AppNavbar';
import Footer from '.././Footer';
import { Link } from 'react-router-dom';
import LoadingSpinner from '.././LoadingSpinner';

class RepoDetails extends Component {
    constructor(props) {
        super(props);
        this.state = {
            repoDetailList: [],
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
        this.props.history.push(`/projects/${value}`, {
            id: value
        });
    }

    async componentDidMount() {
        const { match: { params: { id } } } = this.props;
        const response = await fetch(`/api/get-repo-detail?username=${id}`);
        const body = await response.json();
        this.setState({ repoDetailList: body, isLoading: false });
    }

    render() {
        const { repoDetailList = [], isLoading = true, value = '' } = this.state;
        const { match: { params: { id: gitUser = '' } } } = this.props;

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

        const repoList = repoDetailList.map(repo => (
            <tr key={repo.name}>
                <td className="table-info">
                    <a href={repo.repoUrl} target="_blank" rel="noreferrer">{repo.fullName}</a>
                </td>
                <td>{repo.description}</td>
                <td className="table-info">
                    <a href={repo.appHome} target="_blank" rel="noreferrer">{repo.name}</a>
                </td>
                <td>{repo.topics}</td>
                <td>
                    <ButtonGroup>
                        <Button size="sm" color="secondary" tag={Link} to={`/issues/${gitUser}/${repo.name}`}>List Issues</Button>
                    </ButtonGroup>
                </td>
            </tr>
        ));

        return (
            <div>
                <AppNavbar /><br /><br />
                <Container fluid>
                    <br />
                    <InputGroup className="mb-3">
                        <Form.Control
                            placeholder="Enter GitHub User ID... Example: conorheffron"
                            aria-label="Enter GitHub User ID..."
                            aria-describedby="basic-addon2"
                            value={value}
                            onChange={this.handleChange}
                            type="text"
                        />
                        <Button color="primary" variant="outline-secondary" id="button-addon2" onClick={this.onSubmit}>Search Projects</Button>
                    </InputGroup>
                    <h3 className="table-headers">GitHub Projects for username: <b>{gitUser}</b></h3>
                    <Table striped hover bordered>
                        <thead>
                            <tr className="table-primary">
                                <th width="10%">Repository</th>
                                <th width="50%">Description</th>
                                <th width="10%">App URL</th>
                                <th width="15%">Topics</th>
                                <th width="5%">Actions</th>
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

export default RepoDetails;
