package domain;

import java.util.ArrayList;

public class Receta {
	// Parametros
	private static int contadorRecetas;
	private int idReceta;
	private Medicamento medicamento;
	private String indicaciones;
	private ArrayList<Medicamento> lMedicamentos;

	public Receta() {

	}

	public Receta(Medicamento medicamento, String dosis, String duracion, String indicaciones) {
		contadorRecetas++;
		this.idReceta = contadorRecetas;
		this.medicamento = medicamento;
		this.indicaciones = indicaciones;
		this.lMedicamentos = new ArrayList<Medicamento>();
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

	public Medicamento getMedicamento() {
		return medicamento;
	}

	public void setMedicamento(Medicamento medicamento) {
		this.medicamento = medicamento;
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

	@Override
	public String toString() {
		return "Receta [idReceta=" + idReceta + ", medicamento=" + medicamento + ", indicaciones=" + indicaciones
				+ ", lMedicamentos=" + lMedicamentos + "]";
	}

	public void añadirMedicamento(Medicamento m) {
		lMedicamentos.add(m);
	}
}
