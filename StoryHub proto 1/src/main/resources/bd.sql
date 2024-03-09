CREATE TABLE  StoryHub.User (
  id INT NOT NULL,
  enabled TINYINT NOT NULL,
  roles VARCHAR(45) NOT NULL,
  email VARCHAR(45) PRIMARY KEY NOT NULL,
  password VARCHAR(45) NOT NULL,
  nombre VARCHAR(45) NOT NULL,
  primer_apellido VARCHAR(45) NOT NULL,
  segundo_apellido VARCHAR(45) NOT NULL,
  username VARCHAR(45) NOT NULL
);

CREATE TABLE  StoryHub.List (
  idList INT NOT NULL,
  nombre VARCHAR(45) NOT NULL,
  public TINYINT NOT NULL,
  categorias VARCHAR(45) NOT NULL,
  User_author VARCHAR(45) NOT NULL,
  PRIMARY KEY (idList, User_author),
  FOREIGN KEY (User_author) REFERENCES StoryHub.User(email) ON UPDATE NO ACTION ON DELETE NO ACTION
);

CREATE TABLE  StoryHub.Media (
  id VARCHAR(45) NOT NULL,
  Padre_id VARCHAR(45) NOT NULL,
  acabado TINYINT NOT NULL,
  index VARCHAR(45) NOT NULL,
  valoracion DOUBLE NOT NULL,
  PRIMARY KEY (id, Padre_id),
  UNIQUE (id),
  FOREIGN KEY (Padre_id) REFERENCES StoryHub.Media(id) ON UPDATE NO ACTION ON DELETE NO ACTION
);

CREATE TABLE  StoryHub.Comments (
  id INT NOT NULL,
  fecha DATETIME NOT NULL,
  valoracion DOUBLE NOT NULL,
  texto MEDIUMTEXT,
  likes INT,
  Media_target VARCHAR(45) NOT NULL,
  User_author VARCHAR(45) NOT NULL,
  PRIMARY KEY (id, Media_target, User_author),
  FOREIGN KEY (Media_target) REFERENCES StoryHub.Media(id) ON UPDATE NO ACTION ON DELETE NO ACTION,
  FOREIGN KEY (User_author) REFERENCES StoryHub.User(email) ON UPDATE NO ACTION ON DELETE NO ACTION
);

CREATE TABLE  StoryHub.Reply (
  idReply INT NOT NULL,
  User_reply VARCHAR(45) NOT NULL,
  fecha DATETIME NOT NULL,
  texto MEDIUMTEXT NOT NULL,
  Comments_id INT NOT NULL,
  Comments_Media_target VARCHAR(45) NOT NULL,
  Comments_User_author VARCHAR(45) NOT NULL,
  PRIMARY KEY (idReply, User_reply, Comments_id, Comments_Media_target, Comments_User_author),
  FOREIGN KEY (Comments_id, Comments_Media_target, Comments_User_author) REFERENCES StoryHub.Comments(id, Media_target, User_author) ON UPDATE NO ACTION ON DELETE NO ACTION,
  FOREIGN KEY (User_reply) REFERENCES StoryHub.User(email) ON UPDATE NO ACTION ON DELETE NO ACTION
);

CREATE TABLE  StoryHub.List_has_Media (
  List_idList INT NOT NULL,
  List_User_email VARCHAR(45) NOT NULL,
  Media_id VARCHAR(45) NOT NULL,
  Media_Padre_id VARCHAR(45) NOT NULL,
  PRIMARY KEY (List_idList, List_User_email, Media_id, Media_Padre_id),
  FOREIGN KEY (List_idList, List_User_email) REFERENCES StoryHub.List(idList, User_author) ON UPDATE NO ACTION ON DELETE NO ACTION,
  FOREIGN KEY (Media_id, Media_Padre_id) REFERENCES StoryHub.Media(id, Padre_id) ON UPDATE NO ACTION ON DELETE NO ACTION
);
