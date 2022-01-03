create schema if not exists cadastroclientes;
create schema if not exists cadastrousuarios;

create user if not exists 'root'@'localhost' identified by 'root';

grant select, insert, delete, update on cadastroclientes.* to root@'localhost';
grant select, insert, delete, update on cadastrousuarios.* to root@'localhost';