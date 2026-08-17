DROP TABLE IF EXISTS order_items;
DROP TABLE IF EXISTS orders;
DROP TABLE IF EXISTS stock_talles;
DROP TABLE IF EXISTS productos;


CREATE TABLE productos (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    categoria VARCHAR(50) NOT NULL,
    nombre VARCHAR(100) NOT NULL,
    precio DECIMAL(10,2) NOT NULL,
    imagen VARCHAR(255),
    descripcion VARCHAR(500),
    alt VARCHAR(255)
);


CREATE TABLE stock_talles (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    producto_id BIGINT NOT NULL,
    talle INT NOT NULL,
    stock INT NOT NULL,

    CONSTRAINT fk_stock_talles_producto
        FOREIGN KEY (producto_id)
        REFERENCES productos(id)
);


CREATE TABLE orders (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    fullname VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL,
    phone_number VARCHAR(20),
    address VARCHAR(200),
    status VARCHAR(20) NOT NULL,
    total DECIMAL(10,2) NOT NULL,
    created_at TIMESTAMP NOT NULL
);


CREATE TABLE order_items (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    order_id BIGINT NOT NULL,
    producto_id BIGINT NOT NULL,
    talle VARCHAR(5) NOT NULL,
    cantidad INT NOT NULL,
    precio DECIMAL(10,2) NOT NULL,

    CONSTRAINT fk_order_items_order
        FOREIGN KEY (order_id)
        REFERENCES orders(id),

    CONSTRAINT fk_order_items_producto
        FOREIGN KEY (producto_id)
        REFERENCES productos(id)
);