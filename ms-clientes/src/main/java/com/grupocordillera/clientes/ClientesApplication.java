package com.grupocordillera.clientes;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Clase principal del microservicio de Clientes.
 * Gestiona el CRM, historial de clientes y tickets de atención al cliente.
 * Se conecta a su propia base de datos PostgreSQL (puerto 5436).
 */
@SpringBootApplication
public class ClientesApplication {
    public static void main(String[] args) {
        SpringApplication.run(ClientesApplication.class, args);
    }
}
