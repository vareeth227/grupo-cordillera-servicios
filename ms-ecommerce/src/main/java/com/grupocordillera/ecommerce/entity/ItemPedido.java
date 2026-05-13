package com.grupocordillera.ecommerce.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "items_pedido")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemPedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Columna FK usada en servicios y factory para escritura directa
    @Column(name = "pedido_id", nullable = false, insertable = false, updatable = false)
    private Long pedidoId;

    // Relación JPA que genera la FK real en la BD
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pedido_id", nullable = false)
    @JsonIgnore
    private Pedido pedido;

    /** Código del producto (referencia al ms-inventario) */
    @Column(name = "producto_codigo", nullable = false, length = 50)
    private String productoCodigo;

    /** Nombre del producto al momento de la compra */
    @Column(name = "nombre_producto", nullable = false)
    private String nombreProducto;

    /** Cantidad de unidades del producto */
    @Column(nullable = false)
    private Integer cantidad;

    /** Precio unitario al momento de la compra */
    @Column(name = "precio_unitario", nullable = false, precision = 10, scale = 2)
    private BigDecimal precioUnitario;
}
