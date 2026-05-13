package com.grupocordillera.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Clase principal del API Gateway de Grupo Cordillera.
 * Enruta todas las solicitudes del frontend hacia los microservicios correspondientes.
 * Implementa Circuit Breaker con Resilience4j para tolerancia a fallos.
 */
@SpringBootApplication
public class GatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(GatewayApplication.class, args);
    }
}
