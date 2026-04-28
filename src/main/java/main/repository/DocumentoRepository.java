package main.repository;

import main.model.Documento;
import main.model.Alumno;
import main.model.TutorEmpresa;
import main.model.TutorCentro;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface DocumentoRepository extends JpaRepository<Documento, Long> {
    List<Documento> findByAlumno(Alumno alumno);
    List<Documento> findByTutorEmpresa(TutorEmpresa tutorEmpresa);
    List<Documento> findByTutorCentro(TutorCentro tutorCentro);
}