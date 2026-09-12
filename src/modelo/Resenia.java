package modelo;

public class Resenia {
    private int ID;
    private String comentario;
    private int calificacion;
    private Usuario usuario;
    private int IDContenido;

    /**
     * Constructor vacío.
     */
    public Resenia() { }

    /**
     * Constructor con parámetros de reseña.
     */
    public Resenia(Usuario usuario, int IDContenido, String comentario, int calificacion) {
        this.usuario = usuario;
        this.IDContenido = IDContenido;
        this.comentario = comentario;
        this.calificacion = calificacion;
    }

    /**
     * Obtiene el ID de la reseña.
     */
    public int getID() {
        return ID;
    }

    /**
     * Establece el ID de la reseña.
     */
    public void setID(int ID) {
        this.ID = ID;
    }

    /**
     * Obtiene el comentario de la reseña.
     */
    public String getComentario() {
        return comentario;
    }

    /**
     * Establece el comentario de la reseña.
     */
    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    /**
     * Obtiene la calificación de la reseña.
     */
    public int getCalificacion() {
        return calificacion;
    }

    /**
     * Establece la calificación de la reseña.
     */
    public void setCalificacion(int calificacion) {
        this.calificacion = calificacion;
    }

    /**
     * Obtiene el usuario que escribió la reseña.
     */
    public Usuario getUsuario() {
        return usuario;
    }

    /**
     * Establece el usuario que escribió la reseña.
     */
    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    /**
     * Obtiene el ID del contenido comentado.
     */
    public int getIDContenido() {
        return IDContenido;
    }

    /**
     * Establece el ID del contenido comentado.
     */
    public void setIDContenido(int iDContenido) {
        IDContenido = iDContenido;
    }
}