import axios from 'axios';

export const getProducts = () => axios.get('/api/products');

export const createOrder = (order) => axios.post('/api/orders', order);

export const getOrders = () => axios.get('/api/orders');