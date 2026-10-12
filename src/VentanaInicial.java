import javax.swing.JFrame;

public class VentanaInicial extends JFrame {
	
	public VentanaInicial(){
		setTitle("Inicio");
		setSize(400, 400);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		
		
		setVisible(true);
	}
	
	
	
	public static void main(String[] args) {
		new VentanaInicial();
	}
}
