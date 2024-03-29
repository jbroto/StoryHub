package es.ucm.fdi.iw.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.mapping.List;
import java.util.ArrayList;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestParam;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import es.ucm.fdi.iw.model.TMDBService;
import es.ucm.fdi.iw.model.Media;


/**
 * Non-authenticated requests only.
 */
@Controller
public class RootController {

    private static final Logger log = LogManager.getLogger(RootController.class);

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
            //m.setNombre(resultNode.get("title").asText());
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

    return "busqueda"; // Asegúrate de devolver un valor en caso de que la lógica no llegue al return anterior
}


    @GetMapping("/home")
    public String home(Model model) {
        return "user";
    }

    @GetMapping("/contenido")
    public String contenido(Model model) {
        return "contenido";
    }

    @GetMapping("/adminreport")
    public String adminreport(Model model) {
        return "adminreport";
    }
    

}
