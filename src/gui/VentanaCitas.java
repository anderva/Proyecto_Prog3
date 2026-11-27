package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;

import domain.Cita;
import domain.GestorHospital;
import domain.enums.EstadoCita;
import domain.enums.Sexo;
import domain.personas.Medico;
import domain.personas.Paciente;
import domain.personas.Usuario;

public class VentanaCitas extends JFrame {

	private static final long serialVersionUID = 1L;

	private GestorHospital gestor;
	private Usuario usuario; // usuario que ha iniciado sesion (null si se abre desde el main de pruebas)

	private ModeloCitas modeloCitas;

	// componentes que van a usar los listeners
	private JComboBox<String> comboMedicos;
	private JComboBox<String> comboEstados;
	private JLabel labelNumCitas; // cuantas citas se estan viendo con los filtros puestos
	private JTable tablaCitas;
	private JButton botonNuevaCita;
	private JButton botonModificar;
	private JButton botonCancelarCita;
	private JButton botonRealizada;
	private JButton botonBorrar;

	public VentanaCitas(GestorHospital gestor, Usuario usuario) {
		setTitle("OsakiDeusto - Citas");
		setSize(1000, 620);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE); // porque es ventana secundaria
		setLocationRelativeTo(null); // centrar en la pantalla

		this.gestor = gestor;
		this.usuario = usuario;

		JPanel panelTotal = new JPanel(new BorderLayout(0, 10));
		panelTotal.setBorder(BorderFactory.createEmptyBorder(10, 20, 5, 20));

		//panel superior: logo, titulo, usuario y filtros 
		JPanel panelSuperior = new JPanel(new BorderLayout(0, 10));

		JPanel panelCabecera = new JPanel(new BorderLayout(15, 0));
		ImageIcon logo = new ImageIcon("resources/img/OsakiDeustoLogo.png");
		Image logoEscalado = logo.getImage().getScaledInstance(76, 45, Image.SCALE_SMOOTH);
		panelCabecera.add(new JLabel(new ImageIcon(logoEscalado)), BorderLayout.WEST);

		JPanel panelTitulo = new JPanel(new GridLayout(2, 1));
		JLabel labelTitulo = new JLabel("GESTIÓN DE CITAS");
		labelTitulo.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 20));
		labelTitulo.setForeground(RendererCitas.AZUL_OSAKI);
		panelTitulo.add(labelTitulo);
		JLabel labelUsuario = new JLabel(textoUsuario());
		labelUsuario.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 13));
		panelTitulo.add(labelUsuario);
		panelCabecera.add(panelTitulo, BorderLayout.CENTER);

		panelSuperior.add(panelCabecera, BorderLayout.NORTH);

		// filtros
		JPanel panelFiltro = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));

		panelFiltro.add(new JLabel("Médico: "));
		comboMedicos = new JComboBox<String>();
		comboMedicos.addItem("Todos");
		for (Medico m : gestor.getlMedicos()) {
			comboMedicos.addItem(m.getNombre() + " " + m.getApellidos() + " ("
					+ RendererCitas.textoEspecialidad(m.getEspecialidad()) + ")");
		}
		// si entra un medico, al abrir la ventana se ven directamente sus citas
		if (usuario instanceof Medico) {
			int posicion = gestor.getlMedicos().indexOf(usuario);
			if (posicion != -1) {
				comboMedicos.setSelectedIndex(posicion + 1); // +1 porque el primero es "Todos"
			}
		}
		panelFiltro.add(comboMedicos);
		panelFiltro.add(new JLabel("   Estado: "));
		
		comboEstados = new JComboBox<String>();
		comboEstados.addItem("Todos");
		for (EstadoCita estado : EstadoCita.values()) {
			comboEstados.addItem(RendererCitas.textoEstado(estado));
		}
		panelFiltro.add(comboEstados);

		labelNumCitas = new JLabel();
		labelNumCitas.setFont(new Font(Font.SANS_SERIF, Font.ITALIC, 12));
		panelFiltro.add(labelNumCitas);

		// cada vez que se cambia un filtro se vuelve a rellenar la tabla
		comboMedicos.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				actualizarTabla();
			}
		});
		comboEstados.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				actualizarTabla();
			}
		});
		
		panelSuperior.add(panelFiltro, BorderLayout.CENTER);

		//panel central: tabla de citas
		modeloCitas = new ModeloCitas(new ArrayList<Cita>());
		tablaCitas = new JTable(modeloCitas);
		configurarTabla();
		JScrollPane scrollTabla = new JScrollPane(tablaCitas);

		//panel de abajo: botones
		JPanel panelSur = new JPanel(new BorderLayout(0, 5));

		// GridLayout(1, 0) -> una fila y tantas columnas como botones haya
		JPanel panelBotones = new JPanel(new GridLayout(1, 0, 10, 10));
		panelBotones.setBorder(BorderFactory.createEmptyBorder(5, 0, 0, 0));
		botonNuevaCita = new JButton("Nueva cita", RendererCitas.cargarIcono("boton_nueva"));
		botonNuevaCita.setToolTipText("Pedir una cita nueva");
		panelBotones.add(botonNuevaCita);
		botonModificar = new JButton("Modificar", RendererCitas.cargarIcono("boton_modificar"));
		botonModificar.setToolTipText("Cambiar médico, fecha, hora o motivo de la cita seleccionada");
		panelBotones.add(botonModificar);
		botonCancelarCita = new JButton("Cancelar cita", RendererCitas.cargarIcono("boton_cancelar"));
		botonCancelarCita.setToolTipText("Cancelar la cita seleccionada");
		panelBotones.add(botonCancelarCita);
		botonRealizada = new JButton("Marcar realizada", RendererCitas.cargarIcono("boton_realizada"));
		botonRealizada.setToolTipText("Solo se pueden marcar las citas de hoy o de días anteriores");
		panelBotones.add(botonRealizada);
		botonBorrar = new JButton("Borrar cita");
		botonBorrar.setToolTipText("Eliminar la cita seleccionada");
		panelBotones.add(botonBorrar);
		panelSur.add(panelBotones, BorderLayout.NORTH);

		//listeners 

		botonNuevaCita.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				new VentanaPedirCita(gestor, usuario, null, () -> actualizarTabla());
			}
		});
		
		botonModificar.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				Cita cita = citaParaGestionar();
				if (cita == null) {
					return;
				}
				if (cita.getEstado() != EstadoCita.PENDIENTE) {
					avisar("Solo se pueden modificar las citas pendientes");
					return;
				}
				new VentanaPedirCita(gestor, usuario, cita, () -> actualizarTabla());
			}
		});
		
		botonCancelarCita.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				Cita cita = citaParaGestionar();
				if (cita == null) {
					return;
				}
				if (cita.getEstado() != EstadoCita.PENDIENTE) {
					avisar("Solo se pueden cancelar las citas pendientes");
					return;
				}
				if (confirmar("¿Seguro que quieres cancelar esta cita?")) {
					if (!gestor.cancelarCita(cita)) {
						avisar("No se ha podido guardar el cambio en la base de datos");
					}
					actualizarTabla();
				}
			}
		});
		
		botonRealizada.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				Cita cita = citaParaGestionar();
				if (cita == null) {
					return;
				}
				if (cita.getEstado() != EstadoCita.PENDIENTE) {
					avisar("Solo se pueden marcar como realizadas las citas pendientes");
					return;
				}
				if (cita.getFechaCita().isAfter(LocalDate.now())) {
					avisar("Solo se pueden marcar como realizadas las citas de hoy o de días anteriores");
					return;
				}
				if (!gestor.marcarRealizada(cita)) {
					avisar("No se ha podido guardar el cambio en la base de datos");
				}
				actualizarTabla();
			}
		});
		
		botonBorrar.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				Cita cita = citaParaGestionar();
				if (cita == null) {
					return;
				}
				if (confirmar("¿Seguro que quieres borrar esta cita? No se puede deshacer.")) {
					if (!gestor.borrarCita(cita)) {
						avisar("No se ha podido borrar la cita de la base de datos");
					}
					actualizarTabla();
				}
			}
		});

		panelTotal.add(panelSuperior, BorderLayout.NORTH);
		panelTotal.add(scrollTabla, BorderLayout.CENTER);
		panelTotal.add(panelSur, BorderLayout.SOUTH);

		this.add(panelTotal);

		// se cargan las citas en la tabla
		actualizarTabla();

		setVisible(true);
	}

	// configuracion visual de la tabla: renderers, tamaños de columnas, cabecera...
	private void configurarTabla() {
		tablaCitas.setRowHeight(28);
		tablaCitas.setFillsViewportHeight(true); // fondo blanco aunque haya pocas filas
		tablaCitas.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); // solo una cita a la vez
		tablaCitas.getTableHeader().setReorderingAllowed(false); // no se pueden mover las columnas

		// el mismo renderer para todas las columnas
		RendererCitas renderer = new RendererCitas();
		for (int i = 0; i < tablaCitas.getColumnCount(); i++) {
			tablaCitas.getColumnModel().getColumn(i).setCellRenderer(renderer);
		}

		// anchos de las columnas
		tablaCitas.getColumnModel().getColumn(0).setMaxWidth(55); // ID
		tablaCitas.getColumnModel().getColumn(1).setPreferredWidth(170); // Fecha
		tablaCitas.getColumnModel().getColumn(2).setMaxWidth(70); // Hora
		tablaCitas.getColumnModel().getColumn(3).setPreferredWidth(160); // Paciente
		tablaCitas.getColumnModel().getColumn(4).setPreferredWidth(170); // Medico
		tablaCitas.getColumnModel().getColumn(5).setPreferredWidth(150); // Especialidad
		tablaCitas.getColumnModel().getColumn(6).setPreferredWidth(110); // Estado

		// renderer de la cabecera con los colores del hospital
		tablaCitas.getTableHeader().setDefaultRenderer((table, value, isSelected, hasFocus, row, column) -> {
			JLabel label = new JLabel(value.toString());
			label.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 13));
			label.setForeground(Color.WHITE);
			label.setBackground(RendererCitas.AZUL_OSAKI);
			label.setHorizontalAlignment(SwingConstants.CENTER);
			label.setOpaque(true); // para que se vea el color de fondo
			label.setBorder(BorderFactory.createLineBorder(Color.WHITE)); // borde entre columnas
			return label;
		});
		
	}

	// texto de la cabecera con el usuario que ha entrado
	private String textoUsuario() {
		if (usuario instanceof Medico) {
			Medico m = (Medico) usuario;
			String titulo = "Dr. ";
			if (m.getSexo() == Sexo.MUJER) {
				titulo = "Dra. ";
			}
			return "Médico: " + titulo + m.getNombre() + " " + m.getApellidos() + "  ·  "
					+ RendererCitas.textoEspecialidad(m.getEspecialidad());
		} else if (usuario instanceof Paciente) {
			return "Paciente: " + usuario.getNombre() + " " + usuario.getApellidos();
		} else if (usuario != null) {
			return "Usuario: " + usuario.getNombre() + " " + usuario.getApellidos();
		}
		return "Modo de administrador";
	}

	//tabla

	// vuelve a rellenar la tabla con las citas que cumplen los filtros, ordenadas por fecha y hora
	private void actualizarTabla() {
		Cita seleccionada = getCitaSeleccionada(); // para volver a seleccionarla despues
		List<Cita> citas = new ArrayList<Cita>(gestor.getlCitas());
		if (usuario instanceof Paciente) {
			Paciente paciente = (Paciente) usuario;
			citas.removeIf(cita -> !cita.getPaciente().getDni().equals(paciente.getDni()));
		}
		int totalCitas = citas.size(); // citas que puede ver este usuario sin filtros

		// filtro por medico (la posicion 0 es "Todos")
		int posMedico = comboMedicos.getSelectedIndex();
		if (posMedico > 0) {
			Medico medico = gestor.getlMedicos().get(posMedico - 1);
			citas.removeIf(cita -> cita.getMedico() != medico);
		}

		// filtro por estado (la posicion 0 es "Todos", el resto va en el orden del enum)
		int posEstado = comboEstados.getSelectedIndex();
		if (posEstado > 0) {
			EstadoCita estado = EstadoCita.values()[posEstado - 1];
			citas.removeIf(cita -> cita.getEstado() != estado);
		}
		
		// ordenar por fecha y, si es el mismo dia, por hora
		Collections.sort(citas, (c1, c2) -> {
			int resultado = c1.getFechaCita().compareTo(c2.getFechaCita());
			if (resultado == 0) {
				resultado = c1.getHora().compareTo(c2.getHora());
			}
			return resultado;
		});

		modeloCitas.actualizarCitas(citas);
		seleccionarCita(seleccionada);

		if (citas.size() == totalCitas) {
			labelNumCitas.setText("   " + totalCitas + " citas");
		} else {
			labelNumCitas.setText("   Mostrando " + citas.size() + " de " + totalCitas + " citas");
		}
	}

	// cita de la fila seleccionada o null si no hay ninguna
	private Cita getCitaSeleccionada() {
		int fila = tablaCitas.getSelectedRow();
		if (fila == -1) {
			return null;
		}
		return modeloCitas.getCita(fila);
	}

	// selecciona en la tabla la fila de esa cita (si se ve en la tabla)
	private void seleccionarCita(Cita cita) {
		for (int i = 0; i < modeloCitas.getRowCount(); i++) {
			if (modeloCitas.getCita(i) == cita) {
				tablaCitas.setRowSelectionInterval(i, i);
				return;
			}
		}
	}
	
	//botones
	
	private Cita citaParaGestionar() {
		Cita cita = getCitaSeleccionada();
		if (cita == null) {
			avisar("Selecciona primero una cita de la tabla");
			return null;
		}
		if (!puedeGestionar(cita)) {
			avisar("Solo puedes gestionar tus propias citas");
			return null;
		}
		return cita;
	}
	
	private boolean puedeGestionar(Cita cita) {
		if (usuario instanceof Paciente) {
			return cita.getPaciente() == usuario;
		} else if (usuario instanceof Medico) {
			return cita.getMedico() == usuario;
		}
		return true;
	}
	
	private void avisar(String mensaje) {
		JOptionPane.showMessageDialog(this, mensaje);
	}
	
	private boolean confirmar(String mensaje) {
		return JOptionPane.showConfirmDialog(this, mensaje, "Confirmar", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION;
	}

	public static void main(String[] args) {
		GestorHospital gestor = new GestorHospital();
		new VentanaCitas(gestor, null);
	}
}