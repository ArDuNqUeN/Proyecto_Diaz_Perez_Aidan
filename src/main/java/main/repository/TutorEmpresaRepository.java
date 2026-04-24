package main.repository;

import main.model.TutorEmpresa;
import main.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface TutorEmpresaRepository extends JpaRepository<TutorEmpresa, Long> {
    Optional<TutorEmpresa> findByUsuario(Usuario usuario);
}