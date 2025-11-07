package interfaces;

public class Empresa {

	private int idEmpresa;
	private String nombre;
	private String cifp;
	private String direccion;
	private String contacto;
	private String email;
	public Empresa(int idEmpresa, String nombre, String cifp, String direccion, String contacto, String email) {
		super();
		this.idEmpresa = idEmpresa;
		this.nombre = nombre;
		this.cifp = cifp;
		this.direccion = direccion;
		this.contacto = contacto;
		this.email = email;
	}
	public Empresa() {
		super();
	}
	public int getIdEmpresa() {
		return idEmpresa;
	}
	public void setIdEmpresa(int idEmpresa) {
		this.idEmpresa = idEmpresa;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getCifp() {
		return cifp;
	}
	public void setCifp(String cifp) {
		this.cifp = cifp;
	}
	public String getDireccion() {
		return direccion;
	}
	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}
	public String getContacto() {
		return contacto;
	}
	public void setContacto(String contacto) {
		this.contacto = contacto;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	@Override
	public String toString() {
		return "Empresa [idEmpresa=" + idEmpresa + ", nombre=" + nombre + ", cifp=" + cifp + ", direccion=" + direccion
				+ ", contacto=" + contacto + ", email=" + email + "]";
	}
	
	
}
