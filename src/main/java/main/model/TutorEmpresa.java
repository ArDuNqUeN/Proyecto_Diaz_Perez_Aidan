package main.model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tutores_empresa")
public class TutorEmpresa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "usuario_id", referencedColumnName = "id", unique = true)
    private Usuario usuario;

    private String cargo;
    private String telefono;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "empresa_id")
    private Empresa empresa;

    @OneToMany(mappedBy = "tutorEmpresa", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Evaluacion> evaluacionesRealizadas = new ArrayList<>();

    @OneToMany(mappedBy = "tutorEmpresa", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Informe> informesSubidos = new ArrayList<>();

    public TutorEmpresa() {}

    public TutorEmpresa(Usuario usuario, String cargo, String telefono, Empresa empresa) {
        this.usuario = usuario;
        this.cargo = cargo;
        this.telefono = telefono;
        this.empresa = empresa;
    }

    // Métodos helper
    public void addEvaluacion(Evaluacion evaluacion) {
        evaluacionesRealizadas.add(evaluacion);
        evaluacion.setTutorEmpresa(this);
    }

    public void addInforme(Informe informe) {
        informesSubidos.add(informe);
        informe.setTutorEmpresa(this);
    }

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }

    public String getCargo() { return cargo; }
    public void setCargo(String cargo) { this.cargo = cargo; }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }

    public Empresa getEmpresa() { return empresa; }
    public void setEmpresa(Empresa empresa) { this.empresa = empresa; }

    public List<Evaluacion> getEvaluacionesRealizadas() { return evaluacionesRealizadas; }
    public void setEvaluacionesRealizadas(List<Evaluacion> evaluacionesRealizadas) { this.evaluacionesRealizadas = evaluacionesRealizadas; }

    public List<Informe> getInformesSubidos() { return informesSubidos; }
    public void setInformesSubidos(List<Informe> informesSubidos) { this.informesSubidos = informesSubidos; }

    public String getNombreCompleto() {
        return usuario != null ? usuario.getNombre() : "Sin nombre";
    }
}