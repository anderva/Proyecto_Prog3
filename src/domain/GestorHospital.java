package domain;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

import domain.enums.DiaSemana;
import domain.enums.Especialidad;
import domain.enums.EstadoCita;
import domain.enums.EstadoPago;
import domain.enums.GrupoSanguineo;
import domain.enums.Sexo;
import domain.enums.TipoHabitacion;
import domain.enums.TipoUsuario;
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
	private List<Factura> lFacturas = new ArrayList<Factura>();
	private List<Habitacion> lHabitaciones = new ArrayList<>();
	private List<HistorialClinico> lHistoriales = new ArrayList<>();
	public static final String NOMBRE_BD = "Osaki.db";
	private BD bd;
	
	public GestorHospital(List<Usuario> lUsuarios, List<Paciente> lPacientes, List<Medico> lMedicos) {
		super();
		Usuario usuario = new Usuario("00000000X", "admin", "admin", LocalDate.MIN, null, 0, "admin@gmail.com", "admin", "admin");
		this.lUsuarios = lUsuarios;
		this.lPacientes = lPacientes;
		this.lMedicos = lMedicos;
		this.addUsuario(usuario);
	}
	public GestorHospital() {
		Usuario usuario = new Usuario("00000000X", "admin", "admin", LocalDate.MIN, null, 0, "admin@gmail.com", "admin", "admin");
		// Inicializar listas vacias
		this.setlMedicos(new ArrayList<Medico>());
		this.setlPacientes(new ArrayList<Paciente>());
		this.setlUsuarios(new ArrayList<Usuario>());
		this.setlCitas(new ArrayList<Cita>());
		bd = new BD();
		bd.initBD(NOMBRE_BD);
		bd.crearTablas();
		Runtime.getRuntime().addShutdownHook(new Thread(() -> bd.closeBD()));
		if (bd.estaVacia()) {
			// primera vez que se abre el programa: los datos de prueba se guardan en la BD
			cargarDatosPrueba();
		} else {
			cargarDatosBD();
		}
		this.addUsuario(usuario);
	}
	private void cargarDatosBD() {
		this.setlMedicos(bd.obtenerMedicos());
		this.setlPacientes(bd.obtenerPacientes());
		this.setlCitas(bd.obtenerCitas(lPacientes, lMedicos));
		this.lFacturas = bd.obtenerFacturas(lPacientes);
		List<Usuario> usuarios = new ArrayList<Usuario>();
		usuarios.addAll(lMedicos);
		usuarios.addAll(lPacientes);
		this.setlUsuarios(usuarios);
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
	public boolean addCita(Cita cita) {
		if (bd != null && !bd.insertarCita(cita)) {
			return false;
		}
		lCitas.add(cita);
		return true;
	}
	
	public boolean modificarCita(Cita cita, Medico medico, LocalDate fecha, String hora, String motivo) {
		Medico medicoAntes = cita.getMedico();
		LocalDate fechaAntes = cita.getFechaCita();
		String horaAntes = cita.getHora();
		String motivoAntes = cita.getMotivo();

		cita.setMedico(medico);
		cita.setFechaCita(fecha);
		cita.setHora(hora);
		cita.setMotivo(motivo);
		if (bd != null && !bd.modificarCita(cita)) {
			cita.setMedico(medicoAntes);
			cita.setFechaCita(fechaAntes);
			cita.setHora(horaAntes);
			cita.setMotivo(motivoAntes);
			return false;
		}
		return true;
	}
	
	public boolean cancelarCita(Cita cita) {
		return cambiarEstado(cita, EstadoCita.CANCELADA);
	}

	public boolean marcarRealizada(Cita cita) {
		return cambiarEstado(cita, EstadoCita.REALIZADA);
	}

	private boolean cambiarEstado(Cita cita, EstadoCita estado) {
		if (bd != null && !bd.cambiarEstadoCita(cita.getIdCita(), estado)) {
			return false;
		}
		cita.setEstado(estado);
		return true;
	}

	public boolean borrarCita(Cita cita) {
		if (bd != null && !bd.borrarCita(cita.getIdCita())) {
			return false;
		}
		lCitas.remove(cita);
		return true;
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
	public boolean addPaciente(Paciente paciente) {
		if (bd != null && !bd.insertarPaciente(paciente)) {
			return false;
		}
		List<Paciente> lPacientes = this.getlPacientes();
		lPacientes.add(paciente);
		this.setlPacientes(lPacientes);
		return true;
	}
	public void removePaciente(Paciente paciente) {
		List<Paciente> lPacientes = this.getlPacientes();
		if(lPacientes.contains(paciente)) {
			if (bd != null) {
				bd.borrarPaciente(paciente.getDni());
			}
			lPacientes.remove(paciente);
			this.setlPacientes(lPacientes);
			lCitas.removeIf(c -> c.getPaciente() == paciente);
			lFacturas.removeIf(f -> f.getPaciente() == paciente);
		}
	}
	public boolean addMedico(Medico medico) {
		if (bd != null && !bd.insertarMedico(medico)) {
			return false;
		}
		List<Medico> lMedicos = this.getlMedicos();
		lMedicos.add(medico);
		this.setlMedicos(lMedicos);
		return true;
	}
	public void removeMedico(Medico medico) {
		List<Medico> lMedicos = this.getlMedicos();
		if(lMedicos.contains(medico)) {
			if (bd != null) {
				bd.borrarMedico(medico.getDni());
			}
			lMedicos.remove(medico);
			this.setlMedicos(lMedicos);
			lCitas.removeIf(c -> c.getMedico() == medico);
		}
	}
	
	public boolean registrarPaciente(Paciente paciente) {
		for (Usuario u : lUsuarios) {
			if (u.getDni().equalsIgnoreCase(paciente.getDni()) || u.getEmail().equalsIgnoreCase(paciente.getEmail())) {
				return false;
			}
		}
		if (!addPaciente(paciente)) {
			return false;
		}
		addUsuario(paciente);
		return true;
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
	
	public static TipoUsuario obtenerTipoUsuario(Usuario usuario) {
		if (usuario instanceof Medico) {
			return TipoUsuario.MEDICO;
		} else if (usuario instanceof Paciente) {
			return TipoUsuario.PACIENTE;
		}
		return null;
	}
	
	public List<Habitacion> getlHabitaciones() {
		return lHabitaciones;
	}

	public List<HistorialClinico> getlHistoriales() {
		return lHistoriales;
	}

	public HistorialClinico getHistorialPaciente(Paciente p) {
		if (p.getHistorialClinico() == null) {
			HistorialClinico hc = new HistorialClinico(0, p, "");
			p.setHistorialClinico(hc);
			lHistoriales.add(hc);
		}
		return p.getHistorialClinico();
	}

	public void registrarConsulta(Cita cita, String diagnostico, String observaciones, double precio, ArrayList<Receta> recetas) {
		Paciente p = cita.getPaciente();
		Consulta consulta = new Consulta(0, cita, cita.getMotivo(), diagnostico, observaciones, recetas, precio);
		consulta.setlRecetas(recetas);

		p.setConsulta(consulta);
		getHistorialPaciente(p).getlConsulta().add(consulta);

		if (cita.getEstado() == EstadoCita.PENDIENTE) {
			marcarRealizada(cita);
		}
		// se guarda con addFactura para que tambien quede en la BD
		addFactura(new Factura(p, "Consulta " + cita.getMedico().getEspecialidad() + " (" + cita.getFechaCita().format(FORMATO_FECHA) + ")", precio));
	}

	public void addHabitacion(Habitacion h) {
		lHabitaciones.add(h);
	}

	public int getOcupacionHabitacion(Habitacion h) {
		int ocupados = 0;
		for (Paciente p : lPacientes) {
			if (p.getHabitacion() != null && p.getHabitacion().getNumHabitacion() == h.getNumHabitacion()) {
				ocupados++;
			}
		}
		return ocupados;
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
				"mikel@gmail.com", "Bilbao", "1234");
		Paciente p2 = new Paciente("22222222E", "Ane", "Lopez", LocalDate.of(1995, 8, 5), Sexo.MUJER, 655555555,
				"ane@gmail.com", "Barakaldo", "1234");
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
		addFactura(new Factura(p1, "Consulta de cardiología", 45));
		addFactura(new Factura(p2, "Radiografía de muñeca", 30));
		
		if (lHabitaciones.isEmpty()) {
			lHabitaciones.add(new Habitacion(0, TipoHabitacion.INDIVIDUAL));
			lHabitaciones.add(new Habitacion(0, TipoHabitacion.DOBLE));
			lHabitaciones.add(new Habitacion(0, TipoHabitacion.TRIPLE));
		}

		// Horarios iniciales para los médicos si aún no tienen
		for (Medico m : lMedicos) {
			if (m.getlHorarios() == null) {
				m.setlHorarios(new ArrayList<>());
			}
			if (m.getlHorarios().isEmpty()) {
				m.getlHorarios().add(new Horario(DiaSemana.LUNES, "09:00", "13:30"));
				m.getlHorarios().add(new Horario(DiaSemana.MIERCOLES, "09:00", "13:30"));
				m.getlHorarios().add(new Horario(DiaSemana.VIERNES, "09:00", "13:00"));
			}
		}

		// Asegurar que cada paciente tiene su HistorialClinico y lista de alergias
		for (Paciente p : lPacientes) {
			if (p.getlAlergias() == null) {
				p.setlAlergias(new ArrayList<>());
			}
			if (p.getHistorialClinico() == null) {
				HistorialClinico hc = new HistorialClinico(0, p, "");
				p.setHistorialClinico(hc);
				lHistoriales.add(hc);
			}
		}

		// Datos de ejemplo para consultas, recetas y facturas si están vacíos
		if (lFacturas.isEmpty() && !lPacientes.isEmpty() && !lCitas.isEmpty()) {
			Paciente p3 = lPacientes.get(0);
			if (p3.getNumSegSocial() == 0) {
				p3.setNumSegSocial(481029384);
			}
			if (p3.getlAlergias().isEmpty()) {
				p3.getlAlergias().add("Penicilina");
			}

			Cita citaEjemplo = lCitas.get(0);
			ArrayList<Medicamento> meds = new ArrayList<Medicamento>();
			Medicamento med = new Medicamento("Ibuprofeno 600mg", "1 comprimido cada 8 horas", 5);
			meds.add(med);
			Receta receta = new Receta(med.getDosis(), String.valueOf(med.getDuracionDias()), "Tomar después de las comidas", meds);
			receta.añadirMedicamento(med);

			ArrayList<Receta> recetas = new ArrayList<>();
			recetas.add(receta);

			Consulta consulta = new Consulta(0, citaEjemplo, citaEjemplo.getMotivo(),
					"Evolución favorable sin complicaciones", "Revisar en 15 días si persisten molestias", recetas, 45.0);
			// Nota: el constructor de Consulta reinicia lRecetas, así que las asignamos con el setter
			consulta.setlRecetas(recetas);

			p3.setConsulta(consulta);
			p3.getHistorialClinico().getlConsulta().add(consulta);

			lFacturas.add(new Factura(p1, "Consulta médica - " + citaEjemplo.getMedico().getEspecialidad(), 45.0));
		}
	}
	

	// FACTURAS

	public List<Factura> getlFacturas() {
		return lFacturas;
	}

	// Facturas de un paciente (comparando por DNI)
	public List<Factura> getFacturas(Paciente paciente) {
		List<Factura> facturas = new ArrayList<Factura>();
		for (Factura f : lFacturas) {
			if (f.getPaciente().getDni().equals(paciente.getDni())) {
				facturas.add(f);
			}
		}
		return facturas;
	}

	public boolean addFactura(Factura factura) {
		if (bd != null && !bd.insertarFactura(factura)) {
			return false;
		}
		lFacturas.add(0, factura);
		return true;
	}

	public boolean pagarFactura(Factura factura) {
		if (factura.getEstadoPago() != EstadoPago.PENDIENTE) {
			return false;
		}
		if (bd != null && !bd.cambiarEstadoFactura(factura.getIdFactura(), EstadoPago.PAGADA)) {
			return false;
		}
		factura.pagar();
		return true;
	}

	public boolean anularFactura(Factura factura) {
		if (factura.getEstadoPago() != EstadoPago.PENDIENTE) {
			return false;
		}
		if (bd != null && !bd.cambiarEstadoFactura(factura.getIdFactura(), EstadoPago.CANCELADA)) {
			return false;
		}
		factura.anular();
		return true;
	}
}

