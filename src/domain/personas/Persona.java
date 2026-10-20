package domain.personas;
import domain.enums.Sexo;

import java.io.Serializable;
import java.time.LocalDate;

public abstract class Persona implements Serializable{

	private static final long serialVersionUID = 1L;

	// Parametros:
	private String dni;
	private String nombre;
	private String apellidos;
	private LocalDate fechaNacimiento;
	private Sexo sexo;
	private int telefono;
	private String email;
	private String direccion;

	// Constructor(es)
	public Persona(String dni, String nombre, String apellidos, LocalDate fechaNacimiento, Sexo sexo, int telefono,
			String email, String direccion) {
		super();
		this.dni = dni;
		this.nombre = nombre;
		this.apellidos = apellidos;
		this.fechaNacimiento = fechaNacimiento;
		this.sexo = sexo;
		this.telefono = telefono;
		this.email = email;
		this.direccion = direccion;
	}

	// Getters y Setters 

	private static final String LETRAS_DNI = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";


	public String getDni() {
		return dni;
	}

	public void setDni(String dni) {
        this.dni = dni;
    }

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getApellidos() {
		return apellidos;
	}

	public void setApellidos(String apellidos) {
		this.apellidos = apellidos;
	}

	public LocalDate getFechaNacimiento() {
		return fechaNacimiento;
	}

	public void setFechaNacimiento(LocalDate fechaNacimiento) {
		if(fechaNacimiento.isAfter(LocalDate.now())) {
			System.err.println("Fecha invalida");
			return;
		}
		this.fechaNacimiento = fechaNacimiento;
	}

	public Sexo getSexo() {
		return sexo;
	}

	public void setSexo(Sexo sexo) {
		this.sexo = sexo;
	}

	public int getTelefono() {
		return telefono;
	}

	public void setTelefono(int telefono) {
		this.telefono = telefono;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getDireccion() {
		return direccion;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}


}	
