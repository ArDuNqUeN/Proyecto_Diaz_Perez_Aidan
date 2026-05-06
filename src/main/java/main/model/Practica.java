package main.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "practicas")
public class Practica {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String titulo;

    @Column(length = 1000)
    private String descripcion;

    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private int horasPrevistas;

    @Enumerated(EnumType.STRING)
    private EstadoPractica estado;

    @Transient
    private int horasValidadas;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "alumno_id")
    private Alumno alumno;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "empresa_id")
    private Empresa empresa;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tutor_empresa_id")
    private TutorEmpresa tutorEmpresa;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tutor_centro_id")
    private TutorCentro tutorCentro;

    @OneToMany(mappedBy = "practica", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Evaluacion> evaluaciones = new ArrayList<>();

    @OneToMany(mappedBy = "practica", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Informe> informes = new ArrayList<>();

    @OneToMany(mappedBy = "practica", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RegistroHoras> registrosHoras = new ArrayList<>();

    public Practica() { this.estado = EstadoPractica.ACTIVA; }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public LocalDate getFechaInicio() { return fechaInicio; }
    public void setFechaInicio(LocalDate fechaInicio) { this.fechaInicio = fechaInicio; }

    public LocalDate getFechaFin() { return fechaFin; }
    public void setFechaFin(LocalDate fechaFin) { this.fechaFin = fechaFin; }

    public int getHorasPrevistas() { return horasPrevistas; }
    public void setHorasPrevistas(int horasPrevistas) { this.horasPrevistas = horasPrevistas; }

    public EstadoPractica getEstado() { return estado; }
    public void setEstado(EstadoPractica estado) { this.estado = estado; }

    public int getHorasValidadas() { return horasValidadas; }
    public void setHorasValidadas(int horasValidadas) { this.horasValidadas = horasValidadas; }

    public Alumno getAlumno() { return alumno; }
    public void setAlumno(Alumno alumno) { this.alumno = alumno; }

    public Empresa getEmpresa() { return empresa; }
    public void setEmpresa(Empresa empresa) { this.empresa = empresa; }

    public TutorEmpresa getTutorEmpresa() { return tutorEmpresa; }
    public void setTutorEmpresa(TutorEmpresa tutorEmpresa) { this.tutorEmpresa = tutorEmpresa; }

    public TutorCentro getTutorCentro() { return tutorCentro; }
    public void setTutorCentro(TutorCentro tutorCentro) { this.tutorCentro = tutorCentro; }

    public List<Evaluacion> getEvaluaciones() { return evaluaciones; }
    public void setEvaluaciones(List<Evaluacion> evaluaciones) { this.evaluaciones = evaluaciones; }

    public List<Informe> getInformes() { return informes; }
    public void setInformes(List<Informe> informes) { this.informes = informes; }

    public List<RegistroHoras> getRegistrosHoras() { return registrosHoras; }
    public void setRegistrosHoras(List<RegistroHoras> registrosHoras) { this.registrosHoras = registrosHoras; }
}