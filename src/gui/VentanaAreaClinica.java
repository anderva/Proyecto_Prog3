package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.table.DefaultTableModel;

import domain.Cita;
import domain.Consulta;
import domain.GestorHospital;
import domain.HistorialClinico;
import domain.Medicamento;
import domain.Receta;
import domain.enums.EstadoCita;
import domain.enums.GrupoSanguineo;
import domain.enums.TipoUsuario;
import domain.personas.Medico;
import domain.personas.Paciente;
import domain.personas.Usuario;

public class VentanaAreaClinica extends JFrame {

	private static final long serialVersionUID = 1L;

	private GestorHospital gestor;
	private Usuario usuario;
	private TipoUsuario rol;
	private Paciente pacienteActual;

	private JComboBox<String> comboPacientes;
	private JTextField txtNumSegSocial;
	private JComboBox<GrupoSanguineo> comboGrupoSanguineo;
	private DefaultListModel<String> modeloAlergias;
	private JList<String> listaAlergias;

	private DefaultTableModel modeloConsultas;
	private JTable tablaConsultas;
	private JTextArea areaDetalleRecetas;

	// Campos de nueva consulta (solo médico)
	private JComboBox<String> comboCitasPaciente;
	private List<Cita> citasDisponibles = new ArrayList<>();
	private JTextField txtDiagnostico;
	private JTextField txtObservaciones;
	private JSpinner spinnerPrecio;
	private ArrayList<Receta> recetasBorrador = new ArrayList<>();
	private DefaultListModel<String> modeloRecetasBorrador;

	public VentanaAreaClinica(GestorHospital gestor, Usuario usuario) {
		this.gestor = gestor;
		this.usuario = usuario;
		this.rol = GestorHospital.obtenerTipoUsuario(usuario);

		setTitle("OsakiDeusto - Área Clínica e Historial");
		setSize(980, 650);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setLocationRelativeTo(null);

		if (rol == TipoUsuario.PACIENTE) {
			this.pacienteActual = (Paciente) usuario;
		} else if (!gestor.getlPacientes().isEmpty()) {
			this.pacienteActual = gestor.getlPacientes().get(0);
		}

		JPanel panelPrincipal = new JPanel(new BorderLayout(10, 10));
		panelPrincipal.setBorder(BorderFactory.createEmptyBorder(12, 16, 12, 16));

		// Cabecera con selector de paciente (solo visible si no es un paciente)
		JPanel panelNorte = new JPanel(new BorderLayout(10, 5));
		JLabel lblTitulo = new JLabel();
		if(rol == TipoUsuario.PACIENTE) {
			lblTitulo = new JLabel("MI HISTORIAL CLÍNICO Y RECETAS");
		}else {
			lblTitulo = new JLabel("ÁREA CLÍNICA DEL MÉDICO");
		}
		lblTitulo.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 18));
		lblTitulo.setForeground(Color.blue);
		panelNorte.add(lblTitulo, BorderLayout.WEST);

		if (rol != TipoUsuario.PACIENTE) {
			JPanel panelSel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
			panelSel.add(new JLabel("Paciente:"));
			comboPacientes = new JComboBox<>();
			for (Paciente p : gestor.getlPacientes()) {
				comboPacientes.addItem(p.getNombre() + " " + p.getApellidos() + " (" + p.getDni() + ")");
			}
			comboPacientes.addActionListener(e -> {
				int idx = comboPacientes.getSelectedIndex();
				if (idx >= 0) {
					pacienteActual = gestor.getlPacientes().get(idx);
					recetasBorrador.clear();
					if (modeloRecetasBorrador != null) {
						modeloRecetasBorrador.clear();
					}
					cargarDatosPaciente();
				}
			});
			panelSel.add(comboPacientes);
			panelNorte.add(panelSel, BorderLayout.EAST);
		}

		panelPrincipal.add(panelNorte, BorderLayout.NORTH);

		// Pestañas
		JTabbedPane tabs = new JTabbedPane();
		tabs.addTab("Historial Clínico y Recetas", crearPanelHistorial());
		tabs.addTab("Ficha Médica y Alergias", crearPanelFichaMedica());
		if (rol != TipoUsuario.PACIENTE) {
			tabs.addTab("Registrar Consulta y Receta", crearPanelNuevaConsulta());
		}

		panelPrincipal.add(tabs, BorderLayout.CENTER);
		add(panelPrincipal);

		cargarDatosPaciente();
		setVisible(true);
	}

	private JPanel crearPanelHistorial() {
		JPanel panel = new JPanel(new BorderLayout(10, 10));
		panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

		String[] cols = { "ID Consulta", "Fecha", "Médico", "Motivo", "Diagnóstico", "Precio (€)" };
		modeloConsultas = new DefaultTableModel(cols, 0) {
			private static final long serialVersionUID = 1L;
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		tablaConsultas = new JTable(modeloConsultas);
		tablaConsultas.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		tablaConsultas.setRowHeight(25);
		tablaConsultas.getSelectionModel().addListSelectionListener(e -> {
			if (!e.getValueIsAdjusting()) {
				mostrarDetalleConsultaSeleccionada();
			}
		});

		areaDetalleRecetas = new JTextArea(9, 40);
		areaDetalleRecetas.setEditable(false);
		areaDetalleRecetas.setBorder(BorderFactory.createTitledBorder("Observaciones y Recetas / Medicamentos prescritos"));

		panel.add(new JScrollPane(tablaConsultas), BorderLayout.CENTER);
		panel.add(new JScrollPane(areaDetalleRecetas), BorderLayout.SOUTH);
		return panel;
	}

	private JPanel crearPanelFichaMedica() {
		JPanel panel = new JPanel(new BorderLayout(15, 15));
		panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

		boolean esMedico = (rol != TipoUsuario.PACIENTE);

		JPanel panelDatos = new JPanel(new GridLayout(3, 2, 10, 10));
		panelDatos.setBorder(BorderFactory.createTitledBorder("Datos Clínicos del Paciente"));

		panelDatos.add(new JLabel("Nº Seguridad Social:"));
		txtNumSegSocial = new JTextField();
		txtNumSegSocial.setEditable(esMedico);
		panelDatos.add(txtNumSegSocial);

		panelDatos.add(new JLabel("Grupo Sanguíneo:"));
		comboGrupoSanguineo = new JComboBox<>(GrupoSanguineo.values());
		comboGrupoSanguineo.setEnabled(esMedico);
		panelDatos.add(comboGrupoSanguineo);

		JButton btnGuardarFicha = new JButton("Guardar datos clínicos");
		btnGuardarFicha.setEnabled(esMedico);
		btnGuardarFicha.addActionListener(e -> guardarFichaMedica());
		panelDatos.add(new JLabel());
		panelDatos.add(btnGuardarFicha);

		// Alergias (tanto el médico como el propio paciente pueden añadir/quitar alergias)
		JPanel panelAlergias = new JPanel(new BorderLayout(8, 8));
		panelAlergias.setBorder(BorderFactory.createTitledBorder("Alergias registradas"));
		modeloAlergias = new DefaultListModel<>();
		listaAlergias = new JList<>(modeloAlergias);
		panelAlergias.add(new JScrollPane(listaAlergias), BorderLayout.CENTER);

		JPanel panelBotonesAlergia = new JPanel(new FlowLayout(FlowLayout.LEFT));
		JButton btnAddAlergia = new JButton("Añadir alergia");
		JButton btnDelAlergia = new JButton("Eliminar seleccionada");

		btnAddAlergia.addActionListener(e -> {
			if (pacienteActual == null) return;
			String alergia = JOptionPane.showInputDialog(this, "Introduce el nombre de la alergia:");
			if (alergia != null && !alergia.isBlank()) {
				pacienteActual.getlAlergias().add(alergia.trim());
				cargarDatosPaciente();
			}
		});

		btnDelAlergia.addActionListener(e -> {
			int idx = listaAlergias.getSelectedIndex();
			if (pacienteActual != null && idx >= 0) {
				pacienteActual.getlAlergias().remove(idx);
				cargarDatosPaciente();
			}
		});

		panelBotonesAlergia.add(btnAddAlergia);
		panelBotonesAlergia.add(btnDelAlergia);
		panelAlergias.add(panelBotonesAlergia, BorderLayout.SOUTH);

		panel.add(panelDatos, BorderLayout.NORTH);
		panel.add(panelAlergias, BorderLayout.CENTER);
		return panel;
	}

	private JPanel crearPanelNuevaConsulta() {
		JPanel panel = new JPanel(new BorderLayout(10, 10));
		panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

		JPanel form = new JPanel(new GridLayout(4, 2, 10, 8));
		form.setBorder(BorderFactory.createTitledBorder("Datos de la Consulta Médica"));

		form.add(new JLabel("Cita asociada:"));
		comboCitasPaciente = new JComboBox<>();
		form.add(comboCitasPaciente);

		form.add(new JLabel("Diagnóstico:"));
		txtDiagnostico = new JTextField();
		form.add(txtDiagnostico);

		form.add(new JLabel("Observaciones:"));
		txtObservaciones = new JTextField();
		form.add(txtObservaciones);

		form.add(new JLabel("Importe consulta (€):"));
		spinnerPrecio = new JSpinner(new SpinnerNumberModel(45.0, 0.0, 5000.0, 5.0));
		form.add(spinnerPrecio);

		// Panel de recetas y medicamentos
		JPanel panelRecetas = new JPanel(new BorderLayout(8, 8));
		panelRecetas.setBorder(BorderFactory.createTitledBorder("Recetas y Medicamentos de esta consulta"));
		modeloRecetasBorrador = new DefaultListModel<>();
		JList<String> listaRecetas = new JList<>(modeloRecetasBorrador);
		panelRecetas.add(new JScrollPane(listaRecetas), BorderLayout.CENTER);

		JPanel botonesReceta = new JPanel(new FlowLayout(FlowLayout.LEFT));
		JButton btnNuevaReceta = new JButton("+ Añadir Receta con Medicamento");
		btnNuevaReceta.addActionListener(e -> dialogoAgregarReceta());
		botonesReceta.add(btnNuevaReceta);
		panelRecetas.add(botonesReceta, BorderLayout.SOUTH);

		JButton btnRegistrarConsulta = new JButton("Finalizar Consulta, Emitir Recetas y Generar Factura");
		btnRegistrarConsulta.addActionListener(e -> guardarNuevaConsulta());

		panel.add(form, BorderLayout.NORTH);
		panel.add(panelRecetas, BorderLayout.CENTER);
		panel.add(btnRegistrarConsulta, BorderLayout.SOUTH);
		return panel;
	}

	private void dialogoAgregarReceta() {
		JTextField txtMed = new JTextField();
		JTextField txtDosis = new JTextField();
		JSpinner spDias = new JSpinner(new SpinnerNumberModel(7, 1, 365, 1));
		JTextField txtIndicaciones = new JTextField();

		JPanel p = new JPanel(new GridLayout(4, 2, 8, 8));
		p.add(new JLabel("Medicamento:"));
		p.add(txtMed);
		p.add(new JLabel("Dosis (ej. 1 cada 8h):"));
		p.add(txtDosis);
		p.add(new JLabel("Duración (días):"));
		p.add(spDias);
		p.add(new JLabel("Indicaciones:"));
		p.add(txtIndicaciones);

		int res = JOptionPane.showConfirmDialog(this, p, "Nueva Receta", JOptionPane.OK_CANCEL_OPTION);
		if (res == JOptionPane.OK_OPTION) {
			if (txtMed.getText().isBlank() || txtDosis.getText().isBlank()) {
				JOptionPane.showMessageDialog(this, "Indica el nombre del medicamento y la dosis.");
				return;
			}
			int dias = (Integer) spDias.getValue();
			ArrayList<Medicamento> ms = new ArrayList<Medicamento>();
			Medicamento m = new Medicamento(txtMed.getText().trim(), txtDosis.getText().trim(), dias);
			ms.add(m);
			Receta r = new Receta(m.getDosis(), String.valueOf(dias), txtIndicaciones.getText().trim(), ms);
			r.añadirMedicamento(m);
			recetasBorrador.add(r);
			modeloRecetasBorrador.addElement("Receta #" + r.getIdReceta() + ": " + m.getNombre() + " (" + m.getDosis()
					+ ", " + dias + " días) - " + r.getIndicaciones());
		}
	}

	private void guardarFichaMedica() {
		if (pacienteActual == null) return;
		try {
			int nss = Integer.parseInt(txtNumSegSocial.getText().trim());
			pacienteActual.setNumSegSocial(nss);
			pacienteActual.setGrupoSanguineo((GrupoSanguineo) comboGrupoSanguineo.getSelectedItem());
			JOptionPane.showMessageDialog(this, "Ficha clínica actualizada correctamente.");
		} catch (NumberFormatException ex) {
			JOptionPane.showMessageDialog(this, "El Nº de Seguridad Social debe ser numérico.", "Error", JOptionPane.ERROR_MESSAGE);
		}
	}

	private void guardarNuevaConsulta() {
		if (pacienteActual == null || citasDisponibles.isEmpty()) {
			JOptionPane.showMessageDialog(this, "Este paciente no tiene citas disponibles para asociar la consulta.");
			return;
		}
		if (txtDiagnostico.getText().isBlank()) {
			JOptionPane.showMessageDialog(this, "Debes escribir un diagnóstico.");
			return;
		}

		Cita citaSel = citasDisponibles.get(comboCitasPaciente.getSelectedIndex());
		double precio = (Double) spinnerPrecio.getValue();
		ArrayList<Receta> copiaRecetas = new ArrayList<>(recetasBorrador);

		gestor.registrarConsulta(citaSel, txtDiagnostico.getText().trim(), txtObservaciones.getText().trim(), precio, copiaRecetas);

		txtDiagnostico.setText("");
		txtObservaciones.setText("");
		recetasBorrador.clear();
		modeloRecetasBorrador.clear();
		cargarDatosPaciente();

		JOptionPane.showMessageDialog(this, "Consulta registrada en el historial clínico y factura generada.");
	}

	private void cargarDatosPaciente() {
		if (pacienteActual == null) return;

		txtNumSegSocial.setText(String.valueOf(pacienteActual.getNumSegSocial()));
		if (pacienteActual.getGrupoSanguineo() != null) {
			comboGrupoSanguineo.setSelectedItem(pacienteActual.getGrupoSanguineo());
		}

		modeloAlergias.clear();
		if (pacienteActual.getlAlergias() != null) {
			for (String a : pacienteActual.getlAlergias()) {
				modeloAlergias.addElement(a);
			}
		}

		// Cargar consultas del HistorialClinico
		modeloConsultas.setRowCount(0);
		HistorialClinico hc = gestor.getHistorialPaciente(pacienteActual);
		for (Consulta c : hc.getlConsulta()) {
			String fecha = "";
			String medico = "";
			if(c.getCita() != null) {
				fecha =  c.getCita().getFechaCita().format(GestorHospital.FORMATO_FECHA);
				medico = c.getCita().getMedico().getNombre() + " " + c.getCita().getMedico().getApellidos();
			}else {
				fecha = "-";
				medico = "-";
			}
			modeloConsultas.addRow(new Object[] { c.getIdConsulta(), fecha, medico, c.getMotivo(), c.getDiagnostico(), c.getPrecio() });
		}
		areaDetalleRecetas.setText("");

		// Cargar citas del paciente para el médico
		if (comboCitasPaciente != null) {
			comboCitasPaciente.removeAllItems();
			citasDisponibles.clear();
			for (Cita c : gestor.getlCitas()) {
				boolean esDelPaciente = c.getPaciente().getDni().equals(pacienteActual.getDni());
				if (esDelPaciente && c.getEstado() != EstadoCita.CANCELADA) {
					citasDisponibles.add(c);
					comboCitasPaciente.addItem("#" + c.getIdCita() + " - " + c.getFechaCita().format(GestorHospital.FORMATO_FECHA)
							+ " " + c.getHora() + " (" + c.getMotivo() + ")");
				}
			}
		}
	}

	private void mostrarDetalleConsultaSeleccionada() {
		int fila = tablaConsultas.getSelectedRow();
		if (fila < 0 || pacienteActual == null) {
			return;
		}

		Consulta c = gestor.getHistorialPaciente(pacienteActual).getlConsulta().get(fila);
		String texto = "Observaciones: " + c.getObservaciones() + "\n\n";

		if (c.getlRecetas() == null || c.getlRecetas().isEmpty()) {
			texto += "Sin recetas asociadas a esta consulta.";
		} else {
			for (Receta r : c.getlRecetas()) {
				texto += "• Receta #" + r.getIdReceta() + " - Indicaciones: " + r.getIndicaciones() + "\n";
				for (Medicamento m : r.getlMedicamentos()) {
					texto += "   - " + m.getNombre() + " (" + m.getDosis() + ", " + m.getDuracionDias() + " días)\n";
				}
			}
		}

		areaDetalleRecetas.setText(texto);
	}
	
}
