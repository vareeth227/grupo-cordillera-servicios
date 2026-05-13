package com.grupocordillera.financiero;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Clase principal del microservicio Financiero.
 * Gestiona KPIs financieros, ingresos, egresos y calcula rentabilidad.
 * Se conecta a su propia base de datos PostgreSQL (puerto 5435).
 */
@SpringBootApplication
public class FinancieroApplication {
    public static void main(String[] args) {
        SpringApplication.run(FinancieroApplication.class, args);
    }
}
