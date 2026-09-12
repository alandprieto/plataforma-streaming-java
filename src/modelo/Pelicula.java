package modelo;

import java.time.Duration;
import java.util.List;

import enums.GeneroPelicula;

import java.nio.file.Path;

public class Pelicula extends Contenido {
    private Duration duracion;

    /**
     * Constructor con todos los parámetros de película.
     */
    public Pelicula(String titulo, GeneroPelicula genero, String sinopsis, Staff director, double puntaje, int vistas,
            Path video, List<Resenia> resenias, int ID, Duration duracion, int anio, double ratingPromedio,
            String posterURL) {
        super(titulo, genero, sinopsis, director, puntaje, vistas, video, resenias, ID, anio, ratingPromedio, posterURL);
        this.duracion = duracion;
    }

    /**
     * Constructor vacío.
     */
    public Pelicula() {
        super();
    }

    /**
     * Obtiene la duración de la película.
     */
    public Duration getDuracion() {
        return duracion;
    }

    /**
     * Establece la duración de la película.
     */
    public void setDuracion(Duration duracion) {
        this.duracion = duracion;
    }
}