package es.ucm.fdi.iw.model;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;

import com.fasterxml.jackson.databind.JsonNode;

public class TMDBService {

    private static final String API_KEY = "?api_key=cba3b5b1f6b343e9fc31c5b787b270bd";
    private static final String URL = "https://api.themoviedb.org/3";
    private static final String MULTI_SEARCH = "/search/multi";
    private static final String LANGUAGE = "&language=es-ES";

    public TMDBService() {
    }

    public String searchTerm(String term) {

        OkHttpClient client = new OkHttpClient();
        String url = URL + MULTI_SEARCH + API_KEY + "&query=" + term + LANGUAGE;
        Request request = new Request.Builder()
                .url(url)
                .build();

        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        } catch (Exception e) {
            // TODO: handle exception
            return null;
        }
    }

    public String obtenerContenido(String tipo, long id) {
        OkHttpClient client = new OkHttpClient();
        String url = URL + "/" + tipo + "/" + id + API_KEY + LANGUAGE;

        Request request = new Request.Builder()
                .url(url)
                .build();

        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Media parseTMDBtoMedia(JsonNode resultNode) {
        Media m = new Media();

        System.out.println(resultNode + " el putisimo tmdb");
        m.setApi("TMDB");
        m.setId(resultNode.get("id").asLong());
        // Verificamos si el campo "title" esta
        if (resultNode.has("title")) {
            m.setNombre(resultNode.get("title").asText());
        } else if (resultNode.has("name")) { // Verificamos si el campo "name" esta
            m.setNombre(resultNode.get("name").asText());
        } else {
            // Si ninguno de los campos está, podemos poner que no tiene nombre
            m.setNombre("Nombre no disponible");
        }

        JsonNode posterNode = resultNode.get("poster_path");
        if (posterNode != null && !posterNode.isNull()) {
            // El campo poster_path no es nulo, por lo tanto, no necesitas cambiar la imagen
            // de la portada
            m.setCoverImageUrl("https://image.tmdb.org/t/p/original" + posterNode.asText());
        } else {
            // El campo poster_path es nulo, por lo tanto, establece una imagen de reemplazo
            m.setCoverImageUrl(
                    "https://static.vecteezy.com/system/resources/thumbnails/007/126/739/small/question-mark-icon-free-vector.jpg");
        }

        m.setRating(0.0);
        // cuando hacemos la busqueda de un contenido en concreto, puede no cotener el
        // campo de tipo
        m.setTipo(resultNode.has("media_type") ? resultNode.get("media_type").asText() : "Tipo no disponible");
        // si tiene descripción la insertamos
        m.setDescripcion(resultNode.has("overview") ? resultNode.get("overview").asText() : "Sin descripción :(");
        // si tiene imagen de fondo la insertamos

        JsonNode bgNode = resultNode.get("backdrop_path");
        if (bgNode != null && !bgNode.isNull()) {
            m.setBackdropImageUrl("https://image.tmdb.org/t/p/original" + bgNode.asText());
            
        } else {
            m.setBackdropImageUrl("img/portada.jpg");
        }
        m.setNumFavs(0);
        m.setNumVisto(0);
        m.setNumListas(0);
        m.setNumViendo(0);
        
        return m;
    }

    public String obtenerCapitulos(long id) {

        // https://api.themoviedb.org/3/tv/66732?api_key=cba3b5b1f6b343e9fc31c5b787b270bd&language=es-ES&append_to_response=episode_groups
        // ejemplo para obtener todos los capitulos de srtanger things que tiene id
        // 66732

        OkHttpClient client = new OkHttpClient();
        String url = URL + "/tv/" + id + API_KEY + LANGUAGE + "&append_to_response=episode_groups";

        Request request = new Request.Builder()
                .url(url)
                .build();

        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

    }
}