package main.repository;

import main.model.Alumno;
import main.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface AlumnoRepository extends JpaRepository<Alumno, Long> {
    Optional<Alumno> findByUsuario(Usuario usuario);
    Optional<Alumno> findByMatricula(String matricula);
}