package gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

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
import domain.GestorHospital;
import domain.enums.Especialidad;
import domain.enums.EstadoCita;
import domain.enums.GrupoSanguineo;
import domain.enums.Sexo;
import domain.personas.Medico;
import domain.personas.Paciente;
import domain.personas.Usuario;

public class VentanaCitas extends JFrame {

	private static final long serialVersionUID = 1L;

	// modelo de datos propio de la tabla de citas
	class ModeloCitas extends AbstractTableModel {

		private static final long serialVersionUID = 1L;

		// nombres de las columnas
		private String[] cabeceras = { "ID", "Fecha", "Hora", "Paciente", "Médico", "Especialidad", "Estado" };
		private List<Cita> citas;

		public ModeloCitas(List<Cita> citas) {
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

	public VentanaCitas(GestorHospital gestor, Usuario usuario) {
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
		for (Medico m : gestor.getlMedicos()) {
			comboMedicos.addItem(m.getNombre() + " " + m.getApellidos() + " (" + m.getEspecialidad() + ")");
		}
		panelFiltro.add(comboMedicos);
		panelSuperior.add(panelFiltro, BorderLayout.CENTER);

		// panel central: tabla de citas 
		modeloCitas = new ModeloCitas(gestor.getlCitas());
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
		//actionListener del boton
		botonNuevaCita.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				new VentanaPedirCita(gestor);
			}
		});

		panelTotal.add(panelSuperior, BorderLayout.NORTH);
		panelTotal.add(scrollTabla, BorderLayout.CENTER);
		panelTotal.add(panelInferior, BorderLayout.SOUTH);

		this.add(panelTotal);

		setVisible(true);
	}

	public static void main(String[] args) {
		GestorHospital gestor = new GestorHospital();
		new VentanaCitas(gestor, null);
	}
}