import React, { Component } from 'react';
import { Link, withRouter } from 'react-router-dom';
import { Button, Container, Form, FormGroup, Input, Label } from 'reactstrap';
import AppNavbar from './AppNavbar';

class CreateIssue extends Component {

    emptyItem = {
        title: '',
        body: '',
        assignee: '',
        labels: ''
    };

    constructor(props) {
        super(props);
        this.state = {
            item: this.emptyItem
        };
        this.handleChange = this.handleChange.bind(this);
        this.handleSubmit = this.handleSubmit.bind(this);
    }

    async componentDidMount() {
        const issue = await (await fetch(`create-issue/${this.props.match.params.repo}`)).json();
        this.setState({item: issue});
    }

    handleChange(event) {
        const target = event.target;
        const value = target.value;
        const name = target.name;
        let item = {...this.state.item};
        item[name] = value;
        this.setState({item});
    }

    async handleSubmit(event) {
        event.preventDefault();
        const {item} = this.state;
        console.log(item);
        await fetch(`/create-repo-issue/conorheffron/${this.props.match.params.repo}/`, {
            method: 'POST',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(item)
        });
        this.props.history.push(`/issues/${this.props.match.params.repo}/`);
    }

    render() {
        const {item} = this.state;
        const title = <h2>Create Issue</h2>;

        return <div>
            <AppNavbar/><br /><br />
            <Container fluid>
            <br />
                <h2>{title}</h2>
                <Form onSubmit={this.handleSubmit}>
                    <FormGroup>
                        <Label for="title">Title</Label>
                        <Input type="text" name="title" id="title" value={item.title || ''}
                               onChange={this.handleChange} autoComplete="title"/>
                    </FormGroup>
                    <FormGroup>
                        <Label for="body">Body</Label>
                        <Input type="text" name="body" id="body" value={item.body || ''}
                               onChange={this.handleChange} autoComplete="body"/>
                    </FormGroup>
                    <FormGroup>
                        <Label for="assignee">Assignee</Label>
                        <Input type="text" name="assignee" id="assignee" value={item.assignee || ''}
                               onChange={this.handleChange} autoComplete="assignee"/>
                    </FormGroup>
                    <br />
                    <FormGroup>
                        <Button color="primary" type="submit">Save</Button>{' '}
                    </FormGroup>
                </Form>
            </Container>
        </div>
    }
}
export default withRouter(CreateIssue);