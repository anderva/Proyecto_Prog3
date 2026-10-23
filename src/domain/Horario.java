package domain;

import domain.enums.DiaSemana;

public class Horario {
	
	//Parámetros
	private DiaSemana dia;
	private String horaInicio;
	private String horaFin;
	
	//Constructores
	public Horario() {
		
	}

	public Horario(DiaSemana dia, String horaInicio, String horaFin) {
		super();
		this.dia = dia;
		this.horaInicio = horaInicio;
		this.horaFin = horaFin;
	}
	
	//Getters y Setters
	public DiaSemana getDia() {
		return dia;
	}

	public void setDia(DiaSemana dia) {
		this.dia = dia;
	}

	public String getHoraInicio() {
		return horaInicio;
	}

	public void setHoraInicio(String horaInicio) {
		this.horaInicio = horaInicio;
	}

	public String getHoraFin() {
		return horaFin;
	}

	public void setHoraFin(String horaFin) {
		this.horaFin = horaFin;
	}

	
	@Override
	public String toString() {
		return "Horario [dia=" + dia + ", horaInicio=" + horaInicio + ", horaFin=" + horaFin + "]";
	}
	
	
}
