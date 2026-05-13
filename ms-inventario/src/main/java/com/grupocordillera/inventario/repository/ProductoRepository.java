package com.grupocordillera.inventario.repository;

import com.grupocordillera.inventario.entity.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repositorio JPA para el catálogo de productos.
 */
@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {

    /** Busca un producto por su código SKU único */
    Optional<Producto> findByCodigo(String codigo);

    /** Lista productos por categoría */
    List<Producto> findByCategoria(String categoria);

    /** Lista solo los productos activos en el catálogo */
    List<Producto> findByActivo(Boolean activo);
}
