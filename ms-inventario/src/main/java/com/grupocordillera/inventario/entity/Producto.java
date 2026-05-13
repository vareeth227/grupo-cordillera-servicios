package com.grupocordillera.inventario.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * Entidad que representa un producto del catálogo de la empresa.
 * El código es el identificador de negocio único (SKU) usado por otros microservicios.
 */
@Entity
@Table(name = "productos")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Producto {

    /** Identificador técnico auto-generado */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Código SKU único del producto, usado como referencia entre microservicios */
    @Column(nullable = false, unique = true, length = 50)
    private String codigo;

    /** Nombre descriptivo del producto */
    @Column(nullable = false, length = 150)
    private String nombre;

    /** Categoría del producto (Electrónica, Ropa, Alimentos, etc.) */
    @Column(nullable = false, length = 80)
    private String categoria;

    /** Descripción detallada del producto */
    @Column(columnDefinition = "TEXT")
    private String descripcion;

    /** Precio de venta sugerido */
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal precio;

    /** Indica si el producto está activo en el catálogo */
    @Column(nullable = false)
    private Boolean activo = true;
}
