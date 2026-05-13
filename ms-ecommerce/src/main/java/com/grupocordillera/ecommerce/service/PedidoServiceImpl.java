package com.grupocordillera.ecommerce.service;

import com.grupocordillera.ecommerce.dto.PedidoDTO;
import com.grupocordillera.ecommerce.entity.ItemPedido;
import com.grupocordillera.ecommerce.entity.Pedido;
import com.grupocordillera.ecommerce.factory.PedidoFactory;
import com.grupocordillera.ecommerce.repository.ItemPedidoRepository;
import com.grupocordillera.ecommerce.repository.PedidoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementación del servicio de Ecommerce.
 * Gestiona el ciclo de vida de los pedidos online.
 * Se comunica con ms-clientes para validar el cliente y con ms-inventario para verificar el catálogo.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class PedidoServiceImpl implements PedidoService {

    private final PedidoRepository pedidoRepository;
    private final ItemPedidoRepository itemPedidoRepository;
    private final PedidoFactory pedidoFactory;
    private final RestTemplate restTemplate;

    @Value("${MS_CLIENTES_URL:http://localhost:9095}")
    private String msClientesUrl;

    @Value("${MS_INVENTARIO_URL:http://localhost:9093}")
    private String msInventarioUrl;

    @Override
    @Transactional(readOnly = true)
    public List<PedidoDTO> listarPedidos() {
        return pedidoRepository.findAll().stream()
                .map(p -> pedidoFactory.toDTO(p, itemPedidoRepository.findByPedidoId(p.getId())))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<PedidoDTO> listarPedidosPorCliente(Long clienteId) {
        return pedidoRepository.findByClienteId(clienteId).stream()
                .map(p -> pedidoFactory.toDTO(p, itemPedidoRepository.findByPedidoId(p.getId())))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<PedidoDTO> listarPedidosPorEstado(String estado) {
        return pedidoRepository.findByEstado(estado).stream()
                .map(p -> pedidoFactory.toDTO(p, itemPedidoRepository.findByPedidoId(p.getId())))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public PedidoDTO obtenerPedido(Long id) {
        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado con ID: " + id));
        List<ItemPedido> items = itemPedidoRepository.findByPedidoId(id);
        return pedidoFactory.toDTO(pedido, items);
    }

    /** Valida que el cliente exista en ms-clientes antes de registrar el pedido */
    private void validarCliente(Long clienteId) {
        try {
            restTemplate.getForEntity(msClientesUrl + "/clientes/" + clienteId, Object.class);
        } catch (HttpClientErrorException.NotFound e) {
            throw new IllegalArgumentException("Cliente con ID " + clienteId + " no existe en el CRM");
        } catch (Exception e) {
            log.warn("No se pudo contactar ms-clientes para validar cliente {}: {}", clienteId, e.getMessage());
        }
    }

    /** Verifica que ms-inventario esté disponible y registra el catálogo consultado */
    private void verificarCatalogo() {
        try {
            restTemplate.getForEntity(msInventarioUrl + "/inventario/productos/activos", Object.class);
            log.info("Catálogo de ms-inventario consultado correctamente al registrar pedido");
        } catch (Exception e) {
            log.warn("No se pudo contactar ms-inventario al crear pedido: {}", e.getMessage());
        }
    }

    /** Crea el pedido y persiste sus ítems en una sola transacción */
    @Override
    @Transactional
    public PedidoDTO crearPedido(PedidoDTO dto) {
        validarCliente(dto.getClienteId());
        verificarCatalogo();

        Pedido pedido = pedidoFactory.crearPedido(dto);
        Pedido guardado = pedidoRepository.save(pedido);

        // Persistir cada ítem con la referencia JPA al pedido guardado (genera FK real en BD)
        List<ItemPedido> items = dto.getItems().stream()
                .map(itemDTO -> {
                    ItemPedido item = pedidoFactory.crearItem(itemDTO, guardado.getId());
                    item.setPedido(guardado);
                    return itemPedidoRepository.save(item);
                })
                .collect(Collectors.toList());

        return pedidoFactory.toDTO(guardado, items);
    }

    @Override
    @Transactional
    public void eliminarPedido(Long id) {
        if (!pedidoRepository.existsById(id))
            throw new RuntimeException("Pedido no encontrado con ID: " + id);
        itemPedidoRepository.deleteAll(itemPedidoRepository.findByPedidoId(id));
        pedidoRepository.deleteById(id);
    }

    /** Cambia el estado del pedido (e.g. PENDIENTE -> CONFIRMADO -> EN_ENVIO) */
    @Override
    @Transactional
    public PedidoDTO actualizarEstado(Long id, String nuevoEstado) {
        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado con ID: " + id));
        pedido.setEstado(nuevoEstado);
        Pedido actualizado = pedidoRepository.save(pedido);
        return pedidoFactory.toDTO(actualizado, itemPedidoRepository.findByPedidoId(id));
    }
}
