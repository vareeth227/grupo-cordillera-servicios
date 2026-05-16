package com.grupocordillera.msclientes;

import com.grupocordillera.clientes.ClientesApplication;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(classes = ClientesApplication.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
class MsClientesApplicationTests {

    @Test
    void contextLoads() {
        // Test successfully loads the application context
        assertTrue(true);
    }
}
