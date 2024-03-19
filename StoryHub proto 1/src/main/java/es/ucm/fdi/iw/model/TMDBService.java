package es.ucm.fdi.iw.model;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;

public class TMDBService {

    private static final String API_KEY = "?api_key=cba3b5b1f6b343e9fc31c5b787b270bd";
    private static final String URL = "https://api.themoviedb.org/3";
    private static final String MULTI_SEARCH = "/search/multi";
    private static final String LANGUAGE = "&language=es-ES";

    public TMDBService(){    }

    public String searchTerm(String term) {

        OkHttpClient client = new OkHttpClient();
        String url = URL + MULTI_SEARCH + API_KEY + "?query=" + term + LANGUAGE;
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
}