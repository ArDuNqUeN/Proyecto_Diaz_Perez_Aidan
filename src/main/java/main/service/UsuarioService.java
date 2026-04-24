package main.service;

import main.model.Usuario;
import main.repository.UsuarioRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service  // 👈 Esto le dice a Spring: "esta clase es un servicio, adminístrala tú"
public class UsuarioService {

    // 👇 El repositorio que creaste antes. Spring lo inyecta automáticamente
    private final UsuarioRepository usuarioRepository;

    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    /**
     * Busca un usuario por su email.
     * Útil para el login y para verificar si un email ya existe.
     */
    public Optional<Usuario> findByEmail(String email) {
        return usuarioRepository.findByEmail(email);
    }

    /**
     * Verifica si ya existe un usuario con ese email.
     * Retorna true si existe, false si no.
     */
    public boolean existeEmail(String email) {
        return usuarioRepository.findByEmail(email).isPresent();
    }

    /**
     * Guarda un usuario en la base de datos.
     * Si el usuario ya tiene ID, lo actualiza; si no, lo crea nuevo.
     */
    public Usuario guardar(Usuario usuario) {
        return usuarioRepository.save(usuario);
    }

    /**
     * Busca un usuario por su ID.
     */
    public Optional<Usuario> findById(Long id) {
        return usuarioRepository.findById(id);
    }

    /**
     * Lista todos los usuarios del sistema.
     * Útil para el panel de administrador.
     */
    public List<Usuario> listarTodos() {
        return usuarioRepository.findAll();
    }

    /**
     * Elimina un usuario por su ID.
     */
    public void eliminar(Long id) {
        usuarioRepository.deleteById(id);
    }
}