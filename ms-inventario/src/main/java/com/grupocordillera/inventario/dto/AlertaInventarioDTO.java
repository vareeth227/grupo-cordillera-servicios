package com.grupocordillera.inventario.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para las alertas de inventario bajo.
 * Consolida la información del producto y su nivel de stock crítico.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AlertaInventarioDTO {
    private String codigoProducto;
    private String nombreProducto;
    private String almacen;
    private Integer cantidadActual;
    private Integer umbralMinimo;
    /** Unidades que faltan para alcanzar el umbral mínimo */
    private Integer unidadesFaltantes;
}
