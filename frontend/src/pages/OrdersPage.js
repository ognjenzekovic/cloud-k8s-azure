import React, { useEffect, useState } from 'react';
import { getOrders } from '../api';

function OrdersPage() {
    const [orders, setOrders] = useState([]);
    const [loading, setLoading] = useState(true);

    const fetchOrders = () => {
        getOrders()
            .then(res => setOrders(res.data))
            .catch(err => console.error(err))
            .finally(() => setLoading(false));
    };

    useEffect(() => {
        fetchOrders();
        const interval = setInterval(fetchOrders, 5000);
        return () => clearInterval(interval);
    }, []);

    const getStatusClass = (status) => {
        switch (status) {
            case 'PENDING': return 'status-pending';
            case 'PROCESSING': return 'status-processing';
            case 'COMPLETED': return 'status-completed';
            default: return '';
        }
    };

    if (loading) return <p>Ucitavanje...</p>;

    return (
        <div>
            <h1>Narudzbine</h1>
            {orders.length === 0 ? (
                <p>Nema narudzbina.</p>
            ) : (
                <div className="orders-list">
                    {orders.map(order => (
                        <div key={order.id} className="order-card">
                            <div className="order-header">
                                <h3>Narudzbina #{order.id}</h3>
                                <span className={`status-badge ${getStatusClass(order.status)}`}>
                                    {order.status}
                                </span>
                            </div>
                            <p>Narucilac: {order.customerName} ({order.customerId})</p>
                            <p>Ukupno: {order.totalPrice} RSD</p>

                            <table className="items-table">
                                <thead>
                                    <tr>
                                        <th>Proizvod</th>
                                        <th>Kolicina</th>
                                        <th>Cena</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    {order.items.map(item => (
                                        <tr key={item.id}>
                                            <td>{item.productName}</td>
                                            <td>{item.quantity}</td>
                                            <td>{item.unitPrice} RSD</td>
                                        </tr>
                                    ))}
                                </tbody>
                            </table>

                            {order.status === 'COMPLETED' && order.invoiceUrl && (
                                <a
                                    // href={order.invoiceUrl}
                                    href={`http://localhost:8082/api/orders/${order.id}/invoice`}
                                    target="_blank"
                                    rel="noopener noreferrer"
                                    className="download-btn"
                                >
                                    Preuzmi fakturu (PDF)
                                </a>
                            )}
                        </div>
                    ))}
                </div>
            )}
        </div>
    );
}

export default OrdersPage;