package com.grupocordillera.ventas.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO (Data Transfer Object) para el Punto de Venta.
 * Separa la representación de la entidad de la API REST,
 * evitando exponer la entidad JPA directamente.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PuntoDeVentaDTO {
    private Long id;
    private String nombre;
    private String direccion;
    private String region;
    private Boolean activo;
}
