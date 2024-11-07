INSERT INTO Usuario(id, email, password, rol, activo) VALUES(null, 'test@unlam.edu.ar', 'test', 'ADMIN', true);

INSERT INTO Receta (titulo, tiempo_preparacion, categoria, imagen, descripcion, pasos, contador_visitas)
VALUES
('Milanesa napolitana', 'TREINTA_MIN', 'ALMUERZO_CENA', 'https://i.postimg.cc/7hbGvN2c/mila-napo.webp', 'No vayas más al club de la milanesa, traelo a tu casa.', 'Aplasta la carne y condimenta. Bate un huevo y mezcla pan rallado con perejil. Pasa cada filete por el huevo y luego por el pan rallado. Fríe hasta dorar. Sirve con papas y salsa de tomate, jamón y queso.', 0),
('Tarta jamón y queso', 'UNA_HORA', 'ALMUERZO_CENA', 'https://i.postimg.cc/XYXRZ1Mq/tarta-jamon-queso.jpg', 'Para comer con tus amigos y familia.', 'Precalienta el horno a 180 grados. Extiende una tapa de pascualina en un molde. Mezcla jamón picado, queso y tomate. Bate un huevo y agrégalo. Vierte sobre la base, cubre con otra tapa si deseas y haz cortes. Hornea 30-35 minutos hasta dorar.', 0),
('Café cortado con tostadas', 'DIEZ_MIN', 'DESAYUNO_MERIENDA', 'https://i.postimg.cc/90QVFGGj/cafe-tostada.jpg', 'Un clásico de las mañanas.', 'Prepara el café a tu gusto y añade un chorrito de leche caliente. Tuesta las rebanadas de pan lactal hasta dorarlas. Unta mermelada en las tostadas. Sirve el café cortado en una taza y acompáñalo con las tostadas.', 0),
('Tortas fritas', 'VEINTE_MIN', 'DESAYUNO_MERIENDA', 'https://i.postimg.cc/NMs9s0nP/foto-ilustrativa.jpg', 'Un clásico de la merienda, crujientes y deliciosas.', 'Mezcla la harina, el agua, la sal, el azúcar y la levadura hasta formar una masa. Amasa y deja reposar 10 minutos. Divide en porciones y estira en forma de discos. Fríe en aceite caliente hasta dorar. Escurre y sirve caliente.', 0),
('Panqueques', 'VEINTE_MIN', 'DESAYUNO_MERIENDA', 'https://i.postimg.cc/Pq1zy6yv/como-hacer-panqueques-esponjosos-la-receta-facil-y-economica.png', 'Suaves y esponjosos, perfectos para el desayuno.', 'Mezcla la harina, leche, huevos, azúcar y sal hasta obtener una masa homogénea. Calienta una sartén con mantequilla y vierte un poco de masa. Cocina hasta que estén dorados y voltea. Sirve con miel o mermelada.', 0),
('Empanadas de Carne', 'UNA_HORA', 'ALMUERZO_CENA', 'https://i.postimg.cc/jSPZ3ddj/empandas-de-carne.jpg', 'Clásico platillo argentino, perfectas para cualquier ocasión.', 'Cocina la carne con cebolla y especias. Deja enfriar. Rellena las masas con la mezcla y un poco de huevo duro. Cierra las empanadas y hornéalas a 180°C por 20-25 minutos.', 0),
('Ensalada de Quinoa', 'TREINTA_MIN', 'ALMUERZO_CENA', 'https://i.postimg.cc/X7Sbrqhj/ensalada-quinoa-y-verduras.jpg', 'Nutritiva y fresca, ideal para el almuerzo.', 'Cocina la quinoa y deja enfriar. Pica las verduras y mezcla en un bol. Aliña con limón, aceite, sal y mezcla bien. Sirve fría, perfecta para días calurosos.', 0),
('Tarta de Verduras', 'UNA_HORA', 'ALMUERZO_CENA', 'https://i.postimg.cc/XqVb6XKw/tarta-verdura.png', 'Una opción saludable y deliciosa, llena de sabor.', 'Saltea la cebolla y la espinaca. Mezcla con huevos y queso. Rellena la masa de tarta y hornea a 180°C por 30-35 minutos hasta dorar.', 0),
('Ensalada César', 'VEINTE_MIN', 'ALMUERZO_CENA', 'https://i.postimg.cc/gkMXtNn4/Ensalada-cesar.jpg', 'Clásica y deliciosa, perfecta para una cena ligera.', 'Cocina el pollo a la parrilla y corta en tiras. Lava y corta la lechuga. Mezcla en un bol con el pollo, crutones y queso parmesano. Aliña con aderezo César y un chorrito de limón antes de servir.', 0),
('Hamburguesas de Lentejas', 'TREINTA_MIN', 'ALMUERZO_CENA', 'https://i.postimg.cc/GpF91CcP/hamburguesa-de-lentejas.jpg', 'Sabrosas y llenas de proteínas, perfectas para una cena sustanciosa.', 'Cocina las lentejas y aplástalas. Mezcla con ajo, cebolla picada, pan rallado y especias. Forma hamburguesas y cocina en una sartén hasta dorar. Sirve en pan con lechuga y tomate.', 0);

INSERT INTO Ingrediente (nombre, cantidad, unidad_de_medida, tipo, receta_id)
VALUES
('Carne', 1, 'KILOGRAMOS', 'PROTEINA_ANIMAL', 1),
('Huevo', 2, 'UNIDAD', 'LACTEO', 1),
('Pan rallado', 200, 'GRAMOS', 'CEREAL_O_GRANO', 1),
('Perejil', 50, 'GRAMOS', 'VERDURA', 1),
('Papas', 300, 'GRAMOS', 'VERDURA', 1),

('Jamón', 200, 'GRAMOS', 'PROTEINA_ANIMAL', 2),
('Queso', 300, 'GRAMOS', 'LACTEO', 2),
('Tapa pascualina', 2, 'UNIDAD', 'CEREAL_O_GRANO', 2),
('Huevo', 2, 'UNIDAD', 'LACTEO', 2),
('Tomate', 100, 'GRAMOS', 'VERDURA', 2),
('Cebolla', 50, 'GRAMOS', 'VERDURA', 2),
('Pimiento', 50, 'GRAMOS', 'VERDURA', 2),
('Aceite de oliva', 20, 'MILILITROS', 'ACEITE', 2),
('Orégano', 5, 'GRAMOS', 'ESPECIA', 2),

('Café', 30, 'MILILITROS', 'OTRO', 3),
('Leche', 60, 'MILILITROS', 'LACTEO', 3),
('Pan lactal', 2, 'UNIDAD', 'CEREAL_O_GRANO', 3),
('Mermelada', 30, 'GRAMOS', 'CONDIMENTO', 3),

('Harina', 500, 'GRAMOS', 'CEREAL_O_GRANO', 4),
('Agua', 250, 'MILILITROS', 'OTRO', 4),
('Sal', 10, 'GRAMOS', 'OTRO', 4),
('Levadura', 10, 'GRAMOS', 'OTRO', 4),
('Azúcar', 20, 'GRAMOS', 'OTRO', 4),
('Aceite', 100, 'MILILITROS', 'ACEITE', 4),
('Leche', 50, 'MILILITROS', 'LACTEO', 4),

('Harina', 200, 'GRAMOS', 'CEREAL_O_GRANO', 5),
('Leche', 300, 'MILILITROS', 'LACTEO', 5),
('Huevos', 2, 'UNIDAD', 'OTRO', 5),
('Azúcar', 30, 'GRAMOS', 'OTRO', 5),
('Mantequilla', 50, 'GRAMOS', 'OTRO', 5),
('Sal', 5, 'GRAMOS', 'OTRO', 5),

('Masa para empanadas', 500, 'GRAMOS', 'OTRO', 6),
('Carne molida', 300, 'GRAMOS', 'PROTEINA_ANIMAL', 6),
('Cebolla', 100, 'GRAMOS', 'VERDURA', 6),
('Huevo', 1, 'UNIDAD', 'LACTEO', 6),
('Especias', 10, 'GRAMOS', 'ESPECIA', 6),
('Aceite', 30, 'MILILITROS', 'ACEITE', 6),

('Quinoa', 200, 'GRAMOS', 'CEREAL_O_GRANO', 7),
('Pimiento', 100, 'GRAMOS', 'VERDURA', 7),
('Pepino', 150, 'GRAMOS', 'VERDURA', 7),
('Tomate', 100, 'GRAMOS', 'VERDURA', 7),
('Cebolla', 50, 'GRAMOS', 'VERDURA', 7),
('Limón', 1, 'UNIDAD', 'OTRO', 7),
('Aceite de oliva', 30, 'MILILITROS', 'ACEITE', 7),
('Sal', 5, 'GRAMOS', 'OTRO', 7),

('Masa de tarta', 250, 'GRAMOS', 'OTRO', 8),
('Espinaca', 200, 'GRAMOS', 'VERDURA', 8),
('Cebolla', 100, 'GRAMOS', 'VERDURA', 8),
('Huevos', 3, 'UNIDAD', 'LACTEO', 8),
('Queso', 150, 'GRAMOS', 'LACTEO', 8),
('Sal', 5, 'GRAMOS', 'OTRO', 8),

('Lechuga romana', 150, 'GRAMOS', 'VERDURA', 9),
('Pollo', 200, 'GRAMOS', 'PROTEINA_ANIMAL', 9),
('Crutones', 50, 'GRAMOS', 'OTRO', 9),
('Queso parmesano', 50, 'GRAMOS', 'LACTEO', 9),
('Aderezo César', 30, 'MILILITROS', 'CONDIMENTO', 9),
('Limón', 1, 'UNIDAD', 'OTRO', 9),

('Lentejas', 200, 'GRAMOS', 'PROTEINA_ANIMAL', 10),
('Ajo', 2, 'UNIDAD', 'ESPECIA', 10),
('Cebolla', 100, 'GRAMOS', 'VERDURA', 10),
('Pan rallado', 100, 'GRAMOS', 'CEREAL_O_GRANO', 10),
('Especias', 10, 'GRAMOS', 'ESPECIA', 10),
('Lechuga', 50, 'GRAMOS', 'VERDURA', 10),
('Tomate', 1, 'UNIDAD', 'FRUTA', 10),
('Pan de hamburguesa', 2, 'UNIDAD', 'OTRO', 10);
INSERT INTO Usuario(id, email, password, rol, activo) VALUES(2, 'usuario@mail.com', '123', 'USUARIO', true);
