package com.grupocordillera.inventario.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para el nivel de stock de un producto en un almacén.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StockDTO {
    private Long id;
    private Long productoId;
    private Integer cantidad;
    private Integer umbralMinimo;
    private String almacen;
    /** Indica si la cantidad está por debajo del umbral mínimo */
    private Boolean alertaBajoStock;
}
