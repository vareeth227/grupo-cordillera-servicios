package com.grupocordillera.ecommerce.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * DTO para los ítems de un pedido.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemPedidoDTO {
    private Long id;
    private Long pedidoId;
    private String productoCodigo;
    private String nombreProducto;
    private Integer cantidad;
    private BigDecimal precioUnitario;
}
