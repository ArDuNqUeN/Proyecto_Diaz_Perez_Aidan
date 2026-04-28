package main.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "documentos")
public class Documento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombreOriginal;
    private String nombreGuardado;
    private String rutaArchivo;
    private String tipoDocumento;
    private LocalDateTime fechaSubida;
    private Long tamaño;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "alumno_id")
    private Alumno alumno;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tutor_empresa_id")
    private TutorEmpresa tutorEmpresa;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tutor_centro_id")
    private TutorCentro tutorCentro;

    public Documento() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNombreOriginal() { return nombreOriginal; }
    public void setNombreOriginal(String nombreOriginal) { this.nombreOriginal = nombreOriginal; }

    public String getNombreGuardado() { return nombreGuardado; }
    public void setNombreGuardado(String nombreGuardado) { this.nombreGuardado = nombreGuardado; }

    public String getRutaArchivo() { return rutaArchivo; }
    public void setRutaArchivo(String rutaArchivo) { this.rutaArchivo = rutaArchivo; }

    public String getTipoDocumento() { return tipoDocumento; }
    public void setTipoDocumento(String tipoDocumento) { this.tipoDocumento = tipoDocumento; }

    public LocalDateTime getFechaSubida() { return fechaSubida; }
    public void setFechaSubida(LocalDateTime fechaSubida) { this.fechaSubida = fechaSubida; }

    public Long getTamaño() { return tamaño; }
    public void setTamaño(Long tamaño) { this.tamaño = tamaño; }

    public Alumno getAlumno() { return alumno; }
    public void setAlumno(Alumno alumno) { this.alumno = alumno; }

    public TutorEmpresa getTutorEmpresa() { return tutorEmpresa; }
    public void setTutorEmpresa(TutorEmpresa tutorEmpresa) { this.tutorEmpresa = tutorEmpresa; }

    public TutorCentro getTutorCentro() { return tutorCentro; }
    public void setTutorCentro(TutorCentro tutorCentro) { this.tutorCentro = tutorCentro; }

    public String getTamañoLegible() {
        if (tamaño == null) return "0 B";
        if (tamaño < 1024) return tamaño + " B";
        if (tamaño < 1024 * 1024) return String.format("%.1f KB", tamaño / 1024.0);
        return String.format("%.1f MB", tamaño / (1024.0 * 1024.0));
    }
}