package domain;

import java.util.ArrayList;

public class Receta {
	//Parametros
	private static int contadorRecetas;
	private int idReceta;
	private String medicamento;
	private String dosis;
	private String duracion;
	private String indicaciones;
	
	public Receta() {
		
	}

	public Receta(int idReceta, String medicamento, String dosis, String duracion, String indicaciones) {
		contadorRecetas++;
		this.idReceta = contadorRecetas;
		this.medicamento = medicamento;
		this.dosis = dosis;
		this.duracion = duracion;
		this.indicaciones = indicaciones;
	}

	public static int getContadorRecetas() {
		return contadorRecetas;
	}

	public static void setContadorRecetas(int contadorRecetas) {
		Receta.contadorRecetas = contadorRecetas;
	}

	public int getIdReceta() {
		return idReceta;
	}

	public void setIdReceta(int idReceta) {
		this.idReceta = idReceta;
	}

	public String getMedicamento() {
		return medicamento;
	}

	public void setMedicamento(String medicamento) {
		this.medicamento = medicamento;
	}

	public String getDosis() {
		return dosis;
	}

	public void setDosis(String dosis) {
		this.dosis = dosis;
	}

	public String getDuracion() {
		return duracion;
	}

	public void setDuracion(String duracion) {
		this.duracion = duracion;
	}

	public String getIndicaciones() {
		return indicaciones;
	}

	public void setIndicaciones(String indicaciones) {
		this.indicaciones = indicaciones;
	}

	@Override
	public String toString() {
		return "Receta [idReceta=" + idReceta + ", medicamento=" + medicamento + ", dosis=" + dosis + ", duracion="
				+ duracion + ", indicaciones=" + indicaciones + "]";
	}
	
	
	
	
	
	
}
