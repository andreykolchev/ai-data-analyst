
INSERT INTO customers (name, email, created_at) VALUES
                                                    ('Alice Johnson', 'alice@example.com', '2024-01-15'),
                                                    ('Bob Smith', 'bob@example.com', '2024-02-20'),
                                                    ('Charlie Brown', 'charlie@example.com', '2024-03-10'),
                                                    ('Diana Prince', 'diana@example.com', '2024-04-05'),
                                                    ('Evan Lee', 'evan@example.com', '2024-05-12');

INSERT INTO products (product_name, category) VALUES
                                                  ('Laptop Pro 15', 'Electronics'),
                                                  ('Laptop Air 13', 'Electronics'),
                                                  ('Wireless Mouse', 'Accessories'),
                                                  ('Office Chair', 'Furniture'),
                                                  ('Standing Desk', 'Furniture'),
                                                  ('Noise-cancelling Headphones', 'Accessories'),
                                                  ('4K Monitor', 'Electronics');

INSERT INTO orders (customer_id, order_date, total_amount) VALUES
                                                               (1, '2024-07-02', 1800.00),
                                                               (1, '2024-07-15', 320.00),
                                                               (2, '2024-07-03', 1500.00),
                                                               (2, '2024-06-10', 400.00),
                                                               (3, '2024-07-20', 820.00),
                                                               (4, '2024-05-18', 1200.00),
                                                               (5, '2024-07-05', 210.00),
                                                               (5, '2024-07-25', 980.00);

INSERT INTO order_items (order_id, product_id, quantity, price) VALUES
-- Order 1: Alice buys laptop
(1, 1, 1, 1800.00),

-- Order 2: Alice buys mouse + headphones
(2, 3, 1, 20.00),
(2, 6, 1, 300.00),

-- Order 3: Bob buys laptop
(3, 2, 1, 1500.00),

-- Order 4: Bob buys monitor
(4, 7, 1, 400.00),

-- Order 5: Charlie buys chair + desk
(5, 4, 1, 300.00),
(5, 5, 1, 520.00),

-- Order 6: Diana buys desk
(6, 5, 1, 1200.00),

-- Order 7: Evan buys mouse
(7, 3, 1, 20.00),

-- Order 8: Evan buys laptop + headphones
(8, 1, 1, 1800.00),
(8, 6, 1, 180.00);