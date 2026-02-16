import React, { useEffect, useState } from 'react';
import { getProducts, createOrder } from '../api';

function CreateOrderPage() {
    const [products, setProducts] = useState([]);
    const [customerId, setCustomerId] = useState('');
    const [customerName, setCustomerName] = useState('');
    const [items, setItems] = useState([]);
    const [message, setMessage] = useState('');
    const [error, setError] = useState('');

    useEffect(() => {
        getProducts().then(res => {
            setProducts(res.data);
            setItems(res.data.map(p => ({ productId: p.id, productName: p.name, quantity: 0 })));
        });
    }, []);

    const updateQuantity = (productId, quantity) => {
        setItems(items.map(item =>
            item.productId === productId
                ? { ...item, quantity: Math.max(0, parseInt(quantity) || 0) }
                : item
        ));
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        setMessage('');
        setError('');

        const selectedItems = items
            .filter(item => item.quantity > 0)
            .map(item => ({ productId: item.productId, quantity: item.quantity }));

        if (selectedItems.length === 0) {
            setError('Morate izabrati barem jedan proizvod.');
            return;
        }

        try {
            const response = await createOrder({
                customerId,
                customerName,
                items: selectedItems
            });
            setMessage(`Narudzbina #${response.data.id} uspesno kreirana!`);
            setItems(items.map(item => ({ ...item, quantity: 0 })));
            setCustomerId('');
            setCustomerName('');
        } catch (err) {
            setError(err.response?.data || 'Greska pri kreiranju narudzbine.');
        }
    };

    return (
        <div>
            <h1>Kreiraj narudzbinu</h1>
            <form onSubmit={handleSubmit} className="order-form">
                <div className="form-group">
                    <label>ID porucioca:</label>
                    <input
                        type="text"
                        value={customerId}
                        onChange={e => setCustomerId(e.target.value)}
                        required
                    />
                </div>
                <div className="form-group">
                    <label>Ime porucioca:</label>
                    <input
                        type="text"
                        value={customerName}
                        onChange={e => setCustomerName(e.target.value)}
                        required
                    />
                </div>

                <h3>Proizvodi:</h3>
                <table className="order-table">
                    <thead>
                        <tr>
                            <th>Proizvod</th>
                            <th>Cena</th>
                            <th>Na zalihama</th>
                            <th>Kolicina</th>
                        </tr>
                    </thead>
                    <tbody>
                        {products.map(product => (
                            <tr key={product.id}>
                                <td>{product.name}</td>
                                <td>{product.price} RSD</td>
                                <td>{product.stockQuantity}</td>
                                <td>
                                    <input
                                        type="number"
                                        min="0"
                                        max={product.stockQuantity}
                                        value={items.find(i => i.productId === product.id)?.quantity || 0}
                                        onChange={e => updateQuantity(product.id, e.target.value)}
                                    />
                                </td>
                            </tr>
                        ))}
                    </tbody>
                </table>

                <button type="submit" className="submit-btn">Kreiraj narudzbinu</button>

                {message && <p className="success-message">{message}</p>}
                {error && <p className="error-message">{error}</p>}
            </form>
        </div>
    );
}

export default CreateOrderPage;