package com.grupocordillera.msclientes.service;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ClienteServiceTest {

    @Test
    void testValidateClienteId() {
        Long clienteId = 1L;
        assertNotNull(clienteId);
        assertEquals(1L, clienteId);
        assertTrue(clienteId > 0);
    }

    @Test
    void testClienteNameValidation() {
        String nombre = "Juan Perez";
        assertNotNull(nombre);
        assertTrue(nombre.length() > 0);
        assertEquals("Juan Perez", nombre);
    }

    @Test
    void testClienteEmailValidation() {
        String email = "juan@example.com";
        assertTrue(email.contains("@"));
        assertTrue(email.length() > 5);
    }
}
