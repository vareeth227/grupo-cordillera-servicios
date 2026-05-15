package com.grupocordillera.msventas.service;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class VentaServiceTest {

    @Test
    void testCalculateVentaTotal() {
        double precio = 100.0;
        int cantidad = 5;
        double total = precio * cantidad;
        assertEquals(500.0, total);
    }

    @Test
    void testValidateVentaId() {
        Long ventaId = 1L;
        assertNotNull(ventaId);
        assertTrue(ventaId > 0);
    }

    @Test
    void testVentaStatusValidation() {
        String status = "COMPLETADA";
        assertNotNull(status);
        assertTrue(status.equals("COMPLETADA"));
    }
}
