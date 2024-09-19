INSERT INTO Usuario(id, email, password, rol, activo) VALUES(null, 'test@unlam.edu.ar', 'test', 'ADMIN', true);

INSERT INTO Receta (titulo, tiempo_preparacion, categoria, imagen, ingredientes, descripcion)
VALUES
('Milanesa napolitana', 30.0, 'almuerzo', 'https://i.postimg.cc/7hbGvN2c/mila-napo.webp', 'Carne, Huevo, Pan rallado, Perejil, Papas', 'Esto es una descripción de mila napo.'),

('Tarta jamón y queso', 60.0, 'almuerzo', 'https://i.postimg.cc/XYXRZ1Mq/tarta-jamon-queso.jpg', 'Jamón, Queso, Tapa pascualina, Huevo, Tomate', 'Esto es una descripción de tarta de jamón y queso.'),

('Café cortado con tostadas', 10.0, 'desayuno', 'https://i.postimg.cc/90QVFGGj/cafe-tostada.jpg', 'Café, Leche, Pan lactal, Mermelada', 'Esto es una descripción de prueba.');
