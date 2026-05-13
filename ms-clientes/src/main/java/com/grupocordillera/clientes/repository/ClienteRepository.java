package com.grupocordillera.clientes.repository;

import com.grupocordillera.clientes.entity.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repositorio JPA para el CRM de clientes.
 */
@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {

    /** Busca un cliente por su email (identificador único de negocio) */
    Optional<Cliente> findByEmail(String email);

    /** Lista clientes activos o inactivos */
    List<Cliente> findByActivo(Boolean activo);

    /** Busca clientes cuyo nombre o apellido contenga el texto dado */
    List<Cliente> findByNombreContainingIgnoreCaseOrApellidoContainingIgnoreCase(String nombre, String apellido);
}
