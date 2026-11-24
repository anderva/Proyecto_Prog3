package gui;

import java.awt.BorderLayout;
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

        // =========================
        // PANEL SUPERIOR
        // =========================
        JPanel panelSuperior = new JPanel(new BorderLayout(0, 10));

        JLabel titulo = new JLabel(
                "PANEL DE ADMINISTRADOR",
                JLabel.CENTER);
        titulo.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 22));

        panelSuperior.add(titulo, BorderLayout.NORTH);

        try {
            ImageIcon icono =
                    new ImageIcon(getClass().getResource("/images/admin.png"));

            Image imagenEscalada =
                    icono.getImage().getScaledInstance(
                            100,
                            100,
                            Image.SCALE_SMOOTH);

            JLabel imagen =
                    new JLabel(new ImageIcon(imagenEscalada));

            imagen.setHorizontalAlignment(JLabel.CENTER);

            panelSuperior.add(imagen, BorderLayout.CENTER);

        } catch (Exception e) {
            // Si no existe la imagen no pasa nada
        }

        // =========================
        // PANEL CENTRAL
        // =========================
        JPanel panelCentral = new JPanel(new GridLayout(2, 2, 20, 20));

        JButton btnPacientes = new JButton("Gestionar Pacientes");
        JButton btnMedicos = new JButton("Gestionar Médicos");
        JButton btnCitas = new JButton("Gestionar Citas");
        JButton btnEstadisticas = new JButton("Ver Estadísticas");

        btnPacientes.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 14));
        btnMedicos.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 14));
        btnCitas.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 14));
        btnEstadisticas.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 14));

        panelCentral.add(btnPacientes);
        panelCentral.add(btnMedicos);
        panelCentral.add(btnCitas);
        panelCentral.add(btnEstadisticas);

        // =========================
        // PANEL INFERIOR
        // =========================
        JPanel panelInferior = new JPanel();

        JButton btnCerrarSesion = new JButton("Cerrar Sesión");

        panelInferior.add(btnCerrarSesion);

        // =========================
        // ENSAMBLAJE
        // =========================
        panelPrincipal.add(panelSuperior, BorderLayout.NORTH);
        panelPrincipal.add(panelCentral, BorderLayout.CENTER);
        panelPrincipal.add(panelInferior, BorderLayout.SOUTH);

        add(panelPrincipal);

        setVisible(true);
	}
}
