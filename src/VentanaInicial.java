import java.awt.BorderLayout;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class VentanaInicial extends JFrame {
	
	private JPanel pInicioSur, pInicioCentro, pInicioDer, pInicioIzq;
	private JButton bInicio;
	private JLabel lTitulo, lUsuario, lContraseña;
	private JTextField txtUsuario, txtContraseña;
	
	public VentanaInicial(){
		setTitle("Inicio");
		setSize(400, 400);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		
		pInicioCentro = new JPanel(new GridLayout(5, 1));
		pInicioSur = new JPanel();
		pInicioDer = new JPanel();
		pInicioIzq = new JPanel();
		
		lTitulo = new JLabel("Inicio Sesion");
		lTitulo.setHorizontalAlignment(JLabel.CENTER);
		
		lUsuario = new JLabel("Usuario");
		lContraseña = new JLabel("Contraseña");
		
		txtUsuario = new JTextField();
		txtContraseña = new JTextField();
		
		bInicio = new JButton("OK");
		
		pInicioCentro.add(lTitulo);
		pInicioCentro.add(lUsuario);
		pInicioCentro.add(txtUsuario);
		pInicioCentro.add(lContraseña);
		pInicioCentro.add(txtContraseña);
		pInicioSur.add(bInicio);
		
		
		add(pInicioSur, BorderLayout.SOUTH);
		add(pInicioCentro, BorderLayout.CENTER);
		add(pInicioDer, BorderLayout.WEST);
		add(pInicioIzq, BorderLayout.EAST);
		
		
		
		
		
		
		setVisible(true);
	}
	
	
	
	public static void main(String[] args) {
		new VentanaInicial();
	}
}
