package es.ucm.fdi.iw.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestParam;

import es.ucm.fdi.iw.model.TMDBService;


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
        System.out.println(paramBusqueda+'\n'+'\n');
        TMDBService s = new TMDBService();
        String result = s.searchTerm(paramBusqueda);
        if(result != null){
            System.out.println(result);
        }
        return "busqueda";
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
