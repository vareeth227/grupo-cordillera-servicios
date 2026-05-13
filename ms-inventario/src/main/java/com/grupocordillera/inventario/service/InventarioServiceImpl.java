package com.grupocordillera.inventario.service;

import com.grupocordillera.inventario.dto.AlertaInventarioDTO;
import com.grupocordillera.inventario.dto.ProductoDTO;
import com.grupocordillera.inventario.dto.StockDTO;
import com.grupocordillera.inventario.entity.Producto;
import com.grupocordillera.inventario.entity.Stock;
import com.grupocordillera.inventario.factory.ProductoFactory;
import com.grupocordillera.inventario.repository.ProductoRepository;
import com.grupocordillera.inventario.repository.StockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementación del servicio de Inventario.
 * Gestiona el catálogo de productos, niveles de stock y alertas de reposición.
 */
@Service
@RequiredArgsConstructor
public class InventarioServiceImpl implements InventarioService {

    private final ProductoRepository productoRepository;
    private final StockRepository stockRepository;
    private final ProductoFactory productoFactory;

    @Override
    @Transactional(readOnly = true)
    public List<ProductoDTO> listarProductos() {
        return productoRepository.findAll().stream()
                .map(productoFactory::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductoDTO> listarProductosActivos() {
        return productoRepository.findByActivo(true).stream()
                .map(productoFactory::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ProductoDTO crearProducto(ProductoDTO dto) {
        Producto producto = productoFactory.crearProducto(dto);
        return productoFactory.toDTO(productoRepository.save(producto));
    }

    @Override
    @Transactional(readOnly = true)
    public List<StockDTO> listarStock() {
        return stockRepository.findAll().stream()
                .map(productoFactory::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<StockDTO> listarStockPorProducto(Long productoId) {
        return stockRepository.findByProductoId(productoId).stream()
                .map(productoFactory::toDTO)
                .collect(Collectors.toList());
    }

    /** Actualiza la cantidad de stock y detecta si queda bajo el umbral */
    @Override
    @Transactional
    public StockDTO actualizarStock(Long stockId, Integer nuevaCantidad) {
        Stock stock = stockRepository.findById(stockId)
                .orElseThrow(() -> new RuntimeException("Stock no encontrado con ID: " + stockId));
        stock.setCantidad(nuevaCantidad);
        return productoFactory.toDTO(stockRepository.save(stock));
    }

    @Override
    @Transactional
    public StockDTO crearStock(StockDTO dto) {
        Stock stock = productoFactory.crearStock(dto);
        return productoFactory.toDTO(stockRepository.save(stock));
    }

    @Override
    @Transactional
    public void eliminarProducto(Long id) {
        if (!productoRepository.existsById(id))
            throw new RuntimeException("Producto no encontrado con ID: " + id);
        productoRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void eliminarStock(Long id) {
        if (!stockRepository.existsById(id))
            throw new RuntimeException("Stock no encontrado con ID: " + id);
        stockRepository.deleteById(id);
    }

    /**
     * Detecta todos los productos cuyo stock está por debajo del umbral mínimo.
     * Cruza los datos de stock con el catálogo de productos para generar la alerta completa.
     */
    @Override
    @Transactional(readOnly = true)
    public List<AlertaInventarioDTO> generarAlertas() {
        List<Stock> stocksBajos = stockRepository.findStockBajoUmbral();

        return stocksBajos.stream().map(stock -> {
            Producto producto = productoRepository.findById(stock.getProductoId())
                    .orElse(null);
            String codigo = producto != null ? producto.getCodigo() : "DESCONOCIDO";
            String nombre = producto != null ? producto.getNombre() : "Producto no encontrado";
            int faltantes = stock.getUmbralMinimo() - stock.getCantidad();

            return new AlertaInventarioDTO(codigo, nombre, stock.getAlmacen(),
                    stock.getCantidad(), stock.getUmbralMinimo(), faltantes);
        }).collect(Collectors.toList());
    }
}
