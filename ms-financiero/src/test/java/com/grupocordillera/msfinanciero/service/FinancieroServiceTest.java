package com.grupocordillera.msfinanciero.service;

import com.grupocordillera.financiero.dto.EgresoDTO;
import com.grupocordillera.financiero.dto.IngresoDTO;
import com.grupocordillera.financiero.dto.KpiFinancieroDTO;
import com.grupocordillera.financiero.entity.Egreso;
import com.grupocordillera.financiero.entity.Ingreso;
import com.grupocordillera.financiero.factory.MovimientoFactory;
import com.grupocordillera.financiero.repository.EgresoRepository;
import com.grupocordillera.financiero.repository.IngresoRepository;
import com.grupocordillera.financiero.service.FinancieroServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FinancieroServiceTest {

    @Mock
    private IngresoRepository ingresoRepository;

    @Mock
    private EgresoRepository egresoRepository;

    @Mock
    private MovimientoFactory movimientoFactory;

    @InjectMocks
    private FinancieroServiceImpl financieroService;

    private Ingreso ingreso;
    private IngresoDTO ingresoDTO;
    private Egreso egreso;
    private EgresoDTO egresoDTO;

    @BeforeEach
    void setUp() {
        ingreso = new Ingreso(1L, "Venta productos", new BigDecimal("500000"), LocalDate.of(2025, 1, 15), "VENTAS");
        ingresoDTO = new IngresoDTO(1L, "Venta productos", new BigDecimal("500000"), LocalDate.of(2025, 1, 15), "VENTAS");

        egreso = new Egreso(1L, "Arriendo local", new BigDecimal("200000"), LocalDate.of(2025, 1, 15), "ARRIENDO");
        egresoDTO = new EgresoDTO(1L, "Arriendo local", new BigDecimal("200000"), LocalDate.of(2025, 1, 15), "ARRIENDO");
    }

    @Test
    void listarIngresos_retornaLista() {
        when(ingresoRepository.findAll()).thenReturn(List.of(ingreso));
        when(movimientoFactory.toDTO(ingreso)).thenReturn(ingresoDTO);

        List<IngresoDTO> resultado = financieroService.listarIngresos();

        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).getConcepto()).isEqualTo("Venta productos");
        verify(ingresoRepository).findAll();
    }

    @Test
    void listarIngresosPorPeriodo_filtraPorFechas() {
        LocalDate inicio = LocalDate.of(2025, 1, 1);
        LocalDate fin = LocalDate.of(2025, 1, 31);

        when(ingresoRepository.findByFechaBetween(inicio, fin)).thenReturn(List.of(ingreso));
        when(movimientoFactory.toDTO(ingreso)).thenReturn(ingresoDTO);

        List<IngresoDTO> resultado = financieroService.listarIngresosPorPeriodo(inicio, fin);

        assertThat(resultado).hasSize(1);
        verify(ingresoRepository).findByFechaBetween(inicio, fin);
    }

    @Test
    void registrarIngreso_guardaYRetornaDTO() {
        when(movimientoFactory.crearIngreso(ingresoDTO)).thenReturn(ingreso);
        when(ingresoRepository.save(ingreso)).thenReturn(ingreso);
        when(movimientoFactory.toDTO(ingreso)).thenReturn(ingresoDTO);

        IngresoDTO resultado = financieroService.registrarIngreso(ingresoDTO);

        assertThat(resultado.getMonto()).isEqualByComparingTo("500000");
        verify(ingresoRepository).save(ingreso);
    }

    @Test
    void listarEgresos_retornaLista() {
        when(egresoRepository.findAll()).thenReturn(List.of(egreso));
        when(movimientoFactory.toDTO(egreso)).thenReturn(egresoDTO);

        List<EgresoDTO> resultado = financieroService.listarEgresos();

        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).getConcepto()).isEqualTo("Arriendo local");
    }

    @Test
    void listarEgresosPorPeriodo_filtraPorFechas() {
        LocalDate inicio = LocalDate.of(2025, 1, 1);
        LocalDate fin = LocalDate.of(2025, 1, 31);

        when(egresoRepository.findByFechaBetween(inicio, fin)).thenReturn(List.of(egreso));
        when(movimientoFactory.toDTO(egreso)).thenReturn(egresoDTO);

        List<EgresoDTO> resultado = financieroService.listarEgresosPorPeriodo(inicio, fin);

        assertThat(resultado).hasSize(1);
        verify(egresoRepository).findByFechaBetween(inicio, fin);
    }

    @Test
    void registrarEgreso_guardaYRetornaDTO() {
        when(movimientoFactory.crearEgreso(egresoDTO)).thenReturn(egreso);
        when(egresoRepository.save(egreso)).thenReturn(egreso);
        when(movimientoFactory.toDTO(egreso)).thenReturn(egresoDTO);

        EgresoDTO resultado = financieroService.registrarEgreso(egresoDTO);

        assertThat(resultado.getMonto()).isEqualByComparingTo("200000");
        verify(egresoRepository).save(egreso);
    }

    @Test
    void eliminarIngreso_existente_elimina() {
        when(ingresoRepository.existsById(1L)).thenReturn(true);
        doNothing().when(ingresoRepository).deleteById(1L);

        financieroService.eliminarIngreso(1L);

        verify(ingresoRepository).deleteById(1L);
    }

    @Test
    void eliminarIngreso_noExiste_lanzaExcepcion() {
        when(ingresoRepository.existsById(99L)).thenReturn(false);

        assertThatThrownBy(() -> financieroService.eliminarIngreso(99L))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("99");

        verify(ingresoRepository, never()).deleteById(any());
    }

    @Test
    void eliminarEgreso_existente_elimina() {
        when(egresoRepository.existsById(1L)).thenReturn(true);
        doNothing().when(egresoRepository).deleteById(1L);

        financieroService.eliminarEgreso(1L);

        verify(egresoRepository).deleteById(1L);
    }

    @Test
    void eliminarEgreso_noExiste_lanzaExcepcion() {
        when(egresoRepository.existsById(99L)).thenReturn(false);

        assertThatThrownBy(() -> financieroService.eliminarEgreso(99L))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("99");

        verify(egresoRepository, never()).deleteById(any());
    }

    @Test
    void calcularKpis_calculaUtilidadYMargen() {
        LocalDate inicio = LocalDate.of(2025, 1, 1);
        LocalDate fin = LocalDate.of(2025, 1, 31);

        when(ingresoRepository.findByFechaBetween(inicio, fin)).thenReturn(List.of(ingreso));
        when(egresoRepository.findByFechaBetween(inicio, fin)).thenReturn(List.of(egreso));

        KpiFinancieroDTO kpi = financieroService.calcularKpis(inicio, fin);

        assertThat(kpi.getTotalIngresos()).isEqualByComparingTo("500000");
        assertThat(kpi.getTotalEgresos()).isEqualByComparingTo("200000");
        assertThat(kpi.getUtilidadBruta()).isEqualByComparingTo("300000");
        assertThat(kpi.getMargenRentabilidad()).isEqualByComparingTo("60.00");
    }

    @Test
    void calcularKpis_sinIngresos_margenEsCero() {
        LocalDate inicio = LocalDate.of(2025, 1, 1);
        LocalDate fin = LocalDate.of(2025, 1, 31);

        when(ingresoRepository.findByFechaBetween(inicio, fin)).thenReturn(Collections.emptyList());
        when(egresoRepository.findByFechaBetween(inicio, fin)).thenReturn(Collections.emptyList());

        KpiFinancieroDTO kpi = financieroService.calcularKpis(inicio, fin);

        assertThat(kpi.getTotalIngresos()).isEqualByComparingTo(BigDecimal.ZERO);
        assertThat(kpi.getMargenRentabilidad()).isEqualByComparingTo(BigDecimal.ZERO);
    }
}
