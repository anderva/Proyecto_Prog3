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
import javax.swing.table.AbstractTableModel;

import domain.Cita;
import domain.enums.EstadoCita;
import domain.enums.GrupoSanguineo;
import domain.enums.Sexo;
import domain.personas.Medico;
import domain.personas.Paciente;

public class VentanaCitas extends JFrame {

	private static final long serialVersionUID = 1L;

	// modelo de datos propio de la tabla de citas
	class ModeloCitas extends AbstractTableModel {

		private static final long serialVersionUID = 1L;

		// nombres de las columnas
		private String[] cabeceras = { "ID", "Fecha", "Hora", "Paciente", "Médico", "Especialidad", "Estado" };
		private ArrayList<Cita> citas;

		public ModeloCitas(ArrayList<Cita> citas) {
			this.citas = citas;
		}

		@Override
		public String getColumnName(int columna) {
			return cabeceras[columna];
		}

		@Override
		public int getRowCount() {
			return citas.size();
		}

		@Override
		public int getColumnCount() {
			return cabeceras.length;
		}

		@Override
		public Object getValueAt(int fila, int columna) {
			// se coge la cita de esa fila y se devuelve el dato de la columna
			Cita c = citas.get(fila);
			if (columna == 0) {
				return c.getIdCita();
			} else if (columna == 1) {
				return c.getFechaCita();
			} else if (columna == 2) {
				return c.getHora();
			} else if (columna == 3) {
				return c.getPaciente().getNombre() + " " + c.getPaciente().getApellidos();
			} else if (columna == 4) {
				return c.getMedico().getNombre() + " " + c.getMedico().getApellidos();
			} else if (columna == 5) {
				return c.getMedico().getEspecialidad();
			} else if (columna == 6) {
				return c.getEstado();
			}
			return null;
		}

		@Override
		public boolean isCellEditable(int fila, int columna) {
			return false; // no se puede escribir en la tabla
		}
	}

	private ModeloCitas modeloCitas;

	// componentes que van a usar los listeners
	private JComboBox<String> comboMedicos;
	private JTable tablaCitas;
	private JButton botonNuevaCita;
	private JButton botonCancelarCita;
	private JButton botonReprogramar;
	private JButton botonRealizada;

	public VentanaCitas(ArrayList<Medico> lMedicos, ArrayList<Cita> lCitas) {
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
		modeloCitas = new ModeloCitas(lCitas);
		tablaCitas = new JTable(modeloCitas);
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
		Medico m1 = new Medico("12345678A", "Aitor", "Garcia", LocalDate.of(1980, 1, 15), Sexo.HOMBRE, 611111111,
				"garcia@osaki.eus", "Bilbao", "abcd", "Cardiologia", new ArrayList<>(), 30);
		Medico m2 = new Medico("12345678B", "Ander", "Vallejo", LocalDate.of(1975, 7, 2), Sexo.HOMBRE, 622222222,
				"vallejo@osaki.eus", "Bilbao", "abcd", "Pediatría", new ArrayList<>(), 20);
		Medico m3 = new Medico("12345678C", "Miren", "Ortiz", LocalDate.of(1983, 4, 22), Sexo.MUJER, 633333333,
				"ortiz@osaki.eus", "Getxo", "abcd", "Traumatologia", new ArrayList<>(), 30);
		medicos.add(m1);
		medicos.add(m2);
		medicos.add(m3);

		// pacientes de prueba
		Paciente p1 = new Paciente("11111111D", "Mikel", "Arana", LocalDate.of(2000, 3, 10), Sexo.HOMBRE, 644444444,
				"mikel@gmail.com", "Bilbao", "1234", GrupoSanguineo.A_PLUS);
		Paciente p2 = new Paciente("22222222E", "Ane", "Goikoetxea", LocalDate.of(1995, 8, 5), Sexo.MUJER, 655555555,
				"ane@gmail.com", "Barakaldo", "1234", GrupoSanguineo.ZERO_PLUS);
		Paciente p3 = new Paciente("33333333F", "Jon", "Etxeberria", LocalDate.of(2015, 12, 1), Sexo.HOMBRE, 666666666,
				"jon@gmail.com", "Bilbao", "1234", GrupoSanguineo.B_MINUS);
		Paciente p4 = new Paciente("44444444G", "Laura", "Martinez", LocalDate.of(1970, 6, 18), Sexo.MUJER, 677777777,
				"laura@gmail.com", "Getxo", "1234", GrupoSanguineo.AB_PLUS);

		// citas de prueba
		ArrayList<Cita> citas = new ArrayList<Cita>();
		citas.add(new Cita(0, p1, m1, LocalDate.of(2026, 10, 20), EstadoCita.REALIZADA, "09:00"));
		citas.add(new Cita(0, p2, m3, LocalDate.of(2026, 10, 21), EstadoCita.CANCELADA, "10:30"));
		citas.add(new Cita(0, p4, m1, LocalDate.of(2026, 10, 22), EstadoCita.REALIZADA, "11:00"));
		citas.add(new Cita(0, p3, m2, LocalDate.of(2026, 10, 27), EstadoCita.PENDIENTE, "09:00"));
		citas.add(new Cita(0, p1, m2, LocalDate.of(2026, 10, 27), EstadoCita.PENDIENTE, "09:30"));
		citas.add(new Cita(0, p2, m1, LocalDate.of(2026, 10, 28), EstadoCita.PENDIENTE, "10:00"));
		citas.add(new Cita(0, p4, m3, LocalDate.of(2026, 10, 28), EstadoCita.PENDIENTE, "12:00"));
		citas.add(new Cita(0, p3, m1, LocalDate.of(2026, 10, 29), EstadoCita.PENDIENTE, "11:30"));
		citas.add(new Cita(0, p1, m3, LocalDate.of(2026, 10, 30), EstadoCita.PENDIENTE, "13:00"));
		citas.add(new Cita(0, p2, m2, LocalDate.of(2026, 11, 3), EstadoCita.PENDIENTE, "09:00"));

		new VentanaCitas(medicos, citas);
	}
}