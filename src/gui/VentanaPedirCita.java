package gui;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.time.LocalDate;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import domain.enums.Especialidad;
import domain.enums.Sexo;
import domain.personas.Medico;

public class VentanaPedirCita extends JFrame{
	private static final long serialVersionUID = 1L;
	
	public VentanaPedirCita(ArrayList<Medico> lMedicos) {
		setTitle("OsakiDeusto - Pedir cita");
		setSize(420, 430);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE); //porque es ventana secundaria
		setResizable(false);
		setLocationRelativeTo(null);
		
		JPanel panelTotal = new JPanel(new BorderLayout());
		panelTotal.setBorder(BorderFactory.createEmptyBorder(15, 25, 10, 25));
		
		//Panel Superior
		JPanel panelSuperior = new JPanel(new BorderLayout(0, 5));
		
		ImageIcon imagen = new ImageIcon("img/OsakiDeustoLogo.png");
		Image imagenEscalada = imagen.getImage().getScaledInstance(100, 59, Image.SCALE_SMOOTH);
		JLabel labelImagen = new JLabel(new ImageIcon(imagenEscalada));
		panelSuperior.add(labelImagen, BorderLayout.NORTH);
		
		JLabel labelTitulo = new JLabel("PEDIR CITA", JLabel.CENTER);
		labelTitulo.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 18));
		panelSuperior.add(labelTitulo, BorderLayout.CENTER);

		//Panel Central
		JPanel panelCentral = new JPanel(new GridLayout(3, 1, 10, 10));
		panelCentral.setBorder(BorderFactory.createEmptyBorder(15, 0, 0, 0));

		JPanel medico = new JPanel(new GridLayout(2, 1, 5, 3));
		JLabel labelMedico = new JLabel("Médico: ");
		medico.add(labelMedico);
		JComboBox<String> comboMedicos = new JComboBox<String>();
		for (Medico m : lMedicos) {
			comboMedicos.addItem(m.getNombre() + " " + m.getApellidos() + " (" + m.getEspecialidad() + ")");
		}
		medico.add(comboMedicos);
		panelCentral.add(medico);

		//Fecha y Hora
		JPanel fechaHora = new JPanel(new GridLayout(1, 2, 10, 0));

		JPanel fecha = new JPanel(new GridLayout(2, 1, 5, 3));
		JLabel labelFecha = new JLabel("Fecha (dd/mm/aaaa): ");
		fecha.add(labelFecha);
		JTextField textoFecha = new JTextField();
		fecha.add(textoFecha);
		fechaHora.add(fecha);

		JPanel hora = new JPanel(new GridLayout(2, 1, 5, 3));
		JLabel labelHora = new JLabel("Hora: ");
		hora.add(labelHora);
		JComboBox<String> comboHoras = new JComboBox<String>();
		String[] horas = { "09:00", "09:30", "10:00", "10:30", "11:00", "11:30", "12:00", "12:30", "13:00"};
		for (String h : horas) {
			comboHoras.addItem(h);
		}
		hora.add(comboHoras);
		fechaHora.add(hora);

		panelCentral.add(fechaHora);

		// Motivo de la consulta
		JPanel motivo = new JPanel(new GridLayout(2, 1, 5, 3));
		JLabel labelMotivo = new JLabel("Motivo de la consulta: ");
		motivo.add(labelMotivo);
		JTextField textoMotivo = new JTextField();
		motivo.add(textoMotivo);
		panelCentral.add(motivo);

		// Panel Inferior
		JPanel panelInferior = new JPanel(new GridLayout(1, 2, 10, 10));
		panelInferior.setBorder(BorderFactory.createEmptyBorder(20, 0, 5, 0));
		JButton botonCancelar = new JButton("Cancelar");
		panelInferior.add(botonCancelar);
		JButton botonConfirmar = new JButton("Confirmar");
		panelInferior.add(botonConfirmar);

		panelTotal.add(panelSuperior, BorderLayout.NORTH);
		panelTotal.add(panelCentral, BorderLayout.CENTER);
		panelTotal.add(panelInferior, BorderLayout.SOUTH);

		this.add(panelTotal);

		setVisible(true);
	}
	
	public static void main(String[] args) {
		// Médicos de prueba
		ArrayList<Medico> medicos = new ArrayList<Medico>();
		medicos.add(new Medico("12345678A", "Poyete", "Garcia", LocalDate.of(1980, 1, 15), Sexo.MUJER, 611111111,
				"filete@osaki.eus", "Bilbao", "abcd", Especialidad.CARDIOLOGIA, new ArrayList<>(), 30));
		medicos.add(new Medico("12345678B", "Anderva", "Lopez", LocalDate.of(1975, 7, 2), Sexo.HOMBRE, 622222222,
				"vallejo@osaki.eus", "Bilbao", "abcd", Especialidad.PEDIATRIA, new ArrayList<>(), 20));

		new VentanaPedirCita(medicos);	}

}
