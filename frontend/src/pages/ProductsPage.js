import React, { useEffect, useState } from 'react';
import { getProducts } from '../api';

function ProductsPage() {
    const [products, setProducts] = useState([]);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        getProducts()
            .then(res => setProducts(res.data))
            .catch(err => console.error(err))
            .finally(() => setLoading(false));
    }, []);

    if (loading) return <p>Ucitavanje...</p>;

    return (
        <div>
            <h1>Katalog proizvoda</h1>
            <div className="product-grid">
                {products.map(product => (
                    <div key={product.id} className="product-card">
                        <img src={product.imageUrl} alt={product.name} className="product-image" />
                        <h3>{product.name}</h3>
                        <p className="product-code">Sifra: {product.code}</p>
                        <p className="product-price">{product.price} RSD</p>
                        <p className="product-stock">Na zalihama: {product.stockQuantity}</p>
                    </div>
                ))}
            </div>
        </div>
    );
}

export default ProductsPage;