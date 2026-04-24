package main.service;

import main.model.TutorEmpresa;
import main.model.Usuario;
import main.repository.TutorEmpresaRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class TutorEmpresaService {

    private final TutorEmpresaRepository tutorEmpresaRepository;

    public TutorEmpresaService(TutorEmpresaRepository tutorEmpresaRepository) {
        this.tutorEmpresaRepository = tutorEmpresaRepository;
    }

    /**
     * Busca el perfil de tutor de empresa asociado a un usuario.
     */
    public Optional<TutorEmpresa> findByUsuario(Usuario usuario) {
        return tutorEmpresaRepository.findByUsuario(usuario);
    }

    /**
     * Lista todos los tutores de empresa.
     */
    public List<TutorEmpresa> listarTodos() {
        return tutorEmpresaRepository.findAll();
    }

    /**
     * Busca un tutor de empresa por su ID.
     */
    public Optional<TutorEmpresa> findById(Long id) {
        return tutorEmpresaRepository.findById(id);
    }

    /**
     * Guarda un tutor de empresa.
     */
    public TutorEmpresa guardar(TutorEmpresa tutorEmpresa) {
        return tutorEmpresaRepository.save(tutorEmpresa);
    }
}