package main.service;

import main.model.Administrador;
import main.model.Usuario;
import main.repository.AdministradorRepository;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class AdministradorService {

    private final AdministradorRepository administradorRepository;

    public AdministradorService(AdministradorRepository administradorRepository) {
        this.administradorRepository = administradorRepository;
    }

    /**
     * Busca el perfil de administrador asociado a un usuario.
     * Útil después del login para saber si es admin.
     */
    public Optional<Administrador> findByUsuario(Usuario usuario) {
        return administradorRepository.findByUsuario(usuario);
    }

    /**
     * Busca un administrador por su ID.
     */
    public Optional<Administrador> findById(Long id) {
        return administradorRepository.findById(id);
    }

    /**
     * Guarda un administrador.
     */
    public Administrador guardar(Administrador administrador) {
        return administradorRepository.save(administrador);
    }
}