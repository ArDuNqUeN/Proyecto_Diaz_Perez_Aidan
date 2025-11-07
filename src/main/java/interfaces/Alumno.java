package interfaces;

public class Alumno extends Usuario{

	private int idAlumno;
	private String expedeinte;
	private String curso;
	
	
	public Alumno(int idAlumno, String expedeinte, String curso) {
		super();
		this.idAlumno = idAlumno;
		this.expedeinte = expedeinte;
		this.curso = curso;
	}


	public Alumno() {
		super();
	}


	public int getIdAlumno() {
		return idAlumno;
	}


	public void setIdAlumno(int idAlumno) {
		this.idAlumno = idAlumno;
	}


	public String getExpedeinte() {
		return expedeinte;
	}


	public void setExpedeinte(String expedeinte) {
		this.expedeinte = expedeinte;
	}


	public String getCurso() {
		return curso;
	}


	public void setCurso(String curso) {
		this.curso = curso;
	}


	@Override
	public String toString() {
		return "Alumno [idAlumno=" + idAlumno + ", expedeinte=" + expedeinte + ", curso=" + curso + "]";
	}
	
	

}
