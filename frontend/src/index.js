import { StrictMode } from 'react';
import { createRoot } from 'react-dom/client';
import { ApolloProvider } from '@apollo/client/react';
import reportWebVitals from './reportWebVitals';
import 'bootstrap/dist/css/bootstrap.min.css';
import './index.css';
import App from './App';
import AppNavBar from './AppNavbar';
import Footer from './Footer';
import apolloClient from './apolloClient';

const root = createRoot(document.getElementById('root'));
root.render(
  <StrictMode>
    <ApolloProvider client={apolloClient}>
      <div className="app-wrapper">
          <AppNavBar />
          <div className="content-inner">
              <App />
          </div>
          <Footer />
      </div>
    </ApolloProvider>
  </StrictMode>
);

reportWebVitals();
