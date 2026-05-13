package com.grupocordillera.inventario.controller;

import com.grupocordillera.inventario.dto.AlertaInventarioDTO;
import com.grupocordillera.inventario.dto.ProductoDTO;
import com.grupocordillera.inventario.dto.StockDTO;
import com.grupocordillera.inventario.service.InventarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST del microservicio de Inventario.
 * Expone endpoints para gestionar productos, stock y alertas de reposición.
 */
@RestController
@RequestMapping("/inventario")
@RequiredArgsConstructor
public class InventarioController {

    private final InventarioService inventarioService;

    /** Lista todos los productos del catálogo */
    @GetMapping("/productos")
    public ResponseEntity<List<ProductoDTO>> listarProductos() {
        return ResponseEntity.ok(inventarioService.listarProductos());
    }

    /** Lista solo productos activos */
    @GetMapping("/productos/activos")
    public ResponseEntity<List<ProductoDTO>> listarActivos() {
        return ResponseEntity.ok(inventarioService.listarProductosActivos());
    }

    /** Crea un nuevo producto en el catálogo */
    @PostMapping("/productos")
    public ResponseEntity<ProductoDTO> crearProducto(@RequestBody ProductoDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(inventarioService.crearProducto(dto));
    }

    /** Lista todos los registros de stock */
    @GetMapping("/stock")
    public ResponseEntity<List<StockDTO>> listarStock() {
        return ResponseEntity.ok(inventarioService.listarStock());
    }

    /** Lista el stock de un producto específico */
    @GetMapping("/stock/producto/{productoId}")
    public ResponseEntity<List<StockDTO>> stockPorProducto(@PathVariable Long productoId) {
        return ResponseEntity.ok(inventarioService.listarStockPorProducto(productoId));
    }

    /** Crea un nuevo registro de stock para un producto en un almacén */
    @PostMapping("/stock")
    public ResponseEntity<StockDTO> crearStock(@RequestBody StockDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(inventarioService.crearStock(dto));
    }

    /** Actualiza la cantidad de stock */
    @PutMapping("/stock/{id}")
    public ResponseEntity<StockDTO> actualizarStock(@PathVariable Long id, @RequestParam Integer cantidad) {
        return ResponseEntity.ok(inventarioService.actualizarStock(id, cantidad));
    }

    /** Retorna la lista de productos con stock bajo el umbral mínimo */
    @GetMapping("/alertas")
    public ResponseEntity<List<AlertaInventarioDTO>> alertas() {
        return ResponseEntity.ok(inventarioService.generarAlertas());
    }

    /** Elimina un producto del catálogo por su ID */
    @DeleteMapping("/productos/{id}")
    public ResponseEntity<Void> eliminarProducto(@PathVariable Long id) {
        inventarioService.eliminarProducto(id);
        return ResponseEntity.noContent().build();
    }

    /** Elimina un registro de stock por su ID */
    @DeleteMapping("/stock/{id}")
    public ResponseEntity<Void> eliminarStock(@PathVariable Long id) {
        inventarioService.eliminarStock(id);
        return ResponseEntity.noContent().build();
    }
}
