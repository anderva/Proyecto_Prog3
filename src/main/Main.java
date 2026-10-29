package main;
import domain.*;
import gui.*;

public class Main {
	
	public static void main(String[] args) {
		GestorHospital gestor = new GestorHospital();
		VentanaInicial ventanaInicial = new VentanaInicial(gestor);
	}

}
