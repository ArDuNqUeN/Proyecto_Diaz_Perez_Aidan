package main.model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "empresas")
public class Empresa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombre;

    private String direccion;
    private String sector;

    @Column(unique = true)
    private String cif;

    @OneToMany(mappedBy = "empresa", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TutorEmpresa> tutores = new ArrayList<>();

    @OneToMany(mappedBy = "empresa", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Practica> practicas = new ArrayList<>();

    public Empresa() {}

    public Empresa(String nombre, String direccion, String sector, String cif) {
        this.nombre = nombre;
        this.direccion = direccion;
        this.sector = sector;
        this.cif = cif;
    }

    // Métodos helper
    public void addTutor(TutorEmpresa tutor) {
        tutores.add(tutor);
        tutor.setEmpresa(this);
    }

    public void addPractica(Practica practica) {
        practicas.add(practica);
        practica.setEmpresa(this);
    }

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }

    public String getSector() { return sector; }
    public void setSector(String sector) { this.sector = sector; }

    public String getCif() { return cif; }
    public void setCif(String cif) { this.cif = cif; }

    public List<TutorEmpresa> getTutores() { return tutores; }
    public void setTutores(List<TutorEmpresa> tutores) { this.tutores = tutores; }

    public List<Practica> getPracticas() { return practicas; }
    public void setPracticas(List<Practica> practicas) { this.practicas = practicas; }
}