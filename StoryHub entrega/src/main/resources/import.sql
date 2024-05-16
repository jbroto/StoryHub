-- insert admin (username a, password aa)
INSERT INTO IWUser (id, enabled, roles, username, password)
VALUES (1, TRUE, 'ADMIN,USER', 'a',
    '{bcrypt}$2a$10$2BpNTbrsarbHjNsUWgzfNubJqBRf.0Vz9924nRSHBqlbPKerkgX.W');
INSERT INTO IWUser (id, enabled, roles, username, password)
VALUES (2, TRUE, 'USER', 'b',
    '{bcrypt}$2a$10$2BpNTbrsarbHjNsUWgzfNubJqBRf.0Vz9924nRSHBqlbPKerkgX.W');
INSERT INTO IWUser (id, enabled, roles, username, password)
VALUES (3, TRUE, 'USER', 'User_c',
    '{bcrypt}$2a$10$2BpNTbrsarbHjNsUWgzfNubJqBRf.0Vz9924nRSHBqlbPKerkgX.W');
INSERT INTO IWUser (id, enabled, roles, username, password)
VALUES (4, TRUE, 'USER', 'User_d',
    '{bcrypt}$2a$10$2BpNTbrsarbHjNsUWgzfNubJqBRf.0Vz9924nRSHBqlbPKerkgX.W');
INSERT INTO IWUser (id, enabled, roles, username, password)
VALUES (5, TRUE, 'USER', 'User_e',
    '{bcrypt}$2a$10$2BpNTbrsarbHjNsUWgzfNubJqBRf.0Vz9924nRSHBqlbPKerkgX.W');

-- Seguidores (todos siguen al usuario 2)
INSERT INTO IWUSER_FOLLOWERS (user_id, follower_id) VALUES (2, 1);
INSERT INTO IWUSER_FOLLOWERS (user_id, follower_id) VALUES (2, 3);
INSERT INTO IWUSER_FOLLOWERS (user_id, follower_id) VALUES (2, 4);
INSERT INTO IWUSER_FOLLOWERS (user_id, follower_id) VALUES (2, 5);

-- Siguiendo (el usuario 2 sigue a 2 personas)
INSERT INTO IWUSER_FOLLOWERS (user_id, follower_id) VALUES (1, 2);
INSERT INTO IWUSER_FOLLOWERS (user_id, follower_id) VALUES (3, 2);




--LISTAS DE USERS
INSERT INTO Lista (ID, CATEGORIES, CONTADOR, IS_PUBLIC, NAME, AUTHOR_ID)
VALUES (1,'', 6, TRUE,'Favoritos', 2);
INSERT INTO Lista (ID, CATEGORIES, CONTADOR, IS_PUBLIC, NAME, AUTHOR_ID)
VALUES (2,'', 1, TRUE,'Viendo', 2);
INSERT INTO Lista (ID, CATEGORIES, CONTADOR, IS_PUBLIC, NAME, AUTHOR_ID)
VALUES (3,'', 6, TRUE,'Terminado', 2);

INSERT INTO Lista (ID, CATEGORIES, CONTADOR, IS_PUBLIC, NAME, AUTHOR_ID)
VALUES (7,'', 5, TRUE,'Favoritos', 3);
INSERT INTO Lista (ID, CATEGORIES, CONTADOR, IS_PUBLIC, NAME, AUTHOR_ID)
VALUES (8,'', 0, TRUE,'Viendo', 3);
INSERT INTO Lista (ID, CATEGORIES, CONTADOR, IS_PUBLIC, NAME, AUTHOR_ID)
VALUES (9,'', 5, TRUE,'Terminado', 3);

INSERT INTO Lista (ID, CATEGORIES, CONTADOR, IS_PUBLIC, NAME, AUTHOR_ID)
VALUES (10,'', 5, TRUE,'Favoritos', 4);
INSERT INTO Lista (ID, CATEGORIES, CONTADOR, IS_PUBLIC, NAME, AUTHOR_ID)
VALUES (11,'', 0, TRUE,'Viendo', 4);
INSERT INTO Lista (ID, CATEGORIES, CONTADOR, IS_PUBLIC, NAME, AUTHOR_ID)
VALUES (12,'', 5, TRUE,'Terminado', 4);

INSERT INTO Lista (ID, CATEGORIES, CONTADOR, IS_PUBLIC, NAME, AUTHOR_ID)
VALUES (13,'', 5, TRUE,'Favoritos', 5);
INSERT INTO Lista (ID, CATEGORIES, CONTADOR, IS_PUBLIC, NAME, AUTHOR_ID)
VALUES (14,'', 0, TRUE,'Viendo', 5);
INSERT INTO Lista (ID, CATEGORIES, CONTADOR, IS_PUBLIC, NAME, AUTHOR_ID)
VALUES (15,'', 5, TRUE,'Terminado', 5);

--LISTAS DE EL ADMIN
INSERT INTO Lista (ID, CATEGORIES, CONTADOR, IS_PUBLIC, NAME, AUTHOR_ID)
VALUES (4,'', 1, TRUE,'Favoritos', 1);

INSERT INTO Lista (ID, CATEGORIES, CONTADOR, IS_PUBLIC, NAME, AUTHOR_ID)
VALUES (5,'', 1, TRUE,'Viendo', 1);

INSERT INTO Lista (ID, CATEGORIES, CONTADOR, IS_PUBLIC, NAME, AUTHOR_ID)
VALUES (6,'', 1, TRUE,'Terminado', 1);

INSERT INTO Lista (ID, CATEGORIES, CONTADOR, IS_PUBLIC, NAME, AUTHOR_ID)
VALUES (100,'', 1, TRUE,'Accion', 1);

--CONTENIDO/MEDIA ALMACENADA EN LA BD: 
--Stranger things
INSERT INTO Media (ID, API, COVER_IMAGE_URL, BACKDROP_IMAGE_URL, DESCRIPCION, NOMBRE, RATING, TIPO, FATHER_ID,NUM_FAVS, NUM_VISTO,NUM_LISTAS,NUM_VIENDO,FECHA,ORDEN,NUM_CHILD)
VALUES (66732,'TMDB','https://image.tmdb.org/t/p/original/wHhjZMlYGT9yUEyGpP9jmR6Jq3I.jpg', 'https://image.tmdb.org/t/p/original/56v2KjBlU4XaOv9rVYEQypROD7P.jpg',
'A raíz de la desaparición de un niño, un pueblo desvela un misterio relacionado con experimentos secretos, fuerzas sobrenaturales aterradoras y una niña muy extraña.'
,'Stranger Things',0.0 , 'tv',null,1,1,0,1,'2022-07-01',0,34);

INSERT INTO Media_User_Relation(CALIFICACION, ENDED, FAVORITO, VIENDO, MEDIA_ID, USER_ID)
VALUES(0, TRUE, TRUE, TRUE, 66732, 2);
--media en la listas basicas del user 2
INSERT INTO Lista_Media(LISTA_ID, MEDIA_ID) VALUES (1, 66732);
INSERT INTO Lista_Media(LISTA_ID, MEDIA_ID) VALUES (2, 66732);
INSERT INTO Lista_Media(LISTA_ID, MEDIA_ID) VALUES (3, 66732);

--Godzilla y Kong: El nuevo imperio -------------------------------------------------------
INSERT INTO Media (ID, API, COVER_IMAGE_URL, BACKDROP_IMAGE_URL, DESCRIPCION, NOMBRE, RATING, TIPO, FATHER_ID,NUM_FAVS, NUM_VISTO,NUM_LISTAS,NUM_VIENDO,FECHA,ORDEN,NUM_CHILD)
VALUES (823464,'TMDB','https://image.tmdb.org/t/p/original/2YqZ6IyFk7menirwziJvfoVvSOh.jpg', 'https://image.tmdb.org/t/p/original/lLh39Th5plbrQgbQ4zyIULsd0Pp.jpg',
'Una aventura cinematográfica completamente nueva, que enfrentará al todopoderoso Kong y al temible Godzilla contra una colosal amenaza desconocida escondida dentro de nuestro mundo. La nueva y épica película profundizará en las historias de estos titanes, sus orígenes y los misterios de Isla Calavera y más allá, mientras descubre la batalla mítica que ayudó a forjar a estos seres extraordinarios y los unió a la humanidad para siempre.'
,'Godzilla y Kong: El nuevo imperio',0.0 , 'movie',null,4,4,0,0,'2016-07-15',0,0);
--Media user relation
INSERT INTO Media_User_Relation(CALIFICACION, ENDED, FAVORITO, VIENDO, MEDIA_ID, USER_ID)
VALUES(0, TRUE, TRUE, FALSE, 823464, 2);
INSERT INTO Media_User_Relation(CALIFICACION, ENDED, FAVORITO, VIENDO, MEDIA_ID, USER_ID)
VALUES(0, TRUE, TRUE, FALSE, 823464, 3);
INSERT INTO Media_User_Relation(CALIFICACION, ENDED, FAVORITO, VIENDO, MEDIA_ID, USER_ID)
VALUES(0, TRUE, TRUE, FALSE, 823464, 4);
INSERT INTO Media_User_Relation(CALIFICACION, ENDED, FAVORITO, VIENDO, MEDIA_ID, USER_ID)
VALUES(0, TRUE, TRUE, FALSE, 823464, 5);
--user2
INSERT INTO Lista_Media(LISTA_ID, MEDIA_ID) VALUES (1, 823464);
INSERT INTO Lista_Media(LISTA_ID, MEDIA_ID) VALUES (3, 823464);
--user3
INSERT INTO Lista_Media(LISTA_ID, MEDIA_ID) VALUES (7, 823464);
INSERT INTO Lista_Media(LISTA_ID, MEDIA_ID) VALUES (9, 823464);
--user4
INSERT INTO Lista_Media(LISTA_ID, MEDIA_ID) VALUES (10, 823464);
INSERT INTO Lista_Media(LISTA_ID, MEDIA_ID) VALUES (12, 823464);
--user5
INSERT INTO Lista_Media(LISTA_ID, MEDIA_ID) VALUES (13, 823464);
INSERT INTO Lista_Media(LISTA_ID, MEDIA_ID) VALUES (15, 823464);

--El reino del planeta de los simios -------------------------------------------------------
INSERT INTO Media (ID, API, COVER_IMAGE_URL, BACKDROP_IMAGE_URL, DESCRIPCION, NOMBRE, RATING, TIPO, FATHER_ID,NUM_FAVS, NUM_VISTO,NUM_LISTAS,NUM_VIENDO,FECHA,ORDEN,NUM_CHILD)
VALUES (653346,'TMDB','https://image.tmdb.org/t/p/original/r8L3fUvftNeqPMCITdXJfiXbFBU.jpg', 'https://image.tmdb.org/t/p/original/fqv8v6AycXKsivp1T5yKtLbGXce.jpg',
'Ambientada varias generaciones en el futuro tras el reinado de César, en la que los simios son la especie dominante que vive en armonía y los humanos se han visto reducidos a vivir en la sombra. Mientras un nuevo y tiránico líder simio construye su imperio, un joven simio emprende un angustioso viaje que le llevará a cuestionarse todo lo que sabe sobre el pasado y a tomar decisiones que definirán el futuro de simios y humanos por igual.'
,'El reino del planeta de los simios',0.0 , 'movie',null,4,4,0,0, '2024-05-08',0,0);
--Media user relation
INSERT INTO Media_User_Relation(CALIFICACION, ENDED, FAVORITO, VIENDO, MEDIA_ID, USER_ID)
VALUES(0, TRUE, TRUE, FALSE, 653346, 2);
INSERT INTO Media_User_Relation(CALIFICACION, ENDED, FAVORITO, VIENDO, MEDIA_ID, USER_ID)
VALUES(0, TRUE, TRUE, FALSE, 653346, 3);
INSERT INTO Media_User_Relation(CALIFICACION, ENDED, FAVORITO, VIENDO, MEDIA_ID, USER_ID)
VALUES(0, TRUE, TRUE, FALSE, 653346, 4);
INSERT INTO Media_User_Relation(CALIFICACION, ENDED, FAVORITO, VIENDO, MEDIA_ID, USER_ID)
VALUES(0, TRUE, TRUE, FALSE, 653346, 5);
--user2
INSERT INTO Lista_Media(LISTA_ID, MEDIA_ID) VALUES (1, 653346);
INSERT INTO Lista_Media(LISTA_ID, MEDIA_ID) VALUES (3, 653346);
--user3
INSERT INTO Lista_Media(LISTA_ID, MEDIA_ID) VALUES (7, 653346);
INSERT INTO Lista_Media(LISTA_ID, MEDIA_ID) VALUES (9, 653346);
--user4
INSERT INTO Lista_Media(LISTA_ID, MEDIA_ID) VALUES (10, 653346);
INSERT INTO Lista_Media(LISTA_ID, MEDIA_ID) VALUES (12, 653346);
--user5
INSERT INTO Lista_Media(LISTA_ID, MEDIA_ID) VALUES (13, 653346);
INSERT INTO Lista_Media(LISTA_ID, MEDIA_ID) VALUES (15, 653346);

--Dune: Parte dos -------------------------------------------------------
INSERT INTO Media (ID, API, COVER_IMAGE_URL, BACKDROP_IMAGE_URL, DESCRIPCION, NOMBRE, RATING, TIPO, FATHER_ID,NUM_FAVS, NUM_VISTO,NUM_LISTAS,NUM_VIENDO,FECHA,ORDEN,NUM_CHILD)
VALUES (693134,'TMDB','https://image.tmdb.org/t/p/original/6o5cJjA4srfvU52UKWaqPUuPPgl.jpg', 'https://image.tmdb.org/t/p/original/xOMo8BRK7PfcJv9JCnx7s5hj0PX.jpg',
'Sigue el viaje mítico de Paul Atreides mientras se une a Chani y los Fremen en una guerra de venganza contra los conspiradores que destruyeron a su familia. Al enfrentarse a una elección entre el amor de su vida y el destino del universo conocido, Paul se esfuerza por evitar un futuro terrible que solo él puede prever.'
,'Dune: Parte dos',0.0 , 'movie',null,4,4,0,0,'2024-02-27',0,0);
--Media user relation
INSERT INTO Media_User_Relation(CALIFICACION, ENDED, FAVORITO, VIENDO, MEDIA_ID, USER_ID)
VALUES(0, TRUE, TRUE, FALSE, 693134, 2);
INSERT INTO Media_User_Relation(CALIFICACION, ENDED, FAVORITO, VIENDO, MEDIA_ID, USER_ID)
VALUES(0, TRUE, TRUE, FALSE, 693134, 3);
INSERT INTO Media_User_Relation(CALIFICACION, ENDED, FAVORITO, VIENDO, MEDIA_ID, USER_ID)
VALUES(0, TRUE, TRUE, FALSE, 693134, 4);
INSERT INTO Media_User_Relation(CALIFICACION, ENDED, FAVORITO, VIENDO, MEDIA_ID, USER_ID)
VALUES(0, TRUE, TRUE, FALSE, 693134, 5);
--user2
INSERT INTO Lista_Media(LISTA_ID, MEDIA_ID) VALUES (1, 693134);
INSERT INTO Lista_Media(LISTA_ID, MEDIA_ID) VALUES (3, 693134);
--user3
INSERT INTO Lista_Media(LISTA_ID, MEDIA_ID) VALUES (7, 693134);
INSERT INTO Lista_Media(LISTA_ID, MEDIA_ID) VALUES (9, 693134);
--user4
INSERT INTO Lista_Media(LISTA_ID, MEDIA_ID) VALUES (10, 693134);
INSERT INTO Lista_Media(LISTA_ID, MEDIA_ID) VALUES (12, 693134);
--user5
INSERT INTO Lista_Media(LISTA_ID, MEDIA_ID) VALUES (13, 693134);
INSERT INTO Lista_Media(LISTA_ID, MEDIA_ID) VALUES (15, 693134);

--Godzilla Minus One -------------------------------------------------------
INSERT INTO Media (ID, API, COVER_IMAGE_URL, BACKDROP_IMAGE_URL, DESCRIPCION, NOMBRE, RATING, TIPO, FATHER_ID,NUM_FAVS, NUM_VISTO,NUM_LISTAS,NUM_VIENDO,FECHA,ORDEN,NUM_CHILD)
VALUES (940721,'TMDB','https://image.tmdb.org/t/p/original/q35kdC8ci9Fm2gVQazJdsohtGpD.jpg', 'https://image.tmdb.org/t/p/original/fY3lD0jM5AoHJMunjGWqJ0hRteI.jpg',
'En el Japón de la posguerra surge un nuevo terror. ¿Podrán sobrevivir las personas devastadas... y mucho menos defenderse?'
,'Godzilla Minus One',0.0 , 'movie',null,4,4,0,0,'2023-11-03',0,0);
--Media user relation
INSERT INTO Media_User_Relation(CALIFICACION, ENDED, FAVORITO, VIENDO, MEDIA_ID, USER_ID)
VALUES(0, TRUE, TRUE, FALSE, 940721, 2);
INSERT INTO Media_User_Relation(CALIFICACION, ENDED, FAVORITO, VIENDO, MEDIA_ID, USER_ID)
VALUES(0, TRUE, TRUE, FALSE, 940721, 3);
INSERT INTO Media_User_Relation(CALIFICACION, ENDED, FAVORITO, VIENDO, MEDIA_ID, USER_ID)
VALUES(0, TRUE, TRUE, FALSE, 940721, 4);
INSERT INTO Media_User_Relation(CALIFICACION, ENDED, FAVORITO, VIENDO, MEDIA_ID, USER_ID)
VALUES(0, TRUE, TRUE, FALSE, 940721, 5);
--user2
INSERT INTO Lista_Media(LISTA_ID, MEDIA_ID) VALUES (1, 940721);
INSERT INTO Lista_Media(LISTA_ID, MEDIA_ID) VALUES (3, 940721);
--user3
INSERT INTO Lista_Media(LISTA_ID, MEDIA_ID) VALUES (7, 940721);
INSERT INTO Lista_Media(LISTA_ID, MEDIA_ID) VALUES (9, 940721);
--user4
INSERT INTO Lista_Media(LISTA_ID, MEDIA_ID) VALUES (10, 940721);
INSERT INTO Lista_Media(LISTA_ID, MEDIA_ID) VALUES (12, 940721);
--user5
INSERT INTO Lista_Media(LISTA_ID, MEDIA_ID) VALUES (13, 940721);
INSERT INTO Lista_Media(LISTA_ID, MEDIA_ID) VALUES (15, 940721);

--Garfield: La película -------------------------------------------------------
INSERT INTO Media (ID, API, COVER_IMAGE_URL, BACKDROP_IMAGE_URL, DESCRIPCION, NOMBRE, RATING, TIPO, FATHER_ID,NUM_FAVS, NUM_VISTO,NUM_LISTAS,NUM_VIENDO,FECHA,ORDEN,NUM_CHILD)
VALUES (748783,'TMDB','https://image.tmdb.org/t/p/original/jUSzf612WVRrCRJmMAxXjb7yi9r.jpg', 'https://image.tmdb.org/t/p/original/v5XyXZe8FADw8iHupB4L7QOAwH9.jpg',
'El mundialmente famoso Garfield, el gato casero que odia los lunes y que adora la lasaña, está a punto de vivir una aventura ¡en el salvaje mundo exterior! Tras una inesperada reunión con su largamente perdido padre - el desaliñado gato callejero Vic - Garfield y su amigo canino Odie se ven forzados a abandonar sus perfectas y consentidas vidas al unirse a Vic en un hilarante y muy arriesgado atraco.'
,'Garfield: La película',0.0 , 'movie',null,4,4,0,0,'2024-04-30',0,0);
--Media user relation
INSERT INTO Media_User_Relation(CALIFICACION, ENDED, FAVORITO, VIENDO, MEDIA_ID, USER_ID)
VALUES(0, TRUE, TRUE, FALSE, 748783, 2);
INSERT INTO Media_User_Relation(CALIFICACION, ENDED, FAVORITO, VIENDO, MEDIA_ID, USER_ID)
VALUES(0, TRUE, TRUE, FALSE, 748783, 3);
INSERT INTO Media_User_Relation(CALIFICACION, ENDED, FAVORITO, VIENDO, MEDIA_ID, USER_ID)
VALUES(0, TRUE, TRUE, FALSE, 748783, 4);
INSERT INTO Media_User_Relation(CALIFICACION, ENDED, FAVORITO, VIENDO, MEDIA_ID, USER_ID)
VALUES(0, TRUE, TRUE, FALSE, 748783, 5);
--user2
INSERT INTO Lista_Media(LISTA_ID, MEDIA_ID) VALUES (1, 748783);
INSERT INTO Lista_Media(LISTA_ID, MEDIA_ID) VALUES (3, 748783);
--user3
INSERT INTO Lista_Media(LISTA_ID, MEDIA_ID) VALUES (7, 748783);
INSERT INTO Lista_Media(LISTA_ID, MEDIA_ID) VALUES (9, 748783);
--user4
INSERT INTO Lista_Media(LISTA_ID, MEDIA_ID) VALUES (10, 748783);
INSERT INTO Lista_Media(LISTA_ID, MEDIA_ID) VALUES (12, 748783);
--user5
INSERT INTO Lista_Media(LISTA_ID, MEDIA_ID) VALUES (13, 748783);
INSERT INTO Lista_Media(LISTA_ID, MEDIA_ID) VALUES (15, 748783);

--COMENTARIOS--------------------------------------------------------------------

INSERT INTO Comment (ID, DATE_SENT, REPORT, TEXT, AUTHOR_ID, MEDIA_ID, DELETED)
VALUES (1, CURRENT_DATE, FALSE, 'el prota es un tonto', 4, 66732, FALSE);

INSERT INTO Comment (ID, DATE_SENT, REPORT, TEXT, AUTHOR_ID, MEDIA_ID, DELETED)
VALUES (2, CURRENT_DATE, TRUE, 'vendo opel corsa', 5, 66732, FALSE);

INSERT INTO Comment (ID, DATE_SENT, REPORT, TEXT, AUTHOR_ID, MEDIA_ID, DELETED, FATHER_ID)
VALUES (3, CURRENT_DATE, FALSE, 'vendo opel corsa', 2,66732, FALSE, 1);


-- start id numbering from a value that is larger than any assigned above
ALTER SEQUENCE "PUBLIC"."GEN" RESTART WITH 1024;
