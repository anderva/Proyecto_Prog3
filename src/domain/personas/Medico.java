package domain.personas;

import java.time.LocalDate;
import java.util.ArrayList;

import domain.Horario;
import domain.enums.Especialidad;
import domain.enums.Sexo;

public class Medico extends Usuario {
	// Parámetros
	private static final long serialVersionUID = 1L;
	private static int contadorMedico;
	private int codMedico;
	private Especialidad especialidad;
	private ArrayList<Horario> lHorarios;
	private int duracionCita;

	// Constructores
	public Medico(String dni, String nombre, String apellidos, LocalDate fechaNacimiento, Sexo sexo, int telefono,
			String email, String direccion, String contrasenia) {
		super(dni, nombre, apellidos, fechaNacimiento, sexo, telefono, email, direccion, contrasenia);

	}

	public Medico(String dni, String nombre, String apellidos, LocalDate fechaNacimiento, Sexo sexo, int telefono,
			String email, String direccion, String contrasenia, Especialidad especialidad, ArrayList<Horario> lHorarios,
			int duracionCita) {
		super(dni, nombre, apellidos, fechaNacimiento, sexo, telefono, email, direccion, contrasenia);
		contadorMedico++;
		this.codMedico = contadorMedico;
		this.especialidad = especialidad;
		this.lHorarios = new ArrayList<Horario>();
		this.duracionCita = duracionCita;
	}

	// Getters y Setters
	public Especialidad getEspecialidad() {
		return especialidad;
	}

	public void setEspecialidad(Especialidad especialidad) {
		this.especialidad = especialidad;
	}

	public ArrayList<Horario> getlHorarios() {
		return lHorarios;
	}

	public void setlHorarios(ArrayList<Horario> lHorarios) {
		this.lHorarios = lHorarios;
	}

	public int getDuracionCita() {
		return duracionCita;
	}

	public void setDuracionCita(int duracionCita) {
		this.duracionCita = duracionCita;
	}

	public int getCodMedico() {
		return codMedico;
	}

	public void setCodMedico(int codMedico) {
		this.codMedico = codMedico;
	}

	@Override
	public String toString() {
		return "Medico [codMedico=" + codMedico + ", especialidad=" + especialidad + ", lHorarios=" + lHorarios
				+ ", duracionCita=" + duracionCita + "]";
	}
}
