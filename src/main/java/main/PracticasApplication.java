package main;

import main.model.Usuario;
import main.repository.UsuarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class PracticasApplication {

    public static void main(String[] args) {
        SpringApplication.run(PracticasApplication.class, args);
    }

    @Bean
    public CommandLineRunner init(UsuarioRepository repo) {
        return args -> {

            // 👇 ADMIN
            Usuario admin = new Usuario();
            admin.setNombre("Admin");
            admin.setEmail("admin@admin.com");
            admin.setPassword("{noop}1234");
            admin.setRol("ROLE_ADMIN");

            repo.save(admin);

            // 👇 ALUMNO
            Usuario alumno = new Usuario();
            alumno.setNombre("Alumno");
            alumno.setEmail("alumno@alumno.com");
            alumno.setPassword("{noop}1234");
            alumno.setRol("ROLE_ALUMNO");

            repo.save(alumno);
        };
    }
}