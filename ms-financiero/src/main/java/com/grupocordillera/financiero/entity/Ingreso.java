package com.grupocordillera.financiero.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Entidad que representa un ingreso económico de la empresa.
 * Puede provenir de ventas, servicios, inversiones, etc.
 */
@Entity
@Table(name = "ingresos")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Ingreso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Descripción del origen del ingreso */
    @Column(nullable = false)
    private String concepto;

    /** Monto del ingreso */
    @Column(nullable = false, precision = 14, scale = 2)
    private BigDecimal monto;

    /** Fecha en que se registró el ingreso */
    @Column(nullable = false)
    private LocalDate fecha;

    /** Categoría contable: VENTAS, SERVICIOS, INVERSIONES, OTROS */
    @Column(nullable = false, length = 50)
    private String categoria;
}
