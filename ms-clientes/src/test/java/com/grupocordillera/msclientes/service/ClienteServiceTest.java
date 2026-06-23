package com.grupocordillera.msclientes.service;

import com.grupocordillera.clientes.dto.ClienteDTO;
import com.grupocordillera.clientes.dto.TicketAtencionDTO;
import com.grupocordillera.clientes.entity.Cliente;
import com.grupocordillera.clientes.entity.TicketAtencion;
import com.grupocordillera.clientes.factory.ClienteFactory;
import com.grupocordillera.clientes.repository.ClienteRepository;
import com.grupocordillera.clientes.repository.TicketAtencionRepository;
import com.grupocordillera.clientes.service.ClienteServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClienteServiceTest {

    @Mock
    private ClienteRepository clienteRepository;

    @Mock
    private TicketAtencionRepository ticketRepository;

    @Mock
    private ClienteFactory clienteFactory;

    @InjectMocks
    private ClienteServiceImpl clienteService;

    private Cliente cliente;
    private ClienteDTO clienteDTO;
    private TicketAtencion ticket;
    private TicketAtencionDTO ticketDTO;

    @BeforeEach
    void setUp() {
        cliente = new Cliente(1L, "Juan", "Pérez", "juan@example.com", "+56912345678", LocalDate.of(2024, 1, 1), true, null);
        clienteDTO = new ClienteDTO(1L, "Juan", "Pérez", "juan@example.com", "+56912345678", LocalDate.of(2024, 1, 1), true);

        ticket = new TicketAtencion();
        ticket.setId(1L);
        ticket.setClienteId(1L);
        ticket.setAsunto("Problema con pedido");
        ticket.setEstado("ABIERTO");
        ticket.setCategoria("RECLAMO");
        ticket.setFechaCreacion(LocalDateTime.now());

        ticketDTO = new TicketAtencionDTO(1L, 1L, "Problema con pedido", null, "ABIERTO", "RECLAMO", LocalDateTime.now(), null);
    }

    @Test
    void listarClientes_retornaLista() {
        when(clienteRepository.findAll()).thenReturn(List.of(cliente));
        when(clienteFactory.toDTO(cliente)).thenReturn(clienteDTO);

        List<ClienteDTO> resultado = clienteService.listarClientes();

        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).getNombre()).isEqualTo("Juan");
        verify(clienteRepository).findAll();
    }

    @Test
    void listarClientesActivos_soloRetornaActivos() {
        when(clienteRepository.findByActivo(true)).thenReturn(List.of(cliente));
        when(clienteFactory.toDTO(cliente)).thenReturn(clienteDTO);

        List<ClienteDTO> resultado = clienteService.listarClientesActivos();

        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).getActivo()).isTrue();
    }

    @Test
    void obtenerCliente_existente_retornaDTO() {
        when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));
        when(clienteFactory.toDTO(cliente)).thenReturn(clienteDTO);

        ClienteDTO resultado = clienteService.obtenerCliente(1L);

        assertThat(resultado.getEmail()).isEqualTo("juan@example.com");
    }

    @Test
    void obtenerCliente_noExiste_lanzaExcepcion() {
        when(clienteRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> clienteService.obtenerCliente(99L))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("99");
    }

    @Test
    void registrarCliente_guardaYRetornaDTO() {
        when(clienteFactory.crearCliente(clienteDTO)).thenReturn(cliente);
        when(clienteRepository.save(cliente)).thenReturn(cliente);
        when(clienteFactory.toDTO(cliente)).thenReturn(clienteDTO);

        ClienteDTO resultado = clienteService.registrarCliente(clienteDTO);

        assertThat(resultado.getNombre()).isEqualTo("Juan");
        verify(clienteRepository).save(cliente);
    }

    @Test
    void listarTickets_retornaLista() {
        when(ticketRepository.findAll()).thenReturn(List.of(ticket));
        when(clienteFactory.toDTO(ticket)).thenReturn(ticketDTO);

        List<TicketAtencionDTO> resultado = clienteService.listarTickets();

        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).getEstado()).isEqualTo("ABIERTO");
    }

    @Test
    void listarTicketsPorCliente_retornaTicketsDelCliente() {
        when(ticketRepository.findByClienteId(1L)).thenReturn(List.of(ticket));
        when(clienteFactory.toDTO(ticket)).thenReturn(ticketDTO);

        List<TicketAtencionDTO> resultado = clienteService.listarTicketsPorCliente(1L);

        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).getClienteId()).isEqualTo(1L);
    }

    @Test
    void listarTicketsPorEstado_filtraCorrectamente() {
        when(ticketRepository.findByEstado("ABIERTO")).thenReturn(List.of(ticket));
        when(clienteFactory.toDTO(ticket)).thenReturn(ticketDTO);

        List<TicketAtencionDTO> resultado = clienteService.listarTicketsPorEstado("ABIERTO");

        assertThat(resultado).hasSize(1);
        verify(ticketRepository).findByEstado("ABIERTO");
    }

    @Test
    void desactivarCliente_existente_marcaInactivo() {
        when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));
        when(clienteRepository.save(cliente)).thenReturn(cliente);

        clienteService.desactivarCliente(1L);

        assertThat(cliente.getActivo()).isFalse();
        verify(clienteRepository).save(cliente);
    }

    @Test
    void desactivarCliente_noExiste_lanzaExcepcion() {
        when(clienteRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> clienteService.desactivarCliente(99L))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("99");
    }

    @Test
    void eliminarCliente_existente_elimina() {
        when(clienteRepository.existsById(1L)).thenReturn(true);
        doNothing().when(clienteRepository).deleteById(1L);

        clienteService.eliminarCliente(1L);

        verify(clienteRepository).deleteById(1L);
    }

    @Test
    void eliminarCliente_noExiste_lanzaExcepcion() {
        when(clienteRepository.existsById(99L)).thenReturn(false);

        assertThatThrownBy(() -> clienteService.eliminarCliente(99L))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("99");

        verify(clienteRepository, never()).deleteById(any());
    }

    @Test
    void eliminarTicket_existente_elimina() {
        when(ticketRepository.existsById(1L)).thenReturn(true);
        doNothing().when(ticketRepository).deleteById(1L);

        clienteService.eliminarTicket(1L);

        verify(ticketRepository).deleteById(1L);
    }

    @Test
    void eliminarTicket_noExiste_lanzaExcepcion() {
        when(ticketRepository.existsById(99L)).thenReturn(false);

        assertThatThrownBy(() -> clienteService.eliminarTicket(99L))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("99");
    }

    @Test
    void actualizarEstadoTicket_aResuelto_registraFechaResolucion() {
        TicketAtencionDTO resueltoDTO = new TicketAtencionDTO(1L, 1L, "Problema con pedido", null, "RESUELTO", "RECLAMO", LocalDateTime.now(), LocalDateTime.now());

        when(ticketRepository.findById(1L)).thenReturn(Optional.of(ticket));
        when(ticketRepository.save(ticket)).thenReturn(ticket);
        when(clienteFactory.toDTO(ticket)).thenReturn(resueltoDTO);

        TicketAtencionDTO resultado = clienteService.actualizarEstadoTicket(1L, "RESUELTO");

        assertThat(ticket.getEstado()).isEqualTo("RESUELTO");
        assertThat(ticket.getFechaResolucion()).isNotNull();
        assertThat(resultado.getEstado()).isEqualTo("RESUELTO");
    }

    @Test
    void actualizarEstadoTicket_noExiste_lanzaExcepcion() {
        when(ticketRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> clienteService.actualizarEstadoTicket(99L, "RESUELTO"))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("99");
    }
}
