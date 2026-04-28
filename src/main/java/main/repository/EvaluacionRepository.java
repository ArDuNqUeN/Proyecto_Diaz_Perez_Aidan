package main.repository;

import main.model.Evaluacion;
import main.model.Alumno;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface EvaluacionRepository extends JpaRepository<Evaluacion, Long> {
    List<Evaluacion> findByAlumno(Alumno alumno);
}