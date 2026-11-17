package gui;

import java.time.LocalDate;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import domain.Cita;
import domain.enums.Especialidad;
import domain.enums.EstadoCita;
import domain.personas.Medico;
import domain.personas.Paciente;

// modelo de datos propio de la tabla de citas
// (antes era una clase interna de VentanaCitas, se ha sacado a su propio fichero
// porque tambien lo usa el renderer de la tabla)
public class ModeloCitas extends AbstractTableModel {

	private static final long serialVersionUID = 1L;

	// nombres de las columnas
	private String[] cabeceras = { "ID", "Fecha", "Hora", "Paciente", "Médico", "Especialidad", "Estado" };
	private List<Cita> citas; // citas que se ven en la tabla (las que cumplen el filtro)

	public ModeloCitas(List<Cita> citas) {
		this.citas = citas;
	}

	@Override
	public String getColumnName(int columna) {
		return cabeceras[columna];
	}

	@Override
	public int getRowCount() {
		return citas.size();
	}

	@Override
	public int getColumnCount() {
		return cabeceras.length;
	}

	// tipo de dato de cada columna, lo usa el JTable para elegir el renderer
	@Override
	public Class<?> getColumnClass(int columna) {
		if (columna == 0) {
			return Integer.class;
		} else if (columna == 1) {
			return LocalDate.class;
		} else if (columna == 3) {
			return Paciente.class;
		} else if (columna == 4) {
			return Medico.class;
		} else if (columna == 5) {
			return Especialidad.class;
		} else if (columna == 6) {
			return EstadoCita.class;
		}
		return String.class;
	}

	@Override
	public Object getValueAt(int fila, int columna) {
		// se coge la cita de esa fila y se devuelve el dato de la columna
		// paciente y medico se devuelven como objeto para que el renderer
		// pueda poner el icono y el texto que quiera
		Cita c = citas.get(fila);
		if (columna == 0) {
			return c.getIdCita();
		} else if (columna == 1) {
			return c.getFechaCita();
		} else if (columna == 2) {
			return c.getHora();
		} else if (columna == 3) {
			return c.getPaciente();
		} else if (columna == 4) {
			return c.getMedico();
		} else if (columna == 5) {
			return c.getMedico().getEspecialidad();
		} else if (columna == 6) {
			return c.getEstado();
		}
		return null;
	}

	@Override
	public boolean isCellEditable(int fila, int columna) {
		return false; // no se puede escribir en la tabla, se modifica con la ventana de modificar
	}

	// devuelve la cita completa de una fila (para los botones y el renderer)
	public Cita getCita(int fila) {
		return citas.get(fila);
	}

	// cambia las citas que se ven en la tabla y avisa al JTable para que se repinte
	public void actualizarCitas(List<Cita> citas) {
		this.citas = citas;
		fireTableDataChanged();
	}
}