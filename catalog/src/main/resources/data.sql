INSERT INTO products (code, name, image_url, price, stock_quantity)
VALUES
    ('PROD-001', 'Laptop', 'https://cloudappstorage2026.blob.core.windows.net/product-images/laptop.png', 999.99, 50),
    ('PROD-002', 'Keyboard', 'https://cloudappstorage2026.blob.core.windows.net/product-images/keyboard.png', 49.99, 200),
    ('PROD-003', 'Mouse', 'https://cloudappstorage2026.blob.core.windows.net/product-images/mouse.png', 29.99, 300),
    ('PROD-004', 'Monitor', 'https://cloudappstorage2026.blob.core.windows.net/product-images/monitor.png', 399.99, 75),
    ('PROD-005', 'Headphones', 'https://cloudappstorage2026.blob.core.windows.net/product-images/headphones.png', 79.99, 150)
    ON CONFLICT DO NOTHING;