package com.grupocordillera.ecommerce.repository;

import com.grupocordillera.ecommerce.entity.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repositorio JPA para Pedidos de ecommerce.
 */
@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Long> {

    /** Busca pedidos por cliente */
    List<Pedido> findByClienteId(Long clienteId);

    /** Busca pedidos por estado */
    List<Pedido> findByEstado(String estado);

    /** Busca pedidos de un cliente con un estado específico */
    List<Pedido> findByClienteIdAndEstado(Long clienteId, String estado);
}
