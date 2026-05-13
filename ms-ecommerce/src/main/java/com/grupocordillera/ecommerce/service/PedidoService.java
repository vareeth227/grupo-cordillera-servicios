package com.grupocordillera.ecommerce.service;

import com.grupocordillera.ecommerce.dto.PedidoDTO;

import java.util.List;

/**
 * Interfaz del servicio de Ecommerce.
 * Define operaciones para gestionar pedidos online.
 */
public interface PedidoService {

    /** Lista todos los pedidos registrados */
    List<PedidoDTO> listarPedidos();

    /** Busca los pedidos de un cliente específico */
    List<PedidoDTO> listarPedidosPorCliente(Long clienteId);

    /** Busca pedidos por estado */
    List<PedidoDTO> listarPedidosPorEstado(String estado);

    /** Obtiene el detalle completo de un pedido por su ID */
    PedidoDTO obtenerPedido(Long id);

    /** Crea un nuevo pedido con sus ítems */
    PedidoDTO crearPedido(PedidoDTO dto);

    /** Actualiza el estado de un pedido existente */
    PedidoDTO actualizarEstado(Long id, String nuevoEstado);

    /** Elimina un pedido y sus ítems por su ID */
    void eliminarPedido(Long id);
}
