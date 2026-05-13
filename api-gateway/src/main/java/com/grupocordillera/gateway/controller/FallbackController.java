package com.grupocordillera.gateway.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/fallback")
public class FallbackController {

    private static ResponseEntity<Map<String, String>> unavailable(String servicio, String nombre) {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                .body(Map.of(
                        "mensaje", "El servicio de " + nombre + " no está disponible temporalmente.",
                        "servicio", servicio,
                        "estado", "CIRCUIT_BREAKER_OPEN"
                ));
    }

    @RequestMapping("/ventas")
    public ResponseEntity<Map<String, String>> fallbackVentas() {
        return unavailable("ms-ventas", "Ventas");
    }

    @RequestMapping("/ecommerce")
    public ResponseEntity<Map<String, String>> fallbackEcommerce() {
        return unavailable("ms-ecommerce", "Ecommerce");
    }

    @RequestMapping("/inventario")
    public ResponseEntity<Map<String, String>> fallbackInventario() {
        return unavailable("ms-inventario", "Inventario");
    }

    @RequestMapping("/financiero")
    public ResponseEntity<Map<String, String>> fallbackFinanciero() {
        return unavailable("ms-financiero", "Financiero");
    }

    @RequestMapping("/clientes")
    public ResponseEntity<Map<String, String>> fallbackClientes() {
        return unavailable("ms-clientes", "Clientes");
    }
}
