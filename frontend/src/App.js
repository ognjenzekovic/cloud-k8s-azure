import React from 'react';
import { BrowserRouter as Router, Routes, Route, Link } from 'react-router-dom';
import ProductsPage from './pages/ProductsPage';
import CreateOrderPage from './pages/CreateOrderPage';
import OrdersPage from './pages/OrdersPage';
import './App.css';

function App() {
  return (
    <Router>
      <div className="app">
        <nav className="navbar">
          <h2>Cloud Orders</h2>
          <div className="nav-links">
            <Link to="/">Proizvodi</Link>
            <Link to="/create-order">Nova narudzbina</Link>
            <Link to="/orders">Narudzbine</Link>
          </div>
        </nav>
        <div className="content">
          <Routes>
            <Route path="/" element={<ProductsPage />} />
            <Route path="/create-order" element={<CreateOrderPage />} />
            <Route path="/orders" element={<OrdersPage />} />
          </Routes>
        </div>
      </div>
    </Router>
  );
}

export default App;