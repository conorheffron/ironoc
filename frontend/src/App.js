import React, { Component } from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router';
import { ApolloClient, InMemoryCache, ApolloProvider, HttpLink, split } from '@apollo/client';
import { GraphQLWsLink } from '@apollo/client/link/subscriptions';
import { createClient } from 'graphql-ws';
import { getMainDefinition } from '@apollo/client/utilities';
import './App.css';
import Home from './components/Home';
import CoffeeHome from './components/CoffeeHome';
import Donate from './components/Donate';
import NotFound from './components/NotFound';
import About from './components/About';
import RepoDetails from './components/RepoDetails';
import RepoIssues from './components/RepoIssues';
import ControlledCarousel from './components/ControlledCarousel';

class App extends Component {
  constructor(props) {
    super(props);

    // 1. Establish HTTP Link for Query and Mutation operations
    const httpLink = new HttpLink({
      uri: '/graphql',
    });

    // 2. Establish WebSocket Link for real-time Subscription streams
    const wsProtocol = typeof window !== 'undefined' && window.location.protocol === 'https:' ? 'wss:' : 'ws:';
    let wsHost = typeof window !== 'undefined' ? window.location.host : 'localhost:8080';
    
    // Redirect local React dev server (port 3000) WebSocket traffic to Spring Boot backend (port 8080)
    if (wsHost.startsWith('localhost:3000') || wsHost.startsWith('127.0.0.1:3000')) {
      wsHost = 'localhost:8080';
    }

    const wsLink = new GraphQLWsLink(
      createClient({
        url: `${wsProtocol}//${wsHost}/graphql`,
      })
    );

    // 3. Split operations: Subscriptions go over WS, Queries & Mutations go over HTTP
    const splitLink = split(
      ({ query }) => {
        const definition = getMainDefinition(query);
        return (
          definition.kind === 'OperationDefinition' &&
          definition.operation === 'subscription'
        );
      },
      wsLink,
      httpLink
    );

    // Create Apollo Client configured with the dynamic split link
    this.donateClient = new ApolloClient({
      link: splitLink,
      cache: new InMemoryCache(),
    });
  }

  render() {
    // Default props
    const {
      routes = [
        { path: '/', exact: true, component: Home },
        { path: '/about', exact: true, component: About },
        { path: '/portfolio', exact: true, component: ControlledCarousel },
        { path: '/projects', exact: true, component: RepoDetails },
        { path: '/projects/:id', component: RepoDetails },
        { path: '/issues/:id/:repo', component: RepoIssues },
        { path: '/brews', exact: true, component: CoffeeHome },
        { path: '/donate', exact: true, component: Donate },
        { path: '*', component: NotFound }
      ]
    } = this.props;

    return (
      <Router>
        <Routes>
          {routes.map((route, index) => {
            // Wrap the Donate component with ApolloProvider
            if (route.path === '/donate') {
              return (
                <Route
                  key={index}
                  path={route.path}
                  {...(route.exact ? { exact: true } : {})}
                  element={
                    <ApolloProvider client={this.donateClient}>
                      <Donate />
                    </ApolloProvider>
                  }
                />
              );
            }

            // Return other routes as usual (using axios, fetch, Jest etc.)
            return (
              <Route
                key={index}
                path={route.path}
                {...(route.exact ? { exact: true } : {})}
                element={<route.component />}
              />
            );
          })}
        </Routes>
      </Router>
    );
  }
}

export default App;
