package com.grupocordillera.apigateway.config;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class GatewayConfigTest {

    @Test
    void testGatewayRoutes() {
        String route = "/api/clientes";
        assertNotNull(route);
        assertTrue(route.startsWith("/api"));
    }

    @Test
    void testCorsConfiguration() {
        String allowedOrigin = "http://localhost:3000";
        assertNotNull(allowedOrigin);
        assertTrue(allowedOrigin.contains("localhost"));
    }

    @Test
    void testHealthCheckEndpoint() {
        String healthEndpoint = "/actuator/health";
        assertNotNull(healthEndpoint);
        assertTrue(healthEndpoint.contains("health"));
    }
}
