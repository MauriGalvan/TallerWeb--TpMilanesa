INSERT INTO Usuario(id, email, password, rol, activo) VALUES(null, 'test@unlam.edu.ar', 'test', 'ADMIN', true);

INSERT INTO Receta (titulo, tiempo_preparacion, categoria, imagen, ingredientes, descripcion, pasos)
VALUES
('Milanesa napolitana', 30.0, 'almuerzo', 'https://i.postimg.cc/7hbGvN2c/mila-napo.webp', 'Carne, Huevo, Pan rallado, Perejil, Papas', 'No vayas más al club de la milanesa, traelo a tu casa.', 'Aplasta la carne y condimenta. Bate un huevo y mezcla pan rallado con perejil. Pasa cada filete por el huevo y luego por el pan rallado. Fríe hasta dorar. Sirve con papas y salsa de tomate, jamón y queso.'),

('Tarta jamón y queso', 60.0, 'almuerzo', 'https://i.postimg.cc/XYXRZ1Mq/tarta-jamon-queso.jpg', 'Jamón, Queso, Tapa pascualina, Huevo, Tomate', 'Para comer con tus amigos y familia.', 'Precalienta el horno a 180 grados. Extiende una tapa de pascualina en un molde. Mezcla jamón picado, queso y tomate. Bate un huevo y agrégalo. Vierte sobre la base, cubre con otra tapa si deseas y haz cortes. Hornea 30-35 minutos hasta dorar.'),

('Café cortado con tostadas', 10.0, 'desayuno', 'https://i.postimg.cc/90QVFGGj/cafe-tostada.jpg', 'Café, Leche, Pan lactal, Mermelada', 'Un clásico de las mañanas.', 'Prepara el café a tu gusto y añade un chorrito de leche caliente. Tuesta las rebanadas de pan lactal hasta dorarlas. Unta mermelada en las tostadas. Sirve el café cortado en una taza y acompáñalo con las tostadas.');
