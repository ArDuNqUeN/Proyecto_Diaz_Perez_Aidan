package main.repository;

import main.model.Practica;
import main.model.Alumno;
import main.model.TutorEmpresa;
import main.model.TutorCentro;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PracticaRepository extends JpaRepository<Practica, Long> {
    List<Practica> findByAlumno(Alumno alumno);
    List<Practica> findByTutorEmpresa(TutorEmpresa tutorEmpresa);
    List<Practica> findByTutorCentro(TutorCentro tutorCentro);
}