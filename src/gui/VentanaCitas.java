package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;

import domain.Cita;
import domain.GestorHospital;
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
	private JTable tablaCitas;
	private JButton botonNuevaCita;
	private JButton botonModificar;
	private JButton botonCancelarCita;
	private JButton botonRealizada;

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
		ImageIcon logo = new ImageIcon("img/OsakiDeustoLogo.png");
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
		panelFiltro.add(comboMedicos);

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
		panelSur.add(panelBotones, BorderLayout.NORTH);

		//listeners 

		botonNuevaCita.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				new VentanaPedirCita(gestor);
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
		return "Modo de prueba (sin iniciar sesión)";
	}

	//tabla

	// vuelve a rellenar la tabla con todas las citas, ordenadas por fecha y hora
	private void actualizarTabla() {
		Cita seleccionada = getCitaSeleccionada(); // para volver a seleccionarla despues
		List<Cita> citas = new ArrayList<Cita>(gestor.getlCitas());

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

	public static void main(String[] args) {
		GestorHospital gestor = new GestorHospital();
		new VentanaCitas(gestor, null);
	}
}