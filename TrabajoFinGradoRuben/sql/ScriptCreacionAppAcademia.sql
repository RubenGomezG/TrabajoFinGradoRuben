-- Nombre alumnos/as: Rubén Gómez García
use bu5x9cts_businessplus;

create table Usuarios(
	usuario varchar(50) not null,
    contrasena text not null,
    email varchar(100) unique not null,
    nombre varchar(20) not null,
    apellidos varchar(40) not null,
    telefono integer(15) not null,
    img_perfil varchar(100),
    edad integer(3),
    constraint unico_username unique(usuario)
);

create table Academias(
	cod_academia integer primary key auto_increment,
	usuario varchar(50) not null,
    contrasena text not null,
    email varchar(100) unique not null,
    nombre varchar(20) not null,
    telefono integer(15) not null,
    direccion text not null,
    latitud double not null,
    longitud double not null,
    img_perfil varchar(100)
);

create table Cursos(
	cod_curso integer auto_increment primary key,
    nombre_curso varchar(50) not null,
    fecha_inicio_curso datetime not null,
    fecha_fin_curso datetime not null,
    precio double not null,
    valoracion double not null,
    descripcion text,
    tipo enum('Académico', 'Otros') not null,
    cod_academia integer not null,
    constraint academia_fk foreign key(cod_academia) references Academias(cod_academia) on update cascade on delete cascade,
    constraint chk_Fechas check (fecha_fin_curso > fecha_inicio_curso)
);

create table Inscripciones(
	cod_curso integer not null,
    usuario varchar(50) not null,
    constraint inscripcion_fk PRIMARY KEY (cod_curso, usuario),
    constraint curso_fk foreign key(cod_curso) references Cursos(cod_curso) on update cascade on delete cascade,
    constraint usuario_fk foreign key(usuario) references Usuarios(usuario) on update cascade on delete cascade
);

create table Favoritos(
	cod_curso integer not null,
    usuario varchar(50) not null,
    constraint favorito_fk PRIMARY KEY (cod_curso, usuario),
    constraint cursoFavoritos_fk foreign key(cod_curso) references Cursos(cod_curso) on update cascade on delete cascade,
    constraint usuarioFavoritos_fk foreign key(usuario) references Usuarios(usuario) on update cascade on delete cascade
);

create table Conversaciones (
    cod_conversacion INT AUTO_INCREMENT PRIMARY KEY,
    usuario1_id integer not null,
    usuario2_id varchar(50) not null,
    foreign key (usuario1_id) references Academia(cod_academia) on update cascade on delete cascade,
    foreign key (usuario2_id) references Usuarios(usuario) on update cascade on delete cascade,
    constraint unica_conversacion unique (usuario1_id, usuario2_id)
);

create table Mensajes(
    cod_mensaje INT PRIMARY KEY AUTO_INCREMENT,
    cod_conversacion integer not null,
    sender_username varchar(50),
    sender_cod_academia integer,
    contenido varchar(500) not null,
    timestamp datetime not null,
	foreign key (sender_username) references Usuarios(usuario) on update cascade on delete cascade,
	foreign key (sender_cod_academia) references Academias(cod_academia) on update cascade on delete cascade,
    constraint conversacion_fk foreign key(cod_conversacion) references Conversaciones(cod_conversacion) on update cascade on delete cascade
);

-- Contrasenas de ejemplo: Mikeljorg3@, Luisgold3n@, Pablomonter0@, Javijorg3@, Dguruch4zc@, Velasco9!, Rubengom3z@, Rubenlop3z@, Endika1@, Almudena1@, Rosiros1@
insert into Usuarios(usuario,contrasena,email,nombre,apellidos,telefono,img_perfil,edad) values ('Mikel', 'BA3C9ED3FD0BF7CCBAB132F8AC388DBF2A1B6095BD0CBEC94C83FE3538368AFE', 'mjorgesote@educacion.navarra.es', 'Mikel Aingeru', 'Jorge Soteras', 123456789, 'Mikel.jpg', 33);
insert into Usuarios(usuario,contrasena,email,nombre,apellidos,telefono,img_perfil,edad) values ('LuisDorado', '09908D0D1D5A937C4B9027D90BC806DEBD45A152300D75E664DEDD132A8114BF', 'ldoradogar@educacion.navarra.es', 'Luis', 'Dorado Garcés', 234567891, 'LuisDorado.jpg', 40);
insert into Usuarios(usuario,contrasena,email,nombre,apellidos,telefono,img_perfil,edad) values ('Pablo', 'EECBA7E8EE9991E845862D29A9299339D02B563837E3E9233E2103652459A42A', 'pablomontero@educacion.navarra.es', 'Pablo', 'Montero Quevedo', 345678912, 'Pablo.jpg', 55);
insert into Usuarios(usuario,contrasena,email,nombre,apellidos,telefono,img_perfil,edad) values ('Javi', '0A87B7A653AE14D59E0D767F7FEA9A47F23CBBF47E0BE7892D79ACC66E2F3D73', 'jjorgesote@educacion.navarra.es', 'Javier', 'Jorge Soteras', 456789123, 'Javi.jpg', 35);
insert into Usuarios(usuario,contrasena,email,nombre,apellidos,telefono,img_perfil,edad) values ('David', '83DC450CF8368F12145172FD0773862C7E797EF8917091B1B378B0F71D3B6749', 'dguruchazc@educacion.navarra.es', 'David', 'Gurutxarri Azcona', 567891234, 'David.jpg', 42);
insert into Usuarios(usuario,contrasena,email,nombre,apellidos,telefono,img_perfil,edad) values ('Pedro', '94F91FBE5BF6AA794E109B72BD0B8B148DCCF60A6C325499A4DB5AD4D59DEF55', 'pvelasczuf@educacion.navarra.es', 'Pedro José', 'Velasco Zufia', 678912345, 'Pedro.jpg', 34);
insert into Usuarios(usuario,contrasena,email,nombre,apellidos,telefono,img_perfil,edad) values ('RubenG', '6CF18B710ACAACDF3ED76103D1CABDF08F09C90027EC944C8C9311D24842A462', 'rgomezgarc@educacion.navarra.es', 'Rubén', 'Gómez García', 789123456, 'RubenG.jpg', 30);
insert into Usuarios(usuario,contrasena,email,nombre,apellidos,telefono,img_perfil,edad) values ('RubenL', '52ADAD7CA1244DC9D7A1A0DF1782F27833C61F3AB1D0C8932C04C72A14555226', 'rlopezdled@educacion.navarra.es', 'Rubén', 'López-Davalillo Ledesma', 891234567, 'RubenL.jpg', 20);
insert into Usuarios(usuario,contrasena,email,nombre,apellidos,telefono,img_perfil,edad) values ('Cris', 'EB7FCF146BD067B6A21688911B01F5A684FF22783FFBDEF7864C25518437C3DE', 'clopezlusa1@educacion.navarra.es', 'Cristina', 'López Lusarreta', 912345678, 'Cris.jpg', 19);
insert into Usuarios(usuario,contrasena,email,nombre,apellidos,telefono,img_perfil,edad) values ('Endika', 'C8ECCA19CACA694E980BCA38F60FA2B9B2FDA615DD06408401884D47308DCAD4', 'eeguinogar@educacion.navarra.es', 'Endika', 'Eguino Garbayo', 012345678, 'Endika.jpg', 20);
insert into Usuarios(usuario,contrasena,email,nombre,apellidos,telefono,img_perfil,edad) values ('Almudena', '7DD36242A3437E327718A0619C336425ACC2A94E527DE474B3AE8EC804C0698D', 'aiparracas1@educacion.navarra.es', 'Almudena', 'Iparraguirre Castillo', 629736136, 'Almudena.jpg', 19);
insert into Usuarios(usuario,contrasena,email,nombre,apellidos,telefono,img_perfil,edad) values ('Rosi', 'F7018F16C5B4557C780716A6183050DCEC55630365FB36485E1A97646C5CC5FA', 'rosirosi@educacion.navarra.es', 'Rosi', 'Rosi Rosi', 012345678, 'Endika.jpg', 10);

INSERT INTO Academias (usuario, contrasena, email, nombre, telefono, direccion, latitud, longitud, img_perfil) VALUES ('academia1', 'password1', 'academia1@example.com', 'Academia 1', 123456789, 'Calle Academia 1', 0.0, 0.0, "academia1.jpg");

INSERT INTO Cursos (nombre_curso, fecha_inicio_curso, fecha_fin_curso, precio, valoracion, descripcion, tipo, cod_academia)
VALUES ('Curso 1', '2024-03-01', '2024-04-01', 100.00, 4.5, 'Descripción del curso 1','Académico', 1);

INSERT INTO Cursos (nombre_curso, fecha_inicio_curso, fecha_fin_curso, precio, valoracion, descripcion, tipo, cod_academia)
VALUES ('Curso 2', '2024-03-15', '2024-04-15', 120.00, 4.0, 'Descripción del curso 2','Académico', 1);

INSERT INTO Cursos (nombre_curso, fecha_inicio_curso, fecha_fin_curso, precio, valoracion, descripcion, tipo, cod_academia)
VALUES ('Curso 3', '2024-04-01', '2024-05-01', 80.00, 4.2, 'Descripción del curso 3','Otros', 1);

INSERT INTO Cursos (nombre_curso, fecha_inicio_curso, fecha_fin_curso, precio, valoracion, descripcion, tipo, cod_academia)
VALUES ('Curso 4', '2024-04-15', '2024-05-15', 150.00, 4.8, 'Descripción del curso 4','Académico', 1);

INSERT INTO Cursos (nombre_curso, fecha_inicio_curso, fecha_fin_curso, precio, valoracion, descripcion, tipo, cod_academia)
VALUES ('Curso 5', '2024-05-01', '2024-06-01', 90.00, 4.6, 'Descripción del curso 5','Otros', 1);

INSERT INTO Cursos (nombre_curso, fecha_inicio_curso, fecha_fin_curso, precio, valoracion, descripcion, tipo, cod_academia)
VALUES ('Curso 6', '2024-05-15', '2024-06-15', 130.00, 4.3, 'Descripción del curso 6','Académico', 1);

INSERT INTO Cursos (nombre_curso, fecha_inicio_curso, fecha_fin_curso, precio, valoracion, descripcion, tipo, cod_academia)
VALUES ('Curso 7', '2024-06-01', '2024-07-01', 85.00, 4.7, 'Descripción del curso 7','Otros', 1);

INSERT INTO Cursos (nombre_curso, fecha_inicio_curso, fecha_fin_curso, precio, valoracion, descripcion, tipo, cod_academia)
VALUES ('Curso 8', '2024-06-15', '2024-07-15', 140.00, 4.1, 'Descripción del curso 8','Académico', 1);

INSERT INTO Cursos (nombre_curso, fecha_inicio_curso, fecha_fin_curso, precio, valoracion, descripcion, tipo, cod_academia)
VALUES ('Curso 9', '2024-07-01', '2024-08-01', 95.00, 4.9, 'Descripción del curso 9','Otros', 1);

INSERT INTO Cursos (nombre_curso, fecha_inicio_curso, fecha_fin_curso, precio, valoracion, descripcion, tipo, cod_academia)
VALUES ('Curso 10', '2024-07-15', '2024-08-15', 160.00, 4.4, 'Descripción del curso 10','Académico', 1);

INSERT INTO Inscripciones (cod_curso, usuario) VALUES (1, 'Mikel');
INSERT INTO Inscripciones (cod_curso, usuario) VALUES (2, 'LuisDorado');
INSERT INTO Inscripciones (cod_curso, usuario) VALUES (3, 'Pablo');
INSERT INTO Inscripciones (cod_curso, usuario) VALUES (4, 'Javi');
INSERT INTO Inscripciones (cod_curso, usuario) VALUES (5, 'David');
INSERT INTO Inscripciones (cod_curso, usuario) VALUES (6, 'Pedro');
INSERT INTO Inscripciones (cod_curso, usuario) VALUES (7, 'RubenG');
INSERT INTO Inscripciones (cod_curso, usuario) VALUES (8, 'RubenL');
INSERT INTO Inscripciones (cod_curso, usuario) VALUES (9, 'Cris');
INSERT INTO Inscripciones (cod_curso, usuario) VALUES (10, 'Endika');
INSERT INTO Inscripciones (cod_curso, usuario) VALUES (1, 'Almudena');
INSERT INTO Inscripciones (cod_curso, usuario) VALUES (2, 'Rosi');

INSERT INTO Favoritos (cod_curso, usuario) VALUES (1, 'Endika');
INSERT INTO Favoritos (cod_curso, usuario) VALUES (2, 'Cris');
INSERT INTO Favoritos (cod_curso, usuario) VALUES (3, 'RubenL');
INSERT INTO Favoritos (cod_curso, usuario) VALUES (4, 'RubenG');
INSERT INTO Favoritos (cod_curso, usuario) VALUES (5, 'Pedro');
INSERT INTO Favoritos (cod_curso, usuario) VALUES (6, 'David');
INSERT INTO Favoritos (cod_curso, usuario) VALUES (7, 'Javi');
INSERT INTO Favoritos (cod_curso, usuario) VALUES (8, 'Pablo');
INSERT INTO Favoritos (cod_curso, usuario) VALUES (9, 'LuisDorado');
INSERT INTO Favoritos (cod_curso, usuario) VALUES (10, 'Mikel');
INSERT INTO Favoritos (cod_curso, usuario) VALUES (1, 'Almudena');
INSERT INTO Favoritos (cod_curso, usuario) VALUES (12, 'Rosi');

INSERT INTO Conversaciones (usuario1_id, usuario2_id) VALUES (1, 'Mikel');
INSERT INTO Conversaciones (usuario1_id, usuario2_id) VALUES (1, 'LuisDorado');
INSERT INTO Conversaciones (usuario1_id, usuario2_id) VALUES (1, 'Pablo');
INSERT INTO Conversaciones (usuario1_id, usuario2_id) VALUES (1, 'Javi');
INSERT INTO Conversaciones (usuario1_id, usuario2_id) VALUES (1, 'David');
INSERT INTO Conversaciones (usuario1_id, usuario2_id) VALUES (1, 'Pedro');
INSERT INTO Conversaciones (usuario1_id, usuario2_id) VALUES (1, 'RubenG');
INSERT INTO Conversaciones (usuario1_id, usuario2_id) VALUES (1, 'RubenL');
INSERT INTO Conversaciones (usuario1_id, usuario2_id) VALUES (1, 'Cris');
INSERT INTO Conversaciones (usuario1_id, usuario2_id) VALUES (1, 'Endika');
INSERT INTO Conversaciones (usuario1_id, usuario2_id) VALUES (1, 'Almudena');
INSERT INTO Conversaciones (usuario1_id, usuario2_id) VALUES (1, 'Rosi');

-- Conversación entre Academia1 y 'Mikel'
INSERT INTO Mensajes (cod_mensaje, cod_conversacion, sender_username, sender_cod_academia, contenido, timestamp) VALUES
(1, 1, 'Mikel', NULL, 'Hola, ¿cómo estás?', '2024-05-14 10:00:00'),
INSERT INTO Mensajes (cod_conversacion, sender_username, sender_cod_academia, contenido, timestamp) VALUES
(1, NULL, 1, 'Muy bien, ¿y tú?', '2024-05-14 10:01:00'),
(1, 'Mikel', NULL, 'Estoy bien, gracias.', '2024-05-14 10:02:00'),
(1, NULL, 1, '¿Qué has hecho hoy?', '2024-05-14 10:03:00'),
(1, 'Mikel', NULL, 'He estado trabajando en un proyecto.', '2024-05-14 10:04:00'),
(1, NULL, 1, '¡Qué interesante!', '2024-05-14 10:05:00'),
(1, 'Mikel', NULL, 'Sí, me está gustando mucho.', '2024-05-14 10:06:00'),
(1, NULL, 1, 'Me alegro de oír eso.', '2024-05-14 10:07:00'),
(1, 'Mikel', NULL, 'Gracias.', '2024-05-14 10:08:00'),
(1, NULL, 1, 'De nada.', '2024-05-14 10:09:00');

-- Conversación entre Academia1 y 'LuisDorado'
INSERT INTO Mensajes (cod_conversacion, sender_username, sender_cod_academia, contenido, timestamp) VALUES
(2, 'LuisDorado', NULL, 'Hola, ¿qué tal?', '2024-05-14 10:10:00'),
(2, NULL, 1, 'Bien, ¿y tú?', '2024-05-14 10:11:00'),
(2, 'LuisDorado', NULL, 'Todo bien, gracias.', '2024-05-14 10:12:00'),
(2, NULL, 1, '¿Qué has estado haciendo?', '2024-05-14 10:13:00'),
(2, 'LuisDorado', NULL, 'He estado estudiando.', '2024-05-14 10:14:00'),
(2, NULL, 1, '¡Eso suena bien!', '2024-05-14 10:15:00'),
(2, 'LuisDorado', NULL, 'Sí, ha sido productivo.', '2024-05-14 10:16:00'),
(2, NULL, 1, '¡Genial!', '2024-05-14 10:17:00'),
(2, 'LuisDorado', NULL, 'Gracias.', '2024-05-14 10:18:00'),
(2, NULL, 1, 'De nada.', '2024-05-14 10:19:00');

-- Conversación entre Academia1 y 'Pablo'
INSERT INTO Mensajes (cod_conversacion, sender_username, sender_cod_academia, contenido, timestamp) VALUES
(3, 'Pablo', NULL, 'Hola, ¿cómo estás?', '2024-05-14 10:20:00'),
(3, NULL, 1, 'Bien, ¿y tú?', '2024-05-14 10:21:00'),
(3, 'Pablo', NULL, 'Todo bien, gracias.', '2024-05-14 10:22:00'),
(3, NULL, 1, '¿Qué planes tienes para hoy?', '2024-05-14 10:23:00'),
(3, 'Pablo', NULL, 'Voy a salir a correr.', '2024-05-14 10:24:00'),
(3, NULL, 1, '¡Eso suena divertido!', '2024-05-14 10:25:00'),
(3, 'Pablo', NULL, 'Sí, lo disfruto mucho.', '2024-05-14 10:26:00'),
(3, NULL, 1, 'Me alegra saber eso.', '2024-05-14 10:27:00'),
(3, 'Pablo', NULL, 'Gracias.', '2024-05-14 10:28:00'),
(3, NULL, 1, 'De nada.', '2024-05-14 10:29:00');

-- Conversación entre Academia1 y 'Javi'
INSERT INTO Mensajes (cod_conversacion, sender_username, sender_cod_academia, contenido, timestamp) VALUES
(4, 'Javi', NULL, 'Hola, ¿cómo estás?', '2024-05-14 10:30:00'),
(4, NULL, 1, 'Bien, ¿y tú?', '2024-05-14 10:31:00'),
(4, 'Javi', NULL, 'Todo bien, gracias.', '2024-05-14 10:32:00'),
(4, NULL, 1, '¿Qué has hecho hoy?', '2024-05-14 10:33:00'),
(4, 'Javi', NULL, 'He estado trabajando.', '2024-05-14 10:34:00'),
(4, NULL, 1, '¡Eso suena bien!', '2024-05-14 10:35:00'),
(4, 'Javi', NULL, 'Sí, ha sido un buen día.', '2024-05-14 10:36:00'),
(4, NULL, 1, '¡Genial!', '2024-05-14 10:37:00'),
(4, 'Javi', NULL, 'Gracias.', '2024-05-14 10:38:00'),
(4, NULL, 1, 'De nada.', '2024-05-14 10:39:00');

-- Conversación entre Academia1 y 'David'
INSERT INTO Mensajes (cod_conversacion, sender_username, sender_cod_academia, contenido, timestamp) VALUES
(5, 'David', NULL, 'Hola, ¿qué tal?', '2024-05-14 10:40:00'),
(5, NULL, 1, 'Bien, ¿y tú?', '2024-05-14 10:41:00'),
(5, 'David', NULL, 'Todo bien, gracias.', '2024-05-14 10:42:00'),
(5, NULL, 1, '¿Qué has estado haciendo?', '2024-05-14 10:43:00'),
(5, 'David', NULL, 'He estado leyendo.', '2024-05-14 10:44:00'),
(5, NULL, 1, '¡Eso suena interesante!', '2024-05-14 10:45:00'),
(5, 'David', NULL, 'Sí, ha sido muy educativo.', '2024-05-14 10:46:00'),
(5, NULL, 1, 'Me alegra saber eso.', '2024-05-14 10:47:00'),
(5, 'David', NULL, 'Gracias.', '2024-05-14 10:48:00'),
(5, NULL, 1, 'De nada.', '2024-05-14 10:49:00');

-- Conversación entre Academia1 y 'Pedro'
INSERT INTO Mensajes (cod_conversacion, sender_username, sender_cod_academia, contenido, timestamp) VALUES
(6, 'Pedro', NULL, 'Hola, ¿cómo estás?', '2024-05-14 10:50:00'),
(6, NULL, 1, 'Bien, ¿y tú?', '2024-05-14 10:51:00'),
(6, 'Pedro', NULL, 'Todo bien, gracias.', '2024-05-14 10:52:00'),
(6, NULL, 1, '¿Qué planes tienes para hoy?', '2024-05-14 10:53:00'),
(6, 'Pedro', NULL, 'Voy a salir a caminar.', '2024-05-14 10:54:00'),
(6, NULL, 1, '¡Eso suena genial!', '2024-05-14 10:55:00'),
(6, 'Pedro', NULL, 'Sí, lo es.', '2024-05-14 10:56:00'),
(6, NULL, 1, 'Me alegra saber eso.', '2024-05-14 10:57:00'),
(6, 'Pedro', NULL, 'Gracias.', '2024-05-14 10:58:00'),
(6, NULL, 1, 'De nada.', '2024-05-14 10:59:00');

-- Conversación entre Academia1 y 'RubenG'
INSERT INTO Mensajes (cod_conversacion, sender_username, sender_cod_academia, contenido, timestamp) VALUES
(7, 'RubenG', NULL, 'Hola, ¿cómo estás?', '2024-05-14 11:00:00'),
(7, NULL, 1, 'Bien, ¿y tú?', '2024-05-14 11:01:00'),
(7, 'RubenG', NULL, 'Todo bien, gracias.', '2024-05-14 11:02:00'),
(7, NULL, 1, '¿Qué has hecho hoy?', '2024-05-14 11:03:00'),
(7, 'RubenG', NULL, 'He estado en una reunión.', '2024-05-14 11:04:00'),
(7, NULL, 1, '¡Eso suena interesante!', '2024-05-14 11:05:00'),
(7, 'RubenG', NULL, 'Sí, ha sido útil.', '2024-05-14 11:06:00'),
(7, NULL, 1, 'Me alegra saber eso.', '2024-05-14 11:07:00'),
(7, 'RubenG', NULL, 'Gracias.', '2024-05-14 11:08:00'),
(7, NULL, 1, 'De nada.', '2024-05-14 11:09:00');

-- Conversación entre Academia1 y 'RubenL'
INSERT INTO Mensajes (cod_conversacion, sender_username, sender_cod_academia, contenido, timestamp) VALUES
(8, 'RubenL', NULL, 'Hola, ¿cómo estás?', '2024-05-14 11:10:00'),
(8, NULL, 1, 'Bien, ¿y tú?', '2024-05-14 11:11:00'),
(8, 'RubenL', NULL, 'Todo bien, gracias.', '2024-05-14 11:12:00'),
(8, NULL, 1, '¿Qué has estado haciendo?', '2024-05-14 11:13:00'),
(8, 'RubenL', NULL, 'He estado en clase.', '2024-05-14 11:14:00'),
(8, NULL, 1, '¡Eso suena bien!', '2024-05-14 11:15:00'),
(8, 'RubenL', NULL, 'Sí, lo ha sido.', '2024-05-14 11:16:00'),
(8, NULL, 1, 'Me alegra saber eso.', '2024-05-14 11:17:00'),
(8, 'RubenL', NULL, 'Gracias.', '2024-05-14 11:18:00'),
(8, NULL, 1, 'De nada.', '2024-05-14 11:19:00');

-- Conversación entre Academia1 y 'Cris'
INSERT INTO Mensajes (cod_conversacion, sender_username, sender_cod_academia, contenido, timestamp) VALUES
(9, 'Cris', NULL, 'Hola, ¿cómo estás?', '2024-05-14 11:20:00'),
(9, NULL, 1, 'Bien, ¿y tú?', '2024-05-14 11:21:00'),
(9, 'Cris', NULL, 'Todo bien, gracias.', '2024-05-14 11:22:00'),
(9, NULL, 1, '¿Qué has hecho hoy?', '2024-05-14 11:23:00'),
(9, 'Cris', NULL, 'He estado en el gimnasio.', '2024-05-14 11:24:00'),
(9, NULL, 1, '¡Eso suena genial!', '2024-05-14 11:25:00'),
(9, 'Cris', NULL, 'Sí, me siento muy bien.', '2024-05-14 11:26:00'),
(9, NULL, 1, 'Me alegra saber eso.', '2024-05-14 11:27:00'),
(9, 'Cris', NULL, 'Gracias.', '2024-05-14 11:28:00'),
(9, NULL, 1, 'De nada.', '2024-05-14 11:29:00');

-- Conversación entre Academia1 y 'Endika'
INSERT INTO Mensajes (cod_conversacion, sender_username, sender_cod_academia, contenido, timestamp) VALUES
(10, 'Endika', NULL, 'Hola, ¿cómo estás?', '2024-05-14 11:30:00'),
(10, NULL, 1, 'Bien, ¿y tú?', '2024-05-14 11:31:00'),
(10, 'Endika', NULL, 'Todo bien, gracias.', '2024-05-14 11:32:00'),
(10, NULL, 1, '¿Qué has hecho hoy?', '2024-05-14 11:33:00'),
(10, 'Endika', NULL, 'He estado trabajando.', '2024-05-14 11:34:00'),
(10, NULL, 1, '¡Eso suena interesante!', '2024-05-14 11:35:00'),
(10, 'Endika', NULL, 'Sí, lo ha sido.', '2024-05-14 11:36:00'),
(10, NULL, 1, 'Me alegra saber eso.', '2024-05-14 11:37:00'),
(10, 'Endika', NULL, 'Gracias.', '2024-05-14 11:38:00'),
(10, NULL, 1, 'De nada.', '2024-05-14 11:39:00');

-- Conversación entre Academia1 y 'Almudena'
INSERT INTO Mensajes (cod_conversacion, sender_username, sender_cod_academia, contenido, timestamp) VALUES
(11, 'Almudena', NULL, 'Hola, ¿cómo estás?', '2024-05-14 11:40:00'),
(11, NULL, 1, 'Bien, ¿y tú?', '2024-05-14 11:41:00'),
(11, 'Almudena', NULL, 'Todo bien, gracias.', '2024-05-14 11:42:00'),
(11, NULL, 1, '¿Qué has hecho hoy?', '2024-05-14 11:43:00'),
(11, 'Almudena', NULL, 'He estado en una conferencia.', '2024-05-14 11:44:00'),
(11, NULL, 1, '¡Eso suena interesante!', '2024-05-14 11:45:00'),
(11, 'Almudena', NULL, 'Sí, ha sido muy informativa.', '2024-05-14 11:46:00'),
(11, NULL, 1, 'Me alegra saber eso.', '2024-05-14 11:47:00'),
(11, 'Almudena', NULL, 'Gracias.', '2024-05-14 11:48:00'),
(11, NULL, 1, 'De nada.', '2024-05-14 11:49:00');

-- Conversación entre Academia1 y 'Rosi'
INSERT INTO Mensajes (cod_conversacion, sender_username, sender_cod_academia, contenido, timestamp) VALUES
(12, 'Rosi', NULL, 'Hola, ¿cómo estás?', '2024-05-14 11:50:00'),
(12, NULL, 1, 'Bien, ¿y tú?', '2024-05-14 11:51:00'),
(12, 'Rosi', NULL, 'Todo bien, gracias.', '2024-05-14 11:52:00'),
(12, NULL, 1, '¿Qué has hecho hoy?', '2024-05-14 11:53:00'),
(12, 'Rosi', NULL, 'He estado trabajando en un proyecto.', '2024-05-14 11:54:00'),
(12, NULL, 1, '¡Eso suena interesante!', '2024-05-14 11:55:00'),
(12, 'Rosi', NULL, 'Sí, ha sido desafiante.', '2024-05-14 11:56:00'),
(12, NULL, 1, 'Me alegra saber eso.', '2024-05-14 11:57:00'),
(12, 'Rosi', NULL, 'Gracias.', '2024-05-14 11:58:00'),
(12, NULL, 1, 'De nada.', '2024-05-14 11:59:00');