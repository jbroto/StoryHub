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

--LISTAS DE USERS
INSERT INTO Lista (ID, CATEGORIES, CONTADOR, IS_PUBLIC, NAME, AUTHOR_ID)
VALUES (1,'', 1, FALSE,'favoritos', 2);
INSERT INTO Lista (ID, CATEGORIES, CONTADOR, IS_PUBLIC, NAME, AUTHOR_ID)
VALUES (2,'', 1, FALSE,'viendo', 2);
INSERT INTO Lista (ID, CATEGORIES, CONTADOR, IS_PUBLIC, NAME, AUTHOR_ID)
VALUES (3,'', 1, FALSE,'terminado', 2);

INSERT INTO Lista (ID, CATEGORIES, CONTADOR, IS_PUBLIC, NAME, AUTHOR_ID)
VALUES (7,'', 0, FALSE,'favoritos', 3);
INSERT INTO Lista (ID, CATEGORIES, CONTADOR, IS_PUBLIC, NAME, AUTHOR_ID)
VALUES (8,'', 0, FALSE,'viendo', 3);
INSERT INTO Lista (ID, CATEGORIES, CONTADOR, IS_PUBLIC, NAME, AUTHOR_ID)
VALUES (9,'', 0, FALSE,'terminado', 3);

INSERT INTO Lista (ID, CATEGORIES, CONTADOR, IS_PUBLIC, NAME, AUTHOR_ID)
VALUES (10,'', 0, FALSE,'favoritos', 4);
INSERT INTO Lista (ID, CATEGORIES, CONTADOR, IS_PUBLIC, NAME, AUTHOR_ID)
VALUES (11,'', 0, FALSE,'viendo', 4);
INSERT INTO Lista (ID, CATEGORIES, CONTADOR, IS_PUBLIC, NAME, AUTHOR_ID)
VALUES (12,'', 0, FALSE,'terminado', 4);

INSERT INTO Lista (ID, CATEGORIES, CONTADOR, IS_PUBLIC, NAME, AUTHOR_ID)
VALUES (13,'', 0, FALSE,'favoritos', 5);
INSERT INTO Lista (ID, CATEGORIES, CONTADOR, IS_PUBLIC, NAME, AUTHOR_ID)
VALUES (14,'', 0, FALSE,'viendo', 5);
INSERT INTO Lista (ID, CATEGORIES, CONTADOR, IS_PUBLIC, NAME, AUTHOR_ID)
VALUES (15,'', 0, FALSE,'terminado', 5);

--LISTAS DE EL ADMIN
INSERT INTO Lista (ID, CATEGORIES, CONTADOR, IS_PUBLIC, NAME, AUTHOR_ID)
VALUES (4,'', 1, FALSE,'favoritos', 1);

INSERT INTO Lista (ID, CATEGORIES, CONTADOR, IS_PUBLIC, NAME, AUTHOR_ID)
VALUES (5,'', 1, FALSE,'viendo', 1);

INSERT INTO Lista (ID, CATEGORIES, CONTADOR, IS_PUBLIC, NAME, AUTHOR_ID)
VALUES (6,'', 1, FALSE,'terminado', 1);

INSERT INTO Media (ID, API, COVER_IMAGE_URL, BACKDROP_IMAGE_URL, DESCRIPCION, NOMBRE, RATING, TIPO, FATHER_ID)
VALUES (66732,'TMDB','https://image.tmdb.org/t/p/original/wHhjZMlYGT9yUEyGpP9jmR6Jq3I.jpg', 'https://image.tmdb.org/t/p/original/56v2KjBlU4XaOv9rVYEQypROD7P.jpg',
'A raíz de la desaparición de un niño, un pueblo desvela un misterio relacionado con experimentos secretos, fuerzas sobrenaturales aterradoras y una niña muy extraña.'
,'Stranger Things',8.613000 , 'tv',null);

INSERT INTO Media_User_Relation(CALIFICACION, ENDED, FAVORITO, VIENDO, MEDIA_ID, USER_ID)
VALUES(4, TRUE, TRUE, TRUE, 66732, 2);

INSERT INTO Lista_Media(LISTA_ID, MEDIA_ID)
VALUES (1, 66732);

INSERT INTO Lista_Media(LISTA_ID, MEDIA_ID)
VALUES (2, 66732);

INSERT INTO Lista_Media(LISTA_ID, MEDIA_ID)
VALUES (3, 66732);

--INSERT INTO Comment (AUTHOR_ID, MEDIA_ID, TEXT, DATE_SENT, REPORT)
--VALUES (2, 66732, 'buenisima serie', CURRENT_DATE, FALSE);


-- start id numbering from a value that is larger than any assigned above
ALTER SEQUENCE "PUBLIC"."GEN" RESTART WITH 1024;
