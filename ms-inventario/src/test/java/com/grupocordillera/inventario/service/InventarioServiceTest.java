package com.grupocordillera.inventario.service;

import com.grupocordillera.inventario.dto.AlertaInventarioDTO;
import com.grupocordillera.inventario.dto.ProductoDTO;
import com.grupocordillera.inventario.dto.StockDTO;
import com.grupocordillera.inventario.entity.Producto;
import com.grupocordillera.inventario.entity.Stock;
import com.grupocordillera.inventario.factory.ProductoFactory;
import com.grupocordillera.inventario.repository.ProductoRepository;
import com.grupocordillera.inventario.repository.StockRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InventarioServiceTest {

    @Mock
    private ProductoRepository productoRepository;

    @Mock
    private StockRepository stockRepository;

    @Mock
    private ProductoFactory productoFactory;

    @InjectMocks
    private InventarioServiceImpl inventarioService;

    private Producto producto;
    private ProductoDTO productoDTO;
    private Stock stock;
    private StockDTO stockDTO;

    @BeforeEach
    void setUp() {
        producto = new Producto(1L, "SKU-001", "Laptop", "Electrónica", "Laptop de alta gama", new BigDecimal("999.99"), true);
        productoDTO = new ProductoDTO(1L, "SKU-001", "Laptop", "Electrónica", "Laptop de alta gama", new BigDecimal("999.99"), true);

        stock = new Stock(1L, 1L, 10, 5, "Almacén Central");
        stockDTO = new StockDTO(1L, 1L, 10, 5, "Almacén Central", false);
    }

    @Test
    void listarProductos_retornaListaCompleta() {
        when(productoRepository.findAll()).thenReturn(List.of(producto));
        when(productoFactory.toDTO(producto)).thenReturn(productoDTO);

        List<ProductoDTO> resultado = inventarioService.listarProductos();

        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).getCodigo()).isEqualTo("SKU-001");
        verify(productoRepository).findAll();
    }

    @Test
    void listarProductos_listaVacia_retornaListaVacia() {
        when(productoRepository.findAll()).thenReturn(Collections.emptyList());

        List<ProductoDTO> resultado = inventarioService.listarProductos();

        assertThat(resultado).isEmpty();
    }

    @Test
    void listarProductosActivos_soloRetornaActivos() {
        when(productoRepository.findByActivo(true)).thenReturn(List.of(producto));
        when(productoFactory.toDTO(producto)).thenReturn(productoDTO);

        List<ProductoDTO> resultado = inventarioService.listarProductosActivos();

        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).getActivo()).isTrue();
        verify(productoRepository).findByActivo(true);
    }

    @Test
    void crearProducto_guardaYRetornaDTO() {
        when(productoFactory.crearProducto(productoDTO)).thenReturn(producto);
        when(productoRepository.save(producto)).thenReturn(producto);
        when(productoFactory.toDTO(producto)).thenReturn(productoDTO);

        ProductoDTO resultado = inventarioService.crearProducto(productoDTO);

        assertThat(resultado.getCodigo()).isEqualTo("SKU-001");
        assertThat(resultado.getNombre()).isEqualTo("Laptop");
        verify(productoRepository).save(producto);
    }

    @Test
    void listarStock_retornaListaCompleta() {
        when(stockRepository.findAll()).thenReturn(List.of(stock));
        when(productoFactory.toDTO(stock)).thenReturn(stockDTO);

        List<StockDTO> resultado = inventarioService.listarStock();

        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).getAlmacen()).isEqualTo("Almacén Central");
        verify(stockRepository).findAll();
    }

    @Test
    void listarStockPorProducto_retornaStockDelProducto() {
        when(stockRepository.findByProductoId(1L)).thenReturn(List.of(stock));
        when(productoFactory.toDTO(stock)).thenReturn(stockDTO);

        List<StockDTO> resultado = inventarioService.listarStockPorProducto(1L);

        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).getProductoId()).isEqualTo(1L);
    }

    @Test
    void actualizarStock_existente_actualizaCantidad() {
        Stock stockActualizado = new Stock(1L, 1L, 20, 5, "Almacén Central");
        StockDTO stockDTOActualizado = new StockDTO(1L, 1L, 20, 5, "Almacén Central", false);

        when(stockRepository.findById(1L)).thenReturn(Optional.of(stock));
        when(stockRepository.save(any(Stock.class))).thenReturn(stockActualizado);
        when(productoFactory.toDTO(stockActualizado)).thenReturn(stockDTOActualizado);

        StockDTO resultado = inventarioService.actualizarStock(1L, 20);

        assertThat(resultado.getCantidad()).isEqualTo(20);
        verify(stockRepository).save(any(Stock.class));
    }

    @Test
    void actualizarStock_noExiste_lanzaExcepcion() {
        when(stockRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> inventarioService.actualizarStock(99L, 10))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Stock no encontrado con ID: 99");
    }

    @Test
    void crearStock_guardaYRetornaDTO() {
        when(productoFactory.crearStock(stockDTO)).thenReturn(stock);
        when(stockRepository.save(stock)).thenReturn(stock);
        when(productoFactory.toDTO(stock)).thenReturn(stockDTO);

        StockDTO resultado = inventarioService.crearStock(stockDTO);

        assertThat(resultado.getAlmacen()).isEqualTo("Almacén Central");
        verify(stockRepository).save(stock);
    }

    @Test
    void eliminarProducto_existente_eliminaCorrectamente() {
        when(productoRepository.existsById(1L)).thenReturn(true);
        doNothing().when(productoRepository).deleteById(1L);

        inventarioService.eliminarProducto(1L);

        verify(productoRepository).deleteById(1L);
    }

    @Test
    void eliminarProducto_noExiste_lanzaExcepcion() {
        when(productoRepository.existsById(99L)).thenReturn(false);

        assertThatThrownBy(() -> inventarioService.eliminarProducto(99L))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Producto no encontrado con ID: 99");

        verify(productoRepository, never()).deleteById(any());
    }

    @Test
    void eliminarStock_existente_eliminaCorrectamente() {
        when(stockRepository.existsById(1L)).thenReturn(true);
        doNothing().when(stockRepository).deleteById(1L);

        inventarioService.eliminarStock(1L);

        verify(stockRepository).deleteById(1L);
    }

    @Test
    void eliminarStock_noExiste_lanzaExcepcion() {
        when(stockRepository.existsById(99L)).thenReturn(false);

        assertThatThrownBy(() -> inventarioService.eliminarStock(99L))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Stock no encontrado con ID: 99");

        verify(stockRepository, never()).deleteById(any());
    }

    @Test
    void generarAlertas_stockBajoUmbral_retornaAlertas() {
        Stock stockBajo = new Stock(2L, 1L, 2, 10, "Almacén Norte");

        when(stockRepository.findStockBajoUmbral()).thenReturn(List.of(stockBajo));
        when(productoRepository.findById(1L)).thenReturn(Optional.of(producto));

        List<AlertaInventarioDTO> alertas = inventarioService.generarAlertas();

        assertThat(alertas).hasSize(1);
        AlertaInventarioDTO alerta = alertas.get(0);
        assertThat(alerta.getCodigoProducto()).isEqualTo("SKU-001");
        assertThat(alerta.getNombreProducto()).isEqualTo("Laptop");
        assertThat(alerta.getCantidadActual()).isEqualTo(2);
        assertThat(alerta.getUmbralMinimo()).isEqualTo(10);
        assertThat(alerta.getUnidadesFaltantes()).isEqualTo(8);
    }

    @Test
    void generarAlertas_productoNoEncontrado_usaValoresPorDefecto() {
        Stock stockBajo = new Stock(2L, 99L, 0, 5, "Almacén Sur");

        when(stockRepository.findStockBajoUmbral()).thenReturn(List.of(stockBajo));
        when(productoRepository.findById(99L)).thenReturn(Optional.empty());

        List<AlertaInventarioDTO> alertas = inventarioService.generarAlertas();

        assertThat(alertas).hasSize(1);
        assertThat(alertas.get(0).getCodigoProducto()).isEqualTo("DESCONOCIDO");
        assertThat(alertas.get(0).getNombreProducto()).isEqualTo("Producto no encontrado");
    }

    @Test
    void generarAlertas_sinStockBajo_retornaListaVacia() {
        when(stockRepository.findStockBajoUmbral()).thenReturn(Collections.emptyList());

        List<AlertaInventarioDTO> alertas = inventarioService.generarAlertas();

        assertThat(alertas).isEmpty();
    }
}
