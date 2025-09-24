-- E-Learning Database Setup Script
-- Run this in phpMyAdmin to create your database

-- Create the database
CREATE DATABASE IF NOT EXISTS elearning_db 
CHARACTER SET utf8mb4 
COLLATE utf8mb4_unicode_ci;

-- Use the database
USE elearning_db;

-- Create a user for the application (optional but recommended)
-- CREATE USER 'elearning_user'@'localhost' IDENTIFIED BY 'your_password_here';
-- GRANT ALL PRIVILEGES ON elearning_db.* TO 'elearning_user'@'localhost';
-- FLUSH PRIVILEGES;

-- Note: The tables will be automatically created by Hibernate when you start the application
-- because we have spring.jpa.hibernate.ddl-auto=update in application.properties