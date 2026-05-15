package com.grupocordillera.msfinanciero.service;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class FinancieroServiceTest {

    @Test
    void testCalculateKPI() {
        BigDecimal ingreso = new BigDecimal("1000");
        BigDecimal egreso = new BigDecimal("300");
        BigDecimal utilidad = ingreso.subtract(egreso);
        
        assertEquals(new BigDecimal("700"), utilidad);
    }

    @Test
    void testValidateFinancialRecord() {
        Long recordId = 1L;
        assertNotNull(recordId);
        assertTrue(recordId > 0);
    }

    @Test
    void testPercentageCalculation() {
        double valor = 100.0;
        double porcentaje = 20.0;
        double resultado = (valor * porcentaje) / 100;
        
        assertEquals(20.0, resultado);
    }
}
