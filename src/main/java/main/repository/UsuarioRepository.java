package main.repository;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRepository extends JpaRepository<main.model.Usuario, Long> {
}