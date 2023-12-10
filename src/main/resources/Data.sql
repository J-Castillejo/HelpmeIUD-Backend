create database Helpmeiud;
use helpmeiud;

select * from usuarios u ;

select * from delitos d ;

select * from casos c ;

select * from roles r ;

select * from roles_usuario ru;

insert into roles(nombre, descripcion) 
values("ROLE_ADMIN", "Administrador");

insert into roles(nombre, descripcion) 
values("ROLE_USER", "Usuario Normal");