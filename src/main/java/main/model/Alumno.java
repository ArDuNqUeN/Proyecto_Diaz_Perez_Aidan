package main.model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "alumnos")
public class Alumno {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "usuario_id", referencedColumnName = "id", unique = true)
    private Usuario usuario;

    @Column(unique = true)
    private String matricula;

    private String apellidos;
    private String telefono;

    // Relaciones
    @OneToMany(mappedBy = "alumno", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Practica> practicas = new ArrayList<>();

    @OneToMany(mappedBy = "alumno", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RegistroHoras> registrosHoras = new ArrayList<>();

    @OneToMany(mappedBy = "alumno", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Evaluacion> evaluaciones = new ArrayList<>();

    @OneToMany(mappedBy = "alumno", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Informe> informes = new ArrayList<>();

    public Alumno() {}

    public Alumno(Usuario usuario, String matricula, String apellidos, String telefono) {
        this.usuario = usuario;
        this.matricula = matricula;
        this.apellidos = apellidos;
        this.telefono = telefono;
    }

    // Métodos helper
    public void addPractica(Practica practica) {
        practicas.add(practica);
        practica.setAlumno(this);
    }

    public void removePractica(Practica practica) {
        practicas.remove(practica);
        practica.setAlumno(null);
    }

    public void addRegistroHoras(RegistroHoras registro) {
        registrosHoras.add(registro);
        registro.setAlumno(this);
    }

    public void addEvaluacion(Evaluacion evaluacion) {
        evaluaciones.add(evaluacion);
        evaluacion.setAlumno(this);
    }

    public void addInforme(Informe informe) {
        informes.add(informe);
        informe.setAlumno(this);
    }

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }

    public String getMatricula() { return matricula; }
    public void setMatricula(String matricula) { this.matricula = matricula; }

    public String getApellidos() { return apellidos; }
    public void setApellidos(String apellidos) { this.apellidos = apellidos; }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }

    public List<Practica> getPracticas() { return practicas; }
    public void setPracticas(List<Practica> practicas) { this.practicas = practicas; }

    public List<RegistroHoras> getRegistrosHoras() { return registrosHoras; }
    public void setRegistrosHoras(List<RegistroHoras> registrosHoras) { this.registrosHoras = registrosHoras; }

    public List<Evaluacion> getEvaluaciones() { return evaluaciones; }
    public void setEvaluaciones(List<Evaluacion> evaluaciones) { this.evaluaciones = evaluaciones; }

    public List<Informe> getInformes() { return informes; }
    public void setInformes(List<Informe> informes) { this.informes = informes; }

    public String getNombreCompleto() {
        if (usuario != null && apellidos != null) {
            return usuario.getNombre() + " " + apellidos;
        } else if (usuario != null) {
            return usuario.getNombre();
        }
        return "Sin nombre";
    }
}