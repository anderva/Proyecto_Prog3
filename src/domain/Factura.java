package domain;

import java.time.LocalDate;

import domain.enums.EstadoPago;
import domain.personas.Paciente;

public class Factura {

	private static int contadorFacturas;
	private int idFactura;
	private Paciente paciente;
	private LocalDate fecha;
	private String concepto;
	private double importe;
	private EstadoPago estadoPago;

	public Factura() {

	}

	public Factura(Paciente paciente, String concepto, double importe) {
		contadorFacturas++;
		this.idFactura = contadorFacturas;
		this.paciente = paciente;
		this.fecha = LocalDate.now();
		this.concepto = concepto;
		this.importe = importe;
		this.estadoPago = EstadoPago.PENDIENTE;
	}

	public static int getContadorFacturas() {
		return contadorFacturas;
	}

	public static void setContadorFacturas(int contadorFacturas) {
		Factura.contadorFacturas = contadorFacturas;
	}

	public int getIdFactura() {
		return idFactura;
	}

	public void setIdFactura(int idFactura) {
		this.idFactura = idFactura;
	}

	public Paciente getPaciente() {
		return paciente;
	}

	public void setPaciente(Paciente paciente) {
		this.paciente = paciente;
	}

	public LocalDate getFecha() {
		return fecha;
	}

	public void setFecha(LocalDate fecha) {
		this.fecha = fecha;
	}

	public String getConcepto() {
		return concepto;
	}

	public void setConcepto(String concepto) {
		this.concepto = concepto;
	}

	public double getImporte() {
		return importe;
	}

	public void setImporte(double importe) {
		this.importe = importe;
	}

	public EstadoPago getEstadoPago() {
		return estadoPago;
	}

	public void setEstadoPago(EstadoPago estadoPago) {
		this.estadoPago = estadoPago;
	}

	@Override
	public String toString() {
		return "Factura [idFactura=" + idFactura + ", paciente=" + paciente + ", fecha=" + fecha + ", concepto="
				+ concepto + ", importe=" + importe + ", estadoPago=" + estadoPago + "]";
	}

	public void pagar() {
		if (estadoPago == EstadoPago.PENDIENTE) {
			estadoPago = EstadoPago.PAGADA;
		}
	}

	public void anular() {
		if (estadoPago == EstadoPago.PENDIENTE) {
			estadoPago = EstadoPago.CANCELADA;
		}
	}
}
