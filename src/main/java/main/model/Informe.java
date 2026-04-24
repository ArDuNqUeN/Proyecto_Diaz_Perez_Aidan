package main.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "informes")
public class Informe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String titulo;

    @Column(length = 2000)
    private String descripcion;

    @Column(nullable = false)
    private LocalDateTime fechaSubida;

    @Column(nullable = false)
    private String rutaArchivo;

    @Enumerated(EnumType.STRING)
    private TipoInforme tipoInforme;

    @Enumerated(EnumType.STRING)
    private EstadoInforme estado;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tutor_empresa_id")
    private TutorEmpresa tutorEmpresa;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tutor_centro_id")
    private TutorCentro tutorCentro;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "alumno_id")
    private Alumno alumno;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "practica_id")
    private Practica practica;

    public Informe() {
        this.fechaSubida = LocalDateTime.now();
        this.estado = EstadoInforme.PUBLICADO;
    }

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public LocalDateTime getFechaSubida() { return fechaSubida; }
    public void setFechaSubida(LocalDateTime fechaSubida) { this.fechaSubida = fechaSubida; }

    public String getRutaArchivo() { return rutaArchivo; }
    public void setRutaArchivo(String rutaArchivo) { this.rutaArchivo = rutaArchivo; }

    public TipoInforme getTipoInforme() { return tipoInforme; }
    public void setTipoInforme(TipoInforme tipoInforme) { this.tipoInforme = tipoInforme; }

    public EstadoInforme getEstado() { return estado; }
    public void setEstado(EstadoInforme estado) { this.estado = estado; }

    public TutorEmpresa getTutorEmpresa() { return tutorEmpresa; }
    public void setTutorEmpresa(TutorEmpresa tutorEmpresa) { this.tutorEmpresa = tutorEmpresa; }

    public TutorCentro getTutorCentro() { return tutorCentro; }
    public void setTutorCentro(TutorCentro tutorCentro) { this.tutorCentro = tutorCentro; }

    public Alumno getAlumno() { return alumno; }
    public void setAlumno(Alumno alumno) { this.alumno = alumno; }

    public Practica getPractica() { return practica; }
    public void setPractica(Practica practica) { this.practica = practica; }

    public String getNombreAutor() {
        if (tutorEmpresa != null) return tutorEmpresa.getNombreCompleto();
        if (tutorCentro != null) return tutorCentro.getNombreCompleto();
        return "Desconocido";
    }
}