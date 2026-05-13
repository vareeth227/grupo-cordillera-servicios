package com.grupocordillera.clientes.factory;

import com.grupocordillera.clientes.dto.ClienteDTO;
import com.grupocordillera.clientes.dto.TicketAtencionDTO;
import com.grupocordillera.clientes.entity.Cliente;
import com.grupocordillera.clientes.entity.TicketAtencion;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Factory para crear instancias de Cliente y TicketAtencion.
 * Implementa el patrón Factory Method para centralizar la creación de entidades.
 */
@Component
public class ClienteFactory {

    /**
     * Crea un nuevo Cliente activo a partir de su DTO.
     * Asigna la fecha de registro actual automáticamente.
     */
    public Cliente crearCliente(ClienteDTO dto) {
        Cliente cliente = new Cliente();
        cliente.setNombre(dto.getNombre());
        cliente.setApellido(dto.getApellido());
        cliente.setEmail(dto.getEmail());
        cliente.setTelefono(dto.getTelefono());
        cliente.setFechaRegistro(LocalDate.now());
        cliente.setActivo(true);
        return cliente;
    }

    /**
     * Crea un TicketAtencion en estado ABIERTO.
     * Asigna la fecha de creación actual automáticamente.
     */
    public TicketAtencion crearTicket(TicketAtencionDTO dto) {
        TicketAtencion ticket = new TicketAtencion();
        ticket.setClienteId(dto.getClienteId());
        ticket.setAsunto(dto.getAsunto());
        ticket.setDescripcion(dto.getDescripcion());
        ticket.setEstado("ABIERTO");
        ticket.setCategoria(dto.getCategoria());
        ticket.setFechaCreacion(LocalDateTime.now());
        ticket.setFechaResolucion(null);
        return ticket;
    }

    /** Convierte un Cliente a su DTO */
    public ClienteDTO toDTO(Cliente cliente) {
        return new ClienteDTO(
                cliente.getId(), cliente.getNombre(), cliente.getApellido(),
                cliente.getEmail(), cliente.getTelefono(),
                cliente.getFechaRegistro(), cliente.getActivo()
        );
    }

    /** Convierte un TicketAtencion a su DTO */
    public TicketAtencionDTO toDTO(TicketAtencion ticket) {
        return new TicketAtencionDTO(
                ticket.getId(), ticket.getClienteId(), ticket.getAsunto(),
                ticket.getDescripcion(), ticket.getEstado(), ticket.getCategoria(),
                ticket.getFechaCreacion(), ticket.getFechaResolucion()
        );
    }
}
