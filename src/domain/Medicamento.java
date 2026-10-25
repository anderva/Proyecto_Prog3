package domain;

public class Medicamento {

	private String nombre;
	private String dosis;
	private int duracionDias;

	public Medicamento() {

	}

	public Medicamento(String nombre, String dosis, int duracionDias) {
		super();
		this.nombre = nombre;
		this.dosis = dosis;
		this.duracionDias = duracionDias;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getDosis() {
		return dosis;
	}

	public void setDosis(String dosis) {
		this.dosis = dosis;
	}

	public int getDuracionDias() {
		return duracionDias;
	}

	public void setDuracionDias(int duracionDias) {
		this.duracionDias = duracionDias;
	}

	@Override
	public String toString() {
		return "Medicamento [nombre=" + nombre + ", dosis=" + dosis + ", duracionDias=" + duracionDias + "]";
	}

}
