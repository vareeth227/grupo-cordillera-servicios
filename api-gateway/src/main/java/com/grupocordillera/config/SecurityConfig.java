package com.grupocordillera.config;

import com.grupocordillera.security.JwtAuthenticationFilter;
import com.grupocordillera.security.JwtTokenProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.config.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.AuthenticationWebFilter;
import org.springframework.security.web.server.util.matcher.PathPatternParserServerWebExchangeMatcher;

/**
 * Configuración de seguridad para Spring Cloud Gateway
 * Utiliza WebFlux (reactivo) en lugar de Servlet tradicional
 */
@Configuration
@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
public class SecurityConfig {

    private final JwtTokenProvider jwtTokenProvider;

    public SecurityConfig(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    /**
     * Configuración de la cadena de filtros de seguridad
     * Define qué endpoints son públicos y cuáles requieren autenticación
     */
    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
        http
            // Deshabilitar CSRF en APIs (stateless)
            .csrf().disable()
            
            // Configurar autorización de endpoints
            .authorizeExchange()
                // Endpoints públicos del gateway
                .pathMatchers(
                    "/actuator/**",
                    "/swagger-ui/**",
                    "/v3/api-docs/**",
                    "/swagger-resources/**",
                    "/webjars/**",
                    "/fallback/**"
                ).permitAll()
                
                // Endpoints de API Gateway que requieren autenticación
                .pathMatchers("/api/**").authenticated()
                
                // Cualquier otra ruta es permitida
                .anyExchange().permitAll()
            
            .and()
            // Deshabilitar autenticación básica HTTP (usamos JWT)
            .httpBasic().disable()
            
            // Deshabilitar formularios de login (API no tiene formularios)
            .formLogin().disable()
            
            // Deshabilitar redirección a login (API devuelve 401)
            .logout().disable();
        
        return http.build();
    }

    /**
     * Registrar el filtro JWT en la cadena de filtros
     */
    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter(jwtTokenProvider);
    }
}
