package domain.personas;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;
import domain.*;
import domain.enums.Sexo;

public class Paciente extends Persona implements Serializable{

	@Override
	public String toString() {
		return "Paciente [lAlergias=" + lAlergias + ", diagnostico=" + diagnostico + ", numSegSocial=" + numSegSocial
				+ ", historialClinico=" + historialClinico + ", fechaAlta=" + fechaAlta + ", habitacion=" + habitacion
				+ "]";
	}

	private static final long serialVersionUID = 1L;
	private List<Alergia> lAlergias;
	private Diagnostico diagnostico;
	private int numSegSocial;
	private HistorialClinico historialClinico;
	private LocalDate fechaAlta;
	private Habitacion habitacion;
	
	public Paciente(String dni, String nombre, String apellidos, LocalDate fechaNacimiento, Sexo sexo, int telefono, String email, String direccion,
			int numSegSocial, List<Alergia> lAlergias, Diagnostico diagnostico, HistorialClinico historialClinico, LocalDate fechaAlta, Habitacion habitacion) {
		
		super(dni, nombre, apellidos, fechaNacimiento, sexo, telefono, email, direccion);
		this.setNumSegSocial(numSegSocial);
		this.setlAlergias(lAlergias);
		this.setDiagnostico(diagnostico);
		this.setHistorialClinico(historialClinico);
		this.setFechaAlta(fechaAlta);
		this.setHabitacion(habitacion);
		
		// TODO Auto-generated constructor stub
	}
	public List<Alergia> getlAlergias() {
		return lAlergias;
	}

	public void setlAlergias(List<Alergia> lAlergias) {
		this.lAlergias = lAlergias;
	}

	public Diagnostico getDiagnostico() {
		return diagnostico;
	}

	public void setDiagnostico(Diagnostico diagnostico) {
		this.diagnostico = diagnostico;
	}

	public int getNumSegSocial() {
		return numSegSocial;
	}

	public void setNumSegSocial(int numSegSocial) {
		this.numSegSocial = numSegSocial;
	}

	public HistorialClinico getHistorialClinico() {
		return historialClinico;
	}

	public void setHistorialClinico(HistorialClinico historialClinico) {
		this.historialClinico = historialClinico;
	}

	public LocalDate getFechaAlta() {
		return fechaAlta;
	}

	public void setFechaAlta(LocalDate fechaAlta) {
		this.fechaAlta = fechaAlta;
	}

	public Habitacion getHabitacion() {
		return habitacion;
	}

	public void setHabitacion(Habitacion habitacion) {
		this.habitacion = habitacion;
	}

	
}
