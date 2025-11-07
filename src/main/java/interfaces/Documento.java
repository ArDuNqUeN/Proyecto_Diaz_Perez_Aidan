package interfaces;

import java.sql.Date;

public class Documento {

	private int idDocumento;
	private String tipo;
	private String ruta;
	private Date fechaSubida;
	public Documento(int idDocumento, String tipo, String ruta, Date fechaSubida) {
		super();
		this.idDocumento = idDocumento;
		this.tipo = tipo;
		this.ruta = ruta;
		this.fechaSubida = fechaSubida;
	}
	public Documento() {
		super();
	}
	public int getIdDocumento() {
		return idDocumento;
	}
	public void setIdDocumento(int idDocumento) {
		this.idDocumento = idDocumento;
	}
	public String getTipo() {
		return tipo;
	}
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	public String getRuta() {
		return ruta;
	}
	public void setRuta(String ruta) {
		this.ruta = ruta;
	}
	public Date getFechaSubida() {
		return fechaSubida;
	}
	public void setFechaSubida(Date fechaSubida) {
		this.fechaSubida = fechaSubida;
	}
	@Override
	public String toString() {
		return "Documento [idDocumento=" + idDocumento + ", tipo=" + tipo + ", ruta=" + ruta + ", fechaSubida="
				+ fechaSubida + "]";
	}
	
	
}
