package gui;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class VentanaInicial extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel pInicioSur, pInicioCentro, pInicioNorte;
	private JButton bInicio;
	private JLabel lTitulo, lUsuario, lContraseña, lIconoPrincipal;
	private JTextField txtUsuario;
	private JPasswordField txtContraseña;

	public VentanaInicial() {
		setTitle("OsakiDeusto - Inicio");
		setSize(420, 430);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setResizable(false);
		setLocationRelativeTo(null); // Centrar en la pantalla

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

		lUsuario = new JLabel("Usuario");
		lUsuario.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 13));
		lUsuario.setHorizontalAlignment(JLabel.LEFT);

		txtUsuario = new JTextField();
		txtUsuario.setHorizontalAlignment(JTextField.LEFT);

		lContraseña = new JLabel("Contraseña");
		lContraseña.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 13));
		lContraseña.setHorizontalAlignment(JLabel.LEFT);

		txtContraseña = new JPasswordField();
		txtContraseña.setHorizontalAlignment(JTextField.LEFT);

		bInicio = new JButton("Iniciar");

		// Colocación de componentes en los paneles
		pInicioNorte.add(lIconoPrincipal);
		pInicioCentro.add(lTitulo);
		pInicioCentro.add(lUsuario);
		pInicioCentro.add(txtUsuario);
		pInicioCentro.add(lContraseña);
		pInicioCentro.add(txtContraseña);
		pInicioSur.add(bInicio);

		add(pInicioCentro, BorderLayout.CENTER);
		add(pInicioSur, BorderLayout.SOUTH);
		add(pInicioNorte, BorderLayout.NORTH);

		bInicio.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				String Usuario = txtUsuario.getText();
				String Contraseña = String.valueOf(txtContraseña.getPassword());
				if (Usuario.equals("Alumno") && Contraseña.equals("UnaiCalvo")) {
					// Pasar a la siguiente ventana
				} else {
					JOptionPane.showMessageDialog(null, "Nombre de usuario y/o contraseña incorrectos");
				}

			}
		});

		setVisible(true);

	}

	public static void main(String[] args) {
		new VentanaInicial();
	}
}
