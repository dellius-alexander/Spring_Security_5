alter table user drop foreign key FKmjbs7hnl0lfdc8a1726o3gk42;
alter table user_roles drop foreign key FKqjne5iia9813e1dce27m6377i;
alter table user_roles drop foreign key FKo2j0svxgcf9yhw4j1iboj61yq;
drop table if exists role;
drop table if exists secret;
drop table if exists spring_security_5_db_sequence_generator;
drop table if exists user;
drop table if exists user_roles;
