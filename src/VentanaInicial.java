import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
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
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class VentanaInicial extends JFrame {
	
	private JPanel pInicioSur, pInicioCentro;
	private JButton bInicio;
	private JLabel lTitulo, lUsuario, lContraseña, lLogo;
	private JTextField txtUsuario, txtContraseña;
	private ImageIcon iconoPrincipal;
	
	public VentanaInicial(){
		setTitle("OsakiDeusto - Inicio");
		setSize(400, 250);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setResizable(false);
		setLocationRelativeTo(null); //Centrar en la pantalla
		
		//Creación de paneles
		pInicioCentro = new JPanel(new GridLayout(5, 1));
		pInicioCentro.setBorder(BorderFactory.createEmptyBorder(25, 40, 25, 40));
		pInicioSur = new JPanel();
		
		
		//Creación de componentes
//		iconoPrincipal = new ImageIcon("img/OsakiDeusto.png");
//		lLogo = new JLabel(iconoPrincipal);
//		lLogo.setHorizontalAlignment(JLabel.CENTER);
		
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
		
		txtContraseña = new JTextField();
		txtContraseña.setHorizontalAlignment(JTextField.LEFT);
		
		bInicio = new JButton("Iniciar");
		
		
		
		
		//Colocación de componentes en los paneles
		//pInicioCentro.add(lLogo);
		pInicioCentro.add(lTitulo);
		pInicioCentro.add(lUsuario);
		pInicioCentro.add(txtUsuario);
		pInicioCentro.add(lContraseña);
		pInicioCentro.add(txtContraseña);
		pInicioSur.add(bInicio);
		
		
	
		add(pInicioCentro, BorderLayout.CENTER);
		add(pInicioSur, BorderLayout.SOUTH);
		
		bInicio.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				String Usuario = txtUsuario.getText();
				String Contraseña = txtContraseña.getText();
				
				if(Usuario.equals("Alumno") && Contraseña.equals("UnaiCalvo")) {
					//Pasar a la siguiente ventana
				}else {
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
