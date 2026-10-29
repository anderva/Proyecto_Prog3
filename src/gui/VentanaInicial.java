package gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import domain.personas.*;
import domain.*;

public class VentanaInicial extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel pInicioSur, pInicioCentro, pInicioNorte;
	private JButton bInicio, bRegistrarUsuario;
	private JLabel lTitulo, lEmail, lContraseña, lIconoPrincipal;
	private JTextField txtEmail;
	private JPasswordField txtContraseña;

	public VentanaInicial(GestorHospital gestor) {
		
		setTitle("OsakiDeusto - Inicio");
		setSize(420, 430);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setResizable(false);
		setLocationRelativeTo(null); // Centrar en la pantalla
		// Panel principal
		JPanel pPrincipal = new JPanel(new BorderLayout());
		pPrincipal.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));
		// Creación de paneles
		pInicioCentro = new JPanel(new GridLayout(5, 1));
		pInicioCentro.setBorder(BorderFactory.createEmptyBorder(25, 40, 25, 40));
		pInicioSur = new JPanel();
		pInicioNorte = new JPanel(new FlowLayout(FlowLayout.CENTER));

		// Creación de componentes
		lIconoPrincipal = new JLabel();
		ImageIcon im = new ImageIcon("img/OsakiDeustoLogo.png");
		lIconoPrincipal.setIcon(im);
		lIconoPrincipal.setHorizontalAlignment(JLabel.CENTER);

		lTitulo = new JLabel("Inicio Sesion");
		lTitulo.setHorizontalAlignment(JLabel.CENTER);
		lTitulo.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 22));

		lEmail = new JLabel("Email");
		lEmail.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 13));
		lEmail.setHorizontalAlignment(JLabel.LEFT);

		txtEmail = new JTextField();
		txtEmail.setHorizontalAlignment(JTextField.LEFT);

		lContraseña = new JLabel("Contraseña");
		lContraseña.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 13));
		lContraseña.setHorizontalAlignment(JLabel.LEFT);

		txtContraseña = new JPasswordField();
		txtContraseña.setHorizontalAlignment(JTextField.LEFT);

		bInicio = new JButton("Iniciar");
		
		bInicio.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				String email = txtEmail.getText();
				String contrasenia = String.valueOf(txtContraseña.getPassword());
				boolean haIniciado = false;
				// Checkear si usuario existe y contraseña
				if(!gestor.getlUsuarios().isEmpty()) {
					for(Usuario u: gestor.getlUsuarios()) {
						if (email.equals(u.getEmail()) && contrasenia.equals(u.getContrasenia())) {
							haIniciado = true;
							VentanaInicial.this.dispose();
							// Pasar a la siguiente ventana
						}
					}
				}
				if(!haIniciado) {
					JOptionPane.showMessageDialog(null, "Nombre de usuario y/o contraseña incorrectos");
				}
				
			}});
		
		bRegistrarUsuario = new JButton("Registrarse");
		
		bRegistrarUsuario.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				VentanaRegistro ventanaRegistro = new VentanaRegistro(gestor);
			}
		});

		setVisible(true);

		// Colocación de componentes en los paneles
		pInicioNorte.add(lIconoPrincipal);
		pInicioCentro.add(lTitulo);
		pInicioCentro.add(lEmail);
		pInicioCentro.add(txtEmail);
		pInicioCentro.add(lContraseña);
		pInicioCentro.add(txtContraseña);
		pInicioSur.add(bInicio);
		pInicioSur.add(bRegistrarUsuario);
		
		pPrincipal.add(pInicioCentro, BorderLayout.CENTER);
		pPrincipal.add(pInicioSur, BorderLayout.SOUTH);
		pPrincipal.add(pInicioNorte, BorderLayout.NORTH);
		this.add(pPrincipal);
		
		

	}

	public static void main(String[] args) {
		GestorHospital gestor = new GestorHospital();
		new VentanaInicial(gestor);
	}
}
