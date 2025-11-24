INSERT INTO organizadores (nombre, email, telefono) VALUES
    ('Laura Jiménez', 'laura.jimenez@eventos.com', '555-9001'),
    ('Carlos Mendoza', 'carlos.mendoza@eventos.com', '555-9002'),
    ('Patricia Vega', 'patricia.vega@eventos.com', '555-9003'),
    ('Roberto Silva', 'roberto.silva@eventos.com', '555-9004');

INSERT INTO eventos (titulo, descripcion, fecha, ubicacion, organizador_id) VALUES
    ('Concierto de Primavera', 'Un concierto al aire libre con bandas locales.', '2025-11-10T18:00:00', 'Parque Central', 1),
    ('Feria de Libros', 'Exhibición y venta de libros de autores independientes.', '2025-11-15T10:00:00', 'Centro Cultural', 2),
    ('Taller de Fotografía', 'Aprende las técnicas básicas de fotografía digital.', '2025-11-20T14:00:00', 'Estudio de Arte', 3),
    ('Cine Bajo las Estrellas', 'Proyección de películas clásicas al aire libre.', '2025-11-25T20:00:00', 'Plaza Mayor', 4);

INSERT INTO participantes (nombre, email, telefono, evento_id) VALUES
    ('Juan Pérez', 'juan.perez@email.com', '555-1001', 1),
    ('María García', 'maria.garcia@email.com', '555-1002', 1),
    ('Carlos López', 'carlos.lopez@email.com', '555-1003', 1);

INSERT INTO participantes (nombre, email, telefono, evento_id) VALUES
    ('Ana Martínez', 'ana.martinez@email.com', '555-2001', 2),
    ('Luis Rodríguez', 'luis.rodriguez@email.com', '555-2002', 2),
    ('Elena Fernández', 'elena.fernandez@email.com', '555-2003', 2),
    ('Pedro Sánchez', 'pedro.sanchez@email.com', '555-2004', 2);

INSERT INTO participantes (nombre, email, telefono, evento_id) VALUES
    ('Laura Torres', 'laura.torres@email.com', '555-3001', 3),
    ('Miguel Ramírez', 'miguel.ramirez@email.com', '555-3002', 3);

INSERT INTO participantes (nombre, email, telefono, evento_id) VALUES
    ('Sofía Morales', 'sofia.morales@email.com', '555-4001', 4),
    ('Diego Castro', 'diego.castro@email.com', '555-4002', 4),
    ('Isabel Ruiz', 'isabel.ruiz@email.com', '555-4003', 4),
    ('Roberto Jiménez', 'roberto.jimenez@email.com', '555-4004', 4),
    ('Carmen Ortiz', 'carmen.ortiz@email.com', '555-4005', 4);

