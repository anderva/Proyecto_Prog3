package domain;

import java.time.LocalDate;

import domain.enums.EstadoCita;
import domain.personas.Medico;
import domain.personas.Paciente;

public class Cita {
	//Parametros
	private static int contadorCitas;
	private int idCita;
	private Paciente paciente;
	private Medico medico;
	private LocalDate fechaCita;
	private EstadoCita estado;
	private String hora;
	
	
	//Constructores
	public Cita() {
		
	}


	public Cita(int idCita, Paciente paciente, Medico medico, LocalDate fechaCita, EstadoCita estado, String hora) {
		contadorCitas++;
		this.idCita = contadorCitas;
		this.paciente = paciente;
		this.medico = medico;
		this.fechaCita = fechaCita;
		this.estado = estado;
		this.hora = hora;
	}

	//Getters y Setters
	public int getIdCita() {
		return idCita;
	}


	public void setIdCita(int idCita) {
		this.idCita = idCita;
	}


	public Paciente getPaciente() {
		return paciente;
	}


	public void setPaciente(Paciente paciente) {
		this.paciente = paciente;
	}


	public Medico getMedico() {
		return medico;
	}


	public void setMedico(Medico medico) {
		this.medico = medico;
	}


	public LocalDate getFechaCita() {
		return fechaCita;
	}


	public void setFechaCita(LocalDate fechaCita) {
		this.fechaCita = fechaCita;
	}


	public EstadoCita getEstado() {
		return estado;
	}


	public void setEstado(EstadoCita estado) {
		this.estado = estado;
	}


	public String getHora() {
		return hora;
	}


	public void setHora(String hora) {
		this.hora = hora;
	}


	@Override
	public String toString() {
		return "Cita [idCita=" + idCita + ", paciente=" + paciente + ", medico=" + medico + ", fechaCita=" + fechaCita
				+ ", estado=" + estado + ", hora=" + hora + "]";
	}


	
	
	
	
	
}
