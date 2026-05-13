package com.grupocordillera.ventas;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Clase principal del microservicio de Ventas.
 * Gestiona los puntos de venta, transacciones y reportes de venta diaria.
 * Se conecta a su propia base de datos PostgreSQL (puerto 5432).
 */
@SpringBootApplication
public class VentasApplication {

    public static void main(String[] args) {
        SpringApplication.run(VentasApplication.class, args);
    }
}
