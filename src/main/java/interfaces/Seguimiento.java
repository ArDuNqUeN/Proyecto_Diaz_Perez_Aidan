package interfaces;

import java.sql.Date;

public class Seguimiento {

	private int idSeguimiento;
	private Date fecha;
	private float horas;
	private String descripcion;
	private boolean valido;
	public Seguimiento(int idSeguimiento, Date fecha, float horas, String descripcion, boolean valido) {
		super();
		this.idSeguimiento = idSeguimiento;
		this.fecha = fecha;
		this.horas = horas;
		this.descripcion = descripcion;
		this.valido = valido;
	}
	public Seguimiento() {
		super();
	}
	public int getIdSeguimiento() {
		return idSeguimiento;
	}
	public void setIdSeguimiento(int idSeguimiento) {
		this.idSeguimiento = idSeguimiento;
	}
	public Date getFecha() {
		return fecha;
	}
	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}
	public float getHoras() {
		return horas;
	}
	public void setHoras(float horas) {
		this.horas = horas;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	public boolean isValido() {
		return valido;
	}
	public void setValido(boolean valido) {
		this.valido = valido;
	}
	@Override
	public String toString() {
		return "Seguimiento [idSeguimiento=" + idSeguimiento + ", fecha=" + fecha + ", horas=" + horas
				+ ", descripcion=" + descripcion + ", valido=" + valido + "]";
	}
	
	
}
