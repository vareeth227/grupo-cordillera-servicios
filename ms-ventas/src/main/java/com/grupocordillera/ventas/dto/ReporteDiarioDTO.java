package com.grupocordillera.ventas.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * DTO para el reporte de ventas diarias.
 * Consolida el resumen de operaciones de un día específico.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReporteDiarioDTO {
    /** Fecha del reporte */
    private LocalDate fecha;
    /** Total de ventas del día */
    private BigDecimal totalVentas;
    /** Total de devoluciones del día */
    private BigDecimal totalDevoluciones;
    /** Monto neto (ventas - devoluciones) */
    private BigDecimal montoNeto;
    /** Número de transacciones realizadas */
    private Long numeroTransacciones;
}
