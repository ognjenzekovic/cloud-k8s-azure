import axios from 'axios';

const CATALOG_URL = 'http://localhost:8081/api';
const ORDER_URL = 'http://localhost:8082/api';

export const getProducts = () => axios.get(`${CATALOG_URL}/products`);

export const createOrder = (order) => axios.post(`${ORDER_URL}/orders`, order);

export const getOrders = () => axios.get(`${ORDER_URL}/orders`);