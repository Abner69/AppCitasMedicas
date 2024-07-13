-- Crear tabla Doctor
CREATE TABLE Doctor (
                        id BIGINT AUTO_INCREMENT PRIMARY KEY,
                        nombre VARCHAR(255) NOT NULL,
                        apellido_paterno VARCHAR(255) NOT NULL,
                        apellido_materno VARCHAR(255),
                        especialidad VARCHAR(255) NOT NULL
);

-- Insertar datos en Doctor
INSERT INTO Doctor (nombre, apellido_paterno, apellido_materno, especialidad) VALUES
                                                                                  ('Carlos', 'García', 'Pérez', 'Cardiología'),
                                                                                  ('María', 'López', 'Gómez', 'Neumología'),
                                                                                  ('Juan', 'Hernández', 'Díaz', 'Gastroenterología');

-- Crear tabla Consultorio
CREATE TABLE Consultorio (
                             id BIGINT AUTO_INCREMENT PRIMARY KEY,
                             numero_consultorio VARCHAR(50) NOT NULL,
                             piso VARCHAR(50) NOT NULL
);

-- Insertar datos en Consultorio
INSERT INTO Consultorio (numero_consultorio, piso) VALUES
                                                       ('101', '1'),
                                                       ('102', '1'),
                                                       ('201', '2');

-- Crear tabla Cita
CREATE TABLE Cita (
                      id BIGINT AUTO_INCREMENT PRIMARY KEY,
                      doctor_id BIGINT NOT NULL,
                      consultorio_id BIGINT NOT NULL,
                      horario_consulta TIMESTAMP NOT NULL,
                      nombre_paciente VARCHAR(255) NOT NULL,
                      pendiente BOOLEAN NOT NULL,
                      FOREIGN KEY (doctor_id) REFERENCES Doctor(id),
                      FOREIGN KEY (consultorio_id) REFERENCES Consultorio(id),
                      CONSTRAINT unique_doctor_hora UNIQUE (doctor_id, horario_consulta),
                      CONSTRAINT unique_consultorio_hora UNIQUE (consultorio_id, horario_consulta)
);

-- Insertar datos en Cita
INSERT INTO Cita (doctor_id, consultorio_id, horario_consulta, nombre_paciente, pendiente) VALUES
                                                                                               (1, 1, '2024-07-15 09:00:00', 'Ana Martínez', true),
                                                                                               (2, 2, '2024-07-15 10:00:00', 'Luis Rodríguez', true),
                                                                                               (3, 3, '2024-07-15 11:00:00', 'Pedro Sánchez', true);
