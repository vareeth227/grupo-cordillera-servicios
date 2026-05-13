package com.grupocordillera.gateway.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

/**
 * Configuración de rutas del API Gateway.
 * Define hacia dónde se redirige cada request según su path.
 * Configura Circuit Breaker por cada microservicio.
 * Configura CORS para permitir requests desde el frontend React.
 */
@Configuration
public class GatewayConfig {

    // Orígenes permitidos para CORS (separados por coma si hay varios)
    // En producción distribuida: http://<IP_FRONTEND>:5173
    @Value("${CORS_ALLOWED_ORIGINS:http://localhost:5173}")
    private String corsAllowedOrigins;

    // URLs de los microservicios inyectadas desde variables de entorno.
    // @Value resuelve ${...} en tiempo de arranque; el valor después de ':' es el default local.
    @Value("${MS_VENTAS_URL:http://localhost:9091}")
    private String msVentasUrl;

    @Value("${MS_ECOMMERCE_URL:http://localhost:9092}")
    private String msEcommerceUrl;

    @Value("${MS_INVENTARIO_URL:http://localhost:9093}")
    private String msInventarioUrl;

    @Value("${MS_FINANCIERO_URL:http://localhost:9094}")
    private String msFinancieroUrl;

    @Value("${MS_CLIENTES_URL:http://localhost:9095}")
    private String msClientesUrl;

    /**
     * Define las rutas del gateway: cada prefijo /api/{servicio}/** se enruta
     * al microservicio correspondiente con su propio Circuit Breaker.
     */
    @Bean
    public RouteLocator routeLocator(RouteLocatorBuilder builder) {
        return builder.routes()

                // Ruta hacia el microservicio de Ventas
                .route("ms-ventas", r -> r
                        .path("/api/ventas/**", "/api/ventas")
                        .filters(f -> f
                                .circuitBreaker(c -> c
                                        .setName("cb-ventas")
                                        .setFallbackUri("forward:/fallback/ventas"))
                                .rewritePath("/api/ventas(?<segment>/?.*)", "/ventas${segment}")
                        )
                        .uri(msVentasUrl))

                // Ruta hacia el microservicio de Ecommerce
                .route("ms-ecommerce", r -> r
                        .path("/api/ecommerce/**", "/api/ecommerce")
                        .filters(f -> f
                                .circuitBreaker(c -> c
                                        .setName("cb-ecommerce")
                                        .setFallbackUri("forward:/fallback/ecommerce"))
                                .rewritePath("/api/ecommerce(?<segment>/?.*)", "/ecommerce${segment}")
                        )
                        .uri(msEcommerceUrl))

                // Ruta hacia el microservicio de Inventario
                .route("ms-inventario", r -> r
                        .path("/api/inventario/**", "/api/inventario")
                        .filters(f -> f
                                .circuitBreaker(c -> c
                                        .setName("cb-inventario")
                                        .setFallbackUri("forward:/fallback/inventario"))
                                .rewritePath("/api/inventario(?<segment>/?.*)", "/inventario${segment}")
                        )
                        .uri(msInventarioUrl))

                // Ruta hacia el microservicio Financiero
                .route("ms-financiero", r -> r
                        .path("/api/financiero/**", "/api/financiero")
                        .filters(f -> f
                                .circuitBreaker(c -> c
                                        .setName("cb-financiero")
                                        .setFallbackUri("forward:/fallback/financiero"))
                                .rewritePath("/api/financiero(?<segment>/?.*)", "/financiero${segment}")
                        )
                        .uri(msFinancieroUrl))

                // Ruta hacia el microservicio de Clientes
                .route("ms-clientes", r -> r
                        .path("/api/clientes/**", "/api/clientes")
                        .filters(f -> f
                                .circuitBreaker(c -> c
                                        .setName("cb-clientes")
                                        .setFallbackUri("forward:/fallback/clientes"))
                                .rewritePath("/api/clientes(?<segment>/?.*)", "/clientes${segment}")
                        )
                        .uri(msClientesUrl))

                .build();
    }

    /**
     * Configura CORS para permitir requests desde el frontend React (puerto 3000).
     * En producción, restringir origins al dominio real del frontend.
     */
    @Bean
    public CorsWebFilter corsWebFilter() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOriginPatterns(Arrays.asList(corsAllowedOrigins.split(",")));
        config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(Arrays.asList("*"));
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return new CorsWebFilter(source);
    }
}
