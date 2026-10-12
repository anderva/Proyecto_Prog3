import java.awt.BorderLayout;

import javax.swing.JButton;
import javax.swing.JFrame;

public class VentanaInicial extends JFrame {
	
	private JButton bInicio;
	
	public VentanaInicial(){
		setTitle("Inicio");
		setSize(400, 400);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		
		bInicio = new JButton("Inicio");
		add(bInicio, BorderLayout.SOUTH);
		
		
		setVisible(true);
	}
	
	
	
	public static void main(String[] args) {
		new VentanaInicial();
	}
}
