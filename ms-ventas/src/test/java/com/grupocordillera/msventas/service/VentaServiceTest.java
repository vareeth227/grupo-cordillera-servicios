package com.grupocordillera.msventas.service;

import com.grupocordillera.ventas.dto.PuntoDeVentaDTO;
import com.grupocordillera.ventas.dto.ReporteDiarioDTO;
import com.grupocordillera.ventas.dto.TransaccionDTO;
import com.grupocordillera.ventas.entity.PuntoDeVenta;
import com.grupocordillera.ventas.entity.Transaccion;
import com.grupocordillera.ventas.factory.TransaccionFactory;
import com.grupocordillera.ventas.repository.PuntoDeVentaRepository;
import com.grupocordillera.ventas.repository.TransaccionRepository;
import com.grupocordillera.ventas.service.VentaServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VentaServiceTest {

    @Mock
    private PuntoDeVentaRepository puntoDeVentaRepository;

    @Mock
    private TransaccionRepository transaccionRepository;

    @Mock
    private TransaccionFactory transaccionFactory;

    @InjectMocks
    private VentaServiceImpl ventaService;

    private PuntoDeVenta puntoDeVenta;
    private PuntoDeVentaDTO puntoDeVentaDTO;
    private Transaccion transaccion;
    private TransaccionDTO transaccionDTO;

    @BeforeEach
    void setUp() {
        puntoDeVenta = new PuntoDeVenta(1L, "Tienda Centro", "Av. Principal 123", "RM", true);
        puntoDeVentaDTO = new PuntoDeVentaDTO(1L, "Tienda Centro", "Av. Principal 123", "RM", true);

        transaccion = new Transaccion(1L, LocalDateTime.now(), new BigDecimal("50000"), 1L, "SKU-001", 2, "VENTA");
        transaccionDTO = new TransaccionDTO(1L, LocalDateTime.now(), new BigDecimal("50000"), 1L, "SKU-001", 2, "VENTA");
    }

    @Test
    void listarPuntosDeVenta_retornaLista() {
        when(puntoDeVentaRepository.findAll()).thenReturn(List.of(puntoDeVenta));

        List<PuntoDeVentaDTO> resultado = ventaService.listarPuntosDeVenta();

        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).getNombre()).isEqualTo("Tienda Centro");
    }

    @Test
    void listarPuntosDeVentaActivos_soloRetornaActivos() {
        when(puntoDeVentaRepository.findByActivo(true)).thenReturn(List.of(puntoDeVenta));

        List<PuntoDeVentaDTO> resultado = ventaService.listarPuntosDeVentaActivos();

        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).getActivo()).isTrue();
        verify(puntoDeVentaRepository).findByActivo(true);
    }

    @Test
    void crearPuntoDeVenta_guardaYRetornaDTO() {
        when(puntoDeVentaRepository.save(any(PuntoDeVenta.class))).thenReturn(puntoDeVenta);

        PuntoDeVentaDTO resultado = ventaService.crearPuntoDeVenta(puntoDeVentaDTO);

        assertThat(resultado.getNombre()).isEqualTo("Tienda Centro");
        verify(puntoDeVentaRepository).save(any(PuntoDeVenta.class));
    }

    @Test
    void listarTransacciones_retornaLista() {
        when(transaccionRepository.findAll()).thenReturn(List.of(transaccion));
        when(transaccionFactory.toDTO(transaccion)).thenReturn(transaccionDTO);

        List<TransaccionDTO> resultado = ventaService.listarTransacciones();

        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).getTipo()).isEqualTo("VENTA");
    }

    @Test
    void registrarVenta_creaYPersiste() {
        when(transaccionFactory.crearVenta(transaccionDTO)).thenReturn(transaccion);
        when(transaccionRepository.save(transaccion)).thenReturn(transaccion);
        when(transaccionFactory.toDTO(transaccion)).thenReturn(transaccionDTO);

        TransaccionDTO resultado = ventaService.registrarVenta(transaccionDTO);

        assertThat(resultado.getTipo()).isEqualTo("VENTA");
        verify(transaccionRepository).save(transaccion);
    }

    @Test
    void registrarDevolucion_creaYPersiste() {
        Transaccion devolucion = new Transaccion(2L, LocalDateTime.now(), new BigDecimal("50000"), 1L, "SKU-001", 2, "DEVOLUCION");
        TransaccionDTO devolucionDTO = new TransaccionDTO(2L, LocalDateTime.now(), new BigDecimal("50000"), 1L, "SKU-001", 2, "DEVOLUCION");

        when(transaccionFactory.crearDevolucion(transaccionDTO)).thenReturn(devolucion);
        when(transaccionRepository.save(devolucion)).thenReturn(devolucion);
        when(transaccionFactory.toDTO(devolucion)).thenReturn(devolucionDTO);

        TransaccionDTO resultado = ventaService.registrarDevolucion(transaccionDTO);

        assertThat(resultado.getTipo()).isEqualTo("DEVOLUCION");
    }

    @Test
    void eliminarPuntoDeVenta_existente_elimina() {
        when(puntoDeVentaRepository.existsById(1L)).thenReturn(true);
        doNothing().when(puntoDeVentaRepository).deleteById(1L);

        ventaService.eliminarPuntoDeVenta(1L);

        verify(puntoDeVentaRepository).deleteById(1L);
    }

    @Test
    void eliminarPuntoDeVenta_noExiste_lanzaExcepcion() {
        when(puntoDeVentaRepository.existsById(99L)).thenReturn(false);

        assertThatThrownBy(() -> ventaService.eliminarPuntoDeVenta(99L))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("99");

        verify(puntoDeVentaRepository, never()).deleteById(any());
    }

    @Test
    void eliminarTransaccion_existente_elimina() {
        when(transaccionRepository.existsById(1L)).thenReturn(true);
        doNothing().when(transaccionRepository).deleteById(1L);

        ventaService.eliminarTransaccion(1L);

        verify(transaccionRepository).deleteById(1L);
    }

    @Test
    void eliminarTransaccion_noExiste_lanzaExcepcion() {
        when(transaccionRepository.existsById(99L)).thenReturn(false);

        assertThatThrownBy(() -> ventaService.eliminarTransaccion(99L))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("99");
    }

    @Test
    void generarReporteDiario_calculaTotalesCorrectamente() {
        LocalDate fecha = LocalDate.of(2025, 1, 15);
        Transaccion venta = new Transaccion(1L, fecha.atTime(10, 0), new BigDecimal("100000"), 1L, "SKU-001", 1, "VENTA");
        Transaccion devolucion = new Transaccion(2L, fecha.atTime(11, 0), new BigDecimal("20000"), 1L, "SKU-002", 1, "DEVOLUCION");

        when(transaccionRepository.findTransaccionesDia(any(), any())).thenReturn(List.of(venta, devolucion));

        ReporteDiarioDTO reporte = ventaService.generarReporteDiario(fecha);

        assertThat(reporte.getTotalVentas()).isEqualByComparingTo("100000");
        assertThat(reporte.getTotalDevoluciones()).isEqualByComparingTo("20000");
        assertThat(reporte.getMontoNeto()).isEqualByComparingTo("80000");
        assertThat(reporte.getNumeroTransacciones()).isEqualTo(2L);
    }

    @Test
    void generarReporteDiario_sinTransacciones_retornaCeros() {
        LocalDate fecha = LocalDate.of(2025, 1, 15);
        when(transaccionRepository.findTransaccionesDia(any(), any())).thenReturn(Collections.emptyList());

        ReporteDiarioDTO reporte = ventaService.generarReporteDiario(fecha);

        assertThat(reporte.getTotalVentas()).isEqualByComparingTo(BigDecimal.ZERO);
        assertThat(reporte.getMontoNeto()).isEqualByComparingTo(BigDecimal.ZERO);
        assertThat(reporte.getNumeroTransacciones()).isEqualTo(0L);
    }
}
