package com.grupocordillera.ventas.repository;

import com.grupocordillera.ventas.entity.Transaccion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Repositorio JPA para Transacciones de venta.
 * Incluye consultas personalizadas para reportes diarios.
 */
@Repository
public interface TransaccionRepository extends JpaRepository<Transaccion, Long> {

    /** Busca transacciones dentro de un rango de fechas */
    List<Transaccion> findByFechaBetween(LocalDateTime inicio, LocalDateTime fin);

    /** Busca transacciones por punto de venta */
    List<Transaccion> findByPuntoDeVentaId(Long puntoDeVentaId);

    /** Busca transacciones por tipo (VENTA o DEVOLUCION) */
    List<Transaccion> findByTipo(String tipo);

    /** Consulta para el reporte diario: transacciones de un día específico */
    @Query("SELECT t FROM Transaccion t WHERE t.fecha >= :inicio AND t.fecha < :fin")
    List<Transaccion> findTransaccionesDia(
            @Param("inicio") LocalDateTime inicio,
            @Param("fin") LocalDateTime fin);
}
