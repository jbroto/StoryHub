# Story Hub 
[Story Hub](https://github.com/jbroto/StoryHub/tree/main) es una página web que permitirá a los usuarios llevar un registro de las películas y series que van viendo o libros que van leyendo, manteniendo así en un solo lugar toda la información necesaria. 
También permitirá poner comentarios y reseñar series en general, capítulos o películas, así el usuario podrá ver las opiniones del resto al acceder a los detalles de cada elemento. 
> Contaremos con dos tipos de usuario:
 -  **`Administrador`**: que podrá ver los comentarios reportados y decidir si eliminarlos o no
 - **`Usuario normal`**, que tendrá acceso a las funcionalidades principales de la web.

> La aplicación contará con las siguientes vistas:

 - **`Página principal`**: esta vista es la que aparece nada más entrar y no hay ninguna sesión iniciada, mostrará el logo de la aplicación, junto con más información y la posibilidad de iniciar sesión. 
 - **`Inicio de sesión`**: aquí se pedirá el usuario y contraseña para iniciar sesión y acceder al resto de funcionalidades. 
 - **`Perfil`**: esta vista mostrará información sobre un perfil de usuario (el del usuario que lo usa u otro ajeno), con información como las series/películas/libros vistas/leídos, los comentarios publicados, y las reseñas hechas. 
 - **`Contenido`**: aquí entramos en detalle en un elemento (serie, capitulo, película, libro), permitiendo ver en detalle más información, así como los capítulos que tiene (cuando corresponda), algunos comentarios, y reseñas. 
 - **`Búsqueda`**: aquí permitiremos al usuario hacer una búsqueda de contenido (series, películas, libros) además de buscar usuarios para poder ver su perfil. 
 - **`Home/Lista de Contenido`**: esta vista será básicamente una lista de contenido (series, películas, libros) sin mostrar información detallada de cada uno, simplemente nos dejará acceder a un elemento en concreto de la lista haciendo click sobre su elemento correspondiente. 
 - **`Admin`**: esta vista sólo será accesible para el usuario que se identifique como administrador, aquí se mostrarán los comentarios reportados y se podrá tomar una decisión sobre los mismos y el usuario responsable de cada uno. Al resto de usuarios se les permitirá enviar mensajes al administrador.
# Story Hub Proto 1:

> **cambios realizados**:

 - **Página de inicio** (`index`) con un enlace al `home` del `user`
 -  Login ajustado a la plantilla dada
 -  Navbar con el siguiente contenido:
	 - Logo de Story Hub
	 - **Icono** y **nombre de usuario** en forma de **Dropdown** que contiene:
		 - **`Home`**: para volver a la pagina principal del usuario
		 - 	 **`Peliculas`**: para ir al contenido de películas del usuario
		 - 	 **`Series`**: para ir al contenido de series del usuario
		 - 	 **`Libros`**: para ir al contenido de Libros del usuario
	- Provisionalmente un **`nav-item`** Home (se puede quitar)
	- Provisionalmente un **`nav-item`** Login (se puede quitar)
	- Un **`nav-item`** **Admin** que redirecciona a la pagina para reportar al adminsitrador
	- Provisionalmente un **`nav-item`** Contenido que redirecciona al apartado del contenido (se puede quitar)
	- Un icono con las **notificaciones** que le pueden llegar al usuario
	- Una **Barra de Búsqueda** para encontrar contenido de todo tipo (no funcional)
 -  Pequeños ajustes en los `HTML` para que la redirección sea correcta
 -   Pequeños ajustes en los `CSS` para que los estilos se apliquen correctamente
