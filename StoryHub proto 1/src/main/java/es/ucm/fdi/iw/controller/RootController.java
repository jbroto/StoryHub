package es.ucm.fdi.iw.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.mapping.List;
import java.util.ArrayList;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestParam;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import es.ucm.fdi.iw.model.TMDBService;
import es.ucm.fdi.iw.model.User;
import es.ucm.fdi.iw.model.Media;

/**
 * Non-authenticated requests only.
 */
@Controller
public class RootController {

    private static final Logger log = LogManager.getLogger(RootController.class);

    @Autowired
    private EntityManager entityManager;

    @GetMapping("/login")
    public String login(Model model) {
        return "login";
    }

    @GetMapping("/")
    public String index(Model model) {
        return "index";
    }

    @GetMapping("/busqueda")
    public String busqueda(@RequestParam("paramBusqueda") String paramBusqueda, Model model) {
        System.out.println(paramBusqueda + '\n' + '\n');
        TMDBService s = new TMDBService();
        String result = s.searchTerm(paramBusqueda);

        System.out.println("ESTOY ENTRANDO AQUÍ");

        try {
            // lo parseamos tipo JSON
            ObjectMapper objectMapper = new ObjectMapper();

            JsonNode rootNode = objectMapper.readTree(result);

            // Obtener la matriz "results"
            JsonNode resultsNode = rootNode.get("results");

            ArrayList<Media> lista = new ArrayList<>();

            System.out.println("AQUI TAMBIEN LLEGO");

            // Iterar sobre los elementos de la matriz "results"
            for (JsonNode resultNode : resultsNode) {
                System.out.println("APAAAAAA");
                System.out.println(resultNode.get("id").asLong());
                Media m = new Media();
                m.setApi(result);
                m.setId(resultNode.get("id").asLong());
                // Verificamos si el campo "title" esta
                if (resultNode.has("title")) {
                    m.setNombre(resultNode.get("title").asText());
                } else if (resultNode.has("name")) { // Verificamos si el campo "name" esta
                    m.setNombre(resultNode.get("name").asText());
                } else {
                    // Si ninguno de los campos está, podemos poner que no tiene nombre
                    m.setNombre(paramBusqueda);
                }

                m.setCoverImageUrl(resultNode.get("poster_path").asText());
                m.setRating(resultNode.get("vote_average").asDouble());
                m.setTipo(resultNode.get("media_type").asText());

                lista.add(m);
            }

            System.out.println(lista);
            model.addAttribute("resultado", lista);
            model.addAttribute("result", result);
            return "busqueda";

        } catch (Exception e) {
            e.printStackTrace();
        }

        return "busqueda"; // Asegúrate de devolver un valor en caso de que la lógica no llegue al return
                           // anterior
    }

    @GetMapping("/home")
    public String home(Model model) {
        return "user";
    }

    @GetMapping("/{idUs}/contenido")
    public String contenido(@RequestParam("tipo") String tipo, @PathVariable long idUs, @RequestParam("id") Long id,
            Model model) {
        System.out.println(tipo + '\n' + id + '\n'); // quitar, solo para comprobar en debug
        TMDBService s = new TMDBService();

        // Llamar al servicio para obtener los detalles del contenido
        String resultado = s.obtenerContenido(tipo, id);
        String descripcion = "";
        String backdropImageUrl = "https://image.tmdb.org/t/p/original";

        User usuario = entityManager.find(User.class, idUs);
        model.addAttribute("user", usuario);
        model.addAttribute("Listas", usuario.getListas());

        try {
            // lo parseamos tipo JSON
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode resultNode = objectMapper.readTree(resultado);

            Media m = new Media();
            m.setApi(resultado);
            m.setId(id);
            // Verificamos si el campo "title" esta
            if (resultNode.has("title")) {
                m.setNombre(resultNode.get("title").asText());
            } else if (resultNode.has("name")) { // Verificamos si el campo "name" esta
                m.setNombre(resultNode.get("name").asText());
            } else {
                // Si ninguno de los campos está, podemos poner que no tiene nombre
                m.setNombre("Nombre no disponible");
            }
            m.setCoverImageUrl(resultNode.get("poster_path").asText());
            m.setRating(resultNode.get("vote_average").asDouble());
            m.setTipo(tipo);

            descripcion = resultNode.get("overview").asText();
            backdropImageUrl += resultNode.get("backdrop_path").asText();

            // Agregamos los detalles del contenido al modelo
            model.addAttribute("media", m);
            model.addAttribute("descripcion", descripcion);
            model.addAttribute("fondo", backdropImageUrl);

            // Devolvemos el nombre de la vista a la que se redirigirá
            return "contenido";

        } catch (Exception e) {
            e.printStackTrace();
        }
        return "contenido";// en caso de que no llegue al otro return
    }

    @GetMapping("/adminreport")
    public String adminreport(Model model) {
        return "adminreport";
    }

}
