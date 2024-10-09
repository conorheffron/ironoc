import React, { Component } from 'react';
import { Button, ButtonGroup, Container, InputGroup, Table } from 'reactstrap';
import '.././App.css';
import Form from 'react-bootstrap/Form';
import AppNavbar from '.././AppNavbar';
import { Link } from 'react-router-dom';

class RepoDetails extends Component {

    constructor(props) {
        super(props);
        this.state = {repoDetailList: [], isLoading: true, value: ''};

        this.handleChange = this.handleChange.bind(this);
        this.onSubmit = this.onSubmit.bind(this);
    }

    handleChange(event) {
        const {repos, isL, value} = this.state;
        this.setState({repoDetailList: repos, isLoading: isL, value: event.target.value});
    }

    onSubmit(event){
        let id = this.state.value;
        this.props.history.push(`/projects/${id}`, {
          id: id
        });
    }

    async componentDidMount() {
        const response = await fetch(`/get-repo-detail?username=${this.props.match.params.id}`);
        console.log(response);
        const body = await response.json();
        this.setState({repoDetailList: body, isLoading: false});
    }

    render() {
        const {repoDetailList, isLoading} = this.state;

        if (isLoading) {
            return <p>Loading...</p>;
        }

        let gitUser = this.props.match.params.id;
        const repoList = repoDetailList.map(repo => {
            return <tr key={repo.name}>
                <td className="table-info"><a href={repo.repoUrl} target="_blank" rel="noreferrer">{repo.fullName}</a></td>
                <td>{repo.description}</td>
                <td className="table-info"><a href={repo.appHome} target="_blank" rel="noreferrer">{repo.name}</a></td>
                <td>{repo.topics}</td>
                <td>
                    <ButtonGroup>
                        <Button size="sm" color="secondary" tag={Link} to={"/issues/" + gitUser + "/" + repo.name}>List Issues</Button>
                    </ButtonGroup>
                </td>
            </tr>
        });

        return (
                <div>
                    <AppNavbar/><br /><br />
                    <Container fluid>
                        <br />
                        <InputGroup className="mb-3">
                            <Form.Control placeholder="Enter GitHub User ID... Example: conorheffron" aria-label="Enter GitHub User ID..."
                                aria-describedby="basic-addon2" value={this.state.value}
                                    onChange={e => this.setState({ value: e.target.value })} type="text"/>
                            <Button color="primary" variant="outline-secondary" id="button-addon2"
                                onClick={this.onSubmit}>Search Projects</Button>
                        </InputGroup>
                        <h3>GitHub Projects for username: <b>{this.props.match.params.id}</b></h3>
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
                </div>
            );
    }
}

export default RepoDetails;
