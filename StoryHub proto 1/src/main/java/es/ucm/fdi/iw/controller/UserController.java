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

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.aspectj.apache.bcel.classfile.ExceptionTable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.*;
import java.security.SecureRandom;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * User management.
 *
 * Access to this end-point is authenticated.
 */
@Controller()
@RequestMapping("user")
public class UserController {

	private static final Logger log = LogManager.getLogger(UserController.class);

	@Autowired
	private EntityManager entityManager;

	@Autowired
	private LocalData localData;

	@Autowired
	private SimpMessagingTemplate messagingTemplate;

	@Autowired
	private PasswordEncoder passwordEncoder;

	/**
	 * Exception to use when denying access to unauthorized users.
	 * 
	 * In general, admins are always authorized, but users cannot modify
	 * each other's profiles.
	 */
	@ResponseStatus(value = HttpStatus.FORBIDDEN, reason = "No eres administrador, y éste no es tu perfil") // 403
	public static class NoEsTuPerfilException extends RuntimeException {
	}

	/**
	 * Encodes a password, so that it can be saved for future checking. Notice
	 * that encoding the same password multiple times will yield different
	 * encodings, since encodings contain a randomly-generated salt.
	 * 
	 * @param rawPassword to encode
	 * @return the encoded password (typically a 60-character string)
	 *         for example, a possible encoding of "test" is
	 *         {bcrypt}$2y$12$XCKz0zjXAP6hsFyVc8MucOzx6ER6IsC1qo5zQbclxhddR1t6SfrHm
	 */
	public String encodePassword(String rawPassword) {
		return passwordEncoder.encode(rawPassword);
	}

	/**
	 * Generates random tokens. From https://stackoverflow.com/a/44227131/15472
	 * 
	 * @param byteLength
	 * @return
	 */
	public static String generateRandomBase64Token(int byteLength) {
		SecureRandom secureRandom = new SecureRandom();
		byte[] token = new byte[byteLength];
		secureRandom.nextBytes(token);
		return Base64.getUrlEncoder().withoutPadding().encodeToString(token); // base64 encoding
	}

	/**
	 * Landing page for a user profile
	 */
	@GetMapping("{id}")
	public String index(@PathVariable long id, Model model, HttpSession session) {
		User target = entityManager.find(User.class, id);
		//solucion provisional porque no funciona con thymleaf usuario.followers.contains(session.u)
		User copia = (User) session.getAttribute("u");
		User actual = entityManager.find(User.class, copia.getId());

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

		// cambiar
		model.addAttribute("user", target);
		model.addAttribute("actual", actual);
		model.addAttribute("Lista", lista); // Agregar la lista al modelo
		model.addAttribute("Listas", listasUs);
		model.addAttribute("favoritos", favMedias);
		model.addAttribute("viendo", viendoMedias);
		model.addAttribute("terminado", terminadoMedias);

		return "user";
	}

	/**
	 * Alter or create a user
	 */

	// FALTA TRATAR EXCEPCIONES
	@PostMapping("/{id}")
	@Transactional
	public String postUser(
			HttpServletResponse response,
			@PathVariable long id,
			@ModelAttribute User edited,
			@RequestParam(required = false) String pass2,
			Model model, HttpSession session) throws IOException {

		User requester = (User) session.getAttribute("u");
		User target = null;
		if (id == -1 && requester.hasRole(Role.ADMIN)) {
			// create new user with random password
			target = new User();
			target.setPassword(encodePassword(generateRandomBase64Token(12)));
			target.setEnabled(true);
			entityManager.persist(target);
			entityManager.flush(); // forces DB to add user & assign valid id
			id = target.getId(); // retrieve assigned id from DB
		}

		// retrieve requested user
		target = entityManager.find(User.class, id);
		model.addAttribute("user", target);

		if (requester.getId() != target.getId() &&
				!requester.hasRole(Role.ADMIN)) {
			throw new NoEsTuPerfilException();
		}

		if (edited.getPassword() != null) {
			if (!edited.getPassword().equals(pass2)) {
				// FIXME: complain
			} else {
				// save encoded version of password
				target.setPassword(encodePassword(edited.getPassword()));
			}
		}
		target.setUsername(edited.getUsername());
		target.setFirstName(edited.getFirstName());
		target.setLastName(edited.getLastName());

		// update user session so that changes are persisted in the session, too
		if (requester.getId() == target.getId()) {
			session.setAttribute("u", target);
		}

		return "user";
	}

	@GetMapping("/{id}/editarPerfil")
	public String verEditarPerfil(@PathVariable long id, Model model) {
		User user = entityManager.find(User.class, id);
		model.addAttribute("user", user);
		return "editarPerfil";
	}

	@PostMapping("/{id}/editarPerfil")
	@Transactional
	public String solicitudEditarPerfil(@PathVariable long id, @RequestParam("firstName") String nombre,
			@RequestParam("lastName") String apellidos, Model model) {
		User user = entityManager.find(User.class, id);// buscamos al usuario
		// y cambiamos los campos rellenados
		user.setFirstName(nombre);
		user.setLastName(apellidos);
		// preguntar si deberiamos dejar cambiar el username

		entityManager.persist(user);
		entityManager.flush();

		log.info("CAMBIOS REALIZADOS -> Username : " + user.getUsername() + " FirstName: " + user.getFirstName() +
				" LastName: " + user.getLastName());

		model.addAttribute("user", user);
		model.addAttribute("EditarExitoso", "El perfil de " + user.getUsername() + " se ha actualizado con exito :)");
		return "editarPerfil";// redirigimos otra vez a editar perfil
	}

	@GetMapping("/{id}/busqueda")
	public String busqueda(@PathVariable long id, @RequestParam("paramBusqueda") String paramBusqueda, Model model) {
		System.out.println(paramBusqueda + '\n' + '\n');

		User target = entityManager.find(User.class, id);

		TMDBService s = new TMDBService();
		GoogleBookService s2 = new GoogleBookService();
		String resultTMDB = s.searchTerm(paramBusqueda);
		String resultBooks = s2.searchTerm(paramBusqueda);

		ArrayList<User> users = (ArrayList<User>) entityManager.createNamedQuery("User.aproxUsername", User.class)
				.setParameter("username", '%' + paramBusqueda + '%')
				.getResultList();

		model.addAttribute("users", users);
		System.out.println("ESTOY ENTRANDO AQUÍ" + target.getId());

		try {
			// lo parseamos tipo JSON
			ObjectMapper objectMapper = new ObjectMapper();

			JsonNode rootNodeTMDB = objectMapper.readTree(resultTMDB);
			JsonNode rootNodeBooks = objectMapper.readTree(resultBooks);

			// Obtener la matriz "results"
			JsonNode resultsNodeTMDB = rootNodeTMDB.get("results");
			JsonNode resultsNodeBooks = rootNodeBooks.get("items");

			ArrayList<Media> listaAudiovisual = new ArrayList<>();
			ArrayList<Media> listaBooks = new ArrayList<>();

			// Iterar sobre los elementos de la matriz "results"
			for (JsonNode resultNode : resultsNodeTMDB) {
				System.out.println(resultNode.get("id").asLong());
				// parseamos los datos de la API TMDB
				Media m = s.parseTMDBtoMedia(resultNode);

				listaAudiovisual.add(m);
			}

			for (JsonNode resultNode : resultsNodeBooks) {
				Media m = s2.parseGoogleBook(resultNode);
				listaBooks.add(m);
			}

			System.out.println(listaAudiovisual);
			model.addAttribute("user", target);
			model.addAttribute("resultado", listaAudiovisual);
			model.addAttribute("result", resultTMDB);
			model.addAttribute("resultadoBooks", listaBooks);
			model.addAttribute("resultBooks", resultBooks);
			model.addAttribute("paramBusqueda", paramBusqueda);
			return "busqueda";

		} catch (Exception e) {
			e.printStackTrace();
		}
		model.addAttribute("paramBusqueda", paramBusqueda);
		model.addAttribute("user", target);

		return "busqueda"; // Asegúrate de devolver un valor en caso de que la lógica no llegue al return

	}

	//SUSCRIPCIONES----------------------------------------------
	@PostMapping("/{id}/suscripcion/{lista}")
	@ResponseBody
	@Transactional
	public ResponseEntity<Boolean> suscribirse(@PathVariable long id, @PathVariable long lista, Model model) {
		User u = entityManager.find(User.class, id);
		Lista l = entityManager.find(Lista.class, lista);

		try{
			u.getSuscripciones().add(l);
			l.getSubscribers().add(u);
	
			entityManager.merge(u);
			entityManager.merge(l);
			entityManager.flush();
			return ResponseEntity.ok().body(true);
		}
		catch(Exception e){
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(false);
		}
	}
	

	//SEGUIDORES-------------------------------------------------

	@GetMapping("/{id}/social/{paramSocial}")
	public String cargarSocial(@PathVariable long id, @PathVariable String paramSocial, Model model,HttpSession session){
		User u = entityManager.find(User.class, id);
		//solucion provisional porque no funciona con thymleaf usuario.followers.contains(session.u)
		User copia = (User) session.getAttribute("u");
		User actual = entityManager.find(User.class, copia.getId());


		model.addAttribute("user", u);
		model.addAttribute("actual", actual);
		model.addAttribute("paramSocial", paramSocial);

		return "social";
	}

	@PostMapping("/{id}/follow")
	@Transactional
	public ResponseEntity<Boolean> seguirUsuario(@PathVariable long id, Model model, HttpSession session){
		try {
			User target = entityManager.find(User.class, id);// buscamos al usuario
			//solucion provisional porque no puedo añadir a la lista del user obtenido por la sesion
			User copia = (User) session.getAttribute("u");
			User user = entityManager.find(User.class, copia.getId());

			//actualizamos la lista de followers del objetivo (el usuario al que seguimos)
			List<User> followers = target.getFollowers();
			followers.add(user);
			target.setFollowers(followers);

			//actualizamos nuestra lista de following (añadimos al user que acabamos de seguir)			
			List<User> following = user.getFollowing();
			following.add(target);
			user.setFollowing(following);

			model.addAttribute("user", target);
			log.info("Usuario :" + user.getUsername() + " Comienza a seguir a usuario: " + target.getUsername());
			// El usuario con sesion activa acaba de seguir al usuario con el id dado
			return ResponseEntity.ok(true);
		} catch (Exception e) {
			log.error("Error al intentar seguir al usuario con ID: " + id, e);
			return ResponseEntity.ok(false);
		}
	}

	@PostMapping("/{id}/unfollow")
	@Transactional
	public ResponseEntity<Boolean> dejarDeSeguirUsuario(@PathVariable long id, Model model, HttpSession session){
		try {
			User target = entityManager.find(User.class, id);// buscamos al usuario
			//solucion provisional porque no puedo quitar de la lista del user obtenido por la sesion
			User copia = (User) session.getAttribute("u");
			User user = entityManager.find(User.class, copia.getId());

			//actualizamos la lista de followers del objetivo (el usuario al que ya no seguimos)
			List<User> followers = target.getFollowers();
			followers.remove(user);
			target.setFollowers(followers);

			//actualizamos nuestra lista de following (quitamos al user que acabamos de dar unfollow)			
			List<User> following = user.getFollowing();
			following.remove(target);
			user.setFollowing(following);

			model.addAttribute("user", target);
			log.info("Usuario :" + user.getUsername() + " Ha dejado de seguir al usuario: " + target.getUsername());
			// El usuario con sesion activa acaba de dejar de seguir al usuario con el id dado
			return ResponseEntity.ok(true);
		} catch (Exception e) {
			log.error("Error al intentar dejar de seguir al usuario con ID: " + id, e);
			return ResponseEntity.ok(false);
		}
	}

	//EDITAR PERFIL-------------------------------------------------


	@GetMapping("/{id}/perfilUsuario")
	public String perfilUser(@PathVariable long id, @RequestParam("username") String param, Model model) {
		User target = entityManager.createNamedQuery("User.byUsername", User.class)
				.setParameter("username", param)
				.getSingleResult();
		User u = entityManager.find(User.class, id);

		Lista lista = new Lista(); // Crear una nueva instancia de Lista

		ArrayList<Lista> ls = (ArrayList<Lista>) entityManager.createNamedQuery("Lista.byAuthor", Lista.class)
				.setParameter("author", target.getId())
				.getResultList();

		Lista favoritos = entityManager.createNamedQuery("Lista.byName", Lista.class)
				.setParameter("name", "favoritos").setParameter("author", target.getId()).getSingleResult();
		// obtenemos un solo resultado(ya sabemos que solo hay una lista de fav)
		List<Media> favMedias = favoritos.getMedias(); // creamos la lista de Medias contenidas en la lista

		// Obtenemos la lista de estoy viendo
		Lista viendo = entityManager.createNamedQuery("Lista.byName", Lista.class)
				.setParameter("name", "viendo").setParameter("author", target.getId()).getSingleResult();
		List<Media> viendoMedias = viendo.getMedias(); // creamos la lista de Medias contenidas en la lista

		// Obtenemos la lista de terminado
		Lista terminado = entityManager.createNamedQuery("Lista.byName", Lista.class)
				.setParameter("name", "terminado").setParameter("author", target.getId()).getSingleResult();
		List<Media> terminadoMedias = terminado.getMedias(); // creamos la lista de Medias contenidas en la lista

		model.addAttribute("actual", u);
		model.addAttribute("user", target);
		model.addAttribute("Lista", lista); // Agregar la lista al modelo
		model.addAttribute("Listas", ls);
		model.addAttribute("favoritos", favMedias);
		model.addAttribute("viendo", viendoMedias);
		model.addAttribute("terminado", terminadoMedias);
		return "user";
	}

	@GetMapping("/{id}/contenido")
	@Transactional
	public String contenido(@PathVariable long id, @RequestParam("tipo") String tipo,
			@RequestParam("idMedia") Long idMedia, Model model) {
		System.out.println(tipo + '\n' + idMedia + '\n'); // quitar, solo para comprobar en debug

		User usuario = entityManager.find(User.class, id);

		Comment comentario = new Comment();

		Media m = entityManager.find(Media.class, idMedia);

		try {

			if (m == null) { // si no tenemos el con o en la BD, lo sacamos de la API
				// parseamos los datos de la API TMDB
				TMDBService s = new TMDBService();
				// Llamar al servicio para obtener los detalles del contenido
				String resultado = s.obtenerContenido(tipo, idMedia);
				// lo parseamos tipo JSON
				ObjectMapper objectMapper = new ObjectMapper();
				JsonNode resultNode = objectMapper.readTree(resultado);

				m = s.parseTMDBtoMedia(resultNode);
				m.setTipo(tipo);

				// obtener el numero de temporadas. Para poder obtener mas info tipo el num de
				// capitulos o la sinopsis deberiamos
				// replantearnos crear otra class
				if (m.getTipo().equals("tv")) {
					JsonNode seasonsNode = resultNode.get("seasons");
					List<String> seasonNames = new ArrayList<>();

					if (seasonsNode != null && seasonsNode.isArray()) {
						for (JsonNode seasonNode : seasonsNode) {
							String name = seasonNode.get("name").asText();
							seasonNames.add(name);
						}
					}

					// Ahora tienes la lista de nombres de temporadas
					model.addAttribute("temporadas", seasonNames);
				}
				entityManager.persist(m);
			}
			MediaUserRelationId rel = new MediaUserRelationId(idMedia, id);
			MediaUserRelation r = entityManager.find(MediaUserRelation.class, rel);
			// CADA VEZ QUE UN USUARIO ACCEDA A NUEVO CONTENIDO CREAMOS LA RELACION CON
			// VALORACION 0 Y SIN NINGUNA LISTA AÑADIDA
			if (r == null) {
				MediaUserRelation relacion = new MediaUserRelation();
				relacion.setId(rel);
				relacion.setCalificacion(0);
				relacion.setEnded(false);
				relacion.setFavorito(false);
				relacion.setViendo(false);
				relacion.setUser(usuario);
				relacion.setMedia(m);

				entityManager.persist(relacion);
				entityManager.flush();
				r = relacion;
			}

			// Agregamos los detalles del contenido al modelo
			
			List<Comment> comentsMedia = entityManager.createNamedQuery("Comentario.byMedia", Comment.class)
			.setParameter("idMedia", idMedia).getResultList();

			m.setComments(comentsMedia);
			

			model.addAttribute("comentario", comentario);
			model.addAttribute("user", usuario);
			model.addAttribute("Listas", usuario.getListas());
			model.addAttribute("comentarios", comentsMedia);
			model.addAttribute("media", m);
			model.addAttribute("relacion", r);

			// Devolvemos el nombre de la vista a la que se redirigirá
			return "contenido";

		} catch (Exception e) {
			e.printStackTrace();
		}
		return "contenido";// en caso de que no llegue al otro return
	}

	@GetMapping("/{id}/comentario/{idComentario}")
	public String getLista(@PathVariable long id, @PathVariable long idComentario, Model model, HttpSession session) {
		User target = entityManager.find(User.class, id);
		Comment coment = entityManager.find(Comment.class, idComentario);
		List<Comment> comentarios = entityManager.createNamedQuery("Comentario.byFather", Comment.class)
				.setParameter("father", idComentario).getResultList();

		Comment comentario = new Comment();

		model.addAttribute("comentarios", comentarios);
		model.addAttribute("coment", coment);
		model.addAttribute("user", target);
		model.addAttribute("comentario", comentario);

		return "comentario";
	}

	@PostMapping("/{id}/{idMedia}/nuevoComentario")
	@ResponseBody
	@Transactional
	public ResponseEntity<Map<String, String>> nuevoComentario(@PathVariable long id,
			@RequestParam("mediaId") long idMedia,
			@RequestParam("texto") String texto,
			@RequestParam("mediaTipo") String tipo, @ModelAttribute Comment comentario, HttpSession session,
			Model model) {
		try {
			User usuario = entityManager.find(User.class, id);
			Media m = entityManager.find(Media.class, idMedia);// obtenemos el contenido

			model.addAttribute("user", usuario);
			Comment coment = new Comment();
			model.addAttribute("comentario", coment);
			coment.setAuthor(usuario);
			coment.setText(texto);
			coment.setMedia(m);
			coment.setDateSent(LocalDate.now());

			entityManager.persist(coment);

			m.addComment(coment);
			entityManager.merge(m);
			entityManager.flush();

			List<Comment> comentsMedia = m.getComments();
			model.addAttribute("comentarios", comentsMedia);

			List<Lista> listasUs = usuario.getListas();
			model.addAttribute("Listas", listasUs);

			model.addAttribute("media", m);

			log.info("Comentario de ", id);

			Map<String, String> response = new HashMap<>();
			response.put("author", coment.getAuthor().getUsername());
			response.put("authorId", String.valueOf(coment.getAuthor().getId()));
			response.put("dateSent", coment.getDateSent().toString());
			response.put("text", coment.getText());
			response.put("comentId", String.valueOf(coment.getId()));

			return ResponseEntity.ok().body(response);

		} catch (Exception e) {
			log.error("Error al comentar " + id, e);
			Map<String, String> response = new HashMap<>();
			response.put("error", "Error al agregar el comentario");
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}
	}

	@PostMapping("/{id}/comentario/{idComent}/nuevaRespuesta")
	@ResponseBody
	@Transactional
	public ResponseEntity<Map<String, String>> nuevaRespuesta(@PathVariable long id,
			@RequestParam("fatherId") long idComent, @RequestParam("texto") String text,
			@ModelAttribute Comment comentario, HttpSession session,
			Model model) {
		try {
			User usuario = entityManager.find(User.class, id);
			model.addAttribute("user", usuario);
			Comment padre = entityManager.find(Comment.class, idComent);
			Comment coment = new Comment();
			coment.setAuthor(usuario);
			coment.setText(text);
			coment.setDateSent(LocalDate.now());
			coment.setFather(padre);

			entityManager.persist(coment);
			entityManager.flush();

			List<Comment> comentarios = entityManager.createNamedQuery("Comentario.byFather", Comment.class)
					.setParameter("father", idComent).getResultList();

			model.addAttribute("comentario", coment);
			model.addAttribute("comentarios", comentarios);
			model.addAttribute("coment", padre);
			model.addAttribute("user", usuario);

			log.info("Comentario de ", id);

			Map<String, String> response = new HashMap<>();
			response.put("author", coment.getAuthor().getUsername());
			response.put("authorId", String.valueOf(coment.getAuthor().getId()));
			response.put("dateSent", coment.getDateSent().toString());
			response.put("text", coment.getText());
			response.put("comentId", String.valueOf(coment.getId()));

			return ResponseEntity.ok().body(response);
		} catch (Exception e) {
			log.error("Error al comentar " + id, e);
			Map<String, String> response = new HashMap<>();
			response.put("error", "Error al agregar el comentario");
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}
	}

	@PostMapping("/{id}/{idMedia}/reportarComentario")
	@ResponseBody
	@Transactional
	public ResponseEntity<Boolean> reportar(@PathVariable long id, @PathVariable long idMedia,
	@RequestParam("comentarioId") long comentario, HttpSession session) {


		try {
			Comment c = entityManager.find(Comment.class, comentario);
			if (!c.isReport()) {
				c.setReport(true);
				entityManager.persist(c);
				entityManager.flush();
			}
			return ResponseEntity.ok(true);
		} catch (Exception e) {
			log.error("Error al reportar el comentario con id " + comentario);
			return ResponseEntity.ok(false);
		}

		// mi idea es crear como una list<Comment> donde guardaremos todos los
		// comentarios que han querido ser reportados,
		// para luego esa lista representarla en el .html de admin y que el pueda ver
		// todos los comentarios que han sido reportados, que usuarios
		// los han reportado, y el comentario en si con su contenido y en el media que
		// se encuentra

	}

	// PARA HACER CHECK DEL NOMBRE DE LISTA DISPONIBLE
	@GetMapping("/{id}/check-nombreLista")
	public ResponseEntity<Boolean> checkNombreLista(@PathVariable long id, @RequestParam String nombreLista) {
		try {
			// Verificar si la lista existe para el usuario dado su nombre
			Lista l = entityManager.createNamedQuery("Lista.byName", Lista.class)
					.setParameter("name", nombreLista)
					.setParameter("author", id)
					.getSingleResult();
			// Si la lista existe, devolver true
			return ResponseEntity.ok(true);
		} catch (NoResultException e) {
			// Si la lista no existe, devolver false
			return ResponseEntity.ok(false);
		}
	}

	@GetMapping("/{id}/{nombreLista}/{username}")
	public String getLista(@PathVariable long id, @PathVariable String nombreLista, @PathVariable String username, Model model, HttpSession session) {
		User u = entityManager.find(User.class, id);
		User target = entityManager.createNamedQuery("User.byUsername", User.class).setParameter("username", username).getSingleResult();

		// Obtenemos la lista de favoritos
		Lista l = entityManager.createNamedQuery("Lista.byName", Lista.class)
				.setParameter("name", nombreLista).setParameter("author", target.getId()).getSingleResult();
		// obtenemos un solo resultado(ya sabemos que solo hay una lista de fav)
		List<Media> medias = l.getMedias(); // creamos la lista de Medias contenidas en la lista

		model.addAttribute("user", u);
		model.addAttribute("lista", l); // Agregar la lista al modelo
		model.addAttribute("contenidos", medias);
		model.addAttribute("suscrito", u.getSuscripciones().contains(l));
		System.out.println(u.getSuscripciones().contains(l));
		return "lista";
	}

	// funcion para añadir un contenido a una lista (de tipo bool para llamarlo
	// desde JS)
	@PostMapping("/{id}/addTo/{nombreLista}")
	@ResponseBody
	@Transactional
	public ResponseEntity<Boolean> addMediaToLista(@PathVariable long id, @PathVariable String nombreLista,
			@RequestParam("mediaId") long idMedia, @RequestParam("mediaTipo") String tipoMedia,
			HttpSession session, Model model) {
		try {
			User usuario = entityManager.find(User.class, id);// buscamos al usuario

			Lista lista = entityManager.createNamedQuery("Lista.byName", Lista.class)
					.setParameter("name", nombreLista).setParameter("author", id).getSingleResult();

			Media m = entityManager.find(Media.class, idMedia);// obtenemos el contenido si esta en BD

			System.out.println(lista.getName());

			MediaUserRelationId rel = new MediaUserRelationId(idMedia, id);
			MediaUserRelation r = entityManager.find(MediaUserRelation.class, rel);

			r.listasBasicas(nombreLista, true);

			List<Media> lMedias = lista.getMedias();
			lMedias.add(m);
			int cont = lista.getContador() + 1;// aumentamos el contador
			lista.setContador(cont);
			lista.setMedias(lMedias);

			entityManager.persist(lista);
			entityManager.merge(usuario);
			entityManager.flush();

			model.addAttribute("user", usuario);
			model.addAttribute("lista", lista);
			log.info("Usuario, Media y Lista", id, m, nombreLista);

			// "Elemento agregado correctamente a la lista"
			return ResponseEntity.ok(true);
		} catch (Exception e) {
			log.error("Error al crear la lista para el usuario " + id, e);
			return ResponseEntity.ok(false);
		}
	}

	// funcion para añadir un contenido a una lista (de tipo bool para llamarlo
	// desde JS)
	@PostMapping("/{id}/removeFrom/{nombreLista}")
	@ResponseBody
	@Transactional
	public ResponseEntity<Boolean> removeMediaFromLista(@PathVariable long id, @PathVariable String nombreLista,
			@RequestParam("mediaId") long idMedia, @RequestParam("mediaTipo") String tipoMedia,
			HttpSession session, Model model) {
		try {
			User usuario = entityManager.find(User.class, id);// buscamos al usuario

			Lista lista = entityManager.createNamedQuery("Lista.byName", Lista.class)
					.setParameter("name", nombreLista).setParameter("author", id).getSingleResult();

			Media m = entityManager.find(Media.class, idMedia);// obtenemos el contenido si esta en BD

			System.out.println(lista.getName());

			MediaUserRelationId rel = new MediaUserRelationId(idMedia, id);
			MediaUserRelation r = entityManager.find(MediaUserRelation.class, rel);

			r.listasBasicas(nombreLista, false);

			List<Media> lMedias = lista.getMedias();
			lMedias.remove(m);
			int cont = lista.getContador() - 1;// disminuimos el contador
			lista.setContador(cont);
			lista.setMedias(lMedias);

			entityManager.persist(lista);
			entityManager.merge(usuario);
			entityManager.flush();

			model.addAttribute("user", usuario);
			model.addAttribute("lista", lista);
			log.info("Usuario, Media y Lista", id, m, nombreLista);

			// "Elemento eliminado correctamente a la lista"
			return ResponseEntity.ok(true);
		} catch (Exception e) {
			log.error("Error al eliminar de  la lista para el usuario " + id, e);
			return ResponseEntity.ok(false);
		}
	}

	@PostMapping("/{id}/crearLista")
	@ResponseBody
	@Transactional
	public ResponseEntity<String> crearLista(@PathVariable long id, @ModelAttribute Lista lista, HttpSession session,
			Model model) {
		try {
			User usuario = entityManager.find(User.class, id);
			model.addAttribute("user", usuario);
			Lista nuevaLista = new Lista();
			model.addAttribute("Lista", nuevaLista);
			nuevaLista.setAuthor(usuario);
			nuevaLista.setIsPublic(lista.getIsPublic());
			nuevaLista.setName(lista.getName());

			entityManager.persist(nuevaLista);

			usuario.addList(nuevaLista);
			entityManager.merge(usuario);
			entityManager.flush();

			List<Lista> listasUs = usuario.getListas();
			model.addAttribute("Listas", listasUs);

			log.info("Lista creada para el usuario ", id);

			return ResponseEntity.status(HttpStatus.FOUND).header(HttpHeaders.LOCATION, "/user/" + id).build();
		} catch (Exception e) {
			log.error("Error al crear la lista para el usuario " + id, e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al crear la lista");
		}
	}

	@PostMapping("/{id}/califica")
	@ResponseBody
	@Transactional
	public ResponseEntity<Boolean> califica(HttpSession session,
			Model model, @PathVariable long id, @RequestParam("rating") int rating,
			@RequestParam("mediaTipo") String mediaTipo, @RequestParam("mediaId") long mediaId) {
		try {
			User usuario = entityManager.find(User.class, id);
			Media m = entityManager.find(Media.class, mediaId);// obtenemos el contenido

			model.addAttribute("user", usuario);
			Comment coment = new Comment();
			model.addAttribute("comentario", coment);

			MediaUserRelationId rel = new MediaUserRelationId(mediaId, id);
			MediaUserRelation r = entityManager.find(MediaUserRelation.class, rel);

			r.setCalificacion(rating);
			entityManager.merge(r);
			entityManager.flush();
			model.addAttribute("myRating", r.getCalificacion());

			List<Comment> comentsMedia = m.getComments();
			model.addAttribute("comentarios", comentsMedia);

			List<Lista> listasUs = usuario.getListas();
			model.addAttribute("Listas", listasUs);

			model.addAttribute("media", m);

			log.info("Comentario de " + id + " CON UN RATING DE :" + rating);

			return ResponseEntity.ok(true);

		} catch (Exception e) {
			log.error("Error al comentar " + id, e);
			return ResponseEntity.ok(false);
		}
	}

	/**
	 * Returns the default profile pic
	 * 
	 * @return
	 */
	private static InputStream defaultPic() {
		return new BufferedInputStream(Objects.requireNonNull(
				UserController.class.getClassLoader().getResourceAsStream(
						"static/img/default-avatar.jpg")));
	}

	/**
	 * Downloads a profile pic for a user id
	 * 
	 * @param id
	 * @return
	 * @throws IOException
	 */
	@GetMapping("{id}/pic")
	public StreamingResponseBody getPic(@PathVariable long id) throws IOException {
		File f = localData.getFile("user", "" + id + ".jpg");
		InputStream in = new BufferedInputStream(f.exists() ? new FileInputStream(f) : UserController.defaultPic());
		return os -> FileCopyUtils.copy(in, os);
	}

	/**
	 * Uploads a profile pic for a user id
	 * 
	 * @param id
	 * @return
	 * @throws IOException
	 */
	@PostMapping("{id}/pic")
	@ResponseBody
	public String setPic(@RequestParam("photo") MultipartFile photo, @PathVariable long id,
			HttpServletResponse response, HttpSession session, Model model) throws IOException {

		User target = entityManager.find(User.class, id);
		model.addAttribute("user", target);

		// check permissions
		User requester = (User) session.getAttribute("u");
		if (requester.getId() != target.getId() &&
				!requester.hasRole(Role.ADMIN)) {
			throw new NoEsTuPerfilException();
		}

		log.info("Updating photo for user {}", id);
		File f = localData.getFile("user", "" + id + ".jpg");
		if (photo.isEmpty()) {
			log.info("failed to upload photo: emtpy file?");
		} else {
			try (BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(f))) {
				byte[] bytes = photo.getBytes();
				stream.write(bytes);
				log.info("Uploaded photo for {} into {}!", id, f.getAbsolutePath());
			} catch (Exception e) {
				response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
				log.warn("Error uploading " + id + " ", e);
			}
		}
		return "{\"status\":\"photo uploaded correctly\"}";
	}

	/**
	 * Returns JSON with all received messages
	 */
	@GetMapping(path = "received", produces = "application/json")
	@Transactional // para no recibir resultados inconsistentes
	@ResponseBody // para indicar que no devuelve vista, sino un objeto (jsonizado)
	public List<Message.Transfer> retrieveMessages(HttpSession session) {
		long userId = ((User) session.getAttribute("u")).getId();
		User u = entityManager.find(User.class, userId);
		log.info("Generating message list for user {} ({} messages)",
				u.getUsername(), u.getReceived().size());
		return u.getReceived().stream().map(Transferable::toTransfer).collect(Collectors.toList());
	}

	/**
	 * Returns JSON with count of unread messages
	 */
	@GetMapping(path = "unread", produces = "application/json")
	@ResponseBody
	public String checkUnread(HttpSession session) {
		long userId = ((User) session.getAttribute("u")).getId();
		long unread = entityManager.createNamedQuery("Message.countUnread", Long.class)
				.setParameter("userId", userId)
				.getSingleResult();
		session.setAttribute("unread", unread);
		return "{\"unread\": " + unread + "}";
	}

	/**
	 * Posts a message to a user.
	 * 
	 * @param id of target user (source user is from ID)
	 * @param o  JSON-ized message, similar to {"message": "text goes here"}
	 * @throws JsonProcessingException
	 */
	@PostMapping("/{id}/msg")
	@ResponseBody
	@Transactional
	public String postMsg(@PathVariable long id,
			@RequestBody JsonNode o, Model model, HttpSession session)
			throws JsonProcessingException {

		String text = o.get("message").asText();
		User u = entityManager.find(User.class, id);
		User sender = entityManager.find(
				User.class, ((User) session.getAttribute("u")).getId());
		model.addAttribute("user", u);

		// construye mensaje, lo guarda en BD
		Message m = new Message();
		m.setRecipient(u);
		m.setSender(sender);
		m.setDateSent(LocalDateTime.now());
		m.setText(text);
		entityManager.persist(m);
		entityManager.flush(); // to get Id before commit

		ObjectMapper mapper = new ObjectMapper();
		/*
		 * // construye json: método manual
		 * ObjectNode rootNode = mapper.createObjectNode();
		 * rootNode.put("from", sender.getUsername());
		 * rootNode.put("to", u.getUsername());
		 * rootNode.put("text", text);
		 * rootNode.put("id", m.getId());
		 * String json = mapper.writeValueAsString(rootNode);
		 */
		// persiste objeto a json usando Jackson
		String json = mapper.writeValueAsString(m.toTransfer());

		log.info("Sending a message to {} with contents '{}'", id, json);

		messagingTemplate.convertAndSend("/user/" + u.getUsername() + "/queue/updates", json);
		return "{\"result\": \"message sent.\"}";
	}
}
