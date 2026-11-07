CREATE DATABASE IF NOT EXISTS bd_lab11_categoria_dev CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE bd_lab11_categoria_dev;

CREATE TABLE IF NOT EXISTS categoria (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    estado VARCHAR(8) NOT NULL
);

CREATE DATABASE IF NOT EXISTS bd_lab11_categoria CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE bd_lab11_categoria;

CREATE TABLE IF NOT EXISTS categoria (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    estado VARCHAR(8) NOT NULL
);

CREATE DATABASE IF NOT EXISTS bd_lab11_cliente_dev CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE bd_lab11_cliente_dev;

CREATE TABLE IF NOT EXISTS cliente (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    apellidos VARCHAR(100) NOT NULL,
    direccion VARCHAR(200) NOT NULL,
    telefono VARCHAR(9) NOT NULL,
    nro_dni VARCHAR(8) NOT NULL,
    estado VARCHAR(8) NOT NULL
);

CREATE DATABASE IF NOT EXISTS bd_lab11_cliente CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE bd_lab11_cliente;

CREATE TABLE IF NOT EXISTS cliente (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    apellidos VARCHAR(100) NOT NULL,
    direccion VARCHAR(200) NOT NULL,
    telefono VARCHAR(9) NOT NULL,
    nro_dni VARCHAR(8) NOT NULL,
    estado VARCHAR(8) NOT NULL
);

CREATE DATABASE IF NOT EXISTS bd_lab11_producto_dev CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE bd_lab11_producto_dev;

CREATE TABLE IF NOT EXISTS producto (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    precio DECIMAL(12,2),
    stock INT,
    categoria_id BIGINT
);

CREATE DATABASE IF NOT EXISTS bd_lab11_producto CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE bd_lab11_producto;

CREATE TABLE IF NOT EXISTS producto (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    precio DECIMAL(12,2),
    stock INT,
    categoria_id BIGINT
);

CREATE DATABASE IF NOT EXISTS bd_lab11_pedido_dev CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE bd_lab11_pedido_dev;

CREATE TABLE IF NOT EXISTS pedido (
    id_pedido BIGINT AUTO_INCREMENT PRIMARY KEY,
    fecha DATE,
    id_cliente BIGINT,
    total DECIMAL(12,2)
);

CREATE TABLE IF NOT EXISTS detalle_pedido (
    id_detalle BIGINT AUTO_INCREMENT PRIMARY KEY,
    id_pedido BIGINT NOT NULL,
    id_producto BIGINT,
    cantidad INT,
    precio DECIMAL(12,2)
);

CREATE DATABASE IF NOT EXISTS bd_lab11_pedido CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE bd_lab11_pedido;

CREATE TABLE IF NOT EXISTS pedido (
    id_pedido BIGINT AUTO_INCREMENT PRIMARY KEY,
    fecha DATE,
    id_cliente BIGINT,
    total DECIMAL(12,2)
);

CREATE TABLE IF NOT EXISTS detalle_pedido (
    id_detalle BIGINT AUTO_INCREMENT PRIMARY KEY,
    id_pedido BIGINT NOT NULL,
    id_producto BIGINT,
    cantidad INT,
    precio DECIMAL(12,2)
);
