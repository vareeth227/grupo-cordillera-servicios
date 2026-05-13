package com.grupocordillera.ventas.factory;

import com.grupocordillera.ventas.dto.TransaccionDTO;
import com.grupocordillera.ventas.entity.Transaccion;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * Factory para crear instancias de Transaccion.
 * Implementa el patrón Factory Method para centralizar
 * la creación de entidades y desacoplarlas de los DTOs.
 */
@Component
public class TransaccionFactory {

    /**
     * Crea una Transaccion de tipo VENTA a partir de un DTO.
     * Asigna automáticamente la fecha actual si no se especifica.
     */
    public Transaccion crearVenta(TransaccionDTO dto) {
        Transaccion transaccion = new Transaccion();
        transaccion.setFecha(dto.getFecha() != null ? dto.getFecha() : LocalDateTime.now());
        transaccion.setMonto(dto.getMonto());
        transaccion.setPuntoDeVentaId(dto.getPuntoDeVentaId());
        transaccion.setProductoCodigo(dto.getProductoCodigo());
        transaccion.setCantidad(dto.getCantidad());
        transaccion.setTipo("VENTA");
        return transaccion;
    }

    /**
     * Crea una Transaccion de tipo DEVOLUCION a partir de un DTO.
     */
    public Transaccion crearDevolucion(TransaccionDTO dto) {
        Transaccion transaccion = new Transaccion();
        transaccion.setFecha(dto.getFecha() != null ? dto.getFecha() : LocalDateTime.now());
        transaccion.setMonto(dto.getMonto());
        transaccion.setPuntoDeVentaId(dto.getPuntoDeVentaId());
        transaccion.setProductoCodigo(dto.getProductoCodigo());
        transaccion.setCantidad(dto.getCantidad());
        transaccion.setTipo("DEVOLUCION");
        return transaccion;
    }

    /**
     * Convierte una entidad Transaccion a su DTO correspondiente.
     */
    public TransaccionDTO toDTO(Transaccion transaccion) {
        return new TransaccionDTO(
                transaccion.getId(),
                transaccion.getFecha(),
                transaccion.getMonto(),
                transaccion.getPuntoDeVentaId(),
                transaccion.getProductoCodigo(),
                transaccion.getCantidad(),
                transaccion.getTipo()
        );
    }
}
