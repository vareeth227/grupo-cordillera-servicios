package com.grupocordillera.ventas.controller;

import com.grupocordillera.ventas.dto.PuntoDeVentaDTO;
import com.grupocordillera.ventas.dto.ReporteDiarioDTO;
import com.grupocordillera.ventas.dto.TransaccionDTO;
import com.grupocordillera.ventas.service.VentaService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

/**
 * Controlador REST del microservicio de Ventas.
 * Expone endpoints para gestionar puntos de venta,
 * registrar transacciones y consultar reportes diarios.
 */
@RestController
@RequestMapping("/ventas")
@RequiredArgsConstructor
public class VentaController {

    /** Servicio que contiene la lógica de negocio de ventas */
    private final VentaService ventaService;

    /** Retorna todos los puntos de venta registrados */
    @GetMapping("/puntos")
    public ResponseEntity<List<PuntoDeVentaDTO>> listarPuntosDeVenta() {
        return ResponseEntity.ok(ventaService.listarPuntosDeVenta());
    }

    /** Retorna solo los puntos de venta activos */
    @GetMapping("/puntos/activos")
    public ResponseEntity<List<PuntoDeVentaDTO>> listarPuntosActivos() {
        return ResponseEntity.ok(ventaService.listarPuntosDeVentaActivos());
    }

    /** Registra un nuevo punto de venta */
    @PostMapping("/puntos")
    public ResponseEntity<PuntoDeVentaDTO> crearPuntoDeVenta(@RequestBody PuntoDeVentaDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(ventaService.crearPuntoDeVenta(dto));
    }

    /** Retorna todas las transacciones registradas */
    @GetMapping("/transacciones")
    public ResponseEntity<List<TransaccionDTO>> listarTransacciones() {
        return ResponseEntity.ok(ventaService.listarTransacciones());
    }

    /** Registra una nueva venta */
    @PostMapping("/transacciones/venta")
    public ResponseEntity<TransaccionDTO> registrarVenta(@RequestBody TransaccionDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(ventaService.registrarVenta(dto));
    }

    /** Registra una devolución */
    @PostMapping("/transacciones/devolucion")
    public ResponseEntity<TransaccionDTO> registrarDevolucion(@RequestBody TransaccionDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(ventaService.registrarDevolucion(dto));
    }

    /**
     * Genera el reporte de ventas diarias para una fecha específica.
     * Parámetro de fecha en formato ISO: ?fecha=2024-01-15
     */
    @GetMapping("/reporte-diario")
    public ResponseEntity<ReporteDiarioDTO> reporteDiario(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha) {
        return ResponseEntity.ok(ventaService.generarReporteDiario(fecha));
    }

    @DeleteMapping("/puntos/{id}")
    public ResponseEntity<Void> eliminarPunto(@PathVariable Long id) {
        ventaService.eliminarPuntoDeVenta(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/transacciones/{id}")
    public ResponseEntity<Void> eliminarTransaccion(@PathVariable Long id) {
        ventaService.eliminarTransaccion(id);
        return ResponseEntity.noContent().build();
    }
}
