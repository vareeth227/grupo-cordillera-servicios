package com.grupocordillera.ecommerce.controller;

import com.grupocordillera.ecommerce.dto.PedidoDTO;
import com.grupocordillera.ecommerce.service.PedidoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST del microservicio de Ecommerce.
 * Expone endpoints para gestionar pedidos online.
 */
@RestController
@RequestMapping("/ecommerce")
@RequiredArgsConstructor
public class PedidoController {

    private final PedidoService pedidoService;

    /** Lista todos los pedidos */
    @GetMapping("/pedidos")
    public ResponseEntity<List<PedidoDTO>> listarPedidos() {
        return ResponseEntity.ok(pedidoService.listarPedidos());
    }

    /** Lista pedidos de un cliente específico */
    @GetMapping("/pedidos/cliente/{clienteId}")
    public ResponseEntity<List<PedidoDTO>> pedidosPorCliente(@PathVariable Long clienteId) {
        return ResponseEntity.ok(pedidoService.listarPedidosPorCliente(clienteId));
    }

    /** Lista pedidos filtrados por estado */
    @GetMapping("/pedidos/estado/{estado}")
    public ResponseEntity<List<PedidoDTO>> pedidosPorEstado(@PathVariable String estado) {
        return ResponseEntity.ok(pedidoService.listarPedidosPorEstado(estado));
    }

    /** Obtiene el detalle de un pedido específico */
    @GetMapping("/pedidos/{id}")
    public ResponseEntity<PedidoDTO> obtenerPedido(@PathVariable Long id) {
        return ResponseEntity.ok(pedidoService.obtenerPedido(id));
    }

    /** Crea un nuevo pedido con sus ítems */
    @PostMapping("/pedidos")
    public ResponseEntity<PedidoDTO> crearPedido(@RequestBody PedidoDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(pedidoService.crearPedido(dto));
    }

    /** Actualiza el estado de un pedido */
    @PutMapping("/pedidos/{id}/estado")
    public ResponseEntity<PedidoDTO> actualizarEstado(@PathVariable Long id, @RequestParam String estado) {
        return ResponseEntity.ok(pedidoService.actualizarEstado(id, estado));
    }

    @DeleteMapping("/pedidos/{id}")
    public ResponseEntity<Void> eliminarPedido(@PathVariable Long id) {
        pedidoService.eliminarPedido(id);
        return ResponseEntity.noContent().build();
    }
}
