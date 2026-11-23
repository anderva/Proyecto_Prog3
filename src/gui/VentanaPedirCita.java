package gui;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import domain.Cita;
import domain.GestorHospital;
import domain.enums.EstadoCita;
import domain.personas.Medico;
import domain.personas.Paciente;
import domain.personas.Usuario;

public class VentanaPedirCita extends JFrame{
	private static final long serialVersionUID = 1L;
	
	private GestorHospital gestor;
	private Cita citaModificar; // esta es la cita que se esta modificando null si la cita es una nueva
	private Runnable alGuardar; // esto se usa para actualizar la tabla al guardar la cita
	private Paciente paciente; // es el paciente de la cita null si no hay nada seleccionado
	private JComboBox<String> comboPacientes; // se usa solo si no es un paciente 
	private JComboBox<String> comboMedicos;
	private JTextField textoFecha;
	private JComboBox<String> comboHoras;
	private JTextField textoMotivo;
	
	// para el main de pruebas
		public VentanaPedirCita(GestorHospital gestor) {
			this(gestor, null, null, null);
		}
	
	public VentanaPedirCita(GestorHospital gestor, Usuario usuario, Cita citaModificar, Runnable alGuardar) {
		this.gestor = gestor;
		this.citaModificar = citaModificar;
		this.alGuardar = alGuardar;
		if (citaModificar != null) {
			this.paciente = citaModificar.getPaciente();
		} else if (usuario instanceof Paciente) {
			this.paciente = (Paciente) usuario;
		}
		boolean elegirPaciente = (paciente == null);
		
		setTitle("OsakiDeusto - Pedir cita");
		setSize(420, 430);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE); //porque es ventana secundaria
		setResizable(false);
		setLocationRelativeTo(null);
		
		JPanel panelTotal = new JPanel(new BorderLayout());
		panelTotal.setBorder(BorderFactory.createEmptyBorder(15, 25, 10, 25));
		
		//Panel Superior
		JPanel panelSuperior = new JPanel(new BorderLayout(0, 5));
		
		ImageIcon imagen = new ImageIcon("img/OsakiDeustoLogo.png");
		Image imagenEscalada = imagen.getImage().getScaledInstance(100, 59, Image.SCALE_SMOOTH);
		JLabel labelImagen = new JLabel(new ImageIcon(imagenEscalada));
		panelSuperior.add(labelImagen, BorderLayout.NORTH);
		
		JLabel labelTitulo = new JLabel("PEDIR CITA", JLabel.CENTER);
		labelTitulo.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 18));
		panelSuperior.add(labelTitulo, BorderLayout.CENTER);

		//Panel Central
		JPanel panelCentral = new JPanel(new GridLayout(3, 1, 10, 10));
		panelCentral.setBorder(BorderFactory.createEmptyBorder(15, 0, 0, 0));

		if (elegirPaciente) {
			JPanel panelPaciente = new JPanel(new GridLayout(2, 1, 5, 3));
			panelPaciente.add(new JLabel("Paciente: "));
			comboPacientes = new JComboBox<String>();
			for (Paciente p : gestor.getlPacientes()) {
				comboPacientes.addItem(p.getNombre() + " " + p.getApellidos() + " (" + p.getDni() + ")");
			}
			panelPaciente.add(comboPacientes);
			panelCentral.add(panelPaciente);
		}
		
		JPanel medico = new JPanel(new GridLayout(2, 1, 5, 3));
		JLabel labelMedico = new JLabel("Médico: ");
		medico.add(labelMedico);
		comboMedicos = new JComboBox<String>();
		for (Medico m : gestor.getlMedicos()) {
			comboMedicos.addItem(m.getNombre() + " " + m.getApellidos() + " (" + m.getEspecialidad() + ")");
		}
		medico.add(comboMedicos);
		panelCentral.add(medico);

		//Fecha y Hora
		JPanel fechaHora = new JPanel(new GridLayout(1, 2, 10, 0));

		JPanel fecha = new JPanel(new GridLayout(2, 1, 5, 3));
		JLabel labelFecha = new JLabel("Fecha (dd/mm/aaaa): ");
		fecha.add(labelFecha);
		textoFecha = new JTextField();
		fecha.add(textoFecha);
		fechaHora.add(fecha);

		JPanel hora = new JPanel(new GridLayout(2, 1, 5, 3));
		JLabel labelHora = new JLabel("Hora: ");
		hora.add(labelHora);
		comboHoras = new JComboBox<String>();
		String[] horas = { "09:00", "09:30", "10:00", "10:30", "11:00", "11:30", "12:00", "12:30", "13:00"};
		for (String h : horas) {
			comboHoras.addItem(h);
		}
		hora.add(comboHoras);
		fechaHora.add(hora);

		panelCentral.add(fechaHora);

		// Motivo de la consulta
		JPanel motivo = new JPanel(new GridLayout(2, 1, 5, 3));
		JLabel labelMotivo = new JLabel("Motivo de la consulta: ");
		motivo.add(labelMotivo);
		textoMotivo = new JTextField();
		motivo.add(textoMotivo);
		panelCentral.add(motivo);
		
		// cargar los datos actuales si se esta modificando
		if (citaModificar != null) {
			comboMedicos.setSelectedIndex(gestor.getlMedicos().indexOf(citaModificar.getMedico()));
			textoFecha.setText(citaModificar.getFechaCita().format(GestorHospital.FORMATO_FECHA));
			comboHoras.setSelectedItem(citaModificar.getHora());
			textoMotivo.setText(citaModificar.getMotivo());
		}
		
		
		// cada vez que cambia el medico, el paciente o la fecha se vuelven a calcular las horas
		comboMedicos.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				actualizarHoras();
			}
		});
		if (comboPacientes != null) {
			comboPacientes.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					actualizarHoras();
				}
			});
		}
		textoFecha.getDocument().addDocumentListener(new DocumentListener() {

			@Override
			public void insertUpdate(DocumentEvent e) {
				actualizarHoras(); // se ha escrito algo
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				actualizarHoras(); // se ha borrado algo
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				// solo salta con cambios de estilo
			}
		});
		actualizarHoras(); // por si se esta modificando una cita y ya hay fecha

		// Panel Inferior
		JPanel panelInferior = new JPanel(new GridLayout(1, 2, 10, 10));
		panelInferior.setBorder(BorderFactory.createEmptyBorder(20, 0, 5, 0));
		JButton botonCancelar = new JButton("Cancelar");
		panelInferior.add(botonCancelar);
		botonCancelar.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				VentanaPedirCita.this.dispose();
			}
		});
		JButton botonConfirmar = new JButton("Confirmar");
		panelInferior.add(botonConfirmar);
		botonConfirmar.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				guardarCita();
			}
		});

		panelTotal.add(panelSuperior, BorderLayout.NORTH);
		panelTotal.add(panelCentral, BorderLayout.CENTER);
		panelTotal.add(panelInferior, BorderLayout.SOUTH);

		this.add(panelTotal);

		setVisible(true);
	}
	
	
	// Rellena el combo de horas solo con las horas libres para ese medico y ese paciente en esa fecha
	private void actualizarHoras() {
		String horaElegida = (String) comboHoras.getSelectedItem();
		comboHoras.removeAllItems();

		LocalDate fecha = GestorHospital.parsearFecha(textoFecha.getText().trim());
		if (fecha == null || comboMedicos.getSelectedIndex() == -1) {
			// si todavia no hay una hora valida, se ponen todas las horas
			for (String h : GestorHospital.HORAS_CITA) {
				comboHoras.addItem(h);
			}
			comboHoras.setSelectedItem(horaElegida);
			return;
		}

		Medico medico = gestor.getlMedicos().get(comboMedicos.getSelectedIndex());
		Paciente pacienteCita = paciente;
		if (pacienteCita == null && comboPacientes != null && comboPacientes.getSelectedIndex() != -1) {
			pacienteCita = gestor.getlPacientes().get(comboPacientes.getSelectedIndex());
		}

		// horas libres de ese medico y ese paciente ese dia
		List<String> libres = gestor.getHorasLibres(medico, pacienteCita, fecha, citaModificar);
		for (String h : libres) {
			comboHoras.addItem(h);
		}
		comboHoras.setSelectedItem(horaElegida); // si sigue libre, se vuelve a seleccionar
	}
	
	
	
	// guarda los datos en la BD despues de comprobarlos
	private void guardarCita() {
		if (gestor.getlMedicos().isEmpty()) {
			mostrarError("No hay médicos registrados");
			return;
		}
		Medico medico = gestor.getlMedicos().get(comboMedicos.getSelectedIndex());

		Paciente pacienteCita = paciente;
		if (pacienteCita == null) {
			if (gestor.getlPacientes().isEmpty()) {
				mostrarError("No hay pacientes registrados");
				return;
			}
			pacienteCita = gestor.getlPacientes().get(comboPacientes.getSelectedIndex());
		}

		// fecha que tenga el formato y no este pasada
		LocalDate fecha = GestorHospital.parsearFecha(textoFecha.getText().trim());
		if (fecha == null) {
			mostrarError("La fecha no es válida. Usa el formato dd/mm/aaaa (por ejemplo 25/12/2026)");
			return;
		}
		if (fecha.isBefore(LocalDate.now())) {
			mostrarError("No se puede pedir una cita en una fecha pasada");
			return;
		}

		// el medico y el paciente tienen que estar libres a esa hora
		String hora = (String) comboHoras.getSelectedItem();
		List<String> horasLibres = gestor.getHorasLibres(medico, pacienteCita, fecha, citaModificar);
		if (!horasLibres.contains(hora)) {
			String libres = horasLibres.isEmpty() ? "ninguna" : String.join(", ", horasLibres);
			mostrarError("Esa hora no está disponible (el médico o el paciente ya tienen una cita, "
					+ "o la hora ya ha pasado).\nHoras libres ese día: " + libres);
			return;
		}

		String motivo = textoMotivo.getText().trim();
		boolean guardada;
		if (citaModificar == null) {
			guardada = gestor.addCita(new Cita(0, pacienteCita, medico, fecha, EstadoCita.PENDIENTE, hora, motivo));
		} else {
			guardada = gestor.modificarCita(citaModificar, medico, fecha, hora, motivo);
		}
		if (!guardada) {
			mostrarError("No se ha podido guardar la cita en la base de datos");
			return;
		}

		if (alGuardar != null) {
			alGuardar.run();
		}
		dispose();
	}

	private void mostrarError(String mensaje) {
		JOptionPane.showMessageDialog(this, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
	}

	
	public static void main(String[] args) {
		GestorHospital gestor = new GestorHospital();
		new VentanaPedirCita(gestor);
	}

}
