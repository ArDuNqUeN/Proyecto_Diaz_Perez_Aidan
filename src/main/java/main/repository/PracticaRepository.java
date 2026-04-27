package main.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import main.model.Alumno;
import main.model.Practica;
import main.model.TutorEmpresa;

public interface PracticaRepository extends JpaRepository<Practica, Long> {
    List<Practica> findByAlumno(Alumno alumno);
    List<Practica> findByTutorEmpresa(TutorEmpresa tutorEmpresa);
}