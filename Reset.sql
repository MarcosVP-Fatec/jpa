create user if not exists 'root'@'localhost' identified by 'root';
grant select, insert, delete, update on *.* to root@'localhost';