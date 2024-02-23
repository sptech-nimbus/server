CREATE DATABASE IF NOT EXISTS Gateway_Users;
USE Gateway_Users;
CREATE TABLE User (
    user_id VARCHAR(255) PRIMARY KEY,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    email VARCHAR(255) COLLATE utf8_bin NOT NULL,
    password VARCHAR(255) COLLATE utf8_bin NOT NULL,
    user_type VARCHAR(255) NOT NULL
);