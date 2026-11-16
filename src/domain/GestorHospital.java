package domain;
import java.time.LocalDate;
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

		addCita(new Cita(0, p1, m1, LocalDate.of(2026, 11, 20), EstadoCita.PENDIENTE, "09:00"));
		addCita(new Cita(0, p2, m3, LocalDate.of(2026, 11, 21), EstadoCita.PENDIENTE, "10:30"));
		addCita(new Cita(0, p1, m2, LocalDate.of(2026, 11, 10), EstadoCita.REALIZADA, "11:00"));
	}
}

