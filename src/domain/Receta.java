package domain;

import java.util.ArrayList;

public class Receta {
	// Parametros
	private static int contadorRecetas;
	private int idReceta;
	private String dosis;
	private String duración;
	private String indicaciones;
	private ArrayList<Medicamento> lMedicamentos;

	public Receta() {
		this.lMedicamentos = new ArrayList<Medicamento>();
	}

	public Receta(String dosis, String duracion, String indicaciones, ArrayList<Medicamento> lMedicamentos) {
		contadorRecetas++;
		this.idReceta = contadorRecetas;
		this.indicaciones = indicaciones;
		this.dosis = dosis;
		this.duración = duracion;
		this.lMedicamentos = lMedicamentos;
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


	public String getIndicaciones() {
		return indicaciones;
	}

	public void setIndicaciones(String indicaciones) {
		this.indicaciones = indicaciones;
	}

	public ArrayList<Medicamento> getlMedicamentos() {
		return lMedicamentos;
	}

	public void setlMedicamentos(ArrayList<Medicamento> lMedicamentos) {
		this.lMedicamentos = lMedicamentos;
	}

	

	public String getDosis() {
		return dosis;
	}

	public void setDosis(String dosis) {
		this.dosis = dosis;
	}

	public String getDuración() {
		return duración;
	}

	public void setDuración(String duración) {
		this.duración = duración;
	}
	
	

	@Override
	public String toString() {
		return "Receta [idReceta=" + idReceta + ", dosis=" + dosis + ", duración=" + duración + ", indicaciones="
				+ indicaciones + ", lMedicamentos=" + lMedicamentos + "]";
	}

	public void añadirMedicamento(Medicamento m) {
		lMedicamentos.add(m);
	}
}
