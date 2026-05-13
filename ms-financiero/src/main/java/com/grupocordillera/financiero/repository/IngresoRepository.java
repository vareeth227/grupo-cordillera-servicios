package com.grupocordillera.financiero.repository;

import com.grupocordillera.financiero.entity.Ingreso;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

/**
 * Repositorio JPA para Ingresos económicos.
 */
@Repository
public interface IngresoRepository extends JpaRepository<Ingreso, Long> {

    /** Busca ingresos dentro de un rango de fechas */
    List<Ingreso> findByFechaBetween(LocalDate inicio, LocalDate fin);

    /** Busca ingresos por categoría contable */
    List<Ingreso> findByCategoria(String categoria);
}
