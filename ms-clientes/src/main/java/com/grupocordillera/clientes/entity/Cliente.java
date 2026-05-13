package com.grupocordillera.clientes.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

/**
 * Entidad que representa un cliente del sistema CRM.
 * Almacena información de contacto y registro del cliente.
 */
@Entity
@Table(name = "clientes")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Nombre(s) del cliente */
    @Column(nullable = false, length = 100)
    private String nombre;

    /** Apellido(s) del cliente */
    @Column(nullable = false, length = 100)
    private String apellido;

    /** Email único para identificar al cliente en el sistema */
    @Column(nullable = false, unique = true, length = 150)
    private String email;

    /** Teléfono de contacto */
    @Column(length = 20)
    private String telefono;

    /** Fecha en que el cliente se registró en el sistema */
    @Column(name = "fecha_registro", nullable = false)
    private LocalDate fechaRegistro;

    /** Indica si el cliente está activo en el CRM */
    @Column(nullable = false)
    private Boolean activo = true;

    @OneToMany(mappedBy = "cliente", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<TicketAtencion> tickets;
}
