package com.grupocordillera.financiero.repository;

import com.grupocordillera.financiero.entity.Egreso;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

/**
 * Repositorio JPA para Egresos (gastos) económicos.
 */
@Repository
public interface EgresoRepository extends JpaRepository<Egreso, Long> {

    /** Busca egresos dentro de un rango de fechas */
    List<Egreso> findByFechaBetween(LocalDate inicio, LocalDate fin);

    /** Busca egresos por categoría */
    List<Egreso> findByCategoria(String categoria);
}
