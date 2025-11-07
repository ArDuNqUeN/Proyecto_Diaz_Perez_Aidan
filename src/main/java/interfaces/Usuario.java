package interfaces;

public class Usuario {
	
private int idUsuario;
private String nombre;
private String email;
private String password_harsh;
private String rol;
public Usuario(int idUsuario, String nombre, String email, String password_harsh, String rol) {
	super();
	this.idUsuario = idUsuario;
	this.nombre = nombre;
	this.email = email;
	this.password_harsh = password_harsh;
	this.rol = rol;
}
public Usuario() {
	super();
}
public int getIdUsuario() {
	return idUsuario;
}
public void setIdUsuario(int idUsuario) {
	this.idUsuario = idUsuario;
}
public String getNombre() {
	return nombre;
}
public void setNombre(String nombre) {
	this.nombre = nombre;
}
public String getEmail() {
	return email;
}
public void setEmail(String email) {
	this.email = email;
}
public String getPassword_harsh() {
	return password_harsh;
}
public void setPassword_harsh(String password_harsh) {
	this.password_harsh = password_harsh;
}
public String getRol() {
	return rol;
}
public void setRol(String rol) {
	this.rol = rol;
}
@Override
public String toString() {
	return "Usuario [idUsuario=" + idUsuario + ", nombre=" + nombre + ", email=" + email + ", password_harsh="
			+ password_harsh + ", rol=" + rol + "]";
}


}
