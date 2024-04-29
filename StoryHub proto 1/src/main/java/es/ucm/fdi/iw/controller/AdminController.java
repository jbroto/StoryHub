package es.ucm.fdi.iw.controller;

import es.ucm.fdi.iw.LocalData;
import es.ucm.fdi.iw.model.Message;
import es.ucm.fdi.iw.model.TMDBService;
import es.ucm.fdi.iw.model.Transferable;
import es.ucm.fdi.iw.model.User;
import es.ucm.fdi.iw.model.User.Role;
import es.ucm.fdi.iw.model.Lista;
import es.ucm.fdi.iw.model.Comment;
import es.ucm.fdi.iw.model.GoogleBookService;
import es.ucm.fdi.iw.model.Media;
import es.ucm.fdi.iw.model.MediaUserRelation;
import es.ucm.fdi.iw.model.MediaUserRelationId;



import javax.persistence.EntityManager;
import javax.servlet.http.HttpSession;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.aspectj.apache.bcel.generic.ObjectType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Objects;
import java.util.stream.Collectors;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


/**
 *  Site administration.
 *
 *  Access to this end-point is authenticated - see SecurityConfig
 */
@Controller
@RequestMapping("admin")
public class AdminController {

	private static final Logger log = LogManager.getLogger(AdminController.class);

    @Autowired
	private EntityManager entityManager;
    
	@GetMapping("/")
    public String index(Model model, HttpSession session) {
        User user = ((User)session.getAttribute("u"));
        long id = user.getId();
        User target = entityManager.find(User.class, id);

    
		// Obtenemos la lista de terminado
		List<Comment> reports = entityManager.createNamedQuery("Comentario.isReported", Comment.class).getResultList();


		// cambiar
		model.addAttribute("user", target);
		model.addAttribute("reportes", reports);
        
        //User user = entityManager.find(User.class, );
        
    
        
        
        return "admin";
    }

    @PostMapping("/id/")
    public String postMethodName(@RequestBody String entity) {
        //TODO: process POST request
        
        return entity;
    }
    
}
