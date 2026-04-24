package main.repository;

import main.model.Administrador;
import main.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface AdministradorRepository extends JpaRepository<Administrador, Long> {
    Optional<Administrador> findByUsuario(Usuario usuario);
}