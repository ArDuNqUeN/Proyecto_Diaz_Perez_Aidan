package main.model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tutores_centro")
public class TutorCentro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "usuario_id", referencedColumnName = "id", unique = true)
    private Usuario usuario;

    private String departamento;
    private String despacho;

    @OneToMany(mappedBy = "tutorCentro", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Evaluacion> evaluacionesRealizadas = new ArrayList<>();

    @OneToMany(mappedBy = "tutorCentro", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Informe> informesGenerados = new ArrayList<>();

    public TutorCentro() {}

    public TutorCentro(Usuario usuario, String departamento, String despacho) {
        this.usuario = usuario;
        this.departamento = departamento;
        this.despacho = despacho;
    }

    // Métodos helper
    public void addEvaluacion(Evaluacion evaluacion) {
        evaluacionesRealizadas.add(evaluacion);
        evaluacion.setTutorCentro(this);
    }

    public void addInforme(Informe informe) {
        informesGenerados.add(informe);
        informe.setTutorCentro(this);
    }

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }

    public String getDepartamento() { return departamento; }
    public void setDepartamento(String departamento) { this.departamento = departamento; }

    public String getDespacho() { return despacho; }
    public void setDespacho(String despacho) { this.despacho = despacho; }

    public List<Evaluacion> getEvaluacionesRealizadas() { return evaluacionesRealizadas; }
    public void setEvaluacionesRealizadas(List<Evaluacion> evaluacionesRealizadas) { this.evaluacionesRealizadas = evaluacionesRealizadas; }

    public List<Informe> getInformesGenerados() { return informesGenerados; }
    public void setInformesGenerados(List<Informe> informesGenerados) { this.informesGenerados = informesGenerados; }

    public String getNombreCompleto() {
        return usuario != null ? usuario.getNombre() : "Sin nombre";
    }
}