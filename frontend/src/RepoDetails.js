import React, { Component } from 'react';
import { Button, ButtonGroup, Container, Table } from 'reactstrap';
import AppNavbar from './AppNavbar';
import { Link } from 'react-router-dom';
import logo from './logo.svg';

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
        return (
            <div className="App">
            <AppNavbar/>
              <header className="App-header">
                <img src={logo} className="App-logo" alt="logo" />
                <div className="App-intro">
                  <h2>Repository Details</h2>
                  {repoDetailList.map(repoDetail =>
                      <div key={repoDetail.name}>
                        Name: {repoDetail.fullName} <br />
                        Description: {repoDetail.description} <br />
                        Topics: {repoDetail.topics} <br />
                        Home Page: {repoDetail.appHome} <br />
                        Repo Home: {repoDetail.repoUrl} <br />
                        <br /><br />
                      </div>
                  )}
                </div>
              </header>
            </div>
        );
    }
}
export default RepoDetails;