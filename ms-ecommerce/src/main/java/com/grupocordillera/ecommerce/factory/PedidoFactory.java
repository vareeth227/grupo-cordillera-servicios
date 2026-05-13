package com.grupocordillera.ecommerce.factory;

import com.grupocordillera.ecommerce.dto.ItemPedidoDTO;
import com.grupocordillera.ecommerce.dto.PedidoDTO;
import com.grupocordillera.ecommerce.entity.ItemPedido;
import com.grupocordillera.ecommerce.entity.Pedido;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Factory para crear pedidos e ítems de pedido.
 * Centraliza la lógica de construcción de entidades desde DTOs.
 */
@Component
public class PedidoFactory {

    /**
     * Crea un nuevo Pedido en estado PENDIENTE a partir de un DTO.
     * Asigna la fecha actual como fecha de pedido.
     */
    public Pedido crearPedido(PedidoDTO dto) {
        Pedido pedido = new Pedido();
        pedido.setClienteId(dto.getClienteId());
        pedido.setFechaPedido(LocalDateTime.now());
        pedido.setEstado("PENDIENTE");
        pedido.setTotal(dto.getTotal());
        pedido.setDireccionDespacho(dto.getDireccionDespacho());
        return pedido;
    }

    /**
     * Crea un ItemPedido a partir de su DTO, asociándolo al pedido guardado.
     */
    public ItemPedido crearItem(ItemPedidoDTO dto, Long pedidoId) {
        ItemPedido item = new ItemPedido();
        item.setPedidoId(pedidoId);
        item.setProductoCodigo(dto.getProductoCodigo());
        item.setNombreProducto(dto.getNombreProducto());
        item.setCantidad(dto.getCantidad());
        item.setPrecioUnitario(dto.getPrecioUnitario());
        return item;
    }

    /** Convierte un Pedido y sus ítems a DTO */
    public PedidoDTO toDTO(Pedido pedido, List<ItemPedido> items) {
        List<ItemPedidoDTO> itemsDTO = items.stream()
                .map(i -> new ItemPedidoDTO(i.getId(), i.getPedidoId(), i.getProductoCodigo(),
                        i.getNombreProducto(), i.getCantidad(), i.getPrecioUnitario()))
                .collect(Collectors.toList());

        return new PedidoDTO(
                pedido.getId(), pedido.getClienteId(), pedido.getFechaPedido(),
                pedido.getEstado(), pedido.getTotal(), pedido.getDireccionDespacho(), itemsDTO
        );
    }
}
