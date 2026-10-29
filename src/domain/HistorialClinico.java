package domain;

import java.util.ArrayList;

import domain.personas.Paciente;

public class HistorialClinico {
	//Parametros
	private static int contadorHistorial;
	private int idHistorialClinico;
	private Paciente paciente;
	private ArrayList<Consulta> lConsulta;
	
	//Constructores
	public HistorialClinico() {
		
	}

	public HistorialClinico(int idHistorialClinico, Paciente paciente, String indicaciones) {
		contadorHistorial++;
		this.idHistorialClinico = contadorHistorial;
		this.paciente = paciente;
		this.lConsulta = new ArrayList<Consulta>();
	}
	
	//Getters y Setters
	public static int getContadorHistorial() {
		return contadorHistorial;
	}

	public static void setContadorHistorial(int contadorHistorial) {
		HistorialClinico.contadorHistorial = contadorHistorial;
	}

	public int getIdHistorialClinico() {
		return idHistorialClinico;
	}

	public void setIdHistorialClinico(int idHistorialClinico) {
		this.idHistorialClinico = idHistorialClinico;
	}

	public Paciente getPaciente() {
		return paciente;
	}

	public void setPaciente(Paciente paciente) {
		this.paciente = paciente;
	}

	

	public ArrayList<Consulta> getlConsulta() {
		return lConsulta;
	}

	public void setlConsulta(ArrayList<Consulta> lReceta) {
		this.lConsulta = lReceta;
	}

	@Override
	public String toString() {
		return "HistorialClinico [idHistorialClinico=" + idHistorialClinico + ", paciente=" + paciente + ", lConsulta="
				+ lConsulta + "]";
	}

	
	
	
	
	
}
