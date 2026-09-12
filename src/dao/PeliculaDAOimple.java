package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import database.ConexionBD;
import enums.GeneroPelicula;
import modelo.Pelicula;
import modelo.Staff;

/**
 * Implementación DAO para operaciones de Pelicula en base de datos.
 */
public class PeliculaDAOimple implements PeliculaDAO {

    /**
     * Guarda una nueva película en la base de datos.
     */
    @Override
    public void guardar(Pelicula pelicula) {
        String sql = "INSERT INTO Pelicula (Genero, Titulo, Director, DuracionMinutos, Anio, RatingPromedio, PosterURL) VALUES (?, ?, ?, ?, ?, ?, ?)";
        Connection conn = ConexionBD.getConnection();
        if (conn == null) {
            System.err.println("Sin conexión a la base de datos.");
            return;
        }
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, pelicula.getGenero() != null ? pelicula.getGenero().name() : "OTRO");
            pstmt.setString(2, pelicula.getTitulo());
            pstmt.setString(3, pelicula.getDirector() != null ? pelicula.getDirector().getNombre() : "Desc.");
            pstmt.setLong(4, pelicula.getDuracion() != null ? pelicula.getDuracion().toMinutes() : 90);
            pstmt.setInt(5, pelicula.getAnio());
            pstmt.setDouble(6, pelicula.getRatingPromedio());
            pstmt.setString(7, pelicula.getPosterURL());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error guardando película: " + e.getMessage());
        }
    }

    /**
     * Lista todas las películas ordenadas por rating descendente.
     */
    @Override
    public List<Pelicula> listarTodas() {
        List<Pelicula> peliculas = new ArrayList<>();
        Connection conn = ConexionBD.getConnection();
        if (conn == null) {
            System.err.println("Sin conexión a la base de datos.");
            return peliculas;
        }
        try (Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT * FROM Pelicula ORDER BY RatingPromedio DESC")) {
            while (rs.next()) {
                peliculas.add(mapResultSetToPelicula(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error listando películas: " + e.getMessage());
        }
        return peliculas;
    }

    /**
     * Verifica si hay al menos una película registrada sin cargar los datos.
     */
    @Override
    public boolean existePelicula() {
        Connection conn = ConexionBD.getConnection();
        if (conn == null) {
            System.err.println("Sin conexión a la base de datos.");
            return false;
        }
        String sql = "SELECT 1 FROM Pelicula LIMIT 1";
        try (Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {
            return rs.next();
        } catch (SQLException e) {
            System.err.println("Error al verificar películas: " + e.getMessage());
            return false;
        }
    }

    /**
     * Busca películas cuyo título contenga el texto indicado.
     * Usa LIKE sobre PreparedStatement (seguro) y escapa comodines.
     */
    @Override
    public List<Pelicula> buscarPorTitulo(String titulo) {
        List<Pelicula> peliculas = new ArrayList<>();
        Connection conn = ConexionBD.getConnection();
        if (conn == null) {
            System.err.println("Sin conexión a la base de datos.");
            return peliculas;
        }

        String sql = "SELECT * FROM Pelicula WHERE Titulo LIKE ? ESCAPE '\\' COLLATE NOCASE ORDER BY RatingPromedio DESC LIMIT 24";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, "%" + escaparComodines(titulo) + "%");
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    peliculas.add(mapResultSetToPelicula(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error buscando películas: " + e.getMessage());
        }
        return peliculas;
    }

    /**
     * Escapa los comodines de LIKE para que % y _ se traten como texto literal.
     */
    private String escaparComodines(String valor) {
        return valor.replace("\\", "\\\\").replace("%", "\\%").replace("_", "\\_");
    }

    /**
     * Mapea una fila de ResultSet a un objeto Pelicula.
     */
    private Pelicula mapResultSetToPelicula(ResultSet rs) throws SQLException {
        Pelicula p = new Pelicula();
        p.setID(rs.getInt("ID"));
        p.setTitulo(rs.getString("Titulo"));
        try {
            p.setGenero(GeneroPelicula.valueOf(rs.getString("Genero")));
        } catch (Exception e) {
            p.setGenero(GeneroPelicula.OTRO);
        }
        p.setDirector(new Staff(rs.getString("Director"), "Director"));
        p.setDuracion(Duration.ofMinutes(rs.getLong("DuracionMinutos")));
        p.setAnio(rs.getInt("Anio"));
        p.setRatingPromedio(rs.getDouble("RatingPromedio"));
        p.setPosterURL(rs.getString("PosterURL"));
        return p;
    }
}