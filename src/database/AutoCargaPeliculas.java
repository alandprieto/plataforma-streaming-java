package database;

import servicio.AppImple;
import modelo.Pelicula;
import modelo.Staff;
import enums.GeneroPelicula;
import java.io.BufferedReader;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Carga automáticamente las películas desde un archivo CSV a la base de datos.
 */
public class AutoCargaPeliculas {

    /**
     * Carga películas desde CSV si la base de datos está vacía.
     */
    public static void cargarSiExiste(AppImple servicio) {
        if (servicio.hayPeliculasCargadas()) {
            System.out.println(">>> Base de datos con datos. No se requiere carga CSV.");
            return;
        }

        System.out.println(">>> Base de datos vacía. Iniciando búsqueda de CSV...");

        File f = new File("src/database/movies_database.csv");
        if (!f.exists()) {
            f = new File("database/movies_database.csv");
            if (!f.exists()) {
                f = new File("movies_database.csv");
            }
        }

        if (!f.exists()) {
            System.err.println(">>> ERROR CRÍTICO: No se encuentra 'movies_database.csv'.");
            System.err.println(">>> Asegurate de que el archivo esté en la carpeta 'src/database/'");
            System.err.println(">>> Ruta buscada: " + f.getAbsolutePath());
            return;
        }

        System.out.println(">>> Archivo encontrado en: " + f.getAbsolutePath());

        Connection conn = ConexionBD.getConnection();
        boolean previousAutoCommit = true;
        try {
            if (conn != null) {
                previousAutoCommit = conn.getAutoCommit();
                conn.setAutoCommit(false);
            }
        } catch (SQLException se) {
            System.err.println("No se pudo desactivar autoCommit: " + se.getMessage());
        }

        int cargadas = 0;
        int totales = 0;
        int saltadas = 0;

        try (BufferedReader br = Files.newBufferedReader(f.toPath(), StandardCharsets.UTF_8)) {
            String linea = br.readLine();

            while ((linea = br.readLine()) != null) {
                totales++;
                List<String> datos = parseCSVLine(linea);

                if (datos.size() < 8) {
                    saltadas++;
                    continue;
                }

                try {
                    Pelicula p = new Pelicula();

                    p.setTitulo(datos.get(1).replace("\"", "").trim());

                    try {
                        p.setAnio(Integer.parseInt(datos.get(0).split("-")[0]));
                    } catch (Exception e) {
                        p.setAnio(2022);
                    }

                    try {
                        p.setRatingPromedio(Double.parseDouble(datos.get(5)));
                    } catch (Exception e) {
                        p.setRatingPromedio(5.0);
                    }

                    String generoRaw = datos.get(7).replace("\"", "").trim().toUpperCase().split(",")[0].replace(" ", "_");
                    try {
                        if (generoRaw.contains("ACTION"))
                            p.setGenero(GeneroPelicula.ACCION);
                        else if (generoRaw.contains("COMEDY"))
                            p.setGenero(GeneroPelicula.COMEDIA);
                        else if (generoRaw.contains("DRAMA"))
                            p.setGenero(GeneroPelicula.DRAMA);
                        else if (generoRaw.contains("SCIFI") || generoRaw.contains("SCIENCE"))
                            p.setGenero(GeneroPelicula.CIENCIA_FICCION);
                        else if (generoRaw.contains("HORROR"))
                            p.setGenero(GeneroPelicula.TERROR);
                        else
                            p.setGenero(GeneroPelicula.OTRO);
                    } catch (Exception ex) {
                        p.setGenero(GeneroPelicula.OTRO);
                    }

                    if (datos.size() > 8) {
                        p.setPosterURL(datos.get(8).replace("\"", "").trim());
                    } else {
                        p.setPosterURL("");
                    }

                    p.setDuracion(java.time.Duration.ofMinutes(120));
                    p.setDirector(new Staff("Director Desconocido", "Director"));

                    servicio.registrarPelicula(p);
                    cargadas++;
                    
                    if (cargadas % 100 == 0) {
                        System.out.println(">>> Progreso: " + cargadas + " películas cargadas...");
                    }
                } catch (Exception ex) {
                    saltadas++;
                }
            }

            try {
                if (conn != null)
                    conn.commit();
            } catch (SQLException se) {
                System.err.println("Error haciendo commit: " + se.getMessage());
            }

            System.out.println(">>> LÍNEAS LEÍDAS: " + totales + ", CARGADAS: " + cargadas + ", SALTADAS: " + saltadas);

        } catch (Exception e) {
            System.err.println("Error al leer el archivo CSV: " + e.getMessage());
            try {
                if (conn != null)
                    conn.rollback();
            } catch (SQLException se) {
                System.err.println("Error haciendo rollback: " + se.getMessage());
            }
        } finally {
            try {
                if (conn != null)
                    conn.setAutoCommit(previousAutoCommit);
            } catch (SQLException se) {
                System.err.println("No se pudo restaurar autoCommit: " + se.getMessage());
            }
        }
    }

    /**
     * Parsea una línea de CSV manejando comillas y comas dentro de campos.
     */
    private static List<String> parseCSVLine(String line) {
        List<String> result = new ArrayList<>();
        if (line == null)
            return result;
        StringBuilder cur = new StringBuilder();
        boolean inQuotes = false;
        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);
            if (c == '"') {
                if (inQuotes && i + 1 < line.length() && line.charAt(i + 1) == '"') {
                    cur.append('"');
                    i++;
                } else {
                    inQuotes = !inQuotes;
                }
            } else if (c == ',' && !inQuotes) {
                result.add(cur.toString());
                cur.setLength(0);
            } else {
                cur.append(c);
            }
        }
        result.add(cur.toString());
        return result;
    }
}