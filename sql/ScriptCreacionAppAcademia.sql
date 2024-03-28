-- Nombre alumnos/as: Rubén Gómez García, Cristina López Lusarreta, Rubén Lopez-Davalillo Ledesma, Endika Eguino Garbayo

set global log_bin_trust_function_creators = 1;
drop database if exists xj5trgrj_appacademia;
create database xj5trgrj_appacademia;
use xj5trgrj_appacademia;

drop table if exists Usuarios;
create table Usuarios(
	usuario varchar(50) primary key,
    contrasena text not null,
    email varchar(100) unique not null,
    nombre varchar(20) not null,
    apellidos varchar(40) not null,
    telefono integer(15) not null,
    img_perfil blob,
    edad integer(3)
);

drop table if exists Academias;
create table Academias(
	cod_academia integer primary key auto_increment,
	usuario varchar(50) not null,
    contrasena text not null,
    email varchar(100) unique not null,
    nombre varchar(20) not null,
    telefono integer(15) not null,
    direccion text not null,
    img_perfil blob
);

drop table if exists Cursos;
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
    constraint academia_fk foreign key(cod_academia) references academias(cod_academia) on update cascade on delete cascade,
    constraint chk_Fechas check (fecha_fin_curso > fecha_inicio_curso)
);

drop table if exists Inscripciones;
create table Inscripciones(
	cod_curso integer not null,
    usuario varchar(50) not null,
    fecha_MiInicio_curso datetime not null,
    fecha_MiFin_curso datetime not null,
    constraint curso_fk foreign key(cod_curso) references cursos(cod_curso) on update cascade on delete cascade,
    constraint usuario_fk foreign key(usuario) references usuarios(usuario) on update cascade on delete cascade,
    constraint chk_Fechas_Inscripciones check (fecha_MiFin_curso > fecha_MiInicio_curso)
);

drop table if exists Favoritos;
create table Favoritos(
	cod_curso integer not null,
    usuario varchar(50) not null,
    constraint cursoFavoritos_fk foreign key(cod_curso) references cursos(cod_curso) on update cascade on delete cascade,
    constraint usuarioFavoritos_fk foreign key(usuario) references usuarios(usuario) on update cascade on delete cascade
);


insert into Usuarios(usuario,contrasena,email,nombre,apellidos,telefono) values ('Mikel', 'Mikel', 'mjorgesote@educacion.navarra.es', 'Mikel Aingeru', 'Jorge Soteras', 123456789);
insert into Usuarios(usuario,contrasena,email,nombre,apellidos,telefono) values ('LuisDorado', 'LuisDorado', 'ldoradogar@educacion.navarra.es', 'Luis', 'Dorado Garcés', 234567891);
insert into Usuarios(usuario,contrasena,email,nombre,apellidos,telefono) values ('Pablo', 'Pablo', 'pablomontero@educacion.navarra.es', 'Pablo', 'Montero Quevedo', 345678912);
insert into Usuarios(usuario,contrasena,email,nombre,apellidos,telefono) values ('Javi', 'Javi', 'jjorgesote@educacion.navarra.es', 'Javier', 'Jorge Soteras', 456789123);
insert into Usuarios(usuario,contrasena,email,nombre,apellidos,telefono) values ('David', 'David', 'dguruchazc@educacion.navarra.es', 'David', 'Gurutxarri Azcona', 567891234);
insert into Usuarios(usuario,contrasena,email,nombre,apellidos,telefono) values ('Pedro', 'Pedro', 'pvelasczuf@educacion.navarra.es', 'Pedro José', 'Velasco Zufia', 678912345);
insert into Usuarios(usuario,contrasena,email,nombre,apellidos,telefono) values ('RubenG', 'RubenG', 'rgomezgarc@educacion.navarra.es', 'Rubén', 'Gómez García', 789123456);
insert into Usuarios(usuario,contrasena,email,nombre,apellidos,telefono) values ('RubenL', 'RubenL', 'rlopezdled@educacion.navarra.es', 'Rubén', 'López-Davalillo Ledesma', 891234567);
insert into Usuarios(usuario,contrasena,email,nombre,apellidos,telefono) values ('Cris', 'Cris', 'clopezlusa1@educacion.navarra.es', 'Cristina', 'López Lusarreta', 912345678);
insert into Usuarios(usuario,contrasena,email,nombre,apellidos,telefono) values ('Endika', 'Endika', 'eeguinogar@educacion.navarra.es', 'Endika', 'Eguino Garbayo', 012345678);

INSERT INTO Academias (usuario, contrasena, email, nombre, telefono, direccion, img_perfil) VALUES ('academia1', 'password1', 'academia1@example.com', 'Academia 1', 123456789, 'Calle Academia 1', NULL);
INSERT INTO Academias (usuario, contrasena, email, nombre, telefono, direccion, img_perfil) VALUES ('academia2', 'password2', 'academia2@example.com', 'Academia 2', 234567890, 'Calle Academia 2', NULL);
INSERT INTO Academias (usuario, contrasena, email, nombre, telefono, direccion, img_perfil) VALUES ('academia3', 'password3', 'academia3@example.com', 'Academia 3', 345678901, 'Calle Academia 3', NULL);
INSERT INTO Academias (usuario, contrasena, email, nombre, telefono, direccion, img_perfil) VALUES ('academia4', 'password4', 'academia4@example.com', 'Academia 4', 456789012, 'Calle Academia 4', NULL);
INSERT INTO Academias (usuario, contrasena, email, nombre, telefono, direccion, img_perfil) VALUES ('academia5', 'password5', 'academia5@example.com', 'Academia 5', 567890123, 'Calle Academia 5', NULL);
INSERT INTO Academias (usuario, contrasena, email, nombre, telefono, direccion, img_perfil) VALUES ('academia6', 'password6', 'academia6@example.com', 'Academia 6', 678901234, 'Calle Academia 6', NULL);
INSERT INTO Academias (usuario, contrasena, email, nombre, telefono, direccion, img_perfil) VALUES ('academia7', 'password7', 'academia7@example.com', 'Academia 7', 789012345, 'Calle Academia 7', NULL);
INSERT INTO Academias (usuario, contrasena, email, nombre, telefono, direccion, img_perfil) VALUES ('academia8', 'password8', 'academia8@example.com', 'Academia 8', 890123456, 'Calle Academia 8', NULL);
INSERT INTO Academias (usuario, contrasena, email, nombre, telefono, direccion, img_perfil) VALUES ('academia9', 'password9', 'academia9@example.com', 'Academia 9', 901234567, 'Calle Academia 9', NULL);
INSERT INTO Academias (usuario, contrasena, email, nombre, telefono, direccion, img_perfil) VALUES ('academia10', 'password10', 'academia10@example.com', 'Academia 10', 123456780, 'Calle Academia 10', NULL);

INSERT INTO Cursos (nombre_curso, fecha_inicio_curso, fecha_fin_curso, precio, valoracion, descripcion, tipo, cod_academia)
VALUES ('Curso 1', '2024-03-01', '2024-04-01', 100.00, 4.5, 'Descripción del curso 1','Académico', 1);

INSERT INTO Cursos (nombre_curso, fecha_inicio_curso, fecha_fin_curso, precio, valoracion, descripcion, tipo, cod_academia)
VALUES ('Curso 2', '2024-03-15', '2024-04-15', 120.00, 4.0, 'Descripción del curso 2','Académico', 2);

INSERT INTO Cursos (nombre_curso, fecha_inicio_curso, fecha_fin_curso, precio, valoracion, descripcion, tipo, cod_academia)
VALUES ('Curso 3', '2024-04-01', '2024-05-01', 80.00, 4.2, 'Descripción del curso 3','Otros', 3);

INSERT INTO Cursos (nombre_curso, fecha_inicio_curso, fecha_fin_curso, precio, valoracion, descripcion, tipo, cod_academia)
VALUES ('Curso 4', '2024-04-15', '2024-05-15', 150.00, 4.8, 'Descripción del curso 4','Académico', 4);

INSERT INTO Cursos (nombre_curso, fecha_inicio_curso, fecha_fin_curso, precio, valoracion, descripcion, tipo, cod_academia)
VALUES ('Curso 5', '2024-05-01', '2024-06-01', 90.00, 4.6, 'Descripción del curso 5','Otros', 5);

INSERT INTO Cursos (nombre_curso, fecha_inicio_curso, fecha_fin_curso, precio, valoracion, descripcion, tipo, cod_academia)
VALUES ('Curso 6', '2024-05-15', '2024-06-15', 130.00, 4.3, 'Descripción del curso 6','Académico', 6);

INSERT INTO Cursos (nombre_curso, fecha_inicio_curso, fecha_fin_curso, precio, valoracion, descripcion, tipo, cod_academia)
VALUES ('Curso 7', '2024-06-01', '2024-07-01', 85.00, 4.7, 'Descripción del curso 7','Otros', 7);

INSERT INTO Cursos (nombre_curso, fecha_inicio_curso, fecha_fin_curso, precio, valoracion, descripcion, tipo, cod_academia)
VALUES ('Curso 8', '2024-06-15', '2024-07-15', 140.00, 4.1, 'Descripción del curso 8','Académico', 8);

INSERT INTO Cursos (nombre_curso, fecha_inicio_curso, fecha_fin_curso, precio, valoracion, descripcion, tipo, cod_academia)
VALUES ('Curso 9', '2024-07-01', '2024-08-01', 95.00, 4.9, 'Descripción del curso 9','Otros', 9);

INSERT INTO Cursos (nombre_curso, fecha_inicio_curso, fecha_fin_curso, precio, valoracion, descripcion, tipo, cod_academia)
VALUES ('Curso 10', '2024-07-15', '2024-08-15', 160.00, 4.4, 'Descripción del curso 10','Académico', 10);

INSERT INTO Inscripciones (cod_curso, usuario, fecha_MiInicio_curso, fecha_MiFin_curso) VALUES (1, 'Mikel', '2024-03-01', '2024-04-01');
INSERT INTO Inscripciones (cod_curso, usuario, fecha_MiInicio_curso, fecha_MiFin_curso) VALUES (2, 'LuisDorado', '2024-03-15', '2024-04-15');
INSERT INTO Inscripciones (cod_curso, usuario, fecha_MiInicio_curso, fecha_MiFin_curso) VALUES (3, 'Pablo', '2024-04-01', '2024-05-01');
INSERT INTO Inscripciones (cod_curso, usuario, fecha_MiInicio_curso, fecha_MiFin_curso) VALUES (4, 'Javi', '2024-04-15', '2024-05-15');
INSERT INTO Inscripciones (cod_curso, usuario, fecha_MiInicio_curso, fecha_MiFin_curso) VALUES (5, 'David', '2024-05-01', '2024-06-01');
INSERT INTO Inscripciones (cod_curso, usuario, fecha_MiInicio_curso, fecha_MiFin_curso) VALUES (6, 'Pedro', '2024-05-15', '2024-06-15');
INSERT INTO Inscripciones (cod_curso, usuario, fecha_MiInicio_curso, fecha_MiFin_curso) VALUES (7, 'RubenG', '2024-06-01', '2024-07-01');
INSERT INTO Inscripciones (cod_curso, usuario, fecha_MiInicio_curso, fecha_MiFin_curso) VALUES (8, 'RubenL', '2024-06-15', '2024-07-15');
INSERT INTO Inscripciones (cod_curso, usuario, fecha_MiInicio_curso, fecha_MiFin_curso) VALUES (9, 'Cris', '2024-07-01', '2024-08-01');
INSERT INTO Inscripciones (cod_curso, usuario, fecha_MiInicio_curso, fecha_MiFin_curso) VALUES (10, 'Endika', '2024-07-15', '2024-08-15');

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



