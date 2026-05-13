package com.grupocordillera.clientes.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Entidad que representa un ticket de atención al cliente.
 * Registra consultas, reclamos y solicitudes de soporte.
 */
@Entity
@Table(name = "tickets_atencion")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TicketAtencion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Columna FK para lectura directa en servicios y factory
    @Column(name = "cliente_id", nullable = false, insertable = false, updatable = false)
    private Long clienteId;

    // Relación JPA que genera la FK real en la BD
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cliente_id", nullable = false)
    @JsonIgnore
    private Cliente cliente;

    /** Título breve del problema o consulta */
    @Column(nullable = false, length = 200)
    private String asunto;

    /** Descripción detallada del problema */
    @Column(columnDefinition = "TEXT")
    private String descripcion;

    /** Estado del ticket: ABIERTO, EN_PROCESO, RESUELTO, CERRADO */
    @Column(nullable = false, length = 20)
    private String estado;

    /** Categoría del ticket: RECLAMO, CONSULTA, SOPORTE_TECNICO, DEVOLUCION */
    @Column(nullable = false, length = 50)
    private String categoria;

    /** Fecha y hora de creación del ticket */
    @Column(name = "fecha_creacion", nullable = false)
    private LocalDateTime fechaCreacion;

    /** Fecha y hora de resolución (null si está pendiente) */
    @Column(name = "fecha_resolucion")
    private LocalDateTime fechaResolucion;
}
