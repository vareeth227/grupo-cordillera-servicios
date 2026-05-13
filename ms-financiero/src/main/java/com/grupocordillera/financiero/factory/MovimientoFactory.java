package com.grupocordillera.financiero.factory;

import com.grupocordillera.financiero.dto.EgresoDTO;
import com.grupocordillera.financiero.dto.IngresoDTO;
import com.grupocordillera.financiero.entity.Egreso;
import com.grupocordillera.financiero.entity.Ingreso;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

/**
 * Factory para crear movimientos financieros (ingresos y egresos).
 * Centraliza la construcción de entidades financieras desde DTOs.
 */
@Component
public class MovimientoFactory {

    /**
     * Crea un Ingreso a partir de su DTO.
     * Asigna la fecha actual si no se especifica.
     */
    public Ingreso crearIngreso(IngresoDTO dto) {
        Ingreso ingreso = new Ingreso();
        ingreso.setConcepto(dto.getConcepto());
        ingreso.setMonto(dto.getMonto());
        ingreso.setFecha(dto.getFecha() != null ? dto.getFecha() : LocalDate.now());
        ingreso.setCategoria(dto.getCategoria());
        return ingreso;
    }

    /**
     * Crea un Egreso a partir de su DTO.
     * Asigna la fecha actual si no se especifica.
     */
    public Egreso crearEgreso(EgresoDTO dto) {
        Egreso egreso = new Egreso();
        egreso.setConcepto(dto.getConcepto());
        egreso.setMonto(dto.getMonto());
        egreso.setFecha(dto.getFecha() != null ? dto.getFecha() : LocalDate.now());
        egreso.setCategoria(dto.getCategoria());
        return egreso;
    }

    /** Convierte un Ingreso a su DTO */
    public IngresoDTO toDTO(Ingreso ingreso) {
        return new IngresoDTO(ingreso.getId(), ingreso.getConcepto(),
                ingreso.getMonto(), ingreso.getFecha(), ingreso.getCategoria());
    }

    /** Convierte un Egreso a su DTO */
    public EgresoDTO toDTO(Egreso egreso) {
        return new EgresoDTO(egreso.getId(), egreso.getConcepto(),
                egreso.getMonto(), egreso.getFecha(), egreso.getCategoria());
    }
}
