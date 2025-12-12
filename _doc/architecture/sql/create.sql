-- ============================================
-- CREATE TABLES
-- ============================================

CREATE TABLE customers (
                           id SERIAL PRIMARY KEY,
                           name TEXT NOT NULL,
                           email TEXT,
                           created_at TIMESTAMP NOT NULL DEFAULT NOW()
);

CREATE TABLE products (
                          id SERIAL PRIMARY KEY,
                          product_name TEXT NOT NULL,
                          category TEXT NOT NULL
);

CREATE TABLE orders (
                        id SERIAL PRIMARY KEY,
                        customer_id INTEGER NOT NULL REFERENCES customers(id),
                        order_date TIMESTAMP NOT NULL,
                        total_amount NUMERIC(12,2) NOT NULL
);

CREATE TABLE order_items (
                             id SERIAL PRIMARY KEY,
                             order_id INTEGER NOT NULL REFERENCES orders(id),
                             product_id INTEGER NOT NULL REFERENCES products(id),
                             quantity INTEGER NOT NULL,
                             price NUMERIC(12,2) NOT NULL
);
