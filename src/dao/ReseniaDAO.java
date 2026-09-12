package dao;

import modelo.Resenia;

/**
 * Interfaz DAO para operaciones de reseñas.
 */
public interface ReseniaDAO {
    /**
     * Guarda una nueva reseña en la base de datos.
     *
     * @return true si la reseña se guardó correctamente, false en caso de error.
     */
    boolean guardar(Resenia resenia);

    /**
     * Verifica si existe una reseña de un usuario para una película.
     */
    boolean existeResena(int idUsuario, int idPelicula);
}