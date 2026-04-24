package main.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import main.model.Usuario;
import main.repository.UsuarioRepository;

@Component
public class DataInitializer implements CommandLineRunner {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {

        // Solo crea los usuarios si no existen ya
        if (usuarioRepository.findByEmail("admin@test.com").isEmpty()) {
            Usuario admin = new Usuario();
            admin.setEmail("admin@test.com");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setRol("ROLE_ADMIN");
            // admin.setNombre("Administrador"); // si tienes el campo
            usuarioRepository.save(admin);
            System.out.println("✅ Admin creado");
        }

        if (usuarioRepository.findByEmail("alumno@test.com").isEmpty()) {
            Usuario alumno = new Usuario();
            alumno.setEmail("alumno@test.com");
            alumno.setPassword(passwordEncoder.encode("alumno123"));
            alumno.setRol("ROLE_ALUMNO");
            // alumno.setNombre("Alumno Prueba");
            usuarioRepository.save(alumno);
            System.out.println("✅ Alumno creado");
        }
    }
}