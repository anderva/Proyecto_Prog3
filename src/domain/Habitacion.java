package domain;

import domain.enums.TipoHabitacion;

public class Habitacion {

	private static int cont;
	private int numHabitacion;
	private TipoHabitacion tipoHabitacion;

	public Habitacion() {

	}

	public Habitacion(int camaDisp, TipoHabitacion tipoHabitacion) {
		cont++;
		this.numHabitacion = cont;
		this.tipoHabitacion = tipoHabitacion;
	}

	public static int getCont() {
		return cont;
	}

	public static void setCont(int cont) {
		Habitacion.cont = cont;
	}

	public int getNumHabitacion() {
		return numHabitacion;
	}

	public void setNumHabitacion(int numHabitacion) {
		this.numHabitacion = numHabitacion;
	}

	public TipoHabitacion getTipoHabitacion() {
		return tipoHabitacion;
	}

	public void setTipoHabitacion(TipoHabitacion tipoHabitacion) {
		this.tipoHabitacion = tipoHabitacion;
	}

	@Override
	public String toString() {
		return "Habitacion [numHabitacion=" + numHabitacion + ", tipoHabitacion=" + tipoHabitacion + "]";
	}

	public int getCapacidad() {
		switch (tipoHabitacion) {
		case INDIVIDUAL:
			return 1;
		case DOBLE:
			return 2;
		case TRIPLE:
			return 3;
		default:
			return 0;
		}
	}

}
