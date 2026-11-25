package gui;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import domain.GestorHospital;

public class VentanaAdmin extends JFrame{

	private static final long serialVersionUID = 1L;
	public static void main(String[] args) {
		GestorHospital gestor = new GestorHospital();
		new VentanaAdmin(gestor);
	}
	public VentanaAdmin(GestorHospital gestor) {
		// Configuración de la ventana
        setTitle("Panel de Administración");
        setSize(600, 450);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel panelPrincipal = new JPanel(new BorderLayout(10, 10));
        panelPrincipal.setBorder(
                BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Panel superior
        JPanel panelSuperior = new JPanel(new BorderLayout(0, 10));

        JLabel titulo = new JLabel(
                "PANEL DE ADMINISTRADOR",
                JLabel.CENTER);
        titulo.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 22));

        panelSuperior.add(titulo, BorderLayout.NORTH);

        ImageIcon icono = new ImageIcon("resources/img/admin.png");

        Image imagenEscalada = icono.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);

        JLabel imagen = new JLabel(new ImageIcon(imagenEscalada));
        imagen.setHorizontalAlignment(JLabel.CENTER);
        panelSuperior.add(imagen, BorderLayout.CENTER);


        // Panel central
        JPanel panelCentral = new JPanel(new GridLayout(2, 2, 20, 20));

        ImageIcon iconoPaciente = new ImageIcon("resources/img/paciente.png");
        Image imagenEscaladaPac = iconoPaciente.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
        
        JButton btnPacientes = new JButton("Gestionar Pacientes");
        btnPacientes.setIcon(new ImageIcon(imagenEscaladaPac));
        btnPacientes.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				new VentanaPacientes(gestor);
			}
		});
        
        ImageIcon iconoMedico = new ImageIcon("resources/img/medico.png");
        Image imagenEscaladaMed = iconoMedico.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
        JButton btnMedicos = new JButton("Gestionar Médicos");
        btnMedicos.setIcon(new ImageIcon(imagenEscaladaMed));
        
        ImageIcon iconoCita = new ImageIcon("resources/img/cita.png");
        Image imagenEscaladaCit = iconoCita.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
        JButton btnCitas = new JButton("Gestionar Citas");
        btnCitas.setIcon(new ImageIcon(imagenEscaladaCit));
        btnCitas.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				new VentanaCitas(gestor, null);
			}
		});
        
        
        ImageIcon iconoEstadistica = new ImageIcon("resources/img/estadisticas.png");
        Image imagenEscaladaEst = iconoEstadistica.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
        JButton btnEstadisticas = new JButton("Ver Estadísticas");
        btnEstadisticas.setIcon(new ImageIcon(imagenEscaladaEst));

        btnPacientes.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 14));
        btnMedicos.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 14));
        btnCitas.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 14));
        btnEstadisticas.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 14));

        panelCentral.add(btnPacientes);
        panelCentral.add(btnMedicos);
        panelCentral.add(btnCitas);
        panelCentral.add(btnEstadisticas);

        // Panel inferior
        JPanel panelInferior = new JPanel();

        JButton btnCerrarSesion = new JButton("Cerrar Sesión");
        btnCerrarSesion.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				VentanaAdmin.this.dispose();
				new VentanaInicial(gestor);
			}
		});

        panelInferior.add(btnCerrarSesion);


        panelPrincipal.add(panelSuperior, BorderLayout.NORTH);
        panelPrincipal.add(panelCentral, BorderLayout.CENTER);
        panelPrincipal.add(panelInferior, BorderLayout.SOUTH);

        add(panelPrincipal);

        setVisible(true);
	}
}
