package main.model;

import jakarta.persistence.*;

@Entity
@Table(name = "administradores")
public class Administrador {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "usuario_id", referencedColumnName = "id", unique = true)
    private Usuario usuario;

    private String departamento;

    public Administrador() {}

    public Administrador(Usuario usuario, String departamento) {
        this.usuario = usuario;
        this.departamento = departamento;
    }

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }

    public String getDepartamento() { return departamento; }
    public void setDepartamento(String departamento) { this.departamento = departamento; }

    public String getNombreCompleto() {
        return usuario != null ? usuario.getNombre() : "Sin nombre";
    }
}