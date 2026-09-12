package servicio;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;
import org.json.JSONObject;

/**
 * Servicio para consultar la API de OMDb y obtener detalles de películas
 */
public class ConsultaPeliculasOMDb {

    private static final String URL_API_OMDB = "https://www.omdbapi.com/";

    /**
     * Consulta la API de OMDb por título exacto.
     * Retorna un JSONObject con los datos de la película si la encuentra.
     */
    public static JSONObject consultarPelicula(String titulo) {
        if (titulo == null || titulo.trim().isEmpty()) {
            return null;
        }

        String apiKey = obtenerApiKey();
        if (apiKey == null) {
            System.err.println("API key de OMDb no configurada.");
            System.err.println("Definí la variable de entorno OMDB_API_KEY o creá un archivo omdb.properties con la propiedad omdb.api.key.");
            return null;
        }

        try {
            String url = URL_API_OMDB + "?t=" + titulo.trim().replace(" ", "+") + "&apikey=" + apiKey;

            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            JSONObject json = new JSONObject(response.body());
            if (json.has("Response") && json.getString("Response").equals("True")) {
                return json;
            } else {
                System.out.println("Película no encontrada: " + titulo);
                return null;
            }

        } catch (Exception e) {
            System.err.println("Error al consultar la API: " + e.getMessage());
            return null;
        }
    }

    /**
     * Obtiene la API key de OMDb desde la variable de entorno OMDB_API_KEY
     * o, en su defecto, desde el archivo omdb.properties (propiedad omdb.api.key).
     */
    private static String obtenerApiKey() {
        String key = System.getenv("OMDB_API_KEY");
        if (key != null && !key.trim().isEmpty()) {
            return key.trim();
        }

        Path archivo = Path.of("omdb.properties");
        if (Files.exists(archivo)) {
            try (InputStream in = Files.newInputStream(archivo)) {
                Properties props = new Properties();
                props.load(in);
                String fromFile = props.getProperty("omdb.api.key");
                if (fromFile != null && !fromFile.trim().isEmpty()) {
                    return fromFile.trim();
                }
            } catch (IOException e) {
                System.err.println("No se pudo leer omdb.properties: " + e.getMessage());
            }
        }

        return null;
    }

    /**
     * Extrae título de la respuesta OMDb
     */
    public static String obtenerTitulo(JSONObject json) {
        if (json != null && json.has("Title")) {
            return json.getString("Title");
        }
        return "N/A";
    }

    /**
     * Extrae año de la respuesta OMDb
     */
    public static String obtenerAnio(JSONObject json) {
        if (json != null && json.has("Year")) {
            return json.getString("Year");
        }
        return "N/A";
    }

    /**
     * Extrae sinopsis/plot de la respuesta OMDb
     */
    public static String obtenerSinopsis(JSONObject json) {
        if (json != null && json.has("Plot")) {
            return json.getString("Plot");
        }
        return "N/A";
    }

    /**
     * Extrae rating de la respuesta OMDb
     */
    public static String obtenerRating(JSONObject json) {
        if (json != null && json.has("imdbRating")) {
            return json.getString("imdbRating");
        }
        return "N/A";
    }
}