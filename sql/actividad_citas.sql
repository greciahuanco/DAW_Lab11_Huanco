CREATE DATABASE IF NOT EXISTS bd_lab11_paciente CHARACTER SET utf8mb4;

USE bd_lab11_paciente;

CREATE TABLE IF NOT EXISTS paciente (
    id_paciente INT AUTO_INCREMENT PRIMARY KEY,
    dni VARCHAR(8) UNIQUE,
    nombres VARCHAR(100) NOT NULL,
    apellidos VARCHAR(100) NOT NULL,
    fecha_nacimiento DATE,
    sexo CHAR(1),
    telefono VARCHAR(15),
    direccion VARCHAR(200),
    correo VARCHAR(100),
    estado CHAR(1)
);

CREATE DATABASE IF NOT EXISTS bd_lab11_medico CHARACTER SET utf8mb4;

USE bd_lab11_medico;

CREATE TABLE IF NOT EXISTS medico (
    id_medico INT AUTO_INCREMENT PRIMARY KEY,
    cmp VARCHAR(20) UNIQUE,
    nombres VARCHAR(100) NOT NULL,
    apellidos VARCHAR(100) NOT NULL,
    telefono VARCHAR(15),
    correo VARCHAR(100),
    id_especialidad INT NOT NULL,
    estado CHAR(1)
);

CREATE DATABASE IF NOT EXISTS bd_lab11_cita CHARACTER SET utf8mb4;

USE bd_lab11_cita;

CREATE TABLE IF NOT EXISTS cita (
    id_cita INT AUTO_INCREMENT PRIMARY KEY,
    fecha DATE NOT NULL,
    hora TIME NOT NULL,
    id_paciente INT NOT NULL,
    id_medico INT NOT NULL,
    motivo VARCHAR(300),
    estado VARCHAR(20)
);

USE bd_lab11_medico;

CREATE TABLE IF NOT EXISTS especialidad (
    id_especialidad INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    descripcion VARCHAR(200),
    estado CHAR(1) DEFAULT 'A'
);
CREATE TABLE IF NOT EXISTS horario_medico (
    id_horario INT AUTO_INCREMENT PRIMARY KEY,
    id_medico INT NOT NULL,
    dia_semana VARCHAR(15),
    hora_inicio TIME,
    hora_fin TIME
);
INSERT INTO especialidad (nombre, descripcion)
SELECT 'Medicina general', 'Consulta general'
WHERE NOT EXISTS (SELECT 1 FROM especialidad WHERE nombre = 'Medicina general');


USE bd_lab11_cita;

CREATE TABLE IF NOT EXISTS atencion (
    id_atencion INT AUTO_INCREMENT PRIMARY KEY,
    id_cita INT UNIQUE,
    diagnostico TEXT,
    tratamiento TEXT,
    observaciones TEXT,
    fecha_registro DATETIME DEFAULT CURRENT_TIMESTAMP
);
