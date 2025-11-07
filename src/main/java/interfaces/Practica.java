package interfaces;

import java.sql.Date;

public class Practica {

	private int idPractica;
	private Date fechaInicio;
	private Date fechaFin;
	private String estado;
	public Practica(int idPractica, Date fechaInicio, Date fechaFin, String estado) {
		super();
		this.idPractica = idPractica;
		this.fechaInicio = fechaInicio;
		this.fechaFin = fechaFin;
		this.estado = estado;
	}
	public Practica() {
		super();
	}
	public int getIdPractica() {
		return idPractica;
	}
	public void setIdPractica(int idPractica) {
		this.idPractica = idPractica;
	}
	public Date getFechaInicio() {
		return fechaInicio;
	}
	public void setFechaInicio(Date fechaInicio) {
		this.fechaInicio = fechaInicio;
	}
	public Date getFechaFin() {
		return fechaFin;
	}
	public void setFechaFin(Date fechaFin) {
		this.fechaFin = fechaFin;
	}
	public String getEstado() {
		return estado;
	}
	public void setEstado(String estado) {
		this.estado = estado;
	}
	@Override
	public String toString() {
		return "Practica [idPractica=" + idPractica + ", fechaInicio=" + fechaInicio + ", fechaFin=" + fechaFin
				+ ", estado=" + estado + "]";
	}
	
}
