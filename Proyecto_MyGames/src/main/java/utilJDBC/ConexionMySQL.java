package utilJDBC;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionMySQL {

	private static final String URL = "jdbc:mysql://localhost:3306/";
	private static final String BBDD = "thecartridgedb";
	private static final String USUARIO = "root";
	private static final String CLAVE = "76750075H";

	/**
	 * Método que crea una conexión y la devuelve
	 * 
	 * @return conexión
	 */
	public static Connection conectar() {
		Connection conexion = null;

		try {
			conexion = DriverManager.getConnection(URL + BBDD, USUARIO, CLAVE);
			System.out.println("Conexion OK");
		} catch (SQLException e) {
			System.out.println("Error en la conexion");
			e.printStackTrace();
		}

		return conexion;
	}
}