package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import database.ConexionBD;
import modelo.Resenia;

/**
 * Implementación DAO para operaciones de reseñas en base de datos.
 */
public class ReseniaDAOimple implements ReseniaDAO {

    /**
     * Guarda una nueva reseña en la base de datos.
     */
    @Override
    public boolean guardar(Resenia resenia) {
        String sql = "INSERT INTO Resena (UsuarioID, PeliculaID, Comentario, Puntaje, Aprobada, FechaHora) VALUES (?, ?, ?, ?, ?, ?)";
        Connection conn = ConexionBD.getConnection();
        if (conn == null) {
            System.err.println("Sin conexión a la base de datos.");
            return false;
        }

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, resenia.getUsuario().getID());
            pstmt.setInt(2, resenia.getIDContenido());
            pstmt.setString(3, resenia.getComentario());
            pstmt.setInt(4, resenia.getCalificacion());
            pstmt.setBoolean(5, true);
            pstmt.setString(6, LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME));
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al guardar reseña: " + e.getMessage());
            return false;
        }
    }

    /**
     * Verifica si existe una reseña de un usuario para una película.
     */
    @Override
    public boolean existeResena(int idUsuario, int idPelicula) {
        String sql = "SELECT 1 FROM Resena WHERE UsuarioID = ? AND PeliculaID = ?";
        Connection conn = ConexionBD.getConnection();
        if (conn == null) {
            System.err.println("Sin conexión a la base de datos.");
            return false;
        }
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idUsuario);
            pstmt.setInt(2, idPelicula);
            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            System.err.println("Error al verificar reseña existente: " + e.getMessage());
            return false;
        }
    }
}