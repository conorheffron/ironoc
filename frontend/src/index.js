import React from 'react';
import ReactDOM from 'react-dom/client';
import reportWebVitals from './reportWebVitals';
import 'bootstrap/dist/css/bootstrap.min.css';
import './index.css';
import App from './App';
import AppNavBar from './AppNavbar';
import Footer from './Footer';

const root = ReactDOM.createRoot(document.getElementById('root'));
root.render(
  <React.StrictMode>
    <div className="app-wrapper">
        <AppNavBar />
        <div className="content-inner">
            <App />
        </div>
        <Footer />
    </div>
  </React.StrictMode>
);

reportWebVitals();
