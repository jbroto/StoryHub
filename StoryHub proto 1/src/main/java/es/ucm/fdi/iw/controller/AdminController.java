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

        Lista lista = new Lista(); // Crear una nueva instancia de Lista
		// obtenemos la lista de favoritos, viendo y terminado

		List<Lista> listasUs = target.getListas();

		// Obtenemos la lista de favoritos
		Lista favoritos = entityManager.createNamedQuery("Lista.byName", Lista.class)
				.setParameter("name", "favoritos").setParameter("author", id).getSingleResult();
		// obtenemos un solo resultado(ya sabemos que solo hay una lista de fav)
		List<Media> favMedias = favoritos.getMedias(); // creamos la lista de Medias contenidas en la lista

		// Obtenemos la lista de estoy viendo
		Lista viendo = entityManager.createNamedQuery("Lista.byName", Lista.class)
				.setParameter("name", "viendo").setParameter("author", id).getSingleResult();
		List<Media> viendoMedias = viendo.getMedias(); // creamos la lista de Medias contenidas en la lista

		// Obtenemos la lista de terminado
		Lista terminado = entityManager.createNamedQuery("Lista.byName", Lista.class)
				.setParameter("name", "terminado").setParameter("author", id).getSingleResult();
		List<Media> terminadoMedias = terminado.getMedias(); // creamos la lista de Medias contenidas en la lista

		int contadorVistos = terminado.getContador();
		// cambiar
		model.addAttribute("user", target);
		model.addAttribute("Lista", lista); // Agregar la lista al modelo
		model.addAttribute("Listas", listasUs);
		model.addAttribute("favoritos", favMedias);
		model.addAttribute("viendo", viendoMedias);
		model.addAttribute("terminado", terminadoMedias);
		model.addAttribute("contVisto", contadorVistos);
        
        //User user = entityManager.find(User.class, );
        
    
        
        
        return "admin";
    }
}
