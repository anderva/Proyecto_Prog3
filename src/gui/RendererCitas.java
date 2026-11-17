package gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

import domain.Cita;
import domain.GestorHospital;
import domain.enums.Especialidad;
import domain.enums.EstadoCita;
import domain.enums.Sexo;
import domain.personas.Medico;
import domain.personas.Paciente;

// Renderer de la tabla de citas. Se usa para todas las columnas y decide
// como pintar cada celda segun la columna y la cita de esa fila:
// - filas pares e impares de distinto color
// - icono y color segun el estado de la cita
// - icono segun la especialidad del medico
// - fecha con formato dd/MM/yyyy, "Hoy" y "Mañana" en negrita y citas atrasadas en rojo
// - citas canceladas con el texto en gris
// - el motivo de la consulta sale como tooltip al pasar el raton
//
// Extiende de DefaultTableCellRenderer para aprovechar el JLabel que ya
// configura el renderer por defecto (colores de seleccion, borde del foco...)
public class RendererCitas extends DefaultTableCellRenderer {

	private static final long serialVersionUID = 1L;

	// colores de la aplicacion (sacados del logo de OsakiDeusto)
	public static final Color AZUL_OSAKI = Color.BLUE;
	public static final Color VERDE_OSAKI = Color.GREEN;

	private static final Color FONDO_FILA_PAR = Color.WHITE;
	private static final Color FONDO_FILA_IMPAR = Color.LIGHT_GRAY;
	private static final Color TEXTO_CANCELADA = Color.GRAY;
	private static final Color TEXTO_ATRASADA = Color.RED;

	// color del texto de cada estado
	private Map<EstadoCita, Color> coloresEstado = Map.of(
		EstadoCita.PENDIENTE, Color.ORANGE.darker(),
		EstadoCita.REALIZADA, Color.GREEN.darker(),
		EstadoCita.CANCELADA, Color.RED
	);

	// los iconos se cargan una sola vez en el constructor y se guardan en mapas,
	// si se cargaran dentro de getTableCellRendererComponent se leerian del disco
	// cada vez que se pinta una celda y la tabla iria muy lenta
	private Map<EstadoCita, ImageIcon> iconosEstado = new HashMap<>();
	private Map<Especialidad, ImageIcon> iconosEspecialidad = new HashMap<>();
	private ImageIcon iconoPaciente;
	private ImageIcon iconoMedico;

	public RendererCitas() {
		// un icono por cada estado: /images/citas/pendiente.png, realizada.png, cancelada.png
		for (EstadoCita estado : EstadoCita.values()) {
			iconosEstado.put(estado, cargarIcono(estado.toString().toLowerCase()));
		}
		// un icono por cada especialidad: /images/citas/cardiologia.png, pediatria.png...
		for (Especialidad especialidad : Especialidad.values()) {
			iconosEspecialidad.put(especialidad, cargarIcono(especialidad.toString().toLowerCase()));
		}
		iconoPaciente = cargarIcono("paciente");
		iconoMedico = cargarIcono("medico");
	}

	// carga un icono de la carpeta src/images/citas
	// si no lo encuentra devuelve null y la celda sale sin icono (no da error).
	// Es static para poder usarlo tambien en VentanaCitas para los botones
	public static ImageIcon cargarIcono(String nombre) {
		try {
			return new ImageIcon(RendererCitas.class.getResource("/images/citas/" + nombre + ".png"));
		} catch (Exception e) {
			System.err.println("No se ha podido cargar el icono: " + nombre);
			return null;
		}
	}

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
			int row, int column) {
		// aprovechamos el JLabel del renderer por defecto
		JLabel label = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

		// cita completa de esta fila
		Cita cita = ((ModeloCitas) table.getModel()).getCita(row);

		// como el mismo JLabel se reutiliza para todas las celdas,
		// primero se deja todo como estaba por defecto
		label.setIcon(null);
		label.setHorizontalAlignment(JLabel.LEFT);
		label.setFont(table.getFont());
		label.setBorder(BorderFactory.createEmptyBorder(0, 6, 0, 6)); // un poco de margen
		label.setToolTipText("Motivo: " + cita.getMotivo());

		// colores de la fila (si esta seleccionada se dejan los de seleccion)
		if (!isSelected) {
			if (row % 2 == 0) {
				label.setBackground(FONDO_FILA_PAR);
			} else {
				label.setBackground(FONDO_FILA_IMPAR);
			}
			if (cita.getEstado() == EstadoCita.CANCELADA) {
				label.setForeground(TEXTO_CANCELADA);
			} else {
				label.setForeground(table.getForeground());
			}
		}

		// ahora lo especifico de cada columna
		if (column == 0) {
			// ID centrado
			label.setText("#" + cita.getIdCita());
			label.setHorizontalAlignment(JLabel.CENTER);

		} else if (column == 1) {
			// fecha con formato, hoy y mañana se resaltan
			LocalDate fecha = (LocalDate) value;
			String texto = fecha.format(GestorHospital.FORMATO_FECHA);
			if (fecha.equals(LocalDate.now())) {
				texto = "Hoy · " + texto;
				label.setFont(label.getFont().deriveFont(Font.BOLD));
			} else if (fecha.equals(LocalDate.now().plusDays(1))) {
				texto = "Mañana · " + texto;
				label.setFont(label.getFont().deriveFont(Font.BOLD));
			}
			// cita pendiente cuya fecha ya ha pasado: se avisa en rojo
			if (cita.getEstado() == EstadoCita.PENDIENTE && fecha.isBefore(LocalDate.now())) {
				texto = texto + " (atrasada)";
				if (!isSelected) {
					label.setForeground(TEXTO_ATRASADA);
				}
			}
			label.setText(texto);

		} else if (column == 2) {
			// hora centrada
			label.setHorizontalAlignment(JLabel.CENTER);

		} else if (column == 3) {
			// paciente con icono
			Paciente p = (Paciente) value;
			label.setText(p.getNombre() + " " + p.getApellidos());
			label.setIcon(iconoPaciente);

		} else if (column == 4) {
			// medico con icono y Dr. o Dra.
			Medico m = (Medico) value;
			String titulo = "Dr. ";
			if (m.getSexo() == Sexo.MUJER) {
				titulo = "Dra. ";
			}
			label.setText(titulo + m.getNombre() + " " + m.getApellidos());
			label.setIcon(iconoMedico);

		} else if (column == 5) {
			// especialidad con su icono y bien escrita
			Especialidad e = (Especialidad) value;
			label.setText(textoEspecialidad(e));
			label.setIcon(iconosEspecialidad.get(e));

		} else if (column == 6) {
			// estado con su icono y su color en negrita
			EstadoCita estado = (EstadoCita) value;
			label.setText(textoEstado(estado));
			label.setIcon(iconosEstado.get(estado));
			label.setFont(label.getFont().deriveFont(Font.BOLD));
			if (!isSelected) {
				label.setForeground(coloresEstado.get(estado));
			}
		}

		// se devuelve el label para que el JTable lo pinte en la celda
		return label;
	}

	// texto bonito de cada especialidad (el enum esta en mayusculas y sin tildes)
	public static String textoEspecialidad(Especialidad e) {
		switch (e) {
		case MEDICINA_GENERAL:
			return "Medicina general";
		case PEDIATRIA:
			return "Pediatría";
		case CARDIOLOGIA:
			return "Cardiología";
		case TRAUMATOLOGIA:
			return "Traumatología";
		case DERMATOLOGIA:
			return "Dermatología";
		case NEUROLOGIA:
			return "Neurología";
		case GINECOLOGIA:
			return "Ginecología";
		case OFTALMOLOGIA:
			return "Oftalmología";
		default:
			return e.toString();
		}
	}

	// texto bonito de cada estado
	public static String textoEstado(EstadoCita estado) {
		switch (estado) {
		case PENDIENTE:
			return "Pendiente";
		case REALIZADA:
			return "Realizada";
		case CANCELADA:
			return "Cancelada";
		default:
			return estado.toString();
		}
	}
}