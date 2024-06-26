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
VALUES (1,'', 1, FALSE,'Favoritos', 2);
INSERT INTO Lista (ID, CATEGORIES, CONTADOR, IS_PUBLIC, NAME, AUTHOR_ID)
VALUES (2,'', 1, FALSE,'Viendo', 2);
INSERT INTO Lista (ID, CATEGORIES, CONTADOR, IS_PUBLIC, NAME, AUTHOR_ID)
VALUES (3,'', 1, FALSE,'Terminado', 2);

INSERT INTO Lista (ID, CATEGORIES, CONTADOR, IS_PUBLIC, NAME, AUTHOR_ID)
VALUES (7,'', 0, FALSE,'Favoritos', 3);
INSERT INTO Lista (ID, CATEGORIES, CONTADOR, IS_PUBLIC, NAME, AUTHOR_ID)
VALUES (8,'', 0, FALSE,'Viendo', 3);
INSERT INTO Lista (ID, CATEGORIES, CONTADOR, IS_PUBLIC, NAME, AUTHOR_ID)
VALUES (9,'', 0, FALSE,'Terminado', 3);

INSERT INTO Lista (ID, CATEGORIES, CONTADOR, IS_PUBLIC, NAME, AUTHOR_ID)
VALUES (10,'', 0, FALSE,'Favoritos', 4);
INSERT INTO Lista (ID, CATEGORIES, CONTADOR, IS_PUBLIC, NAME, AUTHOR_ID)
VALUES (11,'', 0, FALSE,'Viendo', 4);
INSERT INTO Lista (ID, CATEGORIES, CONTADOR, IS_PUBLIC, NAME, AUTHOR_ID)
VALUES (12,'', 0, FALSE,'Terminado', 4);

INSERT INTO Lista (ID, CATEGORIES, CONTADOR, IS_PUBLIC, NAME, AUTHOR_ID)
VALUES (13,'', 0, FALSE,'Favoritos', 5);
INSERT INTO Lista (ID, CATEGORIES, CONTADOR, IS_PUBLIC, NAME, AUTHOR_ID)
VALUES (14,'', 0, FALSE,'Viendo', 5);
INSERT INTO Lista (ID, CATEGORIES, CONTADOR, IS_PUBLIC, NAME, AUTHOR_ID)
VALUES (15,'', 0, FALSE,'Terminado', 5);

--LISTAS DE EL ADMIN
INSERT INTO Lista (ID, CATEGORIES, CONTADOR, IS_PUBLIC, NAME, AUTHOR_ID)
VALUES (4,'', 1, TRUE,'Favoritos', 1);

INSERT INTO Lista (ID, CATEGORIES, CONTADOR, IS_PUBLIC, NAME, AUTHOR_ID)
VALUES (5,'', 1, FALSE,'Viendo', 1);

INSERT INTO Lista (ID, CATEGORIES, CONTADOR, IS_PUBLIC, NAME, AUTHOR_ID)
VALUES (6,'', 1, FALSE,'Terminado', 1);

INSERT INTO Lista (ID, CATEGORIES, CONTADOR, IS_PUBLIC, NAME, AUTHOR_ID)
VALUES (100,'', 1, TRUE,'Accion', 1);

--Contenido almacenado en la BD
INSERT INTO Media (ID, API, COVER_IMAGE_URL, BACKDROP_IMAGE_URL, DESCRIPCION, NOMBRE, RATING, TIPO, FATHER_ID,NUM_FAVS, NUM_VISTO,NUM_LISTAS,NUM_VIENDO)
VALUES (66732,'TMDB','https://image.tmdb.org/t/p/original/wHhjZMlYGT9yUEyGpP9jmR6Jq3I.jpg', 'https://image.tmdb.org/t/p/original/56v2KjBlU4XaOv9rVYEQypROD7P.jpg',
'A raíz de la desaparición de un niño, un pueblo desvela un misterio relacionado con experimentos secretos, fuerzas sobrenaturales aterradoras y una niña muy extraña.'
,'Stranger Things',0.0 , 'tv',null,1,1,0,1);

INSERT INTO Media_User_Relation(CALIFICACION, ENDED, FAVORITO, VIENDO, MEDIA_ID, USER_ID)
VALUES(0, TRUE, TRUE, TRUE, 66732, 2);

--media en la listas basicas del user 2
INSERT INTO Lista_Media(LISTA_ID, MEDIA_ID) VALUES (1, 66732);
INSERT INTO Lista_Media(LISTA_ID, MEDIA_ID) VALUES (2, 66732);
INSERT INTO Lista_Media(LISTA_ID, MEDIA_ID) VALUES (3, 66732);

INSERT INTO Comment (ID, DATE_SENT, REPORT, TEXT, AUTHOR_ID, MEDIA_ID, DELETED)
VALUES (1, CURRENT_DATE, FALSE, 'el prota es un tonto', 4, 66732, FALSE);

INSERT INTO Comment (ID, DATE_SENT, REPORT, TEXT, AUTHOR_ID, MEDIA_ID, DELETED)
VALUES (2, CURRENT_DATE, TRUE, 'vendo opel corsa', 5, 66732, FALSE);

INSERT INTO Comment (ID, DATE_SENT, REPORT, TEXT, AUTHOR_ID, MEDIA_ID, DELETED, FATHER_ID)
VALUES (3, CURRENT_DATE, FALSE, 'vendo opel corsa', 2,66732, FALSE, 1);


-- start id numbering from a value that is larger than any assigned above
ALTER SEQUENCE "PUBLIC"."GEN" RESTART WITH 1024;
