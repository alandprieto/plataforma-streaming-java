package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import database.ConexionBD;
import modelo.Administrador;
import modelo.Cliente;
import modelo.Usuario;

/**
 * Implementación DAO para operaciones de Usuario en base de datos.
 */
public class UsuarioDAOimple implements UsuarioDAO {

    /**
     * Guarda un nuevo usuario en la base de datos.
     */
    @Override
    public boolean guardar(Usuario usuario) {
        String sql = "INSERT INTO Usuario (DNI, Nombre, Apellido, Email, Contrasena, TipoUsuario) VALUES (?, ?, ?, ?, ?, ?)";
        Connection conn = ConexionBD.getConnection();

        try (PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setLong(1, usuario.getDNI());
            pstmt.setString(2, usuario.getNombre());
            pstmt.setString(3, usuario.getApellido());
            pstmt.setString(4, usuario.getEmail());
            pstmt.setString(5, usuario.getContrasena());

            if (usuario instanceof Administrador) {
                pstmt.setString(6, "ADMIN");
            } else {
                pstmt.setString(6, "CLIENTE");
            }

            int filasAfectadas = pstmt.executeUpdate();

            if (filasAfectadas > 0) {
                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        usuario.setID(generatedKeys.getInt(1));
                    }
                }
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Error al guardar usuario: " + e.getMessage());
        }
        return false;
    }

    /**
     * Busca un usuario por su ID.
     */
    @Override
    public Usuario buscarPorId(int id) {
        String sql = "SELECT * FROM Usuario WHERE ID = ?";
        Connection conn = ConexionBD.getConnection();

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToUsuario(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar usuario por ID: " + e.getMessage());
        }
        return null;
    }

    /**
     * Lista todos los usuarios de la base de datos.
     */
    @Override
    public List<Usuario> listarTodos() {
        List<Usuario> usuarios = new ArrayList<>();
        String sql = "SELECT * FROM Usuario";
        Connection conn = ConexionBD.getConnection();

        try (Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Usuario usuario = mapResultSetToUsuario(rs);
                if (usuario != null) {
                    usuarios.add(usuario);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al listar usuarios: " + e.getMessage());
        }
        return usuarios;
    }

    /**
     * Elimina un usuario por su ID.
     */
    @Override
    public void eliminar(int id) {
        String sql = "DELETE FROM Usuario WHERE ID = ?";
        Connection conn = ConexionBD.getConnection();

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error al eliminar usuario: " + e.getMessage());
        }
    }

    /**
     * Autentica un usuario con email y contraseña.
     */
    @Override
    public Usuario autenticar(String email, String contrasena) {
        String sql = "SELECT * FROM Usuario WHERE Email = ? AND Contrasena = ?";
        Connection conn = ConexionBD.getConnection();

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, email);
            pstmt.setString(2, contrasena);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToUsuario(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error en autenticación: " + e.getMessage());
        }
        return null;
    }

    /**
     * Verifica si un DNI ya existe en la base de datos.
     */
    @Override
    public boolean dniExiste(long dni) {
        String sql = "SELECT 1 FROM Usuario WHERE DNI = ?";
        Connection conn = ConexionBD.getConnection();
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, dni);
            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            System.err.println("Error al verificar DNI: " + e.getMessage());
            return false;
        }
    }

    /**
     * Verifica si un email ya existe en la base de datos.
     */
    @Override
    public boolean emailExiste(String email) {
        String sql = "SELECT 1 FROM Usuario WHERE Email = ?";
        Connection conn = ConexionBD.getConnection();
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, email);
            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            System.err.println("Error al verificar email: " + e.getMessage());
            return false;
        }
    }

    /**
     * Verifica si el usuario ya vio el Top 10.
     */
    @Override
    public boolean haVistoTop10(int id) {
        String sql = "SELECT VioTop10 FROM Usuario WHERE ID = ?";
        Connection conn = ConexionBD.getConnection();
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    int val = rs.getInt("VioTop10");
                    return val == 1;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al consultar VioTop10: " + e.getMessage());
        }
        return false;
    }

    /**
     * Marca que el usuario vio el Top 10.
     */
    @Override
    public void marcarVioTop10(int id) {
        String sql = "UPDATE Usuario SET VioTop10 = 1 WHERE ID = ?";
        Connection conn = ConexionBD.getConnection();
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error al marcar VioTop10: " + e.getMessage());
        }
    }

    /**
     * Mapea un ResultSet a un objeto Usuario.
     */
    private Usuario mapResultSetToUsuario(ResultSet rs) throws SQLException {
        String tipo = rs.getString("TipoUsuario");
        Usuario usuario = null;

        if ("ADMIN".equalsIgnoreCase(tipo)) {
            usuario = new Administrador();
        } else {
            usuario = new Cliente();
        }

        usuario.setID(rs.getInt("ID"));
        usuario.setDNI(rs.getLong("DNI"));
        usuario.setNombre(rs.getString("Nombre"));
        usuario.setApellido(rs.getString("Apellido"));
        usuario.setEmail(rs.getString("Email"));
        usuario.setContrasena(rs.getString("Contrasena"));
        try {
            int v = rs.getInt("VioTop10");
            usuario.setVistoTop10(v == 1);
        } catch (SQLException ex) {
            System.err.println("No se pudo leer la columna VioTop10: " + ex.getMessage());
        }

        return usuario;
    }
}