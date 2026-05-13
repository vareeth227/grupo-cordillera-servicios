package com.grupocordillera.clientes.service;

import com.grupocordillera.clientes.dto.ClienteDTO;
import com.grupocordillera.clientes.dto.TicketAtencionDTO;

import java.util.List;

/**
 * Interfaz del servicio de Clientes (CRM).
 * Define operaciones para gestionar clientes y tickets de atención.
 */
public interface ClienteService {

    /** Lista todos los clientes registrados */
    List<ClienteDTO> listarClientes();

    /** Lista solo clientes activos */
    List<ClienteDTO> listarClientesActivos();

    /** Obtiene un cliente por su ID */
    ClienteDTO obtenerCliente(Long id);

    /** Registra un nuevo cliente en el CRM */
    ClienteDTO registrarCliente(ClienteDTO dto);

    /** Lista todos los tickets de atención */
    List<TicketAtencionDTO> listarTickets();

    /** Lista tickets de un cliente específico */
    List<TicketAtencionDTO> listarTicketsPorCliente(Long clienteId);

    /** Lista tickets por estado */
    List<TicketAtencionDTO> listarTicketsPorEstado(String estado);

    /** Crea un nuevo ticket de atención */
    TicketAtencionDTO crearTicket(TicketAtencionDTO dto);

    /** Actualiza el estado de un ticket */
    TicketAtencionDTO actualizarEstadoTicket(Long ticketId, String nuevoEstado);

    /** Desactiva un cliente (bloqueo lógico, no elimina el registro) */
    void desactivarCliente(Long id);

    /** Elimina un cliente por su ID */
    void eliminarCliente(Long id);

    /** Elimina un ticket por su ID */
    void eliminarTicket(Long id);
}
