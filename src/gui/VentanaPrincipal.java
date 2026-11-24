package gui;


import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import domain.GestorHospital;
import domain.enums.Sexo;
import domain.enums.TipoUsuario;
import domain.personas.Medico;
import domain.personas.Paciente;
import domain.personas.Usuario;

public class VentanaPrincipal extends JFrame {

	private static final long serialVersionUID = 1L;

	private GestorHospital gestor;
	private Usuario usuario;
	private TipoUsuario tipoUsuario;

	public VentanaPrincipal(GestorHospital gestor, Usuario usuario) {
		this.gestor = gestor;
		this.usuario = usuario;
		this.tipoUsuario = GestorHospital.obtenerTipoUsuario(usuario);

		setTitle("OsakiDeusto - Panel Principal");
		setSize(620, 440);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setResizable(false);
		setLocationRelativeTo(null);

		JPanel panelPrincipal = new JPanel(new BorderLayout(15, 15));
		panelPrincipal.setBorder(BorderFactory.createEmptyBorder(20, 25, 20, 25));

		// Cabecera con logo y bienvenida según el rol
		JPanel panelNorte = new JPanel(new BorderLayout(15, 5));
		ImageIcon logo = new ImageIcon("resources/img/OsakiDeustoLogo.png");
		Image logoEscalado = logo.getImage().getScaledInstance(85, 50, Image.SCALE_SMOOTH);
		panelNorte.add(new JLabel(new ImageIcon(logoEscalado)), BorderLayout.WEST);

		JPanel panelBienvenida = new JPanel(new GridLayout(2, 1));
		if(tipoUsuario == TipoUsuario.MEDICO) {
			JLabel lblRol = new JLabel("PORTAL DEL MÉDICO");
			lblRol.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 19));
			lblRol.setForeground(Color.blue);
			panelBienvenida.add(lblRol);
		} else {
			JLabel lblRol = new JLabel("PORTAL DEL PACIENTE");
			lblRol.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 19));
			lblRol.setForeground(Color.blue);
			panelBienvenida.add(lblRol);
		}		

		JLabel lblNombre = new JLabel(obtenerSaludo());
		lblNombre.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 14));

		
		panelBienvenida.add(lblNombre);
		panelNorte.add(panelBienvenida, BorderLayout.CENTER);

		panelPrincipal.add(panelNorte, BorderLayout.NORTH);

		// Panel central con accesos exclusivos según el tipo de usuario
		if (tipoUsuario == TipoUsuario.PACIENTE) {
			panelPrincipal.add(crearPanelPaciente((Paciente) usuario), BorderLayout.CENTER);
		} else {
			panelPrincipal.add(crearPanelMedico(), BorderLayout.CENTER);
		}

		// Panel inferior: cerrar sesión y volver a VentanaInicial
		JPanel panelSur = new JPanel(new BorderLayout());
		JButton btnCerrarSesion = new JButton("Cerrar sesión");
		btnCerrarSesion.addActionListener(e -> {
			dispose();
			new VentanaInicial(gestor);
		});
		panelSur.add(btnCerrarSesion, BorderLayout.EAST);
		panelPrincipal.add(panelSur, BorderLayout.SOUTH);

		add(panelPrincipal);
		setVisible(true);
	}

	private JPanel crearPanelPaciente(Paciente paciente) {
		JPanel contenedor = new JPanel(new BorderLayout(10, 12));
		JPanel gridBotones = new JPanel(new GridLayout(3, 1, 10, 10));
		Font fuenteBoton = new Font(Font.SANS_SERIF, Font.BOLD, 14);

		JButton btnCitas = new JButton("Mis Citas Médicas", RendererCitas.cargarIcono("pendiente"));
		btnCitas.setFont(fuenteBoton);
		btnCitas.setToolTipText("Consultar tus citas, pedir una nueva, modificarla o cancelarla");
		btnCitas.addActionListener(e -> new VentanaCitas(gestor, paciente));

		JButton btnHistorial = new JButton("Mi Historial Clínico y Recetas", RendererCitas.cargarIcono("medicina_general"));
		btnHistorial.setFont(fuenteBoton);
		btnHistorial.setToolTipText("Ver diagnósticos, medicamentos recetados y alergias");
		//btnHistorial.addActionListener(e -> new VentanaAreaClinica(gestor, paciente));

		JButton btnFacturas = new JButton("Mis Facturas y Pagos", RendererCitas.cargarIcono("realizada"));
		btnFacturas.setFont(fuenteBoton);
		btnFacturas.setToolTipText("Consultar facturas emitidas y pagar las pendientes");
		//btnFacturas.addActionListener(e -> new VentanaFacturas(gestor, paciente));

		gridBotones.add(btnCitas);
		gridBotones.add(btnHistorial);
		gridBotones.add(btnFacturas);
		contenedor.add(gridBotones, BorderLayout.CENTER);

//		if (paciente.getHabitacion() != null) {
//			JLabel lblIngreso = new JLabel("Ingreso activo: Habitación #" + paciente.getHabitacion().getNumHabitacion()
//					+ " (" + paciente.getHabitacion().getTipoHabitacion() + ")", SwingConstants.CENTER);
//			lblIngreso.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 12));
//			lblIngreso.setBorder(BorderFactory.createTitledBorder("Estado Hospitalario"));
//			contenedor.add(lblIngreso, BorderLayout.SOUTH);
//		}

		return contenedor;
	}

	private JPanel crearPanelMedico() {
		JPanel gridBotones = new JPanel(new GridLayout(2, 2, 12, 12));
		Font fuenteBoton = new Font(Font.SANS_SERIF, Font.BOLD, 14);

		JButton btnCitas = new JButton("Agenda de Citas", RendererCitas.cargarIcono("pendiente"));
		btnCitas.setFont(fuenteBoton);
		btnCitas.setToolTipText("Ver citas, filtrar por médico y marcar consultas realizadas");
		btnCitas.addActionListener(e -> new VentanaCitas(gestor, usuario));

		JButton btnClinica = new JButton("Pasar Consulta e Historial", RendererCitas.cargarIcono("medico"));
		btnClinica.setFont(fuenteBoton);
		btnClinica.setToolTipText("Registrar diagnósticos, emitir recetas y editar fichas clínicas");
		//btnClinica.addActionListener(e -> new VentanaAreaClinica(gestor, usuario));

		JButton btnHorariosHab = new JButton("Horarios y Habitaciones", RendererCitas.cargarIcono("paciente"));
		btnHorariosHab.setFont(fuenteBoton);
		btnHorariosHab.setToolTipText("Configurar días de consulta, ingresar pacientes y dar altas");
		//btnHorariosHab.addActionListener(e -> new VentanaHorariosHabitaciones(gestor, usuario));

		JButton btnFacturas = new JButton("Facturación", RendererCitas.cargarIcono("realizada"));
		btnFacturas.setFont(fuenteBoton);
		btnFacturas.setToolTipText("Consultar, emitir o anular facturas de pacientes");
		//btnFacturas.addActionListener(e -> new VentanaFacturas(gestor, usuario));

		gridBotones.add(btnCitas);
		gridBotones.add(btnClinica);
		gridBotones.add(btnHorariosHab);
		gridBotones.add(btnFacturas);

		return gridBotones;
	}

	private String obtenerSaludo() {
		if (usuario instanceof Medico) {
			Medico m = (Medico) usuario;
			String prefijo = (m.getSexo() == Sexo.MUJER) ? "Dra. " : "Dr. ";
			return prefijo + m.getNombre() + " " + m.getApellidos() + " · "
					+ RendererCitas.textoEspecialidad(m.getEspecialidad());
		}
		return usuario.getNombre() + " " + usuario.getApellidos() + " (DNI: " + usuario.getDni() + ")";
	}
}
