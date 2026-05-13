-- ============================================================
-- Datos de prueba para ms-inventario
-- Catálogo de productos y niveles de stock por almacén
-- Varios productos están bajo el umbral mínimo para demostrar alertas
-- ============================================================

INSERT INTO productos (id, codigo, nombre, categoria, descripcion, precio, activo) VALUES
(1,  'ELEC-001', 'iPhone 15 Pro',          'Electrónica',   'Smartphone Apple con chip A17 Pro, 256GB, cámara titanio',      649900, true),
(2,  'ELEC-002', 'Samsung Galaxy S24',     'Electrónica',   'Smartphone Samsung con IA integrada Galaxy, 128GB',             399900, true),
(3,  'ELEC-003', 'MacBook Air M2',         'Electrónica',   'Laptop Apple chip M2, 8GB RAM, 256GB SSD, pantalla Retina',     899900, true),
(4,  'ELEC-004', 'AirPods Pro (2ª gen)',   'Electrónica',   'Audífonos inalámbricos con cancelación activa de ruido',        299900, true),
(5,  'ROPA-001', 'Jeans Slim Fit',         'Vestuario',     'Pantalón de mezclilla corte slim, tallas 28-36, varios colores', 25900, true),
(6,  'ROPA-002', 'Polera Deportiva',       'Vestuario',     'Polera de algodón peinado para ejercicio, secado rápido',        18900, true),
(7,  'ROPA-003', 'Chaqueta Impermeable',   'Vestuario',     'Chaqueta resistente al agua con capucha, ideal para invierno',   89900, true),
(8,  'CALZ-001', 'Zapatillas Running',     'Calzado',       'Zapatillas para correr con suela amortiguada, tallas 36-45',     45900, true),
(9,  'CALZ-002', 'Botas de Cuero',         'Calzado',       'Botas elegantes de cuero genuino cosido, tallas 36-44',          35900, true),
(10, 'HOGAR-001','Aspiradora Robot',       'Hogar',         'Robot aspirador con mapeo láser, control WiFi y app móvil',     189900, true),
(11, 'HOGAR-002','Microondas Digital',     'Hogar',         'Microondas 20L con grill, pantalla LED y 6 programas',           89900, true),
(12, 'HOGAR-003','Sartén Antiadherente',   'Hogar',         'Sartén 28cm con recubrimiento antiadherente de 5 capas',         29900, true),
(13, 'DECO-001', 'Lámpara LED de Pie',     'Decoración',    'Lámpara de pie regulable, luz cálida/neutra/fría, 150cm',        45900, true),
(14, 'ALIM-001', 'Pack Café Premium',      'Alimentación',  'Pack x10 cápsulas de café de origen único, tostado oscuro',     12900, true),
(15, 'DEPO-001', 'Bicicleta Urbana',       'Deportes',      'Bicicleta para ciudad con 7 velocidades Shimano y frenos disco',299900, true)
ON CONFLICT (id) DO NOTHING;

-- Stock por almacén. Productos marcados con "ALERTA" están bajo el umbral_minimo.
INSERT INTO stock (id, producto_id, cantidad, umbral_minimo, almacen) VALUES
-- iPhone 15 Pro
(1,  1, 15, 10, 'Bodega Central Santiago'),
(2,  1,  4,  5, 'Bodega Valparaíso'),        -- ALERTA: 4 < 5
-- Samsung Galaxy
(3,  2, 23, 10, 'Bodega Central Santiago'),
(4,  2,  8,  5, 'Bodega Concepción'),
-- MacBook Air (bajo stock central)
(5,  3,  6, 10, 'Bodega Central Santiago'),  -- ALERTA: 6 < 10
-- AirPods Pro
(6,  4, 32, 15, 'Bodega Central Santiago'),
(7,  4,  7,  5, 'Bodega Antofagasta'),
-- Jeans Slim Fit
(8,  5, 85, 20, 'Bodega Central Santiago'),
(9,  5, 12,  5, 'Bodega Valparaíso'),
-- Polera Deportiva
(10, 6,120, 30, 'Bodega Central Santiago'),
(11, 6, 18, 20, 'Bodega Concepción'),        -- ALERTA: 18 < 20
-- Chaqueta Impermeable (temporada baja, reposición necesaria)
(12, 7, 14, 20, 'Bodega Central Santiago'),  -- ALERTA: 14 < 20
-- Zapatillas Running
(13, 8, 47, 20, 'Bodega Central Santiago'),
(14, 8,  9,  5, 'Bodega Antofagasta'),
-- Botas de Cuero
(15, 9, 31, 15, 'Bodega Central Santiago'),
-- Aspiradora Robot (rotación alta, reposición urgente)
(16,10,  7, 10, 'Bodega Central Santiago'),  -- ALERTA: 7 < 10
-- Microondas
(17,11, 25, 10, 'Bodega Central Santiago'),
-- Sartén Antiadherente
(18,12, 62, 20, 'Bodega Central Santiago'),
-- Lámpara LED (poca rotación, 1 unidad crítica)
(19,13,  2,  5, 'Bodega Central Santiago'),  -- ALERTA: 2 < 5
-- Pack Café
(20,14,210, 50, 'Bodega Central Santiago'),
(21,14, 38, 20, 'Bodega Valparaíso'),
-- Bicicleta Urbana
(22,15,  3,  5, 'Bodega Central Santiago'),  -- ALERTA: 3 < 5
(23,15,  1,  3, 'Bodega Concepción')         -- ALERTA: 1 < 3
ON CONFLICT (id) DO NOTHING;

SELECT setval('productos_id_seq',(SELECT MAX(id) FROM productos));
SELECT setval('stock_id_seq',    (SELECT MAX(id) FROM stock));
