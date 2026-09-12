package database;

import util.PasswordUtil;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Inicializa y configura la estructura de la base de datos.
 */
public class SetupBD {

    private static final String CREATE_USUARIO = "CREATE TABLE IF NOT EXISTS Usuario (" +
            "  ID INTEGER PRIMARY KEY AUTOINCREMENT," +
            "  DNI LONG NOT NULL UNIQUE," +
            "  Nombre VARCHAR(100) NOT NULL," +
            "  Apellido VARCHAR(100) NOT NULL," +
            "  Email VARCHAR(150) NOT NULL UNIQUE," +
            "  Contrasena VARCHAR(200) NOT NULL," +
            "  TipoUsuario VARCHAR(20) NOT NULL" +
            ");";

    private static final String CREATE_PELICULA = "CREATE TABLE IF NOT EXISTS Pelicula (" +
            "  ID INTEGER PRIMARY KEY AUTOINCREMENT," +
            "  Titulo VARCHAR(255) NOT NULL," +
            "  Genero VARCHAR(50)," +
            "  Director VARCHAR(150)," +
            "  DuracionMinutos INT," +
            "  Anio INT," +
            "  RatingPromedio FLOAT," +
            "  PosterURL VARCHAR(255)" +
            ");";

    private static final String CREATE_RESENA = "CREATE TABLE IF NOT EXISTS Resena (" +
            "  ID INTEGER PRIMARY KEY AUTOINCREMENT," +
            "  UsuarioID INT NOT NULL," +
            "  PeliculaID INT NOT NULL," +
            "  Comentario TEXT," +
            "  Puntaje INT NOT NULL," +
            "  Aprobada BOOLEAN DEFAULT 0," +
            "  FechaHora TEXT," +
            "  FOREIGN KEY(UsuarioID) REFERENCES Usuario(ID)," +
            "  FOREIGN KEY(PeliculaID) REFERENCES Pelicula(ID)" +
            ");";

    /**
     * Crea las tablas necesarias en la base de datos si no existen.
     */
    public static void crearTablas() {
        Connection conn = ConexionBD.getConnection();
        if (conn == null) {
            System.err.println("No se pudo crear tablas. Conexión nula.");
            return;
        }

        try (Statement stmt = conn.createStatement()) {
            System.out.println("Verificando/creando tablas...");
            stmt.execute(CREATE_USUARIO);
            stmt.execute(CREATE_PELICULA);
            stmt.execute(CREATE_RESENA);
            System.out.println("Tablas creadas o ya existentes.");

            boolean tieneVioTop10 = false;
            try (ResultSet rs = stmt.executeQuery("PRAGMA table_info(Usuario);")) {
                while (rs.next()) {
                    String nombreCol = rs.getString("name");
                    if ("VioTop10".equalsIgnoreCase(nombreCol)) {
                        tieneVioTop10 = true;
                        break;
                    }
                }
            }

            if (!tieneVioTop10) {
                try {
                    stmt.execute("ALTER TABLE Usuario ADD COLUMN VioTop10 INTEGER DEFAULT 0;");
                    System.out.println("Columna VioTop10 añadida a Usuario.");
                } catch (SQLException ex) {
                    System.err.println("No se pudo añadir columna VioTop10: " + ex.getMessage());
                }
            }

        } catch (SQLException e) {
            System.err.println("Error al crear las tablas: " + e.getMessage());
        }

        crearUsuariosIniciales(conn);
    }

    /**
     * Crea las cuentas de prueba documentadas en el README si no existen.
     */
    private static void crearUsuariosIniciales(Connection conn) {
        insertarSiNoExiste(conn, "alan@gmail.com", "12345678", "CLIENTE", 36123456L, "Alan", "Prieto");
        insertarSiNoExiste(conn, "admin1@streaming.com", "admin123", "ADMIN", 10000001L, "Admin", "Streaming");
    }

    /**
     * Inserta un usuario de prueba solo si su email no está registrado.
     */
    private static void insertarSiNoExiste(Connection conn, String email, String contrasena,
            String tipoUsuario, long dni, String nombre, String apellido) {
        if (emailExiste(conn, email)) {
            return;
        }

        String sql = "INSERT INTO Usuario (DNI, Nombre, Apellido, Email, Contrasena, TipoUsuario) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, dni);
            pstmt.setString(2, nombre);
            pstmt.setString(3, apellido);
            pstmt.setString(4, email);
            pstmt.setString(5, PasswordUtil.hash(contrasena));
            pstmt.setString(6, tipoUsuario);
            pstmt.executeUpdate();
            System.out.println("Usuario inicial creado: " + email);
        } catch (SQLException e) {
            System.err.println("Error al crear usuario inicial " + email + ": " + e.getMessage());
        }
    }

    private static boolean emailExiste(Connection conn, String email) {
        String check = "SELECT 1 FROM Usuario WHERE Email = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(check)) {
            pstmt.setString(1, email);
            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            System.err.println("Error al verificar email inicial: " + e.getMessage());
            return true;
        }
    }
}