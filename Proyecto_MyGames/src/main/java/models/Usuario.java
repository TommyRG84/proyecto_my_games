package models;

import java.util.Date;

public class Usuario {
	private int id;
	private String username;
	private String nombre;
	private String email;
	private Date fechaNac;
	private String generoFavorito;
	private String password;
	private Date fechaRegistro;

	public Usuario(int id, String username, String nombre, String email, Date fechaNac, String generoFavorito,
			String password, Date fechaRegistro) {
		super();
		this.id = id;
		this.username = username;
		this.nombre = nombre;
		this.email = email;
		this.fechaNac = fechaNac;
		this.generoFavorito = generoFavorito;
		this.password = password;
		this.fechaRegistro = fechaRegistro;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
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

	public Date getFechaNac() {
		return fechaNac;
	}

	public void setFechaNac(Date fechaNac) {
		this.fechaNac = fechaNac;
	}

	public String getGeneroFavorito() {
		return generoFavorito;
	}

	public void setGeneroFavorito(String generoFavorito) {
		this.generoFavorito = generoFavorito;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Date getFechaRegistro() {
		return fechaRegistro;
	}

	public void setFechaRegistro(Date fechaRegistro) {
		this.fechaRegistro = fechaRegistro;
	}

}
