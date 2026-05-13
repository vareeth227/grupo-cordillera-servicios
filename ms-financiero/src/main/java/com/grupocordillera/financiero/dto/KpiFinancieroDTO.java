package com.grupocordillera.financiero.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * DTO para los KPIs financieros del dashboard ejecutivo.
 * Consolida los principales indicadores financieros del período.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class KpiFinancieroDTO {
    /** Período analizado (mes/año) */
    private String periodo;
    /** Suma total de ingresos en el período */
    private BigDecimal totalIngresos;
    /** Suma total de egresos en el período */
    private BigDecimal totalEgresos;
    /** Utilidad bruta: ingresos - egresos */
    private BigDecimal utilidadBruta;
    /** Margen de rentabilidad: (utilidad / ingresos) * 100 */
    private BigDecimal margenRentabilidad;
    /** Fecha de inicio del período */
    private LocalDate fechaInicio;
    /** Fecha de fin del período */
    private LocalDate fechaFin;
}
