package com.grupocordillera.ventas.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Entidad que representa una transacción de venta o devolución.
 * Registra cada operación realizada en un punto de venta.
 */
@Entity
@Table(name = "transacciones")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Transaccion {

    /** Identificador único de la transacción */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Fecha y hora exacta de la transacción */
    @Column(nullable = false)
    private LocalDateTime fecha;

    /** Monto total de la transacción */
    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal monto;

    /** Referencia al punto de venta donde ocurrió */
    @Column(name = "punto_de_venta_id", nullable = false)
    private Long puntoDeVentaId;

    /** Código del producto involucrado en la transacción */
    @Column(name = "producto_codigo", nullable = false, length = 50)
    private String productoCodigo;

    /** Cantidad de unidades vendidas o devueltas */
    @Column(nullable = false)
    private Integer cantidad;

    /** Tipo de operación: VENTA o DEVOLUCION */
    @Column(nullable = false, length = 20)
    private String tipo;
}
