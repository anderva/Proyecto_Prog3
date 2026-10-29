package gui;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import domain.enums.Sexo;
import domain.personas.Usuario;

public class VentanaRegistro extends JFrame{

	private static final long serialVersionUID = 1L;

	public static void main(String[] args) {
		new VentanaRegistro();
	}

	public VentanaRegistro() {
		// Configuraciones de la ventana
		setTitle("Registrar Nuevo Usuario");
		setSize(420, 430);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setResizable(false);
		setLocationRelativeTo(null); // Centrar en la pantalla

		JPanel panelTotal = new JPanel(new BorderLayout());
		panelTotal.setBorder(BorderFactory.createEmptyBorder(20, 10, 10, 20));
		// Panel Superior con titulo e imagen
		JPanel panelSuperior = new JPanel(new BorderLayout(0,10));


		// Titulo
		JLabel labelTitulo = new JLabel("NUEVO USUARIO", JLabel.CENTER);
		labelTitulo.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 18));
		panelSuperior.add(labelTitulo, BorderLayout.NORTH);

		// Imagen
		ImageIcon imagen = new ImageIcon(getClass().getResource("/images/NuevoUsuario.png"));
		Image imagenEscalada = imagen.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH); // Reescalacion de imagen
		JLabel labelImagen = new JLabel(new ImageIcon(imagenEscalada));
		panelSuperior.add(labelImagen, BorderLayout.CENTER);

		// Panel Central con parametros

		JPanel panelCentral = new JPanel(new GridLayout(9, 1, 10, 10));

		// Parametros

		// DNI
		JPanel dni = new JPanel(new GridLayout(2,1,5,3));
		JLabel labelDni = new JLabel("DNI: ");
		dni.add(labelDni);
		JTextField textoDni = new JTextField();
		dni.add(textoDni);
		panelCentral.add(dni);


		// Nombre
		JPanel nombre = new JPanel(new GridLayout(2,1,5,3));
		JLabel labelNombre = new JLabel("Nombre: ");
		nombre.add(labelNombre);
		JTextField textoNombre = new JTextField();
		nombre.add(textoNombre);
		panelCentral.add(nombre);


		// Apellidos
		JPanel apellidos = new JPanel(new GridLayout(2,1,5,3));
		JLabel labelApellidos = new JLabel("Apellidos: ");
		apellidos.add(labelApellidos);
		JTextField textoApellidos = new JTextField();
		apellidos.add(textoApellidos);
		panelCentral.add(apellidos);

		// Fecha de nacimiento
		JPanel fechaNac = new JPanel(new GridLayout(2,1,5,3));
		JLabel labelFechaNac = new JLabel("Fecha de nacimiento (formato : yyyy-MM-dd): ");
		fechaNac.add(labelFechaNac);
		JTextField textoFechaNac = new JTextField();
		fechaNac.add(textoFechaNac);
		panelCentral.add(fechaNac);

		// Sexo
		JPanel sexo = new JPanel(new GridLayout(2,1,5,3));
		JLabel labelSexo = new JLabel("Sexo: ");
		sexo.add(labelSexo);
		JComboBox<Sexo> comboSexo = new JComboBox<Sexo>();
		comboSexo.addItem(Sexo.HOMBRE);
		comboSexo.addItem(Sexo.MUJER);		
		sexo.add(comboSexo);
		panelCentral.add(sexo);

		// Telefono
		JPanel tfno = new JPanel(new GridLayout(2,1,5,3));
		JLabel labelTfno = new JLabel("Teléfono: ");
		tfno.add(labelTfno);
		JTextField textoTfno = new JTextField();
		tfno.add(textoTfno);
		panelCentral.add(tfno);

		// Email
		JPanel email = new JPanel(new GridLayout(2,1,5,3));
		JLabel labelEmail = new JLabel("Correo electrónico: ");
		email.add(labelEmail);
		JTextField textoEmail = new JTextField();
		email.add(textoEmail);
		panelCentral.add(email);

		// Direccion
		JPanel direccion = new JPanel(new GridLayout(2,1,5,3));
		JLabel labelDireccion = new JLabel("Dirección: ");
		direccion.add(labelDireccion);
		JTextField textoDireccion = new JTextField();
		direccion.add(textoDireccion);
		panelCentral.add(direccion);

		// Contraseña
		JPanel contraseña = new JPanel(new GridLayout(2,1,5,3));
		JLabel labelContraseña = new JLabel("Contraseña: ");
		contraseña.add(labelContraseña);
		JTextField textoContraseña = new JTextField();
		contraseña.add(textoContraseña);
		panelCentral.add(contraseña);


		// Panel Inferior con boton guardar y cancelar
		JPanel panelInferior = new JPanel(new GridLayout(1, 2, 10, 10));
		panelInferior.setBorder(BorderFactory.createEmptyBorder(20, 10, 10, 20));
		// Boton Guardar
		JButton botonGuardar = new JButton("Guardar");
		panelInferior.add(botonGuardar);
		botonGuardar.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if(	esDniValido(textoDni.getText()) &&
					esNombreValido(textoNombre.getText()) &&
					esNombreValido(textoApellidos.getText()) &&
					esTfnoValido(textoTfno.getText()) &&
					esFechaValida(textoFechaNac.getText())) {
					
					Usuario usuario = new Usuario(	
							textoDni.getText(), 
							textoNombre.getText(),
							textoApellidos.getText(), 
							LocalDate.parse(textoFechaNac.getText()),
							Sexo.valueOf(comboSexo.getSelectedItem().toString()), 
							Integer.parseInt(textoTfno.getText()), 
							textoEmail.getText(),
							textoDireccion.getText(), 
							textoContraseña.getText());
					System.out.println(usuario);
				} else {
					String strErrores = "Existen errores de formato en los siguientes campos: \n";
					if(!esDniValido(textoDni.getText())) {
						strErrores += "- DNI\n";
					}
					if(!esNombreValido(textoNombre.getText())) {
						strErrores += "- NOMBRE\n";
					}
					if(!esNombreValido(textoApellidos.getText())) {
						strErrores += "- APELLIDOS\n";
					}
					if(!esFechaValida(textoFechaNac.getText())) {
						strErrores += "- FECHA NACIMIENTO\n";
					}
					if(!esTfnoValido(textoTfno.getText())) {
						strErrores += "- TELEFONO\n";
					}
					JOptionPane.showMessageDialog(null, strErrores, "Error", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		// Boton Cancelar
		JButton botonCancelar = new JButton("Cancelar");
		panelInferior.add(botonCancelar);
		botonCancelar.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				VentanaRegistro.this.dispose();				
			}
		});

		panelTotal.add(panelSuperior, BorderLayout.NORTH);
		panelTotal.add(panelCentral, BorderLayout.CENTER);
		panelTotal.add(panelInferior, BorderLayout.SOUTH);

		JScrollPane scroller = new JScrollPane(panelTotal);
		scroller.getVerticalScrollBar().setUnitIncrement(8);
		
		this.add(scroller);
		setVisible(true);
		
	}

	public static boolean esNombreValido(String nombre) {
		if (nombre == null || nombre.isBlank()) {
			return false;
		}
		for (char c : nombre.toCharArray()) {
			if (Character.isDigit(c)) {
				return false;
			}
		}
		return true;
	}

	public static boolean esDniValido(String dni) {
		dni = dni.trim();
		dni = dni.toUpperCase();
		if (dni == null || dni.isBlank()) {
			return false;
		}
		if(dni.length()!=9) {
			return false;
		}

		for (char c: dni.toCharArray()) {
			if ( c== dni.charAt(dni.length()-1)) {
				if(!Character.isLetter(c)) {
					return false;
				}
			}
			else{
				if(!Character.isDigit(c)) {
					return false;
				}
			}
		}
		return true;
	}
	public static boolean esFechaValida(String strFecha) {
		if (strFecha == null || strFecha.isBlank()) {
			return false;
		}
		try {
			LocalDate fecha = LocalDate.parse(strFecha);
			if(LocalDate.now().isBefore(fecha)) {
				return false;
			}
		} catch (DateTimeParseException e) {
			return false;
		}
		return true;
	}
	
	public static boolean esTfnoValido(String tfno) {
		if (tfno == null || tfno.isBlank()) {
			return false;
		}
		if(tfno.length() != 9) {
			return false;
		}
		for(char c: tfno.toCharArray()) {
			if(!Character.isDigit(c)) {
				return false;
			}
		}
		return true;
	}
}
