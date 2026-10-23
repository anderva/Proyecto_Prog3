package domain.personas;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;
import domain.*;
import domain.enums.GrupoSanguineo;
import domain.enums.Sexo;

public class Paciente extends Usuario implements Serializable{

	
	private static final long serialVersionUID = 1L;
	private static int contadorPaciente;
	private int codigoPaciente;
	private List<String> lAlergias;
	private Consulta consulta;
	private int numSegSocial;
	private GrupoSanguineo grupoSanguineo;
	private HistorialClinico historialClinico;
	private LocalDate fechaAlta;
	private Habitacion habitacion;
	
	
	
	
	public Paciente(String dni, String nombre, String apellidos, LocalDate fechaNacimiento, Sexo sexo, int telefono,
			String email, String direccion, String contraseña, GrupoSanguineo grupoSanguineo) {
		super(dni, nombre, apellidos, fechaNacimiento, sexo, telefono, email, direccion, contraseña);
	}
	
	public Paciente(String dni, String nombre, String apellidos, LocalDate fechaNacimiento, Sexo sexo, int telefono, String email, String direccion, String contraseña, 
			int numSegSocial, List<String> lAlergias, Consulta consulta, HistorialClinico historialClinico, LocalDate fechaAlta, Habitacion habitacion, GrupoSanguineo grupoSanguineo) {
		
		super(dni, nombre, apellidos, fechaNacimiento, sexo, telefono, email, direccion, contraseña);
		this.codigoPaciente = contadorPaciente;
		contadorPaciente++;
		this.setNumSegSocial(numSegSocial);
		this.setlAlergias(lAlergias);
		this.setConsulta(consulta);
		this.setHistorialClinico(historialClinico);
		this.setFechaAlta(fechaAlta);
		this.setHabitacion(habitacion);
		this.setGrupoSanguineo(grupoSanguineo);
		
	}
	public List<String> getlAlergias() {
		return lAlergias;
	}

	public void setlAlergias(List<String> lAlergias) {
		this.lAlergias = lAlergias;
	}

	public Consulta getConsulta() {
		return consulta;
	}

	public void setConsulta(Consulta consulta) {
		this.consulta = consulta;
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
	
	public int getCodigoPaciente() {
		return codigoPaciente;
	}
	public void setCodigoPaciente(int codigoPaciente) {
		this.codigoPaciente = codigoPaciente;
		
	}
	
	public GrupoSanguineo getGrupoSanguineo() {
		return grupoSanguineo;
	}

	public void setGrupoSanguineo(GrupoSanguineo grupoSanguineo) {
		this.grupoSanguineo = grupoSanguineo;
	}

	@Override
	public String toString() {
		return "Paciente [" + super.toString() + "]";
	}
	
}
