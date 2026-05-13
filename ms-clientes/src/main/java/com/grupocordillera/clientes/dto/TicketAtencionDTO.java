package com.grupocordillera.clientes.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO para tickets de atención al cliente.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TicketAtencionDTO {
    private Long id;
    private Long clienteId;
    private String asunto;
    private String descripcion;
    private String estado;
    private String categoria;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaResolucion;
}
