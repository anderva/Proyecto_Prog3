package domain;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

import domain.enums.Especialidad;
import domain.enums.EstadoCita;
import domain.enums.GrupoSanguineo;
import domain.enums.Sexo;
import domain.personas.Medico;
import domain.personas.Paciente;
import domain.personas.Usuario;

public class GestorHospital {
	
	// horas a las que se pueden pedir citas (las usan las ventanas de citas)
	public static final String[] HORAS_CITA = { "09:00", "09:30", "10:00", "10:30", "11:00", "11:30", "12:00",
			"12:30", "13:00" };
	// formato en el que se escriben y se muestran las fechas de las citas
	public static final DateTimeFormatter FORMATO_FECHA = DateTimeFormatter.ofPattern("dd/MM/yyyy");

	private List<Usuario> lUsuarios;
	private List<Paciente> lPacientes;
	private List<Medico> lMedicos;
	private List<Cita> lCitas;
	
	public GestorHospital(List<Usuario> lUsuarios, List<Paciente> lPacientes, List<Medico> lMedicos) {
		super();
		this.lUsuarios = lUsuarios;
		this.lPacientes = lPacientes;
		this.lMedicos = lMedicos;
	}
	public GestorHospital() {
		// Inicializar listas vacias
		this.setlMedicos(new ArrayList<Medico>());
		this.setlPacientes(new ArrayList<Paciente>());
		this.setlUsuarios(new ArrayList<Usuario>());
		this.setlCitas(new ArrayList<Cita>());
		cargarDatosPrueba();
	}
	public List<Usuario> getlUsuarios() {
		return lUsuarios;
	}
	public void setlUsuarios(List<Usuario> lUsuarios) {
		this.lUsuarios = lUsuarios;
	}
	public List<Paciente> getlPacientes() {
		return lPacientes;
	}
	public void setlPacientes(List<Paciente> lPacientes) {
		this.lPacientes = lPacientes;
	}
	public List<Medico> getlMedicos() {
		return lMedicos;
	}
	public void setlMedicos(List<Medico> lMedicos) {
		this.lMedicos = lMedicos;
	}
	public List<Cita> getlCitas() {
		return lCitas;
	}
	public void setlCitas(List<Cita> lCitas) {
		this.lCitas = lCitas;
	}
	public void addCita(Cita cita) {
		lCitas.add(cita);
	}
	public void addUsuario(Usuario usuario) {
		List<Usuario> lUsuarios = this.getlUsuarios();
		lUsuarios.add(usuario);
		this.setlUsuarios(lUsuarios);
	}
	public void removeUsuario(Usuario usuario) {
		List<Usuario> lUsuarios = this.getlUsuarios();
		if(lUsuarios.contains(usuario)) {
			lUsuarios.remove(usuario);
			this.setlUsuarios(lUsuarios);
		}
	}
	public void addPaciente(Paciente paciente) {
		List<Paciente> lPacientes = this.getlPacientes();
		lPacientes.add(paciente);
		this.setlPacientes(lPacientes);
	}
	public void removePaciente(Paciente paciente) {
		List<Paciente> lPacientes = this.getlPacientes();
		if(lPacientes.contains(paciente)) {
			lPacientes.remove(paciente);
			this.setlPacientes(lPacientes);
		}
	}
	public void addMedico(Medico medico) {
		List<Medico> lMedicos = this.getlMedicos();
		lMedicos.add(medico);
		this.setlMedicos(lMedicos);
	}
	public void removeMedico(Medico medico) {
		List<Medico> lMedicos = this.getlMedicos();
		if(lMedicos.contains(medico)) {
			lMedicos.remove(medico);
			this.setlMedicos(lMedicos);
		}
	}
	
	// Devuelve las horas a las que se puede poner una cita a ese medico y a ese
	// paciente ese dia. Se quitan las horas en las que el medico o el paciente
	// ya tienen una cita pendiente y, si la fecha es hoy, las horas que ya han pasado.
	// citaExcluida es la cita que se esta modificando (su hora cuenta como libre),
	// si se esta pidiendo una cita nueva se pasa null.
	public List<String> getHorasLibres(Medico medico, Paciente paciente, LocalDate fecha, Cita citaExcluida) {
		List<String> horasLibres = new ArrayList<String>();
		for (String hora : HORAS_CITA) {
			boolean libre = true;

			// si la cita es para hoy no se puede pedir una hora que ya ha pasado
			if (fecha.equals(LocalDate.now()) && LocalTime.parse(hora).isBefore(LocalTime.now())) {
				libre = false;
			}

			// se mira si el medico o el paciente ya tienen una cita pendiente a esa hora
			for (Cita c : lCitas) {
				if (c != citaExcluida && c.getEstado() == EstadoCita.PENDIENTE && c.getFechaCita().equals(fecha)
						&& c.getHora().equals(hora) && (c.getMedico() == medico || c.getPaciente() == paciente)) {
					libre = false;
				}
			}

			if (libre) {
				horasLibres.add(hora);
			}
		}
		return horasLibres;
	}

	// Convierte un texto con formato dd/MM/yyyy en un LocalDate.
	// Devuelve null si el texto no es una fecha valida (por ejemplo 31/02/2026)
	public static LocalDate parsearFecha(String texto) {
		try {
			LocalDate fecha = LocalDate.parse(texto, FORMATO_FECHA);
			// LocalDate.parse convierte 31/02 en 28/02 sin dar error, por eso
			// se vuelve a pasar a texto y se comprueba que es el mismo dia
			if (!fecha.format(FORMATO_FECHA).equals(texto)) {
				return null;
			}
			return fecha;
		} catch (DateTimeParseException e) {
			return null;
		}
	}

	
	//Datos de prueba para no tener que registrarse cada vez
	private void cargarDatosPrueba() {
		Medico m1 = new Medico("12345678A", "Aitor", "Garcia", LocalDate.of(2001, 1, 1), Sexo.HOMBRE, 611111111,
				"garcia@osaki.eus", "Bilbao", "abcd", Especialidad.CARDIOLOGIA, new ArrayList<>(), 30);
		Medico m2 = new Medico("12345678B", "Ander", "Vallejo", LocalDate.of(2002, 2, 2), Sexo.HOMBRE, 622222222,
				"vallejo@osaki.eus", "Bilbao", "abcd", Especialidad.PEDIATRIA, new ArrayList<>(), 20);
		Medico m3 = new Medico("12345678C", "Kaiet", "Cabezon", LocalDate.of(2003, 3, 3), Sexo.HOMBRE, 633333333,
				"cabezon@osaki.eus", "Getxo", "abcd", Especialidad.TRAUMATOLOGIA, new ArrayList<>(), 30);
		addMedico(m1);
		addMedico(m2);
		addMedico(m3);
		addUsuario(m1);
		addUsuario(m2);
		addUsuario(m3);

		Paciente p1 = new Paciente("11111111D", "Mikel", "Garcia", LocalDate.of(2000, 3, 10), Sexo.HOMBRE, 644444444,
				"mikel@gmail.com", "Bilbao", "1234", GrupoSanguineo.A_PLUS);
		Paciente p2 = new Paciente("22222222E", "Ane", "Lopez", LocalDate.of(1995, 8, 5), Sexo.MUJER, 655555555,
				"ane@gmail.com", "Barakaldo", "1234", GrupoSanguineo.ZERO_PLUS);
		addPaciente(p1);
		addPaciente(p2);
		addUsuario(p1);
		addUsuario(p2);
		
		addCita(new Cita(0, p1, m1, LocalDate.of(2026, 11, 20), EstadoCita.PENDIENTE, "09:00", "Revisión del electrocardiograma"));
		addCita(new Cita(0, p2, m3, LocalDate.of(2026, 11, 21), EstadoCita.PENDIENTE, "10:30", "Dolor de rodilla al correr"));
		addCita(new Cita(0, p1, m2, LocalDate.of(2026, 11, 10), EstadoCita.REALIZADA, "11:00", "Vacuna de la gripe"));

		// citas con fechas relativas a hoy para que en la demo siempre haya citas
		// de hoy, de mañana, pasadas y canceladas
		LocalDate hoy = LocalDate.now();
		addCita(new Cita(0, p2, m1, hoy, EstadoCita.PENDIENTE, "13:00", "Palpitaciones por la noche"));
		addCita(new Cita(0, p1, m3, hoy.plusDays(1), EstadoCita.PENDIENTE, "09:30", "Esguince de tobillo"));
		addCita(new Cita(0, p2, m2, hoy.plusDays(7), EstadoCita.PENDIENTE, "12:00", "Revisión anual"));
		addCita(new Cita(0, p1, m1, hoy.minusDays(3), EstadoCita.CANCELADA, "10:00", "Control de tensión"));
		addCita(new Cita(0, p2, m3, hoy.minusDays(14), EstadoCita.REALIZADA, "09:00", "Radiografía de muñeca"));
	}
}

