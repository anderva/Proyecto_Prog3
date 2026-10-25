package domain;

import java.util.ArrayList;

public class Consulta {
	// Parametros
	private static int contadorConsultas;
	private int idConsulta;
	private Cita cita;
	private String motivo;
	private String diagnostico;
	private String observaciones;
	private ArrayList<Receta> lRecetas;
	private double precio;

	// Constructores
	public Consulta() {

	}

	public Consulta(int idConsulta, Cita cita, String motivo, String diagnostico, String observaciones,
			ArrayList<Receta> lRecetas, double precio) {
		contadorConsultas++;
		this.idConsulta = contadorConsultas;
		this.cita = cita;
		this.motivo = motivo;
		this.diagnostico = diagnostico;
		this.observaciones = observaciones;
		this.lRecetas = new ArrayList<Receta>();
		this.precio = precio;
	}

	// Getters y Setters
	public int getIdConsulta() {
		return idConsulta;
	}

	public void setIdConsulta(int idConsulta) {
		this.idConsulta = idConsulta;
	}

	public Cita getCita() {
		return cita;
	}

	public void setCita(Cita cita) {
		this.cita = cita;
	}

	public String getMotivo() {
		return motivo;
	}

	public void setMotivo(String motivo) {
		this.motivo = motivo;
	}

	public String getDiagnostico() {
		return diagnostico;
	}

	public void setDiagnostico(String diagnostico) {
		this.diagnostico = diagnostico;
	}

	public String getObservaciones() {
		return observaciones;
	}

	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}

	public ArrayList<Receta> getlRecetas() {
		return lRecetas;
	}

	public void setlRecetas(ArrayList<Receta> lRecetas) {
		this.lRecetas = lRecetas;
	}

	public double getPrecio() {
		return precio;
	}

	public void setPrecio(double precio) {
		this.precio = precio;
	}

	@Override
	public String toString() {
		return "Consulta [idConsulta=" + idConsulta + ", cita=" + cita + ", motivo=" + motivo + ", diagnostico="
				+ diagnostico + ", observaciones=" + observaciones + ", lRecetas=" + lRecetas + ", precio=" + precio
				+ "]";
	}

}
