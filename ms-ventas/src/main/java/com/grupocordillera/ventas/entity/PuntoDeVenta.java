package com.grupocordillera.ventas.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entidad que representa un punto de venta físico de la empresa.
 * Cada sucursal o local es un punto de venta con su propia información.
 */
@Entity
@Table(name = "puntos_de_venta")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PuntoDeVenta {

    /** Identificador único del punto de venta */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Nombre del local o sucursal */
    @Column(nullable = false, length = 100)
    private String nombre;

    /** Dirección física del punto de venta */
    @Column(nullable = false)
    private String direccion;

    /** Región o ciudad donde está ubicado */
    @Column(nullable = false, length = 50)
    private String region;

    /** Indica si el punto de venta está operativo */
    @Column(nullable = false)
    private Boolean activo = true;
}
