package com.grupocordillera.financiero.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * DTO para egresos (gastos) económicos.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EgresoDTO {
    private Long id;
    private String concepto;
    private BigDecimal monto;
    private LocalDate fecha;
    private String categoria;
}
