package domain.personas;

import domain.enums.*;

public class Usuario {
	
	private String nomUsuario;
	private String contraseña;
	private TipoUsuario tipoUsuario;
	
	public String getContraseña() {
		return contraseña;
	}
	public void setContraseña(String contraseña) {
		this.contraseña = contraseña;
	}
	public String getNomUsuario() {
		return nomUsuario;
	}
	public void setNomUsuario(String nomUsuario) {
		this.nomUsuario = nomUsuario;
	}
	public TipoUsuario getTipoUsuario() {
		return tipoUsuario;
	}
	public void setTipoUsuario(TipoUsuario tipoUsuario) {
		this.tipoUsuario = tipoUsuario;
	}
	@Override
	public String toString() {
		return "Usuario [nomUsuario=" + nomUsuario + ", contraseña=" + contraseña + ", tipoUsuario=" + tipoUsuario
				+ "]";
	}	
}
