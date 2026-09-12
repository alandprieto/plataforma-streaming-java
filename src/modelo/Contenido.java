package modelo;

import java.util.List;

import enums.GeneroPelicula;

import java.nio.file.Path;

public abstract class Contenido {
    private String titulo;
    private GeneroPelicula genero;
    private String sinopsis;
    private Staff director;
    private double puntaje;
    private int vistas;
    private Path video;
    private List<Resenia> resenias;
    private int ID;
    private int anio;
    private double ratingPromedio;
    private String posterURL;

    /**
     * Constructor vacío.
     */
    public Contenido() {
    }

    /**
     * Constructor con todos los parámetros del contenido.
     */
    public Contenido(String titulo, GeneroPelicula genero, String sinopsis, Staff director, Double puntaje, int vistas,
            Path video, List<Resenia> resenias, int ID, int anio, double ratingPromedio, String posterURL) {
        this.titulo = titulo;
        this.genero = genero;
        this.sinopsis = sinopsis;
        this.director = director;
        this.puntaje = puntaje;
        this.vistas = vistas;
        this.video = video;
        this.resenias = resenias;
        this.ID = ID;
        this.anio = anio;
        this.ratingPromedio = ratingPromedio;
        this.posterURL = posterURL;
    }

    /**
     * Obtiene el título del contenido.
     */
    public String getTitulo() {
        return titulo;
    }

    /**
     * Establece el título del contenido.
     */
    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    /**
     * Obtiene la sinopsis del contenido.
     */
    public String getSinopsis() {
        return sinopsis;
    }

    /**
     * Establece la sinopsis del contenido.
     */
    public void setSinopsis(String sinopsis) {
        this.sinopsis = sinopsis;
    }

    /**
     * Obtiene el director del contenido.
     */
    public Staff getDirector() {
        return director;
    }

    /**
     * Establece el director del contenido.
     */
    public void setDirector(Staff director) {
        this.director = director;
    }

    /**
     * Obtiene el puntaje promedio del contenido.
     */
    public double getPuntaje() {
        return puntaje;
    }

    /**
     * Calcula y actualiza el puntaje promedio basado en todas las reseñas.
     */
    public void setPuntaje() {
        if (resenias == null || resenias.isEmpty()) {
            this.puntaje = 0.0;
            return;
        }
        double sum = 0;
        for (Resenia resenia : resenias) {
            sum += resenia.getCalificacion();
        }
        this.puntaje = sum / resenias.size();
    }

    /**
     * Obtiene la cantidad de vistas del contenido.
     */
    public int getVistas() {
        return vistas;
    }

    /**
     * Establece la cantidad de vistas del contenido.
     */
    public void setVistas(int vistas) {
        this.vistas = vistas;
    }

    /**
     * Obtiene la ruta del archivo de video.
     */
    public Path getVideo() {
        return video;
    }

    /**
     * Establece la ruta del archivo de video.
     */
    public void setVideo(Path video) {
        this.video = video;
    }

    /**
     * Obtiene la lista de reseñas del contenido.
     */
    public List<Resenia> getResenias() {
        return resenias;
    }

    /**
     * Establece la lista de reseñas del contenido.
     */
    public void setResenias(List<Resenia> resenias) {
        this.resenias = resenias;
    }

    /**
     * Obtiene el género del contenido.
     */
    public GeneroPelicula getGenero() {
        return genero;
    }

    /**
     * Establece el género del contenido.
     */
    public void setGenero(GeneroPelicula genero) {
        this.genero = genero;
    }

    /**
     * Obtiene el ID del contenido.
     */
    public int getID() {
        return ID;
    }

    /**
     * Establece el ID del contenido.
     */
    public void setID(int iD) {
        ID = iD;
    }

    /**
     * Obtiene el año del contenido.
     */
    public int getAnio() {
        return anio;
    }

    /**
     * Establece el año del contenido.
     */
    public void setAnio(int anio) {
        this.anio = anio;
    }

    /**
     * Obtiene el rating promedio del contenido.
     */
    public double getRatingPromedio() {
        return ratingPromedio;
    }

    /**
     * Establece el rating promedio del contenido.
     */
    public void setRatingPromedio(double ratingPromedio) {
        this.ratingPromedio = ratingPromedio;
    }

    /**
     * Obtiene la URL del poster del contenido.
     */
    public String getPosterURL() {
        return posterURL;
    }

    /**
     * Establece la URL del poster del contenido.
     */
    public void setPosterURL(String posterURL) {
        this.posterURL = posterURL;
    }
}