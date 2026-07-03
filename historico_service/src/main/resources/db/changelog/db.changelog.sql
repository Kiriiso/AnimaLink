--liquibase formatted sql

--changeset carlos:1
INSERT INTO historico (total_neto, descuento, iva, total_venta, metodo_pago, fecha_venta) VALUES
(100000, 5000, 19000, 114000, 'Tarjeta', '2026-06-10'),
(75000, 2500, 14250, 86750, 'Efectivo', '2026-06-11'),
(54000, 0, 10260, 64260, 'Transferencia', '2026-06-12'),
(120000, 12000, 21600, 129600, 'Webpay', '2026-06-13'),
(89000, 4400, 16920, 101520, 'Cheque', '2026-06-14');
