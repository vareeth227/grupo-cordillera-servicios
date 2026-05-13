package com.grupocordillera.ecommerce.repository;

import com.grupocordillera.ecommerce.entity.ItemPedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repositorio JPA para los ítems de pedidos.
 */
@Repository
public interface ItemPedidoRepository extends JpaRepository<ItemPedido, Long> {

    /** Busca todos los ítems pertenecientes a un pedido */
    List<ItemPedido> findByPedidoId(Long pedidoId);
}
