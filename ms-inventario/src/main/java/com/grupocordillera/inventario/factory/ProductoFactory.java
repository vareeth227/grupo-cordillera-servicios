package com.grupocordillera.inventario.factory;

import com.grupocordillera.inventario.dto.ProductoDTO;
import com.grupocordillera.inventario.dto.StockDTO;
import com.grupocordillera.inventario.entity.Producto;
import com.grupocordillera.inventario.entity.Stock;
import org.springframework.stereotype.Component;

/**
 * Factory para crear instancias de Producto y Stock.
 * Centraliza la conversión entre DTOs y entidades JPA.
 */
@Component
public class ProductoFactory {

    /** Crea un Producto activo a partir de su DTO */
    public Producto crearProducto(ProductoDTO dto) {
        Producto producto = new Producto();
        producto.setCodigo(dto.getCodigo());
        producto.setNombre(dto.getNombre());
        producto.setCategoria(dto.getCategoria());
        producto.setDescripcion(dto.getDescripcion());
        producto.setPrecio(dto.getPrecio());
        producto.setActivo(true);
        return producto;
    }

    /** Crea un registro de Stock a partir de su DTO */
    public Stock crearStock(StockDTO dto) {
        Stock stock = new Stock();
        stock.setProductoId(dto.getProductoId());
        stock.setCantidad(dto.getCantidad());
        stock.setUmbralMinimo(dto.getUmbralMinimo());
        stock.setAlmacen(dto.getAlmacen());
        return stock;
    }

    /** Convierte un Producto a DTO */
    public ProductoDTO toDTO(Producto producto) {
        return new ProductoDTO(
                producto.getId(), producto.getCodigo(), producto.getNombre(),
                producto.getCategoria(), producto.getDescripcion(),
                producto.getPrecio(), producto.getActivo()
        );
    }

    /** Convierte un Stock a DTO, calculando si está bajo el umbral */
    public StockDTO toDTO(Stock stock) {
        boolean bajoStock = stock.getCantidad() < stock.getUmbralMinimo();
        return new StockDTO(
                stock.getId(), stock.getProductoId(), stock.getCantidad(),
                stock.getUmbralMinimo(), stock.getAlmacen(), bajoStock
        );
    }
}
