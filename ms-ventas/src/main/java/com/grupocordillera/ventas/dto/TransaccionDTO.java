package com.grupocordillera.ventas.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * DTO para la Transacción de venta.
 * Se usa tanto para recibir datos del cliente como para responder.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransaccionDTO {
    private Long id;
    private LocalDateTime fecha;
    private BigDecimal monto;
    private Long puntoDeVentaId;
    private String productoCodigo;
    private Integer cantidad;
    private String tipo;
}
