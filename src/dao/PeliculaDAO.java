package dao;

import java.util.List;

import modelo.Pelicula;

/**
 * Interfaz DAO para operaciones de Pelicula.
 */
public interface PeliculaDAO {
    /**
     * Guarda una nueva película en la base de datos.
     */
    void guardar(Pelicula pelicula);

    /**
     * Lista todas las películas de la base de datos.
     */
    List<Pelicula> listarTodas();
}