DROP TABLE IF EXISTS comprobantes_detalle;
DROP TABLE IF EXISTS comprobantes;
DROP TABLE IF EXISTS stock_talles;
DROP TABLE IF EXISTS clientes;
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

CREATE TABLE clientes (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL,
    telefono VARCHAR(20),
    direccion VARCHAR(200)
);

CREATE TABLE comprobantes (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    cliente_id BIGINT NOT NULL,
    status VARCHAR(20) NOT NULL,
    total DECIMAL(10,2) NOT NULL,
    created_at TIMESTAMP NOT NULL,

    CONSTRAINT fk_comprobantes_cliente
        FOREIGN KEY (cliente_id)
        REFERENCES clientes(id)
);


CREATE TABLE comprobantes_detalle (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    comprobante_id BIGINT NOT NULL,
    producto_id BIGINT NOT NULL,
    talle VARCHAR(5) NOT NULL,
    cantidad INT NOT NULL,
    precio DECIMAL(10,2) NOT NULL,

    CONSTRAINT fk_comprobantes_detalle_comprobantes
        FOREIGN KEY (comprobante_id)
        REFERENCES comprobantes(id),

    CONSTRAINT fk_comprobantes_detalle_producto
        FOREIGN KEY (producto_id)
        REFERENCES productos(id)
    
);
