package es.ucm.fdi.iw.model;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class GoogleBookService {

    private static final String API_KEY = "&key=AIzaSyA3xTzKA7Ks5VrUuDwA2jakeazQxSky7Ts";
    private static final String URL = "https://www.googleapis.com/books/v1/volumes/?q=";
    private static final String LANGUAGE = "&language:es";
//https://www.googleapis.com/books/v1/volumes?q=intitle:harry+potter&language:es&key=AIzaSyA3xTzKA7Ks5VrUuDwA2jakeazQxSky7Ts
    public GoogleBookService() {
    }


    //https://www.googleapis.com/books/v1/volumes?q=isbn:1781101310&lang=es-ES&key=AIzaSyA3xTzKA7Ks5VrUuDwA2jakeazQxSky7Ts
    public String searchTerm(String term) {

        OkHttpClient client = new OkHttpClient();
        String url = URL  +"intitle:"+term + LANGUAGE + API_KEY;
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

    public Media getBookByISBN(long isbn){
       

        OkHttpClient client = new OkHttpClient();
        String url = URL + "isbn:"+isbn + LANGUAGE + API_KEY;
        Request request = new Request.Builder()
                .url(url)
                .build();

        try (Response response = client.newCall(request).execute()) {
            
            ObjectMapper objectMapper = new ObjectMapper();
			JsonNode resultNode = objectMapper.readTree(response.body().string());
            JsonNode resultado = resultNode.get("items");
        
            Media media = parseGoogleBook(resultado.get(0));
            return media;

            
        } catch (Exception e) {
            // TODO: handle exception
            return null;
        }

        
    }
   

    public Media parseGoogleBook(JsonNode resultNode) {
        Media media = new Media();

        System.out.println(resultNode + " el putisimo book");
        media.setApi("Google Books");
        // media.setId(resultNode.get("id").asLong());
        media.setNombre(resultNode.get("volumeInfo").get("title").asText());
        media.setTipo("book");

        JsonNode industryIdentifiersNode = resultNode.get("volumeInfo").get("industryIdentifiers");
        System.out.println(industryIdentifiersNode);
        if (industryIdentifiersNode != null) {
            for (JsonNode identifier : industryIdentifiersNode) {
                String isbn = identifier.get("identifier").asText();
                try {
                    long id = Long.parseLong(isbn);
                    // Si la conversión tiene éxito, asigna el valor a la propiedad id
                    media.setId(id);
                    break;
                } catch (NumberFormatException e) {
                    System.out.println("ESTE ISBN NO PUEDE SER DE TIPO LONG: " + isbn);
                    continue;//continuamos buscando otro isbn
                }
            }
        } else {
            // Manejo de caso donde no hay identificadores de industria disponibles
        }

        // Si hay una descripción, incluirla
        if (resultNode.get("volumeInfo").has("description")) {
            media.setDescripcion(resultNode.get("volumeInfo").get("description").asText());
        }

        // Si existen enlaces de imágenes, obtener la URL de la miniatura
        if (resultNode.get("volumeInfo").has("imageLinks")) {
            media.setCoverImageUrl(resultNode.get("volumeInfo").get("imageLinks").get("thumbnail").asText());
            System.out.println(media.getCoverImageUrl() + "EL ENLACE");
        }
        media.setBackdropImageUrl("https://i.pinimg.com/originals/67/18/22/671822c2f63dd5f65d8fd15c9710420b.jpg");

        media.setRating(0.0);
        media.setNumFavs(0);
        media.setNumVisto(0);
        media.setNumListas(0);
        media.setNumViendo(0);

        return media;
    }

}
