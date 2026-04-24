package main.repository;

import main.model.TutorCentro;
import main.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface TutorCentroRepository extends JpaRepository<TutorCentro, Long> {
    Optional<TutorCentro> findByUsuario(Usuario usuario);
}