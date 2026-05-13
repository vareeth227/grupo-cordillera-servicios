package com.grupocordillera.ecommerce.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "pedidos")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** ID del cliente que realizó el pedido (referencia al ms-clientes) */
    @Column(name = "cliente_id", nullable = false)
    private Long clienteId;

    /** Fecha y hora en que se creó el pedido */
    @Column(name = "fecha_pedido", nullable = false)
    private LocalDateTime fechaPedido;

    /** Estado del pedido: PENDIENTE, CONFIRMADO, EN_ENVIO, ENTREGADO, CANCELADO */
    @Column(nullable = false, length = 20)
    private String estado;

    /** Monto total del pedido */
    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal total;

    /** Dirección de despacho para este pedido */
    @Column(name = "direccion_despacho")
    private String direccionDespacho;

    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ItemPedido> items;
}
