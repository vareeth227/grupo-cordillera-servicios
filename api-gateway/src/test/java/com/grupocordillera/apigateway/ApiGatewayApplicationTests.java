package com.grupocordillera.apigateway;

import com.grupocordillera.gateway.GatewayApplication;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(classes = GatewayApplication.class)
class ApiGatewayApplicationTests {

    @Test
    void contextLoads() {
        assertTrue(true);
    }
}
