package domain;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import domain.enums.Especialidad;
import domain.enums.EstadoCita;
import domain.enums.Sexo;
import domain.personas.Medico;
import domain.personas.Paciente;


public class BD {

	private Connection con;

	public void initBD(String nombreBD) {
		con = null;

		try {
			Class.forName("org.sqlite.JDBC");
			con = DriverManager.getConnection("jdbc:sqlite:resources/db/" + nombreBD);
			PreparedStatement ps = con.prepareStatement("PRAGMA foreign_keys = ON;");
			ps.execute();
			ps.close();

		} catch (ClassNotFoundException e) {
			System.err.println("Falta el jar sqlite-jdbc en el Build Path del proyecto (carpeta lib)");
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void closeBD() {
		if (con != null) {
			try {
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

//	Creacion tablas paciente, medico, cita
	public void crearTablas() {
		String sqlp = "CREATE TABLE IF NOT EXISTS Paciente (" + "dni TEXT PRIMARY KEY," + "nombre TEXT NOT NULL,"
				+ "apellidos TEXT NOT NULL," + "fecha_nacimiento TEXT NOT NULL," + "sexo TEXT NOT NULL,"
				+ "telefono INTEGER," + "email TEXT NOT NULL UNIQUE," + "direccion TEXT," + "contrasenia TEXT NOT NULL);";

		String sqlm = "CREATE TABLE IF NOT EXISTS Medico (" + "dni TEXT PRIMARY KEY," + "nombre TEXT NOT NULL,"
				+ "apellidos TEXT NOT NULL," + "fecha_nacimiento TEXT NOT NULL," + "sexo TEXT NOT NULL,"
				+ "telefono INTEGER," + "email TEXT NOT NULL UNIQUE," + "direccion TEXT," + "contrasenia TEXT NOT NULL,"
				+ "especialidad TEXT NOT NULL," + "duracion_cita INTEGER NOT NULL);";

		String sqlc = "CREATE TABLE IF NOT EXISTS Cita (" + "id_cita INTEGER PRIMARY KEY AUTOINCREMENT,"
				+ "dni_paciente TEXT NOT NULL," + "dni_medico TEXT NOT NULL," + "fecha TEXT NOT NULL,"
				+ "hora TEXT NOT NULL," + "estado TEXT NOT NULL," + "motivo TEXT,"
				+ "FOREIGN KEY (dni_paciente) REFERENCES Paciente(dni) ON DELETE CASCADE,"
				+ "FOREIGN KEY (dni_medico) REFERENCES Medico(dni) ON DELETE CASCADE);";
		PreparedStatement ps;
		try {
			ps = con.prepareStatement(sqlp);
			ps.execute();
			ps = con.prepareStatement(sqlm);
			ps.execute();
			ps = con.prepareStatement(sqlc);
			ps.execute();
			ps.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	
	public boolean estaVacia() {
		String sql = "SELECT (SELECT COUNT(*) FROM Paciente) + (SELECT COUNT(*) FROM Medico);";
		boolean vacia = false;
		try (PreparedStatement ps = con.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
			vacia = rs.getInt(1) == 0;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return vacia;
	}


//	Inserta pacientes
	public boolean insertarPaciente(Paciente p) {
		String sql = "INSERT INTO Paciente(dni,nombre,apellidos,fecha_nacimiento,sexo,telefono,email,direccion,"
				+ "contrasenia) VALUES(?,?,?,?,?,?,?,?,?);";
		try (PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setString(1, p.getDni());
			ps.setString(2, p.getNombre());
			ps.setString(3, p.getApellidos());
			ps.setString(4, p.getFechaNacimiento().toString());
			ps.setString(5, p.getSexo().name());
			ps.setInt(6, p.getTelefono());
			ps.setString(7, p.getEmail());
			ps.setString(8, p.getDireccion());
			ps.setString(9, p.getContrasenia());
			ps.execute();
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

//	Inserta medicos
	public boolean insertarMedico(Medico m) {
		String sql = "INSERT INTO Medico(dni,nombre,apellidos,fecha_nacimiento,sexo,telefono,email,direccion,"
				+ "contrasenia,especialidad,duracion_cita) VALUES(?,?,?,?,?,?,?,?,?,?,?);";
		try (PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setString(1, m.getDni());
			ps.setString(2, m.getNombre());
			ps.setString(3, m.getApellidos());
			ps.setString(4, m.getFechaNacimiento().toString());
			ps.setString(5, m.getSexo().name());
			ps.setInt(6, m.getTelefono());
			ps.setString(7, m.getEmail());
			ps.setString(8, m.getDireccion());
			ps.setString(9, m.getContrasenia());
			ps.setString(10, m.getEspecialidad().name());
			ps.setInt(11, m.getDuracionCita());
			ps.execute();
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

//	Inserta citas
	public boolean insertarCita(Cita c) {
		String sql = "INSERT INTO Cita(dni_paciente,dni_medico,fecha,hora,estado,motivo) VALUES(?,?,?,?,?,?);";
		try (PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
			ps.setString(1, c.getPaciente().getDni());
			ps.setString(2, c.getMedico().getDni());
			ps.setString(3, c.getFechaCita().toString());
			ps.setString(4, c.getHora());
			ps.setString(5, c.getEstado().name());
			ps.setString(6, c.getMotivo());
			ps.execute();
			try (ResultSet rs = ps.getGeneratedKeys()) {
				if (rs.next()) {
					c.setIdCita(rs.getInt(1));
				}
			}
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}


//	Modifica la cita
	public boolean modificarCita(Cita c) {
		String sql = "UPDATE Cita SET dni_medico = ?, fecha = ?, hora = ?, estado = ?, motivo = ? WHERE id_cita = ?;";
		try (PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setString(1, c.getMedico().getDni());
			ps.setString(2, c.getFechaCita().toString());
			ps.setString(3, c.getHora());
			ps.setString(4, c.getEstado().name());
			ps.setString(5, c.getMotivo());
			ps.setInt(6, c.getIdCita());
			return ps.executeUpdate() > 0;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

//	Cambia el estado de la cita
	public boolean cambiarEstadoCita(int idCita, EstadoCita estado) {
		String sql = "UPDATE Cita SET estado = ? WHERE id_cita = ?;";
		try (PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setString(1, estado.name());
			ps.setInt(2, idCita);
			return ps.executeUpdate() > 0;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

//	Cambio de la contraseña de un paciente
	public boolean modificarContraseniaPaciente(String dni, String nuevaContrasenia) {
		String sql = "UPDATE Paciente SET contrasenia = ? WHERE dni = ?;";
		try (PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setString(1, nuevaContrasenia);
			ps.setString(2, dni);
			return ps.executeUpdate() > 0;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}


// Borrar la cita
	public boolean borrarCita(int idCita) {
		String sql = "DELETE FROM Cita WHERE id_cita = ?;";
		try (PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setInt(1, idCita);
			return ps.executeUpdate() > 0;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

//	Borra el paciente
	public boolean borrarPaciente(String dni) {
		String sql = "DELETE FROM Paciente WHERE dni = ?;";
		try (PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setString(1, dni);
			return ps.executeUpdate() > 0;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

//	Borra un medico
	public boolean borrarMedico(String dni) {
		String sql = "DELETE FROM Medico WHERE dni = ?;";
		try (PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setString(1, dni);
			return ps.executeUpdate() > 0;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}



//	obtiene los pacientes y los mete en un AL
	public List<Paciente> obtenerPacientes() {
		List<Paciente> lPacientes = new ArrayList<Paciente>();
		String sql = "SELECT * FROM Paciente;";
		try {
			PreparedStatement ps = con.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				Paciente p = new Paciente(rs.getString("dni"), rs.getString("nombre"), rs.getString("apellidos"),
						LocalDate.parse(rs.getString("fecha_nacimiento")), Sexo.valueOf(rs.getString("sexo")),
						rs.getInt("telefono"), rs.getString("email"), rs.getString("direccion"),
						rs.getString("contrasenia"));
				lPacientes.add(p);
			}
			rs.close();
			ps.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return lPacientes;
	}

//	obtiene los medicos y los mete en un AL
	public List<Medico> obtenerMedicos() {
		List<Medico> lMedicos = new ArrayList<Medico>();
		String sql = "SELECT * FROM Medico;";
		try {
			PreparedStatement ps = con.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				Medico m = new Medico(rs.getString("dni"), rs.getString("nombre"), rs.getString("apellidos"),
						LocalDate.parse(rs.getString("fecha_nacimiento")), Sexo.valueOf(rs.getString("sexo")),
						rs.getInt("telefono"), rs.getString("email"), rs.getString("direccion"),
						rs.getString("contrasenia"), Especialidad.valueOf(rs.getString("especialidad")),
						new ArrayList<Horario>(), rs.getInt("duracion_cita"));
				lMedicos.add(m);
			}
			rs.close();
			ps.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return lMedicos;
	}

// AL con todas las citas ordenadas
	public List<Cita> obtenerCitas(List<Paciente> lPacientes, List<Medico> lMedicos) {
		List<Cita> lCitas = new ArrayList<Cita>();
		String sql = "SELECT * FROM Cita ORDER BY fecha, hora;";
		try {
			PreparedStatement ps = con.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				Paciente paciente = null;
				for (Paciente p : lPacientes) {
					if (p.getDni().equals(rs.getString("dni_paciente"))) {
						paciente = p;
					}
				}
				Medico medico = null;
				for (Medico m : lMedicos) {
					if (m.getDni().equals(rs.getString("dni_medico"))) {
						medico = m;
					}
				}
				if (paciente != null && medico != null) {
					Cita c = new Cita(0, paciente, medico, LocalDate.parse(rs.getString("fecha")),
							EstadoCita.valueOf(rs.getString("estado")), rs.getString("hora"),
							rs.getString("motivo"));
					c.setIdCita(rs.getInt("id_cita"));
					lCitas.add(c);
				}
			}
			rs.close();
			ps.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return lCitas;
	}

//	Mira si ya existe el usuario
	public boolean existeUsuario(String email, String contrasenia) {
		String sql = "SELECT 1 FROM Paciente WHERE email = ? AND contrasenia = ? "
				+ "UNION SELECT 1 FROM Medico WHERE email = ? AND contrasenia = ?;";
		boolean existe = false;
		try (PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setString(1, email);
			ps.setString(2, contrasenia);
			ps.setString(3, email);
			ps.setString(4, contrasenia);
			ResultSet rs = ps.executeQuery();
			existe = rs.next();
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return existe;
	}

//	Devuelve las horas en las que el medico o el paciente ya tienen una cita
	public List<String> obtenerHorasOcupadas(String dniMedico, String dniPaciente, LocalDate fecha) {
		List<String> lHoras = new ArrayList<String>();
		String sql = "SELECT hora FROM Cita WHERE fecha = ? AND estado = ? AND (dni_medico = ? OR dni_paciente = ?);";
		try (PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setString(1, fecha.toString());
			ps.setString(2, EstadoCita.PENDIENTE.name());
			ps.setString(3, dniMedico);
			ps.setString(4, dniPaciente);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				lHoras.add(rs.getString(1));
			}
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return lHoras;
	}
}
