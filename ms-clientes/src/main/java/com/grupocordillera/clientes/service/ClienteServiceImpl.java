package com.grupocordillera.clientes.service;

import com.grupocordillera.clientes.dto.ClienteDTO;
import com.grupocordillera.clientes.dto.TicketAtencionDTO;
import com.grupocordillera.clientes.entity.Cliente;
import com.grupocordillera.clientes.entity.TicketAtencion;
import com.grupocordillera.clientes.factory.ClienteFactory;
import com.grupocordillera.clientes.repository.ClienteRepository;
import com.grupocordillera.clientes.repository.TicketAtencionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementación del servicio de Clientes.
 * Gestiona el CRM: registro de clientes y ciclo de vida de tickets de atención.
 */
@Service
@RequiredArgsConstructor
public class ClienteServiceImpl implements ClienteService {

    private final ClienteRepository clienteRepository;
    private final TicketAtencionRepository ticketRepository;
    private final ClienteFactory clienteFactory;

    @Override
    @Transactional(readOnly = true)
    public List<ClienteDTO> listarClientes() {
        return clienteRepository.findAll().stream()
                .map(clienteFactory::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ClienteDTO> listarClientesActivos() {
        return clienteRepository.findByActivo(true).stream()
                .map(clienteFactory::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public ClienteDTO obtenerCliente(Long id) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado con ID: " + id));
        return clienteFactory.toDTO(cliente);
    }

    @Override
    @Transactional
    public ClienteDTO registrarCliente(ClienteDTO dto) {
        Cliente cliente = clienteFactory.crearCliente(dto);
        return clienteFactory.toDTO(clienteRepository.save(cliente));
    }

    @Override
    @Transactional(readOnly = true)
    public List<TicketAtencionDTO> listarTickets() {
        return ticketRepository.findAll().stream()
                .map(clienteFactory::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<TicketAtencionDTO> listarTicketsPorCliente(Long clienteId) {
        return ticketRepository.findByClienteId(clienteId).stream()
                .map(clienteFactory::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<TicketAtencionDTO> listarTicketsPorEstado(String estado) {
        return ticketRepository.findByEstado(estado).stream()
                .map(clienteFactory::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public TicketAtencionDTO crearTicket(TicketAtencionDTO dto) {
        TicketAtencion ticket = clienteFactory.crearTicket(dto);
        // Set referencia JPA para generar FK real en BD
        ticket.setCliente(clienteRepository.getReferenceById(dto.getClienteId()));
        return clienteFactory.toDTO(ticketRepository.save(ticket));
    }

    @Override
    @Transactional
    public void desactivarCliente(Long id) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado con ID: " + id));
        cliente.setActivo(false);
        clienteRepository.save(cliente);
    }

    @Override
    @Transactional
    public void eliminarCliente(Long id) {
        if (!clienteRepository.existsById(id))
            throw new RuntimeException("Cliente no encontrado con ID: " + id);
        clienteRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void eliminarTicket(Long id) {
        if (!ticketRepository.existsById(id))
            throw new RuntimeException("Ticket no encontrado con ID: " + id);
        ticketRepository.deleteById(id);
    }

    /**
     * Actualiza el estado del ticket.
     * Si el nuevo estado es RESUELTO o CERRADO, registra la fecha de resolución.
     */
    @Override
    @Transactional
    public TicketAtencionDTO actualizarEstadoTicket(Long ticketId, String nuevoEstado) {
        TicketAtencion ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new RuntimeException("Ticket no encontrado con ID: " + ticketId));
        ticket.setEstado(nuevoEstado);
        if ("RESUELTO".equals(nuevoEstado) || "CERRADO".equals(nuevoEstado)) {
            ticket.setFechaResolucion(LocalDateTime.now());
        }
        return clienteFactory.toDTO(ticketRepository.save(ticket));
    }
}
