package domain;
import java.util.ArrayList;
import java.util.List;

import domain.personas.*;

public class GestorHospital {
	private List<Usuario> lUsuarios;
	private List<Paciente> lPacientes;
	private List<Medico> lMedicos;
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
}

