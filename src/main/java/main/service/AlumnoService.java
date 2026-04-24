package main.service;

import main.model.Alumno;
import main.model.Usuario;
import main.model.RegistroHoras;
import main.model.Practica;
import main.repository.AlumnoRepository;
import main.repository.UsuarioRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class AlumnoService {

    private final AlumnoRepository alumnoRepository;
    private final UsuarioRepository usuarioRepository;

    public AlumnoService(AlumnoRepository alumnoRepository, UsuarioRepository usuarioRepository) {
        this.alumnoRepository = alumnoRepository;
        this.usuarioRepository = usuarioRepository;
    }

    /**
     * Busca un alumno por su ID.
     */
    public Optional<Alumno> findById(Long id) {
        return alumnoRepository.findById(id);
    }

    /**
     * Busca un alumno por su matrícula (número único de estudiante).
     */
    public Optional<Alumno> findByMatricula(String matricula) {
        return alumnoRepository.findByMatricula(matricula);
    }

    /**
     * Busca el perfil de alumno asociado a un usuario.
     * Muy útil después del login: sabes el Usuario, necesitas el Alumno.
     */
    public Optional<Alumno> findByUsuario(Usuario usuario) {
        return alumnoRepository.findByUsuario(usuario);
    }

    /**
     * Lista todos los alumnos del sistema.
     * Para que el administrador o el tutor de centro vean a todos.
     */
    public List<Alumno> listarTodos() {
        return alumnoRepository.findAll();
    }

    /**
     * Guarda un alumno (nuevo o actualizado).
     */
    public Alumno guardar(Alumno alumno) {
        return alumnoRepository.save(alumno);
    }

    /**
     * Obtiene las prácticas asignadas a un alumno.
     */
    public List<Practica> getPracticas(Long alumnoId) {
        Alumno alumno = alumnoRepository.findById(alumnoId)
                .orElseThrow(() -> new RuntimeException("Alumno no encontrado con ID: " + alumnoId));
        return alumno.getPracticas();
    }

    /**
     * Obtiene los registros de horas de un alumno.
     */
    public List<RegistroHoras> getRegistrosHoras(Long alumnoId) {
        Alumno alumno = alumnoRepository.findById(alumnoId)
                .orElseThrow(() -> new RuntimeException("Alumno no encontrado con ID: " + alumnoId));
        return alumno.getRegistrosHoras();
    }

    /**
     * Crea un alumno nuevo a partir de un Usuario ya existente.
     */
    public Alumno crearDesdeUsuario(Usuario usuario, String matricula, String apellidos, String telefono) {
        Alumno alumno = new Alumno();
        alumno.setUsuario(usuario);
        alumno.setMatricula(matricula);
        alumno.setApellidos(apellidos);
        alumno.setTelefono(telefono);
        return alumnoRepository.save(alumno);
    }
}