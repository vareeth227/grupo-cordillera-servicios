package com.grupocordillera.inventario;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Clase principal del microservicio de Inventario.
 * Controla el stock, catálogo de productos y genera alertas de inventario bajo.
 * Se conecta a su propia base de datos PostgreSQL (puerto 5434).
 */
@SpringBootApplication
public class InventarioApplication {
    public static void main(String[] args) {
        SpringApplication.run(InventarioApplication.class, args);
    }
}
