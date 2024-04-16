package es.ucm.fdi.iw.model;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;

import com.fasterxml.jackson.databind.JsonNode;

public class GoogleBookService {

    private static final String API_KEY = "&key=AIzaSyA3xTzKA7Ks5VrUuDwA2jakeazQxSky7Ts";
    private static final String URL = "https://www.googleapis.com/books/v1/volumes/?q=";
    private static final String LANGUAGE = "&langRestrict=es";


    public GoogleBookService(){}

    public String searchTerm(String term) {

        OkHttpClient client = new OkHttpClient();
        String url = URL + term + LANGUAGE + API_KEY;
        Request request = new Request.Builder()
            .url(url)
            .build();

        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        }
        catch (Exception e) {
            // TODO: handle exception
            return null;
        }
    }

    public Media parseGoogleBook(JsonNode resultNode) {
        Media media = new Media();

        System.out.println(resultNode);
        media.setApi("Google Books");
        media.setId(resultNode.get("id").asLong());
        media.setNombre(resultNode.get("volumeInfo").get("title").asText());
        media.setTipo("book");

        

        // Obtener el identificador ISBN_10
        for (JsonNode identifier : resultNode.get("volumeInfo").get("industryIdentifiers")) {
            if (identifier.get("type").asText().equals("ISBN_10")) {
                media.setDescripcion(media.getDescripcion() + "\nISBN-10: " + identifier.get("identifier").asText());
                break;
            }
        }

        // Si hay una descripción, incluirla
        if (resultNode.get("volumeInfo").has("description")) {
            media.setDescripcion(media.getDescripcion() + "\nDescripción: " + resultNode.get("volumeInfo").get("description").asText());
        }

        // Si existen enlaces de imágenes, obtener la URL de la miniatura
        if (resultNode.get("volumeInfo").has("imageLinks")) {
            media.setCoverImageUrl(resultNode.get("volumeInfo").get("imageLinks").get("thumbnail").asText());
            System.out.println(media.getCoverImageUrl()+ "EL ENLACE");
        }

        return media;
    }

}

