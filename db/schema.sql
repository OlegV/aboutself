create database testdb;

use testdb;

create table `users` (`email` VARCHAR(254) NOT NULL PRIMARY KEY,`first_name` VARCHAR(254) NOT NULL,`last_name` VARCHAR(254) NOT NULL,`city` VARCHAR(254) NOT NULL,`password` VARCHAR(254) NOT NULL);

delimiter $$
create trigger password_crypto before insert on users
for each row
  begin
    set new.password = CONCAT(md5(new.password),md5(new.email));
  end;
$$
delimiter ;