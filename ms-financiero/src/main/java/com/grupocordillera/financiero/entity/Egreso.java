package com.grupocordillera.financiero.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Entidad que representa un egreso (gasto) de la empresa.
 * Incluye sueldos, arriendos, insumos, servicios, etc.
 */
@Entity
@Table(name = "egresos")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Egreso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Descripción del gasto */
    @Column(nullable = false)
    private String concepto;

    /** Monto del gasto */
    @Column(nullable = false, precision = 14, scale = 2)
    private BigDecimal monto;

    /** Fecha del gasto */
    @Column(nullable = false)
    private LocalDate fecha;

    /** Categoría del gasto: SUELDOS, ARRIENDO, INSUMOS, SERVICIOS, OTROS */
    @Column(nullable = false, length = 50)
    private String categoria;
}
