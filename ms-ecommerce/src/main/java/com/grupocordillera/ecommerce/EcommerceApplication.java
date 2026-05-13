package com.grupocordillera.ecommerce;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Clase principal del microservicio de Ecommerce.
 * Gestiona pedidos online, estado de pedidos y carrito de compras.
 * Se conecta a su propia base de datos PostgreSQL (puerto 5433).
 */
@SpringBootApplication
public class EcommerceApplication {

    public static void main(String[] args) {
        SpringApplication.run(EcommerceApplication.class, args);
    }
}
