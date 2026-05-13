-- ============================================================
-- Datos de prueba para ms-financiero
-- Ingresos y egresos de Grupo Cordillera (enero-mayo 2026)
-- Los KPIs se calculan dinámicamente a partir de estos registros
-- ============================================================

INSERT INTO ingresos (id, concepto, monto, fecha, categoria) VALUES
-- Enero 2026
(1,  'Ventas tiendas físicas - Enero 2026',           45820000, '2026-01-31', 'VENTAS'),
(2,  'Ventas canal ecommerce - Enero 2026',           12340000, '2026-01-31', 'VENTAS'),
(3,  'Servicio de garantías extendidas',                 890000, '2026-01-15', 'SERVICIOS'),
(4,  'Intereses cuenta corriente empresarial',            98000, '2026-01-31', 'INVERSIONES'),
-- Febrero 2026
(5,  'Ventas tiendas físicas - Febrero 2026',         38950000, '2026-02-28', 'VENTAS'),
(6,  'Ventas canal ecommerce - Febrero 2026',         10200000, '2026-02-28', 'VENTAS'),
(7,  'Arriendo espacio publicitario en tiendas',         350000, '2026-02-10', 'SERVICIOS'),
(8,  'Intereses cuenta corriente empresarial',           105000, '2026-02-28', 'INVERSIONES'),
-- Marzo 2026
(9,  'Ventas tiendas físicas - Marzo 2026',           52100000, '2026-03-31', 'VENTAS'),
(10, 'Ventas canal ecommerce - Marzo 2026',           15680000, '2026-03-31', 'VENTAS'),
(11, 'Venta de activos fijos obsoletos',               1200000, '2026-03-20', 'OTROS'),
(12, 'Comisiones por servicios postventa',               320000, '2026-03-15', 'SERVICIOS'),
(13, 'Intereses cuenta corriente empresarial',           112000, '2026-03-31', 'INVERSIONES'),
-- Abril 2026
(14, 'Ventas tiendas físicas - Abril 2026',           49300000, '2026-04-30', 'VENTAS'),
(15, 'Ventas canal ecommerce - Abril 2026',           13450000, '2026-04-30', 'VENTAS'),
(16, 'Comisiones por consignación de proveedores',       780000, '2026-04-15', 'SERVICIOS'),
(17, 'Recuperación deuda incobrable cliente empresa',    450000, '2026-04-22', 'OTROS'),
(18, 'Intereses cuenta corriente empresarial',           118000, '2026-04-30', 'INVERSIONES'),
-- Mayo 2026 (parcial, al 06-May)
(19, 'Ventas tiendas físicas - Mayo 1-6 2026',         8920000, '2026-05-06', 'VENTAS'),
(20, 'Ventas canal ecommerce - Mayo 1-6 2026',         2340000, '2026-05-06', 'VENTAS')
ON CONFLICT (id) DO NOTHING;

INSERT INTO egresos (id, concepto, monto, fecha, categoria) VALUES
-- Enero 2026
(1,  'Remuneraciones y honorarios - Enero 2026',      18500000, '2026-01-31', 'SUELDOS'),
(2,  'Arriendo bodegas y locales comerciales - Enero',  6200000, '2026-01-05', 'ARRIENDO'),
(3,  'Compra de inventario a proveedores - Enero',    22000000, '2026-01-15', 'INSUMOS'),
(4,  'Servicios básicos (luz, agua, internet)',           890000, '2026-01-10', 'SERVICIOS'),
(5,  'Seguro empresarial anual (cuota enero)',            420000, '2026-01-05', 'SERVICIOS'),
-- Febrero 2026
(6,  'Remuneraciones y honorarios - Febrero 2026',    18500000, '2026-02-28', 'SUELDOS'),
(7,  'Arriendo bodegas y locales comerciales - Feb.',   6200000, '2026-02-05', 'ARRIENDO'),
(8,  'Compra de inventario a proveedores - Febrero',  16500000, '2026-02-12', 'INSUMOS'),
(9,  'Campaña de marketing digital - verano',           1200000, '2026-02-20', 'SERVICIOS'),
(10, 'Servicios básicos (luz, agua, internet)',           920000, '2026-02-10', 'SERVICIOS'),
-- Marzo 2026
(11, 'Remuneraciones y honorarios - Marzo 2026',      19200000, '2026-03-31', 'SUELDOS'),
(12, 'Arriendo bodegas y locales comerciales - Mar.',   6200000, '2026-03-05', 'ARRIENDO'),
(13, 'Compra de inventario a proveedores - Marzo',    28000000, '2026-03-10', 'INSUMOS'),
(14, 'Mantenimiento correctivo equipos y locales',       450000, '2026-03-18', 'SERVICIOS'),
(15, 'Licencia anual software ERP corporativo',          980000, '2026-03-01', 'SERVICIOS'),
(16, 'Servicios básicos (luz, agua, internet)',           870000, '2026-03-10', 'SERVICIOS'),
-- Abril 2026
(17, 'Remuneraciones y honorarios - Abril 2026',      19200000, '2026-04-30', 'SUELDOS'),
(18, 'Arriendo bodegas y locales comerciales - Abr.',   6200000, '2026-04-05', 'ARRIENDO'),
(19, 'Compra de inventario a proveedores - Abril',    24500000, '2026-04-08', 'INSUMOS'),
(20, 'Flota de delivery y logística ecommerce',         1800000, '2026-04-15', 'SERVICIOS'),
(21, 'Capacitación personal ventas Q2',                  350000, '2026-04-22', 'SERVICIOS'),
(22, 'Servicios básicos (luz, agua, internet)',           905000, '2026-04-10', 'SERVICIOS'),
-- Mayo 2026 (parcial)
(23, 'Remuneraciones - Mayo 2026 (anticipo)',            9600000, '2026-05-05', 'SUELDOS'),
(24, 'Arriendo bodegas y locales comerciales - Mayo',    6200000, '2026-05-05', 'ARRIENDO'),
(25, 'Compra urgente stock para campaña invierno',       8500000, '2026-05-04', 'INSUMOS')
ON CONFLICT (id) DO NOTHING;

SELECT setval('ingresos_id_seq',(SELECT MAX(id) FROM ingresos));
SELECT setval('egresos_id_seq', (SELECT MAX(id) FROM egresos));
