-- insert admin (username a, password aa)
INSERT INTO IWUser (id, enabled, roles, username, password)
VALUES (1, TRUE, 'ADMIN,USER', 'a',
    '{bcrypt}$2a$10$2BpNTbrsarbHjNsUWgzfNubJqBRf.0Vz9924nRSHBqlbPKerkgX.W');
INSERT INTO IWUser (id, enabled, roles, username, password)
VALUES (2, TRUE, 'USER', 'b',
    '{bcrypt}$2a$10$2BpNTbrsarbHjNsUWgzfNubJqBRf.0Vz9924nRSHBqlbPKerkgX.W');

INSERT INTO Lista (ID, CATEGORIES, IS_PUBLIC, NAME, AUTHOR_ID)
VALUES (1,'', FALSE,'favoritos', 2);

INSERT INTO Lista (ID, CATEGORIES, IS_PUBLIC, NAME, AUTHOR_ID)
VALUES (2,'', FALSE,'viendo', 2);

INSERT INTO Lista (ID, CATEGORIES, IS_PUBLIC, NAME, AUTHOR_ID)
VALUES (3,'', FALSE,'terminado', 2);

INSERT INTO Media (ID, API, COVER_IMAGE_URL, NOMBRE, RATING, TIPO, FATHER_ID)
VALUES (66732,'TMDB','/wHhjZMlYGT9yUEyGpP9jmR6Jq3I.jpg', 'Stranger Things',8.613000 , 'tv',null);

INSERT INTO Media_User_Relation(CALIFICACION, ENDED, FAVORITO, VIENDO, MEDIA_ID, USER_ID)
VALUES(null, TRUE, TRUE, TRUE, 66732, 2);

INSERT INTO Lista_Media(LISTA_ID, MEDIA_ID)
VALUES (1, 66732);

INSERT INTO Lista_Media(LISTA_ID, MEDIA_ID)
VALUES (2, 66732);

INSERT INTO Lista_Media(LISTA_ID, MEDIA_ID)
VALUES (3, 66732);

-- start id numbering from a value that is larger than any assigned above
ALTER SEQUENCE "PUBLIC"."GEN" RESTART WITH 1024;
