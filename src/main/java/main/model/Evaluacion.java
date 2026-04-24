package main.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "evaluaciones")
public class Evaluacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Double nota;

    @Column(length = 1000)
    private String comentario;

    @Column(nullable = false)
    private LocalDateTime fechaEvaluacion;

    @Enumerated(EnumType.STRING)
    private TipoEvaluador tipoEvaluador;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "alumno_id")
    private Alumno alumno;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tutor_empresa_id")
    private TutorEmpresa tutorEmpresa;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tutor_centro_id")
    private TutorCentro tutorCentro;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "practica_id")
    private Practica practica;

    public Evaluacion() {
        this.fechaEvaluacion = LocalDateTime.now();
    }

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Double getNota() { return nota; }
    public void setNota(Double nota) { this.nota = nota; }

    public String getComentario() { return comentario; }
    public void setComentario(String comentario) { this.comentario = comentario; }

    public LocalDateTime getFechaEvaluacion() { return fechaEvaluacion; }
    public void setFechaEvaluacion(LocalDateTime fechaEvaluacion) { this.fechaEvaluacion = fechaEvaluacion; }

    public TipoEvaluador getTipoEvaluador() { return tipoEvaluador; }
    public void setTipoEvaluador(TipoEvaluador tipoEvaluador) { this.tipoEvaluador = tipoEvaluador; }

    public Alumno getAlumno() { return alumno; }
    public void setAlumno(Alumno alumno) { this.alumno = alumno; }

    public TutorEmpresa getTutorEmpresa() { return tutorEmpresa; }
    public void setTutorEmpresa(TutorEmpresa tutorEmpresa) { this.tutorEmpresa = tutorEmpresa; }

    public TutorCentro getTutorCentro() { return tutorCentro; }
    public void setTutorCentro(TutorCentro tutorCentro) { this.tutorCentro = tutorCentro; }

    public Practica getPractica() { return practica; }
    public void setPractica(Practica practica) { this.practica = practica; }

    public String getNombreEvaluador() {
        if (tutorEmpresa != null) return tutorEmpresa.getNombreCompleto();
        if (tutorCentro != null) return tutorCentro.getNombreCompleto();
        return "Desconocido";
    }
}