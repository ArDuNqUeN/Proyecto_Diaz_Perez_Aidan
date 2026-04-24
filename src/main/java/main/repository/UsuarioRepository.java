package main.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import main.model.Usuario;

public interface UsuarioRepository extends JpaRepository<main.model.Usuario, Long> {
	 Optional<Usuario> findByEmail(String email);	
}