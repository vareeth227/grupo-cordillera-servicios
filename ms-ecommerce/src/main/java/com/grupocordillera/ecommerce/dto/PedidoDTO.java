package com.grupocordillera.ecommerce.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO para la creación y consulta de pedidos.
 * Incluye los ítems del pedido para operaciones completas.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PedidoDTO {
    private Long id;
    private Long clienteId;
    private LocalDateTime fechaPedido;
    private String estado;
    private BigDecimal total;
    private String direccionDespacho;
    /** Lista de ítems incluidos en el pedido */
    private List<ItemPedidoDTO> items;
}
