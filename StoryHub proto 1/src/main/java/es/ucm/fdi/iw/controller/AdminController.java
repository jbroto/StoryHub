package es.ucm.fdi.iw.controller;

import es.ucm.fdi.iw.LocalData;
import es.ucm.fdi.iw.model.Message;
import es.ucm.fdi.iw.model.Noti;
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
import javax.transaction.Transactional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.aspectj.apache.bcel.generic.ObjectType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

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
 * Site administration.
 *
 * Access to this end-point is authenticated - see SecurityConfig
 */
@Controller
@RequestMapping("admin")
public class AdminController {

	private static final Logger log = LogManager.getLogger(AdminController.class);

	@Autowired
	private EntityManager entityManager;

	@Autowired
	private SimpMessagingTemplate messagingTemplate;

	@GetMapping("/")
	public String index(Model model, HttpSession session) {
		User user = ((User) session.getAttribute("u"));
		long id = user.getId();
		User target = entityManager.find(User.class, id);

		// Obtenemos la lista de comentarios reportados
		List<Comment> reports = entityManager.createNamedQuery("Comentario.isReported", Comment.class).getResultList();

		model.addAttribute("user", target);
		model.addAttribute("reportes", reports);

		// User user = entityManager.find(User.class, );

		return "admin";
	}

	@PostMapping("{idComentario}/eliminarComentario")
	@ResponseBody
	@Transactional
	public ResponseEntity<Boolean> eliminar(@PathVariable long idComentario) {

		try {
			Comment c = entityManager.find(Comment.class, idComentario);
			if (c.isReport()) {
				String texto = c.getText();
				c.setText("Este comentario ha sido eliminado por infringir las normas");
				c.setReport(false);
				c.setDeleted(true);
				
				String text ="Se ha eliminado tu comentario con texto " + texto + " de " +c.getMedia().getTipo()+": " + c.getMedia().getNombre() + " por infringir las normas.";

				Noti n = new Noti();
				n.setEnlace("/user/"+c.getAuthor().getId()+"/comentario/"+c.getId());
				n.setObjetivo(c.getAuthor());
				n.setTexto(text);
				n.setVisto(false);
				c.getAuthor().addNoti(n);

				entityManager.persist(n);
				entityManager.merge(c);
				entityManager.flush();


				ObjectMapper mapper = new ObjectMapper();
				ObjectNode rootNode = mapper.createObjectNode();
				rootNode.put("text", text);
				rootNode.put("commentId", c.getId());
				rootNode.put("mediaId", c.getMedia().getId());
				String json = mapper.writeValueAsString(rootNode);
				messagingTemplate.convertAndSend("/user/"+c.getAuthor().getUsername()+"/queue/updates", json);

			}
			return ResponseEntity.ok(true);
		} catch (Exception e) {
			log.error("Error al eliminar el comentario con id " + idComentario);
			return ResponseEntity.ok(false);
		}

	}

	@PostMapping("{idComentario}/quitarReporte")
	@ResponseBody
	@Transactional
	public ResponseEntity<Boolean> quitarReporte(@PathVariable long idComentario) {

		try {
			Comment c = entityManager.find(Comment.class, idComentario);
			if (c.isReport()) {
				c.setReport(false);
				entityManager.persist(c);
				entityManager.flush();
			}
			return ResponseEntity.ok(true);
		} catch (Exception e) {
			log.error("Error al quitar reporte en el comentario con id " + idComentario);
			return ResponseEntity.ok(false);
		}

	}

	@PostMapping("/id/")
	public String postMethodName(@RequestBody String entity) {
		// TODO: process POST request

		return entity;
	}

}
