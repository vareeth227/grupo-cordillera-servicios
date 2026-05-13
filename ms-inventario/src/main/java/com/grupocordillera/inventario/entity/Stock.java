package com.grupocordillera.inventario.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entidad que representa el nivel de stock de un producto en un almacén.
 * Permite saber cuántas unidades hay disponibles y si está bajo el umbral de alerta.
 */
@Entity
@Table(name = "stock")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Stock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Referencia al producto en el catálogo */
    @Column(name = "producto_id", nullable = false)
    private Long productoId;

    /** Cantidad actual disponible en el almacén */
    @Column(nullable = false)
    private Integer cantidad;

    /** Cantidad mínima antes de generar alerta de reposición */
    @Column(name = "umbral_minimo", nullable = false)
    private Integer umbralMinimo;

    /** Nombre o código del almacén donde está el stock */
    @Column(nullable = false, length = 100)
    private String almacen;
}
