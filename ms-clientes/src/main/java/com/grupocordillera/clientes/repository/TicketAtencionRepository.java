package com.grupocordillera.clientes.repository;

import com.grupocordillera.clientes.entity.TicketAtencion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repositorio JPA para tickets de atención al cliente.
 */
@Repository
public interface TicketAtencionRepository extends JpaRepository<TicketAtencion, Long> {

    /** Lista todos los tickets de un cliente */
    List<TicketAtencion> findByClienteId(Long clienteId);

    /** Lista tickets por estado */
    List<TicketAtencion> findByEstado(String estado);

    /** Lista tickets abiertos de un cliente */
    List<TicketAtencion> findByClienteIdAndEstado(Long clienteId, String estado);
}
