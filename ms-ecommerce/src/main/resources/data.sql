-- ============================================================
-- Datos de prueba para ms-ecommerce
-- Pedidos online en distintos estados del ciclo de vida
-- ============================================================

INSERT INTO pedidos (id, cliente_id, fecha_pedido, estado, total, direccion_despacho) VALUES
(1,  1, '2026-04-15 10:00:00', 'ENTREGADO',  899900, 'Av. Providencia 1234, Providencia, Santiago'),
(2,  2, '2026-04-18 14:30:00', 'ENTREGADO',  374750, 'Calle Los Leones 567, Las Condes, Santiago'),
(3,  3, '2026-04-20 09:15:00', 'EN_ENVIO',   649900, 'Av. Grecia 890, Ñuñoa, Santiago'),
(4,  4, '2026-04-25 11:00:00', 'CONFIRMADO', 189900, 'Gran Avenida 2345, San Miguel, Santiago'),
(5,  5, '2026-04-28 15:45:00', 'CONFIRMADO', 260500, 'Av. Vicuña Mackenna 3456, Macul, Santiago'),
(6,  1, '2026-05-01 08:30:00', 'EN_ENVIO',   299900, 'Av. Providencia 1234, Providencia, Santiago'),
(7,  6, '2026-05-02 12:00:00', 'PENDIENTE',  129700, 'Calle Ñuble 789, Providencia, Santiago'),
(8,  7, '2026-05-03 16:20:00', 'PENDIENTE',  299900, 'Av. El Bosque Norte 456, Las Condes, Santiago'),
(9,  3, '2026-05-04 10:45:00', 'PENDIENTE',   45900, 'Av. Grecia 890, Ñuñoa, Santiago'),
(10, 8, '2026-05-05 14:00:00', 'CANCELADO',  399900, 'Calle Santo Domingo 1234, Santiago Centro')
ON CONFLICT (id) DO NOTHING;

INSERT INTO items_pedido (id, pedido_id, producto_codigo, nombre_producto, cantidad, precio_unitario) VALUES
-- Pedido 1: MacBook Air (entregado)
(1,  1, 'ELEC-003', 'MacBook Air M2',           1, 899900),
-- Pedido 2: AirPods + Café (entregado)
(2,  2, 'ELEC-004', 'AirPods Pro (2ª gen)',      1, 299900),
(3,  2, 'ALIM-001', 'Pack Café Premium',          3,  24950),
-- Pedido 3: iPhone (en envío)
(4,  3, 'ELEC-001', 'iPhone 15 Pro',             1, 649900),
-- Pedido 4: Aspiradora Robot (confirmado)
(5,  4, 'HOGAR-001','Aspiradora Robot',           1, 189900),
-- Pedido 5: Ropa y calzado variado (confirmado)
(6,  5, 'CALZ-001', 'Zapatillas Running',         1,  45900),
(7,  5, 'CALZ-002', 'Botas de Cuero',             1,  35900),
(8,  5, 'ROPA-003', 'Chaqueta Impermeable',       1,  89900),
(9,  5, 'ROPA-001', 'Jeans Slim Fit',             2,  25900),
(10, 5, 'ROPA-002', 'Polera Deportiva',           2,  18900),
-- Pedido 6: AirPods (en envío)
(11, 6, 'ELEC-004', 'AirPods Pro (2ª gen)',       1, 299900),
-- Pedido 7: Hogar + Alimentación (pendiente)
(12, 7, 'HOGAR-003','Sartén Antiadherente',       2,  29900),
(13, 7, 'ALIM-001', 'Pack Café Premium',           2,  24950),
(14, 7, 'DECO-001', 'Lámpara LED de Pie',         1,  45900),
-- Pedido 8: Bicicleta (pendiente)
(15, 8, 'DEPO-001', 'Bicicleta Urbana',           1, 299900),
-- Pedido 9: Zapatillas (pendiente)
(16, 9, 'CALZ-001', 'Zapatillas Running',          1,  45900),
-- Pedido 10: Samsung cancelado
(17,10, 'ELEC-002', 'Samsung Galaxy S24',          1, 399900)
ON CONFLICT (id) DO NOTHING;

SELECT setval('pedidos_id_seq',     (SELECT MAX(id) FROM pedidos));
SELECT setval('items_pedido_id_seq',(SELECT MAX(id) FROM items_pedido));
