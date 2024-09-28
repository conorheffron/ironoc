import React, { Component } from 'react';
import './App.css';
import AppNavbar from './AppNavbar';
import { Link } from 'react-router-dom';
import { Button, Container } from 'reactstrap';
import logo from './logo.svg';

class Home extends Component {
  state = {
    repoDetails: []
  };

  async componentDidMount() {
    const response = await fetch('/get-repo-detail?username=conorheffron');
    console.log(response)
    const body = await response.json();
    this.setState({repoDetails: body});
  }

  render() {
    const {repoDetails} = this.state;
    return (
        <div className="App">
          <AppNavbar/>
          <header className="App-header">
            <img src={logo} className="App-logo" alt="logo" />
            <div className="App-intro">
              <h2>Repository Details</h2>
              {repoDetails.map(repoDetail =>
                  <div key={repoDetail.name}>
                    Name: {repoDetail.name} <br />
                    Description: {repoDetail.description}  <br />
                    Topics: {repoDetail.topics}
                    <br />
                  </div>
              )}
            </div>
          </header>
        </div>
    );
  }
}

export default Home;