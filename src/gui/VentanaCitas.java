package gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.time.LocalDate;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import domain.enums.Sexo;
import domain.personas.Medico;

public class VentanaCitas extends JFrame {

	private static final long serialVersionUID = 1L;

	// componentes que van a usar los listeners
	private JComboBox<String> comboMedicos;
	private JTable tablaCitas;
	private JButton botonNuevaCita;
	private JButton botonCancelarCita;
	private JButton botonReprogramar;
	private JButton botonRealizada;

	public VentanaCitas(ArrayList<Medico> lMedicos) {
		setTitle("OsakiDeusto - Citas");
		setSize(800, 500);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE); // porque es ventana secundaria
		setLocationRelativeTo(null); // centrar en la pantalla

		JPanel panelTotal = new JPanel(new BorderLayout(0, 10));
		panelTotal.setBorder(BorderFactory.createEmptyBorder(15, 25, 10, 25));

		// panel superior: titulo y filtro
		JPanel panelSuperior = new JPanel(new BorderLayout(0, 10));

		JLabel labelTitulo = new JLabel("GESTIÓN DE CITAS", JLabel.CENTER);
		labelTitulo.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 18));
		panelSuperior.add(labelTitulo, BorderLayout.NORTH);

		JPanel panelFiltro = new JPanel(new FlowLayout(FlowLayout.LEFT));
		JLabel labelMedico = new JLabel("Médico: ");
		panelFiltro.add(labelMedico);
		comboMedicos = new JComboBox<String>();
		comboMedicos.addItem("Todos");
		for (Medico m : lMedicos) {
			comboMedicos.addItem(m.getNombre() + " " + m.getApellidos() + " (" + m.getEspecialidad() + ")");
		}
		panelFiltro.add(comboMedicos);
		panelSuperior.add(panelFiltro, BorderLayout.CENTER);

		// panel central: tabla de citas 
		tablaCitas = new JTable();
		tablaCitas.getTableHeader().setFont(tablaCitas.getTableHeader().getFont().deriveFont(Font.BOLD));
		JScrollPane scrollTabla = new JScrollPane(tablaCitas);

		// panel de abajo: botones de gestion
		JPanel panelInferior = new JPanel(new GridLayout(1, 4, 10, 10));
		panelInferior.setBorder(BorderFactory.createEmptyBorder(10, 0, 5, 0));
		botonNuevaCita = new JButton("Nueva cita");
		panelInferior.add(botonNuevaCita);
		botonCancelarCita = new JButton("Cancelar cita");
		panelInferior.add(botonCancelarCita);
		botonReprogramar = new JButton("Reprogramar");
		panelInferior.add(botonReprogramar);
		botonRealizada = new JButton("Marcar realizada");
		panelInferior.add(botonRealizada);

		panelTotal.add(panelSuperior, BorderLayout.NORTH);
		panelTotal.add(scrollTabla, BorderLayout.CENTER);
		panelTotal.add(panelInferior, BorderLayout.SOUTH);

		this.add(panelTotal);

		setVisible(true);
	}

	public static void main(String[] args) {
		// medicos de prueba
		ArrayList<Medico> medicos = new ArrayList<Medico>();
		medicos.add(new Medico("12345678A", "Aitor", "Garcia", LocalDate.of(1980, 1, 15), Sexo.MUJER, 611111111,
				"filete@osaki.eus", "Bilbao", "abcd", "Cardiologia", new ArrayList<>(), 30));
		medicos.add(new Medico("12345678B", "Ander", "Vallejo", LocalDate.of(1975, 7, 2), Sexo.HOMBRE, 622222222,
				"vallejo@osaki.eus", "Bilbao", "abcd", "Pediatría", new ArrayList<>(), 20));

		new VentanaCitas(medicos);
	}
}