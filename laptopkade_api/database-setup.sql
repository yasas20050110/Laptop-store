-- MySQL Database Setup Script for Laptop Store API
-- Run this script in MySQL to create the database

-- Create Database
CREATE DATABASE IF NOT EXISTS laptopkade;

-- Use the database
USE laptopkade;

-- Create Laptops Table
CREATE TABLE IF NOT EXISTS laptops (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    brand VARCHAR(100) NOT NULL,
    model VARCHAR(100) NOT NULL,
    processor VARCHAR(100) NOT NULL,
    ram VARCHAR(50) NOT NULL,
    storage VARCHAR(100) NOT NULL,
    graphics_card VARCHAR(100) NOT NULL,
    price DOUBLE NOT NULL,
    stock INT NOT NULL,
    description VARCHAR(1000),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    UNIQUE KEY unique_laptop (brand, model)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Create Indexes for better query performance
CREATE INDEX idx_brand ON laptops(brand);
CREATE INDEX idx_model ON laptops(model);
CREATE INDEX idx_price ON laptops(price);
CREATE INDEX idx_stock ON laptops(stock);

-- Sample Data (Optional - insert sample laptops)
INSERT INTO laptops (brand, model, processor, ram, storage, graphics_card, price, stock, description) 
VALUES 
    ('Dell', 'XPS 13', 'Intel i7', '16GB', '512GB SSD', 'Intel Iris Xe', 1299.99, 10, 'Premium ultrabook for professionals'),
    ('HP', 'Pavilion 15', 'AMD Ryzen 5', '8GB', '256GB SSD', 'AMD Radeon', 599.99, 20, 'Budget-friendly laptop'),
    ('Lenovo', 'ThinkPad E15', 'Intel i5', '8GB', '512GB SSD', 'Intel UHD', 749.99, 15, 'Business-class laptop'),
    ('ASUS', 'VivoBook 14', 'AMD Ryzen 7', '16GB', '512GB SSD', 'AMD Radeon', 899.99, 12, 'Lightweight and powerful'),
    ('MacBook', 'Pro 14', 'Apple M3', '16GB', '512GB SSD', 'Apple GPU', 1999.99, 8, 'Professional creative workstation');

-- Display the created table
SELECT * FROM laptops;
