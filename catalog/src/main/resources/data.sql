INSERT INTO products (code, name, image_url, price, stock_quantity)
VALUES
    ('PROD-001', 'Laptop', 'https://placeholder.com/laptop.png', 999.99, 50),
    ('PROD-002', 'Keyboard', 'https://placeholder.com/keyboard.png', 49.99, 200),
    ('PROD-003', 'Mouse', 'https://placeholder.com/mouse.png', 29.99, 300),
    ('PROD-004', 'Monitor', 'https://placeholder.com/monitor.png', 399.99, 75),
    ('PROD-005', 'Headphones', 'https://placeholder.com/headphones.png', 79.99, 150)
    ON CONFLICT DO NOTHING;