package com.grupocordillera.inventario.repository;

import com.grupocordillera.inventario.entity.Stock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repositorio JPA para el control de stock por almacén.
 */
@Repository
public interface StockRepository extends JpaRepository<Stock, Long> {

    /** Busca el stock de un producto en todos los almacenes */
    List<Stock> findByProductoId(Long productoId);

    /** Busca el stock en un almacén específico */
    List<Stock> findByAlmacen(String almacen);

    /**
     * Busca productos cuya cantidad está por debajo del umbral mínimo.
     * Estos son los que necesitan reposición urgente.
     */
    @Query("SELECT s FROM Stock s WHERE s.cantidad < s.umbralMinimo")
    List<Stock> findStockBajoUmbral();
}
