package interfaces;

public class Tutor extends Usuario{
 
	private int idTutor;
	private String tipo;
	private String curso;
	public Tutor(int idTutor, String tipo, String curso) {
		super();
		this.idTutor = idTutor;
		this.tipo = tipo;
		this.curso = curso;
	}
	public Tutor() {
		super();
	}
	public int getIdTutor() {
		return idTutor;
	}
	public void setIdTutor(int idTutor) {
		this.idTutor = idTutor;
	}
	public String getTipo() {
		return tipo;
	}
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	public String getCurso() {
		return curso;
	}
	public void setCurso(String curso) {
		this.curso = curso;
	}
	@Override
	public String toString() {
		return "Tutor [idTutor=" + idTutor + ", tipo=" + tipo + ", curso=" + curso + "]";
	}
	
	
}
