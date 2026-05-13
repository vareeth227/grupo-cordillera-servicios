package com.grupocordillera.clientes.controller;

import com.grupocordillera.clientes.dto.ClienteDTO;
import com.grupocordillera.clientes.dto.TicketAtencionDTO;
import com.grupocordillera.clientes.service.ClienteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST del microservicio de Clientes.
 * Expone endpoints para gestionar clientes y tickets de atención al cliente.
 */
@RestController
@RequestMapping("/clientes")
@RequiredArgsConstructor
public class ClienteController {

    private final ClienteService clienteService;

    /** Lista todos los clientes */
    @GetMapping
    public ResponseEntity<List<ClienteDTO>> listarClientes() {
        return ResponseEntity.ok(clienteService.listarClientes());
    }

    /** Lista solo clientes activos */
    @GetMapping("/activos")
    public ResponseEntity<List<ClienteDTO>> listarActivos() {
        return ResponseEntity.ok(clienteService.listarClientesActivos());
    }

    /** Obtiene el detalle de un cliente por su ID */
    @GetMapping("/{id}")
    public ResponseEntity<ClienteDTO> obtenerCliente(@PathVariable Long id) {
        return ResponseEntity.ok(clienteService.obtenerCliente(id));
    }

    /** Registra un nuevo cliente en el CRM */
    @PostMapping
    public ResponseEntity<ClienteDTO> registrarCliente(@RequestBody ClienteDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(clienteService.registrarCliente(dto));
    }

    /** Lista todos los tickets de atención */
    @GetMapping("/tickets")
    public ResponseEntity<List<TicketAtencionDTO>> listarTickets() {
        return ResponseEntity.ok(clienteService.listarTickets());
    }

    /** Lista tickets de un cliente específico */
    @GetMapping("/{id}/tickets")
    public ResponseEntity<List<TicketAtencionDTO>> ticketsPorCliente(@PathVariable Long id) {
        return ResponseEntity.ok(clienteService.listarTicketsPorCliente(id));
    }

    /** Lista tickets filtrados por estado */
    @GetMapping("/tickets/estado/{estado}")
    public ResponseEntity<List<TicketAtencionDTO>> ticketsPorEstado(@PathVariable String estado) {
        return ResponseEntity.ok(clienteService.listarTicketsPorEstado(estado));
    }

    /** Crea un nuevo ticket de atención */
    @PostMapping("/tickets")
    public ResponseEntity<TicketAtencionDTO> crearTicket(@RequestBody TicketAtencionDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(clienteService.crearTicket(dto));
    }

    /** Actualiza el estado de un ticket */
    @PutMapping("/tickets/{id}/estado")
    public ResponseEntity<TicketAtencionDTO> actualizarEstado(@PathVariable Long id, @RequestParam String estado) {
        return ResponseEntity.ok(clienteService.actualizarEstadoTicket(id, estado));
    }

    /** Desactiva (bloquea) un cliente sin eliminarlo */
    @PatchMapping("/{id}/desactivar")
    public ResponseEntity<Void> desactivarCliente(@PathVariable Long id) {
        clienteService.desactivarCliente(id);
        return ResponseEntity.noContent().build();
    }

    /** Elimina un cliente por su ID */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarCliente(@PathVariable Long id) {
        clienteService.eliminarCliente(id);
        return ResponseEntity.noContent().build();
    }

    /** Elimina un ticket por su ID */
    @DeleteMapping("/tickets/{id}")
    public ResponseEntity<Void> eliminarTicket(@PathVariable Long id) {
        clienteService.eliminarTicket(id);
        return ResponseEntity.noContent().build();
    }
}
