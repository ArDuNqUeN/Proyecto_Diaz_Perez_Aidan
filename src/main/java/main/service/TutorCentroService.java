package main.service;

import main.model.TutorCentro;
import main.model.Usuario;
import main.repository.TutorCentroRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class TutorCentroService {

    private final TutorCentroRepository tutorCentroRepository;

    public TutorCentroService(TutorCentroRepository tutorCentroRepository) {
        this.tutorCentroRepository = tutorCentroRepository;
    }

    /**
     * Busca el perfil de tutor de centro asociado a un usuario.
     */
    public Optional<TutorCentro> findByUsuario(Usuario usuario) {
        return tutorCentroRepository.findByUsuario(usuario);
    }

    /**
     * Lista todos los tutores de centro.
     */
    public List<TutorCentro> listarTodos() {
        return tutorCentroRepository.findAll();
    }

    /**
     * Busca un tutor de centro por su ID.
     */
    public Optional<TutorCentro> findById(Long id) {
        return tutorCentroRepository.findById(id);
    }

    /**
     * Guarda un tutor de centro.
     */
    public TutorCentro guardar(TutorCentro tutorCentro) {
        return tutorCentroRepository.save(tutorCentro);
    }
}