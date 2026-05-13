-- ============================================================
-- Datos de prueba para ms-ventas
-- Tiendas físicas de Grupo Cordillera distribuidas en Chile
-- ============================================================

-- Puntos de venta activos (uno inactivo para mostrar filtrado)
INSERT INTO puntos_de_venta (id, nombre, direccion, region, activo) VALUES
(1, 'Tienda Santiago Centro',  'Av. Libertador B. O''Higgins 1058', 'Región Metropolitana',    true),
(2, 'Tienda Las Condes',       'Av. Apoquindo 4501',                'Región Metropolitana',    true),
(3, 'Tienda Maipú',            'Av. Pajaritos 3450',                'Región Metropolitana',    true),
(4, 'Tienda Valparaíso',       'Av. Argentina 123',                 'Región de Valparaíso',    true),
(5, 'Tienda Concepción',       'Barros Arana 640',                  'Región del Biobío',       true),
(6, 'Tienda Antofagasta',      'Av. Balmaceda 2498',                'Región de Antofagasta',   true),
(7, 'Tienda La Serena',        'Av. Francisco de Aguirre 485',      'Región de Coquimbo',      false)
ON CONFLICT (id) DO NOTHING;

-- Transacciones de abril y mayo 2026 (ventas y algunas devoluciones)
INSERT INTO transacciones (id, fecha, monto, punto_de_venta_id, producto_codigo, cantidad, tipo) VALUES
(1,  '2026-04-01 10:15:00', 649900, 1, 'ELEC-001', 1, 'VENTA'),
(2,  '2026-04-01 11:30:00',  51800, 1, 'ROPA-001', 2, 'VENTA'),
(3,  '2026-04-01 14:00:00', 299900, 2, 'ELEC-004', 1, 'VENTA'),
(4,  '2026-04-02 09:45:00', 899900, 2, 'ELEC-003', 1, 'VENTA'),
(5,  '2026-04-02 12:20:00',  45900, 3, 'CALZ-001', 1, 'VENTA'),
(6,  '2026-04-02 15:10:00', 649900, 1, 'ELEC-001', 1, 'DEVOLUCION'),
(7,  '2026-04-03 10:00:00', 189900, 3, 'HOGAR-001',1, 'VENTA'),
(8,  '2026-04-03 11:45:00',  18900, 4, 'ROPA-002', 1, 'VENTA'),
(9,  '2026-04-04 09:30:00', 399900, 5, 'ELEC-002', 1, 'VENTA'),
(10, '2026-04-04 13:00:00',  35900, 5, 'CALZ-002', 1, 'VENTA'),
(11, '2026-04-05 10:15:00',  89900, 1, 'HOGAR-002',1, 'VENTA'),
(12, '2026-04-05 12:30:00',  38700, 2, 'ALIM-001', 3, 'VENTA'),
(13, '2026-04-06 09:00:00', 299900, 2, 'DEPO-001', 1, 'VENTA'),
(14, '2026-04-06 14:15:00',  25900, 3, 'ROPA-001', 1, 'VENTA'),
(15, '2026-04-07 11:00:00',  59800, 4, 'HOGAR-003',2, 'VENTA'),
(16, '2026-04-07 15:30:00', 299900, 4, 'ELEC-004', 1, 'DEVOLUCION'),
(17, '2026-04-08 10:45:00',  45900, 6, 'CALZ-001', 1, 'VENTA'),
(18, '2026-04-08 12:00:00', 399900, 6, 'ELEC-002', 1, 'VENTA'),
(19, '2026-04-10 09:00:00',  89900, 1, 'ROPA-003', 1, 'VENTA'),
(20, '2026-04-12 14:30:00',  45900, 3, 'DECO-001', 1, 'VENTA'),
-- Mayo 2026
(21, '2026-05-02 09:15:00', 649900, 1, 'ELEC-001', 1, 'VENTA'),
(22, '2026-05-02 11:30:00',  51800, 2, 'ROPA-001', 2, 'VENTA'),
(23, '2026-05-03 14:00:00', 189900, 3, 'HOGAR-001',1, 'VENTA'),
(24, '2026-05-04 10:00:00', 399900, 1, 'ELEC-002', 1, 'VENTA'),
(25, '2026-05-05 11:30:00',  37800, 2, 'ROPA-002', 2, 'VENTA'),
(26, '2026-05-05 13:45:00',  89900, 3, 'HOGAR-002',1, 'VENTA'),
(27, '2026-05-06 09:30:00', 649900, 1, 'ELEC-001', 1, 'VENTA'),
(28, '2026-05-06 10:45:00',  45900, 2, 'CALZ-001', 1, 'VENTA'),
(29, '2026-05-06 13:00:00',  89900, 4, 'ROPA-003', 1, 'VENTA'),
(30, '2026-05-06 15:00:00',  12900, 1, 'ALIM-001', 1, 'DEVOLUCION')
ON CONFLICT (id) DO NOTHING;

-- Reinicia las secuencias para que los próximos inserts no generen conflictos
SELECT setval('puntos_de_venta_id_seq', (SELECT MAX(id) FROM puntos_de_venta));
SELECT setval('transacciones_id_seq',   (SELECT MAX(id) FROM transacciones));
