package com.grupocordillera.clientes.config;

import com.grupocordillera.clientes.entity.Usuario;
import com.grupocordillera.clientes.repository.UsuarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DataInitializer implements CommandLineRunner {

    private final UsuarioRepository usuarioRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public DataInitializer(UsuarioRepository usuarioRepository, BCryptPasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        if (usuarioRepository.count() == 0) {
            usuarioRepository.saveAll(List.of(
                new Usuario(null, "admin@cordillera.cl",
                    passwordEncoder.encode("admin123"), "Administrador Sistema", "ADMIN", true),
                new Usuario(null, "operador@cordillera.cl",
                    passwordEncoder.encode("op123"), "Operador Tienda", "USUARIO", true)
            ));
        }
    }
}
