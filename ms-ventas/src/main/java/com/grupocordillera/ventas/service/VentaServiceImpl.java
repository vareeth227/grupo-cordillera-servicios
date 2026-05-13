package com.grupocordillera.ventas.service;

import com.grupocordillera.ventas.dto.PuntoDeVentaDTO;
import com.grupocordillera.ventas.dto.ReporteDiarioDTO;
import com.grupocordillera.ventas.dto.TransaccionDTO;
import com.grupocordillera.ventas.entity.PuntoDeVenta;
import com.grupocordillera.ventas.entity.Transaccion;
import com.grupocordillera.ventas.factory.TransaccionFactory;
import com.grupocordillera.ventas.repository.PuntoDeVentaRepository;
import com.grupocordillera.ventas.repository.TransaccionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementación del servicio de Ventas.
 * Contiene la lógica de negocio para gestionar puntos de venta,
 * registrar transacciones y generar reportes diarios.
 */
@Service
@RequiredArgsConstructor
public class VentaServiceImpl implements VentaService {

    /** Repositorio para acceder a los datos de puntos de venta */
    private final PuntoDeVentaRepository puntoDeVentaRepository;

    /** Repositorio para acceder a las transacciones */
    private final TransaccionRepository transaccionRepository;

    /** Factory para crear y convertir transacciones */
    private final TransaccionFactory transaccionFactory;

    /** Retorna todos los puntos de venta como DTOs */
    @Override
    @Transactional(readOnly = true)
    public List<PuntoDeVentaDTO> listarPuntosDeVenta() {
        return puntoDeVentaRepository.findAll().stream()
                .map(p -> new PuntoDeVentaDTO(p.getId(), p.getNombre(), p.getDireccion(), p.getRegion(), p.getActivo()))
                .collect(Collectors.toList());
    }

    /** Retorna solo los puntos de venta activos */
    @Override
    @Transactional(readOnly = true)
    public List<PuntoDeVentaDTO> listarPuntosDeVentaActivos() {
        return puntoDeVentaRepository.findByActivo(true).stream()
                .map(p -> new PuntoDeVentaDTO(p.getId(), p.getNombre(), p.getDireccion(), p.getRegion(), p.getActivo()))
                .collect(Collectors.toList());
    }

    /** Crea un nuevo punto de venta en la base de datos */
    @Override
    @Transactional
    public PuntoDeVentaDTO crearPuntoDeVenta(PuntoDeVentaDTO dto) {
        PuntoDeVenta punto = new PuntoDeVenta(null, dto.getNombre(), dto.getDireccion(), dto.getRegion(), true);
        PuntoDeVenta guardado = puntoDeVentaRepository.save(punto);
        return new PuntoDeVentaDTO(guardado.getId(), guardado.getNombre(), guardado.getDireccion(), guardado.getRegion(), guardado.getActivo());
    }

    /** Retorna todas las transacciones como DTOs */
    @Override
    @Transactional(readOnly = true)
    public List<TransaccionDTO> listarTransacciones() {
        return transaccionRepository.findAll().stream()
                .map(transaccionFactory::toDTO)
                .collect(Collectors.toList());
    }

    /** Registra una nueva venta usando la factory */
    @Override
    @Transactional
    public TransaccionDTO registrarVenta(TransaccionDTO dto) {
        Transaccion transaccion = transaccionFactory.crearVenta(dto);
        Transaccion guardada = transaccionRepository.save(transaccion);
        return transaccionFactory.toDTO(guardada);
    }

    /** Registra una devolución usando la factory */
    @Override
    @Transactional
    public TransaccionDTO registrarDevolucion(TransaccionDTO dto) {
        Transaccion transaccion = transaccionFactory.crearDevolucion(dto);
        Transaccion guardada = transaccionRepository.save(transaccion);
        return transaccionFactory.toDTO(guardada);
    }

    @Override
    @Transactional
    public void eliminarPuntoDeVenta(Long id) {
        if (!puntoDeVentaRepository.existsById(id))
            throw new RuntimeException("Punto de venta no encontrado con ID: " + id);
        puntoDeVentaRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void eliminarTransaccion(Long id) {
        if (!transaccionRepository.existsById(id))
            throw new RuntimeException("Transacción no encontrada con ID: " + id);
        transaccionRepository.deleteById(id);
    }

    /**
     * Genera un reporte diario de ventas para la fecha indicada.
     * Calcula totales de ventas, devoluciones y monto neto.
     */
    @Override
    @Transactional(readOnly = true)
    public ReporteDiarioDTO generarReporteDiario(LocalDate fecha) {
        LocalDateTime inicio = fecha.atStartOfDay();
        LocalDateTime fin = fecha.plusDays(1).atStartOfDay();

        List<Transaccion> transacciones = transaccionRepository.findTransaccionesDia(inicio, fin);

        // Calcular total de ventas
        BigDecimal totalVentas = transacciones.stream()
                .filter(t -> "VENTA".equals(t.getTipo()))
                .map(Transaccion::getMonto)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // Calcular total de devoluciones
        BigDecimal totalDevoluciones = transacciones.stream()
                .filter(t -> "DEVOLUCION".equals(t.getTipo()))
                .map(Transaccion::getMonto)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return new ReporteDiarioDTO(
                fecha,
                totalVentas,
                totalDevoluciones,
                totalVentas.subtract(totalDevoluciones),
                (long) transacciones.size()
        );
    }
}
