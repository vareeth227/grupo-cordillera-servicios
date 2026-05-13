package com.grupocordillera.financiero.service;

import com.grupocordillera.financiero.dto.EgresoDTO;
import com.grupocordillera.financiero.dto.IngresoDTO;
import com.grupocordillera.financiero.dto.KpiFinancieroDTO;
import com.grupocordillera.financiero.entity.Egreso;
import com.grupocordillera.financiero.entity.Ingreso;
import com.grupocordillera.financiero.factory.MovimientoFactory;
import com.grupocordillera.financiero.repository.EgresoRepository;
import com.grupocordillera.financiero.repository.IngresoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementación del servicio Financiero.
 * Gestiona ingresos, egresos y calcula KPIs de rentabilidad para el dashboard.
 */
@Service
@RequiredArgsConstructor
public class FinancieroServiceImpl implements FinancieroService {

    private final IngresoRepository ingresoRepository;
    private final EgresoRepository egresoRepository;
    private final MovimientoFactory movimientoFactory;

    @Override
    @Transactional(readOnly = true)
    public List<IngresoDTO> listarIngresos() {
        return ingresoRepository.findAll().stream()
                .map(movimientoFactory::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<IngresoDTO> listarIngresosPorPeriodo(LocalDate inicio, LocalDate fin) {
        return ingresoRepository.findByFechaBetween(inicio, fin).stream()
                .map(movimientoFactory::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public IngresoDTO registrarIngreso(IngresoDTO dto) {
        Ingreso ingreso = movimientoFactory.crearIngreso(dto);
        return movimientoFactory.toDTO(ingresoRepository.save(ingreso));
    }

    @Override
    @Transactional(readOnly = true)
    public List<EgresoDTO> listarEgresos() {
        return egresoRepository.findAll().stream()
                .map(movimientoFactory::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<EgresoDTO> listarEgresosPorPeriodo(LocalDate inicio, LocalDate fin) {
        return egresoRepository.findByFechaBetween(inicio, fin).stream()
                .map(movimientoFactory::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public EgresoDTO registrarEgreso(EgresoDTO dto) {
        Egreso egreso = movimientoFactory.crearEgreso(dto);
        return movimientoFactory.toDTO(egresoRepository.save(egreso));
    }

    @Override
    @Transactional
    public void eliminarIngreso(Long id) {
        if (!ingresoRepository.existsById(id))
            throw new RuntimeException("Ingreso no encontrado con ID: " + id);
        ingresoRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void eliminarEgreso(Long id) {
        if (!egresoRepository.existsById(id))
            throw new RuntimeException("Egreso no encontrado con ID: " + id);
        egresoRepository.deleteById(id);
    }

    /**
     * Calcula los KPIs financieros para el período especificado.
     * Suma ingresos y egresos del período, calcula utilidad y margen de rentabilidad.
     */
    @Override
    @Transactional(readOnly = true)
    public KpiFinancieroDTO calcularKpis(LocalDate inicio, LocalDate fin) {
        // Sumar todos los ingresos del período
        BigDecimal totalIngresos = ingresoRepository.findByFechaBetween(inicio, fin).stream()
                .map(Ingreso::getMonto)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // Sumar todos los egresos del período
        BigDecimal totalEgresos = egresoRepository.findByFechaBetween(inicio, fin).stream()
                .map(Egreso::getMonto)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal utilidad = totalIngresos.subtract(totalEgresos);

        // Margen = (utilidad / ingresos) * 100, evitando división por cero
        BigDecimal margen = totalIngresos.compareTo(BigDecimal.ZERO) > 0
                ? utilidad.divide(totalIngresos, 4, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100))
                : BigDecimal.ZERO;

        String periodo = inicio.format(DateTimeFormatter.ofPattern("MM/yyyy")) +
                " - " + fin.format(DateTimeFormatter.ofPattern("MM/yyyy"));

        return new KpiFinancieroDTO(periodo, totalIngresos, totalEgresos,
                utilidad, margen.setScale(2, RoundingMode.HALF_UP), inicio, fin);
    }
}
