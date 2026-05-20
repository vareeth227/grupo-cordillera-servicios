package com.grupocordillera.ecommerce.service;

import com.grupocordillera.ecommerce.dto.PedidoDTO;
import com.grupocordillera.ecommerce.entity.Pedido;
import com.grupocordillera.ecommerce.factory.PedidoFactory;
import com.grupocordillera.ecommerce.repository.ItemPedidoRepository;
import com.grupocordillera.ecommerce.repository.PedidoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PedidoServiceTest {

    @Mock private PedidoRepository pedidoRepository;
    @Mock private ItemPedidoRepository itemPedidoRepository;
    @Mock private PedidoFactory pedidoFactory;
    @Mock private RestTemplate restTemplate;

    @InjectMocks private PedidoServiceImpl pedidoService;

    private Pedido pedido;
    private PedidoDTO pedidoDTO;

    @BeforeEach
    void setUp() {
        pedido = new Pedido();
        pedido.setId(1L);
        pedido.setClienteId(10L);
        pedido.setEstado("PENDIENTE");
        pedido.setTotal(new BigDecimal("5000"));
        pedido.setFechaPedido(LocalDateTime.now());

        pedidoDTO = new PedidoDTO();
        pedidoDTO.setId(1L);
        pedidoDTO.setClienteId(10L);
        pedidoDTO.setEstado("PENDIENTE");
        pedidoDTO.setTotal(new BigDecimal("5000"));
        pedidoDTO.setItems(List.of());
    }

    @Test
    void listarPedidos_retornaListaDeDTOs() {
        when(pedidoRepository.findAll()).thenReturn(List.of(pedido));
        when(itemPedidoRepository.findByPedidoId(1L)).thenReturn(List.of());
        when(pedidoFactory.toDTO(pedido, List.of())).thenReturn(pedidoDTO);

        List<PedidoDTO> resultado = pedidoService.listarPedidos();

        assertEquals(1, resultado.size());
        assertEquals("PENDIENTE", resultado.get(0).getEstado());
        verify(pedidoRepository).findAll();
    }

    @Test
    void obtenerPedido_lanzaExcepcionSiNoExiste() {
        when(pedidoRepository.findById(99L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> pedidoService.obtenerPedido(99L));

        assertTrue(ex.getMessage().contains("99"));
    }

    @Test
    void obtenerPedido_retornaDTOCuandoExiste() {
        when(pedidoRepository.findById(1L)).thenReturn(Optional.of(pedido));
        when(itemPedidoRepository.findByPedidoId(1L)).thenReturn(List.of());
        when(pedidoFactory.toDTO(pedido, List.of())).thenReturn(pedidoDTO);

        PedidoDTO resultado = pedidoService.obtenerPedido(1L);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
    }

    @Test
    void actualizarEstado_cambiaNuevoEstadoYPersiste() {
        PedidoDTO dtoActualizado = new PedidoDTO();
        dtoActualizado.setId(1L);
        dtoActualizado.setEstado("CONFIRMADO");
        dtoActualizado.setItems(List.of());

        when(pedidoRepository.findById(1L)).thenReturn(Optional.of(pedido));
        when(pedidoRepository.save(pedido)).thenReturn(pedido);
        when(itemPedidoRepository.findByPedidoId(1L)).thenReturn(List.of());
        when(pedidoFactory.toDTO(pedido, List.of())).thenReturn(dtoActualizado);

        PedidoDTO resultado = pedidoService.actualizarEstado(1L, "CONFIRMADO");

        assertEquals("CONFIRMADO", resultado.getEstado());
        verify(pedidoRepository).save(pedido);
    }

    @Test
    void eliminarPedido_lanzaExcepcionSiNoExiste() {
        when(pedidoRepository.existsById(99L)).thenReturn(false);

        assertThrows(RuntimeException.class, () -> pedidoService.eliminarPedido(99L));
        verify(pedidoRepository, never()).deleteById(any());
    }

    @Test
    void listarPedidosPorEstado_filtraCorrectamente() {
        when(pedidoRepository.findByEstado("PENDIENTE")).thenReturn(List.of(pedido));
        when(itemPedidoRepository.findByPedidoId(1L)).thenReturn(List.of());
        when(pedidoFactory.toDTO(pedido, List.of())).thenReturn(pedidoDTO);

        List<PedidoDTO> resultado = pedidoService.listarPedidosPorEstado("PENDIENTE");

        assertEquals(1, resultado.size());
        verify(pedidoRepository).findByEstado("PENDIENTE");
    }
}
