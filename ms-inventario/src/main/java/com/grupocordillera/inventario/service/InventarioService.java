package com.grupocordillera.inventario.service;

import com.grupocordillera.inventario.dto.AlertaInventarioDTO;
import com.grupocordillera.inventario.dto.ProductoDTO;
import com.grupocordillera.inventario.dto.StockDTO;

import java.util.List;

/**
 * Interfaz del servicio de Inventario.
 * Define operaciones para gestionar productos, stock y alertas.
 */
public interface InventarioService {

    /** Lista todos los productos del catálogo */
    List<ProductoDTO> listarProductos();

    /** Lista solo productos activos */
    List<ProductoDTO> listarProductosActivos();

    /** Crea un nuevo producto en el catálogo */
    ProductoDTO crearProducto(ProductoDTO dto);

    /** Lista todos los registros de stock */
    List<StockDTO> listarStock();

    /** Lista el stock de un producto específico */
    List<StockDTO> listarStockPorProducto(Long productoId);

    /** Actualiza la cantidad de stock de un almacén */
    StockDTO actualizarStock(Long stockId, Integer nuevaCantidad);

    /** Crea un registro de stock para un producto en un almacén */
    StockDTO crearStock(StockDTO dto);

    /** Genera la lista de alertas de productos bajo el umbral mínimo */
    List<AlertaInventarioDTO> generarAlertas();

    /** Elimina un producto por su ID */
    void eliminarProducto(Long id);

    /** Elimina un registro de stock por su ID */
    void eliminarStock(Long id);
}
