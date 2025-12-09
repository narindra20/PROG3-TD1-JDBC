
-- Connexion à la base de données
\c product_management_db;

-- Table Product
CREATE TABLE product (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    price NUMERIC(10, 2) NOT NULL,
    creation_datetime TIMESTAMP NOT NULL
);

-- Table Product_category
CREATE TABLE product_category (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    product_id INT NOT NULL,
    FOREIGN KEY (product_id) REFERENCES product(id) ON DELETE CASCADE
);

