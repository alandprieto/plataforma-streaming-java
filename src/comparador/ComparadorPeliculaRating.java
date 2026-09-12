package comparador;

import java.util.Comparator;
import modelo.Pelicula;

/**
 * Comparador de películas por rating promedio en orden descendente.
 */
public class ComparadorPeliculaRating implements Comparator<Pelicula> {
    /**
     * Compara dos películas por su rating promedio.
     */
    @Override
    public int compare(Pelicula p1, Pelicula p2) {
        return Double.compare(p2.getRatingPromedio(), p1.getRatingPromedio());
    }
}