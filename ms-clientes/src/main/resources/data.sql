-- ============================================================
-- Datos de prueba para ms-clientes
-- Clientes del CRM y tickets de atención al cliente
-- ============================================================

INSERT INTO clientes (id, nombre, apellido, email, telefono, fecha_registro, activo) VALUES
(1,  'María José',  'González Soto',    'mjose.gonzalez@gmail.com',    '+56912345678', '2024-03-15', true),
(2,  'Carlos',      'Martínez Lagos',   'carlos.martinez@hotmail.com', '+56923456789', '2024-05-20', true),
(3,  'Valentina',   'López Herrera',    'vlopez@outlook.com',          '+56934567890', '2024-07-08', true),
(4,  'Rodrigo',     'Fernández Muñoz',  'rodrigo.f@gmail.com',         '+56945678901', '2024-09-12', true),
(5,  'Camila',      'Torres Vega',      'camila.torres@yahoo.com',     '+56956789012', '2024-10-03', true),
(6,  'Diego',       'Ramírez Castro',   'diego.ramirez@gmail.com',     '+56967890123', '2024-11-18', true),
(7,  'Isidora',     'Sánchez Pérez',    'isidora.s@gmail.com',         '+56978901234', '2025-01-05', true),
(8,  'Matías',      'Vargas Rojas',     'mvargas@empresa.cl',          '+56989012345', '2025-02-22', true),
(9,  'Sofía',       'Morales Araya',    'sofia.morales@gmail.com',     '+56990123456', '2025-04-14', true),
(10, 'Sebastián',   'Reyes Contreras',  'sreyes@gmail.com',            '+56901234567', '2025-06-30', false)
ON CONFLICT (id) DO NOTHING;

-- Tickets con distintos estados y categorías para representar el ciclo de vida completo
INSERT INTO tickets_atencion (id, cliente_id, asunto, descripcion, estado, categoria, fecha_creacion, fecha_resolucion) VALUES
(1,  1, 'iPhone llegó con pantalla rayada',
     'Recibí mi iPhone 15 Pro con una rayadura visible en la pantalla desde la caja sellada.',
     'RESUELTO', 'RECLAMO',        '2026-04-16 09:00:00', '2026-04-18 14:30:00'),

(2,  2, 'Pedido no llega después de 10 días',
     'Realicé la compra hace 10 días y el tracking no ha cambiado de estado desde hace 5 días.',
     'RESUELTO', 'CONSULTA',       '2026-04-28 11:30:00', '2026-04-30 10:00:00'),

(3,  3, 'Solicitud de cambio - talla incorrecta',
     'Las zapatillas llegaron en talla 40 pero el pedido era talla 42. Necesito el cambio.',
     'RESUELTO', 'DEVOLUCION',     '2026-04-22 15:00:00', '2026-04-25 09:00:00'),

(4,  4, 'Código de descuento CORD20 no funciona',
     'Al intentar aplicar el código CORD20 en el checkout, el sistema indica que es inválido.',
     'CERRADO',  'CONSULTA',       '2026-04-10 10:00:00', '2026-04-10 16:00:00'),

(5,  5, 'App móvil no muestra historial de compras',
     'Desde ayer la app móvil muestra error "Ha ocurrido un problema" al entrar al historial.',
     'RESUELTO', 'SOPORTE_TECNICO','2026-04-29 08:30:00', '2026-05-02 11:00:00'),

(6,  1, 'Teclado defectuoso en MacBook Air M2',
     'La tecla "E" dejó de funcionar a los 2 meses de uso. El equipo está en garantía.',
     'EN_PROCESO','RECLAMO',       '2026-05-01 14:00:00', null),

(7,  6, 'Consulta sobre envío express a regiones',
     'Quiero saber si el envío express (24h) está disponible para Concepción y cuál es el costo.',
     'RESUELTO', 'CONSULTA',       '2026-05-02 09:00:00', '2026-05-02 12:30:00'),

(8,  7, 'Cobro duplicado en tarjeta de crédito',
     'Me aparecen dos cobros de $45.900 por el mismo pedido. Adjunto captura del extracto bancario.',
     'EN_PROCESO','RECLAMO',       '2026-05-03 10:30:00', null),

(9,  8, 'Cancelar pedido antes del despacho',
     'Necesito cancelar urgente el pedido #10 porque encontré el producto más barato en otra tienda.',
     'RESUELTO', 'CONSULTA',       '2026-05-05 08:00:00', '2026-05-05 09:30:00'),

(10, 9, 'Solicitud de factura con razón social empresa',
     'Necesito que me emitan la factura del pedido a nombre de Constructora Araya SpA, RUT 76.543.210-5.',
     'ABIERTO',  'CONSULTA',       '2026-05-06 11:00:00', null),

(11, 3, 'Aspiradora robot llegó sin cargador',
     'El producto llegó en caja sellada pero sin el adaptador de carga. Solo viene el cuerpo principal.',
     'ABIERTO',  'RECLAMO',        '2026-05-06 14:30:00', null),

(12, 2, 'Cómo funciona el programa de puntos',
     'Quisiera saber cómo acumular puntos con mis compras y qué puedo canjear con ellos.',
     'ABIERTO',  'CONSULTA',       '2026-05-06 15:45:00', null),

(13, 6, 'Error al intentar pagar con tarjeta Visa',
     'Intento pagar con mi Visa Banco de Chile y el sistema rechaza la transacción, pero mi banco confirma que hay fondos.',
     'ABIERTO',  'SOPORTE_TECNICO','2026-05-06 16:00:00', null)
ON CONFLICT (id) DO NOTHING;

SELECT setval('clientes_id_seq',         (SELECT MAX(id) FROM clientes));
SELECT setval('tickets_atencion_id_seq', (SELECT MAX(id) FROM tickets_atencion));
