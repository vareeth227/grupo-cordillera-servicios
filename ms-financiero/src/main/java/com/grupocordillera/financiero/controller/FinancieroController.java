package com.grupocordillera.financiero.controller;

import com.grupocordillera.financiero.dto.EgresoDTO;
import com.grupocordillera.financiero.dto.IngresoDTO;
import com.grupocordillera.financiero.dto.KpiFinancieroDTO;
import com.grupocordillera.financiero.service.FinancieroService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

/**
 * Controlador REST del microservicio Financiero.
 * Expone endpoints para ingresos, egresos y KPIs del dashboard ejecutivo.
 */
@RestController
@RequestMapping("/financiero")
@RequiredArgsConstructor
public class FinancieroController {

    private final FinancieroService financieroService;

    /** Lista todos los ingresos */
    @GetMapping("/ingresos")
    public ResponseEntity<List<IngresoDTO>> listarIngresos() {
        return ResponseEntity.ok(financieroService.listarIngresos());
    }

    /** Lista ingresos en un rango de fechas */
    @GetMapping("/ingresos/periodo")
    public ResponseEntity<List<IngresoDTO>> ingresosPorPeriodo(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fin) {
        return ResponseEntity.ok(financieroService.listarIngresosPorPeriodo(inicio, fin));
    }

    /** Registra un nuevo ingreso */
    @PostMapping("/ingresos")
    public ResponseEntity<IngresoDTO> registrarIngreso(@RequestBody IngresoDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(financieroService.registrarIngreso(dto));
    }

    /** Lista todos los egresos */
    @GetMapping("/egresos")
    public ResponseEntity<List<EgresoDTO>> listarEgresos() {
        return ResponseEntity.ok(financieroService.listarEgresos());
    }

    /** Lista egresos en un rango de fechas */
    @GetMapping("/egresos/periodo")
    public ResponseEntity<List<EgresoDTO>> egresosPorPeriodo(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fin) {
        return ResponseEntity.ok(financieroService.listarEgresosPorPeriodo(inicio, fin));
    }

    /** Registra un nuevo egreso */
    @PostMapping("/egresos")
    public ResponseEntity<EgresoDTO> registrarEgreso(@RequestBody EgresoDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(financieroService.registrarEgreso(dto));
    }

    /**
     * Calcula y retorna los KPIs financieros para el período indicado.
     * Ejemplo: GET /financiero/kpis?inicio=2024-01-01&fin=2024-01-31
     */
    @GetMapping("/kpis")
    public ResponseEntity<KpiFinancieroDTO> calcularKpis(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fin) {
        return ResponseEntity.ok(financieroService.calcularKpis(inicio, fin));
    }

    /** Elimina un ingreso por su ID */
    @DeleteMapping("/ingresos/{id}")
    public ResponseEntity<Void> eliminarIngreso(@PathVariable Long id) {
        financieroService.eliminarIngreso(id);
        return ResponseEntity.noContent().build();
    }

    /** Elimina un egreso por su ID */
    @DeleteMapping("/egresos/{id}")
    public ResponseEntity<Void> eliminarEgreso(@PathVariable Long id) {
        financieroService.eliminarEgreso(id);
        return ResponseEntity.noContent().build();
    }
}
