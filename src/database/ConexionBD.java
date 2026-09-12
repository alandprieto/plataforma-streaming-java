package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Gestor de conexión a base de datos SQLite.
 */
public class ConexionBD {
    private static final String URL_SQLITE = "jdbc:sqlite:streaming.db";
    private static Connection connection = null;

    static {
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                cerrarConexion();
            }
        });
    }

    /**
     * Constructor privado para evitar instanciación.
     */
    private ConexionBD() {
    }

    /**
     * Obtiene la conexión a la base de datos SQLite (singleton).
     */
    public static Connection getConnection() {
        if (connection == null) {
            try {
                connection = DriverManager.getConnection(URL_SQLITE);
                System.out.println("Conexión a SQLite establecida con éxito.");
            } catch (SQLException e) {
                System.err.println("Error al conectar con la base de datos: " + e.getMessage());
                return null;
            }
        }
        return connection;
    }

    /**
     * Cierra la conexión a la base de datos.
     */
    public static void cerrarConexion() {
        if (connection != null) {
            try {
                connection.close();
                System.out.println("Conexión a SQLite cerrada.");
            } catch (SQLException e) {
                System.err.println("Error al cerrar la conexión a la base de datos: " + e.getMessage());
            }
        }
    }
}
