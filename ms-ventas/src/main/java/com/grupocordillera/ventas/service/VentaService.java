package com.grupocordillera.ventas.service;

import com.grupocordillera.ventas.dto.PuntoDeVentaDTO;
import com.grupocordillera.ventas.dto.ReporteDiarioDTO;
import com.grupocordillera.ventas.dto.TransaccionDTO;

import java.time.LocalDate;
import java.util.List;

/**
 * Interfaz del servicio de Ventas.
 * Define el contrato de operaciones disponibles para el controlador.
 * Desacopla la implementación de la interfaz (principio de inversión de dependencias).
 */
public interface VentaService {

    /** Obtiene todos los puntos de venta registrados */
    List<PuntoDeVentaDTO> listarPuntosDeVenta();

    /** Obtiene solo los puntos de venta activos */
    List<PuntoDeVentaDTO> listarPuntosDeVentaActivos();

    /** Registra un nuevo punto de venta */
    PuntoDeVentaDTO crearPuntoDeVenta(PuntoDeVentaDTO dto);

    /** Obtiene todas las transacciones registradas */
    List<TransaccionDTO> listarTransacciones();

    /** Registra una nueva transacción de venta */
    TransaccionDTO registrarVenta(TransaccionDTO dto);

    /** Registra una devolución */
    TransaccionDTO registrarDevolucion(TransaccionDTO dto);

    /** Genera el reporte de ventas para una fecha específica */
    ReporteDiarioDTO generarReporteDiario(LocalDate fecha);

    /** Elimina un punto de venta por su ID */
    void eliminarPuntoDeVenta(Long id);

    /** Elimina una transacción por su ID */
    void eliminarTransaccion(Long id);
}
