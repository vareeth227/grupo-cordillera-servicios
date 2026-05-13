package com.grupocordillera.ventas.repository;

import com.grupocordillera.ventas.entity.PuntoDeVenta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repositorio JPA para PuntoDeVenta.
 * Hereda operaciones CRUD de JpaRepository.
 * Agrega consultas específicas del dominio de ventas.
 */
@Repository
public interface PuntoDeVentaRepository extends JpaRepository<PuntoDeVenta, Long> {

    /** Busca todos los puntos de venta activos u inactivos */
    List<PuntoDeVenta> findByActivo(Boolean activo);

    /** Busca puntos de venta por región */
    List<PuntoDeVenta> findByRegion(String region);
}
