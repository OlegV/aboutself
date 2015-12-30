# --- Created by Slick DDL
# To stop Slick DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table `users` (`email` VARCHAR(254) NOT NULL PRIMARY KEY,`first_name` VARCHAR(254) NOT NULL,`last_name` VARCHAR(254) NOT NULL,`city` VARCHAR(254) NOT NULL,`password` VARCHAR(254) NOT NULL);

# --- !Downs

drop table `users`;

