package main.repository;

import main.model.RegistroHoras;
import main.model.Alumno;
import main.model.EstadoRegistro;
import main.model.Practica;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface RegistroHorasRepository extends JpaRepository<RegistroHoras, Long> {
    List<RegistroHoras> findByAlumno(Alumno alumno);
    List<RegistroHoras> findByAlumnoAndEstado(Alumno alumno, EstadoRegistro estado);
    List<RegistroHoras> findByEstado(EstadoRegistro estado);
    List<RegistroHoras> findByPractica(Practica practica); // 👈 NUEVO
    List<RegistroHoras> findByPracticaAndEstado(Practica practica, EstadoRegistro estado); // 👈 NUEVO
}