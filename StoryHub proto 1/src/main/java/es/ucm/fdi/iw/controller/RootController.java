package es.ucm.fdi.iw.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.mapping.List;
import java.util.ArrayList;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestParam;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import es.ucm.fdi.iw.model.TMDBService;
import es.ucm.fdi.iw.model.User;
import es.ucm.fdi.iw.model.Lista;
import es.ucm.fdi.iw.model.Media;

/**
 * Non-authenticated requests only.
 */
@Controller
public class RootController {

    private static final Logger log = LogManager.getLogger(RootController.class);

    @Autowired
    private EntityManager entityManager;

    @Autowired
	private PasswordEncoder passwordEncoder;

    public String encodePassword(String rawPassword) {
		return passwordEncoder.encode(rawPassword);
	}

    @GetMapping("/login")
    public String login(Model model) {
        return "login";
    }

    @GetMapping("/")
    public String index(Model model) {
        return "index";
    }


    @GetMapping("/home")
    public String home(Model model) {
        return "user";
    }

    @GetMapping("/registro")
    public String registro(Model model) {
        model.addAttribute("User", new User());
        return "registro";
    }

    @PostMapping("/registro")
    @Transactional
    public String registroSubmit(@ModelAttribute User user, Model model) {
        try {
            User target = new User();
            target.setFirstName(user.getFirstName());
            target.setLastName(user.getLastName());
            target.setUsername(user.getUsername());
            target.setPassword(encodePassword(user.getPassword()));
            target.setEnabled(true);
            target.setRoles("USER");
            entityManager.persist(target);
            Lista favs = new Lista(target,"favoritos",false,"");
            Lista viewing = new Lista(target,"viendo",false,"");
            Lista ended = new Lista(target,"terminado",false,"");
            entityManager.persist(favs);
            entityManager.persist(viewing);
            entityManager.persist(ended);

            entityManager.flush(); // forces DB to add user & assign valid id            
            // Agregar un mensaje de registro exitoso al modelo
            model.addAttribute("registroExitoso", "Registro realizado con exito :)");
            
            // Redirigir a la página de inicio de sesión con el mensaje de éxito
            return "login";
        } catch (Exception e) {
            // En caso de error, agregar un mensaje de error al modelo
            //tambien agragamos un nuevo user para rellenar el formulario
            model.addAttribute("error", "Error al registrar el usuario. Por favor, inténtelo de nuevo. :(");
            model.addAttribute("User", new User());
            // Redirigir de nuevo a la página de registro con el mensaje de error
            log.error("Error al crear usuario" + user, e);
            return "registro";
        }
    }

    

    @GetMapping("/check-username")
    public ResponseEntity<Boolean> checkUsername(@RequestParam String username) {
    try {
        User target = entityManager.createNamedQuery("User.byUsername", User.class)
                .setParameter("username", username)
                .getSingleResult();
        
        // Si no se lanza ninguna excepción, significa que el usuario existe
        return ResponseEntity.ok(false);
    } catch (NoResultException e) {
        // Si se lanza la excepción, significa que el usuario no existe
        return ResponseEntity.ok(true);
    }
}


    @GetMapping("/adminreport")
    public String adminreport(Model model) {
        return "adminreport";
    }

}
