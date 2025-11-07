package interfaces;

public class Evaluacion {

	private int idEvaluacion;
	private float nota;
	private String comentarios;
	public Evaluacion(int idEvaluacion, float nota, String comentarios) {
		super();
		this.idEvaluacion = idEvaluacion;
		this.nota = nota;
		this.comentarios = comentarios;
	}
	public Evaluacion() {
		super();
	}
	public int getIdEvaluacion() {
		return idEvaluacion;
	}
	public void setIdEvaluacion(int idEvaluacion) {
		this.idEvaluacion = idEvaluacion;
	}
	public float getNota() {
		return nota;
	}
	public void setNota(float nota) {
		this.nota = nota;
	}
	public String getComentarios() {
		return comentarios;
	}
	public void setComentarios(String comentarios) {
		this.comentarios = comentarios;
	}
	@Override
	public String toString() {
		return "Evaluacion [idEvaluacion=" + idEvaluacion + ", nota=" + nota + ", comentarios=" + comentarios + "]";
	}
	
	
}
