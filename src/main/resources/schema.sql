-- Crear tabla de libros
CREATE TABLE IF NOT EXISTS libros (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    titulo VARCHAR(255) NOT NULL,
    autor VARCHAR(255) NOT NULL,
    tipo VARCHAR(50) NOT NULL,
    formato VARCHAR(50) NOT NULL,
    estado VARCHAR(50) NOT NULL,
    descripcion TEXT
);

-- Insertar datos de ejemplo
INSERT INTO libros (titulo, autor, tipo, formato, estado, descripcion) VALUES
('Cien años de soledad', 'Gabriel García Márquez', 'FICCION', 'FISICO', 'DISPONIBLE', 'Novela de realismo mágico'),
('El Quijote', 'Miguel de Cervantes', 'FICCION', 'FISICO', 'DISPONIBLE', 'Clásico de la literatura española'),
('Sapiens', 'Yuval Noah Harari', 'NO_FICCION', 'DIGITAL', 'DISPONIBLE', 'Historia de la humanidad'),
('1984', 'George Orwell', 'FICCION', 'DIGITAL', 'PRESTADO', 'Distopía clásica'),
('El origen de las especies', 'Charles Darwin', 'NO_FICCION', 'FISICO', 'DISPONIBLE', 'Teoría de la evolución');
