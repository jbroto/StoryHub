package es.ucm.fdi.iw.model;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;

public class TMDBService {

    private static final String API_KEY = "?api_key=cba3b5b1f6b343e9fc31c5b787b270bd";
    private static final String URL = "https://api.themoviedb.org/3";
    private static final String MULTI_SEARCH = "/search/multi";
    private static final String MOVIE_SEARCH = "/search/movie";
    private static final String SERIES_SEARCH = "/search/tv";

    private static final String LANGUAGE = "&language=es-ES";

    public TMDBService() {
    }

    public String searchTerm(String term, String tipo) {

        OkHttpClient client = new OkHttpClient();
        String url = URL + MULTI_SEARCH + API_KEY + "&query=" + term + LANGUAGE; // busqueda general

        if (tipo.equalsIgnoreCase("tv")) {// Busqueda de series
            url = URL + SERIES_SEARCH + API_KEY + "&query=" + term + LANGUAGE;
        } else if (tipo.equalsIgnoreCase("movie")) { // Busqueda de peliculas
            url = URL + MOVIE_SEARCH + API_KEY + "&query=" + term + LANGUAGE;
        }

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
    
    // https://api.themoviedb.org/3/tv/66732?api_key=cba3b5b1f6b343e9fc31c5b787b270bd&language=es-ES&append_to_response=episode_groups
    // ejemplo para obtener todos los capitulos de srtanger things que tiene id
    // 66732

    public String obtenerCapitulos(Media season) {
        //detalles de una temporada: EJEMPLO con Stranger Things Temporada 1
        //https://api.themoviedb.org/3/tv/66732/season/1?api_key=cba3b5b1f6b343e9fc31c5b787b270bd&language=es-ES

        OkHttpClient client = new OkHttpClient();
        String url = URL + "/tv/" + season.getFather().getId() + "/season/" + season.getOrden()+ API_KEY + LANGUAGE;

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

        System.out.println("Nodo resultante: " + resultNode);
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
                    "https://i.pinimg.com/736x/3d/30/06/3d30061b6fb8477e90b1d5b87952c05a.jpg");
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
            m.setBackdropImageUrl(
                    "https://drexel.edu/~/media/Drexel/Core-Site-Group/Core/Images/admissions/visit-tours/virtual-background-downloads/blue-pattern.ashx");
        }

        String fecha = "0000-00-00";

        if (resultNode.has("release_date")) { // fecha de salida para peliculas
            fecha = resultNode.get("release_date").asText();
        } else if (resultNode.has("last_air_date")) { // ultimo capitulo emitido de una temporada
            fecha = resultNode.get("last_air_date").asText();
        } else if (resultNode.has("air_date")) { // capitulo emitido de una temporada
            fecha = resultNode.get("air_date").asText();
        } else if (resultNode.has("first_air_date")) { // capitulo emitido de una temporada
            fecha = resultNode.get("first_air_date").asText();
        }

        m.setFecha(fecha);

        int orden = 0;
        if (resultNode.has("season_number")) {
            orden = resultNode.get("season_number").asInt();
        }
        m.setOrden(orden);

        int episodios = 0;
        if (resultNode.has("number_of_episodes")) {
            episodios = resultNode.get("number_of_episodes").asInt();
        }
        m.setNumChild(episodios);

        m.setNumFavs(0);
        m.setNumVisto(0);
        m.setNumListas(0);
        m.setNumViendo(0);

        return m;
    }

    public List<Media> obtenerTemporadas(Media father, JsonNode resultNode) {
        List<Media> temporadas = new ArrayList<Media>();
        // Obtenemos el nodo JSON correspondiente al atributo "seasons"
        JsonNode seasonsNode = resultNode.get("seasons");
        if (seasonsNode != null && seasonsNode.isArray()) {
            // Iteramos sobre los elementos del nodo "seasons"
            for (JsonNode seasonNode : seasonsNode) {
                // Crear una instancia de la clase  Media (tipo season) y asignar los atributos
                Media season = new Media();
                season.setTipo("season");
                season.setId(seasonNode.get("id").asLong());
                season.setNombre(seasonNode.get("name").asText());
                season.setOrden(seasonNode.get("season_number").asInt());
                season.setFecha(seasonNode.get("air_date").asText());
                season.setNumChild(seasonNode.get("episode_count").asInt());
                season.setDescripcion(seasonNode.get("overview").asText());
                season.setCoverImageUrl("https://image.tmdb.org/t/p/original" + seasonNode.get("poster_path").asText());
                season.setBackdropImageUrl(father.getBackdropImageUrl());// tiene el mismo fondo que el padre
                season.setApi("TMDB");
                season.setRating(0.0);
                season.setNumFavs(0);
                season.setNumVisto(0);
                season.setNumListas(0);
                season.setNumViendo(0);
                father.addChild(season);// aqui definimos el padre de la temporada

                // Agregamos la temporada a la lista de temporadas
                temporadas.add(season);
            }
        }
        return temporadas;
    }

    public List<Media> obtenerListaDeCapitulos(Media father, JsonNode resultNode) {
        List<Media> capitulos = new ArrayList<Media>();
        // Obtenemos el nodo JSON correspondiente al atributo "seasons"
        JsonNode episodesNode = resultNode.get("episodes");
        if (episodesNode != null && episodesNode.isArray()) {
            // Iteramos sobre los elementos del nodo "seasons"
            for (JsonNode episodeNode : episodesNode) {
                // Crear una instancia de la clase Media (tipo episode) y asignar los atributos
                Media episode = new Media();
                episode.setTipo("episode");
                episode.setId(episodeNode.get("id").asLong());
                episode.setNombre(episodeNode.get("name").asText());
                episode.setOrden(episodeNode.get("episode_number").asInt());
                episode.setFecha(episodeNode.get("air_date").asText());
                episode.setNumChild(0);
                episode.setDescripcion(episodeNode.get("overview").asText());
                episode.setCoverImageUrl(father.getCoverImageUrl());// tiene el mismo poster que el padre
                episode.setApi("TMDB");
                episode.setBackdropImageUrl("https://image.tmdb.org/t/p/original" + episodeNode.get("still_path").asText());
                episode.setRating(0.0);
                episode.setNumFavs(0);
                episode.setNumVisto(0);
                episode.setNumListas(0);
                episode.setNumViendo(0);
                father.addChild(episode);// aqui definimos el padre de la temporada

                // Agregamos la temporada a la lista de temporadas
                capitulos.add(episode);
            }
        }
        return capitulos;
    }

  
}