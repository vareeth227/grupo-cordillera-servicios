package com.grupocordillera.msfinanciero;

import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;

import com.grupocordillera.financiero.FinancieroApplication;

@SpringBootTest(classes = FinancieroApplication.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
class MsFinancieroApplicationTests {

    @Test
    void contextLoads() {
        assertTrue(true);
    }
}
