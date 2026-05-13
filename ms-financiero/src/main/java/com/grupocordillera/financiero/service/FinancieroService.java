package com.grupocordillera.financiero.service;

import com.grupocordillera.financiero.dto.EgresoDTO;
import com.grupocordillera.financiero.dto.IngresoDTO;
import com.grupocordillera.financiero.dto.KpiFinancieroDTO;

import java.time.LocalDate;
import java.util.List;

/**
 * Interfaz del servicio Financiero.
 * Define operaciones para gestionar ingresos, egresos y calcular KPIs.
 */
public interface FinancieroService {

    /** Lista todos los ingresos registrados */
    List<IngresoDTO> listarIngresos();

    /** Lista ingresos en un rango de fechas */
    List<IngresoDTO> listarIngresosPorPeriodo(LocalDate inicio, LocalDate fin);

    /** Registra un nuevo ingreso */
    IngresoDTO registrarIngreso(IngresoDTO dto);

    /** Lista todos los egresos registrados */
    List<EgresoDTO> listarEgresos();

    /** Lista egresos en un rango de fechas */
    List<EgresoDTO> listarEgresosPorPeriodo(LocalDate inicio, LocalDate fin);

    /** Registra un nuevo egreso */
    EgresoDTO registrarEgreso(EgresoDTO dto);

    /** Calcula los KPIs financieros para un período dado */
    KpiFinancieroDTO calcularKpis(LocalDate inicio, LocalDate fin);

    /** Elimina un ingreso por su ID */
    void eliminarIngreso(Long id);

    /** Elimina un egreso por su ID */
    void eliminarEgreso(Long id);
}
